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
package com.feilong.json.jsonlib.builder;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.addAllIgnoreNull;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;
import static java.util.Collections.emptyList;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.ClassUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.json.SensitiveWords;
import com.feilong.json.jsonlib.processor.SensitiveWordsJsonValueProcessor;

import net.sf.json.processors.JsonValueProcessor;

/**
 * 专门用来构造 <code>SensitiveWordsPropertyNameAndJsonValueProcessorMap</code> 的构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
public class SensitiveWordsPropertyNameAndJsonValueProcessorMapBuilder{

    /** The Constant log. */
    private static final Logger                                   LOGGER = LoggerFactory
                    .getLogger(SensitiveWordsPropertyNameAndJsonValueProcessorMapBuilder.class);

    //---------------------------------------------------------------

    /** The cache. */
    private static Map<Class<?>, Map<String, JsonValueProcessor>> CACHE  = newHashMap();

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SensitiveWordsPropertyNameAndJsonValueProcessorMapBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

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
        if (CACHE.containsKey(klass)){
            return CACHE.get(klass);
        }

        //---------------------------------------------------------------
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = build(klass);
        CACHE.put(klass, propertyNameAndJsonValueProcessorMap);
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
    private static Map<String, JsonValueProcessor> build(Class<?> klass){
        List<String> list = getWithAnnotationName(klass, SensitiveWords.class);
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
        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(klass);
        if (isNullOrEmpty(propertyDescriptors)){
            return emptyList();
        }

        //---------------------------------------------------------------
        List<String> list = newArrayList();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors){
            Method readMethod = propertyDescriptor.getReadMethod();

            //since 1.12.2
            if (null == readMethod){
                LOGGER.warn(
                                "class:[{}],propertyDescriptor name:[{}],has no ReadMethod!!SKIPPED",
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
