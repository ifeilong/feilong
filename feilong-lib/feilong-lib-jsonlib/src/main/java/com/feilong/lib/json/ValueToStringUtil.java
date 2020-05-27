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

import com.feilong.lib.json.util.JSONUtils;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class ValueToStringUtil{

    /**
     * Make a JSON text of an Object value. If the object has an
     * value.toJSONString() method, then that method will be used to produce the
     * JSON text. The method is required to produce a strictly conforming text.
     * If the object does not contain a toJSONString method (which is the most
     * common case), then a text will be produced by the rules.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param value
     *            The value to be serialized.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with <code>{</code>&nbsp;<small>(left brace)</small>
     *         and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws JSONException
     *             If the value is or contains an invalid number.
     */
    public static String valueToString(Object value){
        if (value == null || JSONUtils.isNull(value)){
            return "null";
        }
        if (value instanceof JSONFunction){
            return ((JSONFunction) value).toString();
        }
        if (value instanceof Number){
            return JSONUtils.numberToString((Number) value);
        }
        if (value instanceof Boolean || value instanceof JSONObject || value instanceof JSONArray){
            return value.toString();
        }
        return JSONUtils.quote(value.toString());
    }

    /**
     * Make a prettyprinted JSON text of an object value.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @param value
     *            The value to be serialized.
     * @param indentFactor
     *            The number of spaces to add to each level of
     *            indentation.
     * @param indent
     *            The indentation of the top level.
     * @return a printable, displayable, transmittable representation of the
     *         object, beginning with <code>{</code>&nbsp;<small>(left brace)</small>
     *         and ending with <code>}</code>&nbsp;<small>(right brace)</small>.
     * @throws JSONException
     *             If the object contains an invalid number.
     */
    public static String valueToString(Object value,int indentFactor,int indent){
        if (value == null || JSONUtils.isNull(value)){
            return "null";
        }
        if (value instanceof JSONFunction){
            return ((JSONFunction) value).toString();
        }
        if (value instanceof Number){
            return JSONUtils.numberToString((Number) value);
        }
        if (value instanceof Boolean){
            return value.toString();
        }

        //---------------------------------------------------------------
        if (value instanceof JSONObject){
            return ((JSONObject) value).toString(indentFactor, indent);
        }
        if (value instanceof JSONArray){
            return ((JSONArray) value).toString(indentFactor, indent);
        }
        return JSONUtils.quote(value.toString());
    }

}
