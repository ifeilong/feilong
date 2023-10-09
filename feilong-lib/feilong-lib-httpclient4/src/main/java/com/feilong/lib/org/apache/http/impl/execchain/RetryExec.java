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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.org.apache.http.Header;
import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.NoHttpResponseException;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.client.HttpRequestRetryHandler;
import com.feilong.lib.org.apache.http.client.NonRepeatableRequestException;
import com.feilong.lib.org.apache.http.client.methods.CloseableHttpResponse;
import com.feilong.lib.org.apache.http.client.methods.HttpExecutionAware;
import com.feilong.lib.org.apache.http.client.methods.HttpRequestWrapper;
import com.feilong.lib.org.apache.http.client.protocol.HttpClientContext;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * Request executor in the request execution chain that is responsible
 * for making a decision whether a request failed due to an I/O error
 * should be re-executed.
 * <p>
 * Further responsibilities such as communication with the opposite
 * endpoint is delegated to the next executor in the request execution
 * chain.
 * </p>
 *
 * @since 4.3
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class RetryExec implements ClientExecChain{

    /** The Constant log. */
    private static final Logger           log = LoggerFactory.getLogger(RetryExec.class);

    //---------------------------------------------------------------

    private final ClientExecChain         requestExecutor;

    private final HttpRequestRetryHandler retryHandler;

    public RetryExec(final ClientExecChain requestExecutor, final HttpRequestRetryHandler retryHandler){
        Args.notNull(requestExecutor, "HTTP request executor");
        Args.notNull(retryHandler, "HTTP request retry handler");
        this.requestExecutor = requestExecutor;
        this.retryHandler = retryHandler;
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

        final Header[] origheaders = request.getAllHeaders();
        for (int execCount = 1;; execCount++){
            try{
                return this.requestExecutor.execute(route, request, context, execAware);
            }catch (final IOException ex){
                if (execAware != null && execAware.isAborted()){
                    log.debug("Request has been aborted");
                    throw ex;
                }

                //---------------------------------------------------------------
                log.info(
                                "execCount:[{}],IOException ( {} ) caught when processing request to {}: {},will use {} determines if need retry",
                                execCount,
                                ex.getClass().getName(),
                                route,
                                ex.getMessage(),
                                retryHandler.getClass().getName());

                if (retryHandler.retryRequest(ex, execCount, context)){
                    if (log.isDebugEnabled()){
                        log.debug(ex.getMessage(), ex);
                    }

                    if (log.isInfoEnabled()){
                        log.info(
                                        "execCount:[{}],IOException ( {} ) caught when processing request to {}: {},retryHandler :[{}] need retry",
                                        execCount,
                                        ex.getClass().getName(),
                                        route,
                                        ex.getMessage(),
                                        retryHandler.getClass().getName());
                    }

                    //---------------------------------------------------------------
                    if (!RequestEntityProxy.isRepeatable(request)){
                        log.debug("Cannot retry non-repeatable request");
                        throw new NonRepeatableRequestException("Cannot retry request " + "with a non-repeatable request entity", ex);
                    }
                    request.setHeaders(origheaders);

                    log.info("Retrying request to {}", route);
                }else{
                    if (ex instanceof NoHttpResponseException){
                        final NoHttpResponseException updatedex = new NoHttpResponseException(
                                        route.getTargetHost().toHostString() + " failed to respond");
                        updatedex.setStackTrace(ex.getStackTrace());
                        throw updatedex;
                    }
                    throw ex;
                }
            }
        }
    }

}
