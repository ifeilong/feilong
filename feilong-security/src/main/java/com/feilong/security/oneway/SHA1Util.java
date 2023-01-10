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
import com.feilong.security.EncryptionException;

/**
 * Secure Hash Algorithm,安全散列算法 (单向加密).
 * 
 * <pre class="code">
 * SHA-1与MD5的比较
 * 因为二者均由MD4导出,SHA-1和MD5彼此很相似.相应的,他们的强度和其他特性也是相似,但还有以下几点不同:
 * l 对强行攻击的安全性:最显著和最重要的区别是SHA-1摘要比MD5摘要长32 位.
 *      使用强行技术,产生任何一个报文使其摘要等于给定报摘要的难度对MD5是2^128数量级的操作,而对SHA-1则是2^160数量级的操作.这样,SHA-1对强行攻击有更大的强度.
 * 
 * l 对密码分析的安全性:由于MD5的设计,易受密码分析的攻击,SHA-1显得不易受这样的攻击.
 * 
 * l 速度:在相同的硬件上,SHA-1的运行速度比MD5慢.
 * </pre>
 * 
 * <pre class="code">
 * 检验你的实现是否正确:
 * SHA1Util.encode(&quot;你好&quot;) = 440ee0853ad1e99f962b63e459ef992d7c211722
 * </pre>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.0 2010年10月26日 17:13:58
 * @version 1.0.1 2011-10-18 16:49
 * @version 1.0.2 2013-7-18 17:01 修改了javadoc 和类关系
 * @version 1.0.7 2014-7-10 14:28 update javadoc and remove extends
 * @see OnewayEncryption
 * @see OnewayType
 * @see com.feilong.lib.codec.digest.DigestUtils#sha1(String)
 * @since 1.0.0
 */
public final class SHA1Util{

    /** The oneway type. */
    private static final OnewayType ONEWAYTYPE = OnewayType.SHA1;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SHA1Util(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用sha1算法 单向加密字符串.
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制字符串
     * </p>
     * 
     * @param origin
     *            原始字符串,将使用默认的 {@link String#getBytes()} 转成字节数组<br>
     * @return 加密之后的转成小写的16进制字符串
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encode(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#sha1Hex(String)
     */
    public static String encode(String origin){
        return OnewayEncryption.encode(ONEWAYTYPE, origin);
    }

    /**
     * 使用sha1算法 单向加密字符串.
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制字符串
     * </p>
     * 
     * @param origin
     *            原始字符串,将使用默认的 value.getBytes() 转成字节数组<br>
     *            如果需要string 转码,请自行调用value.getBytes(string chartsetname),再调用{@link #encode(String, String)}
     * @param charsetName
     *            受支持的 {@link CharsetType} 名称,比如 utf-8
     * @return 加密之后的转成小写的16进制字符串
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encode(OnewayType, String, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#sha1(byte[])
     */
    public static String encode(String origin,String charsetName){
        return OnewayEncryption.encode(ONEWAYTYPE, origin, charsetName);
    }

    /**
     * 使用sha1算法 单向加密 inputBytes.
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
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encodeFile(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#sha1Hex(java.io.InputStream)
     */
    public static String encodeFile(String location){
        return OnewayEncryption.encodeFile(ONEWAYTYPE, location);
    }
}