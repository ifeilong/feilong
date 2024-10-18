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

import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.client.methods.HttpUriRequest;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpFullInfo;
import com.feilong.net.http.HttpRequest;

/**
 * 用来解析http完整信息 {@link com.feilong.net.http.HttpFullInfo} 的 {@link ResultCallback}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.2.0
 */
public class HttpFullInfoResultCallback extends AbstractResultCallback<HttpFullInfo>{

    /** Static instance. */
    // the static instance works for all types
    public static final HttpFullInfoResultCallback INSTANCE = new HttpFullInfoResultCallback();

    //---------------------------------------------------------------
    @Override
    public HttpFullInfo on(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    HttpResponse httpResponse,
                    ConnectionConfig useConnectionConfig,
                    Date beginDate){

        com.feilong.net.http.HttpResponse feilongHttpResponse = buildHttpResponse(
                        httpRequest,
                        httpUriRequest,
                        httpResponse,
                        useConnectionConfig,
                        beginDate);
        HttpFullInfo httpFullInfo = new HttpFullInfo(httpRequest, useConnectionConfig);
        httpFullInfo.setHttpResponse(feilongHttpResponse);
        return httpFullInfo;
    }

    //---------------------------------------------------------------

    @Override
    public HttpFullInfo doException(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    ConnectionConfig useConnectionConfig,
                    UncheckedHttpException e,
                    Date beginDate){
        HttpFullInfo httpFullInfo = new HttpFullInfo(httpRequest, useConnectionConfig);
        httpFullInfo.setUncheckedHttpException(e);
        return httpFullInfo;
    }

}
