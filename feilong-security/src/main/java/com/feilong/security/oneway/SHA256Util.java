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
 * SHA256是一种密码散列函数，也称为哈希函数。
 * 
 * <p>
 * SHA256是由美国标准与技术研究所（NIST）设计并于1993年发表的密码散列函数，属于SHA-2系列。<br>
 * 在第一个区块链系统创立时（2008年），SHA256被公认为是最安全最先进的算法之一，因此被广泛应用于区块链系统中的哈希运算。
 * </p>
 * 
 * <p>
 * 它是一种将任意长度的数据映射为固定长度（256位）的散列值的算法。<br>
 * 
 * SHA256非常安全，因为即使输入数据只有微小的差异，输出的散列值也会有很大的不同。
 * 这意味着，如果攻击者尝试通过修改输入数据来生成相同的散列值，他们需要做出很大的努力。
 * 
 * SHA256是SHA-2下细分出的一种算法，SHA-2下又可再分为六个不同的算法标准，包括了：SHA-224、SHA-256、SHA-384、SHA-512、SHA-512/224、SHA-512/256。这些变体除了生成摘要的长度、循环运行的次数等一些微小差异外，算法的基本结构是一致的。
 * 
 * SHA256非常流行，被广泛应用于各种加密和安全应用中，如数字签名、身份验证和数据完整性检查等。它也是比特币等加密货币中使用的哈希函数之一，用于确保交易的安全性和不可篡改性。
 * </p>
 * 
 * <h3>对比 SHA256 和md5:</h3>
 * <blockquote>
 * SHA256和MD5都是密码散列函数，它们都可以将任意长度的数据映射为固定长度的散列值。但是，它们在安全性、碰撞率和性能方面存在显著差异。
 * 
 * <ol>
 * <li><b>安全性：</b>
 * SHA256的安全性远高于MD5。MD5算法已经被证明存在严重的安全漏洞，攻击者可以利用某些方法生成具有相同散列值的恶意数据，这被称为“碰撞攻击”。相比之下，SHA256被认为是更加安全的算法，尽管它也并不是完全无懈可击的。</li>
 * <li><b>碰撞率：</b>
 * SHA256的碰撞几率远低于MD5。这意味着，尽管所有散列函数都存在理论上可能的碰撞，但在现实中，SHA256的碰撞情况更少见。</li>
 * <li><b>性能：</b>
 * SHA256相对于MD5需要更多的计算资源和时间来处理数据。因此，在需要高并发的场景下，使用SHA256可能会带来更高的性能开销。</li>
 * </ol>
 * 
 * 综上所述，SHA256相对于MD5在安全性、碰撞率和性能方面具有优势。在需要高安全性的应用中，建议使用SHA256而不是MD5。
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see OnewayType#SHA256
 * @since 1.14.2
 */
public final class SHA256Util{

    /** The oneway type. */
    private static final OnewayType ONEWAYTYPE = OnewayType.SHA256;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SHA256Util(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用sha256算法 单向加密字符串(小写).
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制字符串
     * </p>
     * 
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * SHA256Util.encode("") = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
     * SHA256Util.encode("123456") = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"
     * </pre>
     * 
     * </blockquote>
     * 
     * @param origin
     *            原始字符串,将使用默认的 {@link String#getBytes()} 转成字节数组<br>
     * @return 加密之后的转成小写的16进制字符串 <br>
     *         如果 <code>origin</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encode(OnewayType, String)
     * @see com.feilong.lib.codec.digest.DigestUtils#sha256Hex(String)
     */
    public static String encode(String origin){
        return OnewayEncryption.encode(ONEWAYTYPE, origin);
    }

    /**
     * 使用sha256算法 单向加密字符串(大写).
     * 
     * <p>
     * 加密之后的转成<span style="color:green">大写的</span>16进制字符串
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * SHA256Util.encodeUpperCase("") = "E3B0C44298FC1C149AFBF4C8996FB92427AE41E4649B934CA495991B7852B855"
     * SHA256Util.encodeUpperCase("123456") = "8D969EEF6ECAD3C29A3A629280E686CF0C3F5D5A86AFF3CA12020C923ADC6C92"
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
     * @see com.feilong.lib.codec.digest.DigestUtils#sha256Hex(String)
     */
    public static String encodeUpperCase(String origin){
        return OnewayEncryption.encodeUpperCase(ONEWAYTYPE, origin);
    }

    /**
     * 使用sha256算法 单向加密字符串.
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
     * @see com.feilong.lib.codec.digest.DigestUtils#sha256(byte[])
     */
    public static String encode(String origin,String charsetName){
        return OnewayEncryption.encode(ONEWAYTYPE, origin, charsetName);
    }

    /**
     * 使用sha256算法 单向加密 inputBytes.
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
     * @see com.feilong.lib.codec.digest.DigestUtils#sha256Hex(java.io.InputStream)
     */
    public static String encodeFile(String location){
        return OnewayEncryption.encodeFile(ONEWAYTYPE, location);
    }
}