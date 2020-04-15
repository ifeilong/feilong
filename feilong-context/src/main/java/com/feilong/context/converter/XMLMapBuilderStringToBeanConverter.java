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

import static com.feilong.core.CharsetType.UTF8;

import com.feilong.context.converter.builder.BeanBuilder;
import com.feilong.context.converter.builder.CommonBeanBuilder;
import com.feilong.context.converter.builder.NameAndValueMapBuilder;
import com.feilong.context.converter.builder.XmlNodeNameAndValueMapBuilder;
import com.feilong.xml.dom.DomParser;

/**
 * xml的转换.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.0.8
 * @since 1.11.3 rename
 */
public class XMLMapBuilderStringToBeanConverter<T> extends MapBuilderStringToBeanConverter<T>{

    /**
     * Instantiates a new XML string to bean converter.
     */
    public XMLMapBuilderStringToBeanConverter(){
        super();
    }

    /**
     * Instantiates a new XML string to bean converter.
     * 
     * <p>
     * 默认使用 {@link XmlNodeNameAndValueMapBuilder}和 {@link CommonBeanBuilder}
     * </p>
     *
     * @param xpathExpression
     *            the xpath expression
     */
    public XMLMapBuilderStringToBeanConverter(String xpathExpression){
        this(new XmlNodeNameAndValueMapBuilder(xpathExpression), CommonBeanBuilder.INSTANCE);
    }

    /**
     * Instantiates a new XML string to bean converter.
     * 
     * <p>
     * 默认使用 {@link XmlNodeNameAndValueMapBuilder}
     * </p>
     *
     * @param xpathExpression
     *            the xpath expression
     * @param beanBuilder
     *            the bean builder
     */
    public XMLMapBuilderStringToBeanConverter(String xpathExpression, BeanBuilder beanBuilder){
        this(new XmlNodeNameAndValueMapBuilder(xpathExpression), beanBuilder);
    }

    /**
     * Instantiates a new XML string to bean converter.
     *
     * @param nameAndValueMapBuilder
     *            the name and value map builder
     * @param beanBuilder
     *            the bean builder
     */
    public XMLMapBuilderStringToBeanConverter(NameAndValueMapBuilder nameAndValueMapBuilder, BeanBuilder beanBuilder){
        super(nameAndValueMapBuilder, beanBuilder);
    }

    //---------------------------------------------------------------

    /**
     * Format value.
     *
     * @param value
     *            the value
     * @return the string
     * @since 1.11.5
     */
    @Override
    protected String formatValue(String value){
        return new DomParser(value, UTF8).format();
    }

}
