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
package com.feilong.xml.temp.annotation;

import static com.feilong.lib.lang3.StringUtils.EMPTY;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.feilong.lib.lang3.StringUtils;
import com.feilong.lib.lang3.Validate;
import com.feilong.test.AbstractTest;

public class Temp extends AbstractTest{

    //---------------------------------------------------------------
    //
    //    /**
    //     * 获得 element by id.
    //     *
    //     * @param elementId
    //     *            the element id
    //     * @return the element by id
    //     */
    //    public Element getElementById(String elementId){
    //        return document.getElementById(elementId);
    //    }

    //---------------------------------------------------------------

    //    /**
    //     * 获取xml对象文档节点里面的属性值.
    //     *
    //     * @param nodeTagName
    //     *            节点名称
    //     * @param attributeName
    //     *            属性名称
    //     * @return 对应的属性值
    //     */
    //    public String getAttributeValue(String nodeTagName,String attributeName){
    //        return getAttributeValue(nodeTagName, 0, attributeName);
    //    }

    //    /**
    //     * 获取xml对象文档节点里面的属性值.
    //     * 
    //     * @param nodeTagName
    //     *            节点名称
    //     * @param tagIndex
    //     *            标签下标
    //     * @param attributeName
    //     *            属性名称
    //     * @return 对应的属性值
    //     */
    //    public String getAttributeValue(String nodeTagName,int tagIndex,String attributeName){
    //        Node node = getNodeByTagName(nodeTagName, tagIndex);
    //        return getAttributeValue(node, attributeName);
    //    }

    //***************************XPath**********************************************************
    //    /**
    //     * xpath 获得node 属性.
    //     *
    //     * @param expression
    //     *            xpath表达式
    //     * @param attributeName
    //     *            属性名称
    //     * @return 获得node 属性
    //     */
    //    public String getAttributeValueByXPath(String expression,String attributeName){
    //        Node node = XPathUtil.evaluate(document, expression, XPathConstants.NODE);
    //        return getAttributeValue(node, attributeName);
    //    }

    //---------------------------------------------------------------
    //
    //    /**
    //     * 获得 document.
    //     *
    //     * @return the document
    //     * @since 1.10.0
    //     */
    //    public Document getDocument(){
    //        return document;
    //    }

    //---------------------------------------------------------------

    //    /**
    //     * 获得xml节点.
    //     *
    //     * @param nodeTagName
    //     *            节点名称
    //     * @return 节点
    //     */
    //    private Node getNodeByTagName(String nodeTagName){
    //        return getNodeByTagName(nodeTagName, 0);
    //    }
    //
    //    /**
    //     * 获取xml node对象.
    //     *
    //     * @param nodeTagName
    //     *            节点名称
    //     * @param tagIndex
    //     *            标签下标
    //     * @return node对象
    //     */
    //    public Node getNodeByTagName(String nodeTagName,int tagIndex){
    //        try{
    //            return document.getElementsByTagName(nodeTagName).item(tagIndex);
    //        }catch (Exception e){
    //            throw new UncheckedXmlParseException("nodeTagName:" + nodeTagName + ",tagIndex:" + tagIndex, e);
    //        }
    //    }

    //
    //    /**
    //     * 将object转成xml字符串.
    //     * 
    //     * <h3>说明:</h3>
    //     * <blockquote>
    //     * <ol>
    //     * <li>支持 {@link HashMap},{@link LinkedHashMap},{@link ConcurrentHashMap}等map子类 按照 <code>rootElementName</code> 根元素名称输出</li>
    //     * </ol>
    //     * </blockquote>
    //     * 
    //     * <h3>使用示例:</h3>
    //     * <p>
    //     * <b>场景1:</b> 对于 bean转成XML
    //     * </p>
    //     * 
    //     * <pre class="code">
    //     * 
    //     * public void name1(){
    //     *     User user = new User(1L);
    //     * 
    //     *     LOGGER.debug(XStreamUtil.toXML(user));
    //     * }
    //     * </pre>
    //     * 
    //     * 返回:
    //     * 
    //     * <pre class="code">
    //     * {@code
    //     *  <com.feilong.test.User>
    //     *   <name>feilong</name>
    //     *   <id>1</id>
    //     *   <userInfo/>
    //     *   <userAddresseList/>
    //     * </com.feilong.test.User>
    //     * }
    //     * </pre>
    //     * 
    //     * 
    //     * <p>
    //     * <b>场景2:</b> 对于简单的map
    //     * </p>
    //     * 
    //     * <pre class="code">
    //     * 
    //     * public void testToXML3(){
    //     *     Map{@code <String, String>} map = new HashMap{@code <>}();
    //     *     map.put("out_trade_no", "112122212");
    //     *     map.put("total_fee", "125.00");
    //     *     map.put("call_back_url", "");
    //     *     map.put("notify_url", "");
    //     *     LOGGER.debug(XStreamUtil.toXML(map, null));
    //     * }
    //     * </pre>
    //     * 
    //     * <b>返回:</b>
    //     * 
    //     * <pre class="code">
    //     * {@code
    //     <map>
    //       <entry>
    //         <string>call_back_url</string>
    //         <string></string>
    //       </entry>
    //       <entry>
    //         <string>total_fee</string>
    //         <string>125.00</string>
    //       </entry>
    //       <entry>
    //         <string>notify_url</string>
    //         <string></string>
    //       </entry>
    //       <entry>
    //         <string>out_trade_no</string>
    //         <string>112122212</string>
    //       </entry>
    //     </map>
    //     }
    //     * </pre>
    //     * 
    //     * <p>
    //     * <b>场景3:</b> 对于 list 转成XML
    //     * </p>
    //     * 
    //     * <pre class="code">
    //     * 
    //     * public void name1(){
    //     *     LOGGER.debug(XStreamUtil.toXML(toList(new User(1L))));
    //     * }
    //     * </pre>
    //     * 
    //     * 返回:
    //     * 
    //     * <pre class="code">
    //     * {@code
    //    <list>
    //    <com.feilong.store.member.User>
    //    <id>1</id>
    //    <name>feilong</name>
    //    <ageInt>0</ageInt>
    //    <userInfo/>
    //    <userAddresseList/>
    //    </com.feilong.store.member.User>
    //    </list>
    //    
    //     * }
    //     * </pre>
    //     * 
    //     * @param bean
    //     *            the obj
    //     * @return 如果 <code>bean</code> 是null,抛出 {@link NullPointerException}<br>
    //     * @see com.thoughtworks.xstream.XStream#toXML(Object)
    //     * @see com.thoughtworks.xstream.XStream#alias(String, Class)
    //     * @see com.thoughtworks.xstream.XStream#addImplicitCollection(Class, String)
    //     * @since 1.10.7
    //     * @deprecated 现实中很少用, 主要 rootElementName 不好
    //     */
    //    @Deprecated
    //    public static String toXML(Object bean){
    //        return toXML(bean, null);
    //    }

    //
    //    /**
    //     * 将{@code xml}字符串转成 T对象.
    //     * 
    //     * <h3>注意:</h3>
    //     * <blockquote>
    //     * <ol>
    //     * <li>内部封装了 {@link XStream#ignoreUnknownElements()} 忽略了不存在的字段</li>
    //     * </ol>
    //     * </blockquote>
    //     *
    //     * @param <T>
    //     *            the generic type
    //     * @param xml
    //     *            the xml
    //     * @return 如果 <code>xml</code> 是null或者是empty,返回 null<br>
    //     */
    //    public static <T> T fromXML(String xml){
    //        return fromXML(xml, (XStreamConfig) null);
    //    }

    /**
     * 获得 元素 <code>element</code> 指定的属性名字 <code>attributeName</code> 的值.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * 比如有以下的xml片段
     * 
     * <pre class="code">
    {@code 
    <novel ID="偷香">
                <lastedCatalogName>第1020节 迷失空间</lastedCatalogName>
                <beginType>next</beginType>
    </novel>
    
    }
     * 
     * </pre>
     * 
     * 此时你可以使用
     * 
     * <pre class="code">
     * 
     * String id = DomParser.getAttributeValue(novelElement, "ID");
     * 
     * </pre>
     * 
     * 来获得字符串 "偷香"
     * 
     * </blockquote>
     * 
     * @param element
     *            the element
     * @param attributeName
     *            属性名字
     * @return 如果 <code>element</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>attributeName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>attributeName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         the empty string if that attribute does not have a specified or default value.
     */
    public static String getAttributeValue(Element element,String attributeName){
        Validate.notNull(element, "element can't be null!");
        Validate.notBlank(attributeName, "attributeName can't be null/empty!");
        //---------------------------------------------------------------
        return element.getAttribute(attributeName);
    }

    /**
     * 获得 element text.
     *
     * @param element
     *            the element
     * @return the element text
     */
    public static String getElementText(Element element){
        Validate.notNull(element, "element can't be null!");
        return element.getTextContent();
    }

    /**
     * 获得 <code>element</code> 元素指定的 子元素 <code>childElementName</code> 的文字.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * 比如有以下的xml片段
     * 
     * <pre class="code">
    {@code 
    <novel ID="偷香">
                <lastedCatalogName>第1020节 迷失空间</lastedCatalogName>
                <beginType>next</beginType>
    </novel>
    
    }
     * 
     * </pre>
     * 
     * 此时你可以使用
     * 
     * <pre class="code">
     * 
     * String beginType = DomParser.getChildElementText(novelElement, "beginType");
     * 
     * </pre>
     * 
     * 来获得字符串 next
     * 
     * </blockquote>
     *
     * @param element
     *            the element
     * @param childElementName
     *            子元素的元素名称
     * @return 如果 <code>element</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>childElementName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>childElementName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>element</code> 找不到指定名字<code>childElementName</code>的子元素 ,那么返回 {@link StringUtils#EMPTY}<br>
     *         如果 <code>element</code> 找到指定名字<code>childElementName</code>的子元素 ,那么返回元素的 {@link org.w3c.dom.Node#getTextContent()}
     */
    public static String getChildElementText(Element element,String childElementName){
        Validate.notNull(element, "element can't be null!");
        Validate.notBlank(childElementName, "childElementName can't be null/empty!");

        //---------------------------------------------------------------
        NodeList nodeList = element.getElementsByTagName(childElementName);
        if (null == nodeList){
            return EMPTY;
        }
        //---------------------------------------------------------------
        Node item = nodeList.item(0);
        if (null == item){
            return EMPTY;
        }
        return item.getTextContent();
    }

    /**
     * 获得 parent by tag name.
     *
     * @param element
     *            the element
     * @param parentTagName
     *            the parent tag name
     * @return 如果 <code>parentTagName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>parentTagName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static Element getParentByTagName(Element element,String parentTagName){
        Validate.notNull(element, "element can't be null!");
        Validate.notBlank(parentTagName, "parentTagName can't be null/empty!");
        //---------------------------------------------------------------
        Element parentElement = (Element) element.getParentNode();
        if (null == parentElement){
            return null;
        }
        if (parentTagName.equals(parentElement.getTagName())){
            return parentElement;
        }
        //递归
        return getParentByTagName(parentElement, parentTagName);
    }

    /**
     * 获得 child by tag name.
     *
     * @param element
     *            the element
     * @param childTagName
     *            the child tag name
     * @return 如果 <code>childTagName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>childTagName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static Element getChildElementByTagName(Element element,String childTagName){
        Validate.notNull(element, "element can't be null!");
        Validate.notBlank(childTagName, "childTagName can't be null/empty!");

        NodeList childNodes = element.getChildNodes();
        for (int i = 0, j = childNodes.getLength(); i < j; ++i){
            Node node = childNodes.item(i);
            if (Node.ELEMENT_NODE != node.getNodeType()){
                continue;
            }
            //---------------------------------------------------------------
            Element childElement = (Element) node;
            if (childTagName.equals(childElement.getTagName())){
                return childElement;
            }
        }
        return null;
    }

    //    /**
    //     * 获取节点及其后代的文本内容.
    //     * 
    //     * @param nodeTagName
    //     *            节点名称
    //     * @return 节点及其后代的文本内容
    //     */
    //    public String getNodeTextContent(String nodeTagName){
    //        Node node = getNodeByTagName(nodeTagName);
    //        return getNodeTextContent(node);
    //    }

    //---------------------------------------------------------------
    //
    //    /**
    //     * 获得节点内容.
    //     * 
    //     * @param node
    //     *            节点
    //     * @return the node text content
    //     */
    //    private static String getNodeTextContent(Node node){
    //        Validate.notNull(node, "element can't be null!");
    //        return node.getTextContent();
    //    }

    /**
     * 获得 children by tag name.
     *
     * @param element
     *            the element
     * @param childTagName
     *            the child tag name
     * @return 如果 <code>childTagName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>childTagName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static List<Element> getChildrenElementsByTagName(Element element,String childTagName){
        Validate.notNull(element, "element can't be null!");
        Validate.notBlank(childTagName, "childTagName can't be null/empty!");

        NodeList childNodes = element.getChildNodes();

        List<Element> list = new LinkedList<>();
        for (int i = 0, j = childNodes.getLength(); i < j; ++i){
            Node node = childNodes.item(i);
            if (Node.ELEMENT_NODE != node.getNodeType()){
                continue;
            }

            Element element2 = (Element) node;
            if (childTagName.equals(element2.getTagName())){
                list.add(element2);
            }
        }
        return list;
    }
}
