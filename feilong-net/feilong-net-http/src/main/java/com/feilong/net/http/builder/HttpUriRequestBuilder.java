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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.builder.httpurirequest.HttpUriRequestFactory;
import com.feilong.net.http.packer.HttpRequestHeadersPacker;

/**
 * 专门用来构造 {@link HttpUriRequest}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
public final class HttpUriRequestBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUriRequestBuilder.class);

    /** Don't let anyone instantiate this class. */
    private HttpUriRequestBuilder(){
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
     * @return 如果 <code>httpRequest</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>httpRequest Uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>httpRequest Uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static HttpUriRequest build(HttpRequest httpRequest,ConnectionConfig connectionConfig){
        Validate.notNull(httpRequest, "httpRequest can't be null!");

        String uri = httpRequest.getUri();
        Validate.notBlank(uri, "uri can't be blank!");

        //---------------------------------------------------------------
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace(
                            "httpRequest info:[{}],connectionConfig:[{}]",
                            JsonUtil.toString(httpRequest, true),
                            JsonUtil.toString(connectionConfig, true));
        }
        //---------------------------------------------------------------
        HttpUriRequest httpUriRequest = HttpUriRequestFactory.create(httpRequest, connectionConfig);

        HttpRequestHeadersPacker.setHeaders(httpUriRequest, httpRequest.getHeaderMap(), connectionConfig);
        return httpUriRequest;
    }

}
