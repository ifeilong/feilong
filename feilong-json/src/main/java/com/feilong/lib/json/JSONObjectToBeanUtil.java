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

import static java.util.Collections.emptyMap;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.lib.ezmorph.Morpher;
import com.feilong.lib.ezmorph.array.ObjectArrayMorpher;
import com.feilong.lib.ezmorph.bean.BeanMorpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.util.JSONExceptionUtil;
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.KeyUpdate;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.json.util.PropertySetStrategy;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class JSONObjectToBeanUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONObjectToBeanUtil.class);

    /** Don't let anyone instantiate this class. */
    private JSONObjectToBeanUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Creates a JSONDynaBean from a JSONObject.
     *
     * @param jsonObject
     *            the json object
     * @return the object
     */
    public static Object toBean(JSONObject jsonObject){
        if (jsonObject == null || jsonObject.isNullObject()){
            return null;
        }

        DynaBean dynaBean = null;

        JsonConfig jsonConfig = new JsonConfig();
        Map props = JSONUtils.getProperties(jsonObject);
        dynaBean = JSONUtils.newDynaBean(jsonObject, jsonConfig);
        for (Iterator entries = jsonObject.names(jsonConfig).iterator(); entries.hasNext();){
            String name = (String) entries.next();
            String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            Class<?> type = (Class<?>) props.get(name);
            Object value = jsonObject.get(name);
            try{
                if (!JSONUtils.isNull(value)){
                    if (value instanceof JSONArray){
                        dynaBean.set(key, JSONArrayToBeanUtil.toCollection((JSONArray) value));
                    }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                                    || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){
                        dynaBean.set(key, value);
                    }else{
                        dynaBean.set(key, toBean((JSONObject) value));
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
        Class<?> beanClass = jsonConfig.getRootClass();
        if (beanClass == null){
            return toBean(jsonObject);
        }

        //---------------------------------------------------------------
        Map classMap = jsonConfig.getClassMap();
        if (classMap == null){
            classMap = emptyMap();
        }

        //---------------------------------------------------------------

        Object bean = null;
        try{
            if (beanClass.isInterface()){
                if (!Map.class.isAssignableFrom(beanClass)){
                    throw new JSONException("beanClass is an interface. " + beanClass);
                }
                bean = new HashMap<>();
            }else{
                bean = ConstructorUtil.newInstance(beanClass);
            }
        }catch (Exception e){
            throw JSONExceptionUtil.build("", e);
        }

        //---------------------------------------------------------------

        Map properties = JSONUtils.getProperties(jsonObject);

        PropertyFilter javaPropertyFilter = jsonConfig.getJavaPropertyFilter();
        for (Iterator entries = jsonObject.names(jsonConfig).iterator(); entries.hasNext();){
            String name = (String) entries.next();
            Object value = jsonObject.get(name);

            if (javaPropertyFilter != null && javaPropertyFilter.apply(bean, name, value)){
                continue;
            }

            //---------------------------------------------------------------
            String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);

            //---------------------------------------------------------------
            PropertyNameProcessor propertyNameProcessor = jsonConfig.findJavaPropertyNameProcessor(beanClass);
            key = KeyUpdate.update(beanClass, key, propertyNameProcessor);

            //---------------------------------------------------------------
            Class<?> type = (Class<?>) properties.get(name);
            try{
                if (Map.class.isAssignableFrom(beanClass)){
                    toBeanDoWithMap(jsonConfig, classMap, bean, name, type, value, key);
                }else{
                    PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, key);
                    if (propertyDescriptor != null && propertyDescriptor.getWriteMethod() == null){
                        LOGGER.debug("Property '{}' of {} has no write method. SKIPPED.", key, bean.getClass());
                        continue;
                    }

                    toBeanDoWithBean(bean, name, type, value, key, jsonConfig, classMap, propertyDescriptor);
                }
            }catch (Exception e){
                throw JSONExceptionUtil.build("Error while setting property=" + name + " type" + type, e);
            }
        }
        return bean;
    }

    private static void toBeanDoWithBean(
                    Object bean,
                    String name,
                    Class<?> type,
                    Object value,

                    String key,

                    JsonConfig jsonConfig,
                    Map classMap,
                    PropertyDescriptor propertyDescriptor) throws Exception{

        Class<?> beanClass = bean.getClass();
        if (propertyDescriptor != null){
            if (JSONUtils.isNull(value)){
                setNull(jsonConfig, bean, type, key);
                return;
            }

            //---------------------------------------------------------------
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            Class<?> targetType = propertyType;
            if (value instanceof JSONArray){
                if (List.class.isAssignableFrom(propertyType) || Set.class.isAssignableFrom(propertyType)){
                    setProperty(
                                    bean,
                                    key,
                                    PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, propertyType),
                                    jsonConfig);
                }else{
                    setProperty(bean, key, PropertyValueConvertUtil.toArray(key, value, targetType, jsonConfig, classMap), jsonConfig);
                }
                return;
            }

            //---------------------------------------------------------------
            if (String.class.isAssignableFrom(type) || //
                            JSONUtils.isBoolean(type) || //
                            JSONUtils.isNumber(type) || //
                            JSONUtils.isString(type) || //
                            JSONFunction.class.isAssignableFrom(type)){
                if (!targetType.isInstance(value)){
                    setProperty(bean, key, PropertyValueMorpher.morphPropertyValue(key, value, type, targetType), jsonConfig);
                }else{
                    setProperty(bean, key, value, jsonConfig);
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
            JsonConfig jsc = jsonConfig.copy();
            jsc.setRootClass(targetType);
            jsc.setClassMap(classMap);
            setProperty(bean, key, toBean((JSONObject) value, jsc), jsonConfig);
            return;
        }

        //---------------------------------------------------------------

        if (!JSONUtils.isNull(value)){
            if (value instanceof JSONArray){
                setProperty(
                                bean,
                                key,
                                PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, List.class),
                                jsonConfig);
                return;
            }
            if (String.class.isAssignableFrom(type) //
                            || JSONUtils.isBoolean(type) //
                            || JSONUtils.isNumber(type) //
                            || JSONUtils.isString(type)//
                            || JSONFunction.class.isAssignableFrom(type)){
                if (beanClass == null || bean instanceof Map){
                    setProperty(bean, key, value, jsonConfig);
                }else{
                    LOGGER.info("skip set [{}] property [{}]:[{}]", bean.getClass().getName(), key, type.getName());
                }
                return;
            }

            //---------------------------------------------------------------

            setProperty(bean, key, value, jsonConfig);

            return;
        }

        //---------------------------------------------------------------
        setNull(jsonConfig, bean, type, key);
    }

    private static void setNull(JsonConfig jsonConfig,Object bean,Class<?> type,String key) throws Exception{
        if (type.isPrimitive()){
            // assume assigned default value
            LOGGER.warn("Tried to assign null value to {}:{}", key, type.getName());
            setProperty(bean, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
        }else{
            setProperty(bean, key, null, jsonConfig);
        }
    }

    private static void toBeanDoWithMap(JsonConfig jsonConfig,Map classMap,Object bean,String name,Class<?> type,Object value,String key)
                    throws Exception{
        // no type info available for conversion
        if (JSONUtils.isNull(value)){
            setProperty(bean, key, value, jsonConfig);
            return;
        }
        if (value instanceof JSONArray){
            setProperty(bean, key, PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, List.class), jsonConfig);
            return;
        }
        if (String.class.isAssignableFrom(type) || //
                        JSONUtils.isBoolean(type) || //
                        JSONUtils.isNumber(type) || //
                        JSONUtils.isString(type) || //
                        JSONFunction.class.isAssignableFrom(type)){
            setProperty(bean, key, value, jsonConfig);
            return;
        }

        //---------------------------------------------------------------
        Class<?> targetClass = ClassResolver.resolveClass(classMap, key, name, type);

        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(targetClass);
        jsonConfigCopy.setClassMap(classMap);
        if (targetClass != null){
            setProperty(bean, key, toBean((JSONObject) value, jsonConfigCopy), jsonConfig);
        }else{
            setProperty(bean, key, toBean((JSONObject) value), jsonConfig);
        }
    }

    /**
     * Creates a bean from a JSONObject, with the specific configuration.
     *
     * @param jsonObject
     *            the json object
     * @param root
     *            the root
     * @param jsonConfig
     *            the json config
     * @return the object
     */
    public static Object toBean(JSONObject jsonObject,Object root,JsonConfig jsonConfig){
        if (jsonObject == null || jsonObject.isNullObject() || root == null){
            return root;
        }

        Class<?> rootClass = root.getClass();
        if (rootClass.isInterface()){
            throw new JSONException("Root bean is an interface. " + rootClass);
        }

        Map classMap = jsonConfig.getClassMap();
        if (classMap == null){
            classMap = emptyMap();
        }

        Map props = JSONUtils.getProperties(jsonObject);
        PropertyFilter javaPropertyFilter = jsonConfig.getJavaPropertyFilter();
        for (Iterator entries = jsonObject.names(jsonConfig).iterator(); entries.hasNext();){
            String name = (String) entries.next();
            Class<?> type = (Class<?>) props.get(name);
            Object value = jsonObject.get(name);
            if (javaPropertyFilter != null && javaPropertyFilter.apply(root, name, value)){
                continue;
            }
            String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            try{
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(root, key);
                if (pd != null && pd.getWriteMethod() == null){
                    LOGGER.info("Property '{}' of {} has no write method. SKIPPED.", key, root.getClass());
                    continue;
                }

                if (!JSONUtils.isNull(value)){
                    if (value instanceof JSONArray){
                        if (pd == null || List.class.isAssignableFrom(pd.getPropertyType())){
                            Class<?> targetClass = ClassResolver.resolveClass(classMap, key, name, type);
                            Object newRoot = ConstructorUtil.newInstance(targetClass);
                            List list = JSONArrayToBeanUtil.toList((JSONArray) value, newRoot, jsonConfig);
                            setProperty(root, key, list, jsonConfig);
                        }else{
                            Class<?> innerType = JSONUtils.getInnerComponentType(pd.getPropertyType());
                            Class<?> targetInnerType = TargetClassFinder.findTargetClass(key, classMap);
                            if (innerType.equals(Object.class) && targetInnerType != null && !targetInnerType.equals(Object.class)){
                                innerType = targetInnerType;
                            }
                            Object newRoot = ConstructorUtil.newInstance(innerType);
                            Object array = JSONArrayToBeanUtil.toArray((JSONArray) value, newRoot, jsonConfig);
                            if (innerType.isPrimitive() || JSONUtils.isNumber(innerType) || Boolean.class.isAssignableFrom(innerType)
                                            || JSONUtils.isString(innerType)){
                                array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);
                            }else if (!array.getClass().equals(pd.getPropertyType())){
                                if (!pd.getPropertyType().equals(Object.class)){
                                    Morpher morpher = JSONUtils.getMorpherRegistry()
                                                    .getMorpherFor(Array.newInstance(innerType, 0).getClass());
                                    if (IdentityObjectMorpher.getInstance().equals(morpher)){
                                        ObjectArrayMorpher beanMorpher = new ObjectArrayMorpher(
                                                        new BeanMorpher(innerType, JSONUtils.getMorpherRegistry()));
                                        JSONUtils.getMorpherRegistry().registerMorpher(beanMorpher);
                                    }
                                    array = JSONUtils.getMorpherRegistry().morph(Array.newInstance(innerType, 0).getClass(), array);
                                }
                            }
                            setProperty(root, key, array, jsonConfig);
                        }
                    }else if (String.class.isAssignableFrom(type) || JSONUtils.isBoolean(type) || JSONUtils.isNumber(type)
                                    || JSONUtils.isString(type) || JSONFunction.class.isAssignableFrom(type)){
                        if (pd != null){
                            if (!pd.getPropertyType().isInstance(value)){
                                Morpher morpher = JSONUtils.getMorpherRegistry().getMorpherFor(pd.getPropertyType());
                                if (IdentityObjectMorpher.getInstance().equals(morpher)){
                                    LOGGER.warn(
                                                    "Can't transform property '{}' from {} into {}. Will register a default BeanMorpher",
                                                    key,
                                                    type.getName(),
                                                    pd.getPropertyType().getName());
                                    JSONUtils.getMorpherRegistry()
                                                    .registerMorpher(new BeanMorpher(pd.getPropertyType(), JSONUtils.getMorpherRegistry()));
                                }
                                setProperty(root, key, JSONUtils.getMorpherRegistry().morph(pd.getPropertyType(), value), jsonConfig);
                            }else{
                                setProperty(root, key, value, jsonConfig);
                            }
                        }else if (root instanceof Map){
                            setProperty(root, key, value, jsonConfig);
                        }else{
                            LOGGER.warn(
                                            "Tried to assign property {}:{} to bean of class {}",
                                            key,
                                            type.getName(),
                                            root.getClass().getName());
                        }
                    }else{
                        if (pd != null){
                            Class<?> targetClass = pd.getPropertyType();

                            if (targetClass == Object.class){
                                targetClass = ClassResolver.resolveClass(classMap, key, name, type);
                                if (targetClass == null){
                                    targetClass = Object.class;
                                }
                            }
                            Object newRoot = ConstructorUtil.newInstance(targetClass);
                            setProperty(root, key, toBean((JSONObject) value, newRoot, jsonConfig), jsonConfig);
                        }else if (root instanceof Map){
                            Class<?> targetClass = TargetClassFinder.findTargetClass(key, classMap);
                            targetClass = targetClass == null ? TargetClassFinder.findTargetClass(name, classMap) : targetClass;
                            Object newRoot = ConstructorUtil.newInstance(targetClass);
                            setProperty(root, key, toBean((JSONObject) value, newRoot, jsonConfig), jsonConfig);
                        }else{
                            LOGGER.warn("Tried to assign property {}:{} to bean of class {}", key, type.getName(), rootClass.getName());
                        }
                    }
                }else{
                    if (type.isPrimitive()){
                        // assume assigned default value
                        LOGGER.warn("Tried to assign null value to {}:{}", key, type.getName());
                        setProperty(root, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
                    }else{
                        setProperty(root, key, null, jsonConfig);
                    }
                }
            }catch (Exception e){
                throw JSONExceptionUtil.build("Error while setting property=" + name + " type " + type, e);
            }
        }

        return root;
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
     * @param jsonConfig
     *            the json config
     * @throws Exception
     *             the exception
     */
    private static void setProperty(Object bean,String key,Object value,JsonConfig jsonConfig) throws Exception{
        PropertySetStrategy propertySetStrategy = PropertySetStrategy.DEFAULT;
        propertySetStrategy.setProperty(bean, key, value, jsonConfig);
    }
}
