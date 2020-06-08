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

import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static java.util.Collections.emptyMap;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.lib.beanutils.PropertyUtils;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.util.ClassResolver;
import com.feilong.lib.json.util.JSONExceptionUtil;
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.json.util.PropertyNameProcessorUtil;
import com.feilong.lib.json.util.PropertySetStrategy;
import com.feilong.lib.json.util.TargetClassFinder;
import com.feilong.lib.lang3.tuple.Triple;

/**
 * JSONObject 转成 Bean的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class JSONObjectToBeanUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONObjectToBeanUtil.class);

    /** Don't let anyone instantiate this class. */
    private JSONObjectToBeanUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Creates a bean from a JSONObject, with the specific configuration.
     *
     * @param jsonObject
     *            the json object
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    public static Object toBean(JSONObject jsonObject,JsonConfig jsonConfig){
        if (jsonObject == null || jsonObject.isNullObject()){
            return null;
        }
        //---------------------------------------------------------------
        Class<?> rootClass = jsonConfig.getRootClass();

        //---------------------------------------------------------------
        Map<String, Class<?>> classMap = defaultIfNull(jsonConfig.getClassMap(), emptyMap());

        Map<String, Class<?>> jsonKeyAndClassMap = JSONUtils.getProperties(jsonObject);

        Object rootBean = newBean(rootClass);

        //---------------------------------------------------------------
        //所有经过filter 以及加工key 之后的数据
        List<Triple<String, String, Object>> list = buildTripleList(rootBean, jsonObject, jsonConfig);
        //---------------------------------------------------------------
        for (Triple<String, String, Object> triple : list){
            String name = triple.getLeft();
            String key = triple.getMiddle();
            Object value = triple.getRight();

            //---------------------------------------------------------------
            Class<?> type = jsonKeyAndClassMap.get(name);
            try{
                if (Map.class.isAssignableFrom(rootClass)){
                    toBeanDoWithMap(classMap, rootBean, name, type, value, key, jsonConfig);
                }else{
                    PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(rootBean, key);
                    if (propertyDescriptor != null && propertyDescriptor.getWriteMethod() == null){
                        LOGGER.debug("{} property '{}' has no write method. SKIPPED.", rootBean.getClass(), key);
                        continue;
                    }
                    toBeanUsePropertyDescriptor(rootBean, key, value, name, type, jsonConfig, classMap, propertyDescriptor);
                }
            }catch (Exception e){
                throw JSONExceptionUtil.build("Error while setting property=" + name + " type" + type, e);
            }
        }
        return rootBean;
    }

    private static List<Triple<String, String, Object>> buildTripleList(Object rootBean,JSONObject jsonObject,JsonConfig jsonConfig){
        List<String> names = jsonObject.names(jsonConfig);

        Class<?> rootClass = jsonConfig.getRootClass();

        PropertyFilter javaPropertyFilter = jsonConfig.getJavaPropertyFilter();
        PropertyNameProcessor propertyNameProcessor = jsonConfig.findJavaPropertyNameProcessor(rootClass);

        List<Triple<String, String, Object>> list = newArrayList();
        for (String name : names){
            Object value = jsonObject.get(name);
            if (javaPropertyFilter != null && javaPropertyFilter.apply(rootBean, name, value)){
                continue;
            }

            //---------------------------------------------------------------
            String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            key = PropertyNameProcessorUtil.update(rootClass, key, propertyNameProcessor);

            list.add(Triple.of(name, key, value));
        }
        return list;
    }

    /**
     * New bean.
     *
     * @param rootClass
     *            the root class
     * @return the object
     * @throws JSONException
     *             the JSON exception
     * @since 3.0.4
     */
    private static Object newBean(Class<?> rootClass) throws JSONException{
        try{
            if (rootClass.isInterface()){
                if (!Map.class.isAssignableFrom(rootClass)){
                    throw new JSONException("beanClass is an interface. " + rootClass);
                }
                return new HashMap<>();
            }
            return ConstructorUtil.newInstance(rootClass);
        }catch (Exception e){
            throw JSONExceptionUtil.build("", e);
        }
    }

    //---------------------------------------------------------------
    private static void toBeanUsePropertyDescriptor(
                    Object bean,
                    String key,
                    Object value,

                    String name,

                    Class<?> type,

                    JsonConfig jsonConfig,
                    Map<String, Class<?>> classMap,
                    PropertyDescriptor propertyDescriptor){

        if (JSONUtils.isNull(value)){
            setNull(bean, type, key);
            return;
        }

        if (propertyDescriptor != null){
            doWithPropertyDescriptor(bean, key, value, name, type, jsonConfig, classMap, propertyDescriptor);
            return;
        }

        //---------------------------------------------------------------
        if (value instanceof JSONArray){
            setProperty(bean, key, PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, List.class));
            return;
        }
        if (isCommonType(type)){
            Class<?> beanClass = bean.getClass();
            if (beanClass == null || bean instanceof Map){
                setProperty(bean, key, value);
                return;
            }
            LOGGER.debug("skip set [{}] property [{}]:[{}]", bean.getClass().getName(), key, type.getName());
            return;
        }
        //---------------------------------------------------------------
        setProperty(bean, key, value);
        return;
    }

    private static void doWithPropertyDescriptor(
                    Object bean,
                    String key,
                    Object value,

                    String name,

                    Class<?> type,
                    JsonConfig jsonConfig,
                    Map<String, Class<?>> classMap,
                    PropertyDescriptor propertyDescriptor){
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        Class<?> targetType = propertyType;

        //---------------------------------------------------------------
        if (isCommonType(type)){
            if (targetType.isInstance(value)){
                setProperty(bean, key, value);
                return;
            }
            setProperty(bean, key, PropertyValueMorpher.morphPropertyValue(key, value, type, targetType));
            return;
        }

        //---------------------------------------------------------------

        if (value instanceof JSONArray){
            if (List.class.isAssignableFrom(propertyType) || Set.class.isAssignableFrom(propertyType)){
                setProperty(bean, key, PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, propertyType));
            }else{
                setProperty(bean, key, PropertyValueConvertUtil.toArray(key, value, targetType, jsonConfig, classMap));
            }
            return;
        }

        //---------------------------------------------------------------
        if (targetType == Object.class || targetType.isInterface()){
            Class<?> targetTypeCopy = targetType;
            targetType = TargetClassFinder.findTargetClass(key, classMap);
            targetType = targetType == null ? TargetClassFinder.findTargetClass(name, classMap) : targetType;
            targetType = targetType == null && targetTypeCopy.isInterface() ? targetTypeCopy : targetType;
        }

        //---------------------------------------------------------------
        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(targetType);
        jsonConfigCopy.setClassMap(classMap);
        setProperty(bean, key, toBean((JSONObject) value, jsonConfigCopy));
    }

    private static boolean isCommonType(Class<?> type){
        return String.class.isAssignableFrom(type) || //
                        JSONUtils.isBoolean(type) || //
                        JSONUtils.isNumber(type) || //
                        JSONUtils.isString(type);
    }

    /**
     * 设置 null.
     *
     * @param bean
     *            the bean
     * @param type
     *            the type
     * @param key
     *            the key
     */
    private static void setNull(Object bean,Class<?> type,String key){
        if (type.isPrimitive()){
            // assume assigned default value
            LOGGER.warn("Tried to assign null value to {}:{}", key, type.getName());
            setProperty(bean, key, JSONUtils.getMorpherRegistry().morph(type, null));
        }else{
            setProperty(bean, key, null);
        }
    }

    private static void toBeanDoWithMap(
                    Map<String, Class<?>> classMap,
                    Object bean,
                    String name,
                    Class<?> type,
                    Object value,
                    String key,
                    JsonConfig jsonConfig) throws Exception{
        // no type info available for conversion
        if (JSONUtils.isNull(value)){
            setProperty(bean, key, value);
            return;
        }

        //---------------------------------------------------------------
        if (value instanceof JSONArray){
            setProperty(bean, key, PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, List.class));
            return;
        }
        if (isCommonType(type)){
            setProperty(bean, key, value);
            return;
        }

        //---------------------------------------------------------------
        Class<?> targetClass = ClassResolver.resolveClass(classMap, key, name, type);

        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(targetClass);
        jsonConfigCopy.setClassMap(classMap);
        if (targetClass != null){
            setProperty(bean, key, toBean((JSONObject) value, jsonConfigCopy));
        }else{
            setProperty(bean, key, toDynaBean((JSONObject) value));
        }
    }

    /**
     * Creates a JSONDynaBean from a JSONObject.
     *
     * @param jsonObject
     *            the json object
     * @return the object
     */
    @Deprecated
    private static DynaBean toDynaBean(JSONObject jsonObject){
        JsonConfig jsonConfig = new JsonConfig();
        Map<String, Class<?>> props = JSONUtils.getProperties(jsonObject);
        DynaBean dynaBean = JSONUtils.newDynaBean(jsonObject, jsonConfig);

        for (Iterator entries = jsonObject.names(jsonConfig).iterator(); entries.hasNext();){
            String name = (String) entries.next();
            String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            Class<?> type = props.get(name);
            Object value = jsonObject.get(name);
            try{
                if (!JSONUtils.isNull(value)){
                    if (value instanceof JSONArray){
                        dynaBean.set(key, JSONArrayToBeanUtil.toCollection((JSONArray) value));
                    }else if (String.class.isAssignableFrom(type) || //
                                    Boolean.class.isAssignableFrom(type) || //
                                    JSONUtils.isNumber(type) || //
                                    Character.class.isAssignableFrom(type)){
                        dynaBean.set(key, value);
                    }else{
                        dynaBean.set(key, toDynaBean((JSONObject) value));
                    }
                }else{
                    if (type.isPrimitive()){
                        // assume assigned default value
                        LOGGER.warn("Tried to assign null value to {}:{}", key, type.getName());
                        dynaBean.set(key, JSONUtils.getMorpherRegistry().morph(type, null));
                    }else{
                        dynaBean.set(key, null);
                    }
                }
            }catch (Exception e){
                throw JSONExceptionUtil.build("Error while setting property=" + name + " type" + type, e);
            }
        }

        return dynaBean;
    }

    //---------------------------------------------------------------

    /**
     * Sets a property on the target bean.<br>
     * Bean may be a Map or a POJO.
     *
     * @param bean
     *            the bean
     * @param key
     *            the key
     * @param value
     *            the value
     */
    private static void setProperty(Object bean,String key,Object value){
        PropertySetStrategy.DEFAULT.setProperty(bean, key, value);
    }
}
