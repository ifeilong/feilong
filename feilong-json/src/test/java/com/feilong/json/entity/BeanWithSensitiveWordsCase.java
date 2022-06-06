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
package com.feilong.json.entity;

import com.feilong.json.SensitiveWords;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.5
 */
public class BeanWithSensitiveWordsCase{

    private String                          pattern;

    @SensitiveWords
    private String                          cvv;

    private BeanWithSensitiveWordsCaseInput beanWithSensitiveWordsCaseInput;

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
     * @return the beanWithSensitiveWordsCaseInput
     */
    public BeanWithSensitiveWordsCaseInput getBeanWithSensitiveWordsCaseInput(){
        return beanWithSensitiveWordsCaseInput;
    }

    /**
     * @param beanWithSensitiveWordsCaseInput
     *            the beanWithSensitiveWordsCaseInput to set
     */
    public void setBeanWithSensitiveWordsCaseInput(BeanWithSensitiveWordsCaseInput beanWithSensitiveWordsCaseInput){
        this.beanWithSensitiveWordsCaseInput = beanWithSensitiveWordsCaseInput;
    }

}
