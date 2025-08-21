/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.feilong.lib.org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.feilong.lib.org.apache.http.HttpClientConnection;
import com.feilong.lib.org.apache.http.HttpHost;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.config.ConnectionConfig;
import com.feilong.lib.org.apache.http.config.Lookup;
import com.feilong.lib.org.apache.http.config.Registry;
import com.feilong.lib.org.apache.http.config.RegistryBuilder;
import com.feilong.lib.org.apache.http.config.SocketConfig;
import com.feilong.lib.org.apache.http.conn.ConnectionRequest;
import com.feilong.lib.org.apache.http.conn.DnsResolver;
import com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager;
import com.feilong.lib.org.apache.http.conn.HttpClientConnectionOperator;
import com.feilong.lib.org.apache.http.conn.HttpConnectionFactory;
import com.feilong.lib.org.apache.http.conn.ManagedHttpClientConnection;
import com.feilong.lib.org.apache.http.conn.SchemePortResolver;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.conn.socket.ConnectionSocketFactory;
import com.feilong.lib.org.apache.http.conn.socket.PlainConnectionSocketFactory;
import com.feilong.lib.org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import com.feilong.lib.org.apache.http.protocol.HttpContext;
import com.feilong.lib.org.apache.http.util.Args;
import com.feilong.lib.org.apache.http.util.Asserts;
import com.feilong.lib.org.apache.http.util.LangUtils;

/**
 * A connection manager for a single connection. This connection manager maintains only one active
 * connection. Even though this class is fully thread-safe it ought to be used by one execution
 * thread only, as only one thread a time can lease the connection at a time.
 * <p>
 * This connection manager will make an effort to reuse the connection for subsequent requests
 * with the same {@link HttpRoute route}. It will, however, close the existing connection and
 * open it for the given route, if the route of the persistent connection does not match that
 * of the connection request. If the connection has been already been allocated
 * {@link IllegalStateException} is thrown.
 * </p>
 * <p>
 * This connection manager implementation should be used inside an EJB container instead of
 * {@link PoolingHttpClientConnectionManager}.
 * </p>
 *
 * @since 4.3
 */
@lombok.extern.slf4j.Slf4j
@Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
public class BasicHttpClientConnectionManager implements HttpClientConnectionManager,Closeable{

    private final HttpClientConnectionOperator                                  connectionOperator;

    private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;

    private ManagedHttpClientConnection                                         conn;

    private HttpRoute                                                           route;

    private Object                                                              state;

    private long                                                                updated;

    private long                                                                expiry;

    private boolean                                                             leased;

    private SocketConfig                                                        socketConfig;

    private ConnectionConfig                                                    connConfig;

    private final AtomicBoolean                                                 isShutdown;

    private static Registry<ConnectionSocketFactory> getDefaultRegistry(){
        return RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.getSocketFactory())
                        .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
    }

    public BasicHttpClientConnectionManager(final Lookup<ConnectionSocketFactory> socketFactoryRegistry,
                    final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
                    final SchemePortResolver schemePortResolver, final DnsResolver dnsResolver){
        this(new DefaultHttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver), connFactory);
    }

    /**
     * @since 4.4
     */
    public BasicHttpClientConnectionManager(final HttpClientConnectionOperator httpClientConnectionOperator,
                    final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory){
        super();
        this.connectionOperator = Args.notNull(httpClientConnectionOperator, "Connection operator");
        this.connFactory = connFactory != null ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE;
        this.expiry = Long.MAX_VALUE;
        this.socketConfig = SocketConfig.DEFAULT;
        this.connConfig = ConnectionConfig.DEFAULT;
        this.isShutdown = new AtomicBoolean(false);
    }

    public BasicHttpClientConnectionManager(final Lookup<ConnectionSocketFactory> socketFactoryRegistry,
                    final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory){
        this(socketFactoryRegistry, connFactory, null, null);
    }

    public BasicHttpClientConnectionManager(final Lookup<ConnectionSocketFactory> socketFactoryRegistry){
        this(socketFactoryRegistry, null, null, null);
    }

    public BasicHttpClientConnectionManager(){
        this(getDefaultRegistry(), null, null, null);
    }

    @Override
    protected void finalize() throws Throwable{
        try{
            shutdown();
        }finally{ // Make sure we call overridden method even if shutdown barfs
            super.finalize();
        }
    }

    @Override
    public void close(){
        if (this.isShutdown.compareAndSet(false, true)){
            closeConnection();
        }
    }

    HttpRoute getRoute(){
        return route;
    }

    Object getState(){
        return state;
    }

    public synchronized SocketConfig getSocketConfig(){
        return socketConfig;
    }

    public synchronized void setSocketConfig(final SocketConfig socketConfig){
        this.socketConfig = socketConfig != null ? socketConfig : SocketConfig.DEFAULT;
    }

    public synchronized ConnectionConfig getConnectionConfig(){
        return connConfig;
    }

    public synchronized void setConnectionConfig(final ConnectionConfig connConfig){
        this.connConfig = connConfig != null ? connConfig : ConnectionConfig.DEFAULT;
    }

    @Override
    public final ConnectionRequest requestConnection(final HttpRoute route,final Object state){
        Args.notNull(route, "Route");
        return new ConnectionRequest(){

            @Override
            public boolean cancel(){
                // Nothing to abort, since requests are immediate.
                return false;
            }

            @Override
            public HttpClientConnection get(final long timeout,final TimeUnit timeUnit){
                return BasicHttpClientConnectionManager.this.getConnection(route, state);
            }

        };
    }

    private synchronized void closeConnection(){
        if (this.conn != null){
            log.debug("Closing connection");
            try{
                this.conn.close();
            }catch (final IOException iox){
                if (log.isDebugEnabled()){
                    log.debug("I/O exception closing connection", iox);
                }
            }
            this.conn = null;
        }
    }

    private void checkExpiry(){
        if (this.conn != null && System.currentTimeMillis() >= this.expiry){
            if (log.isDebugEnabled()){
                log.debug("Connection expired @ " + new Date(this.expiry));
            }
            closeConnection();
        }
    }

    synchronized HttpClientConnection getConnection(final HttpRoute route,final Object state){
        Asserts.check(!this.isShutdown.get(), "Connection manager has been shut down");
        if (log.isDebugEnabled()){
            log.debug("Get connection for route " + route);
        }
        Asserts.check(!this.leased, "Connection is still allocated");
        if (!LangUtils.equals(this.route, route) || !LangUtils.equals(this.state, state)){
            closeConnection();
        }
        this.route = route;
        this.state = state;
        checkExpiry();
        if (this.conn == null){
            this.conn = this.connFactory.create(route, this.connConfig);
        }
        this.conn.setSocketTimeout(this.socketConfig.getSoTimeout());
        this.leased = true;
        return this.conn;
    }

    @Override
    public synchronized void releaseConnection(
                    final HttpClientConnection conn,
                    final Object state,
                    final long keepalive,
                    final TimeUnit timeUnit){
        Args.notNull(conn, "Connection");
        Asserts.check(conn == this.conn, "Connection not obtained from this manager");
        if (log.isDebugEnabled()){
            log.debug("Releasing connection " + conn);
        }
        if (this.isShutdown.get()){
            return;
        }
        try{
            this.updated = System.currentTimeMillis();
            if (!this.conn.isOpen()){
                this.conn = null;
                this.route = null;
                this.conn = null;
                this.expiry = Long.MAX_VALUE;
            }else{
                this.state = state;
                this.conn.setSocketTimeout(0);
                if (log.isDebugEnabled()){
                    final String s;
                    if (keepalive > 0){
                        s = "for " + keepalive + " " + timeUnit;
                    }else{
                        s = "indefinitely";
                    }
                    log.debug("Connection can be kept alive " + s);
                }
                if (keepalive > 0){
                    this.expiry = this.updated + timeUnit.toMillis(keepalive);
                }else{
                    this.expiry = Long.MAX_VALUE;
                }
            }
        }finally{
            this.leased = false;
        }
    }

    @Override
    public void connect(final HttpClientConnection conn,final HttpRoute route,final int connectTimeout,final HttpContext context)
                    throws IOException{
        Args.notNull(conn, "Connection");
        Args.notNull(route, "HTTP route");
        Asserts.check(conn == this.conn, "Connection not obtained from this manager");
        final HttpHost host;
        if (route.getProxyHost() != null){
            host = route.getProxyHost();
        }else{
            host = route.getTargetHost();
        }
        final InetSocketAddress localAddress = route.getLocalSocketAddress();
        this.connectionOperator.connect(this.conn, host, localAddress, connectTimeout, this.socketConfig, context);
    }

    @Override
    public void upgrade(final HttpClientConnection conn,final HttpRoute route,final HttpContext context) throws IOException{
        Args.notNull(conn, "Connection");
        Args.notNull(route, "HTTP route");
        Asserts.check(conn == this.conn, "Connection not obtained from this manager");
        this.connectionOperator.upgrade(this.conn, route.getTargetHost(), context);
    }

    @Override
    public void routeComplete(final HttpClientConnection conn,final HttpRoute route,final HttpContext context) throws IOException{
    }

    @Override
    public synchronized void closeExpiredConnections(){
        if (this.isShutdown.get()){
            return;
        }
        if (!this.leased){
            checkExpiry();
        }
    }

    @Override
    public synchronized void closeIdleConnections(final long idletime,final TimeUnit timeUnit){
        Args.notNull(timeUnit, "Time unit");
        if (this.isShutdown.get()){
            return;
        }
        if (!this.leased){
            long time = timeUnit.toMillis(idletime);
            if (time < 0){
                time = 0;
            }
            final long deadline = System.currentTimeMillis() - time;
            if (this.updated <= deadline){
                closeConnection();
            }
        }
    }

    @Override
    public void shutdown(){
        if (this.isShutdown.compareAndSet(false, true)){
            if (this.conn != null){
                log.debug("Shutting down connection");
                try{
                    this.conn.shutdown();
                }catch (final IOException iox){
                    if (log.isDebugEnabled()){
                        log.debug("I/O exception shutting down connection", iox);
                    }
                }
                this.conn = null;
            }
        }
    }

}
