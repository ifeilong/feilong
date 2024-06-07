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
package com.feilong.context.invoker;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import java.util.Map;

import com.feilong.context.beanproperty.SimpleHttpTypeBeanProperty;
import com.feilong.context.converter.JsonStringToBeanConverter;
import com.feilong.context.converter.JsonStringToListConverter;
import com.feilong.context.converter.StringToBeanConverter;
import com.feilong.context.invoker.http.DefaultHttpRequestBuilder;
import com.feilong.context.invoker.http.HttpRequestBuilder;
import com.feilong.context.invoker.http.HttpResponseStringBuilder;
import com.feilong.context.invoker.http.RequestBodyBuilder;
import com.feilong.context.invoker.http.SimpleMapRequestHeaderBuilder;
import com.feilong.json.JsonHelper;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.net.http.HttpMethodType;

/**
 * 简单的 http请求类型并且返回值是 json格式的ResponseCommandBuilder.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <R>
 *            请求
 * @param <T>
 *            响应的字符串转换成的对象
 * @see DefaultRequestResultInvoker
 * @see HttpResponseStringBuilder
 * @see DefaultHttpRequestBuilder
 * @since 3.4.1
 * @since 4.1.0 remove T extends ResponseCommand
 */
public class SimpleHttpAndJsonResponseCommandBuilder<R extends InvokerRequest, T> extends AbstractResponseCommandBuilder<R, T>{

    /** 提交地址. */
    private String                  uri;

    /** http请求method. */
    private String                  method = HttpMethodType.GET.getMethod();

    //---------------------------------------------------------------

    /** http请求头. */
    private Map<String, String>     headerMap;

    //---------------------------------------------------------------

    /** 请求参数构造器. */
    private RequestParamsBuilder<R> requestParamsBuilder;

    /** 请求体构造器. */
    private RequestBodyBuilder<R>   requestBodyBuilder;

    //---------------------------------------------------------------
    /** 结果转换的类型. */
    private Class<T>                responseCommandRootClass;
    //---------------------------------------------------------------

    /**
     * 获得 响应结果字符串构造器.
     *
     * @return the 响应结果字符串构造器
     */
    @Override
    protected ResponseStringBuilder<R> createResponseStringBuilder(){
        HttpResponseStringBuilder<R> httpResponseStringBuilder = new HttpResponseStringBuilder<>();
        //        httpResponseStringBuilder.setConnectionConfigBuilder(null);
        //        httpResponseStringBuilder.setFormat("json");
        httpResponseStringBuilder.setHttpRequestBuilder(createHttpRequestBuilder());
        //        httpResponseStringBuilder.setResponseStringRebuilder(null);
        //        httpResponseStringBuilder.setStringFormatter(null);

        return httpResponseStringBuilder;
    }

    //---------------------------------------------------------------

    /**
     * Instantiates a new simple http and json format string response command builder.
     */
    public SimpleHttpAndJsonResponseCommandBuilder(){
        super();
    }

    /**
     * Instantiates a new simple http and json format string response command builder.
     *
     * @param uri
     *            the uri
     */
    public SimpleHttpAndJsonResponseCommandBuilder(String uri){
        super();
        this.uri = uri;
    }

    /**
     * Instantiates a new simple http and json format string response command builder.
     *
     * @param uri
     *            the uri
     * @param method
     *            the method
     * @param headerMap
     *            the header map
     */
    public SimpleHttpAndJsonResponseCommandBuilder(String uri, String method, Map<String, String> headerMap){
        super();
        this.uri = uri;
        this.method = method;
        this.headerMap = headerMap;
    }

    /**
     * Instantiates a new simple http and json format string response command builder.
     *
     * @param uri
     *            the uri
     * @param method
     *            the method
     * @param headerMap
     *            the header map
     * @param rootClass
     *            the root class
     */
    public SimpleHttpAndJsonResponseCommandBuilder(String uri, String method, Map<String, String> headerMap, Class<T> rootClass){
        super();
        this.uri = uri;
        this.method = method;
        this.headerMap = headerMap;
        this.responseCommandRootClass = rootClass;
    }

    //---------------------------------------------------------------

    /**
     * 获得 字符串转换成bean 转换器.
     *
     * @return the 字符串转换成bean 转换器
     * @since 4.1.0 add param responseString
     */
    @Override
    protected StringToBeanConverter<T> createStringToBeanConverter(String responseString){
        if (JsonHelper.isNeedConvertToJSONArray(StringUtils.trim(responseString))){
            return new JsonStringToListConverter(responseCommandRootClass);
        }
        return new JsonStringToBeanConverter<>(responseCommandRootClass);
    }

    //---------------------------------------------------------------

    /**
     * 创建 http request builder.
     *
     * @return the http request builder
     */
    private HttpRequestBuilder<R> createHttpRequestBuilder(){
        DefaultHttpRequestBuilder<R> httpRequestBuilder = new DefaultHttpRequestBuilder<>();
        //        httpRequestBuilder.setHttpRequestRebuilder(null);
        httpRequestBuilder.setHttpTypeBeanProperty(new SimpleHttpTypeBeanProperty(uri, method));

        if (isNotNullOrEmpty(headerMap)){
            httpRequestBuilder.setRequestHeaderBuilder(new SimpleMapRequestHeaderBuilder<>(headerMap));
        }

        //---------------------------------------------------------------

        if (isNotNullOrEmpty(requestParamsBuilder)){
            httpRequestBuilder.setRequestParamsBuilder(requestParamsBuilder);

        }
        if (isNotNullOrEmpty(requestBodyBuilder)){
            httpRequestBuilder.setRequestBodyBuilder(requestBodyBuilder);
        }
        return httpRequestBuilder;
    }

    //---------------------------------------------------------------

    /**
     * 设置 提交地址.
     *
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri){
        this.uri = uri;
    }

    /**
     * 设置 http请求method.
     *
     * @param method
     *            the method to set
     */
    public void setMethod(String method){
        this.method = method;
    }

    /**
     * 设置 http请求头.
     *
     * @param headerMap
     *            the headerMap to set
     */
    public void setHeaderMap(Map<String, String> headerMap){
        this.headerMap = headerMap;
    }

    /**
     * 设置 请求参数构造器.
     *
     * @param requestParamsBuilder
     *            the requestParamsBuilder to set
     */
    public void setRequestParamsBuilder(RequestParamsBuilder<R> requestParamsBuilder){
        this.requestParamsBuilder = requestParamsBuilder;
    }

    /**
     * 设置 请求体构造器.
     *
     * @param requestBodyBuilder
     *            the requestBodyBuilder to set
     */
    public void setRequestBodyBuilder(RequestBodyBuilder<R> requestBodyBuilder){
        this.requestBodyBuilder = requestBodyBuilder;
    }

    /**
     * 设置 结果转换的类型.
     *
     * @param rootClass
     *            the rootClass to set
     */
    public void setResponseCommandRootClass(Class<T> rootClass){
        this.responseCommandRootClass = rootClass;
    }

}
