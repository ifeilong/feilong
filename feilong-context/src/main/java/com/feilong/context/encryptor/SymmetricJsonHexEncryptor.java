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
package com.feilong.context.encryptor;

import static com.feilong.core.CharsetType.UTF8;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.json.JsonUtil;
import com.feilong.security.symmetric.SymmetricEncryption;
import com.feilong.security.symmetric.SymmetricType;

/**
 * 默认的加密解密器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.12.0
 */
public class SymmetricJsonHexEncryptor<T extends Encryptorable> implements Encryptor<T>{

    /** The Constant log. */
    private static final Logger LOGGER        = LoggerFactory.getLogger(SymmetricJsonHexEncryptor.class);

    //---------------------------------------------------------------
    /** 对称加密的类型. */
    private SymmetricType       symmetricType = SymmetricType.DESede;

    /** key. */
    private String              key           = SymmetricJsonHexEncryptor.class.getName();

    //---------------------------------------------------------------

    /** 编码格式,默认 {@link CharsetType#UTF8}. */
    private String              charsetName   = UTF8;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.psi.controller.redirectresult.PayResultParamDataSafe#encryptPayResult(java.io.Serializable)
     */
    @Override
    public String encrypt(T bean){
        String json = JsonUtil.format(bean, 0, 0);

        SymmetricEncryption symmetricEncryption = build();
        return symmetricEncryption.encryptHex(json, charsetName);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.psi.controller.redirectresult.PayResultParamDataSafe#decryptPayResult(java.lang.String)
     */
    @Override
    public T decrypt(String encryptPayResultType,Class<T> klass){
        SymmetricEncryption symmetricEncryption = build();
        String json = symmetricEncryption.decryptHex(encryptPayResultType, charsetName);

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("encryptPayResultType:[{}],json:[{}]", encryptPayResultType, json);
        }
        return JsonUtil.toBean(json, klass);
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @return the symmetric encryption
     */
    private SymmetricEncryption build(){
        return new SymmetricEncryption(symmetricType, key);
    }

    //---------------------------------------------------------------

    /**
     * 设置 对称加密的类型.
     *
     * @param symmetricType
     *            the symmetricType to set
     */
    public void setSymmetricType(SymmetricType symmetricType){
        this.symmetricType = symmetricType;
    }

    /**
     * 设置 key.
     *
     * @param key
     *            the key to set
     */
    public void setKey(String key){
        this.key = key;
    }

    /**
     * 设置 编码格式,默认 {@link CharsetType#UTF8}.
     *
     * @param charsetName
     *            the charsetName to set
     */
    public void setCharsetName(String charsetName){
        this.charsetName = charsetName;
    }
}
