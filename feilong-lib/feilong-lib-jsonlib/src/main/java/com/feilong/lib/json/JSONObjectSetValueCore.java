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

import com.feilong.lib.json.processors.JsonVerifier;

public class JSONObjectSetValueCore{

    /** Don't let anyone instantiate this class. */
    private JSONObjectSetValueCore(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static void setValue(JSONObject jsonObject,String key,Object value,Class<?> type,JsonConfig jsonConfig,boolean bypass){
        if (value == null){
            value = jsonConfig.findDefaultValueProcessor(type).getDefaultValue(type);
            if (!JsonVerifier.isValidJsonValue(value)){
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }

        //---------------------------------------------------------------
        boolean accumulated = false;
        if (jsonObject.properties.containsKey(key)){
            if (String.class.isAssignableFrom(type)){
                Object o = jsonObject.get(key);
                if (o instanceof JSONArray){
                    ((JSONArray) o).addString((String) value);
                }else{
                    jsonObject.properties.put(key, new JSONArray().element(o).addString((String) value));
                }
            }else{
                jsonObject.accumulate(key, value, jsonConfig);
            }
            accumulated = true;
        }else{
            if (bypass || String.class.isAssignableFrom(type)){
                jsonObject.properties.put(key, value);
            }else{
                jsonObject.setInternal(key, value, jsonConfig);
            }
        }

        //---------------------------------------------------------------

        value = jsonObject.get(key);
        if (accumulated){
            JSONArray array = (JSONArray) value;
            value = array.get(array.size() - 1);
        }
    }
}
