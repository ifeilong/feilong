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
package com.feilong.context.invoker.http;

import com.feilong.context.invoker.AbstractResponseStringBuilder;
import com.feilong.context.invoker.ResponseStringBuilder;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpClientUtil;
import com.feilong.net.http.HttpRequest;

/**
 * Http 类型的 {@link ResponseStringBuilder}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.3
 */
public class HttpResponseStringBuilder<T> extends AbstractResponseStringBuilder<T>{

    /** 请求参数构造器. */
    private HttpRequestBuilder<T>      httpRequestBuilder;

    /** 配置构造器. */
    private ConnectionConfigBuilder<T> connectionConfigBuilder;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.invoker.AbstractResponseStringBuilder#handler(java.lang.Object)
     */
    @Override
    protected String handler(T request){
        HttpRequest httpRequest = buildHttpRequest(request);
        ConnectionConfig connectionConfig = buildConnectionConfig(request);

        return HttpClientUtil.getResponseBodyAsString(httpRequest, connectionConfig);
    }

    //---------------------------------------------------------------

    /**
     * Builds the connection config.
     *
     * @param request
     *            the request
     * @return the connection config
     * @since 1.11.3
     */
    protected ConnectionConfig buildConnectionConfig(T request){
        return null == connectionConfigBuilder ? null : connectionConfigBuilder.build(request);
    }

    //---------------------------------------------------------------

    /**
     * Builds the http request.
     *
     * @param request
     *            the request
     * @return the http request
     * @since 1.11.3
     */
    protected HttpRequest buildHttpRequest(T request){
        return httpRequestBuilder.build(request);
    }

    //---------------------------------------------------------------

    /**
     * 设置 请求参数构造器.
     *
     * @param httpRequestBuilder
     *            the new 请求参数构造器
     */
    public void setHttpRequestBuilder(HttpRequestBuilder<T> httpRequestBuilder){
        this.httpRequestBuilder = httpRequestBuilder;
    }

    /**
     * 设置 配置构造器.
     *
     * @param connectionConfigBuilder
     *            the new 配置构造器
     */
    public void setConnectionConfigBuilder(ConnectionConfigBuilder<T> connectionConfigBuilder){
        this.connectionConfigBuilder = connectionConfigBuilder;
    }
}
