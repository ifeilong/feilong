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

import java.io.Serializable;

import com.feilong.security.symmetric.builder.KeyBuilder;

/**
 * 参数配置.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class SymmetricEncryptionConfig implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1588283689479766708L;

    //---------------------------------------------------------------

    /** The symmetric type. */
    private SymmetricType     symmetricType;

    //---------------------------------------------------------------

    /**
     * The key string.
     */
    private String            keyString;

    /**
     * 支持设置特殊的 {@link java.security.Key} , 默认使用 {@link com.feilong.security.symmetric.builder.DefaultKeyBuilder},.
     *
     * @since 3.0.0
     */
    private KeyBuilder        keyBuilder;

    //---------------------------------------------------------------

    /** The cipher mode. */
    private CipherMode        cipherMode;

    /** The cipher padding. */
    private CipherPadding     cipherPadding;

    //---------------------------------------------------------------

    /**
     * Gets the symmetric type.
     *
     * @return the symmetricType
     */
    public SymmetricType getSymmetricType(){
        return symmetricType;
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

    /**
     * Gets the key string.
     * 
     * @return the keyString
     * 
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

    /**
     * Gets the cipher mode.
     *
     * @return the cipherMode
     */
    public CipherMode getCipherMode(){
        return cipherMode;
    }

    /**
     * Sets the cipher mode.
     *
     * @param cipherMode
     *            the cipherMode to set
     */
    public void setCipherMode(CipherMode cipherMode){
        this.cipherMode = cipherMode;
    }

    /**
     * Gets the cipher padding.
     *
     * @return the cipherPadding
     */
    public CipherPadding getCipherPadding(){
        return cipherPadding;
    }

    /**
     * Sets the cipher padding.
     *
     * @param cipherPadding
     *            the cipherPadding to set
     */
    public void setCipherPadding(CipherPadding cipherPadding){
        this.cipherPadding = cipherPadding;
    }

    /**
     * 获得 支持设置特殊的 {@link java.security.Key} , 默认使用 {@link com.feilong.security.symmetric.builder.DefaultKeyBuilder},.
     *
     * @return the keyBuilder
     */
    public KeyBuilder getKeyBuilder(){
        return keyBuilder;
    }

    /**
     * 设置 支持设置特殊的 {@link java.security.Key} , 默认使用 {@link com.feilong.security.symmetric.builder.DefaultKeyBuilder},.
     *
     * @param keyBuilder
     *            the keyBuilder to set
     */
    public void setKeyBuilder(KeyBuilder keyBuilder){
        this.keyBuilder = keyBuilder;
    }

}
