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
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class AesUtil extends SymmetricEncryption{

    /**
     * 构造函数.
     *
     * @param keyString
     *            自定义密钥字符串
     * @see SymmetricType
     */
    public AesUtil(String keyString){
        this(keyString, null, null);
    }

    //---------------------------------------------------------------

    /**
     * 构造函数.
     *
     * @param keyString
     *            自定义密钥字符串
     * @param cipherMode
     *            the cipher mode
     * @param cipherPadding
     *            the cipher padding
     * @see "javax.crypto.Cipher#tokenizeTransformation(String)"
     */
    public AesUtil(String keyString, CipherMode cipherMode, CipherPadding cipherPadding){
        super(SymmetricType.AES, keyString, cipherMode, cipherPadding);
    }

    /**
     * 构造函数.
     *
     * @param config
     *            the config
     * @see SymmetricType
     * @see "javax.crypto.Cipher#tokenizeTransformation(String)"
     */
    public AesUtil(SymmetricEncryptionConfig config){
        super(config);
    }

}
