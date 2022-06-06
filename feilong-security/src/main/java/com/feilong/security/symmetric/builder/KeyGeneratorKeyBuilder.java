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
package com.feilong.security.symmetric.builder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.feilong.core.DefaultRuntimeException;

/**
 * 基于 {@link KeyGenerator} 的KeyBuilder .
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.1.0
 */
public class KeyGeneratorKeyBuilder extends AbstractSecretKeySpecBuilder{

    /** Static instance. */
    // the static instance works for all types
    public static final KeyGeneratorKeyBuilder INSTANCE              = new KeyGeneratorKeyBuilder();

    //---------------------------------------------------------------
    /** 用于构造 {@link KeyGenerator}. */
    private int                                keyGeneratorKeysize   = 128;

    /** 用于构造 {@link SecureRandom}. */
    private String                             secureRandomAlgorithm = "SHA1PRNG";

    //---------------------------------------------------------------

    /**
     * Instantiates a new key generator key builder.
     */
    public KeyGeneratorKeyBuilder(){
        super();
    }

    /**
     * Instantiates a new key generator key builder.
     *
     * @param secureRandomAlgorithm
     *            the secure random algorithm
     */
    public KeyGeneratorKeyBuilder(String secureRandomAlgorithm){
        super();
        this.secureRandomAlgorithm = secureRandomAlgorithm;
    }

    /**
     * Instantiates a new key generator key builder.
     *
     * @param keyGeneratorKeysize
     *            the key generator keysize
     * @param secureRandomAlgorithm
     *            the secure random algorithm
     */
    public KeyGeneratorKeyBuilder(int keyGeneratorKeysize, String secureRandomAlgorithm){
        super();
        this.keyGeneratorKeysize = keyGeneratorKeysize;
        this.secureRandomAlgorithm = secureRandomAlgorithm;
    }

    //---------------------------------------------------------------

    /**
     * Builds the key bytes.
     *
     * @param algorithm
     *            the algorithm
     * @param keyString
     *            the key string
     * @return the byte[]
     */
    @Override
    protected byte[] buildKeyBytes(String algorithm,String keyString){
        try{
            SecureRandom secureRandom = buildSecureRandom(keyString);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(keyGeneratorKeysize, secureRandom);

            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        }catch (NoSuchAlgorithmException e){
            throw new DefaultRuntimeException(e);
        }
    }

    /**
     * Builds the secure random.
     *
     * @param keyString
     *            the key string
     * @return the secure random
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     */
    private SecureRandom buildSecureRandom(String keyString) throws NoSuchAlgorithmException{
        SecureRandom secureRandom = SecureRandom.getInstance(secureRandomAlgorithm);
        secureRandom.setSeed(keyString.getBytes());
        return secureRandom;
    }

    //---------------------------------------------------------------
    /**
     * 设置 用于构造 {@link SecureRandom}.
     *
     * @param secureRandomAlgorithm
     *            the secureRandomAlgorithm to set
     */
    public void setSecureRandomAlgorithm(String secureRandomAlgorithm){
        this.secureRandomAlgorithm = secureRandomAlgorithm;
    }

    /**
     * 设置 用于构造 {@link KeyGenerator}.
     *
     * @param keyGeneratorKeysize
     *            the keyGeneratorKeysize to set
     */
    public void setKeyGeneratorKeysize(int keyGeneratorKeysize){
        this.keyGeneratorKeysize = keyGeneratorKeysize;
    }

}
