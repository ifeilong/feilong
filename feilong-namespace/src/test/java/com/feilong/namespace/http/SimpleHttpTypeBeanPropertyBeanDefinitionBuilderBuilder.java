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
package com.feilong.namespace.http;

import static com.feilong.namespace.BeanDefinitionParserUtil.addPropertyValue;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 4.0.6
 */
public class SimpleHttpTypeBeanPropertyBeanDefinitionBuilderBuilder{

    /**
     * Builds the.
     *
     * @param element
     *            the element
     * @return the bean definition builder
     */
    public static BeanDefinitionBuilder build(Element element){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                        .genericBeanDefinition("com.feilong.context.beanproperty.SimpleHttpTypeBeanProperty");

        addPropertyValue(element, beanDefinitionBuilder, "uri", "method");
        return beanDefinitionBuilder;
    }
}
