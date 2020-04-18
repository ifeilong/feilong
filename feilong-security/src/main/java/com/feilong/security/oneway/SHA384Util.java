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
 * The Class SHA384Util.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see OnewayType#SHA384
 * @since 1.14.2
 */
public final class SHA384Util{

    /** The oneway type. */
    private static final OnewayType ONEWAYTYPE = OnewayType.SHA384;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SHA384Util(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用sha384算法 单向加密字符串.
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
     * @see org.apache.commons.codec.digest.DigestUtils#sha384Hex(String)
     */
    public static String encode(String origin){
        return OnewayEncryption.encode(ONEWAYTYPE, origin);
    }

    /**
     * 使用sha384算法 单向加密字符串.
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
     * @see org.apache.commons.codec.digest.DigestUtils#sha384(byte[])
     */
    public static String encode(String origin,String charsetName){
        return OnewayEncryption.encode(ONEWAYTYPE, origin, charsetName);
    }

    //---------------------------------------------------------------

    /**
     * 计算文件的单向加密值.
     * 
     * @param filePath
     *            文件路径 {@link java.io.File#File(String)}
     * @return the string
     * @throws EncryptionException
     *             如果在加密解密的过程中发生了异常,会以EncryptionException形式抛出
     * @see OnewayEncryption#encodeFile(OnewayType, String)
     * @see org.apache.commons.codec.digest.DigestUtils#sha384Hex(java.io.InputStream)
     */
    public static String encodeFile(String filePath){
        return OnewayEncryption.encodeFile(ONEWAYTYPE, filePath);
    }
}