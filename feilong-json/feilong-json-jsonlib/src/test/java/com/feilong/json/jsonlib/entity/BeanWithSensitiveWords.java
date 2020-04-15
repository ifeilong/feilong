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
package com.feilong.json.jsonlib.entity;

import com.feilong.json.SensitiveWords;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
public class BeanWithSensitiveWords{

    private String pattern;

    private String key;

    @SensitiveWords
    private String cvv;

    private String cvv2;

    //---------------------------------------------------------------
    /**
     * 
     */
    public BeanWithSensitiveWords(){
        super();
    }

    /**
     * @param pattern
     * @param key
     */
    public BeanWithSensitiveWords(String pattern, String key){
        super();
        this.pattern = pattern;
        this.key = key;
    }

    /**
     * @param pattern
     * @param key
     * @param cvv
     */
    public BeanWithSensitiveWords(String pattern, String key, String cvv){
        super();
        this.pattern = pattern;
        this.key = key;
        this.cvv = cvv;
    }

    //---------------------------------------------------------------

    /**
     * @return the pattern
     */
    public String getPattern(){
        return pattern;
    }

    /**
     * @param pattern
     *            the pattern to set
     */
    public void setPattern(String pattern){
        this.pattern = pattern;
    }

    /**
     * @return the key
     */
    public String getKey(){
        return key;
    }

    /**
     * @param key
     *            the key to set
     */
    public void setKey(String key){
        this.key = key;
    }

    /**
     * @return the cvv
     */

    public String getCvv(){
        return cvv;
    }

    /**
     * @param cvv
     *            the cvv to set
     */
    public void setCvv(String cvv){
        this.cvv = cvv;
    }

    /**
     * @return the cvv2
     */
    @SensitiveWords
    public String getCvv2(){
        return cvv2;
    }

    /**
     * @param cvv2
     *            the cvv2 to set
     */
    public void setCvv2(String cvv2){
        this.cvv2 = cvv2;
    }

}
