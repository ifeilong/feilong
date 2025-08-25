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
package com.feilong.security.oneway;

import com.feilong.core.CharsetType;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Message Digest algorithm 5,信息摘要算法.
 * 
 * <p>
 * 将任意长度的"字节串"变换成一个128bit的大整数.
 * </p>
 * 
 * <p style="color:red">
 * MD5加密非常不安全,建议使用 {@link SHA1Util}
 * </p>
 * 
 * <pre class="code">
 * 检验你的实现是否正确:
 * MD5Util.encode(&quot;&quot;) = d41d8cd98f00b204e9800998ecf8427e
 * MD5Util.encode(&quot;a&quot;) = 0cc175b9c0f1b6a831c399e269772661
 * </pre>
 * 
 * @author 腾讯通
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.0 2010年10月26日 17:13:58
 * @version 1.0.1 2011-10-18 16:49
 * @version 1.0.7 2014-7-10 14:28 update javadoc and remove extends
 * 
 * @see <a href="http://www.cmd5.com/">MD5解密网站</a>
 * @see com.feilong.security.oneway.OnewayEncryption
 * @see com.feilong.security.oneway.OnewayType
 * @see com.feilong.lib.codec.digest.DigestUtils#md5Hex(String)
 * @see "org.springframework.util.DigestUtils"
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MD5Util{

    /** The oneway type. */
    private static final OnewayType ONEWAYTYPE = OnewayType.MD5;

    //---------------------------------------------------------------

    /**
     * 使用md5算法 单向加密字符串(小写格式).
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制,长度32位的字符串<br>
     * 如果你要大写的值,请调用 {@link #encodeUpperCase(String)}
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * MD5Util.encode("") = "d41d8cd98f00b204e9800998ecf8427e"
     * MD5Util.encode("123456") = "e10adc3949ba59abbe56e057f20f883e"
     * </pre>
     * 
     * </blockquote>
     *
     * @param origin
     *            原始字符串,将使用默认的 {@link String#getBytes()} 转成字节数组<br>
     *            如果 <code>origin</code> 是null,抛出 {@link NullPointerException}<br>
     * @return 加密之后的转成<span style="color:green">小写的</span>16进制字符串
     * @see OnewayEncryption#encode(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#md5Hex(String)
     */
    public static String encode(String origin){
        return OnewayEncryption.encode(ONEWAYTYPE, origin);
    }

    /**
     * 使用md5算法 单向加密字符串返回大写格式.
     * 
     * <p>
     * 加密之后的转成<span style="color:green">大写的</span>16进制,长度32位的字符串;<br>
     * 如果你要小写的值,请调用 {@link #encode(String)}
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * MD5Util.encodeUpperCase("") = "D41D8CD98F00B204E9800998ECF8427E"
     * MD5Util.encodeUpperCase("123456") = "E10ADC3949BA59ABBE56E057F20F883E"
     * </pre>
     * 
     * </blockquote>
     *
     * @param origin
     *            原始字符串,将使用默认的 {@link String#getBytes()} 转成字节数组<br>
     *            如果 <code>origin</code> 是null,抛出 {@link NullPointerException}<br>
     * @return 加密之后的转成<span style="color:green">大写的</span>16进制字符串
     * @see OnewayEncryption#encodeUpperCase(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#md5Hex(String)
     * @since 4.0.8
     */
    public static String encodeUpperCase(String origin){
        return OnewayEncryption.encodeUpperCase(ONEWAYTYPE, origin);
    }

    /**
     * 使用md5算法 单向加密字符串.
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制,长度32位的字符串
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * assertEquals("7eca689f0d3389d9dea66ae112e5cfd7", MD5Util.encode("你好", UTF8));
     * assertEquals("670b14728ad9902aecba32e22fa4f6bd", MD5Util.encode("000000", UTF8));
     * </pre>
     * 
     * </blockquote>
     *
     * @param origin
     *            原始字符串,将使用默认的 value.getBytes() 转成字节数组<br>
     *            如果需要string 转码,请自行调用value.getBytes(string chartsetname),再调用{@link #encode(String, String)}
     * @param charsetName
     *            受支持的 {@link CharsetType} 名称,比如 utf-8
     * @return 加密之后的转成 <span style="color:green">小写的</span>16进制字符串
     *         如果 <code>origin</code> 是null,抛出 {@link NullPointerException}<br>
     * @see OnewayEncryption#encode(OnewayType, String, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#md5Hex(byte[])
     */
    public static String encode(String origin,String charsetName){
        return OnewayEncryption.encode(ONEWAYTYPE, origin, charsetName);
    }

    /**
     * 使用md5算法 单向加密 inputBytes.
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制,长度32位的字符串
     * </p>
     *
     * @param inputBytes
     *            the input bytes
     * @return 加密之后的转成 <span style="color:green">小写的</span>16进制字符串
     * @see OnewayEncryption#encode(OnewayType, byte[])
     * @since 3.4.0
     */
    public static String encode(byte[] inputBytes){
        return OnewayEncryption.encode(ONEWAYTYPE, inputBytes);
    }

    //---------------------------------------------------------------

    /**
     * 计算文件的单向加密值.
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see OnewayEncryption#encodeFile(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#md5Hex(java.io.InputStream)
     */
    public static String encodeFile(String location){
        return OnewayEncryption.encodeFile(ONEWAYTYPE, location);
    }
}