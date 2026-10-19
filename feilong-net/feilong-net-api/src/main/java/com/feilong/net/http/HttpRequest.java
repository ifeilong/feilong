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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.net.http.HttpMethodType.GET;

import java.util.Map;

import com.feilong.core.net.ParamUtil;
import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;
import com.feilong.net.UriProcessor;

/**
 * http 请求信息.
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * HttpRequest httpRequest = HttpRequest.builder()
 * .uri("http://www.feilong.com")
 * .httpMethodType(HttpMethodType.GET)
 * .build();
 * 
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see "org.apache.http.HttpRequest"
 * @see #builder()
 * @since 1.2.0
 */
public class HttpRequest{

    /**
     * 伪造的 useragent.
     * 
     * @since 1.5.0
     */
    public static final String   DEFAULT_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    //---------------------------------------------------------------
    /** 请求的uri地址. */
    private String               uri;

    /** 请求method 类型,默认 {@link HttpMethodType#GET}. */
    private HttpMethodType       httpMethodType     = GET;

    //---------------------------------------------------------------

    /** http 请求 key-value 参数map. */
    private Map<String, String>  paramMap;

    /** 请求头信息. */
    private Map<String, String>  headerMap;

    //---------------------------------------------------------------

    /**
     * 请求正文,比如 webservice 可以传递 xml/json数据体.
     *
     * @see <a href="https://en.wikipedia.org/wiki/HTTP_message_body">HTTP_message_body</a>
     * @since 1.10.0
     */
    private String               requestBody;

    /**
     * 二进制数据:请求体可以包含二进制数据，如文件、图像、音频等。
     * 
     * 这些数据通常以字节流的形式传递。.
     *
     * @since 4.0.1
     */
    private RequestByteArrayBody requestByteArrayBody;

    //---------------------------------------------------------------

    /**
     * 是否uri trim.
     * <p>
     * 有时候程序员在配置uri 的时候,会误操作 uri 前后会多出空格, 这样会导致一些功能不work,要排查好久, <br>
     * 理论上uri前后是没有空格的, 所以默认为true ,会自动去除前后的空格; <br>
     * 如果真的有特殊需求, 可以设置为false
     * </p>
     * 
     * @since 3.0.10
     */
    private boolean              isTrimUri          = true;

    //---------------------------------------------------------------
    /**
     * 日志追踪上下文.
     * <p>
     * 所有的feilong http此次调用的log 都会拼上改日志字符串
     * </p>
     * 
     * @since 4.0.8
     */
    private String               logTraceContext    = "";
    //---------------------------------------------------------------

    /**
     * The Constructor.
     * 
     * @since 1.5.4
     */
    public HttpRequest(){
        super();
    }

    /**
     * Instantiates a new http request.
     *
     * @param uri
     *            the uri
     * @since 1.10.4
     */
    public HttpRequest(String uri){
        super();
        this.uri = uri;
    }

    /**
     * Instantiates a new http request.
     *
     * @param uri
     *            the uri
     * @param httpMethodType
     *            the http method type
     * @since 1.11.0
     */
    public HttpRequest(String uri, HttpMethodType httpMethodType){
        super();
        this.uri = uri;
        this.httpMethodType = httpMethodType;
    }

    /**
     * The Constructor.
     *
     * @param uri
     *            the uri
     * @param paramMap
     *            http 请求 key-value 参数map
     * @param httpMethodType
     *            the http method type
     * @since 1.5.4
     */
    public HttpRequest(String uri, Map<String, String> paramMap, HttpMethodType httpMethodType){
        super();
        this.uri = uri;
        this.paramMap = paramMap;
        this.httpMethodType = httpMethodType;
    }

    /**
     * The Constructor.
     *
     * @param uri
     *            the uri
     * @param paramMap
     *            http 请求 key-value 参数map
     * @param httpMethodType
     *            <span style="color:red">不区分大小写</span>, 比如get,Get,GET都可以,但是需要对应 {@link HttpMethodType}的支持的枚举值
     * @since 1.5.4
     */
    public HttpRequest(String uri, Map<String, String> paramMap, String httpMethodType){
        super();
        this.uri = uri;
        this.paramMap = paramMap;
        this.httpMethodType = HttpMethodType.getByMethodValueIgnoreCase(httpMethodType);
    }

    //---------------------------------------------------------------

    /**
     * 创建 {@link HttpRequest} 的 {@link Builder}, 支持链式构造 {@link HttpRequest}.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * HttpRequest httpRequest = HttpRequest.builder()
     * .uri("http://www.feilong.com")
     * .httpMethodType(HttpMethodType.GET)
     * .build();
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * @return the builder
     * @see Builder
     * @since 4.5.6
     */
    public static Builder builder(){
        return new Builder();
    }

    //---------------------------------------------------------------

    /**
     * {@link HttpRequest} 的构造器, 支持链式调用.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * HttpRequest httpRequest = HttpRequest.builder()
     * .uri("http://www.feilong.com")
     * .paramMap(toMap("name", "feilong"))
     * .trimUri(false)
     * .build();
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>没有设置的字段使用 builder 的默认值, 和 {@code new HttpRequest()} 一致: {@code httpMethodType} 是 {@link HttpMethodType#GET},
     * {@code isTrimUri} 是 {@code true}, {@code logTraceContext} 是 empty</li>
     * <li>{@link #build()} 每次都会创建新的 {@link HttpRequest} 对象, 而不是返回 builder 自身</li>
     * </ol>
     * </blockquote>
     * 
     * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
     * @since 4.5.6
     */
    public static class Builder{

        /** 请求的uri地址. */
        private String               uri;

        /** 请求method 类型,默认 {@link HttpMethodType#GET}. */
        private HttpMethodType       httpMethodType  = GET;

        //---------------------------------------------------------------

        /** http 请求 key-value 参数map. */
        private Map<String, String>  paramMap;

        /** 请求头信息. */
        private Map<String, String>  headerMap;

        //---------------------------------------------------------------

        /** 请求正文,比如 webservice 可以传递 xml/json数据体. */
        private String               requestBody;

        /** 二进制数据:请求体可以包含二进制数据，如文件、图像、音频等。 */
        private RequestByteArrayBody requestByteArrayBody;

        //---------------------------------------------------------------

        /** 是否uri trim,默认 {@code true}. */
        private boolean              isTrimUri       = true;

        /** 日志追踪上下文,默认 empty. */
        private String               logTraceContext = "";

        //---------------------------------------------------------------

        /**
         * 私有构造器, 请通过 {@link HttpRequest#builder()} 创建.
         */
        private Builder(){
            super();
        }

        /**
         * 设置 请求的uri地址.
         * 
         * @param uri
         *            the new 请求的uri地址
         * @return the builder
         * @since 4.5.6
         */
        public Builder uri(String uri){
            this.uri = uri;
            return this;
        }

        /**
         * 设置 请求method 类型,默认 {@link HttpMethodType#GET}.
         * 
         * @param httpMethodType
         *            the httpMethodType to set
         * @return the builder
         * @since 4.5.6
         */
        public Builder httpMethodType(HttpMethodType httpMethodType){
            this.httpMethodType = httpMethodType;
            return this;
        }

        /**
         * 设置 http 请求 key-value 参数map.
         * 
         * @param paramMap
         *            http 请求 key-value 参数map
         * @return the builder
         * @since 4.5.6
         */
        public Builder paramMap(Map<String, String> paramMap){
            this.paramMap = paramMap;
            return this;
        }

        /**
         * 设置 请求头 信息.
         * 
         * @param headerMap
         *            the headerMap to set
         * @return the builder
         * @since 4.5.6
         */
        public Builder headerMap(Map<String, String> headerMap){
            this.headerMap = headerMap;
            return this;
        }

        /**
         * 设置 请求正文,比如 webservice 可以传递 xml/json数据体.
         * 
         * @param requestBody
         *            the requestBody to set
         * @return the builder
         * @since 4.5.6
         */
        public Builder requestBody(String requestBody){
            this.requestBody = requestBody;
            return this;
        }

        /**
         * 设置 二进制数据:请求体可以包含二进制数据，如文件、图像、音频等。
         * 
         * @param requestByteArrayBody
         *            the requestByteArrayBody to set
         * @return the builder
         * @since 4.5.6
         */
        public Builder requestByteArrayBody(RequestByteArrayBody requestByteArrayBody){
            this.requestByteArrayBody = requestByteArrayBody;
            return this;
        }

        /**
         * 设置 是否uri trim,默认 {@code true}.
         * 
         * @param isTrimUri
         *            the isTrimUri to set
         * @return the builder
         * @since 4.5.6
         */
        public Builder trimUri(boolean isTrimUri){
            this.isTrimUri = isTrimUri;
            return this;
        }

        /**
         * 设置 日志追踪上下文,默认 empty.
         * 
         * @param logTraceContext
         *            the logTraceContext to set
         * @return the builder
         * @since 4.5.6
         */
        public Builder logTraceContext(String logTraceContext){
            this.logTraceContext = logTraceContext;
            return this;
        }

        /**
         * 创建 {@link HttpRequest}.
         * 
         * <h3>说明:</h3>
         * <blockquote>
         * <ol>
         * <li>每次调用都会创建新的 {@link HttpRequest} 对象</li>
         * <li>没有设置的字段使用 builder 的默认值, 和 {@code new HttpRequest()} 一致</li>
         * </ol>
         * </blockquote>
         * 
         * @return the http request
         * @since 4.5.6
         */
        public HttpRequest build(){
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.setUri(uri);
            httpRequest.setHttpMethodType(httpMethodType);
            httpRequest.setParamMap(paramMap);
            httpRequest.setHeaderMap(headerMap);
            httpRequest.setRequestBody(requestBody);
            httpRequest.setRequestByteArrayBody(requestByteArrayBody);
            httpRequest.setIsTrimUri(isTrimUri);
            httpRequest.setLogTraceContext(logTraceContext);
            return httpRequest;
        }
    }

    //---------------------------------------------------------------

    /**
     * 完整的请求路径.
     * 
     * @return the full encoded url
     */
    public String getFullEncodedUrl(){
        return ParamUtil.addParameterSingleValueMap(getUri(), paramMap, UTF8);
    }

    //---------------------------------------------------------------

    /**
     * 获得 请求的uri地址.
     * 
     * <p>
     * 如果uri是null,将返回empty
     * 如果uri中有空格,将会替换成%20
     * </p>
     *
     * @return the 请求的uri地址
     * @see <a href="https://github.com/venusdrogon/feilong-platform/issues/257">增加自动转义-请求参数放在路径中的配置</a>
     * @see <a href="https://github.com/venusdrogon/feilong-net/issues/66">HttpClient uri 中如果有空格会报错</a>
     */
    public String getUri(){
        return UriProcessor.process(uri, isTrimUri);
    }

    /**
     * 设置 请求的uri地址.
     *
     * @param uri
     *            the new 请求的uri地址
     */
    public void setUri(String uri){
        this.uri = uri;
    }

    //---------------------------------------------------------------

    /**
     * 获得 请求method 类型,默认 {@link HttpMethodType#GET}.
     *
     * @return the httpMethodType
     */
    public HttpMethodType getHttpMethodType(){
        return httpMethodType;
    }

    /**
     * 设置 请求method 类型,默认 {@link HttpMethodType#GET}.
     *
     * @param httpMethodType
     *            the httpMethodType to set
     */
    public void setHttpMethodType(HttpMethodType httpMethodType){
        this.httpMethodType = httpMethodType;
    }

    //---------------------------------------------------------------

    /**
     * 获得 http 请求 key-value 参数map.
     *
     * @return http 请求 key-value 参数map
     */
    public Map<String, String> getParamMap(){
        return paramMap;
    }

    /**
     * 设置 http 请求 key-value 参数map.
     *
     * @param paramMap
     *            http 请求 key-value 参数map
     */
    public void setParamMap(Map<String, String> paramMap){
        this.paramMap = paramMap;
    }

    //---------------------------------------------------------------

    /**
     * 获得 请求头 信息.
     *
     * @return the headerMap
     */
    public Map<String, String> getHeaderMap(){
        return headerMap;
    }

    /**
     * 设置 请求头 信息.
     *
     * @param headerMap
     *            the headerMap to set
     */
    public void setHeaderMap(Map<String, String> headerMap){
        this.headerMap = headerMap;
    }

    //---------------------------------------------------------------

    /**
     * 获得 请求正文,比如 webservice 可以传递 xml/json数据体.
     *
     * @return the requestBody
     * @see <a href="https://en.wikipedia.org/wiki/HTTP_message_body">HTTP_message_body</a>
     * @since 1.10.0
     */
    public String getRequestBody(){
        return requestBody;
    }

    /**
     * 设置 请求正文,比如 webservice 可以传递 xml/json数据体.
     *
     * @param requestBody
     *            the requestBody to set
     * @see <a href="https://en.wikipedia.org/wiki/HTTP_message_body">HTTP_message_body</a>
     * @since 1.10.0
     */
    public void setRequestBody(String requestBody){
        this.requestBody = requestBody;
    }

    //---------------------------------------------------------------

    /**
     * 是否uri trim.
     * 
     * <p>
     * 有时候程序员在配置uri 的时候,会误操作 uri 前后会多出空格, 这样会导致一些功能不work,要排查好久, <br>
     * 理论上uri前后是没有空格的, 所以默认为true ,会自动去除前后的空格; <br>
     * 如果真的有特殊需求, 可以设置为false
     * </p>
     * 
     * @param isTrimUri
     *            the isTrimUri to set
     * @since 3.0.10
     */
    public void setIsTrimUri(boolean isTrimUri){
        this.isTrimUri = isTrimUri;
    }

    /**
     * 是否uri trim.
     * 
     * <p>
     * 有时候程序员在配置uri 的时候,会误操作 uri 前后会多出空格, 这样会导致一些功能不work,要排查好久, <br>
     * 理论上uri前后是没有空格的, 所以默认为true ,会自动去除前后的空格; <br>
     * 如果真的有特殊需求, 可以设置为false
     * </p>
     * 
     * @return the isTrimUri
     * @since 3.0.10
     */
    public boolean getIsTrimUri(){
        return isTrimUri;
    }

    /**
     * 获得 二进制数据:请求体可以包含二进制数据，如文件、图像、音频等。 这些数据通常以字节流的形式传递。.
     *
     * @return the requestByteArrayBody
     * @since 4.0.1
     */
    public RequestByteArrayBody getRequestByteArrayBody(){
        return requestByteArrayBody;
    }

    /**
     * 设置 二进制数据:请求体可以包含二进制数据，如文件、图像、音频等。 这些数据通常以字节流的形式传递。.
     *
     * @param requestByteArrayBody
     *            the requestByteArrayBody to set
     * @since 4.0.1
     */
    public void setRequestByteArrayBody(RequestByteArrayBody requestByteArrayBody){
        this.requestByteArrayBody = requestByteArrayBody;
    }

    /**
     * 日志追踪上下文.
     * <p>
     * 所有的feilong http此次调用的log 都会拼上改日志字符串
     * </p>
     *
     * @return the logTraceContext
     * @since 4.0.8
     */
    public String getLogTraceContext(){
        return logTraceContext;
    }

    /**
     * 日志追踪上下文.
     * <p>
     * 所有的feilong http此次调用的log 都会拼上改日志字符串
     * </p>
     *
     * @param logTraceContext
     *            the logTraceContext to set
     * @since 4.0.8
     */
    public void setLogTraceContext(String logTraceContext){
        this.logTraceContext = logTraceContext;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }
}
