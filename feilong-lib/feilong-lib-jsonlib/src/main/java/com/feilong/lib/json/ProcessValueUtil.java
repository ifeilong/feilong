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

import static com.feilong.lib.json.ToStringUtil.ARRAY_END;
import static com.feilong.lib.json.ToStringUtil.ARRAY_START;
import static com.feilong.lib.json.ToStringUtil.OBJECT_END;
import static com.feilong.lib.json.ToStringUtil.OBJECT_START;

import java.lang.annotation.Annotation;

import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.JsonVerifier;
import com.feilong.lib.json.util.JSONUtils;

/**
 * 值的处理工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class ProcessValueUtil{

    /** Don't let anyone instantiate this class. */
    private ProcessValueUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Process value.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    static Object processArrayValue(Object value,JsonConfig jsonConfig){
        if (value != null){

            //Finds a JsonValueProcessor registered to the target type.<br>
            //Returns null if none is registered.<br>
            //[Java -&gt; JSON]
            JsonValueProcessor jsonValueProcessor = jsonConfig.getTypeMap().get(value.getClass());
            if (jsonValueProcessor != null){
                value = jsonValueProcessor.processArrayValue(value, jsonConfig);
                if (!JsonVerifier.isValidJsonValue(value)){
                    throw new JSONException("Value is not a valid JSON value. " + value);
                }
            }
        }

        if (value instanceof JSONTokener){
            return JSONTokenerParser.toJSONArray((JSONTokener) value, jsonConfig);
        }
        if (value != null && Enum.class.isAssignableFrom(value.getClass())){
            return ((Enum<?>) value).name();
        }
        if (value instanceof Annotation || (value != null && value.getClass().isAnnotation())){
            throw new JSONException("Unsupported type");
        }
        return process(value, jsonConfig);
    }
    //---------------------------------------------------------------

    /**
     * Process value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    static Object processJsonObjectValue(String key,Object value,JsonConfig jsonConfig){
        if (value != null){
            JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(value.getClass(), key);
            if (jsonValueProcessor != null){
                value = jsonValueProcessor.processObjectValue(null, value, jsonConfig);
                if (!JsonVerifier.isValidJsonValue(value)){
                    throw new JSONException("Value is not a valid JSON value. " + value);
                }
            }
        }
        if (value instanceof JSONTokener){
            return JSONTokenerParser.toJSONObject((JSONTokener) value, jsonConfig);
        }
        if (value != null && Enum.class.isAssignableFrom(value.getClass())){
            return ((Enum<?>) value).name();
        }
        return process(value, jsonConfig);
    }

    //---------------------------------------------------------------

    /**
     * Process value.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    private static Object process(Object value,JsonConfig jsonConfig){
        if (JSONNull.getInstance().equals(value)){
            return JSONNull.getInstance();
        }
        if (Class.class.isAssignableFrom(value.getClass()) || value instanceof Class){
            return ((Class<?>) value).getName();
        }

        //---------------------------------------------------------------
        if (value instanceof JSON){
            return JSONSerializer.toJSON(value, jsonConfig);
        }
        if (JSONUtils.isArray(value)){
            return JSONArrayBuilder.build(value, jsonConfig);
        }

        //---------------------------------------------------------------
        if (JSONUtils.isString(value)){
            String str = String.valueOf(value);
            if (JSONUtils.hasQuotes(str)){
                String stripped = JSONUtils.stripQuotes(str);
                if (stripped.startsWith(OBJECT_START) && stripped.endsWith(OBJECT_END)){
                    return stripped;
                }
                if (stripped.startsWith(ARRAY_START) && stripped.endsWith(ARRAY_END)){
                    return stripped;
                }
                return str;
            }

            //---------------------------------------------------------------
            if (JSONUtils.isJsonKeyword(str)){
                return str;
            }
            if (JSONUtils.mayBeJSON(str)){
                try{
                    return JSONSerializer.toJSON(str, jsonConfig);
                }catch (JSONException jsone){
                    return str;
                }
            }
            return str;
        }

        //---------------------------------------------------------------
        if (JSONUtils.isNumber(value)){
            JSONUtils.testValidity(value);
            return JSONUtils.transformNumber((Number) value);
        }
        if (JSONUtils.isBoolean(value)){
            return value;
        }
        return JSONObjectBuilder.build(value, jsonConfig);
    }
}
