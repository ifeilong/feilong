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

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

import com.feilong.lib.json.util.JSONUtils;

/**
 * Base class for JSONObject and JSONArray.
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
abstract class AbstractJSON{

    /**
     * The Class CycleSet.
     */
    private static class CycleSet extends ThreadLocal{

        /**
         * Initial value.
         *
         * @return the object
         */
        @Override
        protected Object initialValue(){
            return new SoftReference(new HashSet());
        }

        /**
         * Gets the 设置.
         *
         * @return the 设置
         */
        public Set getSet(){
            Set set = (Set) ((SoftReference) get()).get();
            if (set == null){
                set = new HashSet();
                set(new SoftReference(set));
            }
            return set;
        }
    }

    /** The cycle set. */
    private static CycleSet cycleSet = new CycleSet();

    //---------------------------------------------------------------

    /**
     * Adds a reference for cycle detection check.
     *
     * @param instance
     *            the reference to add
     * @return true if the instance has not been added previously, false
     *         otherwise.
     */
    protected static boolean addInstance(Object instance){
        return getCycleSet().add(instance);
    }

    //---------------------------------------------------------------

    /**
     * Removes a reference for cycle detection check.
     *
     * @param instance
     *            the instance
     */
    protected static void removeInstance(Object instance){
        Set set = getCycleSet();
        set.remove(instance);
        if (set.size() == 0){
            cycleSet.remove();
        }
    }

    /**
     * Process value.
     *
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    protected Object _processValue(Object value,JsonConfig jsonConfig){
        if (JSONNull.getInstance().equals(value)){
            return JSONNull.getInstance();
        }
        if (Class.class.isAssignableFrom(value.getClass()) || value instanceof Class){
            return ((Class) value).getName();
        }

        if (JSONUtils.isFunction(value)){
            if (value instanceof String){
                value = JSONFunction.parse((String) value);
            }
            return value;
        }
        if (value instanceof JSONString){
            return JSONSerializer.toJSON(value, jsonConfig);
        }

        if (value instanceof JSON){
            return JSONSerializer.toJSON(value, jsonConfig);
        }
        if (JSONUtils.isArray(value)){
            return JSONArray.fromObject(value, jsonConfig);
        }

        //---------------------------------------------------------------
        if (JSONUtils.isString(value)){
            String str = String.valueOf(value);
            if (JSONUtils.hasQuotes(str)){
                String stripped = JSONUtils.stripQuotes(str);
                if (JSONUtils.isFunction(stripped)){
                    return JSONUtils.DOUBLE_QUOTE + stripped + JSONUtils.DOUBLE_QUOTE;
                }
                if (stripped.startsWith("[") && stripped.endsWith("]")){
                    return stripped;
                }
                if (stripped.startsWith("{") && stripped.endsWith("}")){
                    return stripped;
                }
                return str;
            }
            if (JSONUtils.isJsonKeyword(str, jsonConfig)){
                if (jsonConfig.isJavascriptCompliant() && "undefined".equals(str)){
                    return JSONNull.getInstance();
                }
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

        JSONObject jsonObject = JSONObject.fromObject(value, jsonConfig);
        if (jsonObject.isNullObject()){
            return JSONNull.getInstance();
        }
        return jsonObject;
    }

    //---------------------------------------------------------------

    /**
     * Gets the cycle set.
     *
     * @return the cycle set
     */
    private static Set getCycleSet(){
        return cycleSet.getSet();
    }
}