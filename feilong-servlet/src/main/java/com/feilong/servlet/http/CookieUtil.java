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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static java.util.Collections.emptyMap;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.servlet.http.entity.CookieEntity;

/**
 * {@link javax.servlet.http.Cookie Cookie} 工具类.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * 
 * <p style="color:red">
 * 注意:该类创建Cookie仅支持 <b>Servlet3</b> 以上的版本
 * </p>
 * 
 * <p>
 * 不建议直接调用这个类,可以使用feilong-accessor CookieAccessor 含有更多的特性和更简便,更易管理的配置
 * </p>
 * 
 * 依赖方式:
 * 
 * <pre>
 * {@code 
 *  <dependency>
 *      <groupId>com.feilong.platform</groupId>
 *      <artifactId>feilong-accessor</artifactId>
 *  </dependency>
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>使用说明:</h3>
 * 
 * <h4>
 * case:创建Cookie
 * </h4>
 * 
 * <blockquote>
 * <p>
 * 1.创建一个name名字是shopName,value是feilong的 Cookie (通常出于安全起见,存放到Cookie的值需要加密或者混淆,此处为了举例方便使用原码)<br>
 * 可以调用{@link #addCookie(String, String, HttpServletResponse)}<br>
 * 如:
 * </p>
 * 
 * <p>
 * <code>CookieUtil.addCookie("shopName","feilong",response)</code>
 * </p>
 * 
 * <p>
 * 注意:该方法创建的cookie,有效期是默认值 -1,即浏览器退出就删除
 * </p>
 * 
 * <p>
 * 2.如果想给该cookie加个过期时间,有效期一天,可以调用 {@link #addCookie(String, String, int, HttpServletResponse)}<br>
 * 如:
 * </p>
 * 
 * <p>
 * <code>CookieUtil.addCookie("shopName","feilong", SECONDS_PER_DAY,response)</code>
 * </p>
 * 
 * <p>
 * 3.如果还想给该cookie加上httpOnly等标识,可以调用 {@link #addCookie(CookieEntity, HttpServletResponse)}<br>
 * 如:
 * </p>
 * 
 * <pre class="code">
 * CookieEntity cookieEntity = new CookieEntity("shopName", "feilong", SECONDS_PER_DAY);
 * cookieEntity.setHttpOnly(true);
 * CookieUtil.addCookie(cookieEntity, response);
 * </pre>
 * 
 * <p>
 * 此外,如果有特殊需求,还可以对cookieEntity设置 path,domain等属性
 * </p>
 * </blockquote>
 * 
 * <h4>
 * case:获取Cookie
 * </h4>
 * 
 * <blockquote>
 * 
 * <h5>
 * 特殊说明:
 * </h5>
 * 
 * <p style="color:red">
 * 在读取Cookie 操作时，除了 {@link Cookie#getName()},{@link Cookie#getValue()} 外，不要妄图得到其他信息，如下方法不会得到值的：
 * 
 * {@link Cookie#getMaxAge()};
 * {@link Cookie#getDomain()};
 * ...
 * 
 * 因为,客户端传来的时候，就只剩下key和value
 * </p>
 * 
 * <p>
 * 1.可以使用 {@link #getCookie(HttpServletRequest, String)}来获得 {@link Cookie}对象
 * <br>
 * 如:
 * </p>
 * 
 * <p>
 * <code>CookieUtil.getCookie(request, "shopName")</code>
 * </p>
 * 
 * <p>
 * 2.更多的时候,可以使用 {@link #getCookieValue(HttpServletRequest, String)}来获得Cookie对象的值
 * <br>
 * 如:
 * </p>
 * 
 * <p>
 * <code>CookieUtil.getCookieValue(request, "shopName")</code>
 * </p>
 * 
 * <p>
 * 返回 "feilong" 字符串
 * </p>
 * 
 * 
 * <p>
 * 3.当然,你也可以使用 {@link #getCookieMap(HttpServletRequest)}来获得 所有的Cookie name和value组成的map
 * <br>
 * 如:
 * </p>
 * 
 * <p>
 * <code>CookieUtil.getCookieMap(request)</code>
 * </p>
 * 
 * <p>
 * 使用场景,如 {@link com.feilong.servlet.http.RequestLogBuilder#build()}
 * </p>
 * </blockquote>
 * 
 * 
 * <h4>
 * case:删除Cookie
 * </h4>
 * 
 * <blockquote>
 * 
 * <p>
 * 1.可以使用 {@link #deleteCookie(String, HttpServletResponse)}来删除Cookie
 * <br>
 * 如:
 * </p>
 * 
 * <p>
 * <code>CookieUtil.deleteCookie(request, "shopName")</code>
 * </p>
 * 
 * <p>
 * 2.特殊时候,由于Cookie原先保存时候设置了path属性,可以使用 {@link #deleteCookie(CookieEntity, HttpServletResponse)}来删除Cookie
 * <br>
 * 如:
 * </p>
 * 
 * <pre class="code">
 * CookieEntity cookieEntity = new CookieEntity("shopName", "feilong");
 * cookieEntity.setPath("/member/account");
 * CookieUtil.deleteCookie(request, "shopName");
 * </pre>
 * 
 * </blockquote>
 * 
 * <p>
 * 更多,请参考 <a href="https://github.com/venusdrogon/feilong-servlet/wiki/CookieUtil">CookieUtil wiki</a>
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.servlet.http.Cookie
 * @see "org.springframework.web.util.CookieGenerator"
 * @see "org.springframework.session.web.http.CookieSerializer"
 * @see com.feilong.servlet.http.entity.CookieEntity
 * @see <a href="http://tools.ietf.org/html/rfc6265">HTTP State Management Mechanism</a>
 * @see <a href="http://www.ietf.org/rfc/rfc2109.txt">HTTP State Management Mechanism (废弃 被rfc6265取代)</a>
 * @since 1.0.0
 */
public final class CookieUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieUtil.class);

    /** Don't let anyone instantiate this class. */
    private CookieUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //----------------------get-----------------------------------------

    /**
     * 取指定<code>cookieName</code>的 {@link Cookie}值.
     *
     * @param request
     *            the request
     * @param cookieName
     *            cookie名字,{@link Cookie#getName()},名称区分大小写,比如 JSESSIONID 如果改成 jsessionid 将取不到值
     * @return 如果取不到cookie,返回 <code>null</code>;<br>
     *         否则,返回 {@link Cookie#getValue()}
     * @see #getCookie(HttpServletRequest, String)
     * @see Cookie#getValue()
     */
    public static String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie cookie = getCookie(request, cookieName);
        return null == cookie ? null : getReadValue(cookie);
    }

    /**
     * 获得 {@link Cookie}对象.
     * 
     * <p>
     * 循环遍历 {@link HttpServletRequest#getCookies()},找到 {@link Cookie#getName()}是 <code>cookieName</code> 的 {@link Cookie}
     * </p>
     * 
     * @param request
     *            the request
     * @param cookieName
     *            cookie名字,{@link Cookie#getName()},名称区分大小写,比如 JSESSIONID 如果改成 jsessionid 将取不到值
     * @return 如果 {@link HttpServletRequest#getCookies()}是 null,则返回null;<br>
     *         如果通过 <code>cookieName</code> 找不到指定的 {@link Cookie},也返回null
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     * @see javax.servlet.http.Cookie#getName()
     */
    public static Cookie getCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if (isNullOrEmpty(cookies)){
            LOGGER.debug("when get cookieName:[{}],but request's cookies is null or empty!!", cookieName);
            return null;
        }

        //---------------------------------------------------------------
        for (Cookie cookie : cookies){
            if (cookie.getName().equals(cookieName)){
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("[getCookie],cookieName:[{}],cookie info:[{}]", cookieName, JsonUtil.format(cookie, 0, 0));
                }
                return cookie;
            }
        }
        LOGGER.debug("can't find the cookie:[{}]", cookieName);
        return null;
    }

    /**
     * 将{@link Cookie}的 key 和value转成 map({@link TreeMap}).
     *
     * @param request
     *            the request
     * @return 如果没有 {@link HttpServletRequest#getCookies()},返回 {@link Collections#emptyMap()};<br>
     *         否则,返回 loop cookies,取到 {@link Cookie#getName()}以及 {@link Cookie#getValue()} 设置成map 返回
     * @see HttpServletRequest#getCookies()
     * @see javax.servlet.http.Cookie#getName()
     * @see javax.servlet.http.Cookie#getValue()
     * @see "javax.servlet.jsp.el.ImplicitObjectELResolver.ImplicitObjects#createCookieMap(javax.servlet.jsp.PageContext)"
     */
    public static Map<String, String> getCookieMap(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (isNullOrEmpty(cookies)){
            return emptyMap();
        }

        //---------------------------------------------------------------

        Map<String, String> map = new TreeMap<>();
        for (Cookie cookie : cookies){
            map.put(cookie.getName(), getReadValue(cookie));
        }
        return map;
    }

    //--------------------delete-------------------------------------------

    /**
     * 删除{@link Cookie}.
     * 
     * <p style="color:red">
     * 删除{@link Cookie}的时候,path必须保持一致;<br>
     * 如果path不一致,请使用 {@link CookieUtil#deleteCookie(CookieEntity, HttpServletResponse)}
     * </p>
     * 
     * @param cookieName
     *            cookie名字,{@link Cookie#getName()},名称区分大小写,比如 JSESSIONID 如果改成 jsessionid 将取不到值
     * @param response
     *            the response
     * @see #deleteCookie(CookieEntity, HttpServletResponse)
     */
    public static void deleteCookie(String cookieName,HttpServletResponse response){
        deleteCookie(new CookieEntity(cookieName, ""), response);
    }

    /**
     * 删除cookie.
     * 
     * <p style="color:red">
     * 删除 Cookie的时候,path必须保持一致
     * </p>
     * 
     * @param cookieEntity
     *            the cookie entity
     * @param response
     *            the response
     * @see #addCookie(CookieEntity, HttpServletResponse)
     * @since 1.5.0
     */
    public static void deleteCookie(CookieEntity cookieEntity,HttpServletResponse response){
        Validate.notNull(cookieEntity, "cookieEntity can't be null!");

        cookieEntity.setMaxAge(0);// 设置为0为立即删除该Cookie
        addCookie(cookieEntity, response);

        LOGGER.debug("[deleteCookie],cookieName:[{}]", cookieEntity.getName());
    }

    //-------------------------add--------------------------------------

    /**
     * 创建cookie.
     * 
     * <p>
     * 注意:该方法创建的cookie,有效期是默认值 -1,即浏览器退出就删除
     * </p>
     * 
     * <p>
     * 如果 <code>cookieName</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>cookieName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param cookieName
     *            cookie名字,{@link Cookie#getName()},名称区分大小写,比如 JSESSIONID 如果改成 jsessionid 将取不到值
     * @param value
     *            cookie的值,更多说明,参见 {@link CookieEntity#getValue()}
     *            <p style="color:red">
     *            注意:如果值长度超过4K,浏览器会忽略,不会执行记录的操作
     *            </p>
     * @param response
     *            response
     * @see CookieUtil#addCookie(CookieEntity, HttpServletResponse)
     * @since 1.5.0
     */
    public static void addCookie(String cookieName,String value,HttpServletResponse response){
        Validate.notBlank(cookieName, "cookieName can't be null/empty!");
        addCookie(new CookieEntity(cookieName, value), response);
    }

    /**
     * 创建cookie.
     * 
     * <p>
     * 如果 <code>cookieName</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>cookieName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param cookieName
     *            cookie名字,{@link Cookie#getName()},名称区分大小写,比如 JSESSIONID 如果改成 jsessionid 将取不到值
     * @param value
     *            cookie的值,更多说明,参见 {@link CookieEntity#getValue()}
     *            <p style="color:red">
     *            注意:如果值长度超过4K,浏览器会忽略,不会执行记录的操作
     *            </p>
     * @param maxAge
     *            设置以秒计的cookie的最大存活时间,可以使用 {@link com.feilong.core.TimeInterval TimeInterval}相关常量
     * @param response
     *            response
     * @see CookieUtil#addCookie(CookieEntity, HttpServletResponse)
     * @since 1.5.0
     */
    public static void addCookie(String cookieName,String value,int maxAge,HttpServletResponse response){
        Validate.notBlank(cookieName, "cookieName can't be null/empty!");
        addCookie(new CookieEntity(cookieName, value, maxAge), response);
    }

    /**
     * 创建cookie.
     * 
     * <p>
     * 如果 <code>cookieEntity</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>cookieEntity.cookieName</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>cookieEntity.cookieName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     * 
     * @param cookieEntity
     *            cookieEntity
     * @param response
     *            response
     * @see "org.apache.catalina.connector.Response#generateCookieString(Cookie, boolean)"
     */
    public static void addCookie(CookieEntity cookieEntity,HttpServletResponse response){
        validateCookieEntity(cookieEntity);//校验

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("[addCookie],cookieName:[{}],cookieEntity info:[{}]", cookieEntity.getName(), JsonUtil.format(cookieEntity, 0, 0));
        }
        response.addCookie(toCookie(cookieEntity));
    }

    //---------------------------------------------------------------

    /**
     * To cookie.
     *
     * @param cookieEntity
     *            the cookie entity
     * @return the cookie
     * @since 1.5.3
     */
    private static Cookie toCookie(CookieEntity cookieEntity){
        Cookie cookie = new Cookie(cookieEntity.getName(), resolveWriteValue(cookieEntity));
        PropertyUtil.copyProperties(cookie, cookieEntity, "maxAge", "secure", "version", "httpOnly");

        PropertyUtil.setPropertyIfValueNotNullOrEmpty(cookie, "comment", cookieEntity.getComment());//指定一个注释来描述cookie的目的.
        //NullPointerException at javax.servlet.http.Cookie.setDomain(Cookie.java:213) ~[servlet-api-6.0.37.jar:na]
        PropertyUtil.setPropertyIfValueNotNullOrEmpty(cookie, "domain", cookieEntity.getDomain());// 指明cookie应当被声明的域.
        PropertyUtil.setPropertyIfValueNotNullOrEmpty(cookie, "path", cookieEntity.getPath());//指定客户端将cookie返回的cookie的路径.
        return cookie;
    }

    /**
     * Validate cookie entity.
     *
     * @param cookieEntity
     *            the cookie entity
     * @since 1.5.0
     */
    private static void validateCookieEntity(CookieEntity cookieEntity){
        Validate.notNull(cookieEntity, "cookieEntity can't be null!");
        Validate.notBlank(cookieEntity.getName(), "cookieName can't be null/empty!");

        //---------------------------------------------------------------
        if (LOGGER.isWarnEnabled()){
            String value = resolveWriteValue(cookieEntity);

            //如果长度超过4000,浏览器可能不支持
            if (isNotNullOrEmpty(value) && value.length() > 4000){
                String pattern = "cookie value:{},length:{},more than [4000]!!!some browser may be not support!!!!!,cookieEntity info :{}";
                LOGGER.warn(pattern, value, value.length(), JsonUtil.format(cookieEntity, 0, 0));
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Resolve write value.
     *
     * @param cookieEntity
     *            the cookie entity
     * @return the string
     * @see "org.springframework.session.web.http.DefaultCookieSerializer#writeCookieValue(CookieValue)"
     * @since 1.10.4
     */
    private static String resolveWriteValue(CookieEntity cookieEntity){
        //保持兼容,暂不encode  see https://github.com/venusdrogon/feilong-servlet/issues/7
        //com.feilong.accessor.cookie.CookieAccessor 支持
        return cookieEntity.getValue();
    }

    /**
     * 获得value.
     *
     * @param cookie
     *            the cookie
     * @return the read value
     * @since 1.10.4
     */
    private static String getReadValue(Cookie cookie){
        //保持兼容,暂不encode  see https://github.com/venusdrogon/feilong-servlet/issues/7
        //com.feilong.accessor.cookie.CookieAccessor 支持
        return cookie.getValue();
    }
}
