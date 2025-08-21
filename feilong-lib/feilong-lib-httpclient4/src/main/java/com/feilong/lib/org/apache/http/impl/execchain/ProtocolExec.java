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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpHost;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.ProtocolException;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.auth.AuthScope;
import com.feilong.lib.org.apache.http.auth.UsernamePasswordCredentials;
import com.feilong.lib.org.apache.http.client.CredentialsProvider;
import com.feilong.lib.org.apache.http.client.methods.CloseableHttpResponse;
import com.feilong.lib.org.apache.http.client.methods.HttpExecutionAware;
import com.feilong.lib.org.apache.http.client.methods.HttpRequestWrapper;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.lib.org.apache.http.client.protocol.HttpClientContext;
import com.feilong.lib.org.apache.http.client.utils.URIUtils;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.impl.client.BasicCredentialsProvider;
import com.feilong.lib.org.apache.http.protocol.HttpCoreContext;
import com.feilong.lib.org.apache.http.protocol.HttpProcessor;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * Request executor in the request execution chain that is responsible
 * for implementation of HTTP specification requirements.
 * Internally this executor relies on a {@link HttpProcessor} to populate
 * requisite HTTP request headers, process HTTP response headers and update
 * session state in {@link HttpClientContext}.
 * <p>
 * Further responsibilities such as communication with the opposite
 * endpoint is delegated to the next executor in the request execution
 * chain.
 * </p>
 *
 * @since 4.3
 */
@lombok.extern.slf4j.Slf4j
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
@SuppressWarnings("deprecation")
public class ProtocolExec implements ClientExecChain{

    private final ClientExecChain requestExecutor;

    private final HttpProcessor   httpProcessor;

    public ProtocolExec(final ClientExecChain requestExecutor, final HttpProcessor httpProcessor){
        Args.notNull(requestExecutor, "HTTP client request executor");
        Args.notNull(httpProcessor, "HTTP protocol processor");
        this.requestExecutor = requestExecutor;
        this.httpProcessor = httpProcessor;
    }

    void rewriteRequestURI(final HttpRequestWrapper request,final HttpRoute route,final boolean normalizeUri) throws ProtocolException{
        final URI uri = request.getURI();
        if (uri != null){
            try{
                request.setURI(URIUtils.rewriteURIForRoute(uri, route, normalizeUri));
            }catch (final URISyntaxException ex){
                throw new ProtocolException("Invalid URI: " + uri, ex);
            }
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

        final HttpRequest original = request.getOriginal();
        URI uri = null;
        if (original instanceof HttpUriRequest){
            uri = ((HttpUriRequest) original).getURI();
        }else{
            final String uriString = original.getRequestLine().getUri();
            try{
                uri = URI.create(uriString);
            }catch (final IllegalArgumentException ex){
                if (log.isDebugEnabled()){
                    log.debug(
                                    "Unable to parse '" + uriString + "' as a valid URI; "
                                                    + "request URI and Host header may be inconsistent",
                                    ex);
                }
            }

        }
        request.setURI(uri);

        // Re-write request URI if needed
        rewriteRequestURI(request, route, context.getRequestConfig().isNormalizeUri());

        //        final HttpParams params = request.getParams();
        HttpHost virtualHost = null;

        //        HttpHost virtualHost = (HttpHost) params.getParameter(ClientPNames.VIRTUAL_HOST);
        // HTTPCLIENT-1092 - add the port if necessary
        if (virtualHost != null && virtualHost.getPort() == -1){
            final int port = route.getTargetHost().getPort();
            if (port != -1){
                virtualHost = new HttpHost(virtualHost.getHostName(), port, virtualHost.getSchemeName());
            }
            if (log.isDebugEnabled()){
                log.debug("Using virtual host" + virtualHost);
            }
        }

        HttpHost target = null;
        if (virtualHost != null){
            target = virtualHost;
        }else{
            if (uri != null && uri.isAbsolute() && uri.getHost() != null){
                target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
            }
        }
        if (target == null){
            target = request.getTarget();
        }
        if (target == null){
            target = route.getTargetHost();
        }

        // Get user info from the URI
        if (uri != null){
            final String userinfo = uri.getUserInfo();
            if (userinfo != null){
                CredentialsProvider credsProvider = context.getCredentialsProvider();
                if (credsProvider == null){
                    credsProvider = new BasicCredentialsProvider();
                    context.setCredentialsProvider(credsProvider);
                }
                credsProvider.setCredentials(new AuthScope(target), new UsernamePasswordCredentials(userinfo));
            }
        }

        // Run request protocol interceptors
        context.setAttribute(HttpCoreContext.HTTP_TARGET_HOST, target);
        context.setAttribute(HttpClientContext.HTTP_ROUTE, route);
        context.setAttribute(HttpCoreContext.HTTP_REQUEST, request);

        this.httpProcessor.process(request, context);

        final CloseableHttpResponse response = this.requestExecutor.execute(route, request, context, execAware);
        try{
            // Run response protocol interceptors
            context.setAttribute(HttpCoreContext.HTTP_RESPONSE, response);
            this.httpProcessor.process(response, context);
            return response;
        }catch (final RuntimeException ex){
            response.close();
            throw ex;
        }catch (final IOException ex){
            response.close();
            throw ex;
        }catch (final HttpException ex){
            response.close();
            throw ex;
        }
    }

}
