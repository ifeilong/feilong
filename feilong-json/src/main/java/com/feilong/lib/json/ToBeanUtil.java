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
import java.util.Collection;
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
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.json.util.PropertySetStrategy;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class ToBeanUtil{

    private static final Logger LOGGER = LoggerFactory.getLogger(ToBeanUtil.class);

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
            Class type = (Class) props.get(name);
            Object value = jsonObject.get(name);
            try{
                if (!JSONUtils.isNull(value)){
                    if (value instanceof JSONArray){
                        dynaBean.set(key, JSONArray.toCollection((JSONArray) value));
                    }else if (String.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type) || JSONUtils.isNumber(type)
                                    || Character.class.isAssignableFrom(type) || JSONFunction.class.isAssignableFrom(type)){
                        dynaBean.set(key, value);
                    }else{
                        dynaBean.set(key, toBean((JSONObject) value));
                    }
                }else{
                    if (type.isPrimitive()){
                        // assume assigned default value
                        LOGGER.warn("Tried to assign null value to " + key + ":" + type.getName());
                        dynaBean.set(key, JSONUtils.getMorpherRegistry().morph(type, null));
                    }else{
                        dynaBean.set(key, null);
                    }
                }
            }catch (JSONException jsone){
                throw jsone;
            }catch (Exception e){
                throw new JSONException("Error while setting property=" + name + " type" + type, e);
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
        Class beanClass = jsonConfig.getRootClass();
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
                bean = new HashMap();
            }else{
                bean = ConstructorUtil.newInstance(beanClass);
            }
        }catch (JSONException jsone){
            throw jsone;
        }catch (Exception e){
            throw new JSONException(e);
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
            String key = Map.class.isAssignableFrom(beanClass) && jsonConfig.isSkipJavaIdentifierTransformationInMapKeys()//
                            ? name
                            : JSONUtils.convertToJavaIdentifier(name, jsonConfig);

            //---------------------------------------------------------------
            PropertyNameProcessor propertyNameProcessor = jsonConfig.findJavaPropertyNameProcessor(beanClass);
            if (propertyNameProcessor != null){
                key = propertyNameProcessor.processPropertyName(beanClass, key);
            }

            //---------------------------------------------------------------
            Class type = (Class) properties.get(name);
            try{
                if (Map.class.isAssignableFrom(beanClass)){
                    toBeanDoWithMap(jsonConfig, classMap, bean, name, type, value, key);
                }else{
                    PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, key);
                    if (propertyDescriptor != null && propertyDescriptor.getWriteMethod() == null){
                        LOGGER.debug("Property '{}' of {} has no write method. SKIPPED.", key, bean.getClass());
                        continue;
                    }

                    toBeanDoWithBean(jsonConfig, beanClass, classMap, bean, name, type, value, key, propertyDescriptor);
                }
            }catch (JSONException jsone){
                throw jsone;
            }catch (Exception e){
                throw new JSONException("Error while setting property=" + name + " type " + type, e);
            }
        }
        return bean;
    }

    private static void toBeanDoWithBean(
                    JsonConfig jsonConfig,
                    Class beanClass,
                    Map classMap,
                    Object bean,
                    String name,
                    Class type,
                    Object value,
                    String key,
                    PropertyDescriptor propertyDescriptor) throws Exception{
        //---------------------------------------------------------------

        if (propertyDescriptor != null){
            Class<?> propertyType = propertyDescriptor.getPropertyType();
            Class targetType = propertyType;
            if (!JSONUtils.isNull(value)){
                if (value instanceof JSONArray){
                    if (List.class.isAssignableFrom(propertyType)){
                        setProperty(
                                        bean,
                                        key,
                                        PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, propertyType),
                                        jsonConfig);
                    }else if (Set.class.isAssignableFrom(propertyType)){
                        setProperty(
                                        bean,
                                        key,
                                        PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, propertyType),
                                        jsonConfig);
                    }else{
                        setProperty(bean, key, PropertyValueConvertUtil.toArray(key, value, targetType, jsonConfig, classMap), jsonConfig);
                    }
                }else if (String.class.isAssignableFrom(type) || //
                                JSONUtils.isBoolean(type) || //
                                JSONUtils.isNumber(type) || //
                                JSONUtils.isString(type) || //
                                JSONFunction.class.isAssignableFrom(type)){
                    if (jsonConfig.isHandleJettisonEmptyElement() && "".equals(value)){
                        setProperty(bean, key, null, jsonConfig);
                    }else if (!targetType.isInstance(value)){
                        setProperty(bean, key, PropertyValueMorpher.morphPropertyValue(key, value, type, targetType), jsonConfig);
                    }else{
                        setProperty(bean, key, value, jsonConfig);
                    }

                    LOGGER.warn("Tried to assign property {}:{} to bean of class {}", key, type.getName(), bean.getClass().getName());
                }else{
                    if (jsonConfig.isHandleJettisonSingleElementArray()){
                        JSONArray array = new JSONArray().element(value, jsonConfig);
                        Class newTargetClass = ClassResolver.resolveClass(classMap, key, name, type);
                        JsonConfig jsc = jsonConfig.copy();
                        jsc.setRootClass(newTargetClass);
                        jsc.setClassMap(classMap);
                        if (targetType.isArray()){
                            setProperty(bean, key, JSONArray.toArray(array, jsc), jsonConfig);
                        }else if (JSONArray.class.isAssignableFrom(targetType)){
                            setProperty(bean, key, array, jsonConfig);
                        }else if (List.class.isAssignableFrom(targetType) || Set.class.isAssignableFrom(targetType)){
                            jsc.setCollectionType(targetType);
                            setProperty(bean, key, JSONArray.toCollection(array, jsc), jsonConfig);
                        }else{
                            setProperty(bean, key, toBean((JSONObject) value, jsc), jsonConfig);
                        }
                    }else{
                        if (targetType == Object.class || targetType.isInterface()){
                            Class targetTypeCopy = targetType;
                            targetType = TargetClassFinder.findTargetClass(key, classMap);
                            targetType = targetType == null ? TargetClassFinder.findTargetClass(name, classMap) : targetType;
                            targetType = targetType == null && targetTypeCopy.isInterface() ? targetTypeCopy : targetType;
                        }
                        JsonConfig jsc = jsonConfig.copy();
                        jsc.setRootClass(targetType);
                        jsc.setClassMap(classMap);
                        setProperty(bean, key, toBean((JSONObject) value, jsc), jsonConfig);
                    }
                }
            }else{
                if (type.isPrimitive()){
                    // assume assigned default value
                    LOGGER.warn("Tried to assign null value to " + key + ":" + type.getName());
                    setProperty(bean, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
                }else{
                    setProperty(bean, key, null, jsonConfig);
                }
            }
        }else{
            if (!JSONUtils.isNull(value)){
                if (value instanceof JSONArray){
                    setProperty(
                                    bean,
                                    key,
                                    PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, List.class),
                                    jsonConfig);
                }else if (String.class.isAssignableFrom(type) || JSONUtils.isBoolean(type) || JSONUtils.isNumber(type)
                                || JSONUtils.isString(type) || JSONFunction.class.isAssignableFrom(type)){
                    if (beanClass == null || bean instanceof Map || jsonConfig.getPropertySetStrategy() != null){
                        setProperty(bean, key, value, jsonConfig);
                    }else{
                        LOGGER.warn(
                                        "Tried to assign property " + key + ":" + type.getName() + " to bean of class "
                                                        + bean.getClass().getName());
                    }
                }else{
                    if (jsonConfig.isHandleJettisonSingleElementArray()){
                        Class newTargetClass = ClassResolver.resolveClass(classMap, key, name, type);
                        JsonConfig jsc = jsonConfig.copy();
                        jsc.setRootClass(newTargetClass);
                        jsc.setClassMap(classMap);
                        setProperty(bean, key, toBean((JSONObject) value, jsc), jsonConfig);
                    }else{
                        setProperty(bean, key, value, jsonConfig);
                    }
                }
            }else{
                if (type.isPrimitive()){
                    // assume assigned default value
                    LOGGER.warn("Tried to assign null value to " + key + ":" + type.getName());
                    setProperty(bean, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
                }else{
                    setProperty(bean, key, null, jsonConfig);
                }
            }
        }
    }

    /**
     * @param jsonConfig
     * @param classMap
     * @param bean
     * @param name
     * @param type
     * @param value
     * @param key
     * @throws Exception
     * @since 3.0.0
     */
    private static void toBeanDoWithMap(JsonConfig jsonConfig,Map classMap,Object bean,String name,Class type,Object value,String key)
                    throws Exception{
        // no type info available for conversion
        if (JSONUtils.isNull(value)){
            setProperty(bean, key, value, jsonConfig);
        }else if (value instanceof JSONArray){
            setProperty(bean, key, PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, classMap, List.class), jsonConfig);
        }else if (String.class.isAssignableFrom(type) || //
                        JSONUtils.isBoolean(type) || //
                        JSONUtils.isNumber(type) || //
                        JSONUtils.isString(type) || //
                        JSONFunction.class.isAssignableFrom(type)){
            if (jsonConfig.isHandleJettisonEmptyElement() && "".equals(value)){
                setProperty(bean, key, null, jsonConfig);
            }else{
                setProperty(bean, key, value, jsonConfig);
            }
        }else{
            Class targetClass = ClassResolver.resolveClass(classMap, key, name, type);
            JsonConfig newJsonConfig = jsonConfig.copy();
            newJsonConfig.setRootClass(targetClass);
            newJsonConfig.setClassMap(classMap);
            if (targetClass != null){
                setProperty(bean, key, toBean((JSONObject) value, newJsonConfig), jsonConfig);
            }else{
                setProperty(bean, key, toBean((JSONObject) value), jsonConfig);
            }
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

        Class rootClass = root.getClass();
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
            Class type = (Class) props.get(name);
            Object value = jsonObject.get(name);
            if (javaPropertyFilter != null && javaPropertyFilter.apply(root, name, value)){
                continue;
            }
            String key = JSONUtils.convertToJavaIdentifier(name, jsonConfig);
            try{
                PropertyDescriptor pd = PropertyUtils.getPropertyDescriptor(root, key);
                if (pd != null && pd.getWriteMethod() == null){
                    LOGGER.info("Property '" + key + "' of " + root.getClass() + " has no write method. SKIPPED.");
                    continue;
                }

                if (!JSONUtils.isNull(value)){
                    if (value instanceof JSONArray){
                        if (pd == null || List.class.isAssignableFrom(pd.getPropertyType())){
                            Class targetClass = ClassResolver.resolveClass(classMap, key, name, type);
                            Object newRoot = ConstructorUtil.newInstance(targetClass);
                            List list = JSONArray.toList((JSONArray) value, newRoot, jsonConfig);
                            setProperty(root, key, list, jsonConfig);
                        }else{
                            Class innerType = JSONUtils.getInnerComponentType(pd.getPropertyType());
                            Class targetInnerType = TargetClassFinder.findTargetClass(key, classMap);
                            if (innerType.equals(Object.class) && targetInnerType != null && !targetInnerType.equals(Object.class)){
                                innerType = targetInnerType;
                            }
                            Object newRoot = ConstructorUtil.newInstance(innerType);
                            Object array = JSONArray.toArray((JSONArray) value, newRoot, jsonConfig);
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
                            if (jsonConfig.isHandleJettisonEmptyElement() && "".equals(value)){
                                setProperty(root, key, null, jsonConfig);
                            }else if (!pd.getPropertyType().isInstance(value)){
                                Morpher morpher = JSONUtils.getMorpherRegistry().getMorpherFor(pd.getPropertyType());
                                if (IdentityObjectMorpher.getInstance().equals(morpher)){
                                    LOGGER.warn(
                                                    "Can't transform property '" + key + "' from " + type.getName() + " into "
                                                                    + pd.getPropertyType().getName()
                                                                    + ". Will register a default BeanMorpher");
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
                                            "Tried to assign property " + key + ":" + type.getName() + " to bean of class "
                                                            + root.getClass().getName());
                        }
                    }else{
                        if (pd != null){
                            Class targetClass = pd.getPropertyType();
                            if (jsonConfig.isHandleJettisonSingleElementArray()){
                                JSONArray array = new JSONArray().element(value, jsonConfig);
                                Class newTargetClass = ClassResolver.resolveClass(classMap, key, name, type);
                                Object newRoot = ConstructorUtil.newInstance(newTargetClass);
                                if (targetClass.isArray()){
                                    setProperty(root, key, JSONArray.toArray(array, newRoot, jsonConfig), jsonConfig);
                                }else if (Collection.class.isAssignableFrom(targetClass)){
                                    setProperty(root, key, JSONArray.toList(array, newRoot, jsonConfig), jsonConfig);
                                }else if (JSONArray.class.isAssignableFrom(targetClass)){
                                    setProperty(root, key, array, jsonConfig);
                                }else{
                                    setProperty(root, key, toBean((JSONObject) value, newRoot, jsonConfig), jsonConfig);
                                }
                            }else{
                                if (targetClass == Object.class){
                                    targetClass = ClassResolver.resolveClass(classMap, key, name, type);
                                    if (targetClass == null){
                                        targetClass = Object.class;
                                    }
                                }
                                Object newRoot = ConstructorUtil.newInstance(targetClass);
                                setProperty(root, key, toBean((JSONObject) value, newRoot, jsonConfig), jsonConfig);
                            }
                        }else if (root instanceof Map){
                            Class targetClass = TargetClassFinder.findTargetClass(key, classMap);
                            targetClass = targetClass == null ? TargetClassFinder.findTargetClass(name, classMap) : targetClass;
                            Object newRoot = ConstructorUtil.newInstance(targetClass);
                            setProperty(root, key, toBean((JSONObject) value, newRoot, jsonConfig), jsonConfig);
                        }else{
                            LOGGER.warn(
                                            "Tried to assign property " + key + ":" + type.getName() + " to bean of class "
                                                            + rootClass.getName());
                        }
                    }
                }else{
                    if (type.isPrimitive()){
                        // assume assigned default value
                        LOGGER.warn("Tried to assign null value to " + key + ":" + type.getName());
                        setProperty(root, key, JSONUtils.getMorpherRegistry().morph(type, null), jsonConfig);
                    }else{
                        setProperty(root, key, null, jsonConfig);
                    }
                }
            }catch (JSONException jsone){
                throw jsone;
            }catch (Exception e){
                throw new JSONException("Error while setting property=" + name + " type " + type, e);
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
        PropertySetStrategy propertySetStrategy = jsonConfig.getPropertySetStrategy() != null //
                        ? jsonConfig.getPropertySetStrategy()
                        : PropertySetStrategy.DEFAULT;
        propertySetStrategy.setProperty(bean, key, value, jsonConfig);
    }
}
