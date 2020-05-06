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

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.json.util.JSONExceptionUtil;
import com.feilong.lib.json.util.JSONTokener;
import com.feilong.lib.json.util.JSONUtils;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class JSONArrayBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONArrayBuilder.class);

    public static JSONArray fromObject(Object object,JsonConfig jsonConfig){
        if (object instanceof JSONString){
            return _fromJSONString((JSONString) object, jsonConfig);
        }
        if (object instanceof JSONArray){
            return _fromJSONArray((JSONArray) object, jsonConfig);
        }
        if (object instanceof Collection){
            return _fromCollection((Collection) object, jsonConfig);
        }
        if (object instanceof JSONTokener){
            return _fromJSONTokener((JSONTokener) object, jsonConfig);
        }
        if (object instanceof String){
            return _fromString((String) object, jsonConfig);
        }
        if (object != null && object.getClass().isArray()){
            Class type = object.getClass().getComponentType();
            if (!type.isPrimitive()){
                return _fromArray((Object[]) object, jsonConfig);
            }
            if (type == Boolean.TYPE){
                return _fromArray((boolean[]) object, jsonConfig);
            }else if (type == Byte.TYPE){
                return _fromArray((byte[]) object, jsonConfig);
            }else if (type == Short.TYPE){
                return _fromArray((short[]) object, jsonConfig);
            }else if (type == Integer.TYPE){
                return _fromArray((int[]) object, jsonConfig);
            }else if (type == Long.TYPE){
                return _fromArray((long[]) object, jsonConfig);
            }else if (type == Float.TYPE){
                return _fromArray((float[]) object, jsonConfig);
            }else if (type == Double.TYPE){
                return _fromArray((double[]) object, jsonConfig);
            }else if (type == Character.TYPE){
                return _fromArray((char[]) object, jsonConfig);
            }

            throw new JSONException("Unsupported type");
        }else if (JSONUtils.isBoolean(object) || JSONUtils.isFunction(object) || JSONUtils.isNumber(object) || JSONUtils.isNull(object)
                        || JSONUtils.isString(object) || object instanceof JSON){
            JSONArray jsonArray = new JSONArray().element(object, jsonConfig);
            return jsonArray;
        }else if (object instanceof Enum){
            return _fromArray((Enum) object, jsonConfig);
        }else if (object instanceof Annotation || (object != null && object.getClass().isAnnotation())){
            throw new JSONException("Unsupported type");
        }else if (JSONUtils.isObject(object)){
            JSONArray jsonArray = new JSONArray().element(JSONObject.fromObject(object, jsonConfig));
            return jsonArray;
        }
        throw new JSONException("Unsupported type");
    }

    /**
     * Construct a JSONArray from an boolean[].<br>
     *
     * @param array
     *            An boolean[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(boolean[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Boolean b = array[i] ? Boolean.TRUE : Boolean.FALSE;
            jsonArray.addValue(b, jsonConfig);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an byte[].<br>
     *
     * @param array
     *            An byte[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(byte[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }

        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = JSONUtils.transformNumber(new Byte(array[i]));
            jsonArray.addValue(n, jsonConfig);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an char[].<br>
     *
     * @param array
     *            An char[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(char[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Character c = new Character(array[i]);
            jsonArray.addValue(c, jsonConfig);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an double[].<br>
     *
     * @param array
     *            An double[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(double[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        try{
            for (int i = 0; i < array.length; i++){
                Double d = new Double(array[i]);
                JSONUtils.testValidity(d);
                jsonArray.addValue(d, jsonConfig);
            }
        }catch (Exception e){
            CycleSetUtil.removeInstance(array);
            throw JSONExceptionUtil.build("", e);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an Enum value.
     *
     * @param e
     *            A enum value.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     * @throws JSONException
     *             If there is a syntax error.
     */
    private static JSONArray _fromArray(Enum e,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(e)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(e);
            }catch (Exception ex){
                CycleSetUtil.removeInstance(e);
                throw JSONExceptionUtil.build("", ex);
            }
        }
        JSONArray jsonArray = new JSONArray();
        if (e != null){
            jsonArray.addValue(e, jsonConfig);
        }else{
            JSONException jsone = new JSONException("enum value is null");
            CycleSetUtil.removeInstance(e);
            throw jsone;
        }

        CycleSetUtil.removeInstance(e);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an float[].<br>
     *
     * @param array
     *            An float[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(float[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        try{
            for (int i = 0; i < array.length; i++){
                Float f = new Float(array[i]);
                JSONUtils.testValidity(f);
                jsonArray.addValue(f, jsonConfig);
            }
        }catch (Exception e){
            CycleSetUtil.removeInstance(array);
            throw JSONExceptionUtil.build("", e);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an int[].<br>
     *
     * @param array
     *            An int[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(int[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = new Integer(array[i]);
            jsonArray.addValue(n, jsonConfig);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an long[].<br>
     *
     * @param array
     *            An long[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(long[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = JSONUtils.transformNumber(new Long(array[i]));
            jsonArray.addValue(n, jsonConfig);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    // ------------------------------------------------------

    /**
     * From array.
     *
     * @param array
     *            the array
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(Object[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        try{
            for (int i = 0; i < array.length; i++){
                Object element = array[i];
                jsonArray.addValue(element, jsonConfig);
            }
        }catch (Exception e){
            CycleSetUtil.removeInstance(array);
            throw JSONExceptionUtil.build("", e);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * Construct a JSONArray from an short[].<br>
     *
     * @param array
     *            An short[] array.
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromArray(short[] array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < array.length; i++){
            Number n = JSONUtils.transformNumber(new Short(array[i]));
            jsonArray.addValue(n, jsonConfig);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * From collection.
     *
     * @param collection
     *            the collection
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    static JSONArray _fromCollection(Collection collection,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(collection)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(collection);
            }catch (Exception e){
                CycleSetUtil.removeInstance(collection);
                throw JSONExceptionUtil.build("", e);
            }
        }

        JSONArray jsonArray = new JSONArray();
        try{
            int i = 0;
            for (Iterator elements = collection.iterator(); elements.hasNext();){
                Object element = elements.next();
                jsonArray.addValue(element, jsonConfig);
            }
        }catch (Exception e){
            CycleSetUtil.removeInstance(collection);
            throw JSONExceptionUtil.build("", e);
        }

        CycleSetUtil.removeInstance(collection);
        return jsonArray;
    }

    /**
     * From JSON array.
     *
     * @param array
     *            the array
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromJSONArray(JSONArray array,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(array)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsArray(array);
            }catch (Exception e){
                CycleSetUtil.removeInstance(array);
                throw JSONExceptionUtil.build("", e);
            }
        }
        JSONArray jsonArray = new JSONArray();
        int index = 0;
        for (Iterator elements = array.iterator(); elements.hasNext();){
            Object element = elements.next();
            jsonArray.addValue(element, jsonConfig);
        }

        CycleSetUtil.removeInstance(array);
        return jsonArray;
    }

    /**
     * From JSON string.
     *
     * @param string
     *            the string
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromJSONString(JSONString string,JsonConfig jsonConfig){
        return _fromJSONTokener(new JSONTokener(string.toJSONString()), jsonConfig);
    }

    /**
     * From JSON tokener.
     *
     * @param tokener
     *            the tokener
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    static JSONArray _fromJSONTokener(JSONTokener tokener,JsonConfig jsonConfig){

        JSONArray jsonArray = new JSONArray();
        int index = 0;

        try{
            if (tokener.nextClean() != '['){
                throw tokener.syntaxError("A JSONArray text must start with '['");
            }
            if (tokener.nextClean() == ']'){
                return jsonArray;
            }
            tokener.back();
            for (;;){
                if (tokener.nextClean() == ','){
                    tokener.back();
                    jsonArray.elements.add(JSONNull.getInstance());
                }else{
                    tokener.back();
                    Object v = tokener.nextValue(jsonConfig);
                    if (!JSONUtils.isFunctionHeader(v)){
                        if (v instanceof String && JSONUtils.mayBeJSON((String) v)){
                            jsonArray.addValue(JSONUtils.DOUBLE_QUOTE + v + JSONUtils.DOUBLE_QUOTE, jsonConfig);
                        }else{
                            jsonArray.addValue(v, jsonConfig);
                        }
                    }else{
                        // read params if any
                        String params = JSONUtils.getFunctionParams((String) v);
                        // read function text
                        int i = 0;
                        StringBuffer sb = new StringBuffer();
                        for (;;){
                            char ch = tokener.next();
                            if (ch == 0){
                                break;
                            }
                            if (ch == '{'){
                                i++;
                            }
                            if (ch == '}'){
                                i--;
                            }
                            sb.append(ch);
                            if (i == 0){
                                break;
                            }
                        }
                        if (i != 0){
                            throw tokener.syntaxError("Unbalanced '{' or '}' on prop: " + v);
                        }
                        // trim '{' at start and '}' at end
                        String text = sb.toString();
                        text = text.substring(1, text.length() - 1).trim();
                        jsonArray.addValue(new JSONFunction((params != null) ? StringUtils.split(params, ",") : null, text), jsonConfig);
                    }
                }
                switch (tokener.nextClean()) {
                    case ';':
                    case ',':
                        if (tokener.nextClean() == ']'){
                            return jsonArray;
                        }
                        tokener.back();
                        break;
                    case ']':
                        return jsonArray;
                    default:
                        throw tokener.syntaxError("Expected a ',' or ']'");
                }
            }
        }catch (Exception e){
            throw JSONExceptionUtil.build("", e);
        }
    }

    /**
     * From string.
     *
     * @param string
     *            the string
     * @param jsonConfig
     *            the json config
     * @return the JSON array
     */
    private static JSONArray _fromString(String string,JsonConfig jsonConfig){
        return _fromJSONTokener(new JSONTokener(string), jsonConfig);
    }

}
