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
package com.feilong.lib.json;

import java.util.Map;

import com.feilong.lib.json.processors.DefaultDefaultValueProcessor;
import com.feilong.lib.json.processors.DefaultValueProcessor;
import com.feilong.lib.json.processors.JsonVerifier;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONObjectValueSetter{

    static void set(JSONObject jsonObject,String key,Object value,Class<?> propertyType,JsonConfig jsonConfig,boolean bypass){
        if (value == null){
            DefaultValueProcessor defaultValueProcessor = findDefaultValueProcessor(propertyType, jsonConfig);
            value = defaultValueProcessor.getDefaultValue(propertyType);
            if (!JsonVerifier.isValidJsonValue(value)){
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }

        //---------------------------------------------------------------
        //不包含 就put
        if (!jsonObject.properties.containsKey(key)){
            if (bypass || String.class.isAssignableFrom(propertyType)){
                jsonObject.properties.put(key, value);
            }else{
                jsonObject.accumulate(key, value, jsonConfig);
            }
        }else{
            if (String.class.isAssignableFrom(propertyType)){
                Object o = jsonObject.get(key);
                if (o instanceof JSONArray){
                    ((JSONArray) o).addString((String) value);
                }else{
                    jsonObject.properties.put(key, new JSONArray().addValue(o).addString((String) value));
                }
            }else{
                jsonObject.accumulate(key, value, jsonConfig);
            }
        }
    }

    /**
     * Finds a DefaultValueProcessor registered to the target class.<br>
     * Returns null if none is registered.<br>
     * [Java -&gt; JSON]
     *
     * @param target
     *            a class used for searching a DefaultValueProcessor.
     * @param jsonConfig
     * @return the default value processor
     */
    public static DefaultValueProcessor findDefaultValueProcessor(Class<?> target,JsonConfig jsonConfig){
        Map<Class<?>, DefaultValueProcessor> defaultValueMap = jsonConfig.getDefaultValueMap();
        if (!defaultValueMap.isEmpty()){
            DefaultValueProcessor processor = defaultValueMap.get(target);
            if (processor != null){
                return processor;
            }
        }
        return DefaultDefaultValueProcessor.INSTANCE;
    }
}
