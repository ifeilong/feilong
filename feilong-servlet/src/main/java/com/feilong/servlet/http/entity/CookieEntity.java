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
package com.feilong.servlet.http.entity;

import java.io.Serializable;

import com.feilong.core.TimeInterval;

/**
 * cookie实体,用于 {@link com.feilong.servlet.http.CookieUtil CookieUtil}.
 * 
 * <h3>关于 {@link #name} 和 {@link #value}字符说明:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>name</td>
 * <td>名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>value</td>
 * <td>名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * <h3>关于cookie大小:</h3>
 * <blockquote>
 * 
 * <p>
 * 虽然,<a href="http://tools.ietf.org/html/rfc6265">RFC 2965</a>官方文档没有说key name的长度限制, 并且鼓励支持大的 cookie<br>
 * 但是每个浏览器的实现不同,以下是最佳实践:
 * </p>
 * 
 * <p>
 * 
 * <span style="color:red">整个cookie(包含name,value,expiry date 等等)大小限制在4K,如果你想兼容大部分的浏览器,建议name小于 4000 bytes,并且整个cookie小于 4093 bytes.</span>
 * <br>
 * 有一点要注意的,如果name太大的话,那么你删不掉cookie(至少在javascript是这样的).删除一个cookie是将它设置为过期.如果name太大(比如4090 bytes),发现我不能设置过期时间,我这么做只是个兴趣,并不是真的要有一个很大name的
 * cookie!
 * </p>
 * 
 * <p>
 * 关于这个话题,如果你要兼容大部分的浏览器,那么一个domain下面不要超过50个cookies,并且每个domain下面cookie的总大小不要超过4093 bytes.<br>
 * 也就是说,所有的cookie大小不要超过4093 bytes.<br>
 * 也就是说,你可以有一个 1 个 4093 bytes大小的 cookie, 或者 2 个 2045 bytes 大小的cookies, etc.<br>
 * </p>
 * </blockquote>
 * 
 * 
 * <h3>关于 {@link #maxAge}</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>正数(positive value)</td>
 * <td>则表示该cookie会在max-age秒之后自动失效.<br>
 * 浏览器会将max-age为正数的cookie持久化,即写到对应的cookie文件中.<br>
 * 无论客户关闭了浏览器还是电脑,只要还在max-age秒之前,登录网站时该cookie仍然有效 .</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>负数(negative value)</td>
 * <td>表示不保存不持久化,当浏览器退出就会删除</td>
 * </tr>
 * <tr valign="top">
 * <td>0(zero value)</td>
 * <td>表示删除</td>
 * </tr>
 * </table>
 * 
 * <p>
 * 默认和servlet 保持一致,为-1,表示不保存不持久化, 浏览器退出就删除;也就是所谓的会话Cookie<br>
 * 如果需要设置有效期,可以调用 {@link TimeInterval}类
 * </p>
 * </blockquote>
 * 
 * 
 * <h3>关于 {@link #path}:</h3>
 * 
 * <blockquote>
 * <p>
 * 默认情况下,如果在某个页面创建了一个cookie,那么该页面所在目录中的其他页面也可以访问该cookie,如果这个目录下还有子目录,则在子目录中也可以访问.
 * </p>
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * <p>
 * 例如在www.xxxx.com/html/a.html中所创建的cookie,可以被www.xxxx.com/html/b.html或www.xxx.com/html/some/c.html所访问,<br>
 * 但不能被www.xxxx.com/d.html访问.
 * </p>
 * </blockquote>
 * 
 * <p>
 * 为了控制cookie可以访问的目录,需要使用path参数设置cookie,语法如下: document.cookie="name=value; path=cookieDir";
 * </p>
 * <p>
 * 其中cookieDir表示可访问cookie的目录.例如: document.cookie="userId=320; path=/shop"; 就表示当前cookie仅能在shop目录下使用.<br>
 * 如果要使cookie在整个网站下可用,可以将cookie_dir指定为根目录,例如: document.cookie="userId=320; path=/";
 * </p>
 * 
 * 
 * <h3>注意点:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>path 属性值有大小写之分,应与浏览器中的地址栏的输入一致</li>
 * <li>path 不可读;<br>
 * 同 expires一样,path 只可写,不可读.</li>
 * <li>path 不可更改;<br>
 * 同 expires 不一样,如果我们试图更改 path,那么实际上我们是另外写了一个 cookie,而不是更改了 path 值.</li>
 * <li>path 权限有继承性;<br>
 * 假如指定了 /test/ 目录有权限读取某 cookie,那么 /test/ 之下的目录 /test/t/ 也有权限读取该 cookie.</li>
 * </ol>
 * </blockquote>
 * 
 * </blockquote>
 * 
 * 
 * <h3>关于 {@link #domain}:</h3>
 * 
 * <blockquote>
 * <p>
 * 和路径类似,主机名是指同一个域下的不同主机,例如:www.google.com和gmail.google.com就是两个不同的主机名.<br>
 * 默认情况下,一个主机中创建的cookie在另一个主机下是不能被访问的,<br>
 * 但可以通过domain参数来实现对其的控制,<br>
 * 其语法格式为: document.cookie="name=value; domain=cookieDomain";
 * </p>
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * <p>
 * 以google为例,要实现跨主机访问,可以写为: document.cookie="name=value;domain=.google.com"; <br>
 * 这样,所有google.com下的主机都可以访问该cookie.
 * </p>
 * </blockquote>
 * 
 * </blockquote>
 * 
 * <h3>关于 {@link #httpOnly}和 {@link #secure}:</h3>
 * <blockquote>
 * <ol>
 * <li>secure属性是防止信息在传递的过程中被监听捕获后信息泄漏</li>
 * <li>HttpOnly属性的目的是防止程序(JS脚本、Applet等),获取cookie后进行攻击.</li>
 * </ol>
 * 
 * <p>
 * 但是这两个属性,并不能解决cookie在本机出现的信息泄漏的问题(FireFox的插件FireBug能直接看到cookie的相关信息)
 * </p>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="http://tools.ietf.org/html/rfc6265">HTTP State Management Mechanism</a>
 * @see <a href="http://www.ietf.org/rfc/rfc2109.txt">HTTP State Management Mechanism (废弃 被rfc6265取代)</a>
 * @see <a href="http://stackoverflow.com/questions/640938/what-is-the-maximum-size-of-a-web-browsers-cookies-key">What is the maximum size
 *      of a web browser cookie</a>
 * @see <a href="http://browsercookielimits.squawky.net/">test page and size limits for common browsers</a>
 * @since 1.0.0
 */
public class CookieEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5580364261848277853L;

    //---------------------------------------------------------------

    /** name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号. */
    private String            name;

    /**
     * value,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     * 
     * <p style="color:red">
     * 注意:如果值长度超过4K,浏览器会忽略,不会执行记录的操作
     * </p>
     * 
     * <h3>关于 non-ASCII (Unicode) characters:</h3>
     * <blockquote>
     * 
     * What isn't mentioned and browsers are totally inconsistent about, is non-ASCII (Unicode) characters:
     * 
     * <ol>
     * <li>in <b>Opera</b> and <b>Google Chrome</b>, they are encoded to Cookie headers with UTF-8;</li>
     * 
     * <li>in <b>IE</b>, the machine's default code page is used (locale-specific and never UTF-8);</li>
     * 
     * <li>
     * <b>Firefox</b> (and other Mozilla-based browsers) use the low byte of each UTF-16 code point on its own (so ISO-8859-1 is OK but
     * anything else is mangled);
     * </li>
     * 
     * <li><b>Safari</b> simply refuses to send any cookie containing non-ASCII characters.</li>
     * 
     * </ol>
     * 
     * <p>
     * so in practice you cannot use non-ASCII characters in cookies at all. <br>
     * If you want to use Unicode, control codes or other arbitrary byte sequences, <br>
     * the cookie_spec demands you use an ad-hoc encoding scheme of your own choosing and suggest URL-encoding (as produced
     * by JavaScript's encodeURIComponent) as a reasonable choice.
     * </p>
     * 
     * </blockquote>
     */
    private String            value;

    /**
     * 设置存活时间,单位秒.
     * 
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">说明</th>
     * </tr>
     * <tr valign="top">
     * <td>正数(positive value)</td>
     * <td>则表示该cookie会在max-age秒之后自动失效.<br>
     * 浏览器会将max-age为正数的cookie持久化,即写到对应的cookie文件中.无论客户关闭了浏览器还是电脑,只要还在max-age秒之前,登录网站时该cookie仍然有效 .</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>负数(negative value)</td>
     * <td>表示不保存不持久化,当浏览器退出就会删除</td>
     * </tr>
     * <tr valign="top">
     * <td>0(zero value)</td>
     * <td>表示删除</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * 默认和servlet 保持一致,为-1,表示不保存不持久化, 浏览器退出就删除,如果需要设置有效期,可以调用 {@link TimeInterval}类
     * 
     * <p>
     * ;Max-Age=VALUE
     * </p>
     * 
     * @see javax.servlet.http.Cookie#setMaxAge(int)
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.2">4.1.2.2. The Max-Age Attribute</a>
     */
    private int               maxAge           = -1;

    //---------------------------------------------------------------

    /**
     * ;Comment=VALUE ... describes cookie's use ;Discard ... implied by maxAge {@code <} 0
     */
    private String            comment;

    /**
     * ;Domain=VALUE ... domain that sees cookie.
     * 
     * <p>
     * 和路径类似,主机名是指同一个域下的不同主机,例如:www.google.com和gmail.google.com就是两个不同的主机名.<br>
     * 默认情况下,一个主机中创建的cookie在另一个主机下是不能被访问的,<br>
     * 但可以通过domain参数来实现对其的控制,<br>
     * 其语法格式为: document.cookie="name=value; domain=cookieDomain";
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * <p>
     * 以google为例,要实现跨主机访问,可以写为: document.cookie="name=value;domain=.google.com"; <br>
     * 这样,所有google.com下的主机都可以访问该cookie.
     * </p>
     * </blockquote>
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.3">4.1.2.3. The Domain Attribute</a>
     */
    private String            domain;

    /**
     * ;Path=VALUE ... URLs that see the cookie
     * 
     * <p>
     * 当不设置值的时候(tomcat默认情况),如果在某个页面创建了一个cookie,那么该页面所在目录中的其他页面也可以访问该cookie,如果这个目录下还有子目录,则在子目录中也可以访问.<br>
     * <span style="color:red">为了方便使用cookie,特意将此默认值设置为/,表示所有页面均可读取改cookie</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * <p>
     * 例如在www.xxxx.com/html/a.html中所创建的cookie,可以被www.xxxx.com/html/b.html或www.xxx.com/html/some/c.html所访问,<br>
     * 但不能被www.xxxx.com/d.html访问.
     * </p>
     * 
     * <p>
     * 为了控制cookie可以访问的目录,需要使用path参数设置cookie,语法如下: document.cookie="name=value; path=cookieDir";
     * </p>
     * 
     * <p>
     * 其中cookieDir表示可访问cookie的目录.例如: document.cookie="userId=320; path=/shop"; 就表示当前cookie仅能在shop目录下使用.<br>
     * 如果要使cookie在整个网站下可用,可以将cookie_dir指定为根目录,例如: document.cookie="userId=320; path=/";
     * </p>
     * </blockquote>
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.4">4.1.2.4. The Path Attribute</a>
     * 
     */
    private String            path             = "/";

    /**
     * ;Secure ... e.g. use SSL.
     * 
     * <p>
     * 指定是否cookie应该只通过安全协议,例如HTTPS或SSL,传送给浏览器.
     * </p>
     * 
     * <p>
     * secure值为true时,在http中是无效的; 在https中才有效
     * </p>
     * 
     * <p>
     * keep cookie communication limited to encrypted transmission, directing browsers to use cookies only
     * via secure/encrypted connections.
     * </p>
     * <p>
     * However, if a web server sets a cookie with a secure attribute from a non-secure connection, the
     * cookie can still be intercepted when it is sent to the user by man-in-the-middle attacks. <br>
     * Therefore, for maximum security, cookies with the Secure attribute should only be set over a secure connection.
     * </p>
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.5">4.1.2.5. The Secure Attribute</a>
     */
    private boolean           secure;

    /**
     * supports both the <b>Version 0 (by Netscape)</b> and <b>Version 1 (by RFC 2109)</b> cookie specifications.
     * 
     * <p>
     * By default, cookies are created using <b>Version 0</b> to ensure the best interoperability.
     * </p>
     * 
     * <p>
     * ;Version=1 ... means RFC 2109++ style
     * </p>
     * 
     */
    private int               version          = 0;

    /**
     * Not in cookie specs, but supported by browsers.
     * 
     * <p>
     * 如果在Cookie中设置了"HttpOnly"属性,那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息,这样能有效的防止XSS攻击.
     * </p>
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.6">4.1.2.6. The HttpOnly Attribute</a>
     */
    private boolean           httpOnly;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     */
    public CookieEntity(){
        super();
    }

    /**
     * The Constructor.
     * 
     * @param name
     *            name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     * @param value
     *            value,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     *            <p style="color:red">
     *            注意:如果值长度超过4K,浏览器会忽略,不会执行记录的操作
     *            </p>
     */
    public CookieEntity(String name, String value){
        this.name = name;
        this.value = value;
    }

    /**
     * The Constructor.
     *
     * @param name
     *            name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     * @param value
     *            value,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     *            <p style="color:red">
     *            注意:如果值长度超过4K,浏览器会忽略,不会执行记录的操作
     *            </p>
     * @param maxAge
     *            设置存活时间,单位秒.
     *            <blockquote>
     *            <table border="1" cellspacing="0" cellpadding="4" summary="">
     *            <tr style="background-color:#ccccff">
     *            <th align="left">字段</th>
     *            <th align="left">说明</th>
     *            </tr>
     *            <tr valign="top">
     *            <td>正数(positive value)</td>
     *            <td>则表示该cookie会在max-age秒之后自动失效.<br>
     *            浏览器会将max-age为正数的cookie持久化,即写到对应的cookie文件中.无论客户关闭了浏览器还是电脑,只要还在max-age秒之前,登录网站时该cookie仍然有效 .</td>
     *            </tr>
     *            <tr valign="top" style="background-color:#eeeeff">
     *            <td>负数(negative value)</td>
     *            <td>表示不保存不持久化,当浏览器退出就会删除</td>
     *            </tr>
     *            <tr valign="top">
     *            <td>0(zero value)</td>
     *            <td>表示删除</td>
     *            </tr>
     *            </table>
     *            </blockquote>
     * 
     *            默认和servlet 保持一致,为-1,表示不保存不持久化, 浏览器退出就删除,如果需要设置有效期,可以调用 {@link TimeInterval}类
     *            <p>
     *            ;Max-Age=VALUE
     *            </p>
     */
    public CookieEntity(String name, String value, int maxAge){
        this.name = name;
        this.value = value;
        this.maxAge = maxAge;
    }

    //---------------------------------------------------------------

    /**
     * Gets the name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     * 
     * @return the name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     * 
     * @param name
     *            the new name名称,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * value,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     * 
     * <p style="color:red">
     * 注意:如果值长度超过4K,浏览器会忽略,不会执行记录的操作
     * </p>
     * 
     * <h3>关于 non-ASCII (Unicode) characters:</h3>
     * <blockquote>
     * 
     * What isn't mentioned and browsers are totally inconsistent about, is non-ASCII (Unicode) characters:
     * 
     * <ol>
     * <li>in <b>Opera</b> and <b>Google Chrome</b>, they are encoded to Cookie headers with UTF-8;</li>
     * 
     * <li>in <b>IE</b>, the machine's default code page is used (locale-specific and never UTF-8);</li>
     * 
     * <li>
     * <b>Firefox</b> (and other Mozilla-based browsers) use the low byte of each UTF-16 code point on its own (so ISO-8859-1 is OK but
     * anything else is mangled);
     * </li>
     * 
     * <li><b>Safari</b> simply refuses to send any cookie containing non-ASCII characters.</li>
     * 
     * </ol>
     * 
     * <p>
     * so in practice you cannot use non-ASCII characters in cookies at all. <br>
     * If you want to use Unicode, control codes or other arbitrary byte sequences, <br>
     * the cookie_spec demands you use an ad-hoc encoding scheme of your own choosing and suggest URL-encoding (as produced
     * by JavaScript's encodeURIComponent) as a reasonable choice.
     * </p>
     * 
     * </blockquote>
     * 
     * @return the value,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号
     */
    public String getValue(){
        return value;
    }

    /**
     * value,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号.
     * 
     * <p style="color:red">
     * 注意:如果值长度超过4K,浏览器会忽略,不会执行记录的操作
     * </p>
     * 
     * <h3>关于 non-ASCII (Unicode) characters:</h3>
     * <blockquote>
     * 
     * What isn't mentioned and browsers are totally inconsistent about, is non-ASCII (Unicode) characters:
     * 
     * <ol>
     * <li>in <b>Opera</b> and <b>Google Chrome</b>, they are encoded to Cookie headers with UTF-8;</li>
     * 
     * <li>in <b>IE</b>, the machine's default code page is used (locale-specific and never UTF-8);</li>
     * 
     * <li>
     * <b>Firefox</b> (and other Mozilla-based browsers) use the low byte of each UTF-16 code point on its own (so ISO-8859-1 is OK but
     * anything else is mangled);
     * </li>
     * 
     * <li><b>Safari</b> simply refuses to send any cookie containing non-ASCII characters.</li>
     * 
     * </ol>
     * 
     * <p>
     * so in practice you cannot use non-ASCII characters in cookies at all. <br>
     * If you want to use Unicode, control codes or other arbitrary byte sequences, <br>
     * the cookie_spec demands you use an ad-hoc encoding scheme of your own choosing and suggest URL-encoding (as produced
     * by JavaScript's encodeURIComponent) as a reasonable choice.
     * </p>
     * 
     * </blockquote>
     * 
     * @param value
     *            the new value,名字和值都不能包含空白字符以及下列字符: @ : ;? , " / [ ] ( ) = 这些符号
     */
    public void setValue(String value){
        this.value = value;
    }

    /**
     * 获得 设置存活时间,单位秒.
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">说明</th>
     * </tr>
     * <tr valign="top">
     * <td>正数(positive value)</td>
     * <td>则表示该cookie会在max-age秒之后自动失效.<br>
     * 浏览器会将max-age为正数的cookie持久化,即写到对应的cookie文件中.无论客户关闭了浏览器还是电脑,只要还在max-age秒之前,登录网站时该cookie仍然有效 .</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>负数(negative value)</td>
     * <td>表示不保存不持久化,当浏览器退出就会删除</td>
     * </tr>
     * <tr valign="top">
     * <td>0(zero value)</td>
     * <td>表示删除</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * @return the maxAge
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.2">4.1.2.2. The Max-Age Attribute</a>
     */
    public int getMaxAge(){
        return maxAge;
    }

    /**
     * 设置 设置存活时间,单位秒.
     * <blockquote>
     * <table border="1" cellspacing="0" cellpadding="4" summary="">
     * <tr style="background-color:#ccccff">
     * <th align="left">字段</th>
     * <th align="left">说明</th>
     * </tr>
     * <tr valign="top">
     * <td>正数(positive value)</td>
     * <td>则表示该cookie会在max-age秒之后自动失效.<br>
     * 浏览器会将max-age为正数的cookie持久化,即写到对应的cookie文件中.无论客户关闭了浏览器还是电脑,只要还在max-age秒之前,登录网站时该cookie仍然有效 .</td>
     * </tr>
     * <tr valign="top" style="background-color:#eeeeff">
     * <td>负数(negative value)</td>
     * <td>表示不保存不持久化,当浏览器退出就会删除</td>
     * </tr>
     * <tr valign="top">
     * <td>0(zero value)</td>
     * <td>表示删除</td>
     * </tr>
     * </table>
     * </blockquote>
     * 
     * @param maxAge
     *            the maxAge to set
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.2">4.1.2.2. The Max-Age Attribute</a>
     */
    public void setMaxAge(int maxAge){
        this.maxAge = maxAge;
    }

    /**
     * 获得 ;Comment=VALUE .
     *
     * @return the comment
     */
    public String getComment(){
        return comment;
    }

    /**
     * 设置 ;Comment=VALUE .
     *
     * @param comment
     *            the comment to set
     */
    public void setComment(String comment){
        this.comment = comment;
    }

    /**
     * 获得 ;Domain=VALUE.
     * 
     * <p>
     * 和路径类似,主机名是指同一个域下的不同主机,例如:www.google.com和gmail.google.com就是两个不同的主机名.<br>
     * 默认情况下,一个主机中创建的cookie在另一个主机下是不能被访问的,<br>
     * 但可以通过domain参数来实现对其的控制,<br>
     * 其语法格式为: document.cookie="name=value; domain=cookieDomain";
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * <p>
     * 以google为例,要实现跨主机访问,可以写为: document.cookie="name=value;domain=.google.com"; <br>
     * 这样,所有google.com下的主机都可以访问该cookie.
     * </p>
     * </blockquote>
     * 
     * @return the domain
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.3">4.1.2.3. The Domain Attribute</a>
     */
    public String getDomain(){
        return domain;
    }

    /**
     * 设置 ;Domain=VALUE .
     * 
     * <p>
     * 和路径类似,主机名是指同一个域下的不同主机,例如:www.google.com和gmail.google.com就是两个不同的主机名.<br>
     * 默认情况下,一个主机中创建的cookie在另一个主机下是不能被访问的,<br>
     * 但可以通过domain参数来实现对其的控制,<br>
     * 其语法格式为: document.cookie="name=value; domain=cookieDomain";
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * <p>
     * 以google为例,要实现跨主机访问,可以写为: document.cookie="name=value;domain=.google.com"; <br>
     * 这样,所有google.com下的主机都可以访问该cookie.
     * </p>
     * </blockquote>
     * 
     * @param domain
     *            the domain to set
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.3">4.1.2.3. The Domain Attribute</a>
     */
    public void setDomain(String domain){
        this.domain = domain;
    }

    /**
     * 获得 ;Path=VALUE .
     *
     * ;Path=VALUE ... URLs that see the cookie
     * 
     * <p>
     * 当不设置值的时候(tomcat默认情况),如果在某个页面创建了一个cookie,那么该页面所在目录中的其他页面也可以访问该cookie,如果这个目录下还有子目录,则在子目录中也可以访问.<br>
     * <span style="color:red">为了方便使用cookie,特意将此默认值设置为/,表示所有页面均可读取改cookie</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * <p>
     * 例如在www.xxxx.com/html/a.html中所创建的cookie,可以被www.xxxx.com/html/b.html或www.xxx.com/html/some/c.html所访问,<br>
     * 但不能被www.xxxx.com/d.html访问.
     * </p>
     * 
     * <p>
     * 为了控制cookie可以访问的目录,需要使用path参数设置cookie,语法如下: document.cookie="name=value; path=cookieDir";
     * </p>
     * 
     * <p>
     * 其中cookieDir表示可访问cookie的目录.例如: document.cookie="userId=320; path=/shop"; 就表示当前cookie仅能在shop目录下使用.<br>
     * 如果要使cookie在整个网站下可用,可以将cookie_dir指定为根目录,例如: document.cookie="userId=320; path=/";
     * </p>
     * </blockquote>
     * 
     * @return the path
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.4">4.1.2.4. The Path Attribute</a>
     */
    public String getPath(){
        return path;
    }

    /**
     * 设置 ;Path=VALUE .
     *
     * ;Path=VALUE ... URLs that see the cookie
     * 
     * <p>
     * 当不设置值的时候(tomcat默认情况),如果在某个页面创建了一个cookie,那么该页面所在目录中的其他页面也可以访问该cookie,如果这个目录下还有子目录,则在子目录中也可以访问.<br>
     * <span style="color:red">为了方便使用cookie,特意将此默认值设置为/,表示所有页面均可读取改cookie</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * <p>
     * 例如在www.xxxx.com/html/a.html中所创建的cookie,可以被www.xxxx.com/html/b.html或www.xxx.com/html/some/c.html所访问,<br>
     * 但不能被www.xxxx.com/d.html访问.
     * </p>
     * 
     * <p>
     * 为了控制cookie可以访问的目录,需要使用path参数设置cookie,语法如下: document.cookie="name=value; path=cookieDir";
     * </p>
     * 
     * <p>
     * 其中cookieDir表示可访问cookie的目录.例如: document.cookie="userId=320; path=/shop"; 就表示当前cookie仅能在shop目录下使用.<br>
     * 如果要使cookie在整个网站下可用,可以将cookie_dir指定为根目录,例如: document.cookie="userId=320; path=/";
     * </p>
     * </blockquote>
     * 
     * @param path
     *            the path to set
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.4">4.1.2.4. The Path Attribute</a>
     */
    public void setPath(String path){
        this.path = path;
    }

    /**
     * ;Secure ... e.g. use SSL.
     * 
     * <p>
     * 指定是否cookie应该只通过安全协议,例如HTTPS或SSL,传送给浏览器.
     * </p>
     * 
     * <p>
     * secure值为true时,在http中是无效的; 在https中才有效
     * </p>
     * 
     * <p>
     * keep cookie communication limited to encrypted transmission, directing browsers to use cookies only
     * via secure/encrypted connections.
     * </p>
     * <p>
     * However, if a web server sets a cookie with a secure attribute from a non-secure connection, the
     * cookie can still be intercepted when it is sent to the user by man-in-the-middle attacks. <br>
     * Therefore, for maximum security, cookies with the Secure attribute should only be set over a secure connection.
     * </p>
     *
     * @return the secure
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.5">4.1.2.5. The Secure Attribute</a>
     */
    public boolean getSecure(){
        return secure;
    }

    /**
     * ;Secure ... e.g. use SSL.
     * 
     * <p>
     * 指定是否cookie应该只通过安全协议,例如HTTPS或SSL,传送给浏览器.
     * </p>
     * 
     * <p>
     * secure值为true时,在http中是无效的; 在https中才有效
     * </p>
     * 
     * <p>
     * keep cookie communication limited to encrypted transmission, directing browsers to use cookies only
     * via secure/encrypted connections.
     * </p>
     * <p>
     * However, if a web server sets a cookie with a secure attribute from a non-secure connection, the
     * cookie can still be intercepted when it is sent to the user by man-in-the-middle attacks. <br>
     * Therefore, for maximum security, cookies with the Secure attribute should only be set over a secure connection.
     * </p>
     *
     * @param secure
     *            the secure to set
     * 
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.5">4.1.2.5. The Secure Attribute</a>
     */
    public void setSecure(boolean secure){
        this.secure = secure;
    }

    /**
     * supports both the <b>Version 0 (by Netscape)</b> and <b>Version 1 (by RFC 2109)</b> cookie specifications.
     * 
     * <p>
     * By default, cookies are created using <b>Version 0</b> to ensure the best interoperability.
     * </p>
     * 
     * <p>
     * ;Version=1 ... means RFC 2109++ style
     * </p>
     * 
     * @return the version
     */
    public int getVersion(){
        return version;
    }

    /**
     * supports both the <b>Version 0 (by Netscape)</b> and <b>Version 1 (by RFC 2109)</b> cookie specifications.
     * 
     * <p>
     * By default, cookies are created using <b>Version 0</b> to ensure the best interoperability.
     * </p>
     * 
     * <p>
     * ;Version=1 ... means RFC 2109++ style
     * </p>
     * 
     * @param version
     *            the version to set
     */
    public void setVersion(int version){
        this.version = version;
    }

    /**
     * Not in cookie specs, but supported by browsers.
     * 
     * <p>
     * 如果在Cookie中设置了"HttpOnly"属性,那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息,这样能有效的防止XSS攻击.
     * </p>
     *
     * @return the httpOnly
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.6">4.1.2.6. The HttpOnly Attribute</a>
     */
    public boolean getHttpOnly(){
        return httpOnly;
    }

    /**
     * Not in cookie specs, but supported by browsers.
     * 
     * <p>
     * 如果在Cookie中设置了"HttpOnly"属性,那么通过程序(JS脚本、Applet等)将无法读取到Cookie信息,这样能有效的防止XSS攻击.
     * </p>
     *
     * @param httpOnly
     *            the httpOnly to set
     * @see <a href="http://tools.ietf.org/html/rfc6265#section-4.1.2.6">4.1.2.6. The HttpOnly Attribute</a>
     */
    public void setHttpOnly(boolean httpOnly){
        this.httpOnly = httpOnly;
    }

}