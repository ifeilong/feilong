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
package com.feilong.net.httpclient4.callback;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateUtil.getIntervalTime;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.util.MapUtil.newTreeMap;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.formatter.FormatterUtil;
import com.feilong.json.processor.StringOverLengthJsonValueProcessor;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;
import com.feilong.net.httpclient4.builder.HttpResponseUtil;

/**
 * 用来解析 全数据 {@link com.feilong.net.entity.HttpResponse} 的 {@link ResultCallback}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.1.0
 */
public class HttpResponseResultCallback implements ResultCallback<com.feilong.net.entity.HttpResponse>{

    /** The Constant LOGGER. */
    private static final Logger                    LOGGER   = LoggerFactory.getLogger(HttpResponseResultCallback.class);

    /** Static instance. */
    // the static instance works for all types
    public static final HttpResponseResultCallback INSTANCE = new HttpResponseResultCallback();

    //---------------------------------------------------------------
    @Override
    public com.feilong.net.entity.HttpResponse on(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    HttpResponse httpResponse,
                    ConnectionConfig useConnectionConfig,
                    Date beginDate){
        com.feilong.net.entity.HttpResponse resultResponse = build(beginDate, httpResponse);
        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            String pattern = "request:[{}],useConnectionConfig:[{}],response:[{}]";
            String response = JsonUtil.format(
                            resultResponse,
                            new JavaToJsonConfig(toMap("resultString", (JsonValueProcessor) new StringOverLengthJsonValueProcessor())));

            LOGGER.info(pattern, JsonUtil.format(httpRequest), JsonUtil.format(useConnectionConfig), response);
        }
        return resultResponse;
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param beginDate
     *            the begin date
     * @param httpResponse
     *            the http response
     * @return the com.feilong.net.entity. http response
     * @since 1.10.6
     */
    private static com.feilong.net.entity.HttpResponse build(Date beginDate,HttpResponse httpResponse){
        StatusLine statusLine = httpResponse.getStatusLine();

        //---------------------------------------------------------------
        com.feilong.net.entity.HttpResponse result = new com.feilong.net.entity.HttpResponse();
        result.setStatusCode(statusLine.getStatusCode());
        //since 1.12.5
        result.setHeaderMap(convert(httpResponse.getAllHeaders()));
        result.setResultString(HttpResponseUtil.getResultString(httpResponse));
        result.setUseTime(getIntervalTime(beginDate, now()));

        return result;
    }

    //---------------------------------------------------------------

    /**
     * 用来将 Header[] 转成 Map<String, String>.
     *
     * @param allHeaders
     *            the all headers
     * @return 如果 <code>allHeaders</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     */
    private static Map<String, String> convert(Header[] allHeaders){
        if (isNullOrEmpty(allHeaders)){
            return emptyMap();
        }

        //---------------------------------------------------------------
        Map<String, String> map = newTreeMap();
        for (Header header : allHeaders){
            String name = header.getName();
            if (isNotNullOrEmpty(name)){
                map.put(name, header.getValue());
            }
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(FormatterUtil.formatToSimpleTable(map));
        }
        return map;
    }
}
