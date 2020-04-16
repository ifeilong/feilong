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
package com.feilong.security.symmetric;

/**
 * 对称加密的类型.
 * 
 * <h3>对比</h3>
 * 
 * <blockquote>
 * <p>
 * Use AES.
 * 
 * In more details:
 * <a href="http://stackoverflow.com/questions/5554526/comparison-of-des-triple-des-aes-blowfish-encryption-for-data">Comparison of
 * DES, Triple DES, AES, blowfish encryption for data</a>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #DES}</td>
 * <td>is the old "data encryption standard" from the seventies. <br>
 * Its key size is too short for proper security (56 effective bits; this can be brute-forced, as has been demonstrated more than ten years
 * ago). <br>
 * Also, DES uses 64-bit blocks, which raises some potential issues when encrypting several gigabytes of data with the same key (a gigabyte
 * is not that big nowadays).</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #DESede} or {@link #TripleDES}</td>
 * <td>is a trick to reuse DES implementations, by cascading three instances of DES (with distinct keys). <br>
 * 3DES is believed to be secure up to at least "2112" security (which is quite a lot, and quite far in the realm of
 * "not breakable with today's technology"). <br>
 * But <span style="color:red">it is slow</span>, especially in software (DES was designed for efficient hardware implementation, but it
 * sucks in software; and 3DES sucks three times as much).</td>
 * </tr>
 * 
 * <tr valign="top">
 * <td>{@link #Blowfish}</td>
 * <td>is a block cipher proposed by Bruce Schneier, and deployed in some softwares. <br>
 * Blowfish can use huge keys and is believed secure, except with regards to its block size, which is 64 bits, just like DES and 3DES.
 * Blowfish is efficient in software, at least on some software platforms (it uses key-dependent lookup tables, hence performance depends on
 * how the platform handles memory and caches).</td>
 * </tr>
 * 
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>{@link #AES}<span style="color:red">(推荐使用)</span></td>
 * <td>is the successor of DES as standard symmetric encryption algorithm for US federal organizations (and as standard for pretty much
 * everybody else, too). <br>
 * AES accepts keys of 128, 192 or 256 bits (128 bits is already very unbreakable), uses 128-bit blocks (so no issue there), and is
 * efficient in both software and hardware. <br>
 * It was selected through an open competition involving hundreds of cryptographers during several years. <br>
 * <span style="color:green">Basically, you cannot have better than that.</span></td>
 * </tr>
 * 
 * </table>
 * </blockquote>
 * 
 * So, when in doubt, use AES.<br>
 * 
 * Note that a block cipher is a box which encrypts "blocks" (128-bit chunks of data with AES). When encrypting a "message" which may be
 * longer than 128 bits, the message must be split into blocks, and the actual way you do the split is called the mode of operation or
 * "chaining". The naive mode (simple split) is called ECB and has issues. Using a block cipher properly is not easy, and it is more
 * important than selecting between, e.g., AES or 3DES.
 * </p>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2012-3-24 下午11:36:22
 * @see <a href="http://tripledes.online-domain-tools.com/">加解密在线测试网站</a>
 * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#AppA">JCA Reference Guide</a>
 * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html">JCA Standard Algorithm Name
 *      Documentation</a>
 */
public enum SymmetricType{

    /**
     * Data Encryption Standard,即数据加密算法.
     * 
     * <p>
     * 数据示例: <span style="color:green">LdCGo0dplVASWwJrvlHqpw==</span>
     * </p>
     * <p>
     * key size must be equal to 56
     * </p>
     * <p>
     * 它是IBM公司于1975年研究成功并公开发表的
     * </p>
     * <p>
     * DES算法把64位的明文输入块变为64位的密文输出块,它所使用的密钥也是64位<br>
     * DES共有四种工作模式-->>ECB:电子密码本模式、CBC:加密分组链接模式、CFB:加密反馈模式、OFB:输出反馈模式
     * </p>
     * 最常用的对称加密算法,安全性较差,<br>
     * The Digital Encryption Standard as described in FIPS PUB 46-2.
     */
    DES("DES"),

    /**
     * Triple DES Encryption (DES-EDE),针对DES安全性的改进产生了能满足当前安全需要的TripleDES算法,等于 {@link #TripleDES}
     * <p>
     * 数据示例: <span style="color:green">sIVcl7DB9hzAsiGKGFVJ2g==</span>
     * </p>
     * <p>
     * key size must be equal to 112 or 168.
     * </p>
     * <br>
     * 3DES(或称为Triple DES)是三重数据加密算法(TDEA,Triple Data Encryption Algorithm)块密码的通称.<br>
     * 它相当于是对每个数据块应用三次DES加密算法.由于计算机运算能力的增强,原版DES密码的密钥长度变得容易被暴力破解;<br>
     * 3DES即是设计用来提供一种相对简单的方法,即通过增加DES的密钥长度来避免类似的攻击,而不是设计一种全新的块密码算法.
     * 
     * @see <a href="http://tripledes.online-domain-tools.com/">加解密在线测试网站</a>
     */
    DESede("DESede"),

    /**
     * The Triple des.
     * 
     * @deprecated please use {@link #DESede}
     */
    @SuppressWarnings("dep-ann")
    TripleDES("TripleDES"),

    /**
     * <span style="color:red">(对称加密首选)</span> <span style="color:red">A</span>dvanced <span style="color:red">E</span>ncryption
     * <span style="color:red">S</span>tandard as specified by NIST in a draft FIPS.
     * 
     * <p>
     * 是替代DES算法的新算法,可提供很好的安全性.
     * </p>
     * 
     * <p>
     * 数据示例: <span style="color:green">MKNbK/ieTaepCk8SefgPMw==</span>
     * </p>
     * 
     * Based on the Rijndael algorithm by Joan Daemen and Vincent Rijmen<br>
     * AES is a 128-bit block cipher supporting keys of 128, 192, and 256 bits<br>
     */
    AES("AES"),

    /**
     * Blowfish.
     * <p>
     * 数据示例: <span style="color:green">BVl2k0U5+qrX8Otcg/4NXQ==</span>
     * </p>
     * The block cipher designed by Bruce Schneier,key size must be multiple of 8, and can only range from 32 to 448 (inclusive)<br>
     * 密钥长度可达448位.
     */
    Blowfish("Blowfish"),

    /**
     * RC2.
     * <p>
     * 数据示例: <span style="color:green">CyJ22S/ct5YAhv5wMCTFZQ==</span>
     * </p>
     * key size must be between 40 and 1024 bits.
     */
    RC2("RC2"),

    /**
     * RC4.
     * <p>
     * 数据示例: <span style="color:green">Jo5UARgjNRbDaL0VW77a</span>
     * </p>
     * <br>
     * s key size must be between 40 and 1024 bits.
     */
    RC4("RC4"),

    /**
     * ARCFOUR.
     * <p>
     * 数据示例: <span style="color:green">R1qRmIN8s4VY7OTRspIA</span>
     * </p>
     */
    ARCFOUR("ARCFOUR");

    // java.security.NoSuchAlgorithmException: RSA KeyGenerator not available
    // java.security.InvalidKeyException: No installed provider supports this key: (null)
    // RSA,

    // java.security.NoSuchAlgorithmException: Cannot find any provider supporting RC5
    // RC5,

    // java.security.NoSuchAlgorithmException: Cannot find any provider supporting Serpent
    // Serpent,

    // Cannot find any provider supporting Twofish
    // Twofish,

    // PBEWithMD5AndDES KeyGenerator not available
    // PBEWithMD5AndDES,

    //---------------------------------------------------------------

    // Cannot find any provider supporting PBE
    // PBE,

    // Cannot find any provider supporting HMAC
    // HMAC,

    // Cannot find any provider supporting HmacMD5
    // HmacMD5,

    //Cannot find any provider supporting HmacSHA1
    //HMAC(Hash Message Authentication Code,散列消息鉴别码,基于密钥的Hash算法的认证协议。
    //    HmacSHA1("HMAC-SHA1");

    //---------------------------------------------------------------

    /** 算法. */
    private String algorithm;

    //---------------------------------------------------------------

    /**
     * Instantiates a new symmetric type.
     *
     * @param algorithm
     *            the algorithm
     */
    private SymmetricType(String algorithm){
        this.algorithm = algorithm;
    }

    //---------------------------------------------------------------

    /**
     * Gets the 算法.
     * 
     * @return the algorithm
     */
    public String getAlgorithm(){
        return algorithm;
    }

}