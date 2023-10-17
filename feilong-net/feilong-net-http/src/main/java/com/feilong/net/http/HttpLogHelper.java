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
package com.feilong.net.http;

import com.feilong.json.JsonUtil;

/**
 * 专注于辅助生成http日志.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.1
 */
public class HttpLogHelper{

    /**
     * 创建 http request log.
     *
     * @param httpRequest
     *            the http request
     * @return the string
     */
    public static String createHttpRequestLog(HttpRequest httpRequest){
        return JsonUtil.toString(httpRequest, true);
    }

    /**
     * 创建 connection config log.
     *
     * @param useConnectionConfig
     *            the use connection config
     * @return the string
     */
    public static String createConnectionConfigLog(ConnectionConfig useConnectionConfig){
        return JsonUtil.toString(useConnectionConfig, true);
    }
}
