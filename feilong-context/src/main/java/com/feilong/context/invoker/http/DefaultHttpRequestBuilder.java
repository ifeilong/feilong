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
package com.feilong.context.invoker.http;

import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;

import java.util.Collections;
import java.util.Map;

import com.feilong.context.Rebuilder;
import com.feilong.context.beanproperty.HttpTypeBeanProperty;
import com.feilong.context.invoker.RequestParamsBuilder;
import com.feilong.net.http.HttpRequest;

/**
 * 默认的 {@link HttpRequest} 构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.3
 */
public class DefaultHttpRequestBuilder<T> implements HttpRequestBuilder<T>{

    /** http请求类型的bean属性. */
    private HttpTypeBeanProperty    httpTypeBeanProperty;

    //---------------------------------------------------------------

    /** 请求参数构造器. */
    private RequestParamsBuilder<T> requestParamsBuilder;

    /** 请求头构造器. */
    private RequestHeaderBuilder<T> requestHeaderBuilder;

    /** 请求体构造器. */
    private RequestBodyBuilder<T>   requestBodyBuilder;

    //---------------------------------------------------------------
    /**
     * 再加工器.
     * 
     * <p>
     * 支持在组装完 {@link HttpRequest} 之后,再次加工
     * </p>
     * 
     * <p>
     * 典型应用在于, 某接口传递了requestbody 属性之后, 还需要额外在uri中加入参数 sign , 值是 requestbody 再次ase加密之后的结果
     * </p>
     * 
     * <p>
     * <span style="color:red">一般情况下用不到</span>
     * </p>
     * 
     * @since 1.11.4
     */
    private Rebuilder<HttpRequest>  httpRequestRebuilder;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.invoker.HttpRequestBuilder#build(java.lang.Object)
     */
    @Override
    public HttpRequest build(T request){
        String uri = HttpRequestUriResolver.resolve(httpTypeBeanProperty.getUri(), request);

        HttpRequest httpRequest = new HttpRequest(//
                        uri,
                        sortMapByKeyAsc(buildRequestParamsMap(request)),
                        httpTypeBeanProperty.getMethod());

        //---------------------------------------------------------------
        httpRequest.setHeaderMap(buildRequestHeaderMap(request));
        httpRequest.setRequestBody(null == requestBodyBuilder ? null : requestBodyBuilder.build(request));

        //---------------------------------------------------------------
        if (null != httpRequestRebuilder){
            return httpRequestRebuilder.rebuild(httpRequest);
        }
        return httpRequest;
    }

    //---------------------------------------------------------------

    /**
     * 构造请求参数map.
     *
     * @param request
     *            the request
     * @return the map
     * @since 1.11.3
     */
    private Map<String, String> buildRequestParamsMap(T request){
        return null == requestParamsBuilder ? Collections.<String, String> emptyMap() : //
                        requestParamsBuilder.build(request);
    }

    /**
     * 构造请求头 map.
     *
     * @param request
     *            the request
     * @return the map
     */
    private Map<String, String> buildRequestHeaderMap(T request){
        return null == requestHeaderBuilder ? Collections.<String, String> emptyMap() : //
                        requestHeaderBuilder.build(request);
    }

    //---------------------------------------------------------------
    /**
     * 设置 http请求类型的bean属性.
     *
     * @param httpTypeBeanProperty
     *            the httpTypeBeanProperty to set
     */
    public void setHttpTypeBeanProperty(HttpTypeBeanProperty httpTypeBeanProperty){
        this.httpTypeBeanProperty = httpTypeBeanProperty;
    }

    /**
     * 设置 参数构造器.
     *
     * @param requestParamsBuilder
     *            the requestParamsBuilder to set
     */
    public void setRequestParamsBuilder(RequestParamsBuilder<T> requestParamsBuilder){
        this.requestParamsBuilder = requestParamsBuilder;
    }

    /**
     * 设置 头构造器.
     *
     * @param requestHeaderBuilder
     *            the requestHeaderBuilder to set
     */
    public void setRequestHeaderBuilder(RequestHeaderBuilder<T> requestHeaderBuilder){
        this.requestHeaderBuilder = requestHeaderBuilder;
    }

    /**
     * 设置 requestBody构造器.
     *
     * @param requestBodyBuilder
     *            the requestBodyBuilder to set
     */
    public void setRequestBodyBuilder(RequestBodyBuilder<T> requestBodyBuilder){
        this.requestBodyBuilder = requestBodyBuilder;
    }

    /**
     * 再加工器.
     * 
     * <p>
     * 支持在组装完 {@link HttpRequest} 之后,再次加工
     * </p>
     * 
     * <p>
     * 典型应用在于, 某接口传递了requestbody 属性之后, 还需要额外在uri中加入参数 sign , 值是 requestbody 再次ase加密之后的结果
     * </p>
     * 
     * <p>
     * <span style="color:red">一般情况下用不到</span>
     * </p>
     *
     * @param rebuilder
     *            the rebuilder to set
     * @since 1.11.4
     */
    public void setHttpRequestRebuilder(Rebuilder<HttpRequest> rebuilder){
        this.httpRequestRebuilder = rebuilder;
    }

}
