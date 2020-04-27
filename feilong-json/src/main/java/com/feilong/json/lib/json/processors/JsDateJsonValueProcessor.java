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

package com.feilong.json.lib.json.processors;

import com.feilong.json.lib.json.JsonConfig;

import net.sf.json.processors.JsonValueProcessor;

/**
 * Transforms a java.util.Date property into a JSONObject ideal for JsDate
 * conversion
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class JsDateJsonValueProcessor implements JsonValueProcessor{

    /** The processor. */
    private final JsonBeanProcessor processor;

    /**
     * Instantiates a new js date json value processor.
     */
    public JsDateJsonValueProcessor(){
        processor = new JsDateJsonBeanProcessor();
    }

    /**
     * Process array value.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    @Override
    public Object processArrayValue(Object value,JsonConfig jsonConfig){
        return process(value, jsonConfig);
    }

    /**
     * Process object value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    @Override
    public Object processObjectValue(String key,Object value,JsonConfig jsonConfig){
        return process(value, jsonConfig);
    }

    /**
     * Process.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    private Object process(Object value,JsonConfig jsonConfig){
        return processor.processBean(value, jsonConfig);
    }
}