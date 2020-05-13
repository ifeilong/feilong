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
package com.feilong.context.signer;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.lib.lang3.StringUtils.EMPTY;

import com.feilong.core.CharsetType;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.security.oneway.SHA1Util;

/**
 * 基于 sha1 的实现.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.1
 */
public class SHA1Signer implements Signer{

    /** 可以拼接的key. */
    private String key         = SHA1Signer.class.getName();

    /** 编码,默认是 {@link CharsetType#UTF8}. */
    private String charsetName = UTF8;

    //---------------------------------------------------------------
    /**
     * Instantiates a new SHA 1 signer.
     */
    public SHA1Signer(){
        super();
    }

    /**
     * Instantiates a new SHA 1 signer.
     *
     * @param key
     *            the key
     */
    public SHA1Signer(String key){
        super();
        this.key = key;
    }

    /**
     * Instantiates a new SHA 1 signer.
     *
     * @param key
     *            the key
     * @param charsetName
     *            the charset name
     */
    public SHA1Signer(String key, String charsetName){
        super();
        this.key = key;
        this.charsetName = charsetName;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.spring.web.handler.signer.Signer#sign(java.lang.CharSequence[])
     */
    @Override
    public String sign(CharSequence...strs){
        if (isNullOrEmpty(strs)){
            return EMPTY;
        }

        //---------------------------------------------------------------
        String tobeSign = ConvertUtil.toString(strs, "");
        return SHA1Util.encode(tobeSign + key, charsetName);
    }

    //---------------------------------------------------------------

    /**
     * 设置 可以拼接的key.
     *
     * @param key
     *            the new 可以拼接的key
     */
    public void setKey(String key){
        this.key = key;
    }

    /**
     * 设置 编码,默认是 {@link CharsetType#UTF8}.
     *
     * @param charsetName
     *            the new 编码,默认是 {@link CharsetType#UTF8}
     */
    public void setCharsetName(String charsetName){
        this.charsetName = charsetName;
    }

}