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

/**
 * Request 作用域中的属性.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.apache.catalina.Globals"
 * @see "org.springframework.web.util.WebUtils"
 * @since 1.0.4
 */
public final class RequestAttributes{

    //---------------------------include(Servlet 2.2)------------------------------------

    /**
     * <code>{@value}</code> <br>
     * Standard Servlet 2.3+ spec request attributes for include URI and paths.
     * <p>
     * If included via a RequestDispatcher, the current resource will see the originating request. <br>
     * Its own URI and paths are exposed as request attributes.
     * 
     * @see "org.springframework.web.util.WebUtils.INCLUDE_REQUEST_URI_ATTRIBUTE"
     */
    public static final String INCLUDE_REQUEST_URI     = "javax.servlet.include.request_uri";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the context path of the included servlet is stored on an included dispatcher request.
     * 
     * @see "org.springframework.web.util.WebUtils.INCLUDE_CONTEXT_PATH_ATTRIBUTE"
     */
    public static final String INCLUDE_CONTEXT_PATH    = "javax.servlet.include.context_path";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the servlet path of the included servlet is stored on an included dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE"
     */
    public static final String INCLUDE_SERVLET_PATH    = "javax.servlet.include.servlet_path";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the path info of the included servlet is stored on an included dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.INCLUDE_PATH_INFO_ATTRIBUTE"
     */
    public static final String INCLUDE_PATH_INFO       = "javax.servlet.include.path_info";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the query string of the included servlet is stored on an included dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.INCLUDE_QUERY_STRING_ATTRIBUTE"
     */
    public static final String INCLUDE_QUERY_STRING    = "javax.servlet.include.query_string";

    //---------------------------forward (Servlet 2.4)------------------------------------
    // 某些情况下一个forward()方法的目标servlet可能会需要知道真正原始的request URI

    /**
     * <code>{@value}</code><br>
     * Standard Servlet 2.4+ spec request attributes for forward URI and paths.
     * <p>
     * If forwarded to via a RequestDispatcher, the current resource will see its own URI and paths.<br>
     * The originating URI and paths are exposed as request attributes.<br>
     * The request attribute under which the original request URI is stored on an forwarded dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.FORWARD_REQUEST_URI_ATTRIBUTE"
     */
    public static final String FORWARD_REQUEST_URI     = "javax.servlet.forward.request_uri";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the original context path is stored on an forwarded dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.FORWARD_CONTEXT_PATH_ATTRIBUTE"
     */
    public static final String FORWARD_CONTEXT_PATH    = "javax.servlet.forward.context_path";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the original servlet path is stored on an forwarded dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.FORWARD_SERVLET_PATH_ATTRIBUTE"
     */
    public static final String FORWARD_SERVLET_PATH    = "javax.servlet.forward.servlet_path";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the original path info is stored on an forwarded dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.FORWARD_PATH_INFO_ATTRIBUTE"
     */
    public static final String FORWARD_PATH_INFO       = "javax.servlet.forward.path_info";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which the original query string is stored on an forwarded dispatcher request.
     *
     * @see "org.springframework.web.util.WebUtils.FORWARD_QUERY_STRING_ATTRIBUTE"
     */
    public static final String FORWARD_QUERY_STRING    = "javax.servlet.forward.query_string";

    //------------------------error---------------------------------------

    /**
     * <code>{@value}</code><br>
     * Standard Servlet 2.3+ spec request attributes for error pages.
     * <p>
     * To be exposed to JSPs that are marked as error pages,<br>
     * when forwarding to them directly rather than through the servlet container's error page resolution mechanism. <br>
     * The request attribute under which we forward an HTTP status code (as an object of type Integer) to an error page.
     * 
     * @see "org.springframework.web.util.WebUtils.ERROR_STATUS_CODE_ATTRIBUTE"
     */
    public static final String ERROR_STATUS_CODE       = "javax.servlet.error.status_code";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which we forward a Java exception type (as an object of type Class) to an error page.
     *
     * @see "org.springframework.web.util.WebUtils.ERROR_EXCEPTION_TYPE_ATTRIBUTE"
     */
    public static final String ERROR_EXCEPTION_TYPE    = "javax.servlet.error.exception_type";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which we forward an HTTP status message (as an object of type STring) to an error page.
     *
     * @see "org.springframework.web.util.WebUtils.ERROR_MESSAGE_ATTRIBUTE"
     */
    public static final String ERROR_MESSAGE           = "javax.servlet.error.message";

    /**
     * Servlet 2.3+ <code>{@value}</code><br>
     * The request attribute under which we forward a Java exception (as an object of type Throwable) to an error page.
     *
     * @see "org.springframework.web.util.WebUtils.ERROR_EXCEPTION_ATTRIBUTE"
     */
    public static final String ERROR_EXCEPTION         = "javax.servlet.error.exception";

    /**
     * Servlet 2.3+<code>{@value}</code><br>
     * The request attribute under which we forward the request URI (as an object of type String) of the page on which an error occurred.
     *
     * @see "org.springframework.web.util.WebUtils.ERROR_REQUEST_URI_ATTRIBUTE"
     */
    public static final String ERROR_REQUEST_URI       = "javax.servlet.error.request_uri";

    /**
     * <code>{@value}</code><br>
     * The request attribute under which we forward a servlet name to an error page.
     *
     * @see "org.springframework.web.util.WebUtils.ERROR_SERVLET_NAME_ATTRIBUTE"
     */
    public static final String ERROR_SERVLET_NAME      = "javax.servlet.error.servlet_name";

    //----------------------------Servlet API 2.3 -----------------------------------

    // Servlet API 2.3 also adds two new request attributes that can help a servlet make an informed decision about how to handle secure
    // HTTPS connections.
    // For requests made using HTTPS, the server will provide these new request attributes:

    /** A String representing the cipher suite used by HTTPS, if any. */
    public static final String REQUEST_CIPHER_SUITE    = "javax.servlet.request.cipher_suite";

    /** An Integer representing the bit size of the algorithm, if any. */
    public static final String REQUEST_KEY_SIZE        = "javax.servlet.request.key_size";

    //---------------------------------------------------------------
    /** ATTRIBUTE_REQUEST_X509CERTIFICATE. */
    public static final String REQUEST_X509CERTIFICATE = "javax.servlet.request.X509Certificate";

    /**
     * The ATTRIBUT e_ reques t_ ss l_ session
     * <a href="http://stackoverflow.com/questions/1422977/how-to-prevent-tomcat-session-hijacking">how-to-prevent-tomcat-session-hijacking
     * </a>}.
     */
    public static final String REQUEST_SSL_SESSION     = "javax.servlet.request.ssl_session";

    // 暂时还用不到下面的属性 "javax.servlet.context.tempdir" "javax.security.auth.subject"

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private RequestAttributes(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
}
