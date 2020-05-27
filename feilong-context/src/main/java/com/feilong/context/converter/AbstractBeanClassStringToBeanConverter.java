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
package com.feilong.context.converter;

/**
 * 抽象的指定class类型的将 字符串转成 bean 的转换器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.3
 */
public abstract class AbstractBeanClassStringToBeanConverter<T> extends AbstractStringToBeanConverter<T>{

    /** 需要被转换的bean对象类型. */
    protected Class<T> beanClass;

    //---------------------------------------------------------------

    /**
     * 获得 需要被转换的bean对象类型.
     *
     * @return the beanClass
     */
    public Class<T> getBeanClass(){
        return beanClass;
    }

    /**
     * 设置 需要被转换的bean对象类型.
     *
     * @param beanClass
     *            the beanClass to set
     */
    public void setBeanClass(Class<T> beanClass){
        this.beanClass = beanClass;
    }
}
