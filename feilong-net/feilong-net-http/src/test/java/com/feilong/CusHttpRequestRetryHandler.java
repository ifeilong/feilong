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
package com.feilong;

import java.io.IOException;

import javax.net.ssl.SSLHandshakeException;

import com.feilong.lib.org.apache.http.HttpEntityEnclosingRequest;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.NoHttpResponseException;
import com.feilong.lib.org.apache.http.client.methods.HttpExecutionAware;
import com.feilong.lib.org.apache.http.client.methods.HttpRequestWrapper;
import com.feilong.lib.org.apache.http.client.protocol.HttpClientContext;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import com.feilong.lib.org.apache.http.protocol.HttpContext;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.1.1
 * 
 *        Builds the.
 *
 * @see com.feilong.lib.org.apache.http.impl.client.DefaultHttpRequestRetryHandler#retryRequest(IOException, int, HttpContext)
 * @see com.feilong.lib.org.apache.http.impl.execchain.RetryExec#execute(HttpRoute, HttpRequestWrapper, HttpClientContext,
 *      HttpExecutionAware)
 * 
 * @see com.feilong.lib.org.apache.http.impl.client.DefaultHttpRequestRetryHandler
 * @see com.feilong.lib.org.apache.http.impl.client.StandardHttpRequestRetryHandler
 * @since 3.1.1
 * @deprecated 没有用
 */
@Deprecated
public class CusHttpRequestRetryHandler extends DefaultHttpRequestRetryHandler{

    @Override
    public boolean retryRequest(IOException exception,int executionCount,HttpContext context){
        if (executionCount >= 5){
            // 如果超过最大重试次数，那么就不要继续了
            return false;
        }
        if (exception instanceof NoHttpResponseException){
            // 如果服务器丢掉了连接，那么就重试
            return true;
        }
        if (exception instanceof SSLHandshakeException){
            // 不要重试SSL握手异常
            return false;
        }

        //---------------------------------------------------------------
        HttpRequest request = (HttpRequest) context.getAttribute("http.request");
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent){
            // 如果请求被认为是幂等的，那么就重试
            return true;
        }
        return false;
    }
}
