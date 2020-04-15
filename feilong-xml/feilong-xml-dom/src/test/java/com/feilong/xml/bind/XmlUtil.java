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
package com.feilong.xml.bind;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * xml相关,使用原始的 javax.xml. 标准的jdk api (Java Architecture for XML Binding)
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 * @deprecated 将来会重命名
 */
@Deprecated
public class XmlUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlUtil.class);

    /**
     * QName 表示 XML 规范中定义的限定名称 <br>
     * QName 的值包含名称空间 URI、本地部分和前缀.
     * 
     * <pre class="code">
     * 
     * 指定名称空间 URI 和本地部分的 QName 构造方法
     * 如果名称空间 URI 为 null,则将它设置为 XMLConstants.NULL_NS_URI.此值表示非显式定义的名称空间,在 Namespaces in XML 规范中定义.此操作保持了与 QName 1.0 兼容的行为.显式提供 XMLConstants.NULL_NS_URI 值是首选的编码风格.
     * 
     * 如果本地部分为 null,则抛出 {@link IllegalArgumentException}.允许 &quot;&quot; 的本地部分保持与 QName 1.0 的兼容行为.
     * 当使用此构造方法时,将前缀设置为 XMLConstants.DEFAULT_NS_PREFIX.
     * 名称空间 URI 不根据 URI 参考验证.没有按 Namespaces in XML 中的指定将本地部分作为 NCName 来验证.
     * 
     * </pre>
     * 
     * @param namespaceURI
     *            QName 的名称空间 URI
     * @param localPart
     *            QName 的本地部分
     * @return the q name
     */
    public static QName getQName(String namespaceURI,String localPart){
        return new QName(namespaceURI, localPart);
    }

    //---------------------------------------------------------------

    /**
     * Xml 元素的 JAXB 表示形式..
     * 
     * @param namespaceURI
     *            QName 的名称空间 URI
     * @param localPart
     *            QName 的本地部分
     * @param value
     *            值
     * @return Xml 元素的 JAXB 表示形式.
     */
    public static JAXBElement<String> getJAXBElement(String namespaceURI,String localPart,String value){
        QName name = getQName(namespaceURI, localPart);
        return getJAXBElement(name, value);
    }

    /**
     * Timestamp类型的传值方式.
     * 
     * @param name
     *            QName
     * @param date
     *            日期
     * @return Timestamp类型的传值方式
     */
    public static JAXBElement<XMLGregorianCalendar> getJAXBElement(QName name,Date date){
        XMLGregorianCalendar xml_gregorianCalendar = convertDateToXMLGregorianCalendar(date);
        return new JAXBElement<XMLGregorianCalendar>(name, XMLGregorianCalendar.class, xml_gregorianCalendar);
    }

    /**
     * 将date 转成XMLGregorianCalendar.
     * 
     * @param date
     *            date
     * @return 将date 转成XMLGregorianCalendar
     */
    public static XMLGregorianCalendar convertDateToXMLGregorianCalendar(Date date){
        try{
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            GregorianCalendar gregorianCalendar = (GregorianCalendar) DateUtils.toCalendar(date);
            return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        }catch (DatatypeConfigurationException e){
            LOGGER.error("", e);
        }
        return null;
    }

    //---------------------------------------------------------------

    /**
     * Double类型的传值方式.
     * 
     * @param name
     *            QName
     * @param value
     *            值
     * @return Double类型的传值方式
     */
    public static JAXBElement<Double> getJAXBElement(QName name,Double value){
        return new JAXBElement<Double>(name, Double.class, value);
    }

    /**
     * Integer类型的传值方式.
     * 
     * @param name
     *            QName
     * @param value
     *            值
     * @return Integer类型的传值方式
     */
    public static JAXBElement<Integer> getJAXBElement(QName name,Integer value){
        return new JAXBElement<Integer>(name, Integer.class, value);
    }

    /**
     * 字符串传值.
     * 
     * @param name
     *            QName
     * @param str
     *            字符串
     * @return 字符串传值
     */
    public static JAXBElement<String> getJAXBElement(QName name,String str){
        return new JAXBElement<String>(name, String.class, str);
    }
}
