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

package com.feilong.lib.org.apache.http.impl.execchain;

import static com.feilong.lib.org.apache.http.client.utils.URIUtils.DROP_FRAGMENT;
import static com.feilong.lib.org.apache.http.client.utils.URIUtils.DROP_FRAGMENT_AND_NORMALIZE;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.feilong.lib.org.apache.http.ConnectionReuseStrategy;
import com.feilong.lib.org.apache.http.HttpClientConnection;
import com.feilong.lib.org.apache.http.HttpEntity;
import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpHost;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.ProtocolException;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.client.config.RequestConfig;
import com.feilong.lib.org.apache.http.client.methods.CloseableHttpResponse;
import com.feilong.lib.org.apache.http.client.methods.HttpExecutionAware;
import com.feilong.lib.org.apache.http.client.methods.HttpRequestWrapper;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.lib.org.apache.http.client.protocol.HttpClientContext;
import com.feilong.lib.org.apache.http.client.protocol.RequestClientConnControl;
import com.feilong.lib.org.apache.http.client.utils.URIUtils;
import com.feilong.lib.org.apache.http.conn.ConnectionKeepAliveStrategy;
import com.feilong.lib.org.apache.http.conn.ConnectionRequest;
import com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.impl.conn.ConnectionShutdownException;
import com.feilong.lib.org.apache.http.protocol.HttpCoreContext;
import com.feilong.lib.org.apache.http.protocol.HttpProcessor;
import com.feilong.lib.org.apache.http.protocol.HttpRequestExecutor;
import com.feilong.lib.org.apache.http.protocol.ImmutableHttpProcessor;
import com.feilong.lib.org.apache.http.protocol.RequestContent;
import com.feilong.lib.org.apache.http.protocol.RequestTargetHost;
import com.feilong.lib.org.apache.http.protocol.RequestUserAgent;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * Request executor that implements the most fundamental aspects of
 * the HTTP specification and the most straight-forward request / response
 * exchange with the target server. This executor does not support
 * execution via proxy and will make no attempts to retry the request
 * in case of a redirect, authentication challenge or I/O error.
 *
 * @since 4.3
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class MinimalClientExec implements ClientExecChain{

    /**
     * 伪造的 useragent.
     * 
     * @since 1.5.0
     */
    public static final String                DEFAULT_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    private final HttpRequestExecutor         requestExecutor;

    private final HttpClientConnectionManager connManager;

    private final ConnectionReuseStrategy     reuseStrategy;

    private final ConnectionKeepAliveStrategy keepAliveStrategy;

    private final HttpProcessor               httpProcessor;

    public MinimalClientExec(final HttpRequestExecutor requestExecutor, final HttpClientConnectionManager connManager,
                    final ConnectionReuseStrategy reuseStrategy, final ConnectionKeepAliveStrategy keepAliveStrategy){
        Args.notNull(requestExecutor, "HTTP request executor");
        Args.notNull(connManager, "Client connection manager");
        Args.notNull(reuseStrategy, "Connection reuse strategy");
        Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
        this.httpProcessor = new ImmutableHttpProcessor(
                        new RequestContent(),
                        new RequestTargetHost(),
                        new RequestClientConnControl(),
                        new RequestUserAgent(DEFAULT_USER_AGENT));
        this.requestExecutor = requestExecutor;
        this.connManager = connManager;
        this.reuseStrategy = reuseStrategy;
        this.keepAliveStrategy = keepAliveStrategy;
    }

    static void rewriteRequestURI(final HttpRequestWrapper request,final HttpRoute route,final boolean normalizeUri)
                    throws ProtocolException{
        try{
            URI uri = request.getURI();
            if (uri != null){
                // Make sure the request URI is relative
                if (uri.isAbsolute()){
                    uri = URIUtils.rewriteURI(uri, null, normalizeUri ? DROP_FRAGMENT_AND_NORMALIZE : DROP_FRAGMENT);
                }else{
                    uri = URIUtils.rewriteURI(uri);
                }
                request.setURI(uri);
            }
        }catch (final URISyntaxException ex){
            throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
        }
    }

    @Override
    public CloseableHttpResponse execute(
                    final HttpRoute route,
                    final HttpRequestWrapper request,
                    final HttpClientContext context,
                    final HttpExecutionAware execAware) throws IOException,HttpException{
        Args.notNull(route, "HTTP route");
        Args.notNull(request, "HTTP request");
        Args.notNull(context, "HTTP context");

        rewriteRequestURI(request, route, context.getRequestConfig().isNormalizeUri());

        final ConnectionRequest connRequest = connManager.requestConnection(route, null);
        if (execAware != null){
            if (execAware.isAborted()){
                connRequest.cancel();
                throw new RequestAbortedException("Request aborted");
            }
            execAware.setCancellable(connRequest);
        }

        final RequestConfig config = context.getRequestConfig();

        final HttpClientConnection managedConn;
        try{
            final int timeout = config.getConnectionRequestTimeout();
            managedConn = connRequest.get(timeout > 0 ? timeout : 0, TimeUnit.MILLISECONDS);
        }catch (final InterruptedException interrupted){
            Thread.currentThread().interrupt();
            throw new RequestAbortedException("Request aborted", interrupted);
        }catch (final ExecutionException ex){
            Throwable cause = ex.getCause();
            if (cause == null){
                cause = ex;
            }
            throw new RequestAbortedException("Request execution failed", cause);
        }

        final ConnectionHolder releaseTrigger = new ConnectionHolder(connManager, managedConn);
        try{
            if (execAware != null){
                if (execAware.isAborted()){
                    releaseTrigger.close();
                    throw new RequestAbortedException("Request aborted");
                }
                execAware.setCancellable(releaseTrigger);
            }

            if (!managedConn.isOpen()){
                final int timeout = config.getConnectTimeout();
                this.connManager.connect(managedConn, route, timeout > 0 ? timeout : 0, context);
                this.connManager.routeComplete(managedConn, route, context);
            }
            final int timeout = config.getSocketTimeout();
            if (timeout >= 0){
                managedConn.setSocketTimeout(timeout);
            }

            HttpHost target = null;
            final HttpRequest original = request.getOriginal();
            if (original instanceof HttpUriRequest){
                final URI uri = ((HttpUriRequest) original).getURI();
                if (uri.isAbsolute()){
                    target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
                }
            }
            if (target == null){
                target = route.getTargetHost();
            }

            context.setAttribute(HttpCoreContext.HTTP_TARGET_HOST, target);
            context.setAttribute(HttpCoreContext.HTTP_REQUEST, request);
            context.setAttribute(HttpCoreContext.HTTP_CONNECTION, managedConn);
            context.setAttribute(HttpClientContext.HTTP_ROUTE, route);

            httpProcessor.process(request, context);
            final HttpResponse response = requestExecutor.execute(request, managedConn, context);
            httpProcessor.process(response, context);

            // The connection is in or can be brought to a re-usable state.
            if (reuseStrategy.keepAlive(response, context)){
                // Set the idle duration of this connection
                final long duration = keepAliveStrategy.getKeepAliveDuration(response, context);
                releaseTrigger.setValidFor(duration, TimeUnit.MILLISECONDS);
                releaseTrigger.markReusable();
            }else{
                releaseTrigger.markNonReusable();
            }

            // check for entity, release connection if possible
            final HttpEntity entity = response.getEntity();
            if (entity == null || !entity.isStreaming()){
                // connection not needed and (assumed to be) in re-usable state
                releaseTrigger.releaseConnection();
                return new HttpResponseProxy(response, null);
            }
            return new HttpResponseProxy(response, releaseTrigger);
        }catch (final ConnectionShutdownException ex){
            final InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
            ioex.initCause(ex);
            throw ioex;
        }catch (final HttpException ex){
            releaseTrigger.abortConnection();
            throw ex;
        }catch (final IOException ex){
            releaseTrigger.abortConnection();
            throw ex;
        }catch (final RuntimeException ex){
            releaseTrigger.abortConnection();
            throw ex;
        }catch (final Error error){
            connManager.shutdown();
            throw error;
        }
    }

}
