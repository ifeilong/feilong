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
package com.feilong.json.builder;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.equalsAny;
import static com.feilong.core.util.CollectionsUtil.addAllIgnoreNull;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.feilong.core.Validate;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.ClassUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.json.SensitiveWords;
import com.feilong.json.processor.SensitiveWordsJsonValueProcessor;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.lib.lang3.reflect.FieldUtils;
import com.feilong.lib.lang3.reflect.MethodUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 专门用来构造 <code>SensitiveWordsPropertyNameAndJsonValueProcessorMap</code> 的构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.5
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SensitiveWordsPropertyNameAndJsonValueProcessorMapBuilder{

    /** The cache. */
    private static Map<Class<?>, Map<String, JsonValueProcessor>> cache = newHashMap();

    //---------------------------------------------------------------

    /**
     * Builds the sensitive words property name and json value processor map.
     *
     * @param javaBean
     *            the java bean
     * @return the map
     * @since 1.10.3
     */
    static Map<String, JsonValueProcessor> build(Object javaBean){
        Validate.notNull(javaBean, "javaBean can't be null!");

        //---------------------------------------------------------------
        if (ClassUtil.isInstance(javaBean, Map.class)){
            return parseMap(javaBean);
        }

        if (ClassUtil.isInstance(javaBean, Iterable.class)){
            return parseList(javaBean);
        }

        return parseBean(javaBean);
    }

    //---------------------------------------------------------------

    /**
     * Parses the list.
     *
     * @param inputMap
     *            the input map
     * @return the map
     * @since 1.13.0
     */
    private static Map<String, JsonValueProcessor> parseList(Object inputMap){
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();

        //---------------------------------------------------------------
        Iterable<?> list = (Iterable<?>) inputMap;
        for (Object bean : list){
            if (null != bean){
                MapUtil.putAllIfNotNull(propertyNameAndJsonValueProcessorMap, parseBean(bean));
            }
        }
        //---------------------------------------------------------------
        return propertyNameAndJsonValueProcessorMap;
    }
    //---------------------------------------------------------------

    /**
     * Parses the map.
     *
     * @param inputMap
     *            the input map
     * @return the map
     * @since 1.13.0
     */
    private static Map<String, JsonValueProcessor> parseMap(Object inputMap){
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();

        //---------------------------------------------------------------
        Map<?, ?> map = (Map<?, ?>) inputMap;
        for (Map.Entry<?, ?> entry : map.entrySet()){
            Object bean = entry.getValue();
            if (null != bean){
                MapUtil.putAllIfNotNull(propertyNameAndJsonValueProcessorMap, parseBean(bean));
            }
        }
        //---------------------------------------------------------------
        return propertyNameAndJsonValueProcessorMap;
    }

    //---------------------------------------------------------------

    /**
     * Parses the bean.
     *
     * @param javaBean
     *            the java bean
     * @return the map
     * @since 1.13.0
     */
    private static Map<String, JsonValueProcessor> parseBean(Object javaBean){
        Class<?> klass = javaBean.getClass();
        if (cache.containsKey(klass)){
            return cache.get(klass);
        }

        //---------------------------------------------------------------
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = buildObject(javaBean);
        cache.put(klass, propertyNameAndJsonValueProcessorMap);
        return propertyNameAndJsonValueProcessorMap;
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param klass
     *            the klass
     * @return the map
     * @since 1.11.5
     */
    private static Map<String, JsonValueProcessor> buildObject(Object javaBean){
        Class<?> klass = javaBean.getClass();
        if (equalsAny(klass, DontFindAnnotaionHelper.BASE_CLASS_ARRAY)){
            return emptyMap();
        }
        if (equalsAny(klass, DontFindAnnotaionHelper.ARRAY_CLASS_ARRAY)){
            return emptyMap();
        }
        if (ClassUtil.isInstanceAnyClass(javaBean, DontFindAnnotaionHelper.COMMON_CLASS_ARRAY)){
            return emptyMap();
        }

        List<String> list = getWithAnnotationName(klass, SensitiveWords.class);
        if (isNullOrEmpty(list)){
            return emptyMap();
        }
        //---------------------------------------------------------------------------------------------------------
        //敏感字段
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();
        for (String propertyName : list){
            propertyNameAndJsonValueProcessorMap.put(propertyName, SensitiveWordsJsonValueProcessor.INSTANCE);
        }

        return propertyNameAndJsonValueProcessorMap;
    }

    //---------------------------------------------------------------

    /**
     * Gets the with annotation name.
     *
     * @param <A>
     *            the generic type
     * @param klass
     *            the klass
     * @param annotationCls
     *            the annotation cls
     * @return the with annotation name
     * @since 1.11.5
     */
    private static <A extends Annotation> List<String> getWithAnnotationName(Class<?> klass,Class<A> annotationCls){
        List<String> list = newArrayList();
        addAllIgnoreNull(list, getFiledNamesWithAnnotation(klass, annotationCls));
        addAllIgnoreNull(list, getPropertyNamesWithAnnotation(klass, annotationCls));
        return list;
    }

    //---------------------------------------------------------------
    /**
     * Gets the filed names with annotation.
     *
     * @param <A>
     *            the generic type
     * @param klass
     *            the klass
     * @param annotationCls
     *            the annotation cls
     * @return the filed names with annotation
     * @since 1.11.5
     */
    private static <A extends Annotation> List<String> getFiledNamesWithAnnotation(Class<?> klass,Class<A> annotationCls){
        List<String> list = newArrayList();
        List<Field> fieldsList = FieldUtils.getFieldsListWithAnnotation(klass, annotationCls);
        for (Field field : fieldsList){
            list.add(field.getName());
        }
        return list;
    }

    //---------------------------------------------------------------

    /**
     * Gets the property names with annotation.
     *
     * @param <A>
     *            the generic type
     * @param klass
     *            the klass
     * @param annotationCls
     *            the annotation cls
     * @return 如果 <code>klass PropertyDescriptors</code> 是null或者empty,返回 {@link Collections#emptyList()}<br>
     * @since 1.11.5
     */
    private static <A extends Annotation> List<String> getPropertyNamesWithAnnotation(Class<?> klass,Class<A> annotationCls){
        PropertyDescriptor[] propertyDescriptors = PropertyUtil.getPropertyDescriptors(klass);
        if (isNullOrEmpty(propertyDescriptors)){
            return emptyList();
        }

        //---------------------------------------------------------------
        List<String> list = newArrayList();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors){
            Method readMethod = propertyDescriptor.getReadMethod();

            //since 1.12.2
            if (null == readMethod){
                log.warn(
                                "class:[{}],property:[{}],has no ReadMethod!!SKIPPED",
                                klass.getCanonicalName(),
                                propertyDescriptor.getDisplayName());
                continue;
            }

            //---------------------------------------------------------------
            A sensitiveWords = MethodUtils.getAnnotation(readMethod, annotationCls, true, true);
            if (null != sensitiveWords){
                list.add(propertyDescriptor.getName());
            }
        }
        return list;
    }
}
