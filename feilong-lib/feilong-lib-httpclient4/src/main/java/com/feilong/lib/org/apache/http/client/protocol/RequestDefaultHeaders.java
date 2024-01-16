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

package com.feilong.lib.org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Collection;

import com.feilong.lib.org.apache.http.Header;
import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.HttpRequestInterceptor;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.protocol.HttpContext;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * Request interceptor that adds default request headers.
 *
 * @since 4.0
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class RequestDefaultHeaders implements HttpRequestInterceptor{

    private final Collection<? extends Header> defaultHeaders;

    /**
     * @since 4.3
     */
    public RequestDefaultHeaders(final Collection<? extends Header> defaultHeaders){
        super();
        this.defaultHeaders = defaultHeaders;
    }

    public RequestDefaultHeaders(){
        this(null);
    }

    @Override
    public void process(final HttpRequest request,final HttpContext context) throws HttpException,IOException{
        Args.notNull(request, "HTTP request");

        final String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT")){
            return;
        }

        // Add default headers
        Collection<? extends Header> defHeaders = null;
        //        (Collection<? extends Header>) request.getParams()
        //                        .getParameter(ClientPNames.DEFAULT_HEADERS);
        if (defHeaders == null){
            defHeaders = this.defaultHeaders;
        }

        if (defHeaders != null){
            for (final Header defHeader : defHeaders){
                request.addHeader(defHeader);
            }
        }
    }

}
