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

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JsonUtil;
import com.feilong.json.processor.StringOverLengthJsonValueProcessor;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.builder.HttpResponseUtil;

/**
 * 通常用来解析接口返回的字符串 的 {@link ResultCallback}.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.1.0
 */
public class ResponseBodyAsStringResultCallback implements ResultCallback<String>{

    /** The Constant LOGGER. */
    private static final Logger                            LOGGER   = LoggerFactory.getLogger(ResponseBodyAsStringResultCallback.class);

    /** Static instance. */
    // the static instance works for all types
    public static final ResponseBodyAsStringResultCallback INSTANCE = new ResponseBodyAsStringResultCallback();

    //---------------------------------------------------------------

    @Override
    public String on(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    HttpResponse httpResponse,
                    ConnectionConfig useConnectionConfig,
                    Date beginDate){
        String resultString = HttpResponseUtil.getResultString(httpResponse);
        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(
                            "request:[{}],useConnectionConfig:[{}],resultString:[{}]",
                            JsonUtil.format(httpRequest, true),
                            JsonUtil.format(useConnectionConfig, true),
                            StringOverLengthJsonValueProcessor.format(resultString, 1000));
        }
        return resultString;
    }

}
