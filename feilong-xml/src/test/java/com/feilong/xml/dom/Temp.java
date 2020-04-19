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
package com.feilong.xml.dom;

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
}
