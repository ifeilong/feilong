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
package com.feilong.core.bean;

import static com.feilong.core.bean.ConvertUtil.convert;
import static com.feilong.core.bean.PropertyDescriptorUtil.getSpringPropertyDescriptor;
import static com.feilong.core.bean.PropertyDescriptorUtil.isUseSpringOperate;
import static com.feilong.core.lang.StringUtil.formatPattern;
import static com.feilong.core.util.CollectionsUtil.first;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.lib.beanutils.PropertyUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 属性值获取器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyValueObtainer{

    /**
     * Gets the property focus.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @param propertyName
     *            the property name
     * @return the property focus
     */
    static <T> T obtain(Object bean,String propertyName){
        if (PropertyDescriptorUtil.isUseSpringOperate(bean.getClass(), propertyName)){
            return getDataUseSpring(bean, propertyName);
        }
        return getDataUseApache(bean, propertyName);
    }

    //---------------------------------------------------------------

    /**
     * Gets the data use apache.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @param propertyName
     *            the property name
     * @return the data use apache
     */
    @SuppressWarnings("unchecked")
    private static <T> T getDataUseApache(Object bean,String propertyName){
        try{
            return (T) PropertyUtils.getProperty(bean, propertyName);
        }catch (Throwable e){
            String pattern = "getProperty exception,bean:[{}],propertyName:[{}]";
            throw new BeanOperationException(formatPattern(pattern, bean, propertyName), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Gets the data use spring.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @param propertyName
     *            the property name
     * @return the data use spring
     */
    private static <T> T getDataUseSpring(Object bean,String propertyName){
        log.trace("will use spring beanutils to execute:[{}],propertyName:[{}]", bean, propertyName);
        try{
            PropertyDescriptor propertyDescriptor = PropertyDescriptorUtil.getSpringPropertyDescriptor(bean.getClass(), propertyName);
            return getValue(bean, propertyDescriptor);
        }catch (Throwable e){
            String pattern = "getProperty exception,bean:[{}],propertyName:[{}]";
            throw new BeanOperationException(formatPattern(pattern, bean, propertyName), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 循环<code>beanIterable</code>,调用 {@link PropertyUtil#getProperty(Object, String)} 获得 propertyName的值,塞到 <code>returnCollection</code>
     * 中返回.
     *
     * @param <T>
     *            the generic type
     * @param <O>
     *            the generic type
     * @param <K>
     *            the key type
     * @param beanIterable
     *            支持
     * 
     *            <ul>
     *            <li>bean Iterable,比如List{@code <User>},Set{@code <User>}等</li>
     *            <li>map Iterable,比如{@code List<Map<String, String>>}</li>
     *            <li>list Iterable , 比如 {@code  List<List<String>>}</li>
     *            <li>数组 Iterable ,比如 {@code  List<String[]>}</li>
     *            </ul>
     * @param propertyName
     *            泛型O对象指定的属性名称,Possibly indexed and/or nested name of the property to be modified,参见
     *            <a href="../bean/BeanUtil.html#propertyName">propertyName</a>
     * @param returnCollection
     *            the return collection
     * @return 如果 <code>returnCollection</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beanIterable</code> 是null或者empty,返回 <code>returnCollection</code><br>
     *         如果 <code>propertyName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>propertyName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see PropertyUtil#getProperty(Object, String)
     * @see "org.apache.commons.beanutils.BeanToPropertyValueTransformer"
     * @since 1.0.8
     */
    public static <T, O, K extends Collection<T>> K getPropertyValueCollection(
                    Iterable<O> beanIterable,
                    String propertyName,
                    K returnCollection){
        return getPropertyValueCollection(beanIterable, propertyName, returnCollection, null);
    }

    /**
     * 循环<code>beanIterable</code>,调用 {@link PropertyUtil#getProperty(Object, String)} 获得 propertyName的值,将值进行类型转换成
     * <code>returnElementClass</code> ,塞到 <code>returnCollection</code> 中返回.
     *
     * @param <T>
     *            the generic type
     * @param <O>
     *            the generic type
     * @param <K>
     *            the key type
     * @param beanIterable
     *            支持
     * 
     *            <ul>
     *            <li>bean Iterable,比如List{@code <User>},Set{@code <User>}等</li>
     *            <li>map Iterable,比如{@code List<Map<String, String>>}</li>
     *            <li>list Iterable , 比如 {@code  List<List<String>>}</li>
     *            <li>数组 Iterable ,比如 {@code  List<String[]>}</li>
     *            </ul>
     * @param propertyName
     *            泛型O对象指定的属性名称,Possibly indexed and/or nested name of the property to be modified,参见
     *            <a href="../bean/BeanUtil.html#propertyName">propertyName</a>
     * @param returnCollection
     *            the return collection
     * @param returnElementClass
     *            如果 <code>returnElementClass</code> 是null,表示不需要类型转换
     * @return 如果 <code>returnCollection</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beanIterable</code> 是null或者empty,返回 <code>returnCollection</code><br>
     *         如果 <code>propertyName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>propertyName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>returnElementClass</code> 是null,表示不需要类型转换<br>
     * @see PropertyUtil#getProperty(Object, String)
     * @see "org.apache.commons.beanutils.BeanToPropertyValueTransformer"
     * @since 3.3.1
     */
    public static <T, O, K extends Collection<T>> K getPropertyValueCollection(
                    Iterable<O> beanIterable,
                    String propertyName,
                    K returnCollection,
                    Class<T> returnElementClass){
        Validate.notBlank(propertyName, "propertyName can't be null/empty!");

        //---------------------------------------------------------------
        O o = first(beanIterable);
        Class<?> klass = o.getClass();
        //spring 操作
        if (isUseSpringOperate(klass, propertyName)){
            PropertyDescriptor propertyDescriptor = getSpringPropertyDescriptor(klass, propertyName);
            for (O bean : beanIterable){
                returnCollection.add(
                                convertValue(PropertyValueObtainer.<Object, O> getValue(bean, propertyDescriptor), returnElementClass));
            }
            return returnCollection;
        }

        //---------------------------------------------------------------
        for (O bean : beanIterable){
            returnCollection.add(convertValue(PropertyUtil.<Object> getProperty(bean, propertyName), returnElementClass));
        }
        return returnCollection;
    }

    //---------------------------------------------------------------

    /**
     * 将 value 转成returnElementClass 类型.
     *
     * @param <T>
     *            the generic type
     * @param value
     *            the value
     * @param returnElementClass
     *            the return element class
     * @return 如果 <code>returnElementClass</code> 是null,表示不需要转换,直接返回 value<br>
     * @since 3.3.1
     */
    @SuppressWarnings("unchecked")
    private static <T> T convertValue(Object value,Class<T> returnElementClass){
        if (null == returnElementClass){
            return (T) value;
        }
        return convert(value, returnElementClass);
    }

    /**
     * Gets the value.
     *
     * @param <T>
     *            the generic type
     * @param <O>
     *            the generic type
     * @param obj
     *            the obj
     * @param propertyDescriptor
     *            the property descriptor
     * @return 如果 <code>obj</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>propertyDescriptor</code> 是null,抛出 {@link NullPointerException}<br>
     * @see <a href="https://github.com/venusdrogon/feilong-core/issues/760">PropertyUtil.getProperty(Object, String) 排序异常</a>
     */
    @SuppressWarnings("unchecked")
    public static <T, O> T getValue(O obj,PropertyDescriptor propertyDescriptor){
        Validate.notNull(obj, "obj can't be null!");
        Validate.notNull(propertyDescriptor, "propertyDescriptor can't be null!");

        //---------------------------------------------------------------
        Method readMethod = propertyDescriptor.getReadMethod();

        //---------------------------------------------------------------
        //since 1.12.2
        Validate.notNull(
                        readMethod,
                        "class:[%s],propertyDescriptor name:[%s],has no ReadMethod!!,pls check",
                        obj.getClass().getCanonicalName(),
                        propertyDescriptor.getDisplayName());

        //---------------------------------------------------------------
        //since 1.12.1
        //https://github.com/venusdrogon/feilong-core/issues/760
        readMethod = com.feilong.lib.beanutils.MethodUtils.getAccessibleMethod(obj.getClass(), readMethod);

        try{
            return (T) readMethod.invoke(obj);
        }catch (ReflectiveOperationException e){
            throw new DefaultRuntimeException(e);
        }
    }
}
