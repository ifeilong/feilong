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
 * SHA-512是一种安全哈希算法，也称为SHA-2 512，它是SHA-2系列中的一种。
 * 
 * <p>
 * SHA-512是一种密码散列函数，可以将任意长度的数据映射为固定长度的512位（64字节）的散列值。
 * 
 * SHA-512由美国国家标准与技术研究院（NIST）于2001年发布，作为美国联邦信息处理标准（FIPS）。它是在SHA-2系列中继SHA-256之后发布的第二种算法。
 * 
 * SHA-512的主要用途是用于验证数据的完整性和来源，例如电子邮件、数字签名等。由于其产生的散列值长度较长，因此被认为比SHA-256更安全。然而，由于其计算复杂度较高，因此在某些场景下可能会比SHA-256慢一些。
 * 
 * 需要注意的是，SHA-512并不是一种可以解密的算法，它是一种单向散列函数，只能用于将数据加密成固定长度的字符串，无法将加密后的字符串还原为原始数据。因此，在使用SHA-512进行数据加密时，需要谨慎选择合适的密钥和参数，以确保数据的安全性和完整性。
 * </p>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see OnewayType#SHA512
 * @since 1.14.2
 */
public final class SHA512Util{

    /** The oneway type. */
    private static final OnewayType ONEWAYTYPE = OnewayType.SHA512;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SHA512Util(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用sha512算法 单向加密字符串(小写).
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制字符串
     * </p>
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * SHA512Util.encode("") = "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e"
     * SHA512Util.encode("123456") = "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413"
     * </pre>
     * 
     * </blockquote>
     * 
     * 
     * @param origin
     *            原始字符串,将使用默认的 {@link String#getBytes()} 转成字节数组<br>
     * @return 加密之后的转成小写的16进制字符串<br>
     *         如果 <code>origin</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encode(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#sha512Hex(String)
     */
    public static String encode(String origin){
        return OnewayEncryption.encode(ONEWAYTYPE, origin);
    }

    /**
     * 使用sha512算法 单向加密字符串(大写).
     * 
     * <p>
     * 加密之后的转成<span style="color:green">大写的</span>16进制字符串
     * </p>
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * SHA512Util.encodeUpperCase("") = "CF83E1357EEFB8BDF1542850D66D8007D620E4050B5715DC83F4A921D36CE9CE47D0D13C5D85F2B0FF8318D2877EEC2F63B931BD47417A81A538327AF927DA3E"
     * SHA512Util.encodeUpperCase("123456") = "BA3253876AED6BC22D4A6FF53D8406C6AD864195ED144AB5C87621B6C233B548BAEAE6956DF346EC8C17F5EA10F35EE3CBC514797ED7DDD3145464E2A0BAB413"
     * </pre>
     * 
     * </blockquote>
     * 
     * @param origin
     *            原始字符串,将使用默认的 {@link String#getBytes()} 转成字节数组<br>
     * @return 加密之后的转成大写的16进制字符串<br>
     *         如果 <code>origin</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encodeUpperCase(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#sha512Hex(String)
     * @since 4.0.8
     */
    public static String encodeUpperCase(String origin){
        return OnewayEncryption.encodeUpperCase(ONEWAYTYPE, origin);
    }

    /**
     * 使用sha512算法 单向加密字符串(小写).
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
     * @return 加密之后的转成小写的16进制字符串<br>
     *         如果 <code>origin</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encode(OnewayType, String, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#sha512(byte[])
     */
    public static String encode(String origin,String charsetName){
        return OnewayEncryption.encode(ONEWAYTYPE, origin, charsetName);
    }

    /**
     * 使用sha512算法 单向加密 inputBytes.
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
     * @see com.feilong.lib.codec.digest.DigestUtils#sha512Hex(java.io.InputStream)
     */
    public static String encodeFile(String location){
        return OnewayEncryption.encodeFile(ONEWAYTYPE, location);
    }
}