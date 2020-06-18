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
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.feilong.lib.beanutils.DynaProperty;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.JsonVerifier;
import com.feilong.lib.json.util.CycleDetectionStrategy;
import com.feilong.lib.json.util.CycleSetUtil;
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.PropertyFilter;

public class JSONObjectBuilder{

    /** Don't let anyone instantiate this class. */
    private JSONObjectBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Creates a JSONObject.
     * <p>
     * Inspects the object type to call the correct JSONObject factory method.
     * Accepts JSON formatted strings, Maps, DynaBeans and JavaBeans.
     * </p>
     *
     * @param object
     *            the object
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    public static JSONObject build(Object object,JsonConfig jsonConfig){
        if (object == null || JSONUtils.isNull(object)){
            return new JSONObject(true);
        }
        if (object instanceof String){
            String str = (String) object;
            if ("null".equals(str)){
                return new JSONObject(true);
            }
            return JSONTokenerParser.toJSONObject(new JSONTokener(str), jsonConfig);
        }
        //---------------------------------------------------------------
        if (object instanceof JSONObject){
            return fromJSONObject((JSONObject) object, jsonConfig);
        }
        if (object instanceof Map){
            return fromMap((Map<?, ?>) object, jsonConfig);
        }
        if (JSONUtils.isNumber(object) || JSONUtils.isBoolean(object) || JSONUtils.isString(object)){
            return new JSONObject();
        }
        if (object instanceof DynaBean){
            return fromDynaBean((DynaBean) object, jsonConfig);
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
        return fromBean(object, jsonConfig);
    }

    //---------------------------------------------------------------
    /**
     * From dyna bean.
     *
     * @param bean
     *            the bean
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject fromDynaBean(DynaBean bean,JsonConfig jsonConfig){
        return build(bean, jsonObject -> {

            DynaProperty[] dynaPropertys = bean.getDynaClass().getDynaProperties();
            Collection<String> exclusions = jsonConfig.getMergedExcludes();
            PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();

            for (int i = 0; i < dynaPropertys.length; i++){
                boolean bypass = false;
                DynaProperty dynaProperty = dynaPropertys[i];
                String key = dynaProperty.getName();
                if (exclusions.contains(key)){
                    continue;
                }
                Class<?> type = dynaProperty.getType();
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
                JSONObjectValueSetter.set(jsonObject, key, value, type, jsonConfig, bypass);
            }
        });
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
    private static JSONObject fromMap(Map<?, ?> map,JsonConfig jsonConfig){
        return build(map, jsonObject -> {
            Collection<String> exclusions = jsonConfig.getMergedExcludes();
            PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();

            for (Map.Entry<?, ?> entry : map.entrySet()){
                Object k = entry.getKey();
                if (k == null){
                    throw new JSONException("JSON keys can't be null.");
                }
                //---------------------------------------------------------------
                String key = String.valueOf(k);
                if ("null".equals(key)){
                    throw new NullPointerException("JSON keys must not be null nor the 'null' string.");
                }
                if (exclusions.contains(key)){
                    continue;
                }
                //---------------------------------------------------------------
                Object value = entry.getValue();
                if (jsonPropertyFilter != null && jsonPropertyFilter.apply(map, key, value)){
                    continue;
                }

                boolean bypass = false;
                //---------------------------------------------------------------
                if (value != null){
                    JsonValueProcessor jsonValueProcessor = jsonConfig.findJsonValueProcessor(value.getClass(), key);
                    if (jsonValueProcessor != null){
                        value = jsonValueProcessor.processObjectValue(key, value, jsonConfig);
                        bypass = true;
                        if (!JsonVerifier.isValidJsonValue(value)){
                            throw new JSONException("Value is not a valid JSON value. " + value);
                        }
                    }
                    JSONObjectValueSetter.set(jsonObject, key, value, value.getClass(), jsonConfig, bypass);
                }else{
                    jsonObject.accumulate(key, JSONNull.getInstance(), new JsonConfig());
                }
            }
        });
    }

    /**
     * Creates a JSONObject from a POJO. Supports nested maps, POJOs, and arrays/collections.
     *
     * @param bean
     *            An object with POJO conventions
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject fromBean(Object bean,JsonConfig jsonConfig){
        return build(
                        bean,
                        jsonObject -> DefaultBeanProcesser.process(bean, jsonObject, jsonConfig));
    }

    //---------------------------------------------------------------

    /**
     * From JSON object.
     *
     * @param inputJsonObject
     *            the object
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject fromJSONObject(JSONObject inputJsonObject,JsonConfig jsonConfig){
        return build(inputJsonObject, jsonObject -> {
            Collection<String> exclusions = jsonConfig.getMergedExcludes();
            PropertyFilter jsonPropertyFilter = jsonConfig.getJsonPropertyFilter();

            List<String> names = inputJsonObject.names(jsonConfig);
            for (String key : names){
                if (key == null){
                    throw new JSONException("JSON keys cannot be null.");
                }
                if ("null".equals(key)){
                    throw new NullPointerException("JSON keys must not be null nor the 'null' string.");
                }
                if (exclusions.contains(key)){
                    continue;
                }
                Object value = inputJsonObject.get(key);
                if (jsonPropertyFilter != null && jsonPropertyFilter.apply(inputJsonObject, key, value)){
                    continue;
                }

                jsonObject.accumulate(key, value, jsonConfig);
            }
        });
    }

    private static JSONObject build(Object object,JsonHook<JSONObject> jsonHook){
        if (!CycleSetUtil.addInstance(object)){
            try{
                return CycleDetectionStrategy.LENIENT.handleRepeatedReferenceAsObject(object);
            }catch (Exception e){
                CycleSetUtil.removeInstance(object);
                throw new JSONException("", e);
            }
        }
        //---------------------------------------------------------------
        JSONObject jsonObject = new JSONObject();
        try{
            jsonHook.handle(jsonObject);
            CycleSetUtil.removeInstance(object);
            return jsonObject;
        }catch (Exception e){
            CycleSetUtil.removeInstance(object);
            throw new JSONException("", e);
        }
    }
}
