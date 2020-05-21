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
package com.feilong.namespace;

import static com.feilong.core.Validator.isNotNullOrEmpty;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.w3c.dom.Element;

import com.feilong.core.Validate;

/**
 * The Class BeanDefinitionParserUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.0.0
 */
public class BeanDefinitionParserUtil{

    /** Don't let anyone instantiate this class. */
    private BeanDefinitionParserUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 添加 property value.
     *
     * @param element
     *            the element
     * @param beanDefinitionBuilder
     *            the bean definition builder
     * @param attributeName
     *            the attribute name
     * @param required
     *            the required
     */
    public static void addPropertyValue(Element element,BeanDefinitionBuilder beanDefinitionBuilder,String attributeName,boolean required){
        Validate.notBlank(attributeName, "name can't be blank!,element:[%s]", element.toString());

        //---------------------------------------------------------------
        //没有属性
        if (!element.hasAttribute(attributeName)){
            if (required){
                throw new IllegalArgumentException("must config attributeName:[" + attributeName + "]");
            }
            return;
        }

        //---------------------------------------------------------------
        //有元素
        String value = element.getAttribute(attributeName);
        if (required){
            Validate.notBlank(value, "when name is :[%s],value can't be blank!", attributeName);
        }

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(value)){
            beanDefinitionBuilder.addPropertyValue(attributeName, value);
        }
    }

    /**
     * 添加 property value.
     *
     * @param element
     *            the element
     * @param rootBeanDefinition
     *            the root bean definition
     * @param attributeName
     *            the attribute name
     * @param required
     *            the required
     */
    public static void addPropertyValue(Element element,RootBeanDefinition rootBeanDefinition,String attributeName,boolean required){
        Validate.notBlank(attributeName, "name can't be blank!,element:[%s]", element.toString());

        //没有属性
        if (!element.hasAttribute(attributeName)){
            if (required){
                throw new IllegalArgumentException("must config attributeName:[" + attributeName + "]");
            }
            return;
        }

        //---------------------------------------------------------------
        //有元素
        String value = element.getAttribute(attributeName);
        if (required){
            Validate.notBlank(value, "when name is :[%s],value can't be blank!", attributeName);
        }

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(value)){
            rootBeanDefinition.getPropertyValues().addPropertyValue(attributeName, value);
        }
    }

    /**
     * 添加 property value.
     *
     * @param element
     *            the element
     * @param beanDefinitionBuilder
     *            the bean definition builder
     * @param attributeNameList
     *            the attribute name list
     */
    public static void addPropertyValue(Element element,BeanDefinitionBuilder beanDefinitionBuilder,String...attributeNameList){
        for (String attributeName : attributeNameList){
            add(element, beanDefinitionBuilder, attributeName);
        }
    }

    /**
     * 添加.
     *
     * @param element
     *            the element
     * @param beanDefinitionBuilder
     *            the bean definition builder
     * @param attributeName
     *            the attribute name
     */
    private static void add(Element element,BeanDefinitionBuilder beanDefinitionBuilder,String attributeName){
        addPropertyValue(element, beanDefinitionBuilder, attributeName, false);
    }

    //    /**
    //     * 添加.
    //     */
    //    private static void add(Element element,BeanDefinitionBuilder beanDefinitionBuilder,List<String> attributeNameList,boolean required){
    //        for (String attributeName : attributeNameList){
    //            addPropertyValue(element, beanDefinitionBuilder, attributeName, required);
    //        }
    //    }
}
