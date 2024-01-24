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
     * <p>
     * <b>场景:</b> 某些公司json格式化在日志系统中会换行显示, 可阅读性特别差, 因此使用JsonUtil.toString 代替JsonUtil.format; <br>
     * 但是开发环境为了更快的发现以及排查问题需要使用JsonUtil.format;
     * 
     * <br>
     * 目前通过设置环境变量 feilong_httpRequest_format_enable 来区分, 如果值是true(忽略大小写) 那么会使用format,否则使用tostring
     * </p>
     *
     * @param httpRequest
     *            the http request
     * @return the string
     * @see <a href="https://github.com/ifeilong/feilong/issues/602">支持http 根据不同环境 日志是format 还是tostring 输出</a>
     */
    public static String createHttpRequestLog(HttpRequest httpRequest){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig(true);
        javaToJsonConfig.setPropertyNameAndJsonValueProcessorMap(
                        toMap("requestByteArrayBody", ToStringJsonValueProcessor.DEFAULT_INSTANCE));

        //---------------------------------------------------------------
        //环境变量 see https://github.com/ifeilong/feilong/issues/602
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
