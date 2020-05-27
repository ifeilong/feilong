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
package com.feilong.formatter.builder;

import java.util.List;

import com.feilong.formatter.entity.BeanFormatterConfig;
import com.feilong.formatter.entity.FormatterColumnEntity;

/**
 * 不同类型构造格式化的构造器.
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * 
 * <p>
 * 目前已知实现类有:
 * </p>
 * 
 * <ol>
 * <li>{@link BeanTypeFormatterBuilder} bean 对象类型</li>
 * <li>{@link MapTypeFormatterBuilder} map 类型</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
public interface FormatterBuilder{

    /**
     * 构造 {@link FormatterColumnEntity} List.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @param beanFormatterConfig
     *            the bean formatter config
     * @return the list
     */
    <T> List<FormatterColumnEntity> build(T bean,BeanFormatterConfig beanFormatterConfig);

    /**
     * 构造 数据.
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @param propertyNameList
     *            the property name list
     * @param beanFormatterConfig
     * @return the object[]
     * 
     * @since 1.10.6 add beanFormatterConfig param
     */
    <T> Object[] buildLineData(T bean,List<String> propertyNameList,BeanFormatterConfig beanFormatterConfig);
}
