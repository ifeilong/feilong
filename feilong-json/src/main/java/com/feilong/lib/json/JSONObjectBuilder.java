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

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.feilong.lib.json.processors.JsonBeanProcessor;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.JsonVerifier;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.util.JSONExceptionUtil;
import com.feilong.lib.json.util.JSONTokener;
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.PropertyFilter;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class JSONObjectBuilder{

    /** Don't let anyone instantiate this class. */
    private JSONObjectBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static JSONObject build(Object object,JsonConfig jsonConfig){
        if (object == null || JSONUtils.isNull(object)){
            return new JSONObject(true);
        }

        //---------------------------------------------------------------
        if (object instanceof JSONObject){
            return _fromJSONObject((JSONObject) object, jsonConfig);
        }
        if (object instanceof DynaBean){
            return _fromDynaBean((DynaBean) object, jsonConfig);
        }
        if (object instanceof JSONTokener){
            return _fromJSONTokener((JSONTokener) object, jsonConfig);
        }
        if (object instanceof JSONString){
            return _fromJSONString((JSONString) object, jsonConfig);
        }
        if (object instanceof Map){
            return _fromMap((Map) object, jsonConfig);
        }
        if (object instanceof String){
            return _fromString((String) object, jsonConfig);
        }
        if (JSONUtils.isNumber(object) || JSONUtils.isBoolean(object) || JSONUtils.isString(object)){
            return new JSONObject();
        }

        //---------------------------------------------------------------
        if (object instanceof Enum){
            throw new JSONException("'object' is an Enum. Use JSONArray instead");
        }
        if (object instanceof Annotation || object.getClass().isAnnotation()){
            throw new JSONException("'object' is an Annotation.");
        }
        if (JSONUtils.isArray(object)){
            throw new JSONException("'object' is an array. Use JSONArray instead");
        }

        //---------------------------------------------------------------
        return _fromBean(object, jsonConfig);
    }

    /**
     * From JSON string.
     *
     * @param string
     *            the string
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject _fromJSONString(JSONString string,JsonConfig jsonConfig){
        return _fromJSONTokener(new JSONTokener(string.toJSONString()), jsonConfig);
    }

    /**
     * From string.
     *
     * @param str
     *            the str
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject _fromString(String str,JsonConfig jsonConfig){
        if (str == null || "null".equals(str)){
            return new JSONObject(true);
        }
        return _fromJSONTokener(new JSONTokener(str), jsonConfig);
    }

    /**
     * From dyna bean.
     *
     * @param bean
     *            the bean
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject _fromDynaBean(DynaBean bean,JsonConfig jsonConfig){
        if (bean == null){
            return new JSONObject(true);
        }

        if (!CycleSetUtil.addInstance(bean)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(bean);
            }catch (Exception e){
                CycleSetUtil.removeInstance(bean);
                throw JSONExceptionUtil.build("", e);
            }
        }

        //---------------------------------------------------------------

        JSONObject jsonObject = new JSONObject();
        try{
            DynaProperty[] props = bean.getDynaClass().getDynaProperties();
            Collection exclusions = jsonConfig.getMergedExcludes();
            PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
            for (int i = 0; i < props.length; i++){
                boolean bypass = false;
                DynaProperty dynaProperty = props[i];
                String key = dynaProperty.getName();
                if (exclusions.contains(key)){
                    continue;
                }
                Class type = dynaProperty.getType();
                Object value = bean.get(dynaProperty.getName());
                if (jsonPropertyFilter != null && jsonPropertyFilter.apply(bean, key, value)){
                    continue;
                }
                JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(type, key);
                if (jsonValueProcessor != null){
                    value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
                    bypass = true;
                    if (!JsonVerifier.isValidJsonValue(value)){
                        throw new JSONException("Value is not a valid JSON value. " + value);
                    }
                }
                setValue(jsonObject, key, value, type, jsonConfig, bypass);
            }
        }catch (Exception e){
            CycleSetUtil.removeInstance(bean);
            throw JSONExceptionUtil.build("", e);
        }

        CycleSetUtil.removeInstance(bean);
        return jsonObject;
    }

    /**
     * From JSON tokener.
     *
     * @param tokener
     *            the tokener
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    static JSONObject _fromJSONTokener(JSONTokener tokener,JsonConfig jsonConfig){
        char c;
        String key;
        Object value;

        if (tokener.matches("null.*")){
            return new JSONObject(true);
        }

        try{
            if (tokener.nextClean() != '{'){
                throw tokener.syntaxError("A JSONObject text must begin with '{'");
            }

            Collection exclusions = jsonConfig.getMergedExcludes();
            PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
            JSONObject jsonObject = new JSONObject();
            for (;;){
                c = tokener.nextClean();
                switch (c) {
                    case 0:
                        throw tokener.syntaxError("A JSONObject text must end with '}'");
                    case '}':
                        return jsonObject;
                    default:
                        tokener.back();
                        key = tokener.nextValue(jsonConfig).toString();
                }

                //The key is followed by ':'. We will also tolerate '=' or '=>'.
                c = tokener.nextClean();
                if (c == '='){
                    if (tokener.next() != '>'){
                        tokener.back();
                    }
                }else if (c != ':'){
                    throw tokener.syntaxError("Expected a ':' after a key");
                }

                char peek = tokener.peek();
                boolean quoted = peek == '"' || peek == '\'';
                Object v = tokener.nextValue(jsonConfig);
                if (quoted || !JSONUtils.isFunctionHeader(v)){
                    if (exclusions.contains(key)){
                        switch (tokener.nextClean()) {
                            case ';':
                            case ',':
                                if (tokener.nextClean() == '}'){
                                    return jsonObject;
                                }
                                tokener.back();
                                break;
                            case '}':
                                return jsonObject;
                            default:
                                throw tokener.syntaxError("Expected a ',' or '}'");
                        }
                        continue;
                    }
                    if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(tokener, key, v)){
                        if (quoted && v instanceof String && (JSONUtils.mayBeJSON((String) v) || JSONUtils.isFunction(v))){
                            v = JSONUtils.DOUBLE_QUOTE + v + JSONUtils.DOUBLE_QUOTE;
                        }
                        if (jsonObject.properties.containsKey(key)){
                            jsonObject.accumulate(key, v, jsonConfig);
                        }else{
                            jsonObject.element(key, v, jsonConfig);
                        }
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
                    value = new JSONFunction((params != null) ? StringUtils.split(params, ",") : null, text);
                    if (jsonPropertyFilter == null || !jsonPropertyFilter.apply(tokener, key, value)){
                        if (jsonObject.properties.containsKey(key)){
                            jsonObject.accumulate(key, value, jsonConfig);
                        }else{
                            jsonObject.element(key, value, jsonConfig);
                        }
                    }
                }

                // Pairs are separated by ','. We will also tolerate ';'.
                switch (tokener.nextClean()) {
                    case ';':
                    case ',':
                        if (tokener.nextClean() == '}'){
                            return jsonObject;
                        }
                        tokener.back();
                        break;
                    case '}':
                        return jsonObject;
                    default:
                        throw tokener.syntaxError("Expected a ',' or '}'");
                }
            }
        }catch (JSONException jsone){
            throw jsone;
        }
    }

    /**
     * From map.
     *
     * @param map
     *            the map
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject _fromMap(Map map,JsonConfig jsonConfig){
        if (map == null){
            return new JSONObject(true);
        }

        if (!CycleSetUtil.addInstance(map)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(map);
            }catch (Exception e){
                CycleSetUtil.removeInstance(map);
                throw JSONExceptionUtil.build("", e);
            }
        }

        Collection exclusions = jsonConfig.getMergedExcludes();
        JSONObject jsonObject = new JSONObject();
        PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
        try{
            for (Iterator entries = map.entrySet().iterator(); entries.hasNext();){
                boolean bypass = false;
                Map.Entry entry = (Map.Entry) entries.next();
                Object k = entry.getKey();
                if (k == null){
                    throw new JSONException("JSON keys cannot be null.");
                }
                if (!(k instanceof String) && !jsonConfig.isAllowNonStringKeys()){
                    throw new ClassCastException("JSON keys must be strings.");
                }
                String key = String.valueOf(k);
                if ("null".equals(key)){
                    throw new NullPointerException("JSON keys must not be null nor the 'null' string.");
                }
                if (exclusions.contains(key)){
                    continue;
                }
                Object value = entry.getValue();
                if (jsonPropertyFilter != null && jsonPropertyFilter.apply(map, key, value)){
                    continue;
                }
                if (value != null){
                    JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(value.getClass(), key);
                    if (jsonValueProcessor != null){
                        value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
                        bypass = true;
                        if (!JsonVerifier.isValidJsonValue(value)){
                            throw new JSONException("Value is not a valid JSON value. " + value);
                        }
                    }
                    setValue(jsonObject, key, value, value.getClass(), jsonConfig, bypass);
                }else{
                    if (jsonObject.properties.containsKey(key)){
                        jsonObject.accumulate(key, JSONNull.getInstance(), new JsonConfig());
                    }else{
                        jsonObject.element(key, JSONNull.getInstance(), new JsonConfig());
                    }
                }
            }
        }catch (Exception e){
            CycleSetUtil.removeInstance(map);
            throw JSONExceptionUtil.build("", e);
        }

        CycleSetUtil.removeInstance(map);
        return jsonObject;
    }

    /**
     * Creates a JSONObject from a POJO.<br>
     * Supports nested maps, POJOs, and arrays/collections.
     *
     * @param bean
     *            An object with POJO conventions
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     * @throws JSONException
     *             if the bean can not be converted to a proper
     *             JSONObject.
     */
    private static JSONObject _fromBean(Object bean,JsonConfig jsonConfig){
        if (!CycleSetUtil.addInstance(bean)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(bean);
            }catch (Exception e){
                CycleSetUtil.removeInstance(bean);
                throw JSONExceptionUtil.build("", e);
            }
        }
        //---------------------------------------------------------------
        Class<?> beanClass = bean.getClass();
        JsonBeanProcessor processor = jsonConfig.findJsonBeanProcessor(beanClass);
        if (processor != null){
            JSONObject json = null;
            try{
                json = processor.processBean(bean, jsonConfig);
                if (json == null){
                    json = (JSONObject) jsonConfig.findDefaultValueProcessor(beanClass).getDefaultValue(beanClass);
                    if (json == null){
                        json = new JSONObject(true);
                    }
                }
                CycleSetUtil.removeInstance(bean);
            }catch (Exception e){
                CycleSetUtil.removeInstance(bean);
                throw JSONExceptionUtil.build("", e);
            }
            return json;
        }

        JSONObject jsonObject = defaultBeanProcessing(bean, jsonConfig);
        CycleSetUtil.removeInstance(bean);
        return jsonObject;
    }

    //---------------------------------------------------------------

    /**
     * Default bean processing.
     *
     * @param bean
     *            the bean
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject defaultBeanProcessing(Object bean,JsonConfig jsonConfig){
        Class beanClass = bean.getClass();
        PropertyNameProcessor propertyNameProcessor = jsonConfig.findJsonPropertyNameProcessor(beanClass);
        Collection exclusions = jsonConfig.getMergedExcludes(beanClass);
        JSONObject jsonObject = new JSONObject();
        PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();

        //---------------------------------------------------------------

        try{
            PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(bean);

            for (int i = 0; i < propertyDescriptors.length; i++){
                PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
                if (IsIgnoreUtil.isIgnore(propertyDescriptor, exclusions, beanClass, jsonConfig)){
                    continue;
                }
                //---------------------------------------------------------------
                String key = propertyDescriptor.getName();
                Object value = PropertyUtils.getProperty(bean, key);
                if (jsonPropertyFilter != null && jsonPropertyFilter.apply(bean, key, value)){
                    continue;
                }
                //---------------------------------------------------------------
                Class type = propertyDescriptor.getPropertyType();
                set(key, value, jsonConfig, beanClass, propertyNameProcessor, type, jsonObject);
            }

            //---------------------------------------------------------------
        }catch (Exception e){
            CycleSetUtil.removeInstance(bean);
            throw JSONExceptionUtil.build("", e);
        }
        return jsonObject;
    }

    private static void set(
                    String key,
                    Object value,
                    JsonConfig jsonConfig,
                    Class beanClass,
                    PropertyNameProcessor propertyNameProcessor,
                    Class type,
                    JSONObject jsonObject){
        JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(beanClass, type, key);
        boolean bypass = false;
        if (jsonValueProcessor != null){
            value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
            bypass = true;
            if (!JsonVerifier.isValidJsonValue(value)){
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }
        if (propertyNameProcessor != null){
            key = propertyNameProcessor.processPropertyName(beanClass, key);
        }
        setValue(jsonObject, key, value, type, jsonConfig, bypass);
    }

    //---------------------------------------------------------------

    /**
     * From JSON object.
     *
     * @param object
     *            the object
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject _fromJSONObject(JSONObject object,JsonConfig jsonConfig){
        if (object == null || object.isNullObject()){
            return new JSONObject(true);
        }

        if (!CycleSetUtil.addInstance(object)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(object);
            }catch (Exception e){
                CycleSetUtil.removeInstance(object);
                throw JSONExceptionUtil.build("", e);
            }
        }

        //---------------------------------------------------------------

        JSONArray sa = object.names(jsonConfig);
        Collection exclusions = jsonConfig.getMergedExcludes();
        JSONObject jsonObject = new JSONObject();
        PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();
        for (Iterator i = sa.iterator(); i.hasNext();){
            Object k = i.next();
            if (k == null){
                throw new JSONException("JSON keys cannot be null.");
            }
            if (!(k instanceof String) && !jsonConfig.isAllowNonStringKeys()){
                throw new ClassCastException("JSON keys must be strings.");
            }
            String key = String.valueOf(k);
            if ("null".equals(key)){
                throw new NullPointerException("JSON keys must not be null nor the 'null' string.");
            }
            if (exclusions.contains(key)){
                continue;
            }
            Object value = object.get(key);
            if (jsonPropertyFilter != null && jsonPropertyFilter.apply(object, key, value)){
                continue;
            }
            if (jsonObject.properties.containsKey(key)){
                jsonObject.accumulate(key, value, jsonConfig);
            }else{
                jsonObject.setInternal(key, value, jsonConfig);
            }
        }

        CycleSetUtil.removeInstance(object);
        return jsonObject;
    }

    //---------------------------------------------------------------

    /**
     * 设置 value.
     *
     * @param jsonObject
     *            the json object
     * @param key
     *            the key
     * @param value
     *            the value
     * @param type
     *            the type
     * @param jsonConfig
     *            the json config
     * @param bypass
     *            the bypass
     */
    private static void setValue(JSONObject jsonObject,String key,Object value,Class type,JsonConfig jsonConfig,boolean bypass){
        boolean accumulated = false;
        if (value == null){
            value = jsonConfig.findDefaultValueProcessor(type).getDefaultValue(type);
            if (!JsonVerifier.isValidJsonValue(value)){
                throw new JSONException("Value is not a valid JSON value. " + value);
            }
        }

        //---------------------------------------------------------------
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
