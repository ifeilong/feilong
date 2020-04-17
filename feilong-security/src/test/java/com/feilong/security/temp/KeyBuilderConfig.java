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
package com.feilong.security.temp;

/**
 * 构造key的配置.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
@Deprecated
class KeyBuilderConfig{

    /** The algorithm. */
    private String algorithm;

    /** The key string. */
    private String keyString;

    //---------------------------------------------------------------
    /**
     * The secure random algorithm.
     * 
     * <p>
     * 默认 SHA1PRNG
     * </p>
     * 
     * <p>
     * 用于 {@link java.security.SecureRandom#getInstance(String)}
     * </p>
     */
    private String secureRandomAlgorithm = "SHA1PRNG";

    //---------------------------------------------------------------

    /** 密钥大小。这是特定于算法的一种规格，是以位数为单位指定的. */
    private int    keySize;

    //---------------------------------------------------------------

    /**
     * Instantiates a new key config.
     */
    public KeyBuilderConfig(){
        super();
    }

    /**
     * Instantiates a new key config.
     *
     * @param algorithm
     *            the algorithm
     * @param keyString
     *            the key string
     */
    public KeyBuilderConfig(String algorithm, String keyString){
        super();
        this.algorithm = algorithm;
        this.keyString = keyString;
    }

    //---------------------------------------------------------------
    /**
     * Gets the algorithm.
     *
     * @return the algorithm
     */
    public String getAlgorithm(){
        return algorithm;
    }

    /**
     * Sets the algorithm.
     *
     * @param algorithm
     *            the algorithm to set
     */
    public void setAlgorithm(String algorithm){
        this.algorithm = algorithm;
    }

    /**
     * Gets the key string.
     *
     * @return the keyString
     */
    public String getKeyString(){
        return keyString;
    }

    /**
     * Sets the key string.
     *
     * @param keyString
     *            the keyString to set
     */
    public void setKeyString(String keyString){
        this.keyString = keyString;
    }

    //---------------------------------------------------------------p

    /**
     * The secure random algorithm.
     * 
     * <p>
     * 默认 SHA1PRNG
     * </p>
     * 
     * <p>
     * 用于 {@link java.security.SecureRandom#getInstance(String)}
     * </p>
     *
     * @return the secureRandomAlgorithm
     */
    public String getSecureRandomAlgorithm(){
        return secureRandomAlgorithm;
    }

    /**
     * The secure random algorithm.
     * 
     * <p>
     * 默认 SHA1PRNG
     * </p>
     * 
     * <p>
     * 用于 {@link java.security.SecureRandom#getInstance(String)}
     * </p>
     *
     * @param secureRandomAlgorithm
     *            the secureRandomAlgorithm to set
     */
    public void setSecureRandomAlgorithm(String secureRandomAlgorithm){
        this.secureRandomAlgorithm = secureRandomAlgorithm;
    }

    /**
     * 获得 密钥大小。这是特定于算法的一种规格，是以位数为单位指定的.
     *
     * @return the keySize
     */
    public int getKeySize(){
        return keySize;
    }

    /**
     * 设置 密钥大小。这是特定于算法的一种规格，是以位数为单位指定的.
     *
     * @param keySize
     *            the keySize to set
     */
    public void setKeySize(int keySize){
        this.keySize = keySize;
    }
}
