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
package com.feilong.net.http.builder.httpurirequest;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.NotImplementedException;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpMethodType;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.builder.RequestConfigBuilder;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * A factory for creating {@link HttpUriRequest} objects.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
public class HttpUriRequestFactory{

    /** Don't let anyone instantiate this class. */
    private HttpUriRequestFactory(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 基于 <code>httpRequest</code> 和 <code>connectionConfig</code> 构造 {@link HttpUriRequest}.
     * 
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     * @return the http uri request
     * @see RequestConfigBuilder
     */
    public static HttpUriRequest create(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        HttpRequestBase httpRequestBase = create(httpRequest);
        // RequestConfig requestConfig = RequestConfigBuilder.build(connectionConfig);
        //httpRequestBase.setConfig(requestConfig);
        return httpRequestBase;
    }

    //---------------------------------------------------------------

    /**
     * 创建.
     *
     * @param httpRequest
     *            the http request
     * @return the http request base
     */
    private static HttpRequestBase create(HttpRequest httpRequest){
        HttpMethodType httpMethodType = httpRequest.getHttpMethodType();
        //since 2.1.0
        Validate.notNull(httpMethodType, "httpMethodType can't be null!,%s", httpRequest.getFullEncodedUrl());

        switch (httpMethodType) {
            case GET:
                return buildGet(httpRequest);

            case POST:
                return buildPost(httpRequest);

            //since 1.12.5
            case PUT:
                return buildPut(httpRequest);

            default:
                throw new NotImplementedException(httpMethodType + " is not implemented!");
        }
    }

    //---------------------------------------------------------------

    private static HttpGet buildGet(HttpRequest httpRequest){
        URIBuilder uriBuilder = URIBuilderBuilder.build(httpRequest);
        try{
            URI buildUri = uriBuilder.build();
            return new HttpGet(buildUri);
        }catch (URISyntaxException e){
            String message = Slf4jUtil.format("httpRequest:[{}]", JsonUtil.format(httpRequest));
            throw new UncheckedHttpException(message, e);
        }
    }

    //---------------------------------------------------------------

    private static HttpPost buildPost(HttpRequest httpRequest){
        HttpPost httpPost = new HttpPost(httpRequest.getUri());
        httpPost.setEntity(HttpEntityBuilder.build(httpRequest));
        return httpPost;
    }

    //---------------------------------------------------------------

    private static HttpPut buildPut(HttpRequest httpRequest){
        HttpPut httpPut = new HttpPut(httpRequest.getUri());
        httpPut.setEntity(HttpEntityBuilder.build(httpRequest));
        return httpPut;
    }
}
