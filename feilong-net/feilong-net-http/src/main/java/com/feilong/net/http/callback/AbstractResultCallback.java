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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateUtil.getIntervalTime;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.util.MapUtil.newTreeMap;
import static com.feilong.net.http.HttpLogHelper.autoLog;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.org.apache.http.Header;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.StatusLine;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.builder.HttpResponseUtil;

/**
 * 抽象 ResultCallback.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 * @since 3.5.0
 */
public abstract class AbstractResultCallback<T> implements ResultCallback<T>{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResultCallback.class);

    //---------------------------------------------------------------
    protected com.feilong.net.http.HttpResponse buildHttpResponse(
                    HttpRequest httpRequest,
                    @SuppressWarnings("unused") HttpUriRequest httpUriRequest,
                    HttpResponse httpResponse,
                    ConnectionConfig useConnectionConfig,
                    Date beginDate){
        com.feilong.net.http.HttpResponse resultResponse = build(beginDate, httpResponse);
        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(
                            autoLog(
                                            httpRequest,
                                            resultResponse,
                                            useConnectionConfig, //

                                            "statusCode:[{}]",
                                            httpResponse.getStatusLine()));

        }
        return resultResponse;
    }

    //---------------------------------------------------------------

    private static com.feilong.net.http.HttpResponse build(Date beginDate,HttpResponse httpResponse){
        StatusLine statusLine = httpResponse.getStatusLine();

        //---------------------------------------------------------------
        com.feilong.net.http.HttpResponse result = new com.feilong.net.http.HttpResponse();
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
        return map;
    }

    @Override
    public T doException(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    ConnectionConfig useConnectionConfig,
                    UncheckedHttpException e,
                    Date beginDate){
        //默认抛出异常
        throw e;
    }
}
