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
package com.feilong.lib.org.apache.http.protocol;

import java.io.IOException;
import java.util.List;

import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.HttpRequestInterceptor;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.HttpResponseInterceptor;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;

/**
 * Immutable {@link HttpProcessor}.
 *
 * @since 4.1
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public final class ImmutableHttpProcessor implements HttpProcessor{

    private final HttpRequestInterceptor[]  requestInterceptors;

    private final HttpResponseInterceptor[] responseInterceptors;

    public ImmutableHttpProcessor(final HttpRequestInterceptor[] requestInterceptors, final HttpResponseInterceptor[] responseInterceptors){
        super();
        if (requestInterceptors != null){
            final int l = requestInterceptors.length;
            this.requestInterceptors = new HttpRequestInterceptor[l];
            System.arraycopy(requestInterceptors, 0, this.requestInterceptors, 0, l);
        }else{
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors != null){
            final int l = responseInterceptors.length;
            this.responseInterceptors = new HttpResponseInterceptor[l];
            System.arraycopy(responseInterceptors, 0, this.responseInterceptors, 0, l);
        }else{
            this.responseInterceptors = new HttpResponseInterceptor[0];
        }
    }

    /**
     * @since 4.3
     */
    public ImmutableHttpProcessor(final List<HttpRequestInterceptor> requestInterceptors,
                    final List<HttpResponseInterceptor> responseInterceptors){
        super();
        if (requestInterceptors != null){
            final int l = requestInterceptors.size();
            this.requestInterceptors = requestInterceptors.toArray(new HttpRequestInterceptor[l]);
        }else{
            this.requestInterceptors = new HttpRequestInterceptor[0];
        }
        if (responseInterceptors != null){
            final int l = responseInterceptors.size();
            this.responseInterceptors = responseInterceptors.toArray(new HttpResponseInterceptor[l]);
        }else{
            this.responseInterceptors = new HttpResponseInterceptor[0];
        }
    }

    public ImmutableHttpProcessor(final HttpRequestInterceptor...requestInterceptors){
        this(requestInterceptors, null);
    }

    public ImmutableHttpProcessor(final HttpResponseInterceptor...responseInterceptors){
        this(null, responseInterceptors);
    }

    @Override
    public void process(final HttpRequest request,final HttpContext context) throws IOException,HttpException{
        for (final HttpRequestInterceptor requestInterceptor : this.requestInterceptors){
            requestInterceptor.process(request, context);
        }
    }

    @Override
    public void process(final HttpResponse response,final HttpContext context) throws IOException,HttpException{
        for (final HttpResponseInterceptor responseInterceptor : this.responseInterceptors){
            responseInterceptor.process(response, context);
        }
    }

}
