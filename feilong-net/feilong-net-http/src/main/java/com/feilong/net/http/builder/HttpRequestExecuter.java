/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.net.http.builder;

import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;

import java.util.Date;

import com.feilong.core.Validate;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.client.HttpClient;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.callback.ResultCallback;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 专门发送请求 <code>httpUriRequest</code> .
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpRequestExecuter{

    /**
     * Execute.
     *
     * @param <T>
     *            the generic type
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @param resultCallback
     *            the call
     * @return 如果 <code>resultCallback</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 2.1.0
     */
    public static <T> T execute(HttpRequest httpRequest,ConnectionConfig connectionConfig,ResultCallback<T> resultCallback){
        Validate.notNull(resultCallback, "resultCallback can't be null!");

        //---------------------------------------------------------------
        Date beginDate = now();
        ConnectionConfig useConnectionConfig = defaultIfNull(connectionConfig, ConnectionConfig.INSTANCE);
        HttpUriRequest httpUriRequest = HttpUriRequestBuilder.build(httpRequest, useConnectionConfig);

        HttpResponse httpResponse = null;
        try{
            httpResponse = execute(httpRequest, httpUriRequest, useConnectionConfig);
            //---------------------------------------------------------------
            //回手掏
            return resultCallback.on(httpRequest, httpUriRequest, httpResponse, useConnectionConfig, beginDate);

        }catch (UncheckedHttpException e){
            return resultCallback.doException(httpRequest, httpUriRequest, useConnectionConfig, e, beginDate);
        }
    }

    //---------------------------------------------------------------

    /**
     * Execute.
     *
     * @param httpRequest
     *            the http request
     * @param httpUriRequest
     *            the http uri request
     * @param useConnectionConfig
     *            the connection config
     * @return 如果 <code>httpUriRequest</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.11.0 change method Access Modifiers
     * @since 2.1.0 change method Access Modifiers to private
     */
    private static HttpResponse execute(HttpRequest httpRequest,HttpUriRequest httpUriRequest,ConnectionConfig useConnectionConfig){
        Validate.notNull(httpUriRequest, "httpUriRequest can't be null!");

        //---------------------------------------------------------------
        HttpClient httpClient = HttpClientBuilder.build(httpRequest.getLogTraceContext(), useConnectionConfig);

        try{
            return httpClient.execute(httpUriRequest);
        }catch (Exception e){
            httpUriRequest.abort();
            String message = HttpRequestExecuterExceptionMessageBuilder.build(httpRequest, useConnectionConfig, e);
            throw new UncheckedHttpException(message, e);
        }finally{
            //不能直接abort,否则内容查不出来
            //参见 https://github.com/venusdrogon/feilong-net/issues/80
            //httpUriRequest.abort()
        }
    }

}
