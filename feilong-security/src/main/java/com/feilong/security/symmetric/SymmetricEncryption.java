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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.security.symmetric.LogBuilder.errorMessage;

import java.security.Key;

import com.feilong.core.CharsetType;
import com.feilong.core.Validate;
import com.feilong.core.lang.StringUtil;
import com.feilong.lib.codec.binary.Base64;
import com.feilong.security.ByteUtil;
import com.feilong.security.EncryptionException;
import com.feilong.security.symmetric.builder.DefaultKeyBuilder;
import com.feilong.security.symmetric.builder.KeyBuilder;
import com.feilong.security.symmetric.builder.TransformationBuilder;

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
 *      log.info(symmetricEncryption.encryptHex(original,UTF8));
 *      
 *      输出:055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
 * 
 * Example 2,decryptHex解密:
 *      String keyString = "feilong";
 *      String hexString = "055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6";
 * 
 *      SymmetricEncryption symmetricEncryption = new SymmetricEncryption(SymmetricType.Blowfish, keyString);
 *      log.info(symmetricEncryption.decryptHex(hexString,UTF8));
 * 
 *      输出:鑫哥爱feilong
 * }
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.0 2011-12-26 上午11:05:53
 * @version 1.0.1 2013-1-15 15:18 json log
 * @version 1.0.7 2014-6-5 16:26 丰富了javadoc
 * @see javax.crypto.Cipher
 * @see javax.crypto.Cipher#ENCRYPT_MODE
 * @see javax.crypto.Cipher#DECRYPT_MODE
 * @see javax.crypto.KeyGenerator
 * @see java.security.Key
 * @see com.feilong.lib.codec.binary.Base64
 * @see SymmetricType
 * @see #encryptBase64(String, String)
 * @see #decryptBase64(String, String)
 * @see #encryptHex(String, String)
 * @see #decryptHex(String, String)
 */
@lombok.extern.slf4j.Slf4j
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
public class SymmetricEncryption{

    /** The symmetric type. */
    //---------------------------------------------------------------
    private SymmetricType symmetricType;

    /**
     * The key string.
     */

    private String        keyString;

    /**
     * 支持设置特殊的 {@link java.security.Key} , 默认使用 {@link com.feilong.security.symmetric.builder.DefaultKeyBuilder},.
     *
     * @since 3.0.0
     */
    private KeyBuilder    keyBuilder;

    //---------------------------------------------------------------

    /** The cipher mode. */
    private CipherMode    cipherMode;

    /** The cipher padding. */
    private CipherPadding cipherPadding;

    //---------------------------------------------------------------

    /**
     * Instantiates a new symmetric encryption.
     */
    public SymmetricEncryption(){
    }

    /**
     * Instantiates a new symmetric encryption.
     *
     * @param symmetricType
     *            the symmetric type
     */
    public SymmetricEncryption(SymmetricType symmetricType){
        this.symmetricType = symmetricType;
    }

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
        this.symmetricType = symmetricType;
        this.keyString = keyString;
    }

    /**
     * Instantiates a new symmetric encryption.
     *
     * @param symmetricType
     *            the symmetric type
     * @param keyString
     *            the key string
     * @param keyBuilder
     *            支持设置特殊的 {@link java.security.Key} , 默认使用 {@link com.feilong.security.symmetric.builder.DefaultKeyBuilder},.
     *
     * @since 3.0.0
     */
    public SymmetricEncryption(SymmetricType symmetricType, String keyString, KeyBuilder keyBuilder){
        super();
        this.symmetricType = symmetricType;
        this.keyString = keyString;
        this.keyBuilder = keyBuilder;
    }

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
     */
    public SymmetricEncryption(SymmetricType symmetricType, String keyString, CipherMode cipherMode, CipherPadding cipherPadding){
        this.symmetricType = symmetricType;
        this.keyString = keyString;
        this.cipherMode = cipherMode;
        this.cipherPadding = cipherPadding;
    }

    /**
     * Instantiates a new symmetric encryption.
     *
     * @param symmetricType
     *            the symmetric type
     * @param keyString
     *            the key string
     * @param keyBuilder
     *            支持设置特殊的 {@link java.security.Key} , 默认使用 {@link com.feilong.security.symmetric.builder.DefaultKeyBuilder},.
     * @param cipherMode
     *            the cipher mode
     * @param cipherPadding
     *            the cipher padding
     * @since 3.0.0
     */
    public SymmetricEncryption(SymmetricType symmetricType, String keyString, KeyBuilder keyBuilder, CipherMode cipherMode,
                    CipherPadding cipherPadding){
        super();
        this.symmetricType = symmetricType;
        this.keyString = keyString;
        this.keyBuilder = keyBuilder;
        this.cipherMode = cipherMode;
        this.cipherPadding = cipherPadding;
    }

    //---------------------------------------------------------------
    /**
     * 将加密之后的字节码,使用 Base64封装返回.
     * 
     * <pre class="code">
     * keyString=feilong
     * encrypBase64("鑫哥爱feilong") {@code ---->}BVl2k0U5+qokOeI6ufFlVS8XnkwEwff2
     * </pre>
     *
     * @param original
     *            原字符串
     * @return 加密之后的字符串 <br>
     *         如果 <code>original</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>original</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see "sun.misc.BASE64Encoder"
     * @see com.feilong.lib.codec.binary.Base64
     * @since 3.0.0
     */
    public String encryptBase64(String original){
        return encryptBase64(original, UTF8);
    }

    /**
     * 将加密之后的字节码,使用 Base64封装返回.
     * 
     * <pre class="code">
     * keyString=feilong
     * encrypBase64("鑫哥爱feilong") {@code ---->}BVl2k0U5+qokOeI6ufFlVS8XnkwEwff2
     * </pre>
     *
     * @param original
     *            原字符串
     * @param charsetName
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     * @return 加密之后的字符串 <br>
     *         如果 <code>original</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>original</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see "sun.misc.BASE64Encoder"
     * @see com.feilong.lib.codec.binary.Base64
     */
    public String encryptBase64(String original,String charsetName){
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            byte[] encryptBytes = toEncryptBytes(original, charsetName);
            String value = Base64.encodeBase64String(encryptBytes);

            LogBuilder.logEncrypt("encrypBase64", original, value, symmetricType.getAlgorithm(), keyString);
            return value;
        }catch (Exception e){
            throw new EncryptionException(errorMessage("original", original, symmetricType.getAlgorithm(), keyString, charsetName), e);
        }
    }

    /**
     * des Base64解密.
     * 
     * <pre class="code">
     * keyString=feilong
     * decryptBase64("BVl2k0U5+qokOeI6ufFlVS8XnkwEwff2") {@code ---->}鑫哥爱feilong
     * 
     * </pre>
     *
     * @param base64String
     *            加密后的字符串
     * @return 解密返回的原始密码<br>
     *         如果 <code>base64String</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>base64String</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see "sun.misc.BASE64Decoder"
     * @see "sun.misc.BASE64Decoder#decodeBuffer(String)"
     * @see com.feilong.lib.codec.binary.Base64
     * @see com.feilong.lib.codec.binary.Base64#decodeBase64(byte[])
     * @since 3.0.0
     */
    public String decryptBase64(String base64String){
        return decryptBase64(base64String, UTF8);
    }

    /**
     * des Base64解密.
     * 
     * <pre class="code">
     * keyString=feilong
     * decryptBase64("BVl2k0U5+qokOeI6ufFlVS8XnkwEwff2") {@code ---->}鑫哥爱feilong
     * 
     * </pre>
     *
     * @param base64String
     *            加密后的字符串
     * @param charsetName
     *            编码集 {@link CharsetType}
     * @return 解密返回的原始密码<br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>base64String</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>base64String</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see "sun.misc.BASE64Decoder"
     * @see "sun.misc.BASE64Decoder#decodeBuffer(String)"
     * @see com.feilong.lib.codec.binary.Base64
     * @see com.feilong.lib.codec.binary.Base64#decodeBase64(byte[])
     */
    public String decryptBase64(String base64String,String charsetName){
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            byte[] byteMi = Base64.decodeBase64(base64String);
            String original = toDecryptString(byteMi, charsetName);

            LogBuilder.logDecrypt("base64String", base64String, original, symmetricType.getAlgorithm(), keyString);
            return original;
        }catch (Exception e){
            throw new EncryptionException(
                            errorMessage("base64String", base64String, symmetricType.getAlgorithm(), keyString, charsetName),
                            e);
        }
    }

    /**
     * 将加密之后的字节码,使用大写的 Hex形式封装返回.
     * 
     * 
     * <pre class="code">
     * 例如:key=feilong
     * encryptHex("鑫哥爱feilong"){@code ---->}055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
     * </pre>
     *
     * @param original
     *            明文,原始内容
     * @return 加密String明文输入,String密文输出<br>
     *         如果 <code>original</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>original</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see StringUtil#getBytes(String, String)
     * @see CipherUtil#opBytes(byte[], int, String, Key)
     * @see ByteUtil#bytesToHexStringUpperCase(byte[])
     * @since 3.0.0
     */
    public String encryptHex(String original){
        return encryptHex(original, UTF8);
    }

    /**
     * 将加密之后的字节码,使用大写的 Hex形式封装返回.
     * 
     * 
     * <pre class="code">
     * 例如:key=feilong
     * encryptHex("鑫哥爱feilong"){@code ---->}055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
     * </pre>
     *
     * @param original
     *            明文,原始内容
     * @param charsetName
     *            编码集 {@link CharsetType}
     * @return 加密String明文输入,String密文输出<br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>original</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>original</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see StringUtil#getBytes(String, String)
     * @see CipherUtil#opBytes(byte[], int, String, Key)
     * @see ByteUtil#bytesToHexStringUpperCase(byte[])
     * 
     * @since 1.11.0 change original type to String
     */
    public String encryptHex(String original,String charsetName){
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            byte[] encryptBytes = toEncryptBytes(original, charsetName);
            String value = ByteUtil.bytesToHexStringUpperCase(encryptBytes);

            LogBuilder.logEncrypt("hexStringUpperCase", original, value, symmetricType.getAlgorithm(), keyString);
            return value;
        }catch (Exception e){
            throw new EncryptionException(errorMessage("original", original, symmetricType.getAlgorithm(), keyString, charsetName), e);
        }
    }

    /**
     * 16进制 des 解密,解密 以String密文输入,String明文输出.
     * 
     * <pre class="code">
     * 例如:key=feilong
     * decryptHex("055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6"){@code ---->}"鑫哥爱feilong"
     * </pre>
     *
     * @param hexString
     *            一串经过加密的16进制形式字符串,例如 055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
     * @return 解密 String明文输出<br>
     *         如果 <code>hexString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>hexString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see CipherUtil#opBytes(byte[], int, String, Key)
     * @since 3.0.0
     */
    public String decryptHex(String hexString){
        return decryptHex(hexString, UTF8);
    }

    /**
     * 16进制 des 解密,解密 以String密文输入,String明文输出.
     * 
     * <pre class="code">
     * 例如:key=feilong
     * decryptHex("055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6"){@code ---->}"鑫哥爱feilong"
     * </pre>
     *
     * @param hexString
     *            一串经过加密的16进制形式字符串,例如 055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
     * @param charsetName
     *            编码集 {@link CharsetType}
     * @return 解密 String明文输出<br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>hexString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>hexString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see CipherUtil#opBytes(byte[], int, String, Key)
     */
    public String decryptHex(String hexString,String charsetName){
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            byte[] bs = ByteUtil.hexBytesToBytes(StringUtil.getBytes(hexString, charsetName));
            String original = toDecryptString(bs, charsetName);

            LogBuilder.logDecrypt("hexString", hexString, original, symmetricType.getAlgorithm(), keyString);
            return original;
        }catch (Exception e){
            throw new EncryptionException(errorMessage("hexString", hexString, symmetricType.getAlgorithm(), keyString, charsetName), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * To encrypt bytes.
     *
     * @param original
     *            the original
     * @param charsetName
     *            the charset name
     * @return the byte[]
     */
    private byte[] toEncryptBytes(String original,String charsetName){
        byte[] bs = StringUtil.getBytes(original, charsetName);
        return CipherUtil.encrypt(bs, buildTransformation(), buildKey());
    }

    /**
     * To decrypt string.
     *
     * @param bs
     *            the bs
     * @param charsetName
     *            the charset name
     * @return the string
     */
    private String toDecryptString(byte[] bs,String charsetName){
        byte[] decryptBytes = CipherUtil.decrypt(bs, buildTransformation(), buildKey());
        return StringUtil.newString(decryptBytes, charsetName);
    }

    /**
     * Builds the transformation.
     *
     * @return the string
     */
    private String buildTransformation(){
        String transformation = TransformationBuilder.build(symmetricType.getAlgorithm(), cipherMode, cipherPadding);
        log.debug("algorithm:[{}],keyString:[{}],transformation:[{}]", symmetricType.getAlgorithm(), keyString, transformation);
        return transformation;
    }

    /**
     * Builds the key.
     *
     * @return the key
     */
    private Key buildKey(){
        KeyBuilder useKeyBuilder = defaultIfNull(keyBuilder, DefaultKeyBuilder.INSTANCE);
        return useKeyBuilder.build(symmetricType.getAlgorithm(), keyString);
    }

    /**
     * 设置 key string.
     *
     * @param keyString
     *            the keyString to set
     */
    public void setKeyString(String keyString){
        this.keyString = keyString;
    }

    /**
     * 支持设置特殊的 {@link java.security.Key} , 默认使用 {@link com.feilong.security.symmetric.builder.DefaultKeyBuilder},.
     *
     * @param keyBuilder
     *            the keyBuilder to set
     * @since 3.0.0
     */
    public void setKeyBuilder(KeyBuilder keyBuilder){
        this.keyBuilder = keyBuilder;
    }

    /**
     * 设置 cipher mode.
     *
     * @param cipherMode
     *            the cipherMode to set
     */
    public void setCipherMode(CipherMode cipherMode){
        this.cipherMode = cipherMode;
    }

    /**
     * 设置 cipher padding.
     *
     * @param cipherPadding
     *            the cipherPadding to set
     */
    public void setCipherPadding(CipherPadding cipherPadding){
        this.cipherPadding = cipherPadding;
    }

    /**
     * Sets the symmetric type.
     *
     * @param symmetricType
     *            the symmetricType to set
     */
    public void setSymmetricType(SymmetricType symmetricType){
        this.symmetricType = symmetricType;
    }

}