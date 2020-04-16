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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;

import com.feilong.core.lang.StringUtil;
import com.feilong.security.ByteUtil;

/**
 * 对称加密解密工具
 * 
 * <h3>特点:</h3> <blockquote>
 * <ul>
 * <li>支持spring 参数注入
 * 
 * <pre class="code">
 * {@code
 *  <bean id="blowfishForPassword" class="com.feilong.tools.security.symmetric.SymmetricEncryption" lazy-init="true">
 *      <!-- 第1个参数是加密解密方式 -->
 *      <constructor-arg index="0" value="Blowfish" />
 *      <!-- 第2个参数是密钥字符串 -->
 *      <constructor-arg index="1" value="feilong" />
 *  </bean>
 * }
 * </pre>
 * 
 * </li>
 * <li>支持多种双向加密类型:{@link SymmetricType}</li>
 * </ul>
 * </blockquote>
 * 
 * 
 * <h3>支持的类型:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link SymmetricType#DES}</li>
 * <li>{@link SymmetricType#DESede}</li>
 * <li>{@link SymmetricType#AES}</li>
 * <li>{@link SymmetricType#Blowfish}</li>
 * <li>{@link SymmetricType#RC2}</li>
 * <li>{@link SymmetricType#RC4}</li>
 * <li>{@link SymmetricType#ARCFOUR}</li>
 * </ul>
 * </blockquote>
 * 
 * 
 * <h3>两种对称加密解密方式:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link #encryptBase64(String, String)},{@link #decryptBase64(String, String)}<br>
 * 将加密之后的字节码,使用 Base64封装返回.</li>
 * <li>{@link #encryptHex(String, String)},{@link #decryptHex(String, String)}<br>
 * 将加密之后的字节码,使用<b>大写的</b> Hex十六进制码形式封装返回<b>(推荐使用这种,生成的字符串不会有特殊字符比如=号,可用于url参数传递)</b></li>
 * </ul>
 * </blockquote>
 * 
 * 
 * <h3>使用示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * {@code
 * Example 1,encryptHex加密:
 *      String original = "鑫哥爱feilong";
 *      String keyString = "feilong";
 * 
 *      SymmetricEncryption symmetricEncryption = new SymmetricEncryption(SymmetricType.Blowfish, keyString);
 *      LOGGER.info(symmetricEncryption.encryptHex(original,UTF8));
 *      
 *      输出:055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
 * 
 * Example 2,decryptHex解密:
 *      String keyString = "feilong";
 *      String hexString = "055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6";
 * 
 *      SymmetricEncryption symmetricEncryption = new SymmetricEncryption(SymmetricType.Blowfish, keyString);
 *      LOGGER.info(symmetricEncryption.decryptHex(hexString,UTF8));
 * 
 *      输出:鑫哥爱feilong
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0.0 2011-12-26 上午11:05:53
 * @version 1.0.1 2013-1-15 15:18 json log
 * @version 1.0.7 2014-6-5 16:26 丰富了javadoc
 * @see javax.crypto.Cipher
 * @see javax.crypto.Cipher#ENCRYPT_MODE
 * @see javax.crypto.Cipher#DECRYPT_MODE
 * @see javax.crypto.KeyGenerator
 * @see java.security.Key
 * @see org.apache.commons.codec.binary.Base64
 * @see SymmetricType
 * @see #encryptBase64(String, String)
 * @see #decryptBase64(String, String)
 * @see #encryptHex(String, String)
 * @see #decryptHex(String, String)
 */
public class SymmetricEncryption extends AbstractSymmetricEncryption{

    /**
     * 构造函数(固定枚举支持范围).
     *
     * @param symmetricType
     *            the symmetric type
     * @param keyString
     *            自定义密钥字符串
     * @see SymmetricType
     * @see #SymmetricEncryption(SymmetricType, String, CipherMode, CipherPadding)
     */
    public SymmetricEncryption(SymmetricType symmetricType, String keyString){
        this(symmetricType, keyString, null, null);
    }

    //---------------------------------------------------------------

    /**
     * 构造函数.
     *
     * @param symmetricType
     *            the symmetric type
     * @param keyString
     *            自定义密钥字符串
     * @param cipherMode
     *            the cipher mode
     * @param cipherPadding
     *            the cipher padding
     * @see SymmetricType
     * @see "javax.crypto.Cipher#tokenizeTransformation(String)"
     * @since 1.0.7
     */
    public SymmetricEncryption(SymmetricType symmetricType, String keyString, CipherMode cipherMode, CipherPadding cipherPadding){
        Validate.notBlank(keyString, "keyString can't be null/empty!");
        Validate.notNull(symmetricType, "symmetricType can't be null!");

        //---------------------------------------------------------------
        SymmetricEncryptionConfig config = new SymmetricEncryptionConfig();
        config.setSymmetricType(symmetricType);
        config.setKeyString(keyString);
        config.setCipherMode(cipherMode);
        config.setCipherPadding(cipherPadding);
        init(config);
    }

    /**
     * 构造函数.
     *
     * @param config
     *            the symmetric encryption config
     * @see SymmetricType
     * @see "javax.crypto.Cipher#tokenizeTransformation(String)"
     * @since 1.11.0
     */
    public SymmetricEncryption(SymmetricEncryptionConfig config){
        init(config);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.security.symmetric.AbstractSymmetricEncryption#doEncryptBase64(java.lang.String, java.lang.String)
     */
    @Override
    protected String doEncryptBase64(String original,String charsetName){
        byte[] encryptBytes = toEncryptBytes(original, charsetName);
        return Base64.encodeBase64String(encryptBytes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.security.symmetric.AbstractSymmetricEncryption#doDecryptBase64(java.lang.String, java.lang.String)
     */
    @Override
    protected String doDecryptBase64(String base64String,String charsetName){
        byte[] byteMi = Base64.decodeBase64(base64String);
        return toDecryptString(byteMi, charsetName);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.security.symmetric.AbstractSymmetricEncryption#doEncryptHex(java.lang.String, java.lang.String)
     */
    @Override
    protected String doEncryptHex(String original,String charsetName){
        byte[] encryptBytes = toEncryptBytes(original, charsetName);
        return ByteUtil.bytesToHexStringUpperCase(encryptBytes);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.security.symmetric.AbstractSymmetricEncryption#doDecryptHex(java.lang.String, java.lang.String)
     */
    @Override
    protected String doDecryptHex(String hexString,String charsetName){
        byte[] bs = ByteUtil.hexBytesToBytes(StringUtil.getBytes(hexString, charsetName));
        return toDecryptString(bs, charsetName);
    }

}