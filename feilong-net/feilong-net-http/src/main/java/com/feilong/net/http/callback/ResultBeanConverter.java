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
package com.feilong.net.http.callback;

import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.HttpResponse;

/**
 * A callback interface,用来执行httpclient之后,渲染拿到的结果.
 *
 * @param <T>
 *            the generic type
 * @since 4.3.0
 */
public interface ResultBeanConverter<T> {

    /**
     * 转换数据.
     * 
     * <p>
     * 注意:每个参数都不会为null
     * </p>
     *
     * @param httpRequest
     *            the http request
     * @param useConnectionConfig
     *            the use connection config
     * @param httpResponse
     *            the http response
     * @return the t
     */
    T convert(
                    HttpRequest httpRequest,
                    ConnectionConfig useConnectionConfig,//
                    HttpResponse httpResponse);

}