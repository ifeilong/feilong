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

import java.io.Serializable;

import com.feilong.net.UncheckedHttpException;

/**
 * http完整信息, 方便在外层做日志或者保存日志到数据库中.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 4.2.0
 */
public class HttpFullInfo<T> implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long      serialVersionUID = 4945181504153761835L;

    //---------------------------------------------------------------

    /** The http request. */
    private HttpRequest            httpRequest;

    /** The connection config. */
    private ConnectionConfig       connectionConfig;

    //---------------------------------------------------------------

    /** The http response. */
    private HttpResponse           httpResponse;

    /** 如果有异常. */
    private UncheckedHttpException uncheckedHttpException;

    /**
     * 转换的bean.
     * 
     * @since 4.3.0
     */
    private T                      resultBean;

    //---------------------------------------------------------------

    /**
     * Instantiates a new http full info.
     */
    public HttpFullInfo(){
        super();
    }

    /**
     * Instantiates a new http full info.
     *
     * @param httpRequest
     *            the http request
     * @param connectionConfig
     *            the connection config
     */
    public HttpFullInfo(HttpRequest httpRequest, ConnectionConfig connectionConfig){
        super();
        this.httpRequest = httpRequest;
        this.connectionConfig = connectionConfig;
    }

    //---------------------------------------------------------------

    /**
     * Gets the http request.
     *
     * @return the httpRequest
     */
    public HttpRequest getHttpRequest(){
        return httpRequest;
    }

    /**
     * Sets the http request.
     *
     * @param httpRequest
     *            the httpRequest to set
     */
    public void setHttpRequest(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    /**
     * Gets the connection config.
     *
     * @return the connectionConfig
     */
    public ConnectionConfig getConnectionConfig(){
        return connectionConfig;
    }

    /**
     * Sets the connection config.
     *
     * @param connectionConfig
     *            the connectionConfig to set
     */
    public void setConnectionConfig(ConnectionConfig connectionConfig){
        this.connectionConfig = connectionConfig;
    }

    /**
     * Gets the http response.
     *
     * @return the httpResponse
     */
    public HttpResponse getHttpResponse(){
        return httpResponse;
    }

    /**
     * Sets the http response.
     *
     * @param httpResponse
     *            the httpResponse to set
     */
    public void setHttpResponse(HttpResponse httpResponse){
        this.httpResponse = httpResponse;
    }

    /**
     * 获得 如果有异常.
     *
     * @return the uncheckedHttpException
     */
    public UncheckedHttpException getUncheckedHttpException(){
        return uncheckedHttpException;
    }

    /**
     * 设置 如果有异常.
     *
     * @param uncheckedHttpException
     *            the uncheckedHttpException to set
     */
    public void setUncheckedHttpException(UncheckedHttpException uncheckedHttpException){
        this.uncheckedHttpException = uncheckedHttpException;
    }

    /**
     * 获得 转换的bean.
     *
     * @return the resultBean
     * @since 4.3.0
     */
    public T getResultBean(){
        return resultBean;
    }

    /**
     * 设置 转换的bean.
     *
     * @param resultBean
     *            the resultBean to set
     * @since 4.3.0
     */
    public void setResultBean(T resultBean){
        this.resultBean = resultBean;
    }

}
