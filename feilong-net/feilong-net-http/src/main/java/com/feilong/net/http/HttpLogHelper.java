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

import static com.feilong.core.bean.ConvertUtil.toMap;

import com.feilong.core.lang.StringUtil;
import com.feilong.core.lang.SystemUtil;
import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.processor.ToStringJsonValueProcessor;

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

        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig(true);
        javaToJsonConfig.setPropertyNameAndJsonValueProcessorMap(
                        toMap("requestByteArrayBody", ToStringJsonValueProcessor.DEFAULT_INSTANCE));

        //---------------------------------------------------------------
        //环境变量
        String formatEnable = SystemUtil.getEnv("feilong_httpRequest_format_enable");
        if (StringUtil.trimAndEqualsIgnoreCase("true", formatEnable)){
            return JsonUtil.format(httpRequest, javaToJsonConfig);
        }
        return JsonUtil.toString(httpRequest, javaToJsonConfig);
    }

    //---------------------------------------------------------------

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
