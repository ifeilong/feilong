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

import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.entity.ConnectionConfig;
import com.feilong.net.entity.HttpRequest;

/**
 * 通常用来解析接口返回状态码的 {@link ResultCallback}.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.1.0
 */
public class StatusCodeResultCallback implements ResultCallback<Integer>{

    private static final Logger                  LOGGER   = LoggerFactory.getLogger(StatusCodeResultCallback.class);

    /** Static instance. */
    // the static instance works for all types
    public static final StatusCodeResultCallback INSTANCE = new StatusCodeResultCallback();

    //---------------------------------------------------------------

    @Override
    public Integer on(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    HttpResponse httpResponse,
                    ConnectionConfig useConnectionConfig,
                    Date beginDate){
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();
        //---------------------------------------------------------------
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace(
                            "httpRequest:[{}],connectionConfig:[{}],statusCode:[{}]",
                            JsonUtil.format(httpRequest),
                            JsonUtil.format(useConnectionConfig),
                            statusCode);
        }
        return statusCode;
    }

}
