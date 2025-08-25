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

import static com.feilong.net.http.HttpLogHelper.autoLog;

import com.feilong.core.Validate;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.builder.httpurirequest.HttpUriRequestFactory;
import com.feilong.net.http.packer.HttpRequestHeadersPacker;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 专门用来构造 {@link HttpUriRequest}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HttpUriRequestBuilder{

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
        if (log.isTraceEnabled()){
            log.trace(autoLog(httpRequest, connectionConfig, ""));
        }
        //---------------------------------------------------------------
        HttpUriRequest httpUriRequest = HttpUriRequestFactory.create(httpRequest, connectionConfig);

        HttpRequestHeadersPacker.setHeaders(httpUriRequest, httpRequest.getHeaderMap(), connectionConfig);
        return httpUriRequest;
    }

}
