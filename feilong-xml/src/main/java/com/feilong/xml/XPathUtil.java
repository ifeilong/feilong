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
package com.feilong.xml;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;

/**
 * XPathUtil.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
final class XPathUtil{

    /** Don't let anyone instantiate this class. */
    private XPathUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 通过xpath 获取对象.
     * 
     * @param expression
     *            xpath表达式
     * @param qName
     *            qname 定义的数据类型
     * 
     * @param <T>
     *
     * @return 通过xpath 获取对象
     */
    @SuppressWarnings("unchecked")
    static <T> T evaluate(Node node,String expression,QName qName){
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        try{
            return (T) xpath.evaluate(expression, node, qName);
        }catch (XPathExpressionException e){
            throw new UncheckedXmlParseException("expression:" + expression, e);
        }
    }

}
