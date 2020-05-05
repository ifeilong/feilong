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
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.lib.ezmorph.Morpher;
import com.feilong.lib.ezmorph.array.ObjectArrayMorpher;
import com.feilong.lib.ezmorph.bean.BeanMorpher;
import com.feilong.lib.ezmorph.object.IdentityObjectMorpher;
import com.feilong.lib.json.processors.JsonBeanProcessor;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.json.processors.JsonVerifier;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.util.CycleDetectionStrategy;
import com.feilong.lib.json.util.EnumMorpher;
import com.feilong.lib.json.util.JSONTokener;
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.json.util.PropertySetStrategy;

/**
 * A JSONObject is an unordered collection of name/value pairs. Its external
 * form is a string wrapped in curly braces with colons between the names and
 * values, and commas between the values and names. The internal form is an
 * object having <code>get</code> and <code>opt</code> methods for accessing
 * the values by name, and <code>put</code> methods for adding or replacing
 * values by name. The values can be any of these types: <code>Boolean</code>,
 * <code>JSONArray</code>, <code>JSONObject</code>, <code>Number</code>,
 * <code>String</code>, or the <code>JSONNull</code> object. A JSONObject
 * constructor can be used to convert an external form JSON text into an
 * internal form whose values can be retrieved with the <code>get</code> and
 * <code>opt</code> methods, or to convert values into a JSON text using the
 * <code>element</code> and <code>toString</code> methods. A
 * <code>get</code> method returns a value if one can be found, and throws an
 * exception if one cannot be found. An <code>opt</code> method returns a
 * default value instead of throwing an exception, and so is useful for
 * obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an
 * object, which you can cast or query for type. There are also typed
 * <code>get</code> and <code>opt</code> methods that do type checking and
 * type coercion for you.
 * <p>
 * The <code>put</code> methods adds values to an object. For example,
 *
 * <pre>
 * myString = new JSONObject().put("JSON", "Hello, World!").toString();
 * </pre>
 *
 * produces the string <code>{"JSON": "Hello, World"}</code>.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to
 * the JSON syntax rules. The constructors are more forgiving in the texts they
 * will accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 * before the closing brace.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a
 * quote or single quote, and if they do not contain leading or trailing spaces,
 * and if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , = ; #</code> and if they do not look like numbers and
 * if they are not the reserved words <code>true</code>, <code>false</code>,
 * or <code>null</code>.</li>
 * <li>Keys can be followed by <code>=</code> or <code>=></code> as well as
 * by <code>:</code>.</li>
 * <li>Values can be followed by <code>;</code> <small>(semicolon)</small>
 * as well as by <code>,</code> <small>(comma)</small>.</li>
 * <li>Numbers may have the <code>0-</code> <small>(octal)</small> or
 * <code>0x-</code> <small>(hex)</small> prefix.</li>
 * <li>Comments written in the slashshlash, slashstar, and hash conventions
 * will be ignored.</li>
 * </ul>
 *
 * @author JSON.org
 */
public final class JSONObject extends AbstractJSON implements JSON{

    /** The Constant serialVersionUID. */
    private static final long   serialVersionUID = -7895449812672706822L;

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(JSONObject.class);

    //---------------------------------------------------------------

    /**
     * Creates a JSONObject.<br>
     * Inspects the object type to call the correct JSONObject factory method.
     * Accepts JSON formatted strings, Maps, DynaBeans and JavaBeans.
     *
     * @param object
     *            the object
     * @return the JSON object
     * @throws JSONException
     *             if the object can not be converted to a proper
     *             JSONObject.
     */
    public static JSONObject fromObject(Object object){
        return fromObject(object, new JsonConfig());
    }

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
     * @throws JSONException
     *             if the object can not be converted to a proper
     *             JSONObject.
     */
    public static JSONObject fromObject(Object object,JsonConfig jsonConfig){
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
                    doWithMap(jsonConfig, classMap, bean, name, type, value, key);
                }else{
                    PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(bean, key);
                    if (propertyDescriptor != null && propertyDescriptor.getWriteMethod() == null){
                        LOGGER.debug("Property '{}' of {} has no write method. SKIPPED.", key, bean.getClass());
                        continue;
                    }

                    doWithBean(jsonConfig, beanClass, classMap, bean, name, type, value, key, propertyDescriptor);
                }
            }catch (JSONException jsone){
                throw jsone;
            }catch (Exception e){
                throw new JSONException("Error while setting property=" + name + " type " + type, e);
            }
        }
        return bean;
    }

    private static void doWithBean(
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
                        setProperty(bean, key, morphPropertyValue(key, value, type, targetType), jsonConfig);
                    }else{
                        setProperty(bean, key, value, jsonConfig);
                    }

                    LOGGER.warn("Tried to assign property {}:{} to bean of class {}", key, type.getName(), bean.getClass().getName());
                }else{
                    if (jsonConfig.isHandleJettisonSingleElementArray()){
                        JSONArray array = new JSONArray().element(value, jsonConfig);
                        Class newTargetClass = resolveClass(classMap, key, name, type);
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
                        Class newTargetClass = resolveClass(classMap, key, name, type);
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
    private static void doWithMap(JsonConfig jsonConfig,Map classMap,Object bean,String name,Class type,Object value,String key)
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
            Class targetClass = resolveClass(classMap, key, name, type);
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
                            Class targetClass = resolveClass(classMap, key, name, type);
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
                                Class newTargetClass = resolveClass(classMap, key, name, type);
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
                                    targetClass = resolveClass(classMap, key, name, type);
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
        if (!addInstance(bean)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(bean);
            }catch (JSONException jsone){
                removeInstance(bean);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(bean);
                throw new JSONException(e);
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
                removeInstance(bean);
            }catch (JSONException jsone){
                removeInstance(bean);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(bean);
                throw new JSONException(e);
            }
            return json;
        }

        JSONObject jsonObject = defaultBeanProcessing(bean, jsonConfig);
        removeInstance(bean);
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
        }catch (JSONException jsone){
            removeInstance(bean);
            throw jsone;
        }catch (Exception e){
            removeInstance(bean);
            throw new JSONException(e);
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

        if (!addInstance(bean)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(bean);
            }catch (JSONException jsone){
                removeInstance(bean);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(bean);
                throw new JSONException(e);
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
        }catch (JSONException jsone){
            removeInstance(bean);
            throw jsone;
        }catch (RuntimeException e){
            removeInstance(bean);
            throw new JSONException(e);
        }

        removeInstance(bean);
        return jsonObject;
    }

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

        if (!addInstance(object)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(object);
            }catch (JSONException jsone){
                removeInstance(object);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(object);
                throw new JSONException(e);
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

        removeInstance(object);
        return jsonObject;
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

    //---------------------------------------------------------------

    /**
     * From JSON tokener.
     *
     * @param tokener
     *            the tokener
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private static JSONObject _fromJSONTokener(JSONTokener tokener,JsonConfig jsonConfig){
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

    //---------------------------------------------------------------

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

        if (!addInstance(map)){
            try{
                return jsonConfig.getCycleDetectionStrategy().handleRepeatedReferenceAsObject(map);
            }catch (JSONException jsone){
                removeInstance(map);
                throw jsone;
            }catch (RuntimeException e){
                removeInstance(map);
                throw new JSONException(e);
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
                        jsonObject.accumulate(key, JSONNull.getInstance());
                    }else{
                        jsonObject.element(key, JSONNull.getInstance());
                    }
                }
            }
        }catch (JSONException jsone){
            removeInstance(map);
            throw jsone;
        }catch (RuntimeException e){
            removeInstance(map);
            throw new JSONException(e);
        }

        removeInstance(map);
        return jsonObject;
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
     * Resolve class.
     *
     * @param classMap
     *            the class map
     * @param key
     *            the key
     * @param name
     *            the name
     * @param type
     *            the type
     * @return the class
     */
    private static Class resolveClass(Map classMap,String key,String name,Class type){
        Class targetClass = TargetClassFinder.findTargetClass(key, classMap);
        if (targetClass == null){
            targetClass = TargetClassFinder.findTargetClass(name, classMap);
        }
        if (targetClass == null && type != null){
            if (List.class.equals(type)){
                targetClass = ArrayList.class;
            }else if (Map.class.equals(type)){
                targetClass = LinkedHashMap.class;
            }else if (Set.class.equals(type)){
                targetClass = LinkedHashSet.class;
            }else if (!type.isInterface() && !Object.class.equals(type)){
                targetClass = type;
            }
        }
        return targetClass;
    }

    /**
     * Morph property value.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param type
     *            the type
     * @param targetType
     *            the target type
     * @return the object
     */
    private static Object morphPropertyValue(String key,Object value,Class type,Class targetType){
        Morpher morpher = JSONUtils.getMorpherRegistry().getMorpherFor(targetType);
        if (IdentityObjectMorpher.getInstance().equals(morpher)){
            LOGGER.warn(
                            "Can't transform property '" + key + "' from " + type.getName() + " into " + targetType.getName()
                                            + ". Will register a default Morpher");
            if (Enum.class.isAssignableFrom(targetType)){
                JSONUtils.getMorpherRegistry().registerMorpher(new EnumMorpher(targetType));
            }else{
                JSONUtils.getMorpherRegistry().registerMorpher(new BeanMorpher(targetType, JSONUtils.getMorpherRegistry()));
            }
        }

        return JSONUtils.getMorpherRegistry().morph(targetType, value);
    }

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

    // ------------------------------------------------------

    /** identifies this object as null. */
    private boolean   nullObject;

    /**
     * The Map where the JSONObject's properties are kept.
     */
    private final Map properties;

    /**
     * Construct an empty JSONObject.
     */
    public JSONObject(){
        this.properties = new ListOrderedMap();
    }

    /**
     * Creates a JSONObject that is null.
     *
     * @param isNull
     *            the is null
     */
    public JSONObject(boolean isNull){
        this();
        this.nullObject = isNull;
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key
     *            A key string.
     * @param value
     *            An object to be accumulated under the key.
     * @return this.
     * @throws JSONException
     *             If the value is an invalid number or if the key is
     *             null.
     */
    public JSONObject accumulate(String key,Object value){
        return _accumulate(key, value, new JsonConfig());
    }

    /**
     * Accumulate values under a key. It is similar to the element method except
     * that if there is already an object stored under the key then a JSONArray
     * is stored under the key to hold all of the accumulated values. If there is
     * already a JSONArray, then the new value is appended to it. In contrast,
     * the replace method replaces the previous value.
     *
     * @param key
     *            A key string.
     * @param value
     *            An object to be accumulated under the key.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the value is an invalid number or if the key is
     *             null.
     */
    public JSONObject accumulate(String key,Object value,JsonConfig jsonConfig){
        return _accumulate(key, value, jsonConfig);
    }

    /**
     * Put a key/int pair in the JSONObject.
     *
     * @param key
     *            A key string.
     * @param value
     *            An int which is the value.
     * @return this.
     * @throws JSONException
     *             If the key is null.
     */
    public JSONObject element(String key,int value){
        verifyIsNull();
        return element(key, new Integer(value));
    }

    /**
     * Put a key/value pair in the JSONObject. If the value is null, then the key
     * will be removed from the JSONObject if it is present.<br>
     * If there is a previous value assigned to the key, it will call accumulate.
     *
     * @param key
     *            A key string.
     * @param value
     *            An object which is the value. It should be of one of these
     *            types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *            String, or the JSONNull object.
     * @return this.
     * @throws JSONException
     *             If the value is non-finite number or if the key is
     *             null.
     */
    public JSONObject element(String key,Object value){
        return element(key, value, new JsonConfig());
    }

    /**
     * Put a key/value pair in the JSONObject. If the value is null, then the key
     * will be removed from the JSONObject if it is present.<br>
     * If there is a previous value assigned to the key, it will call accumulate.
     *
     * @param key
     *            A key string.
     * @param value
     *            An object which is the value. It should be of one of these
     *            types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *            String, or the JSONNull object.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the value is non-finite number or if the key is
     *             null.
     */
    public JSONObject element(String key,Object value,JsonConfig jsonConfig){
        verifyIsNull();
        if (key == null){
            throw new JSONException("Null key.");
        }
        if (value != null){
            value = processValue(key, value, jsonConfig);
            _setInternal(key, value, jsonConfig);
        }else{
            remove(key);
        }
        return this;
    }

    /**
     * Get the value object associated with a key.
     *
     * @param key
     *            A key string.
     * @return The object associated with the key.
     * @throws JSONException
     *             if this.isNull() returns true.
     */
    public Object get(String key){
        verifyIsNull();
        return this.properties.get(key);
    }

    /**
     * Determine if the JSONObject contains a specific key.
     *
     * @param key
     *            A key string.
     * @return true if the key exists in the JSONObject.
     */
    public boolean has(String key){
        verifyIsNull();
        return this.properties.containsKey(key);
    }

    /**
     * Checks if is array.
     *
     * @return true, if is array
     */
    @Override
    public boolean isArray(){
        return false;
    }

    /**
     * Returs if this object is a null JSONObject.
     *
     * @return the identifies this object as null
     */
    public boolean isNullObject(){
        return nullObject;
    }

    /**
     * Get an enumeration of the keys of the JSONObject.
     *
     * @return An iterator of the keys.
     */
    public Iterator keys(){
        verifyIsNull();
        Set keySet = Collections.unmodifiableSet(properties.keySet());
        return keySet.iterator();
    }

    /**
     * Produce a JSONArray containing the names of the elements of this
     * JSONObject.
     *
     * @param jsonConfig
     *            the json config
     * @return A JSONArray containing the key strings, or null if the JSONObject
     *         is empty.
     */
    public JSONArray names(JsonConfig jsonConfig){
        verifyIsNull();
        JSONArray ja = new JSONArray();
        Iterator keys = keys();
        while (keys.hasNext()){
            ja.element(keys.next(), jsonConfig);
        }
        return ja;
    }

    /**
     * Remove a name and its value, if present.
     *
     * @param key
     *            The name to be removed.
     * @return The value that was associated with the name, or null if there was
     *         no value.
     */
    public Object remove(String key){
        verifyIsNull();
        return this.properties.remove(key);
    }

    /**
     * Get the number of keys stored in the JSONObject.
     *
     * @return The number of keys in the JSONObject.
     */
    @Override
    public int size(){
        return this.properties.size();
    }

    /**
     * Make a JSON text of this JSONObject. For compactness, no whitespace is
     * added. If this would not result in a syntactically correct JSON text, then
     * null will be returned instead.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
     * @return a printable, displayable, portable, transmittable representation
     *         of the object, beginning with <code>{</code>&nbsp;<small>(left
     *         brace)</small> and ending with <code>}</code>&nbsp;<small>(right
     *         brace)</small>.
     */
    @Override
    public String toString(){
        if (isNullObject()){
            return JSONNull.getInstance().toString();
        }
        return ToStringUtil.toString(this.properties);
    }

    //---------------------------------------------------------------

    /**
     * Make a prettyprinted JSON text of this JSONObject.
     * <p>
     * Warning: This method assumes that the data structure is acyclical.
     *
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
    @Override
    public String toString(int indentFactor,int indent){
        if (isNullObject()){
            return JSONNull.getInstance().toString();
        }
        //---------------------------------------------------------------
        int n = size();
        if (n == 0){
            return "{}";
        }
        if (indentFactor == 0){
            return toString();
        }
        return ToStringUtil.toString(properties, indentFactor, indent);
    }

    /**
     * Accumulate.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @param jsonConfig
     *            the json config
     * @return the JSON object
     */
    private JSONObject _accumulate(String key,Object value,JsonConfig jsonConfig){
        if (isNullObject()){
            throw new JSONException("Can't accumulate on null object");
        }

        if (!has(key)){
            setInternal(key, value, jsonConfig);
        }else{
            Object o = get(key);
            if (o instanceof JSONArray){
                ((JSONArray) o).element(value, jsonConfig);
            }else{
                setInternal(key, new JSONArray().element(o).element(value, jsonConfig), jsonConfig);
            }
        }

        return this;
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
    @Override
    protected Object _processValue(Object value,JsonConfig jsonConfig){
        if (value instanceof JSONTokener){
            return _fromJSONTokener((JSONTokener) value, jsonConfig);
        }else if (value != null && Enum.class.isAssignableFrom(value.getClass())){
            return ((Enum) value).name();
        }
        return super._processValue(value, jsonConfig);
    }

    /**
     * Put a key/value pair in the JSONObject.
     *
     * @param key
     *            A key string.
     * @param value
     *            An object which is the value. It should be of one of these
     *            types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *            String, or the JSONNull object.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the value is non-finite number or if the key is
     *             null.
     */
    private JSONObject _setInternal(String key,Object value,JsonConfig jsonConfig){
        verifyIsNull();
        if (key == null){
            throw new JSONException("Null key.");
        }

        //---------------------------------------------------------------
        if (JSONUtils.isString(value) && JSONUtils.mayBeJSON(String.valueOf(value))){
            this.properties.put(key, value);
        }else{
            if (CycleDetectionStrategy.IGNORE_PROPERTY_OBJ == value || CycleDetectionStrategy.IGNORE_PROPERTY_ARR == value){
                // do nothing
            }else{
                this.properties.put(key, value);
            }
        }
        return this;
    }

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
    private Object processValue(String key,Object value,JsonConfig jsonConfig){
        if (value != null){
            JsonValueProcessor processor = jsonConfig.findJsonValueProcessor(value.getClass(), key);
            if (processor != null){
                value = processor.processObjectValue(null, value, jsonConfig);
                if (!JsonVerifier.isValidJsonValue(value)){
                    throw new JSONException("Value is not a valid JSON value. " + value);
                }
            }
        }
        return _processValue(value, jsonConfig);
    }

    //---------------------------------------------------------------

    /**
     * Put a key/value pair in the JSONObject.
     *
     * @param key
     *            A key string.
     * @param value
     *            An object which is the value. It should be of one of these
     *            types: Boolean, Double, Integer, JSONArray, JSONObject, Long,
     *            String, or the JSONNull object.
     * @param jsonConfig
     *            the json config
     * @return this.
     * @throws JSONException
     *             If the value is non-finite number or if the key is
     *             null.
     */
    private JSONObject setInternal(String key,Object value,JsonConfig jsonConfig){
        return _setInternal(key, processValue(key, value, jsonConfig), jsonConfig);
    }

    /**
     * Checks if this object is a "null" object.
     */
    private void verifyIsNull(){
        if (isNullObject()){
            throw new JSONException("null object");
        }
    }

}
