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
package com.feilong.context.converter;

import com.feilong.json.JsonToJavaConfig;
import com.feilong.json.JsonUtil;

/**
 * json的转换.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.3
 */
public class JsonStringToBeanConverter<T> extends AbstractStringToBeanConverter<T>{

    /** The json to java config. */
    private JsonToJavaConfig jsonToJavaConfig;

    //---------------------------------------------------------------

    /**
     * Instantiates a new json string to bean converter.
     */
    public JsonStringToBeanConverter(){
        super();
    }

    /**
     * Instantiates a new json string to bean converter.
     *
     * @param rootClass
     *            the root class
     */
    public JsonStringToBeanConverter(Class<T> rootClass){
        super();
        this.jsonToJavaConfig = new JsonToJavaConfig(rootClass);
    }

    /**
     * Instantiates a new json string to bean converter.
     *
     * @param jsonToJavaConfig
     *            the json to java config
     */
    public JsonStringToBeanConverter(JsonToJavaConfig jsonToJavaConfig){
        super();
        this.jsonToJavaConfig = jsonToJavaConfig;
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.bind.AbstractBeanClassStringToBeanConverter#handler(java.lang.Class, java.lang.String)
     */
    @Override
    protected T handler(String inputString){
        return JsonUtil.toBean(inputString, jsonToJavaConfig);
    }

    //---------------------------------------------------------------

    @Override
    protected String formatValue(String value){
        return JsonUtil.toString(value);
    }

    //---------------------------------------------------------------

    /**
     * Sets the json to java config.
     *
     * @param jsonToJavaConfig
     *            the jsonToJavaConfig to set
     */
    public void setJsonToJavaConfig(JsonToJavaConfig jsonToJavaConfig){
        this.jsonToJavaConfig = jsonToJavaConfig;
    }
}
