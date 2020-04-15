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

import static com.feilong.core.CharsetType.ISO_8859_1;
import static com.feilong.core.URIComponents.QUESTIONMARK;
import static com.feilong.core.URIComponents.SCHEME_HTTP;
import static com.feilong.core.URIComponents.SCHEME_HTTPS;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.core.util.SortUtil.sortMapByKeyAsc;
import static com.feilong.servlet.http.HttpHeaders.ORIGIN;
import static com.feilong.servlet.http.HttpHeaders.PROXY_CLIENT_IP;
import static com.feilong.servlet.http.HttpHeaders.REFERER;
import static com.feilong.servlet.http.HttpHeaders.USER_AGENT;
import static com.feilong.servlet.http.HttpHeaders.WL_PROXY_CLIENT_IP;
import static com.feilong.servlet.http.HttpHeaders.X_FORWARDED_FOR;
import static com.feilong.servlet.http.HttpHeaders.X_REAL_IP;
import static com.feilong.servlet.http.HttpHeaders.X_REQUESTED_WITH;
import static com.feilong.servlet.http.HttpHeaders.X_REQUESTED_WITH_VALUE_AJAX;
import static java.util.Collections.emptyMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.core.UncheckedIOException;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.StringUtil;
import com.feilong.core.util.EnumerationUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.io.ReaderUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.entity.RequestLogSwitch;

/**
 * {@link javax.servlet.http.HttpServletRequest HttpServletRequest}工具类.
 * 
 * <h3>{@link HttpServletRequest#getRequestURI() getRequestURI()} 和 {@link HttpServletRequest#getRequestURL() getRequestURL()}:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">返回值</th>
 * </tr>
 * <tr valign="top">
 * <td><span style="color:red">request.getRequestURI()</span></td>
 * <td>/feilong/requestdemo.jsp</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td><span style="color:red">request.getRequestURL()</span></td>
 * <td>http://localhost:8080/feilong/requestdemo.jsp</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * <h3>关于从request中获得相关路径和url:</h3>
 * 
 * <blockquote>
 * 
 * <ol>
 * <li>getServletContext().getRealPath("/") 后包含当前系统的文件夹分隔符(windows系统是"\",linux系统是"/"),而getPathInfo()以"/"开头.</li>
 * <li>getPathInfo()与getPathTranslated()在servlet的url-pattern被设置为/*或/aa/*之类的pattern时才有值,其他时候都返回null.</li>
 * <li>在servlet的url-pattern被设置为*.xx之类的pattern时,getServletPath()返回的是getRequestURI()去掉前面ContextPath的剩余部分.</li>
 * </ol>
 * 
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link HttpServletRequest#getContextPath()}</td>
 * <td>{@link HttpServletRequest#getContextPath()}</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link HttpServletRequest#getPathInfo()}</td>
 * <td>Returns any extra path information associated with the URL the client sent when it made this request. <br>
 * Servlet访问路径之后,QueryString之前的中间部分</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link HttpServletRequest#getServletPath()}</td>
 * <td>web.xml中定义的Servlet访问路径</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link HttpServletRequest#getPathTranslated()}</td>
 * <td>等于getServletContext().getRealPath("/") + getPathInfo()</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link HttpServletRequest#getRequestURI()}</td>
 * <td>等于getContextPath() + getServletPath() + getPathInfo()</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link HttpServletRequest#getRequestURL()}</td>
 * <td>等于getScheme() + "://" + getServerName() + ":" + getServerPort() + getRequestURI()</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link HttpServletRequest#getQueryString()}</td>
 * <td>{@code &}之后GET方法的参数部分<br>
 * Returns the query string that is contained in the request URL after the path. <br>
 * This method returns null if the URL does not have a query string. <br>
 * Same as the value of the CGI variable QUERY_STRING.</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * 
 * <h3><a href="http://tomcat.apache.org/whichversion.html">Apache Tomcat Versions:</a></h3>
 * 
 * <p>
 * Apache Tomcat™ is an open source software implementation of the Java Servlet and JavaServer Pages technologies. <br>
 * Different versions of Apache Tomcat are available for different versions of the Servlet and JSP specifications. <br>
 * The mapping between the specifications and the respective Apache Tomcat versions is:
 * </p>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">Servlet Spec</th>
 * <th align="left">JSP Spec</th>
 * <th align="left">EL Spec</th>
 * <th align="left">WebSocket Spec</th>
 * <th align="left">Apache Tomcat version</th>
 * <th align="left">Actual release revision</th>
 * <th align="left">Support Java Versions</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>4.0</td>
 * <td>TBD (2.4?)</td>
 * <td>TBD (3.1?)</td>
 * <td>TBD (1.2?)</td>
 * <td>9.0.x</td>
 * <td>9.0.0.M8 (alpha)</td>
 * <td>8 and later</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>3.1</td>
 * <td>2.3</td>
 * <td>3.0</td>
 * <td>1.1</td>
 * <td>8.5.x</td>
 * <td>8.5.3</td>
 * <td>7 and later</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>3.1</td>
 * <td>2.3</td>
 * <td>3.0</td>
 * <td>1.1</td>
 * <td>8.0.x (superseded)</td>
 * <td>8.0.35 (superseded)</td>
 * <td>7 and later</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>3.0</td>
 * <td>2.2</td>
 * <td>2.2</td>
 * <td>1.1</td>
 * <td>7.0.x</td>
 * <td>7.0.70</td>
 * <td>6 and later<br>
 * (7 and later for WebSocket)</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>2.5</td>
 * <td>2.1</td>
 * <td>2.1</td>
 * <td>N/A</td>
 * <td>6.0.x</td>
 * <td>6.0.45</td>
 * <td>5 and later</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>2.4</td>
 * <td>2.0</td>
 * <td>N/A</td>
 * <td>N/A</td>
 * <td>5.5.x (archived)</td>
 * <td>5.5.36 (archived)</td>
 * <td>1.4 and later</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>2.3</td>
 * <td>1.2</td>
 * <td>N/A</td>
 * <td>N/A</td>
 * <td>4.1.x (archived)</td>
 * <td>4.1.40 (archived)</td>
 * <td>1.3 and later</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>2.2</td>
 * <td>1.1</td>
 * <td>N/A</td>
 * <td>N/A</td>
 * <td>3.3.x (archived)</td>
 * <td>3.3.2 (archived)</td>
 * <td>1.1 and later</td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see RequestAttributes
 * @see RequestLogSwitch
 * @since 1.0.0
 */
public final class RequestUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                            = LoggerFactory.getLogger(RequestUtil.class);

    //---------------------------------------------------------------

    /**
     * 存放到 request 作用域中的 requestbody 名字.
     * 
     * @since 1.14.2
     * @see #getRequestBody(HttpServletRequest)
     */
    private static String       REQUEST_BODY_SCOPE_ATTRIBUTE_NAME = RequestUtil.class.getName() + ".REQUEST_BODY";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private RequestUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 静态资源的后缀.
     * <code>{@value}</code>.
     *
     * @see com.feilong.io.entity.MimeType
     * @since 1.12.0
     */
    private static final String[] STATIC_RESOURCE_SUFFIX = toArray(
                    ".bmp",
                    ".jpe",
                    ".jpg",
                    ".jpeg",
                    ".png",
                    ".gif",
                    ".ico",
                    ".js",
                    ".css",
                    //  ".html",  since 1.12.4 排除, 很多 restful 的地址 会伪装成 htm和html ,导致误判断
                    //  ".htm", since 1.12.4 排除, 很多 restful 的地址 会伪装成 htm和html ,导致误判断
                    ".xml",
                    ".swf",
                    ".woff",
                    ".woff2",
                    ".ttf",
                    ".mp3",
                    ".zip",
                    ".tar");

    //---------------------------------------------------------------

    /**
     * 判断请求是否是静态资源.
     *
     * @param requestURI
     *            the request URI
     * @return 如果 <code>requestURI</code> 是null或者empty,返回 false<br>
     *         如果 <code>requestURI</code> 不包含.,返回 false<br>
     *         其他循环 {@link #STATIC_RESOURCE_SUFFIX} ,忽视大小写判断后缀<br>
     * @see HttpServletRequest#getRequestURI()
     * @since 1.12.0
     */
    public static boolean isStaticResource(String requestURI){
        if (isNullOrEmpty(requestURI)){
            return false;
        }

        //---------------------------------------------------------------
        if (!requestURI.contains(".")){
            return false;
        }
        //---------------------------------------------------------------
        for (String uriSuffix : STATIC_RESOURCE_SUFFIX){
            //XXX 性能
            if (StringUtils.endsWithIgnoreCase(requestURI, uriSuffix)){
                return true;
            }
        }
        return false;
    }

    //---------------------------------------------------------------

    /**
     * 判断传入的<code>method</code> 是否在 支持的<code>supportHttpMethods</code>数组中.
     * 
     * @param supportHttpMethods
     *            支持的method 数组
     * @param method
     *            the method
     * @return 如果 <code>method</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>method</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>supportHttpMethods</code> 是null或者empty,返回 false <br>
     *         循环 supportHttpMethods, 忽视大小写判断和 method 是否equalsIgnoreCase, 如果是返回true,否则false
     * @since 1.12.1
     */
    public static boolean isSupportMethod(String[] supportHttpMethods,String method){
        Validate.notBlank(method, "method can't be blank!");

        //---------------------------------------------------------------
        //null 或者 empty 表示没有一个 method 支持的,不过滤
        if (isNullOrEmpty(supportHttpMethods)){
            return false;
        }

        //---------------------------------------------------------------
        for (String supportHttpMethod : supportHttpMethods){
            //如果当前的请求 method ,在支持的列表里面, 那么表示要过滤
            if (StringUtils.equalsIgnoreCase(supportHttpMethod, method)){
                return true;
            }
        }
        return false;
    }

    //-------------------------是否包含--------------------------------------

    /**
     * 请求路径中是否包含某个参数名称 (注意:这是判断是否包含参数,而不是判断参数值是否为空).
     *
     * @param request
     *            请求
     * @param paramName
     *            参数名称
     * @return 包含该参数返回true,不包含返回false <br>
     *         如果 <code>paramName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>paramName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see com.feilong.core.util.EnumerationUtil#contains(Enumeration, Object)
     * @since 1.4.0
     */
    public static boolean containsParam(HttpServletRequest request,String paramName){
        Validate.notBlank(paramName, "paramName can't be null/empty!");
        return EnumerationUtil.contains(request.getParameterNames(), paramName);
    }

    //---------------------------------------------------------------

    /**
     * 获得参数map(结果转成了key自然排序的TreeMap).
     * 
     * <p>
     * 此方式会将tomcat返回的map 转成TreeMap 返回,便于log; 也可以<span style="color:red">对这个返回的map进行操作</span>
     * </p>
     * 
     * <h3>tomcat getParameterMap() <span style="color:red">locked</span>(只能读):</h3>
     * 
     * <blockquote>
     * 注意:tomcat 默认实现,返回的是 {@code org.apache.catalina.util#ParameterMap<K, V>},tomcat返回之前,会将此map的状态设置为locked,<br>
     * 
     * <p>
     * 不像普通的map数据一样可以修改.<br>
     * 这是因为服务器为了实现一定的安全规范,所作的限制,WebLogic,Tomcat,Resin,JBoss等服务器均实现了此规范.
     * </p>
     * 
     * 不能做以下的map操作:
     * 
     * <ul>
     * <li>{@link Map#clear()}</li>
     * <li>{@link Map#put(Object, Object)}</li>
     * <li>{@link Map#putAll(Map)}</li>
     * <li>{@link Map#remove(Object)}</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * @param request
     *            the request
     * @return the parameter map
     * @see "org.apache.catalina.connector.Request#getParameterMap()"
     */
    public static Map<String, String[]> getParameterMap(HttpServletRequest request){
        // http://localhost:8888/s.htm?keyword&a=
        // 这种链接  map key 会是 keyword,a 值都是空

        return sortMapByKeyAsc(request.getParameterMap()); // servlet 3.0 此处返回类型的是 泛型数组 Map<String, String[]>
    }

    /**
     * 获得请求参数和单值map.
     * 
     * <p>
     * 由于调用的 {@link #getParameterMap(HttpServletRequest)}结果是<span style="color:green">有序的</span>,
     * 此方法返回的map也是<span style="color:green">有序的</span>
     * </p>
     * 
     * <p>
     * 由于j2ee{@link ServletRequest#getParameterMap()}返回的map值是数组形式,<br>
     * 对于一些确认是单值的请求(比如支付宝notify/return request),不便后续处理
     * </p>
     * 
     * @param request
     *            the request
     * @return the parameter single value map
     * @see #getParameterMap(HttpServletRequest)
     * @see MapUtil#toSingleValueMap(Map)
     * @since 1.2.0
     */
    public static Map<String, String> getParameterSingleValueMap(HttpServletRequest request){
        return MapUtil.toSingleValueMap(getParameterMap(request));
    }

    //---------------------------------------------------------------

    /**
     * 将 {@link HttpServletRequest} 相关属性,数据转成json格式 以便log显示(目前仅作log使用).
     * 
     * <p>
     * 默认使用 {@link RequestLogSwitch#NORMAL}
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * Map{@code <String, Object>} requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request);}
     * LOGGER.debug("class:[{}],request info:{}", getClass().getSimpleName(), JsonUtil.format(requestInfoMapForLog);
     * </pre>
     * 
     * 输出结果:
     * 
     * <pre class="code">
     * 19:28:37 DEBUG (AbstractWriteContentTag.java:63) execute() - class:[HttpConcatTag],request info: {
     * "requestFullURL": "/member/login.htm?a=b",
     * "request.getMethod": "GET",
     * "parameterMap": {"a": ["b"]}
     * }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param request
     *            the request
     * @return the request string for log
     * @see RequestLogSwitch
     * @see #getRequestInfoMapForLog(HttpServletRequest, RequestLogSwitch)
     */
    public static Map<String, Object> getRequestInfoMapForLog(HttpServletRequest request){
        return getRequestInfoMapForLog(request, RequestLogSwitch.NORMAL);
    }

    /**
     * 将request 相关属性,数据转成json格式 以便log显示(目前仅作log使用).
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * RequestLogSwitch requestLogSwitch = RequestLogSwitch.NORMAL;
     * Map{@code <String, Object>} requestInfoMapForLog = RequestUtil.getRequestInfoMapForLog(request, requestLogSwitch);
     * LOGGER.debug("class:[{}],request info:{}", getClass().getSimpleName(), JsonUtil.format(requestInfoMapForLog);
     * </pre>
     * 
     * 输出结果:
     * 
     * <pre class="code">
     * 19:28:37 DEBUG (AbstractWriteContentTag.java:63) execute() - class:[HttpConcatTag],request info: {
     * "requestFullURL": "/member/login.htm?a=b",
     * "request.getMethod": "GET",
     * "parameterMap": {"a": ["b"]}
     * }
     * </pre>
     * 
     * </blockquote>
     * 
     * @param request
     *            the request
     * @param requestLogSwitch
     *            the request log switch
     * @return the request string for log
     * @see RequestLogBuilder#RequestLogBuilder(HttpServletRequest, RequestLogSwitch)
     */
    public static Map<String, Object> getRequestInfoMapForLog(HttpServletRequest request,RequestLogSwitch requestLogSwitch){
        return new RequestLogBuilder(request, requestLogSwitch).build();
    }

    //-------------------------url参数相关 getAttribute--------------------------------------

    // [start] url参数相关

    /**
     * 获得 attribute.
     *
     * @param <T>
     *            the generic type
     * @param request
     *            the request
     * @param attributeName
     *            属性名称
     * @return 如果 <code>attributeName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>attributeName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see javax.servlet.ServletRequest#getAttribute(String)
     * @since 1.3.0
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request,String attributeName){
        Validate.notBlank(attributeName, "attributeName can't be null/empty!");
        return (T) request.getAttribute(attributeName);
    }

    /**
     * 取到request里面的属性值,<span style="color:green">转换类型成指定的参数 <code>klass</code></span>.
     *
     * @param <T>
     *            the generic type
     * @param request
     *            请求
     * @param name
     *            属性名称
     * @param klass
     *            需要被转换成的类型
     * @return the attribute
     * @see com.feilong.core.bean.ConvertUtil#convert(Object, Class)
     * @see #getAttribute(HttpServletRequest, String)
     * @since 1.3.0
     */
    public static <T> T getAttribute(HttpServletRequest request,String name,Class<T> klass){
        Object value = getAttribute(request, name);
        return ConvertUtil.convert(value, klass);
    }

    //---------------------------------------------------------------

    /**
     * 获得请求的?部分前面的地址.
     * <p>
     * <span style="color:red">自动识别 request 是否 forword</span>,如果是forword过来的,那么取 {@link RequestAttributes#FORWARD_REQUEST_URI}变量
     * </p>
     * 
     * <pre class="code">
     * 如:http://localhost:8080/feilong/requestdemo.jsp?id=2
     * <b>返回:</b>http://localhost:8080/feilong/requestdemo.jsp
     * </pre>
     * 
     * 注:
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">返回值</th>
     * </tr>
     * <tr valign="top">
     * <td><span style="color:red">request.getRequestURI()</span></td>
     * <td>/feilong/requestdemo.jsp</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td><span style="color:red">request.getRequestURL()</span></td>
     * <td>http://localhost:8080/feilong/requestdemo.jsp</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * @param request
     *            the request
     * @return 获得请求的?部分前面的地址
     */
    public static String getRequestURL(HttpServletRequest request){
        String forwardRequestUri = getAttribute(request, RequestAttributes.FORWARD_REQUEST_URI);
        return isNotNullOrEmpty(forwardRequestUri) ? forwardRequestUri : request.getRequestURL().toString();
    }

    /**
     * Return the servlet path for the given request, detecting an include request URL if called within a RequestDispatcher include.
     * 
     * @param request
     *            current HTTP request
     * @return the servlet path
     */
    public static String getOriginatingServletPath(HttpServletRequest request){
        String servletPath = getAttribute(request, RequestAttributes.FORWARD_SERVLET_PATH);
        return isNotNullOrEmpty(servletPath) ? servletPath : request.getServletPath();
    }

    /**
     * 获得请求的全地址.
     * 
     * <ul>
     * <li>如果request不含queryString,直接返回 requestURL(比如post请求)</li>
     * <li>如果request含queryString,直接返回 requestURL+编码后的queryString</li>
     * </ul>
     * 
     * @param request
     *            the request
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     * @return 如:http://localhost:8080/feilong/requestdemo.jsp?id=2
     */
    public static String getRequestFullURL(HttpServletRequest request,String charsetType){
        String requestURL = getRequestURL(request);
        String queryString = request.getQueryString();
        return isNullOrEmpty(queryString) ? requestURL : requestURL + QUESTIONMARK + decodeISO88591String(queryString, charsetType);
    }

    //---------------------------------------------------------------

    /**
     * {@link CharsetType#ISO_8859_1} 的方式去除乱码.
     * 
     * <p>
     * {@link CharsetType#ISO_8859_1} 是JAVA网络传输使用的标准 字符集
     * </p>
     * 
     * <h3>关于URI Encoding</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>tomcat server.xml Connector URIEncoding="UTF-8"</li>
     * <li>{@code <fmt:requestEncoding value="UTF-8"/>}</li>
     * </ul>
     * </blockquote>
     *
     * @param str
     *            字符串
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     * @return 如果 <code>str</code> 是null或者empty,返回 {@link StringUtils#EMPTY}<br>
     * @see "org.apache.commons.codec.net.URLCodec#encode(String, String)"
     * @see "org.apache.taglibs.standard.tag.common.fmt.RequestEncodingSupport"
     * @see "org.apache.catalina.filters.SetCharacterEncodingFilter"
     * @since 1.7.3 move from feilong-core
     * @deprecated may delete
     */
    @Deprecated
    public static String decodeISO88591String(String str,String charsetType){
        return StringUtil.newString(StringUtil.getBytes(str, ISO_8859_1), charsetType);
    }

    /**
     * scheme+serverName+port+getContextPath.
     * 
     * <p>
     * 区分 http 和https.
     * <p>
     * 
     * @param request
     *            the request
     * @return 如:http://localhost:8080/feilong/
     * @see "org.apache.catalina.connector.Request#getRequestURL()"
     * @see "org.apache.catalina.realm.RealmBase#hasUserDataPermission(Request, Response, SecurityConstraint[])"
     * @see javax.servlet.http.HttpUtils#getRequestURL(HttpServletRequest)
     */
    public static String getServerRootWithContextPath(HttpServletRequest request){
        String scheme = request.getScheme();
        int port = request.getServerPort() < 0 ? 80 : request.getServerPort();// Work around java.net.URL bug
        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();
        sb.append(scheme);
        sb.append("://");
        sb.append(request.getServerName());

        if ((scheme.equals(SCHEME_HTTP) && (port != 80)) || (scheme.equals(SCHEME_HTTPS) && (port != 443))){
            sb.append(':');
            sb.append(port);
        }

        sb.append(request.getContextPath());
        return sb.toString();
    }

    //---------------------------------------------------------------

    // [end]

    /**
     * 用于将请求转发到 {@link RequestDispatcher} 对象封装的资源,Servlet程序在调用该方法转发之前可以对请求进行前期预处理.
     * 
     * <p style="color:red">
     * 该方法将 checked exception 转成了 unchecked exception,方便书写和调用
     * </p>
     * 
     * <p>
     * Forwards a request from a servlet to another resource (servlet, JSP file, or HTML file) on the server.<br>
     * This method allows one servlet to do preliminary processing of a request and another resource to generate the response.
     * </p>
     * 
     * <p>
     * For a <code>RequestDispatcher</code> obtained via <code>getRequestDispatcher()</code>, the <code>ServletRequest</code> object has its
     * path elements and parameters adjusted to match the path of the target resource.
     * </p>
     * 
     * <p>
     * <code>forward</code> <span style="color:red">should be called before the response has been committed to the client</span> (before
     * response body output has been flushed). <br>
     * 
     * If the response already has been committed, this method throws an <code>IllegalStateException</code>. Uncommitted output in
     * the response buffer is automatically cleared before the forward.
     * </p>
     * 
     * <p>
     * 如果 <code>path</code> 是null,抛出 {@link NullPointerException}<br>
     * </p>
     * 
     * @param path
     *            a String specifying the pathname to the resource.<br>
     *            If it is relative, it must be relative against the current servlet.The
     *            pathname specified may be relative, although it cannot extend outside the current servlet context. <br>
     *            If the path begins with a "/" it is interpreted as relative to the current context root.<br>
     *            This method returns null if the servlet container cannot return a RequestDispatcher.<br>
     *            如果 <code>path</code> 是null,抛出 {@link NullPointerException}<br>
     * @param request
     *            a {@link ServletRequest} object that represents the request the client makes of the servlet
     * @param response
     *            a {@link ServletResponse} object,that represents the response the servlet returns to the client
     * @since 1.2.2
     */
    public static void forward(String path,HttpServletRequest request,HttpServletResponse response){
        //since 2.0.1
        Validate.notNull(path, "path can't be null!");

        LOGGER.debug("will forward to path:[{}]", path);

        try{
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
            requestDispatcher.forward(request, response);
        }catch (ServletException | IOException e){
            throw new RequestException("when forward to:" + path, e);
        }
    }

    /**
     * 用于将 {@link RequestDispatcher} 对象封装的资源内容作为当前响应内容的一部分包含进来,从而实现可编程服务器的服务器端包含功能.
     * 
     * <p style="color:red">
     * 该方法将 checked exception 转成了 unchecked exception,方便书写和调用
     * </p>
     * 
     * <p>
     * Includes the content of a resource (servlet, JSP page,HTML file) in the response. <br>
     * In essence, this method enables programmatic server-side includes.
     * </p>
     * 
     * <p>
     * 注:被包含的Servlet程序不能改变响应信息的状态码和响应头,如果里面包含这样的语句将被忽略.<br>
     * The {@link ServletResponse} object has its path elements and parameters remain unchanged from the caller's. <br>
     * The included servlet cannot change the response status code or set headers; any attempt to make a change is ignored.
     * </p>
     * 
     * @param path
     *            a String specifying the pathname to the resource.<br>
     *            If it is relative, it must be relative against the current servlet.The
     *            pathname specified may be relative, although it cannot extend outside the current servlet context. <br>
     *            If the path begins with a "/" it is interpreted as relative to the current context root.<br>
     *            This method returns null if the servlet container cannot return a RequestDispatcher.<br>
     *            如果 <code>path</code> 是null,抛出 {@link NullPointerException}<br>
     * @param request
     *            a {@link ServletRequest} object,that contains the client's request
     * @param response
     *            a {@link ServletResponse} object,that contains the servlet's response
     * @see javax.servlet.RequestDispatcher#include(ServletRequest, ServletResponse)
     * @see "org.springframework.web.servlet.ResourceServlet"
     * @since 1.2.2
     */
    public static void include(String path,HttpServletRequest request,HttpServletResponse response){
        //since 2.0.1
        Validate.notNull(path, "path can't be null!");

        LOGGER.debug("will include to path:[{}]", path);

        try{
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(path);
            requestDispatcher.include(request, response);
        }catch (ServletException | IOException e){
            throw new RequestException("when include:" + path, e);
        }
    }

    //--------------------------Header-------------------------------------

    /**
     * 获得客户端真实ip地址.
     * 
     * @param request
     *            the request
     * @return 获得客户端ip地址
     * @see "org.apache.catalina.valves.RemoteIpValve"
     * @see <a href="http://distinctplace.com/infrastructure/2014/04/23/story-behind-x-forwarded-for-and-x-real-ip-headers/">Story behind
     *      X-Forwarded-For and X-Real-IP headers</a>
     * @see <a href="http://lavafree.iteye.com/blog/1559183">nginx做负载CDN加速获取端真实ip</a>
     */
    public static String getClientIp(HttpServletRequest request){
        // WL-Proxy-Client-IP=215.4.1.29
        // Proxy-Client-IP=215.4.1.29
        // X-Forwarded-For=215.4.1.29

        //ip header可控制的, 以后如果有新增加在这里(比如多CDN 可能是cdn_real_ip),而不是通过传参的形式
        //这样做的好处是,对开发透明
        String[] ipHeaderNames = { X_FORWARDED_FOR, X_REAL_IP, PROXY_CLIENT_IP, WL_PROXY_CLIENT_IP };
        return getClientIp(request, ipHeaderNames);
    }

    /**
     * 获得客户端真实ip地址.
     * 
     * @param request
     * @param ipHeaderNames
     * @return
     * @since 2.0.1
     */
    private static String getClientIp(HttpServletRequest request,String...ipHeaderNames){
        String ipAddress = "";

        //---------------------------------------------------------------
        Map<String, String> map = newLinkedHashMap();
        //先在代理里面找一找
        for (String ipHeaderName : ipHeaderNames){
            String ipHeaderValue = request.getHeader(ipHeaderName);//The header name is case insensitive (不区分大小写)
            map.put(ipHeaderName, ipHeaderValue);
            if (isNotNullOrEmpty(ipHeaderValue) && !"unknown".equalsIgnoreCase(ipHeaderValue)){
                ipAddress = ipHeaderValue;
                break;
            }
        }

        //----------------------getRemoteAddr-----------------------------------------
        //如果都没有,那么读取 request.getRemoteAddr()
        if (isNullOrEmpty(ipAddress)){
            ipAddress = request.getRemoteAddr();
            map.put("request.getRemoteAddr()", ipAddress);
        }

        //---------------------------------------------------------------

        // 对于通过多个代理的情况,第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.indexOf(',') > 0){
            //如果通过了多级反向代理的话,X-Forwarded-For的值并不止一个,而是一串ip值,取第一个非unknown的有效IP字符串. 
            ipAddress = ipAddress.substring(0, ipAddress.indexOf(','));
            map.put("firstIp", ipAddress);
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("client real ips:{}", JsonUtil.format(map));
        }
        return ipAddress;
    }

    //---------------------------------------------------------------

    /**
     * User Agent中文名为用户代理,简称 UA.
     * 
     * <p>
     * 它是一个特殊字符串头,使得服务器能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等.
     * </p>
     * 
     * @param request
     *            the request
     * @return 如果request没有指定名称 {@link HttpHeaders#USER_AGENT} 的header,那么返回null
     * @see HttpHeaders#USER_AGENT
     */
    public static String getHeaderUserAgent(HttpServletRequest request){
        return request.getHeader(USER_AGENT);
    }

    /**
     * 获得上个请求的URL.
     * 
     * <p>
     * referer是浏览器在用户提交请求当前页面中的一个链接时,将当前页面的URL放在头域中提交给服务端的,如当前页面为a.html,
     * 它里面有一个b.html的链接,当用户要访问b.html时浏览器就会把a.html作为referer发给服务端.
     * </p>
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * 请用于常规请求,必须走http/https协议才有值,javascript跳转无效
     * 
     * 也就是说要通过&lt;a href=&quot;url&quot;&gt;sss&lt;/a&gt;标记才能获得那个值
     * 而通过改变location或是&lt;a href=&quot;javascript:location='url'&quot;&gt;sss&lt;/a&gt;都是得不到那个值得
     * </blockquote>
     * 
     * <h3>不能正常取值的情况:</h3>
     * <blockquote>
     * <ol>
     * <li>从收藏夹链接</li>
     * <li>用Window.open打开地址或者自定义的地址</li>
     * <li>利用Jscript的location.href or location.replace()</li>
     * <li>在浏览器直接输入地址</li>
     * <li>Response.Redirect</li>
     * <li>Response.AddHeader或{@code <meta http-equiv=refresh>}转向</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>关于referer的获取问题</h3>
     * 
     * <blockquote>
     * 在上一页面做跳转操作，可以在下一页面获得上一页面的Referer从而判断页面的来路。
     * <br>
     * 
     * 目前web开发有以下几种页面跳转方式:
     * 
     * 
     * <blockquote>
     * 
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">方式</th>
     * <th align="left">是否支持跨域</th>
     * <th align="left">是否可以取到referer</th>
     * <th align="left">代码示例</th>
     * </tr>
     * 
     * <tr valign="top">
     * <td>{@link RequestDispatcher} 跳转</td>
     * <td>不支持跨域</td>
     * <td>目的页面无法取得referer</td>
     * <td>
     * 
     * <pre>
     * RequestDispatcher rd = request.getRequestDispatcher(url);
     * rd.forward(request, response);
     * </pre>
     * 
     * </td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>response.setHeader</td>
     * <td>支持跨域</td>
     * <td>目的页面无法取得referer</td>
     * <td>
     * 
     * <pre>
     * response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
     * response.setHeader("Location", url);
     * </pre>
     * 
     * </td>
     * </tr>
     * 
     * 
     * <tr valign="top">
     * <td>response.sendRedirect(url)</td>
     * <td>支持跨域</td>
     * <td>目的页面无法取得referer</td>
     * <td></td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>用form表单, post方法提交</td>
     * <td>既可跨域</td>
     * <td>又能得到referer</td>
     * <td>并且支持form表单的action属性中url使用参数</td>
     * </tr>
     * 
     * <tr valign="top">
     * <td>用form表单, get方法提交</td>
     * <td>既可跨域</td>
     * <td>又能得到referer</td>
     * <td>
     * 
     * 但不支持form表单的action属性中url使用参数,<br>
     * 这种方式不会将action的值后面添加"?"提交到web服务器。<br>
     * 如果actio中的url就含有"?"则会将"?"后的数据忽略掉。而post方式不存在这个问题
     * 
     * </td>
     * </tr>
     * 
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>使用html中href来跳转页面</td>
     * <td></td>
     * <td>目的页面可以获得referer</td>
     * <td></td>
     * </tr>
     * </table>
     * 
     * </blockquote>
     * 
     * </blockquote>
     * 
     * @param request
     *            the request
     * @return 如果request没有指定名称 {@link HttpHeaders#REFERER} 的header,那么返回null
     * @see HttpHeaders#REFERER
     * @see <a href="http://www.blogjava.net/jiangjf/archive/2014/01/27/201339.html">关于referer的获取问题</a>
     */
    public static String getHeaderReferer(HttpServletRequest request){
        return request.getHeader(REFERER);
    }

    /**
     * 1、Origin字段里只包含是谁发起的请求,并没有其他信息 (通常情况下是方案,主机和活动文档URL的端口). <br>
     * 跟Referer不一样的是,Origin字段并没有包含涉及到用户隐私的URL路径和请求内容,这个尤其重要. <br>
     * 2、Origin字段只存在于POST请求,而Referer则存在于所有类型的请求.
     * 
     * @param request
     *            the request
     * @return 如果request没有指定名称 {@link HttpHeaders#ORIGIN} 的header,那么返回null
     * @see HttpHeaders#ORIGIN
     */
    public static String getHeaderOrigin(HttpServletRequest request){
        return request.getHeader(ORIGIN);
    }

    //---------------------------------------------------------------

    /**
     * 判断一个请求是否是微信浏览器的请求.
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>在iPhone下，返回<br>
     * Mozilla/5.0 (iPhone; CPU iPhone OS 5_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Mobile/9B176 MicroMessenger/4.3.2</li>
     * 
     * <li>在Android下，返回<br>
     * Mozilla/5.0 (Linux; U; Android 2.3.6; zh-cn; GT-S5660 Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile
     * Safari/533.1 MicroMessenger/4.5.255
     * </li>
     * </ol>
     * 不难发现微信浏览器为 MicroMessenger,并且有版本号,也可以判断手机类型为iPhone还是Android
     * </blockquote>
     *
     * @param request
     *            the request
     * @return 如果没有ua,那么返回false;否则判断ua 里面是否包含 micromessenger 值
     * @see <a href="http://www.cnblogs.com/dengxinglin/archive/2013/05/29/3106004.html">微信浏览器的HTTP_USER_AGENT</a>
     * @see <a href="https://www.zhihu.com/question/21507953">如何在服务器端判断请求的客户端是微信调用的浏览器？</a>
     * @since 1.10.4
     */
    public static boolean isWechatRequest(HttpServletRequest request){
        String userAgent = getHeaderUserAgent(request);
        if (isNullOrEmpty(userAgent)){
            return false;
        }

        return (userAgent.toLowerCase()).contains("micromessenger");
    }

    /**
     * 判断一个请求是否是ajax请求.
     * 
     * @param request
     *            the request
     * @return 如果是ajax 请求 返回true
     * @see "http://en.wikipedia.org/wiki/X-Requested-With#Requested-With"
     */
    public static boolean isAjaxRequest(HttpServletRequest request){
        String header = request.getHeader(X_REQUESTED_WITH);
        return isNotNullOrEmpty(header) && header.equalsIgnoreCase(X_REQUESTED_WITH_VALUE_AJAX);
    }

    /**
     * 判断一个请求 ,不是ajax 请求.
     * 
     * @param request
     *            the request
     * @return 如果不是ajax 返回true
     * @see #isAjaxRequest(HttpServletRequest)
     */
    public static boolean isNotAjaxRequest(HttpServletRequest request){
        return !isAjaxRequest(request);
    }

    //---------------------------------------------------------------

    /**
     * 遍历显示request的attribute,将 name /attributeValue 存入到map(TreeMap).
     * 
     * 
     * <h3>注意:</h3>
     * 
     * <blockquote>
     * <p style="color:red">
     * 1.目前如果属性有级联关系,如果直接转json,可能会报错,此时建议使用 {@link JsonUtil#formatSimpleMap(Map, Class...) }
     * </p>
     * 
     * <p>
     * 2.可以做返回的map进行remove操作,不会影响request的 Attribute
     * </p>
     * </blockquote>
     * 
     * @param request
     *            the request
     * @return 如果{@link javax.servlet.ServletRequest#getAttributeNames()} 是null或者empty,返回{@link Collections#emptyMap()}<br>
     */
    public static Map<String, Object> getAttributeMap(HttpServletRequest request){
        Enumeration<String> attributeNames = request.getAttributeNames();
        if (isNullOrEmpty(attributeNames)){
            return emptyMap();
        }

        //---------------------------------------------------------------
        Map<String, Object> map = new TreeMap<>();
        while (attributeNames.hasMoreElements()){
            String name = attributeNames.nextElement();
            map.put(name, getAttribute(request, name));
        }
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 获得request中的请求参数值.
     * 
     * @param request
     *            当前请求
     * @param paramName
     *            参数名称
     * @return 获得request中的请求参数值
     */
    public static String getParameter(HttpServletRequest request,String paramName){
        return request.getParameter(paramName);
    }

    /**
     * 获取request body中的内容.
     * 
     * <h3>使用场景:</h3>
     * 
     * <blockquote>
     * wechat notify 的时候数据在 request body 里面,需要提取
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>request.getInputStream(); request.getReader();和request.getParameter("key");<br>
     * 这三个函数中任何一个函数执行一次后（可正常读取body数据），之后再执行就无效了。</li>
     * </ol>
     * </blockquote>
     *
     * @param request
     *            the request
     * @return the request body
     * @see "org.springframework.web.bind.annotation.RequestBody"
     * @see "org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor"
     * @see <a href="https://stackoverflow.com/questions/8100634/get-the-post-request-body-from-httpservletrequest">Get the POST request
     *      body from HttpServletRequest
     *      </a>
     * @since 1.10.6
     * @since 1.14.2 add 多次获取特性
     */
    public static String getRequestBody(HttpServletRequest request){
        //since 1.14.2
        String requestBodyInRequestScope = getAttribute(request, REQUEST_BODY_SCOPE_ATTRIBUTE_NAME);
        if (null != requestBodyInRequestScope){
            //有就返回
            return requestBodyInRequestScope;
        }

        //---------------------------------------------------------------
        //解析
        String requestBody = parseBody(request);
        if (null != requestBody){
            request.setAttribute(REQUEST_BODY_SCOPE_ATTRIBUTE_NAME, requestBody);
        }

        return requestBody;
    }

    /**
     * Parses the body.
     *
     * @param request
     *            the request
     * @return the string
     * @throws UncheckedIOException
     *             the unchecked IO exception
     * @since 1.14.2
     */
    private static String parseBody(HttpServletRequest request) throws UncheckedIOException{
        try{
            //Retrieves the body of the request as character data using a BufferedReader. 

            //The reader translates the character data according to the character encoding used on the body.
            //Either this method or getInputStream may be called to read the body, not both.
            BufferedReader reader = request.getReader();
            return ReaderUtil.toString(reader);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
