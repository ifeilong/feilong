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
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JsonUtil;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;

/**
 * 通常用来解析接口返回状态码的 {@link ResultCallback}.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.1.0
 */
public class StatusCodeResultCallback extends AbstractResultCallback<Integer>{

    /** The Constant log. */
    private static final Logger                  LOGGER   = LoggerFactory.getLogger(StatusCodeResultCallback.class);

    //---------------------------------------------------------------

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

        //只需要http status code see https://github.com/ifeilong/feilong/issues/584
        StatusLine statusLine = httpResponse.getStatusLine();
        int statusCode = statusLine.getStatusCode();

        //---------------------------------------------------------------

        if (LOGGER.isInfoEnabled()){
            LOGGER.info(
                            "request:[{}],useConnectionConfig:[{}],statusCode:[{}]",
                            JsonUtil.toString(httpRequest, true),
                            JsonUtil.toString(useConnectionConfig, true),
                            httpResponse.getStatusLine());
        }
        return statusCode;
    }

}
