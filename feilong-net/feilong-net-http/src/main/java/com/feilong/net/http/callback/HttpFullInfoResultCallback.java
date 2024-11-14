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
 * @param <T>
 * @since 4.2.0
 */
public class HttpFullInfoResultCallback<T> extends AbstractResultCallback<HttpFullInfo<T>>{

    private ResultBeanConverter<T> resultBeanConverter;

    /**
     * @param resultBeanConverter
     */
    public HttpFullInfoResultCallback(ResultBeanConverter<T> resultBeanConverter){
        super();
        this.resultBeanConverter = resultBeanConverter;
    }

    //---------------------------------------------------------------
    @Override
    public HttpFullInfo<T> on(
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
        HttpFullInfo<T> httpFullInfo = new HttpFullInfo<T>(httpRequest, useConnectionConfig);
        httpFullInfo.setHttpResponse(feilongHttpResponse);

        T resultBean = null == resultBeanConverter ? null
                        : resultBeanConverter.convert(httpRequest, useConnectionConfig, feilongHttpResponse);
        httpFullInfo.setResultBean(resultBean);
        return httpFullInfo;
    }

    //---------------------------------------------------------------

    @Override
    public HttpFullInfo<T> doException(
                    HttpRequest httpRequest,
                    HttpUriRequest httpUriRequest,
                    ConnectionConfig useConnectionConfig,
                    UncheckedHttpException e,
                    Date beginDate){
        HttpFullInfo<T> httpFullInfo = new HttpFullInfo<T>(httpRequest, useConnectionConfig);
        httpFullInfo.setUncheckedHttpException(e);
        return httpFullInfo;
    }

    //---------------------------------------------------------------

    /**
     * @return the resultBeanConverter
     */
    public ResultBeanConverter<T> getResultBeanConverter(){
        return resultBeanConverter;
    }

    /**
     * @param resultBeanConverter
     *            the resultBeanConverter to set
     */
    public void setResultBeanConverter(ResultBeanConverter<T> resultBeanConverter){
        this.resultBeanConverter = resultBeanConverter;
    }

}
