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
package com.feilong.namespace.parser;

import static com.feilong.namespace.BeanDefinitionParserUtil.addPropertyValue;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.feilong.accessor.cookie.CookieAccessor;
import com.feilong.namespace.RuntimeBeanReferenceBuilder;

/**
 * CookieAccessorBeanDefinitionParser.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see "org.springframework.web.servlet.config.AnnotationDrivenBeanDefinitionParser"
 * @since 2.0.0
 */
public class CookieAccessorBeanDefinitionParser extends AbstractBeanDefinitionParser{

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element,
     * org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    protected AbstractBeanDefinition parseInternal(Element element,ParserContext parserContext){
        // create a RootBeanDefinition that will serve as configuration holder for the 'pattern' attribute and the 'lenient' attribute  
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition();
        rootBeanDefinition.setBeanClass(CookieAccessor.class);

        //---------------------------------------------------------------

        rootBeanDefinition.getPropertyValues().addPropertyValue(
                        "cookieEntity",
                        RuntimeBeanReferenceBuilder.build(parserContext, CookieEntityBeanDefinitionBuilderBuilder.build(element)));
        addPropertyValue(element, rootBeanDefinition, "isValueEncoding", false);

        return rootBeanDefinition;
    }

}