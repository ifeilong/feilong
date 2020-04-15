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

import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.core.lang.StringUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.security.ByteUtil;
import com.feilong.security.EncryptionException;
import com.feilong.security.symmetric.builder.KeyBuilder;
import com.feilong.security.symmetric.builder.KeyBuilderConfig;
import com.feilong.security.symmetric.builder.TransformationBuilder;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 双向加密父类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public abstract class AbstractSymmetricEncryption{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSymmetricEncryption.class);

    //---------------------------------------------------------------

    /** key值. */
    private String              keyString;

    /** 算法. */
    private String              algorithm;

    //---------------------------------------------------------------

    /** 对称加密key. */
    private Key                 key;

    //---------------------------------------------------------------

    /**
     * 转换的名称,例如 DES/CBC/PKCS5Padding.
     * <p>
     * 有关标准转换名称的信息,请参见 Java Cryptography Architecture Reference Guide 的附录 A.
     * </p>
     * 
     * @see <a href="http://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html">StandardNames</a>
     */
    private String              transformation;

    //---------------------------------------------------------------

    /**
     * Inits the.
     *
     * @param inputSymmetricEncryptionConfig
     *            the symmetric encryption config
     * @since 1.11.0
     */
    protected void init(SymmetricEncryptionConfig inputSymmetricEncryptionConfig){
        Validate.notNull(inputSymmetricEncryptionConfig, "symmetricEncryptionConfig can't be null!");

        SymmetricType symmetricType = inputSymmetricEncryptionConfig.getSymmetricType();
        Validate.notNull(symmetricType, "symmetricEncryptionConfig.getSymmetricType() can't be null!");

        //---------------------------------------------------------------
        this.keyString = inputSymmetricEncryptionConfig.getKeyString();
        Validate.notBlank(keyString, "keyString can't be blank!");

        //---------------------------------------------------------------
        this.algorithm = symmetricType.getAlgorithm();

        //---------------------------------------------------------------
        this.transformation = TransformationBuilder.build(
                        algorithm,
                        inputSymmetricEncryptionConfig.getCipherMode(),
                        inputSymmetricEncryptionConfig.getCipherPadding());

        LOGGER.debug("algorithm:[{}],keyString:[{}],transformation:[{}]", algorithm, keyString, transformation);

        //---------------------------------------------------------------

        //由于是固定的类型枚举,枚举里面的加密类型都经过测试过的,所以理论上来说不会再出现   NoSuchAlgorithmException
        try{
            KeyBuilderConfig keyBuilderConfig = new KeyBuilderConfig(algorithm, keyString);
            keyBuilderConfig.setSecureRandomAlgorithm(inputSymmetricEncryptionConfig.getSecureRandomAlgorithm());
            this.key = KeyBuilder.build(keyBuilderConfig);
        }catch (NoSuchAlgorithmException e){
            throw new EncryptionException(
                            Slf4jUtil.format("inputSymmetricEncryptionConfig:[{}]", JsonUtil.format(inputSymmetricEncryptionConfig)),
                            e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 将加密之后的字节码,使用 Base64封装返回.
     * 
     * <pre class="code">
     * keyString=feilong
     * encrypBase64("鑫哥爱feilong") ---->BVl2k0U5+qokOeI6ufFlVS8XnkwEwff2
     * </pre>
     *
     * @param original
     *            原字符串
     * @param charsetName
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     * @return 加密之后的字符串 <br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see sun.misc.BASE64Encoder
     * @see org.apache.commons.codec.binary.Base64
     */
    @SuppressWarnings("restriction")
    public String encryptBase64(String original,String charsetName){
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            String encode = doEncryptBase64(original, charsetName);

            logEncrypt("encrypBase64", original, encode);
            return encode;
        }catch (Exception e){
            logException("original", original, e);
            throw new EncryptionException(Slf4jUtil.format("original:[{}],charsetName:[{}]", original, charsetName), e);
        }
    }

    /**
     * des Base64解密.
     * 
     * <pre class="code">
     * keyString=feilong
     * decryptBase64("BVl2k0U5+qokOeI6ufFlVS8XnkwEwff2") ---->鑫哥爱feilong
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
     * @see sun.misc.BASE64Decoder
     * @see sun.misc.BASE64Decoder#decodeBuffer(String)
     * @see org.apache.commons.codec.binary.Base64
     * @see org.apache.commons.codec.binary.Base64#decodeBase64(byte[])
     */
    @SuppressWarnings("restriction")
    public String decryptBase64(String base64String,String charsetName){
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            String original = doDecryptBase64(base64String, charsetName);

            logDecrypt("base64String", base64String, original);
            return original;
        }catch (Exception e){
            logException("base64String", base64String, e);
            throw new EncryptionException(Slf4jUtil.format("base64String:[{}],charsetName:[{}]", base64String, charsetName), e);
        }
    }

    /**
     * 将加密之后的字节码,使用大写的 Hex形式封装返回.
     * 
     * 
     * <pre class="code">
     * 例如:key=feilong
     * encryptHex("鑫哥爱feilong")---->055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
     * </pre>
     *
     * @param original
     *            明文,原始内容
     * @param charsetName
     *            编码集 {@link CharsetType}
     * @return 加密String明文输入,String密文输出<br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
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
            String hexStringUpperCase = doEncryptHex(original, charsetName);

            logEncrypt("hexStringUpperCase", original, hexStringUpperCase);
            return hexStringUpperCase;
        }catch (Exception e){
            logException("original", original, e);
            throw new EncryptionException(Slf4jUtil.format("original:[{}],charsetName:[{}]", original, charsetName), e);
        }
    }

    /**
     * 16进制 des 解密,解密 以String密文输入,String明文输出.
     * 
     * <pre class="code">
     * 例如:key=feilong
     * decryptHex("055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6")---->"鑫哥爱feilong"
     * </pre>
     *
     * @param hexString
     *            一串经过加密的16进制形式字符串,例如 055976934539FAAA2439E23AB9F165552F179E4C04C1F7F6
     * @param charsetName
     *            编码集 {@link CharsetType}
     * @return 解密 String明文输出<br>
     *         如果 <code>charsetName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>charsetName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see CipherUtil#opBytes(byte[], int, String, Key)
     */
    public String decryptHex(String hexString,String charsetName){
        Validate.notBlank(charsetName, "charsetName can't be blank!");

        //---------------------------------------------------------------
        try{
            String original = doDecryptHex(hexString, charsetName);

            logDecrypt("hexString", hexString, original);
            return original;
        }catch (Exception e){
            logException("hexString", hexString, e);
            throw new EncryptionException(Slf4jUtil.format("hexString:[{}],charsetName:[{}]", hexString, charsetName), e);
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
    protected byte[] toEncryptBytes(String original,String charsetName){
        byte[] bs = StringUtil.getBytes(original, charsetName);
        return CipherUtil.encrypt(bs, transformation, key);
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
    protected String toDecryptString(byte[] bs,String charsetName){
        byte[] decryptBytes = CipherUtil.decrypt(bs, transformation, key);
        return StringUtil.newString(decryptBytes, charsetName);
    }

    //---------------------------------------------------------------

    /**
     * Do encrypt base 64.
     *
     * @param original
     *            the original
     * @param charsetName
     *            the charset type
     * @return the string
     */
    protected abstract String doEncryptBase64(String original,String charsetName);

    /**
     * Do decrypt base 64.
     *
     * @param base64String
     *            the base 64 string
     * @param charsetName
     *            the charset name
     * @return the string
     */
    protected abstract String doDecryptBase64(String base64String,String charsetName);

    /**
     * Do encrypt hex.
     *
     * @param original
     *            the original
     * @param charsetName
     *            the charset name
     * @return the string
     */
    protected abstract String doEncryptHex(String original,String charsetName);

    /**
     * Do decrypt hex.
     *
     * @param hexString
     *            the hex string
     * @param charsetName
     *            the charset name
     * @return the string
     */
    protected abstract String doDecryptHex(String hexString,String charsetName);

    //---------------------------------------------------------------

    /**
     * Log decrypt.
     *
     * @param typeName
     *            the type
     * @param needDecryptValue
     *            the need decrypt value
     * @param original
     *            the original
     */
    protected void logDecrypt(String typeName,String needDecryptValue,String original){
        if (LOGGER.isDebugEnabled()){
            Map<String, Object> map = newLinkedHashMap();
            map.put("algorithm", algorithm);
            map.put("keyString", keyString);
            map.put(typeName, needDecryptValue);
            map.put("original", original);

            LOGGER.debug(JsonUtil.format(map));
        }
    }

    //---------------------------------------------------------------
    /**
     * Log exception.
     *
     * @param typeName
     *            the name
     * @param value
     *            the value
     * @param e
     *            the e
     */
    protected void logException(String typeName,String value,Exception e){
        if (LOGGER.isErrorEnabled()){

            Map<String, Object> map = newLinkedHashMap();
            map.put("algorithm", algorithm);
            map.put("keyString", keyString);
            map.put(typeName, value);

            LOGGER.error(JsonUtil.format(map), e);
        }
    }

    /**
     * Log encrypt.
     *
     * @param typeName
     *            the type
     * @param original
     *            the original
     * @param value
     *            the value
     */
    protected void logEncrypt(String typeName,String original,String value){
        if (LOGGER.isDebugEnabled()){
            Map<String, String> map = newLinkedHashMap();
            map.put("algorithm", algorithm);
            map.put("original", original);
            map.put("keyString", keyString);

            map.put(typeName, value);
            map.put("valueLength", "" + value.length());

            LOGGER.debug(JsonUtil.format(map));
        }
    }

}
