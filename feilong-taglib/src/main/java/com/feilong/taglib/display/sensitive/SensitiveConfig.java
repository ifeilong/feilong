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
package com.feilong.taglib.display.sensitive;

import org.apache.commons.lang3.Validate;

/**
 * 敏感词配置相关.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.1
 */
public class SensitiveConfig{

    /**
     * 敏感词类型.
     * 
     * @since 1.14.0
     */
    private String type;

    /** 掩码字符,比如*,#等,默认是*. */
    private char   maskChar = '*';

    //---------------------------------------------------------------
    /**
     * Instantiates a new sensitive config.
     *
     * @param type
     *            the type
     */
    public SensitiveConfig(String type){
        this.type = type;
    }

    /**
     * Instantiates a new sensitive config.
     *
     * @param type
     *            the type
     * @param maskChar
     *            掩码字符,比如*,#等,默认是*
     */
    public SensitiveConfig(String type, char maskChar){
        this.type = type;
        this.maskChar = maskChar;
    }

    /**
     * Instantiates a new sensitive config.
     *
     * @param sensitiveType
     *            the sensitive type
     */
    //---------------------------------------------------------------
    public SensitiveConfig(SensitiveType sensitiveType){
        Validate.notNull(sensitiveType, "sensitiveType can't be null!");
        this.type = sensitiveType.getType();
    }

    /**
     * Instantiates a new sensitive config.
     *
     * @param sensitiveType
     *            the sensitive type
     * @param maskChar
     *            掩码字符,比如*,#等,默认是*
     */
    public SensitiveConfig(SensitiveType sensitiveType, char maskChar){
        Validate.notNull(sensitiveType, "sensitiveType can't be null!");
        this.type = sensitiveType.getType();
        this.maskChar = maskChar;
    }

    //---------------------------------------------------------------

    /**
     * 获得 掩码字符,比如*,#等,默认是*.
     *
     * @return the 掩码字符,比如*,#等,默认是*
     */
    public char getMaskChar(){
        return maskChar;
    }

    /**
     * 设置 掩码字符,比如*,#等,默认是*.
     *
     * @param maskChar
     *            the new 掩码字符,比如*,#等,默认是*
     */
    public void setMaskChar(char maskChar){
        this.maskChar = maskChar;
    }

    /**
     * 获得 敏感词类型.
     *
     * @return the type
     * @since 1.14.0
     */
    public String getType(){
        return type;
    }

    /**
     * 设置 敏感词类型.
     *
     * @param type
     *            the type to set
     * @since 1.14.0
     */
    public void setType(String type){
        this.type = type;
    }
}
