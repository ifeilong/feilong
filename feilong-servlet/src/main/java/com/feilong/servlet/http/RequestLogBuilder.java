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
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.servlet.http.RequestAttributes.ERROR_EXCEPTION;
import static com.feilong.servlet.http.RequestAttributes.ERROR_EXCEPTION_TYPE;
import static com.feilong.servlet.http.RequestAttributes.ERROR_MESSAGE;
import static com.feilong.servlet.http.RequestAttributes.ERROR_REQUEST_URI;
import static com.feilong.servlet.http.RequestAttributes.ERROR_SERVLET_NAME;
import static com.feilong.servlet.http.RequestAttributes.ERROR_STATUS_CODE;
import static com.feilong.servlet.http.RequestAttributes.FORWARD_CONTEXT_PATH;
import static com.feilong.servlet.http.RequestAttributes.FORWARD_PATH_INFO;
import static com.feilong.servlet.http.RequestAttributes.FORWARD_QUERY_STRING;
import static com.feilong.servlet.http.RequestAttributes.FORWARD_REQUEST_URI;
import static com.feilong.servlet.http.RequestAttributes.FORWARD_SERVLET_PATH;
import static com.feilong.servlet.http.RequestAttributes.INCLUDE_CONTEXT_PATH;
import static com.feilong.servlet.http.RequestAttributes.INCLUDE_PATH_INFO;
import static com.feilong.servlet.http.RequestAttributes.INCLUDE_QUERY_STRING;
import static com.feilong.servlet.http.RequestAttributes.INCLUDE_REQUEST_URI;
import static com.feilong.servlet.http.RequestAttributes.INCLUDE_SERVLET_PATH;
import static com.feilong.servlet.http.entity.RequestLogSwitch.NORMAL;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.builder.Builder;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.net.ParamUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.servlet.http.entity.RequestIdentity;
import com.feilong.servlet.http.entity.RequestLogSwitch;

/**
 * 基于 {@link RequestLogSwitch} ,构造需要输出的Map,以便输出 request log.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.commons.lang3.builder.Builder
 * @see org.apache.commons.lang3.builder.HashCodeBuilder
 * @since 1.4.0
 * @since 1.10.6 调整访问权限
 */
class RequestLogBuilder implements Builder<Map<String, Object>>{

    /** The request. */
    private final HttpServletRequest request;

    /** The request log switch. */
    private final RequestLogSwitch   requestLogSwitch;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param request
     *            the request
     * @param requestLogSwitch
     *            the request log switch
     */
    public RequestLogBuilder(HttpServletRequest request, RequestLogSwitch requestLogSwitch){
        super();
        this.request = request;
        this.requestLogSwitch = requestLogSwitch;
    }

    //---------------------------------------------------------------

    /**
     * 将request 相关属性,数据转成json格式 以便log显示(目前仅作log使用).
     * 
     * the request log switch
     * 
     * @return the request string for log
     */
    @Override
    public Map<String, Object> build(){
        RequestLogSwitch opRequestLogSwitch = defaultIfNull(this.requestLogSwitch, NORMAL);

        Map<String, Object> map = newLinkedHashMap();

        String clientIp = getClientIP(opRequestLogSwitch);

        //---------------------------------------------------------------

        // requestFullURL
        if (opRequestLogSwitch.getShowFullURL()){
            map.put("requestFullURL", RequestUtil.getRequestFullURL(request, UTF8));
        }
        // Method
        if (opRequestLogSwitch.getShowMethod()){
            map.put("requestMethod", request.getMethod());
        }

        // parameterMap
        if (opRequestLogSwitch.getShowParams()){
            // 在3.0 是数组Map<String, String[]> getParameterMap
            // The keys in the parameter map are of type String.
            // The values in the parameter map are of type String array.
            MapUtil.putIfValueNotNullOrEmpty(map, "parameters", RequestUtil.getParameterMap(request));
        }

        //---------------------------------------------------------------

        //RequestIdentity
        if (opRequestLogSwitch.getShowIdentity()){
            RequestIdentity requestIdentity = new RequestIdentity();
            requestIdentity.setClientIP(clientIp);
            requestIdentity.setUserAgent(RequestUtil.getHeaderUserAgent(request));
            requestIdentity.setSessionId(getSessionId(request));
            map.put("requestIdentity", requestIdentity);
        }

        // _headerMap
        if (opRequestLogSwitch.getShowHeaders()){
            map.put("headerInfos", getHeaderMap());
        }

        // _cookieMap
        if (opRequestLogSwitch.getShowCookies()){
            MapUtil.putIfValueNotNullOrEmpty(map, "cookieInfos", CookieUtil.getCookieMap(request));
        }

        // aboutURLMap
        if (opRequestLogSwitch.getShowURLs()){
            map.put("urlInfos", buildURLInfosMap());
        }

        // aboutElseMap
        if (opRequestLogSwitch.getShowElses()){
            map.put("elseInfos", buildElseMap());
        }

        //-------------------ip--------------------------------------------

        // aboutIPMap
        if (opRequestLogSwitch.getShowIPs()){
            map.put("ipInfos", buildIpsMap(clientIp));
        }

        //-----------------------ports----------------------------------------

        // aboutPortMap
        if (opRequestLogSwitch.getShowPorts()){
            map.put("portInfos", buildPortsMap());
        }

        //------------------------errorInfos---------------------------------------

        // _errorInfos
        if (opRequestLogSwitch.getShowErrors()){
            MapUtil.putIfValueNotNullOrEmpty(map, "errorInfos", getErrorMap(request));
        }
        // forwardInfos
        if (opRequestLogSwitch.getShowForwardInfos()){
            MapUtil.putIfValueNotNullOrEmpty(map, "forwardInfos", getForwardMap(request));
        }
        // includeInfos
        if (opRequestLogSwitch.getShowIncludeInfos()){
            MapUtil.putIfValueNotNullOrEmpty(map, "includeInfos", getIncludeMap(request));
        }

        //---------------------------------------------------------------
        return map;
    }

    /**
     * 如果需要显示客户端ip,那么显示,否则不显示.
     *
     * @param opRequestLogSwitch
     *            the op request log switch
     * @return the client IP
     * @since 1.10.6
     */
    private String getClientIP(RequestLogSwitch opRequestLogSwitch){
        return (opRequestLogSwitch.getShowIdentity() || opRequestLogSwitch.getShowIPs()) ? RequestUtil.getClientIp(request) : null;
    }

    /**
     * Builds the ports map.
     * 
     * <pre>
    {@code
        "about Port Info Map":         {
            "request.getLocalPort()": "8084",
            "request.getRemotePort()": "59225",
            "request.getServerPort()": "8084"
        }
    }
     * </pre>
     *
     * @return the map
     * @since 1.10.5
     */
    private Map<String, String> buildPortsMap(){
        Map<String, String> aboutPortMap = new TreeMap<>();

        //Returns the Internet Protocol (IP) port number of the interface on which the request was received.
        aboutPortMap.put("request.getLocalPort()", "" + request.getLocalPort());

        //Returns the Internet Protocol (IP) source port of the client or last proxy that sent the request.
        aboutPortMap.put("request.getRemotePort()", "" + request.getRemotePort());

        //Returns the port number to which the request was sent. It is the value of the part after ":" in the Host header value, if any, or the server port where the client connection was accepted on.
        aboutPortMap.put("request.getServerPort()", "" + request.getServerPort());
        return aboutPortMap;
    }

    /**
     * ip相关信息.
     * 
     * <pre>
     *     {@code
     *     "about IP Info Map":         {
     *             "getClientIp": "127.0.0.1",
     *             "request.getLocalAddr()": "127.0.0.1",
     *             "request.getRemoteAddr()": "127.0.0.1",
     *             "request.getRemoteHost()": "127.0.0.1",
     *             "request.getServerName()": "127.0.0.1"
     *         },
     *     }
     * </pre>
     *
     * @param clientIP
     *            the client IP
     * @return the map
     * @since 1.10.5
     */
    private Map<String, String> buildIpsMap(String clientIP){
        Map<String, String> aboutIPMap = new TreeMap<>();

        //Returns the Internet Protocol (IP) address of the interface on which the request was received.
        aboutIPMap.put("request.getLocalAddr()", request.getLocalAddr());//获得项目本地ip地址

        //Returns the fully qualified name of the client or the last proxy that sent the request. If the engine cannot or chooses not to resolve the hostname (to improve performance), this method returns the dotted-string form of the IP address. For HTTP servlets, same as the value of the CGI variable REMOTE_HOST.
        aboutIPMap.put("request.getRemoteAddr()", request.getRemoteAddr());

        //Returns the fully qualified name of the client or the last proxy that sent the request. If the engine cannot or chooses not to resolve the hostname (to improve performance), this method returns the dotted-string form of the IP address. For HTTP servlets, same as the value of the CGI variable REMOTE_HOST.
        aboutIPMap.put("request.getRemoteHost()", request.getRemoteHost());

        //Returns the host name of the server to which the request was sent. It is the value of the part before ":" in the Host header value, if any, or the resolved server name, or the server IP address.
        aboutIPMap.put("request.getServerName()", request.getServerName());

        aboutIPMap.put("getClientIp", clientIP);
        return aboutIPMap;
    }

    /**
     * 杂项相关信息.
     * 
     * <pre>
    {@code
    "about Else Map":         {
            "request.getScheme()": "http",
            "request.getProtocol()": "HTTP/1.1",
            "request.getAuthType()": null,
            "request.getCharacterEncoding()": "UTF-8",
            "request.getContentType()": null,
            "request.getContentLength()": "-1",
            "request.getLocale()": "zh_CN",
            "request.getLocalName()": "localhost",
            "request.getRemoteUser()": null,
            "request.isRequestedSessionIdFromCookie()": false,
            "request.isRequestedSessionIdFromURL()": false,
            "request.isRequestedSessionIdValid()": false,
            "request.isSecure()": false,
            "request.getUserPrincipal()": null
        },
    }
     * </pre>
     *
     * @return the map
     * @since 1.10.5
     */
    private Map<String, Object> buildElseMap(){
        Map<String, Object> aboutElseMap = newLinkedHashMap();

        //Returns the name of the scheme used to make this request, for example, http, https, or ftp. Different schemes have different rules for constructing URLs, as noted in RFC 1738.
        aboutElseMap.put("request.getScheme()", request.getScheme());

        //Returns the name and version of the protocol the request uses in the form protocol/majorVersion.minorVersion, for example, HTTP/1.1. For HTTP servlets, the value returned is the same as the value of the CGI variable SERVER_PROTOCOL.
        aboutElseMap.put("request.getProtocol()", request.getProtocol());

        //Returns the name of the authentication scheme used to protect the servlet. 
        //All servlet containers support basic, form and client certificate authentication, and may additionally support digest authentication. If the servlet is not authenticated null is returned. 
        //Same as the value of the CGI variable AUTH_TYPE.
        aboutElseMap.put("request.getAuthType()", request.getAuthType());

        //Returns the name of the character encoding used in the body of this request. 
        //This method returns null if the request does not specify a character encoding
        aboutElseMap.put("request.getCharacterEncoding()", request.getCharacterEncoding());

        //Returns the MIME type of the body of the request, or null if the type is not known. 
        //For HTTP servlets, same as the value of the CGI variable CONTENT_TYPE.
        aboutElseMap.put("request.getContentType()", "" + request.getContentType());

        //Returns the length, in bytes, of the request body and made available by the input stream, 
        //or -1 if the length is not known. 
        //For HTTP servlets, same as the value of the CGI variable CONTENT_LENGTH.
        aboutElseMap.put("request.getContentLength()", "" + request.getContentLength());

        //Returns the preferred Locale that the client will accept content in, based on the Accept-Language header. 
        //If the client request doesn't provide an Accept-Language header, this method returns the default locale for the server.
        aboutElseMap.put("request.getLocale()", "" + request.getLocale());

        //Returns the host name of the Internet Protocol (IP) interface on which the request was received.
        //2.4
        aboutElseMap.put("request.getLocalName()", request.getLocalName());

        //Returns the login of the user making this request, if the user has been authenticated, or null if the user has not been authenticated. Whether the user name is sent with each subsequent request depends on the browser and type of authentication. Same as the value of the CGI variable REMOTE_USER.
        aboutElseMap.put("request.getRemoteUser()", request.getRemoteUser());

        //Checks whether the requested session ID came in as a cookie.
        aboutElseMap.put("request.isRequestedSessionIdFromCookie()", request.isRequestedSessionIdFromCookie());

        //Checks whether the requested session ID came in as part of the request URL.
        aboutElseMap.put("request.isRequestedSessionIdFromURL()", request.isRequestedSessionIdFromURL());

        //Checks whether the requested session ID is still valid. If the client did not specify any session ID, this method returns false. 
        aboutElseMap.put("request.isRequestedSessionIdValid()", request.isRequestedSessionIdValid());

        //Returns a boolean indicating whether this request was made using a secure channel, such as HTTPS.
        aboutElseMap.put("request.isSecure()", request.isSecure());

        //Returns a java.security.Principal object containing the name of the current authenticated user. If the user has not been authenticated, the method returns null.
        aboutElseMap.put("request.getUserPrincipal()", request.getUserPrincipal());
        return aboutElseMap;
    }

    /**
     * 取到session id信息.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * 以前使用的方式是 先取session,再取sessionid
     * 
     * <pre>
    {@code
            try{
                HttpSession session = request.getSession(false);
                return null == session ? EMPTY : session.getId();
            }catch (IllegalStateException e){//Cannot create a session after the response has been committed 
                String msg = Slf4jUtil.format("uri:[{}],paramMap:{}", request.getRequestURI(), request.getParameterMap());
                LOGGER.error(msg, e);
                return e.getMessage();
            }
    }
     * </pre>
     * 
     * 有时候程序会报错.
     * 
     * <p>
     * 有时候操作log的时候,会出现 Cannot create a session after the response has been committed <br>
     * 很奇怪的错误
     * </p>
     * 
     * <p>
     * I have learnt that maybe my 8K buffer gets full in some cases (as you said, my contect is dynamic and sometimes could be large). <br>
     * 
     * In that case, I have understanded that a full buffer triggers a commit, and when that happens the JSP error page can not do its job
     * and then "java.lang.IllegalStateException: Cannot create a session after the response has been committed" happens. <br>
     * 
     * OK, but is there any other possible reason for the early commit? <br>
     * My session is created early enough, and in fact the JSP page creates it if necessary, by default.
     * </p>
     * 
     * </blockquote>
     * 
     * @param request
     *            the request
     * @return the session id,如果有异常, 返回 {@link java.lang.Throwable#getMessage()}
     * @since 1.4.1
     */
    private static String getSessionId(HttpServletRequest request){
        return CookieUtil.getCookieValue(request, "JSESSIONID");
    }

    //---------------------------------------------------------------

    /**
     * 获得 forward map.
     *
     * @param request
     *            the request
     * @return the forward map
     */
    private static Map<String, String> getForwardMap(HttpServletRequest request){
        String[] array = { FORWARD_CONTEXT_PATH, FORWARD_REQUEST_URI, FORWARD_SERVLET_PATH, FORWARD_PATH_INFO, FORWARD_QUERY_STRING };
        return getAttributeMapIfValueNotNull(request, array);
    }

    /**
     * 获得 include map.
     *
     * @param request
     *            the request
     * @return the include map
     */
    private static Map<String, String> getIncludeMap(HttpServletRequest request){
        String[] array = { INCLUDE_CONTEXT_PATH, INCLUDE_PATH_INFO, INCLUDE_QUERY_STRING, INCLUDE_REQUEST_URI, INCLUDE_SERVLET_PATH };
        return getAttributeMapIfValueNotNull(request, array);
    }

    /**
     * 获得request error 相关参数 map.
     *
     * @param request
     *            the request
     * @return 如果request 有 {@link RequestAttributes#ERROR_STATUS_CODE}属性,则返回error 相关属性 封装到map,<br>
     *         如果 request没有 {@link RequestAttributes#ERROR_STATUS_CODE}属性,返回null
     */
    private static Map<String, String> getErrorMap(HttpServletRequest request){
        String[] array = { ERROR_STATUS_CODE, ERROR_REQUEST_URI, ERROR_EXCEPTION, ERROR_EXCEPTION_TYPE, ERROR_MESSAGE, ERROR_SERVLET_NAME };
        return getAttributeMapIfValueNotNull(request, array);
    }

    //---------------------------------------------------------------

    /**
     * 将指定的attributeName当作key,request找到属性值,设置到map中(当且仅当 <code>null != map && null != value </code>才将key/value put到map中).
     *
     * @param request
     *            the request
     * @param attributeNames
     *            the attribute names
     * @return the attribute map if value not null
     * @since 1.7.3
     */
    private static Map<String, String> getAttributeMapIfValueNotNull(HttpServletRequest request,String...attributeNames){
        Map<String, String> map = newLinkedHashMap(attributeNames.length);
        for (String attributeName : attributeNames){
            //有的request value 可能是 其他类型, 比如 ERROR_STATUS_CODE
            MapUtil.putIfValueNotNull(map, attributeName, ConvertUtil.toString(RequestUtil.getAttribute(request, attributeName)));
        }
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 获得 about url map.
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
     * <tr valign="top">
     * <td>request.getContextPath()</td>
     * <td>request.getContextPath()</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>request.getPathInfo()</td>
     * <td>Returns any extra path information associated with the URL the client sent when it made this request. <br>
     * Servlet访问路径之后,QueryString之前的中间部分</td>
     * </tr>
     * <tr valign="top">
     * <td>request.getServletPath()</td>
     * <td>web.xml中定义的Servlet访问路径</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>request.getPathTranslated()</td>
     * <td>等于getServletContext().getRealPath("/") + getPathInfo()</td>
     * </tr>
     * <tr valign="top">
     * <td>request.getRequestURI()</td>
     * <td>等于getContextPath() + getServletPath() + getPathInfo()</td>
     * </tr>
     * <tr valign="top">
     * <td>request.getRequestURL()</td>
     * <td>等于getScheme() + "://" + getServerName() + ":" + getServerPort() + getRequestURI()</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>request.getQueryString()</td>
     * <td>&之后GET方法的参数部分<br>
     * Returns the query string that is contained in the request URL after the path. <br>
     * This method returns null if the URL does not have a query string. <br>
     * Same as the value of the CGI variable QUERY_STRING.</td>
     * </tr>
     * </table>
     * </blockquote>
     *
     * @return the about url map
     * @since 1.0.9
     */
    private Map<String, String> buildURLInfosMap(){
        // 1.getServletContext().getRealPath("/") 后包含当前系统的文件夹分隔符(windows系统是"\",linux系统是"/"),而getPathInfo()以"/"开头.
        // 2.getPathInfo()与getPathTranslated()在servlet的url-pattern被设置为/*或/aa/*之类的pattern时才有值,其他时候都返回null.
        // 3.在servlet的url-pattern被设置为*.xx之类的pattern时,getServletPath()返回的是getRequestURI()去掉前面ContextPath的剩余部分.

        Map<String, String> map = newLinkedHashMap();

        map.put("request.getContextPath()", request.getContextPath());

        // web.xml中定义的Servlet访问路径
        map.put("request.getServletPath()", request.getServletPath());

        // Returns any extra path information associated with the URL the client sent when it made this request.
        // Servlet访问路径之后,QueryString之前的中间部分
        map.put("request.getPathInfo()", request.getPathInfo());

        // 等于getServletContext().getRealPath("/") + getPathInfo()
        map.put("request.getPathTranslated()", request.getPathTranslated());

        //---------------------------------------------------------------
        // 等于getContextPath() + getServletPath() + getPathInfo()
        map.put("request.getRequestURI()", request.getRequestURI());

        // 等于getScheme() + "://" + getServerName() + ":" + getServerPort() + getRequestURI()
        map.put("request.getRequestURL()", "" + request.getRequestURL());

        //---------------------------------------------------------------

        // &之后GET方法的参数部分
        //Returns the query string that is contained in the request URL after the path. 
        //This method returns null if the URL does not have a query string. 
        //Same as the value of the CGI variable QUERY_STRING. 
        map.put("request.getQueryString()", request.getQueryString());

        map.put("getQueryStringLog", getQueryStringLog());
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 遍历显示request的header 用于debug.
     * 
     * <p>
     * 将 request header name 和value 封装到map.
     * </p>
     * 
     * <pre>
    {@code
        "headerInfo":         {
            "accept-encoding": "gzip,deflate",
            "connection": "Keep-Alive",
            "host": "127.0.0.1:8084",
            "user-agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21"
        },
    }
     * </pre>
     * 
     * @return the header map
     */
    private Map<String, String> getHeaderMap(){
        Map<String, String> map = new TreeMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            map.put(name, request.getHeader(name));
        }
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 获取queryString (支持 post/get).
     *
     * @return the query string
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    private String getQueryStringLog(){
        // Returns the name of the HTTP method with which this request was made,
        // for example, GET, POST, or PUT.
        // Same as the value of the CGI variable REQUEST_METHOD.
        String method = request.getMethod();

        if ("post".equalsIgnoreCase(method)){
            Map<String, String[]> map = RequestUtil.getParameterMap(request);
            if (isNotNullOrEmpty(map)){
                return ParamUtil.toQueryStringUseArrayValueMap(map);
            }
        }
        // Returns the query string that is contained in the request URL after the path.
        // This method returns null if the URL does not have a query string.
        // Same as the value of the CGI variable QUERY_STRING.
        // 它只对get方法得到的数据有效.
        return request.getQueryString();
    }
}
