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

import static com.feilong.net.http.HttpLogHelper.autoLog;

import java.net.URI;
import java.net.URISyntaxException;

import com.feilong.core.Validate;
import com.feilong.lib.lang3.NotImplementedException;
import com.feilong.lib.org.apache.http.client.methods.HttpGet;
import com.feilong.lib.org.apache.http.client.methods.HttpPatch;
import com.feilong.lib.org.apache.http.client.methods.HttpPost;
import com.feilong.lib.org.apache.http.client.methods.HttpPut;
import com.feilong.lib.org.apache.http.client.methods.HttpRequestBase;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.lib.org.apache.http.client.utils.URIBuilder;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpMethodType;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.builder.RequestConfigBuilder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * A factory for creating {@link HttpUriRequest} objects.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpUriRequestFactory{

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
    public static HttpUriRequest create(HttpRequest httpRequest,@SuppressWarnings("unused") ConnectionConfig connectionConfig){
        HttpRequestBase httpRequestBase = create(httpRequest);
        // RequestConfig requestConfig = RequestConfigBuilder.build(connectionConfig)
        //httpRequestBase.setConfig(requestConfig)
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

            //since 3.5.1
            case PATCH:
                return buildPatch(httpRequest);

            default:
                throw new NotImplementedException(httpMethodType + " is not implemented!");
        }
    }

    //---------------------------------------------------------------
    /**
     * 基于 httpRequest 来构造uri.
     *
     * @param httpRequest
     *            the http request
     * @return the uri
     * @since 3.1.0
     */
    private static URI buildURI(HttpRequest httpRequest){
        URIBuilder uriBuilder = URIBuilderBuilder.build(httpRequest);
        try{
            return uriBuilder.build();
        }catch (URISyntaxException e){
            throw new UncheckedHttpException(autoLog(httpRequest, ""), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the get.
     *
     * @param httpRequest
     *            the http request
     * @return the http get
     */
    private static HttpGet buildGet(HttpRequest httpRequest){
        return new HttpGet(buildURI(httpRequest));
    }

    /**
     * Builds the post.
     *
     * @param httpRequest
     *            the http request
     * @return the http post
     */
    private static HttpPost buildPost(HttpRequest httpRequest){
        HttpPost httpPost = new HttpPost(buildURI(httpRequest));
        httpPost.setEntity(HttpEntityBuilder.build(httpRequest));
        return httpPost;
    }

    //---------------------------------------------------------------

    /**
     * Builds the put.
     *
     * @param httpRequest
     *            the http request
     * @return the http put
     */
    private static HttpPut buildPut(HttpRequest httpRequest){
        HttpPut httpPut = new HttpPut(buildURI(httpRequest));

        httpPut.setEntity(HttpEntityBuilder.build(httpRequest));
        return httpPut;
    }

    /**
     * Builds the put.
     *
     * @param httpRequest
     *            the http request
     * @return the http put
     * @since 3.5.1
     */
    private static HttpPatch buildPatch(HttpRequest httpRequest){
        HttpPatch httpPatch = new HttpPatch(buildURI(httpRequest));

        httpPatch.setEntity(HttpEntityBuilder.build(httpRequest));
        return httpPatch;
    }
}
