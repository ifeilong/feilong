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
package com.feilong.net;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * SSL协议.
 * 
 * <h3>历史:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 互联网加密通信协议的历史，几乎与互联网一样长。
 * </p>
 * 
 * <ol>
 * <li>1994年，NetScape公司设计了SSL协议（Secure Sockets Layer）的1.0版，但是未发布。</li>
 * <li>1995年，NetScape公司发布SSL 2.0版，很快发现有严重漏洞。</li>
 * <li>1996年，SSL 3.0版问世，得到大规模应用。</li>
 * <li>1999年，互联网标准化组织ISOC接替NetScape公司，发布了SSL的升级版TLS 1.0版。</li>
 * <li>2006年和2008年，TLS进行了两次升级，分别为TLS 1.1版和TLS 1.2版。最新的变动是2011年TLS 1.2的修订版。</li>
 * </ol>
 * 
 * <p>
 * 目前，应用最广泛的是TLS 1.0，接下来是SSL 3.0。但是，主流浏览器都已经实现了TLS 1.2的支持。
 * TLS 1.0通常被标示为SSL 3.1，TLS 1.1为SSL 3.2，TLS 1.2为SSL 3.3。
 * </p>
 * 
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">协议</th>
 * <th align="left">时间</th>
 * <th align="left">建议</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>SSLv1</td>
 * <td>/</td>
 * <td>/</td>
 * <td>实际从未公开发布</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>SSLv2</td>
 * <td>1995</td>
 * <td>弃用</td>
 * <td>IETF已于2011年弃用</td>
 * </tr>
 * 
 * 
 * <tr valign="top">
 * <td>SSLv3</td>
 * <td>1996</td>
 * <td>弃用</td>
 * <td>IETF已于2015年弃用</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>TLSv1.0</td>
 * <td>1999</td>
 * <td>兼容</td>
 * <td>-</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>TLSv1.1</td>
 * <td>2006</td>
 * <td>兼容</td>
 * <td>-</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>TLSv1.2</td>
 * <td>2008</td>
 * <td>主推</td>
 * <td>目前最新可用版本</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>TLSv1.3</td>
 * <td>/</td>
 * <td>/</td>
 * <td>2016开始草案制定</td>
 * </tr>
 * 
 * </table>
 * 
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="http://www.ruanyifeng.com/blog/2014/02/ssl_tls.html">SSL/TLS协议运行机制的概述</a>
 * @since 1.10.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SSLProtocol{

    /**
     * Supports some version of TLS; may support other versions.
     * 
     * <p>
     * SSL（Secure Sockets Layer）是网景公司（Netscape）设计的主要用于Web的安全传输协议。<br>
     * 
     * IETF将SSL作了标准化，即RFC2246，并将其称为TLS（Transport Layer Security），其最新版本是RFC5246,版本1.2。<br>
     * 
     * 从技术上讲，TLS1.0与SSL3.0的差异非常微小。
     * </p>
     * 
     */
    public static final String TLS    = "TLS";

    /** Supports RFC 2246: TLS version 1.0 ; may support other versions */
    public static final String TLSv1  = "TLSv1";

    /** Supports RFC 4346: TLS version 1.1 ; may support other versions */
    public static final String TLSv11 = "TLSv1.1";

    /**
     * Supports RFC 5246: TLS version 1.2 ; may support other versions
     * 
     * <p>
     * 由于TLSv1.1容易被黑客攻击，于是很多企业要求站点只提供TLSv1.2协议支持
     * </p>
     * 
     * <p>
     * TLSv1.2的实现中， oracle 从JDK1.7 update96以后的版本才开始支持
     * </p>
     * 
     * <h3>现代浏览器对TLS 1.2 默认支持的版本如下:</h3>
     * <blockquote>
     * <ol>
     * <li>Firefox: Next 6 months (either version 27 or 28)</li>
     * <li>IE version 11</li>
     * <li>Google Chrome 31</li>
     * <li>Opera 18 on Windows</li>
     * <li>Safari 7.0 on Mac</li>
     * </ol>
     * </blockquote>
     * 
     * @see <a href="https://bugs.openjdk.java.net/browse/JDK-7093640">JDK-7093640</a>
     * @see <a href="https://www.java.com/en/configure_crypto.html#enableTLSv1_2"></a>
     * @see <a href="https://wiki.openssl.org/index.php/Manual:Ciphers"> 参见TLSv1.2支持的cipher list</a>
     * @see <a href="http://blog.csdn.net/fred_lzy/article/details/74178023">https站点强制通信协议TLSv1.2</a>
     */
    public static final String TLSv12 = "TLSv1.2";

    //---------------------------------------------------------------

    /** Supports some version of SSL; may support other versions. */
    public static final String SSL    = "SSL";

    /** Supports SSL version 2 or later; may support other versions. */
    public static final String SSLv2  = "SSLv2";

    /** Supports SSL version 3; may support other versions. */
    public static final String SSLv3  = "SSLv3";

}
