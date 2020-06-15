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

import static com.feilong.lib.json.ToStringUtil.ARRAY_START;
import static com.feilong.lib.json.ToStringUtil.OBJECT_START;

import com.feilong.lib.json.util.JSONUtils;

/**
 * Transforms java objects into JSON and back.<br>
 * Transformation from java to JSON is pretty straightforward, but the other way
 * around needs certain configuration, otherwise the java objects produced will
 * be DynaBeans and Lists, because the JSON notation does not carry any
 * information on java classes.<br>
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class JSONSerializer{

    /** Don't let anyone instantiate this class. */
    private JSONSerializer(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Transform a JSON value to a java object.<br>
     * Depending on the configured values for conversion this will return a
     * DynaBean, a bean, a List, or and array.
     *
     * @param json
     *            a JSON value
     * @return depends on the nature of the source object (JSONObject, JSONArray,
     *         JSONNull).
     */
    public static Object toJava(JSON json){
        return toJava(json, new JsonConfig());
    }

    /**
     * Transform a JSON value to a java object.<br>
     * Depending on the configured values for conversion this will return a
     * DynaBean, a bean, a List, or and array.
     *
     * @param json
     *            a JSON value
     * @param jsonConfig
     *            additional configuration
     * @return depends on the nature of the source object (JSONObject, JSONArray,
     *         JSONNull) and the configured rootClass, classMap and arrayMode
     */
    private static Object toJava(JSON json,JsonConfig jsonConfig){
        if (JSONUtils.isNull(json)){
            return null;
        }

        //---------------------------------------------------------------
        if (json instanceof JSONArray){
            if (jsonConfig.getArrayMode() == JsonConfig.MODE_OBJECT_ARRAY){
                return JSONArrayToBeanUtil.toArray((JSONArray) json, jsonConfig);
            }
            return JSONArrayToBeanUtil.toCollection((JSONArray) json, jsonConfig);
        }
        return JSONObjectToBeanUtil.toBean((JSONObject) json, jsonConfig);
    }

    /**
     * Creates a JSONObject, JSONArray or a JSONNull from object.<br>
     * Accepts JSON formatted strings, Maps, arrays, Collections, DynaBeans and
     * JavaBeans.
     *
     * @param object
     *            any java Object
     * @return the json
     */
    public static JSON toJSON(Object object){
        return toJSON(object, new JsonConfig());
    }

    /**
     * Creates a JSONObject, JSONArray or a JSONNull from object.<br>
     * Accepts JSON formatted strings, Maps, arrays, Collections, DynaBeans and
     * JavaBeans.
     *
     * @param object
     *            any java Object
     * @param jsonConfig
     *            additional configuration
     * @return the json
     */
    public static JSON toJSON(Object object,JsonConfig jsonConfig){
        if (object == null){
            return JSONNull.getInstance();
        }
        if (object instanceof String){
            return toJSON((String) object, jsonConfig);
        }
        if (JSONUtils.isArray(object)){
            return JSONArrayBuilder.fromObject(object, jsonConfig);
        }
        //---------------------------------------------------------------
        try{
            return JSONObjectBuilder.build(object, jsonConfig);
        }catch (JSONException e){
            if (object instanceof JSONTokener){
                ((JSONTokener) object).reset();
            }
            return JSONArrayBuilder.fromObject(object, jsonConfig);
        }
    }

    /**
     * Creates a JSONObject, JSONArray or a JSONNull from a JSONString.
     *
     * @param string
     *            the string
     * @param jsonConfig
     *            the json config
     * @return the json
     * @throws JSONException
     *             if the string is not a valid JSON string
     */
    private static JSON toJSON(String string,JsonConfig jsonConfig){
        if (string.startsWith(OBJECT_START)){
            return JSONArrayBuilder.fromObject(string, jsonConfig);
        }
        if (string.startsWith(ARRAY_START)){
            return JSONObjectBuilder.build(string, jsonConfig);
        }
        if ("null".equals(string)){
            return JSONNull.getInstance();
        }
        throw new JSONException("Invalid JSON String:" + string);
    }
}