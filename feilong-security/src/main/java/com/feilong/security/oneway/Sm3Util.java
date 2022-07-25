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

/**
 * 国产哈希算法.
 * 
 * <h3>说明:</h3>
 * 
 * <p>
 * 需要自行依赖 bcprov-jdk15on jar
 * </p>
 * 
 * <blockquote>
 * 
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
 * <pre class="code">
 * SM3是中华人民共和国政府采用的一种密码散列函数标准，由国家密码管理局于2010年12月17日发布。
 * 相关标准为“GM/T 0004-2012 《SM3密码杂凑算法》”。
 * 在商用密码体系中，SM3主要用于数字签名及验证、消息认证码生成及验证、随机数生成等，其算法公开。
 * 
 * 据国家密码管理局表示，其安全性及效率与SHA-256相当。
 * </pre>
 * 
 * <pre class="code">
 * 你可以使用以下代码来检验你的实现是否正确:
 * 
 * Sm3Util.encode(&quot;你好&quot;) = 78e5c78c5322ca174089e58dc7790acf8ce9d542bee6ae4a5a0797d5e356be61
 * </pre>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see OnewayEncryption
 * @see OnewayType#SM3
 * @see <a href="https://tools.ietf.org/html/draft-shen-sm3-hash-00">SM3 Hash function draft-shen-sm3-hash-00</a>
 * @see org.bouncycastle.crypto.digests.SM3Digest
 * @since 2.0.1
 */
public final class Sm3Util{

    /** The oneway type. */
    private static final OnewayType ONEWAYTYPE = OnewayType.SM3;

    static{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }
    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private Sm3Util(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用Sm3算法 单向加密字符串.
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
     */
    public static String encode(String origin){
        return OnewayEncryption.encode(ONEWAYTYPE, origin);
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