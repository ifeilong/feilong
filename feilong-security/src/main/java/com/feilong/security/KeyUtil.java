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
package com.feilong.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import com.feilong.core.DefaultRuntimeException;

/**
 * The Class KeyUtil.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.0
 */
public class KeyUtil{

    /**
     * 基于私钥字符串, 生成私钥.
     *
     * @param privateKeyString
     *            the private key string
     * @return the key
     */
    public static Key generatePrivateKey(String privateKeyString){
        String algorithm = "EC";
        return generatePrivateKey(privateKeyString, algorithm);
    }

    /**
     * 基于私钥字符串, 生成私钥.
     *
     * @param privateKeyString
     *            the private key string
     * @param algorithm
     *            the algorithm
     * @return the key
     * @throws DefaultRuntimeException
     *             the default runtime exception
     */
    public static Key generatePrivateKey(String privateKeyString,String algorithm){
        try{
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            // 使用PKCS8EncodedKeySpec加载ECDSA私钥
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            PrivateKey key = keyFactory.generatePrivate(keySpec);
            return key;
        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new DefaultRuntimeException(e);
        }
    }
}
