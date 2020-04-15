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
package com.feilong.servlet.http;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.core.TimeInterval;
import com.feilong.core.UncheckedIOException;
import com.feilong.io.entity.MimeType;

/**
 * {@link javax.servlet.http.HttpServletResponse HttpServletResponse} 工具类.
 * 
 * <h3>关于 {@link RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse) RequestDispatcher.forward} 和
 * {@link HttpServletResponse#sendRedirect(String) HttpServletResponse.sendRedirect}</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">{@link RequestDispatcher#forward(javax.servlet.ServletRequest, javax.servlet.ServletResponse) RequestDispatcher.forward}
 * </th>
 * <th align="left">{@link HttpServletResponse#sendRedirect(String) HttpServletResponse.sendRedirect}</th>
 * </tr>
 * <tr valign="top">
 * <td>只能将请求转发给同一个Web应用中的组件;</td>
 * <td>可以定向到应用程序外的其他资源.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>重定向后URL不会改变;</td>
 * <td>URL会改变</td>
 * </tr>
 * <tr valign="top">
 * <td>在服务器端内部将请求转发给另一个资源, 浏览器只知道发出请求并得到相应结果,并不知在服务器内部发生的转发行为</td>
 * <td>对浏览器的请求直接作出响应,响应的结果告诉浏览器重新发出对另外一个URL的访问请求</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>调用者与被调用者之间共享相同的request、response对象,它们属于同一个访问请求和相应过程;</td>
 * <td>调用者和被调用者使用各自的request、response对象,它们属于两个独立的访问请求和相应过程</td>
 * </tr>
 * <tr>
 * <td>适用于一次请求响应过程由Web程序内部的多个资源来协同完成, 需要在同一个Web程序内部资源之间跳转, 使用 {@link HttpServletRequest#setAttribute(String, Object)}方法将预处理结果传递给下一个资源.</td>
 * <td>适用于不同Web程序之间的重定向.</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>关于 {@link HttpServletResponse#sendRedirect(String)}:</h3>
 * 
 * <blockquote>
 * <p>
 * 用于生成302响应码和Location响应头,从而通知客户端重新访问Location响应头指定的URL.
 * </p>
 * 
 * <p>
 * 在 {@link HttpServletResponse#sendRedirect(String)}之后,<span style="color:red">应该紧跟一句return;</span> <br>
 * 我们已知道 {@link HttpServletResponse#sendRedirect(String)}是通过浏览器来做转向的,所以只有在页面处理完成后,才会有实际的动作.<br>
 * 既然您已要做转向了,那么后的输出更有什么意义呢？而且有可能会因为后面的输出导致转向失败.
 * </p>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.servlet.http.HttpServletResponse
 * @since 1.0.0
 */
public final class ResponseUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseUtil.class);

    /** Don't let anyone instantiate this class. */
    private ResponseUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 跳转.
     * 
     * <p>
     * {@link HttpServletResponse#sendRedirect(String)}方法用于生成302响应码和Location响应头,从而通知客户端重新访问Location响应头指定的URL.
     * </p>
     * 
     * <p>
     * 在 {@link HttpServletResponse#sendRedirect(String)}之后,<span style="color:red">应该紧跟一句return;</span>; <br>
     * 我们已知道 {@link HttpServletResponse#sendRedirect(String)}是通过浏览器来做转向的,所以只有在页面处理完成后,才会有实际的动作.<br>
     * 既然您已要做转向了,那么后的输出更有什么意义呢？而且有可能会因为后面的输出导致转向失败.
     * </p>
     * 
     * <p>
     * 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param response
     *            the response
     * @param url
     *            the redirect location URL
     * @see HttpServletResponse#sendRedirect(String)
     * @since 1.2.2
     */
    public static void sendRedirect(HttpServletResponse response,String url){
        Validate.notBlank(url, "url can't be blank!");

        //---------------------------------------------------------------
        try{
            LOGGER.debug("response sendRedirect to:[{}]", url);
            response.sendRedirect(url);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Sends an error response to the client using the specified status code and clears the buffer.
     * 
     * The server will preserve cookies and may clear or update any headers needed to serve the error page as a valid response.
     * 
     * If an error-page declaration has been made for the web application corresponding to the status code passed in, it will be served back
     * the error page
     * 
     * <p>
     * If the response has already been committed, this method throws an IllegalStateException.
     * After using this method, the response should be considered to be committed and should not be written to.
     *
     * @param response
     *            the response
     * @param errorStatusCode
     *            错误码
     * @since 1.11.5
     * @exception IllegalStateException
     *                If the response was committed before this method call
     */
    public static void sendError(HttpServletResponse response,int errorStatusCode){
        sendError(response, errorStatusCode, "");
    }

    /**
     * Sends an error response to the client using the specified
     * status and clears the buffer. The server defaults to creating the
     * response to look like an HTML-formatted server error page
     * containing the specified message, setting the content type
     * to "text/html". The server will preserve cookies and may clear or
     * update any headers needed to serve the error page as a valid response.
     * 
     * If an error-page declaration has been made for the web application
     * corresponding to the status code passed in, it will be served back in
     * preference to the suggested msg parameter and the msg parameter will
     * be ignored.
     * 
     * <p>
     * If the response has already been committed, this method throws
     * an IllegalStateException.
     * After using this method, the response should be considered
     * to be committed and should not be written to.
     *
     * @param response
     *            the response
     * @param errorStatusCode
     *            错误码
     * @param errorMessage
     *            the descriptive message
     * @since 1.11.5
     * @exception IllegalStateException
     *                If the response was committed before this method call
     */
    public static void sendError(HttpServletResponse response,int errorStatusCode,String errorMessage){
        Validate.notNull(errorMessage, "errorMessage can't be null!");

        try{
            LOGGER.debug("will sendError,errorStatusCode:[{}],errorMessage:[{}]", errorStatusCode, errorMessage);
            response.sendError(errorStatusCode, errorMessage);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置页面不缓存.
     * 
     * <p>
     * 当HTTP1.1服务器指定 CacheControl = no-cache时,浏览器就不会缓存该网页.<br>
     * 旧式 HTTP1.0 服务器不能使用 Cache-Control 标题
     * </p>
     * 
     * <h3>注意:</h3>
     * 
     * <blockquote>
     * <p>
     * 仅仅设置 Cache-Control:no-cache,如
     * </p>
     * 
     * <pre class="code">
     * {@code
     <meta http-equiv="Cache-Control" content="no-cache" />
     <meta http-equiv="Pragma" content="no-cache" />
     <meta http-equiv="Expires" content="0" />
     }
     * </pre>
     * 
     * <p>
     * 在 chrome 浏览器下面不起作用, 需要设置成 Cache-Control:no-cache,no-store,参见
     * <a href="http://stackoverflow.com/questions/5918408/google-chrome-cache">google-chrome-cache</a>
     * </p>
     * 
     * <p>
     * We see the same here: Chrome no cache需要 明确指定 no-store.如果不设置,那么 must-revalidate+ETag 没有效果, 并且 页面都会被 cached
     * while they should not be since Chrome does not check for a 304 at all.
     * </p>
     * 
     * </blockquote>
     * 
     * @param response
     *            HttpServletResponse
     * @see <a href="http://stackoverflow.com/questions/5918408/google-chrome-cache">google-chrome-cache</a>
     * @see <a href="https://bugs.chromium.org/p/chromium/issues/detail?id=28035">Cache doesnt adhear to No cache options(需要翻墙)</a>
     * @see <a href="https://code.google.com/p/chromium/codesearch#chromium/src/net/http/http_response_headers.cc&l=1082&rcl=1421094684">
     *      chrome http_response_headers源码</a>
     * @see HttpHeaders#CACHE_CONTROL
     * @see HttpHeaders#PRAGMA
     * @see HttpHeaders#EXPIRES
     */
    public static void setNoCacheHeader(HttpServletResponse response){
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache,no-store,max-age=0");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache,no-store");
        response.setDateHeader(HttpHeaders.EXPIRES, -1);
    }

    /**
     * 设置页面缓存.
     * 
     * <p>
     * 过期时间 = max-age 属性,单位<span style="color:red">秒</span>.
     * </p>
     * 
     * @param response
     *            the response
     * @param cacheTime
     *            过期时间 = max-age 属性,单位<span style="color:red">秒</span>,建议使用{@link TimeInterval}里面定义的常量.
     *            <p>
     *            if cacheTime {@code <=0} 表示不缓存<br>
     *            默认:0 不缓存
     *            </p>
     * 
     *            设置为int类型,int 最大值是{@link Integer#MAX_VALUE} 为 68.096259734906年,参见 {@link TimeInterval} ,绝对够用了
     * @see HttpHeaders#CACHE_CONTROL
     * @since 1.5.3
     * @since 1.11.0 if cacheTime {@code <=0} 表示不缓存
     */
    public static void setCacheHeader(HttpServletResponse response,int cacheTime){
        if (cacheTime <= 0){
            setNoCacheHeader(response);
        }else{
            response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=" + cacheTime);
        }
    }

    //   [start] PrintWriter

    /**
     * 以text的方式输出.
     *
     * @param response
     *            HttpServletResponse
     * @param text
     *            text字符串
     * @see #writeText(HttpServletResponse, Object, String)
     * @see com.feilong.io.entity.MimeType#TXT
     * @since 1.10.6
     */
    public static void writeText(HttpServletResponse response,Object text){
        writeText(response, text, UTF8);
    }

    /**
     * 以text的方式输出.
     *
     * @param response
     *            HttpServletResponse
     * @param text
     *            json字符串
     * @param characterEncoding
     *            编码<br>
     *            如果 <code>characterEncoding</code> 是null,抛出 {@link NullPointerException}<br>
     *            如果 <code>characterEncoding</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #write(HttpServletResponse, Object, String, String)
     * @see com.feilong.io.entity.MimeType#TXT
     * @since 1.10.6
     */
    public static void writeText(HttpServletResponse response,Object text,String characterEncoding){
        Validate.notBlank(characterEncoding, "characterEncoding can't be blank!");

        String contentType = MimeType.TXT.getMime() + ";charset=" + characterEncoding;
        write(response, text, contentType, characterEncoding);
    }

    //---------------------------------------------------------------

    /**
     * 以json的方式输出.
     *
     * @param response
     *            HttpServletResponse
     * @param json
     *            json字符串
     * @see #writeJson(HttpServletResponse, Object, String)
     * @see com.feilong.io.entity.MimeType#JSON
     * @since 1.0.9
     */
    public static void writeJson(HttpServletResponse response,Object json){
        writeJson(response, json, UTF8);
    }

    /**
     * 以json的方式输出.
     *
     * @param response
     *            HttpServletResponse
     * @param json
     *            json字符串
     * @param characterEncoding
     *            编码<br>
     *            如果 <code>characterEncoding</code> 是null,抛出 {@link NullPointerException}<br>
     *            如果 <code>characterEncoding</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #write(HttpServletResponse, Object, String, String)
     * @see com.feilong.io.entity.MimeType#JSON
     * @since 1.0.9
     */
    public static void writeJson(HttpServletResponse response,Object json,String characterEncoding){
        Validate.notBlank(characterEncoding, "characterEncoding can't be blank!");

        String contentType = MimeType.JSON.getMime() + ";charset=" + characterEncoding;
        write(response, json, contentType, characterEncoding);
    }

    //---------------------------------------------------------------

    /**
     * 输出.
     *
     * @param response
     *            HttpServletResponse
     * @param content
     *            相应内容
     * @see javax.servlet.ServletResponse#getWriter()
     * @see java.io.PrintWriter#print(Object)
     * @see java.io.PrintWriter#flush()
     * @see #write(HttpServletResponse, Object, String, String)
     */
    public static void write(HttpServletResponse response,Object content){
        String contentType = null;
        String characterEncoding = null;
        write(response, content, contentType, characterEncoding);
    }

    /**
     * 输出.
     *
     * @param response
     *            HttpServletResponse
     * @param content
     *            相应内容
     * @param contentType
     *            内容类型, 可以为null或者empty; <br>
     *            如果不为null或者empty,将会设置 {@link ServletResponse#setContentType(String)}
     * @param characterEncoding
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量;<br>
     *            可以为null或者empty; <br>
     *            如果不为null或者empty,将会设置 {@link ServletResponse#setCharacterEncoding(String)}
     * @see javax.servlet.ServletResponse#getWriter()
     * @see java.io.PrintWriter#print(Object)
     * @see java.io.PrintWriter#flush()
     * @since 1.0.9
     */
    public static void write(HttpServletResponse response,Object content,String contentType,String characterEncoding){
        //编码 需要在 getWriter之前设置
        if (isNotNullOrEmpty(contentType)){
            response.setContentType(contentType);
        }
        if (isNotNullOrEmpty(characterEncoding)){
            response.setCharacterEncoding(characterEncoding);
        }

        //---------------------------------------------------------------

        try{
            PrintWriter printWriter = response.getWriter();
            printWriter.print(content);
            printWriter.flush();

            //http://www.iteye.com/problems/56543
            //你是用了tomcat,jetty这样的容器,就不需要 printWriter.close()
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    // [end]

    //---------------------------------------------------------------

    /**
     * 获得 response info map for LOGGER.
     *
     * @param response
     *            the response
     * @return the response info map for log
     * @since 1.5.4
     */
    public static Map<String, Object> getResponseInfoMapForLog(HttpServletResponse response){
        Map<String, Object> map = newLinkedHashMap();
        map.put("response.getBufferSize()", response.getBufferSize());
        map.put("response.getCharacterEncoding()", response.getCharacterEncoding());
        map.put("response.getContentType()", response.getContentType());
        map.put("response headers", getResponseHeaderMap(response));
        map.put("response.getLocale()", "" + response.getLocale());
        return map;
    }

    /**
     * 获得 response header map.
     *
     * @param response
     *            the response
     * @return the response header map
     * @since 1.6.3
     */
    private static Map<String, Object> getResponseHeaderMap(HttpServletResponse response){
        Collection<String> headerNames = response.getHeaderNames(); //Servlet 3.0
        Map<String, Object> map = newHashMap();
        for (String headerName : headerNames){
            map.put(headerName, response.getHeader(headerName)); //Servlet 3.0
        }
        return map;
    }
}
