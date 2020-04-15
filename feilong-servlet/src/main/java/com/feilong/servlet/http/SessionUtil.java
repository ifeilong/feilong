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

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.core.util.SortUtil.sortList;
import static java.util.Collections.emptyMap;

import java.io.Serializable;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.date.DateUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * {@link javax.servlet.http.HttpSession HttpSession} 工具类.
 * 
 * <h3>session什么时候被创建?</h3>
 * 
 * <blockquote>
 * <p>
 * 一个常见的错误是以为session在有客户端访问时就被创建,<br>
 * 然而事实是直到某server端程序(如Servlet)调用 {@link javax.servlet.http.HttpServletRequest#getSession()} 这样的语句时才会被创建.
 * </p>
 * </blockquote>
 * 
 * <h3>session何时被删除?</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>程序调用 {@link javax.servlet.http.HttpSession#invalidate()}</li>
 * <li>距离上一次收到客户端发送的session id时间间隔超过了session的最大有效时间</li>
 * <li>服务器进程被停止</li>
 * </ol>
 * 再次注意关闭浏览器只会使存储在客户端浏览器内存中的session cookie失效,不会使服务器端的session对象失效
 * </blockquote>
 * 
 * <h3>SessionId会重复吗?</h3>
 * 
 * <blockquote>
 * 不会,参见 <code>org.apache.catalina.session.ManagerBase#generateSessionId()</code> 实现, 使用 while循环再次确认判断
 * 
 * <pre class="code">
 * 
 * protected String generateSessionId(){
 *     String result = null;
 *     do{
 *         if (result != null){
 *             duplicates++;
 *         }
 *         result = sessionIdGenerator.generateSessionId();
 *     }while (sessions.containsKey(result));
 *     return result;
 * }
 * </pre>
 * 
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.apache.catalina.session.ManagerBase#generateSessionId()"
 * @see "org.apache.catalina.session.StandardSession#StandardSession(Manager)"
 * @see "org.apache.catalina.core.ApplicationSessionCookieConfig#createSessionCookie(Context, String, boolean)"
 * @since 1.0.0
 */
public final class SessionUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionUtil.class);

    /** Don't let anyone instantiate this class. */
    private SessionUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得给定request的session id,如果有的话.
     *
     * @param request
     *            current HTTP request
     * @return 如果没有session,将返回null,否则返回 session id<br>
     *         如果 <code>request</code> 是null,抛出 {@link NullPointerException}<br>
     * @see "org.springframework.web.util.WebUtils#getSessionId"
     * @since 1.10.5
     */
    public static String getSessionId(HttpServletRequest request){
        Validate.notNull(request, "request can't be null!");

        HttpSession session = request.getSession(false);
        return session == null ? null : session.getId();
    }

    /**
     * 从指定的request 对应的session中获取指定的属性.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果没有会话，则不创建新会话！</li>
     * <li>如果没有session或者session没有指定的属性, 那么返回null</li>
     * </ol>
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param request
     *            current HTTP request
     * @param attributeName
     *            the name of the session attribute
     * @return 如果没有session,那么返回 null<br>
     *         如果session中找不到指定的属性也返回null<br>
     *         如果 <code>request</code> 是null,抛出 {@link NullPointerException}<br>
     * @see "org.springframework.web.util.WebUtils#getSessionAttribute"
     * @since 1.10.5
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request,String attributeName){
        Validate.notNull(request, "request can't be null!");

        HttpSession session = request.getSession(false);
        return session == null ? null : (T) session.getAttribute(attributeName);
    }

    /**
     * 从指定的request 对应的session中获取指定的属性.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果没有会话，则不创建新会话！</li>
     * <li>如果没有session或者session没有指定的属性, 那么抛出异常</li>
     * </ol>
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param request
     *            current HTTP request
     * @param attributeName
     *            the name of the session attribute
     * @return the value of the session attribute, or {@code null} if not found <br>
     *         如果 <code>request</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws IllegalStateException
     *             if the session attribute could not be found
     * @see "org.springframework.web.util.WebUtils#getRequiredSessionAttribute"
     * @since 1.10.5
     */
    public static <T> T getRequiredAttribute(HttpServletRequest request,String attributeName){
        T value = getAttribute(request, attributeName);
        Validate.validState(null != value, "No session attribute [%s] found", attributeName);
        return value;
    }

    /**
     * 设置 the session attribute with the given name to the given value.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果 <code>attributeValue!=null</code>,那么调用 {@link HttpSession#setAttribute(String, Object)} 设置属性</li>
     * <li>如果 <code>attributeValue==null</code>,看看当前有没有session,没有session 那么什么都不做, 有session 那么从session中删除指定的属性名称</li>
     * <li>如果 <code>request</code> 是null,抛出 {@link NullPointerException}</li>
     * </ol>
     * </blockquote>
     * 
     * @param request
     *            current HTTP request
     * @param attributeName
     *            the name of the session attribute
     * @param attributeValue
     *            the value of the session attribute
     * @see "org.springframework.web.util.WebUtils#setSessionAttribute"
     * @since 1.10.5
     */
    public static void setAttribute(HttpServletRequest request,String attributeName,Serializable attributeValue){
        Validate.notNull(request, "request can't be null!");

        if (attributeValue == null){
            HttpSession session = request.getSession(false);
            if (session != null){
                session.removeAttribute(attributeName);
            }
            return;
        }

        request.getSession().setAttribute(attributeName, attributeValue);
    }

    /**
     * 删除session 中指定的属性.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果 <code>request</code> 是null,抛出 {@link NullPointerException}</li>
     * <li>如果找不到session 什么都不做</li>
     * <li>如果找到session,从中删除指定的属性</li>
     * </ol>
     * </blockquote>
     *
     * @param request
     *            the request
     * @param attributeName
     *            the attribute name
     * @since 1.10.5
     */
    public static void removeAttribute(HttpServletRequest request,String attributeName){
        Validate.notNull(request, "request can't be null!");

        HttpSession session = request.getSession(false);
        if (null == session){
            return;
        }
        session.removeAttribute(attributeName);
    }

    //---------------------------------------------------------------

    /**
     * 获得常用的session 属性值 map(仅仅用于log和debug使用).
     * 
     * @param session
     *            the session
     * @return 如果session 是 null,则返回 empty的{@link LinkedHashMap}
     * @see HttpSession#getId()
     * @see HttpSession#getCreationTime()
     * @see HttpSession#getLastAccessedTime()
     * @see HttpSession#getMaxInactiveInterval()
     * @see HttpSession#getAttributeNames()
     * @see HttpSession#isNew()
     */
    public static Map<String, Object> getSessionInfoMapForLog(HttpSession session){
        if (isNullOrEmpty(session)){
            return emptyMap();
        }

        //---------------------------------------------------------------

        Map<String, Object> map = newLinkedHashMap();
        // 返回SESSION创建时JSP引擎为它设的惟一ID号 
        map.put("id", session.getId());

        // 返回服务器创建的一个SESSION,客户端是否已经加入 
        map.put("isNew", session.isNew());

        //返回SESSION创建时间
        map.put("creationTime", toPrettyMessage(session.getCreationTime()));

        //返回此SESSION里客户端最近一次请求时间 
        map.put("lastAccessedTime", toPrettyMessage(session.getLastAccessedTime()));

        //返回两次请求间隔多长时间此SESSION被取消(in seconds) 
        //Returns the maximum time interval, in seconds, 
        //that the servlet container will keep this session open between client accesses. 
        //After this interval, the servlet container will invalidate the session. 

        //The maximum time interval can be set with the setMaxInactiveInterval method.

        //A negative time indicates the session should never timeout.
        int maxInactiveInterval = session.getMaxInactiveInterval(); //单位 秒
        map.put("maxInactiveInterval", formatDuration(1000L * maxInactiveInterval));

        map.put("attributeNames", sortList(toList(session.getAttributeNames())));
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 遍历session的attribute,将 name /attributeValue 存入到map里.
     * 
     * @param session
     *            the session
     * @return the attribute map
     * @see javax.servlet.http.HttpSession#getAttributeNames()
     * @see javax.servlet.http.HttpSession#getAttribute(String)
     */
    public static Map<String, Serializable> getAttributeMap(HttpSession session){
        Map<String, Serializable> map = newHashMap();

        Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()){
            String name = attributeNames.nextElement();
            map.put(name, (Serializable) session.getAttribute(name));
        }
        return map;
    }

    /**
     * 替换session,防止利用JSESSIONID 伪造url进行session hack.
     * 
     * <h3>代码流程:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>使用<code>request.getSession(false)</code>,判断原先是否存在session,如果不存在,那么直接开启一个新的session并返回;</li>
     * <li>如果老session存在,那么取到里面所有的attribute属性map,然后让老session 失效 {@link HttpSession#invalidate()}</li>
     * <li>而后,开始一个新的session,并将老session 里面的属性设置进去,并返回</li>
     * </ol>
     * </blockquote>
     * 
     * <p>
     * 该方法通常在用户登录逻辑里面调用,要确保登录前和登录后的session不相同,(确切的说,登录后使用新的JSESSIONID),如果登录前和登录后的JSESSIONID不发生改变的话,那么这就是一个固定SessionID的漏洞(详见《黑客攻防技术宝典-web实战》
     * 第七章)
     * </p>
     * 
     * <h3>简单的漏洞攻击:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>第一步,需要获取被攻击用户的JSESSIONID,可以通过给被攻击用户一个伪造的JSESSIONID,使其用该JESSIONID登录,获取用户登录后的JESSIONID.(这里作为示范,直接从浏览器中获取)</li>
     * <li>第二步,等被攻击用户登录,是JESSIONID成为已登录状态.</li>
     * <li>第三步,伪造请求,访问登录后的资源.在用户登录使该JSESSIONID称为已登录的ID后,攻击者就可以利用这个ID伪造请求访问登录后的资源.</li>
     * </ul>
     * </blockquote>
     * 
     * @param request
     *            request
     * @return the http session
     * @see "org.owasp.esapi.reference.DefaultHTTPUtilities#changeSessionIdentifier(HttpServletRequest)"
     * @see "org.springframework.security.util.SessionUtils#startNewSessionIfRequired(HttpServletRequest, boolean, SessionRegistry)"
     * @see <a href="http://blog.csdn.net/jiangbo_hit/article/details/6073710">固定SessionID漏洞</a>
     */
    public static HttpSession replaceSession(HttpServletRequest request){
        // 当session存在时返回该session,否则不会新建session,返回null
        // getSession()/getSession(true):当session存在时返回该session,否则新建一个session并返回该对象
        HttpSession oldSession = request.getSession(false);

        //---------------------------------------------------------------

        if (null == oldSession){// 是null 新建一个并直接返回
            LOGGER.debug("oldSession is null,return a new session~~");
            return request.getSession();
        }

        //---------------------------------------------------------------

        String oldSessionId = oldSession.getId();
        Map<String, Serializable> attributeMap = getAttributeMap(oldSession);

        //---------------------------------------------------------------

        oldSession.invalidate();//老的session失效

        //---------------------------------------------------------------

        HttpSession newSession = request.getSession();
        //When only the keys from a map are needed in a loop, iterating the keySet makes sense. But when both the key and the value are needed, it's more efficient to iterate the entrySet, which will give access to both the key and value, instead.
        for (Map.Entry<String, Serializable> entry : attributeMap.entrySet()){
            newSession.setAttribute(entry.getKey(), entry.getValue());
        }

        //---------------------------------------------------------------
        LOGGER.debug("old sessionId:[{}],invalidate it!new session:[{}],and put all attributes", oldSessionId, newSession.getId());
        return newSession;
    }

    //---------------------------------------------------------------

    /**
     * To pretty message.
     *
     * @param creationTime
     *            the creation time
     * @return the string
     * @since 1.5.2
     */
    private static String toPrettyMessage(long creationTime){
        Date creationTimeDate = new Date(creationTime);
        String dateString = DateUtil.toString(creationTimeDate, COMMON_DATE_AND_TIME_WITH_MILLISECOND);
        return Slf4jUtil.format("[{}],intervalToNow:[{}]", dateString, formatDuration(creationTimeDate));
    }
}