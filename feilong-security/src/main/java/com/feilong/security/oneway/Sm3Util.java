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

import java.security.Security;

import com.feilong.core.CharsetType;
import com.feilong.security.EncryptionException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 国产哈希算法.
 * 
 * <p>
 * SM3算法是一种密码哈希函数，由中国国家密码管理局在2010年发布，用于商用密码应用中的数字签名和验证。<span style="color:red">该算法的安全性被认为与SHA-256相当。</span>
 * 
 * SM3算法基于SHA-256的改进版本，主要包括三个阶段：初始化阶段、数据处理阶段和输出阶段。在初始化阶段，算法对输入的数据进行预处理，包括填充和添加长度信息。填充方式是在输入数据的末尾添加一个1，然后添加若干个0，直到填充满512位。长度信息是将输入数据的长度转换为64位的二进制表示，然后添加到填充后的数据末尾。
 * 
 * 在数据处理阶段，算法使用一个64字节（512位）的缓冲区来存储数据，并对缓冲区中的数据进行处理。首先将缓冲区中的数据分成16个32位的字，并进行合理的排列。然后使用一个压缩函数对排列后的数据进行处理，每次处理512位的数据。在处理过程中，算法使用一些常量和变量来完成各种操作，包括异或、与、或、非等。
 * 
 * 在输出阶段，算法将处理结果转换为256位的二进制表示，并输出为一个64位的十六进制字符串。
 * 
 * SM3算法适用于商用密码应用中的数字签名和验证，接受文本大小要小于264位，并以512位为单位分组，输出长度为256位的摘要。整个算法的执行过程可以概括成四个步骤：消息填充、消息扩展、迭代压缩、输出结果。
 * 
 * 需要注意的是，SM3算法是一种单向散列函数，只能用于将数据加密成固定长度的字符串，无法将加密后的字符串还原为原始数据。因此，在使用SM3进行数据加密时，需要谨慎选择合适的密钥和参数，以确保数据的安全性和完整性。
 * </p>
 * 
 * <p>
 * 国密算法有很多，具体包括SM1、SM2、SM3、SM4、SM7、SM9、ZUC祖冲之算法等。其中SM2、SM3、SM4是公开的
 * </p>
 * 
 * <pre class="code">
 * SM3是中华人民共和国政府采用的一种密码散列函数标准，由国家密码管理局于2010年12月17日发布。
 * 相关标准为“GM/T 0004-2012 《SM3密码杂凑算法》”。
 * 在商用密码体系中，SM3主要用于数字签名及验证、消息认证码生成及验证、随机数生成等，其算法公开。
 * 
 * <span style="color:green">据国家密码管理局表示，其安全性及效率与SHA-256相当。</span>
 * </pre>
 * 
 * <pre class="code">
 * 你可以使用以下代码来检验你的实现是否正确:
 * 
 * Sm3Util.encode(&quot;你好&quot;) = 78e5c78c5322ca174089e58dc7790acf8ce9d542bee6ae4a5a0797d5e356be61
 * </pre>
 * 
 * <p>
 * 已知 chinaums 支付方式会使用这种算法
 * </p>
 * 
 * <h3>说明:</h3>
 * 
 * <p>
 * 需要自行依赖 bcprov-jdk15on jar
 * </p>
 * 
 * <blockquote>
 * 
 * <pre>
{@code
    <dependency>
      <groupId>org.bouncycastle</groupId>
      <artifactId>bcprov-jdk15on</artifactId>
      <version>1.70</version>
    </dependency>
}
 * </pre>
 * 
 * 
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see OnewayEncryption
 * @see OnewayType#SM3
 * @see <a href="https://tools.ietf.org/html/draft-shen-sm3-hash-00">SM3 Hash function draft-shen-sm3-hash-00</a>
 * @see org.bouncycastle.crypto.digests.SM3Digest
 * @since 2.0.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Sm3Util{

    /** The oneway type. */
    private static final OnewayType ONEWAYTYPE = OnewayType.SM3;

    static{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    //---------------------------------------------------------------

    /**
     * 使用Sm3算法 单向加密字符串.
     * 
     * <p>
     * 加密之后的转成<span style="color:green">小写的</span>16进制字符串
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * Sm3Util.encode("") = "1ab21d8355cfa17f8e61194831e81a8f22bec8c728fefb747ed035eb5082aa2b"
     * Sm3Util.encode("123456") = "207cf410532f92a47dee245ce9b11ff71f578ebd763eb3bbea44ebd043d018fb"
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
     */
    public static String encode(String origin){
        return OnewayEncryption.encode(ONEWAYTYPE, origin);
    }

    /**
     * 使用Sm3算法 单向加密字符串(大写).
     * 
     * <p>
     * 加密之后的转成<span style="color:green">大写的</span>16进制字符串
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * Sm3Util.encode("") = "1AB21D8355CFA17F8E61194831E81A8F22BEC8C728FEFB747ED035EB5082AA2B"
     * Sm3Util.encode("123456") = "207CF410532F92A47DEE245CE9B11FF71F578EBD763EB3BBEA44EBD043D018FB"
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
     * @since 4.0.8
     */
    public static String encodeUpperCase(String origin){
        return OnewayEncryption.encodeUpperCase(ONEWAYTYPE, origin);
    }

    /**
     * 使用Sm3算法 单向加密字符串.
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
     */
    public static String encode(String origin,String charsetName){
        return OnewayEncryption.encode(ONEWAYTYPE, origin, charsetName);
    }

    /**
     * 使用Sm3算法 单向加密 inputBytes.
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
     * @see com.feilong.lib.codec.digest.DigestUtils#sha384Hex(java.io.InputStream)
     */
    public static String encodeFile(String location){
        return OnewayEncryption.encodeFile(ONEWAYTYPE, location);
    }

}