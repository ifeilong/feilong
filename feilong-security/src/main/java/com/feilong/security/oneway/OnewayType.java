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

/**
 * 单向加密类型.
 * 
 * <p>
 * 支持以下的类型:
 * </p>
 * 
 * <ul>
 * <li>{@link #MD5},Message Digest algorithm 5(信息摘要算法),if</li>
 * <li>{@link #SHA},Secure Hash Algorithm(安全散列算法)</li>
 * <li>{@link #SHA1},Secure Hash Algorithm(安全散列算法)</li>
 * <li>{@link #SHA256}<span style="color:red">(推荐使用)</span>,Secure Hash Algorithm(安全散列算法)</li>
 * <li>{@link #SHA384},Secure Hash Algorithm(安全散列算法)</li>
 * <li>{@link #SHA512},Secure Hash Algorithm(安全散列算法)</li>
 * </ul>
 * 
 * 注:SHA家族的五个算法,分别是SHA-1、SHA-224(java 不支持)、SHA-256、SHA-384,和SHA-512<br>
 * 支持的单向加密类型,也可以参阅 {@link java.security.MessageDigestSpi}的实现类
 * 
 * <h3>String origin = "你好"; 结果对比:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * String origin = &quot;你好&quot;;
 * OnewayEncryption.encode(OnewayType.MD5, origin, UTF8)));
 * OnewayEncryption.encode(OnewayType.SHA, origin, UTF8)));
 * OnewayEncryption.encode(OnewayType.SHA1, origin, UTF8)));
 * OnewayEncryption.encode(OnewayType.SHA256, origin, UTF8)));
 * OnewayEncryption.encode(OnewayType.SHA384, origin, UTF8)));
 * OnewayEncryption.encode(OnewayType.SHA512, origin, UTF8)));
 *  
 * MD5:     7eca689f0d3389d9dea66ae112e5cfd7 [32]
 * SHA:     440ee0853ad1e99f962b63e459ef992d7c211722 [40]
 * SHA1:    440ee0853ad1e99f962b63e459ef992d7c211722 [40]
 * SHA256:  670d9743542cae3ea7ebe36af56bd53648b0a1126162e78d81a32934a711302e [64]
 * SHA384:  05f076c7d180e91d80a56d70b226fca01e2353554c315ac1e8caaaeca2ce0dc0d9d84e206a2bf1143a0ae1b9be9bcfa8 [96]
 * SHA512:  5232181bc0d9888f5c9746e410b4740eb461706ba5dacfbc93587cecfc8d068bac7737e92870d6745b11a25e9cd78b55f4ffc706f73cfcae5345f1b53fb8f6b5 [128]
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>SHA-1与MD5的比较</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 因为二者均由MD4导出,SHA-1和MD5彼此很相似.相应的,他们的强度和其他特性也是相似,但还有以下几点不同:
 * </p>
 * 
 * <ol>
 * <li>对强行攻击的安全性:最显著和最重要的区别是SHA-1摘要比MD5摘要长32 位.<br>
 * 使用强行技术,产生任何一个报文使其摘要等于给定报摘要的难度对MD5是<b>2^128</b>数量级的操作,而对SHA-1则是<b>2^160</b>数量级的操作.<br>
 * 这样,SHA-1对强行攻击有更大的强度.</li>
 * <li>对密码分析的安全性:由于MD5的设计,易受密码分析的攻击,SHA-1显得不易受这样的攻击.</li>
 * <li>速度:在相同的硬件上,SHA-1的运行速度比MD5慢.</li>
 * </ol>
 * 
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.0 2012-3-25 上午7:19:44
 * @version 1.0.2 2013-1-14 20:27 增加 SHA-1、 SHA-256、SHA-384,和SHA-512
 * @see java.security.MessageDigestSpi
 * @see com.feilong.lib.codec.digest.MessageDigestAlgorithms
 */
//无访问控制符修饰的内容可以被同一个包中的类访问,
enum OnewayType{

    /**
     * Message Digest algorithm 5,信息摘要算法.
     * 
     * <p>
     * 将任意长度的"字节串"变换成一个128bit的大整数.
     * </p>
     */
    MD5("MD5"),

    /**
     * 国产哈希算法.
     * 
     * <pre class="code">
     * SM3是中华人民共和国政府采用的一种密码散列函数标准，由国家密码管理局于2010年12月17日发布。
     * 相关标准为“GM/T 0004-2012 《SM3密码杂凑算法》”。
     * 在商用密码体系中，SM3主要用于数字签名及验证、消息认证码生成及验证、随机数生成等，其算法公开。
     * 
     * 据国家密码管理局表示，其安全性及效率与SHA-256相当。
     * </pre>
     * 
     * @since 2.0.1
     */
    SM3("SM3"),

    // *****SHA家族的五个算法,分别是SHA-1、SHA-224(java 不支持)、SHA-256、SHA-384,和SHA-512
    /**
     * 值和 {@link #SHA1} 其实是一样的.
     * 
     * <p>
     * 注:SHA家族的五个算法,分别是SHA-1、SHA-224(java 不支持)、SHA-256、SHA-384,和SHA-512
     * </p>
     * 
     * @see #SHA1
     */
    SHA("SHA"),

    /**
     * Secure Hash Algorithm,安全散列算法.
     * 
     * <p>
     * 在1993年,安全散列算法(SHA)由美国国家标准和技术协会(NIST)提出,并作为联邦信息处理标准(FIPS PUB 180)公布; 1995年又发布了一个修订版FIPS PUB 180-1,通常称之为SHA-1.
     * SHA-1是基于MD4算法的,并且它的设计在很大程度上是模仿MD4的.现在已成为公认的最安全的散列算法之一,并被广泛使用.
     * </p>
     * 
     * <br>
     * 注:SHA家族的五个算法,分别是SHA-1、SHA-224(java 不支持)、SHA-256、SHA-384,和SHA-512
     * 
     * <p>
     * 摘要算法不够安全，替换者是 {@link #SHA256}
     * </p>
     * 
     * @see #SHA
     */
    SHA1("SHA-1"),

    // java.lang.IllegalArgumentException: No such algorithm [SHA-2]
    // SHA2("SHA-2"),

    // No such algorithm [SHA-224] java不支持 java.lang.IllegalArgumentException:
    // SHA224("SHA-224"),

    /**
     * The SHa256.
     * 
     * <p>
     * 注:SHA家族的五个算法,分别是SHA-1、SHA-224(java 不支持)、SHA-256、SHA-384,和SHA-512
     * </p>
     */
    SHA256("SHA-256"),

    /**
     * The SHa384.
     * 
     * <p>
     * 注:SHA家族的五个算法,分别是SHA-1、SHA-224(java 不支持)、SHA-256、SHA-384,和SHA-512
     * </p>
     */
    SHA384("SHA-384"),

    /**
     * The SHa512.
     * 
     * <p>
     * 注:SHA家族的五个算法,分别是SHA-1、SHA-224(java 不支持)、SHA-256、SHA-384,和SHA-512
     * </p>
     */
    SHA512("SHA-512");

    //---------------------------------------------------------------

    /** 算法. */
    private String algorithm;

    /**
     * Instantiates a new oneway type.
     * 
     * @param algorithm
     *            the algorithm
     */
    private OnewayType(String algorithm){
        this.algorithm = algorithm;
    }

    /**
     * Gets the 算法.
     * 
     * @return the algorithm
     */
    public String getAlgorithm(){
        return algorithm;
    }

}