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

package com.feilong.lib.org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpHost;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.auth.AuthSchemeProvider;
import com.feilong.lib.org.apache.http.auth.AuthState;
import com.feilong.lib.org.apache.http.client.ClientProtocolException;
import com.feilong.lib.org.apache.http.client.CookieStore;
import com.feilong.lib.org.apache.http.client.CredentialsProvider;
import com.feilong.lib.org.apache.http.client.config.RequestConfig;
import com.feilong.lib.org.apache.http.client.methods.CloseableHttpResponse;
import com.feilong.lib.org.apache.http.client.methods.Configurable;
import com.feilong.lib.org.apache.http.client.methods.HttpExecutionAware;
import com.feilong.lib.org.apache.http.client.methods.HttpRequestWrapper;
import com.feilong.lib.org.apache.http.client.protocol.HttpClientContext;
import com.feilong.lib.org.apache.http.config.Lookup;
import com.feilong.lib.org.apache.http.conn.HttpClientConnectionManager;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoutePlanner;
import com.feilong.lib.org.apache.http.cookie.CookieSpecProvider;
import com.feilong.lib.org.apache.http.impl.execchain.ClientExecChain;
import com.feilong.lib.org.apache.http.protocol.BasicHttpContext;
import com.feilong.lib.org.apache.http.protocol.HttpContext;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * Internal class.
 *
 * @since 4.3
 */
@lombok.extern.slf4j.Slf4j
@Contract(threading = ThreadingBehavior.SAFE_CONDITIONAL)
class InternalHttpClient extends CloseableHttpClient implements Configurable{

    private final ClientExecChain             execChain;

    private final HttpClientConnectionManager connManager;

    private final HttpRoutePlanner            routePlanner;

    private final Lookup<CookieSpecProvider>  cookieSpecRegistry;

    private final Lookup<AuthSchemeProvider>  authSchemeRegistry;

    private final CookieStore                 cookieStore;

    private final CredentialsProvider         credentialsProvider;

    private final RequestConfig               defaultConfig;

    private final List<Closeable>             closeables;

    public InternalHttpClient(final ClientExecChain execChain, final HttpClientConnectionManager connManager,
                    final HttpRoutePlanner routePlanner, final Lookup<CookieSpecProvider> cookieSpecRegistry,
                    final Lookup<AuthSchemeProvider> authSchemeRegistry, final CookieStore cookieStore,
                    final CredentialsProvider credentialsProvider, final RequestConfig defaultConfig, final List<Closeable> closeables){
        super();
        Args.notNull(execChain, "HTTP client exec chain");
        Args.notNull(connManager, "HTTP connection manager");
        Args.notNull(routePlanner, "HTTP route planner");
        this.execChain = execChain;
        this.connManager = connManager;
        this.routePlanner = routePlanner;
        this.cookieSpecRegistry = cookieSpecRegistry;
        this.authSchemeRegistry = authSchemeRegistry;
        this.cookieStore = cookieStore;
        this.credentialsProvider = credentialsProvider;
        this.defaultConfig = defaultConfig;
        this.closeables = closeables;
    }

    private HttpRoute determineRoute(final HttpHost target,final HttpRequest request,final HttpContext context) throws HttpException{
        HttpHost host = target;
        //        if (host == null){
        //            host = (HttpHost) request.getParams().getParameter(ClientPNames.DEFAULT_HOST);
        //        }
        return this.routePlanner.determineRoute(host, request, context);
    }

    private void setupContext(final HttpClientContext context){
        if (context.getAttribute(HttpClientContext.TARGET_AUTH_STATE) == null){
            context.setAttribute(HttpClientContext.TARGET_AUTH_STATE, new AuthState());
        }
        if (context.getAttribute(HttpClientContext.PROXY_AUTH_STATE) == null){
            context.setAttribute(HttpClientContext.PROXY_AUTH_STATE, new AuthState());
        }
        if (context.getAttribute(HttpClientContext.AUTHSCHEME_REGISTRY) == null){
            context.setAttribute(HttpClientContext.AUTHSCHEME_REGISTRY, this.authSchemeRegistry);
        }
        if (context.getAttribute(HttpClientContext.COOKIESPEC_REGISTRY) == null){
            context.setAttribute(HttpClientContext.COOKIESPEC_REGISTRY, this.cookieSpecRegistry);
        }
        if (context.getAttribute(HttpClientContext.COOKIE_STORE) == null){
            context.setAttribute(HttpClientContext.COOKIE_STORE, this.cookieStore);
        }
        if (context.getAttribute(HttpClientContext.CREDS_PROVIDER) == null){
            context.setAttribute(HttpClientContext.CREDS_PROVIDER, this.credentialsProvider);
        }
        if (context.getAttribute(HttpClientContext.REQUEST_CONFIG) == null){
            context.setAttribute(HttpClientContext.REQUEST_CONFIG, this.defaultConfig);
        }
    }

    @Override
    protected CloseableHttpResponse doExecute(final HttpHost target,final HttpRequest request,final HttpContext context)
                    throws IOException,ClientProtocolException{
        Args.notNull(request, "HTTP request");
        HttpExecutionAware execAware = null;
        if (request instanceof HttpExecutionAware){
            execAware = (HttpExecutionAware) request;
        }
        try{
            final HttpRequestWrapper wrapper = HttpRequestWrapper.wrap(request, target);
            final HttpClientContext localcontext = HttpClientContext.adapt(context != null ? context : new BasicHttpContext());
            RequestConfig config = null;
            if (request instanceof Configurable){
                config = ((Configurable) request).getConfig();
            }
            //            if (config == null){
            //                final HttpParams params = request.getParams();
            //                if (params instanceof HttpParamsNames){
            //                    if (!((HttpParamsNames) params).getNames().isEmpty()){
            //                        config = HttpClientParamConfig.getRequestConfig(params, this.defaultConfig);
            //                    }
            //                }else{
            //                    config = HttpClientParamConfig.getRequestConfig(params, this.defaultConfig);
            //                }
            //            }
            if (config != null){
                localcontext.setRequestConfig(config);
            }
            setupContext(localcontext);
            final HttpRoute route = determineRoute(target, wrapper, localcontext);
            return this.execChain.execute(route, wrapper, localcontext, execAware);
        }catch (final HttpException httpException){
            throw new ClientProtocolException(httpException);
        }
    }

    @Override
    public RequestConfig getConfig(){
        return this.defaultConfig;
    }

    @Override
    public void close(){
        if (this.closeables != null){
            for (final Closeable closeable : this.closeables){
                try{
                    closeable.close();
                }catch (final IOException ex){
                    log.error(ex.getMessage(), ex);
                }
            }
        }
    }
    //
    //    @Override
    //    public HttpParams getParams(){
    //        throw new UnsupportedOperationException();
    //    }

    //    @Override
    //    public ClientConnectionManager getConnectionManager(){
    //
    //        return new ClientConnectionManager(){
    //
    //            @Override
    //            public void shutdown(){
    //                connManager.shutdown();
    //            }
    //
    //            @Override
    //            public ClientConnectionRequest requestConnection(final HttpRoute route,final Object state){
    //                throw new UnsupportedOperationException();
    //            }
    //
    //            @Override
    //            public void releaseConnection(final ManagedClientConnection conn,final long validDuration,final TimeUnit timeUnit){
    //                throw new UnsupportedOperationException();
    //            }
    //
    //            @Override
    //            public SchemeRegistry getSchemeRegistry(){
    //                throw new UnsupportedOperationException();
    //            }
    //
    //            @Override
    //            public void closeIdleConnections(final long idletime,final TimeUnit timeUnit){
    //                connManager.closeIdleConnections(idletime, timeUnit);
    //            }
    //
    //            @Override
    //            public void closeExpiredConnections(){
    //                connManager.closeExpiredConnections();
    //            }
    //
    //        };
    //
    //    }

}
