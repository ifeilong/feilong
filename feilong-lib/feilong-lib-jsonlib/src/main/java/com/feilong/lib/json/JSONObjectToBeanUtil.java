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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.defaultEmptyMapIfNull;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;
import static com.feilong.lib.json.util.PropertySetStrategy.setProperty;
import static java.util.Collections.emptyMap;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.lib.json.processors.PropertyNameProcessor;
import com.feilong.lib.json.processors.PropertyNameProcessorMatcher;
import com.feilong.lib.json.util.ClassResolver;
import com.feilong.lib.json.util.JSONUtils;
import com.feilong.lib.json.util.KeyUpdater;
import com.feilong.lib.json.util.PropertyFilter;
import com.feilong.lib.lang3.tuple.Triple;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * JSONObject 转成 Bean的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONObjectToBeanUtil{

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
        Object rootBean = BeanNewer.newBean(rootClass);

        //---------------------------------------------------------------
        //所有经过filter 以及加工key 之后的数据
        //Triple.of(name, key, value)
        List<Triple<String, String, Object>> list = buildTripleList(rootBean, jsonObject, jsonConfig);
        if (isNullOrEmpty(list)){
            return rootBean;
        }

        //---------------------------------------------------------------
        Map<String, Class<?>> jsonKeyAndClassMap = JSONUtils.getKeyAndTypeMap(jsonObject);
        if (!Map.class.isAssignableFrom(rootClass)){
            return doWithBean(rootBean, rootClass, list, jsonKeyAndClassMap, jsonConfig);
        }
        return doMap(rootBean, list, jsonKeyAndClassMap, jsonConfig);
    }

    private static Object doWithBean(
                    Object rootBean,
                    Class<?> rootClass,
                    List<Triple<String, String, Object>> list,
                    Map<String, Class<?>> jsonKeyAndClassMap,
                    JsonConfig jsonConfig){
        //Triple.of(name, key, value)
        Map<Triple<String, String, Object>, PropertyDescriptor> map = build(rootClass, list);
        if (isNullOrEmpty(map)){
            return rootBean;
        }

        //---------------------------------------------------------------
        for (Map.Entry<Triple<String, String, Object>, PropertyDescriptor> entry : map.entrySet()){
            Triple<String, String, Object> triple = entry.getKey();

            String key = triple.getMiddle();
            PropertyDescriptor propertyDescriptor = entry.getValue();
            if (propertyDescriptor.getWriteMethod() == null){
                log.debug("{} property '{}' has no write method. SKIPPED.", rootClass.getCanonicalName(), key);
                continue;
            }

            String name = triple.getLeft();
            Class<?> jsonValueType = jsonKeyAndClassMap.get(name);
            try{
                toBeanUsePropertyDescriptor(rootBean, triple, jsonValueType, jsonConfig, propertyDescriptor);
            }catch (Exception e){
                throw new JSONException("Error while setting property=[" + name + "] type: " + jsonValueType, e);
            }
        }
        return rootBean;
    }

    private static Object doMap(
                    Object rootBean,
                    List<Triple<String, String, Object>> list,
                    Map<String, Class<?>> jsonKeyAndClassMap,
                    JsonConfig jsonConfig){
        for (Triple<String, String, Object> triple : list){
            String name = triple.getLeft();
            String key = triple.getMiddle();
            Object value = triple.getRight();

            //---------------------------------------------------------------
            Class<?> jsonValueType = jsonKeyAndClassMap.get(name);
            toBeanDoWithMap(rootBean, name, jsonValueType, value, key, jsonConfig);
        }
        return rootBean;
    }

    /**
     * 查找json 以及 属性的对应关系
     */
    private static Map<Triple<String, String, Object>, PropertyDescriptor> build(
                    Class<?> rootClass,
                    List<Triple<String, String, Object>> list){
        PropertyDescriptor[] propertyDescriptors = PropertyUtil.getPropertyDescriptors(rootClass);
        if (isNullOrEmpty(propertyDescriptors)){
            return emptyMap();
        }

        //---------------------------------------------------------------
        Map<Triple<String, String, Object>, PropertyDescriptor> map = newHashMap();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors){
            String propertyName = propertyDescriptor.getName();

            //Triple.of(name, key, value)
            for (Triple<String, String, Object> triple : list){
                String key = triple.getMiddle();
                if (key.equals(propertyName)){
                    map.put(triple, propertyDescriptor);
                    break;
                }
            }
        }
        return map;
    }

    private static void toBeanUsePropertyDescriptor(
                    Object bean,
                    Triple<String, String, Object> triple,
                    Class<?> jsonValueType,

                    JsonConfig jsonConfig,
                    PropertyDescriptor propertyDescriptor){
        String key = triple.getMiddle();
        Object value = triple.getRight();

        if (JSONUtils.isNull(value)){
            setNull(bean, jsonValueType, key);
            return;
        }

        Class<?> beanPropertyType = propertyDescriptor.getPropertyType();
        //---------------------------------------------------------------
        if (isCommonType(jsonValueType)){
            if (beanPropertyType.isInstance(value)){
                setProperty(bean, key, value);
                return;
            }
            setProperty(bean, key, PropertyValueMorpher.morph(key, value, jsonValueType, beanPropertyType));
            return;
        }

        Map<String, Class<?>> configClassMap = defaultEmptyMapIfNull(jsonConfig.getClassMap());
        String name = triple.getLeft();
        //---------------------------------------------------------------
        if (value instanceof JSONArray){
            if (List.class.isAssignableFrom(beanPropertyType) || Set.class.isAssignableFrom(beanPropertyType)){
                setProperty(
                                bean,
                                key,
                                PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, configClassMap, beanPropertyType));
            }else{
                setProperty(bean, key, PropertyValueConvertUtil.toArray(key, value, beanPropertyType, jsonConfig, configClassMap));
            }
            return;
        }

        //---------------------------------------------------------------
        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(ClassResolver.resolve(key, name, configClassMap, beanPropertyType));
        jsonConfigCopy.setClassMap(configClassMap);
        setProperty(bean, key, toBean((JSONObject) value, jsonConfigCopy));
        return;

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
            log.warn("Tried to assign null value to {}:{}", key, type.getName());
            setProperty(bean, key, JSONUtils.getMorpherRegistry().morph(type, null));
        }else{
            setProperty(bean, key, null);
        }
    }

    private static void toBeanDoWithMap(Object bean,String name,Class<?> type,Object value,String key,JsonConfig jsonConfig){
        Map<String, Class<?>> configClassMap = defaultEmptyMapIfNull(jsonConfig.getClassMap());

        // no type info available for conversion
        if (JSONUtils.isNull(value)){
            setProperty(bean, key, value);
            return;
        }

        //---------------------------------------------------------------
        if (value instanceof JSONArray){
            setProperty(bean, key, PropertyValueConvertUtil.toCollection(key, value, jsonConfig, name, configClassMap, List.class));
            return;
        }
        if (isCommonType(type)){
            setProperty(bean, key, value);
            return;
        }

        //---------------------------------------------------------------
        Class<?> targetClass = ClassResolver.resolve(key, name, type, configClassMap);

        JsonConfig jsonConfigCopy = jsonConfig.copy();
        jsonConfigCopy.setRootClass(targetClass);
        jsonConfigCopy.setClassMap(configClassMap);
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
        Map<String, Class<?>> props = JSONUtils.getKeyAndTypeMap(jsonObject);
        DynaBean dynaBean = JSONUtils.newDynaBean(jsonObject, jsonConfig);

        List<String> names = jsonObject.names(jsonConfig);
        for (String name : names){
            String key = jsonConfig.getJavaIdentifierTransformer().transformToJavaIdentifier(name);
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
                        log.warn("Tried to assign null value to {}:{}", key, type.getName());
                        dynaBean.set(key, JSONUtils.getMorpherRegistry().morph(type, null));
                    }else{
                        dynaBean.set(key, null);
                    }
                }
            }catch (Exception e){
                throw new JSONException("Error while setting property=" + name + " type" + type, e);
            }
        }

        return dynaBean;
    }

    //Triple.of(name, key, value)
    private static List<Triple<String, String, Object>> buildTripleList(Object rootBean,JSONObject jsonObject,JsonConfig jsonConfig){
        List<String> names = jsonObject.names(jsonConfig);

        Class<?> rootClass = jsonConfig.getRootClass();

        PropertyFilter javaPropertyFilter = jsonConfig.getJavaPropertyFilter();
        PropertyNameProcessor propertyNameProcessor = findJavaPropertyNameProcessor(rootClass, jsonConfig);

        List<Triple<String, String, Object>> list = newArrayList();
        for (String name : names){
            Object value = jsonObject.get(name);
            if (javaPropertyFilter != null && javaPropertyFilter.apply(rootBean, name, value)){
                continue;
            }

            //---------------------------------------------------------------
            String key = jsonConfig.getJavaIdentifierTransformer().transformToJavaIdentifier(name);
            key = KeyUpdater.update(rootClass, key, propertyNameProcessor);

            list.add(Triple.of(name, key, value));
        }
        return list;
    }

    private static boolean isCommonType(Class<?> type){
        return String.class.isAssignableFrom(type) || //
                        JSONUtils.isBoolean(type) || //
                        JSONUtils.isNumber(type) || //
                        JSONUtils.isString(type);
    }

    /**
     * Finds a PropertyNameProcessor registered to the target class.<br>
     * Returns null if none is registered.<br>
     * [JSON -&gt; Java]
     *
     * @param beanClass
     *            the bean class
     * @param jsonConfig
     * @return the property name processor
     */
    private static PropertyNameProcessor findJavaPropertyNameProcessor(Class<?> beanClass,JsonConfig jsonConfig){
        Map<Class<?>, PropertyNameProcessor> javaPropertyNameProcessorMap = jsonConfig.getJavaPropertyNameProcessorMap();
        return javaPropertyNameProcessorMap.isEmpty() ? null
                        : javaPropertyNameProcessorMap
                                        .get(PropertyNameProcessorMatcher.getMatch(beanClass, javaPropertyNameProcessorMap.keySet()));
    }

}
