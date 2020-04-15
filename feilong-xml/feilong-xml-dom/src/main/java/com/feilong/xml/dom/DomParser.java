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

import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.feilong.core.CharsetType;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.xml.UncheckedXmlParseException;
import com.feilong.xml.XXEUtil;

/**
 * 使用 java 自带的 dom,{@code javax.xml}包 相关工具类 来解析xml.
 * 
 * <p>
 * Convenience methods for working with the DOM API, in particular for working with DOM Nodes and DOM Elements.
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.apache.solr.common.util.DOMUtil"
 * @see "org.springframework.util.xml.DomUtils"
 * @since 1.5.0
 * @since 1.9.0 change name from DomUtil
 */
public final class DomParser{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DomParser.class);

    /** The document. */
    private final Document      document;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     *
     * @param xmlFileUriString
     *            xml 文件的uri地址
     */
    public DomParser(String xmlFileUriString){
        this.document = buildDocument(xmlFileUriString);
    }

    /**
     * The Constructor.
     *
     * @param xmlString
     *            xml字符串
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量
     */
    public DomParser(String xmlString, String charsetType){
        InputStream inputStream = IOUtils.toInputStream(xmlString, Charset.forName(charsetType));
        this.document = buildDocument(inputStream);
    }

    /**
     * The Constructor.
     *
     * @param xmlFile
     *            the xml file
     */
    public DomParser(File xmlFile){
        this.document = buildDocument(xmlFile);
    }

    /**
     * Instantiates a new dom parser.
     *
     * @param inputStream
     *            the input stream
     * @since 1.10.0
     */
    public DomParser(InputStream inputStream){
        this.document = buildDocument(inputStream);
    }

    //---------------------------------------------------------------

    /**
     * 获得 document.
     *
     * @return the document
     * @since 1.10.0
     */
    public Document getDocument(){
        return document;
    }

    /**
     * Construct document.
     *
     * @param xml
     *            the xml
     * @return the document
     */
    private static Document buildDocument(Object xml){
        DocumentBuilder documentBuilder = newDocumentBuilder();
        try{
            if (xml instanceof String){//注意这里是  xmlFileUriString
                return documentBuilder.parse((String) xml);
            }
            if (xml instanceof File){
                return documentBuilder.parse((File) xml);
            }
            if (xml instanceof InputStream){
                return documentBuilder.parse((InputStream) xml);
            }
            throw new UnsupportedOperationException("xml:[" + xml + "] not support!");
        }catch (SAXException | IOException e){
            throw new UncheckedXmlParseException("input xml:" + xml, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 获得xml节点.
     *
     * @param nodeTagName
     *            节点名称
     * @return 节点
     */
    private Node getNodeByTagName(String nodeTagName){
        return getNodeByTagName(nodeTagName, 0);
    }

    /**
     * 获取xml node对象.
     *
     * @param nodeTagName
     *            节点名称
     * @param tagIndex
     *            标签下标
     * @return node对象
     */
    public Node getNodeByTagName(String nodeTagName,int tagIndex){
        try{
            return document.getElementsByTagName(nodeTagName).item(tagIndex);
        }catch (Exception e){
            throw new UncheckedXmlParseException("nodeTagName:" + nodeTagName + ",tagIndex:" + tagIndex, e);
        }
    }

    /**
     * 获取xml node对象.
     *
     * @param expression
     *            xpath表达式
     * @return node对象
     */
    public Node getNodeByXPath(String expression){
        return (Node) getObjectByXPath(expression, XPathConstants.NODE);
    }

    /**
     * 获得 node list by x path.
     *
     * @param expression
     *            the expression
     * @return the node list by x path
     */
    public NodeList getNodeListByXPath(String expression){
        return (NodeList) getObjectByXPath(expression, XPathConstants.NODESET);
    }

    /**
     * 获得 name and string value map.
     * 
     * @param xpathExpression
     *            the xpath expression
     * @return the name and string value map
     */
    public Map<String, String> getNodeNameAndStringValueMap(String xpathExpression){
        NodeList nodeList = getNodeListByXPath(xpathExpression);

        Map<String, String> nodeNameAndStringValueMap = newLinkedHashMap();

        for (int i = 0, j = nodeList.getLength(); i < j; ++i){
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()){
                nodeNameAndStringValueMap.put(node.getNodeName(), node.getTextContent());
            }
        }

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("nameAndValueMap:{}", formatToSimpleTable(nodeNameAndStringValueMap));
        }
        return nodeNameAndStringValueMap;
    }

    /**
     * 获得 node attribute value and string value map.
     *
     * @param xpathExpression
     *            the xpath expression
     * @param nodeAttributeName
     *            the node attribute name
     * @return the node attribute value and string value map
     */
    public Map<String, String> getNodeAttributeValueAndStringValueMap(String xpathExpression,String nodeAttributeName){
        NodeList nodeList = getNodeListByXPath(xpathExpression);

        Map<String, String> nodeAttributeValueAndStringValueMap = newLinkedHashMap();
        for (int i = 0, j = nodeList.getLength(); i < j; ++i){
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()){

                String attributeValue = getAttributeValue(node, nodeAttributeName);
                nodeAttributeValueAndStringValueMap.put(attributeValue, node.getTextContent());
            }
        }

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("nodeAttributeValueAndStringValueMap:{}", JsonUtil.format(nodeAttributeValueAndStringValueMap));
        }
        return nodeAttributeValueAndStringValueMap;
    }

    //---------------------------------------------------------------

    /**
     * 获得 element by id.
     *
     * @param elementId
     *            the element id
     * @return the element by id
     */
    public Element getElementById(String elementId){
        return document.getElementById(elementId);
    }

    /**
     * 通过xpath 获取对象.
     *
     * @param expression
     *            xpath表达式
     * @param qName
     *            qname 定义的数据类型
     * @return 通过xpath 获取对象
     */
    public Object getObjectByXPath(String expression,QName qName){
        XPathFactory pathFactory = XPathFactory.newInstance();
        XPath xpath = pathFactory.newXPath();
        try{
            return xpath.evaluate(expression, document, qName);
        }catch (XPathExpressionException e){
            throw new UncheckedXmlParseException("expression:" + expression, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 获取xml对象文档节点里面的属性值.
     *
     * @param nodeTagName
     *            节点名称
     * @param attributeName
     *            属性名称
     * @return 对应的属性值
     */
    public String getAttributeValue(String nodeTagName,String attributeName){
        return getAttributeValue(nodeTagName, 0, attributeName);
    }

    /**
     * 获取xml对象文档节点里面的属性值.
     * 
     * @param nodeTagName
     *            节点名称
     * @param tagIndex
     *            标签下标
     * @param attributeName
     *            属性名称
     * @return 对应的属性值
     */
    public String getAttributeValue(String nodeTagName,int tagIndex,String attributeName){
        Node node = getNodeByTagName(nodeTagName, tagIndex);
        return getAttributeValue(node, attributeName);
    }

    //***************************XPath**********************************************************
    /**
     * xpath 获得node 属性.
     *
     * @param expression
     *            xpath表达式
     * @param attributeName
     *            属性名称
     * @return 获得node 属性
     */
    public String getAttributeValueByXPath(String expression,String attributeName){
        Node node = getNodeByXPath(expression);
        return getAttributeValue(node, attributeName);
    }

    /**
     * 获得node属性值.
     * 
     * @param node
     *            节点
     * @param attributeName
     *            属性名称
     * @return 获得node属性值<br>
     *         如果 <code>attributeName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>attributeName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static String getAttributeValue(Node node,String attributeName){
        Validate.notNull(node, "node can't be null!");
        Validate.notBlank(attributeName, "attributeName can't be null/empty!");

        NamedNodeMap namedNodeMap = node.getAttributes();
        Node node_current = namedNodeMap.getNamedItem(attributeName);
        if (null != node_current){
            return node_current.getNodeValue();
        }
        return EMPTY;
    }

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

    //---------------------------------------------------------------

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

    //---------------------------------------------------------------

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

            if (Node.ELEMENT_NODE == node.getNodeType()){
                Element element2 = (Element) node;

                if (childTagName.equals(element2.getTagName())){
                    return element2;
                }
            }
        }
        return null;
    }

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

            if (Node.ELEMENT_NODE == node.getNodeType()){
                Element element2 = (Element) node;

                if (childTagName.equals(element2.getTagName())){
                    list.add(element2);
                }
            }
        }
        return list;
    }

    /**
     * 返回节点及其后代的文本内容.
     * 
     * @param nodeTagName
     *            节点名称
     * @return 节点及其后代的文本内容
     */
    public String getNodeTextContent(String nodeTagName){
        Node node = getNodeByTagName(nodeTagName);
        return getNodeTextContent(node);
    }

    //---------------------------------------------------------------

    /**
     * 获得节点内容.
     * 
     * @param node
     *            节点
     * @return the node text content
     */
    private static String getNodeTextContent(Node node){
        Validate.notNull(node, "element can't be null!");
        return node.getTextContent();
    }

    //---------------------------------------------------------------

    /**
     * Creates the document builder.
     * 
     * @return the document builder
     */
    private static DocumentBuilder newDocumentBuilder(){
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        //设置将空白字符忽略  
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);

        //since 1.12.1
        XXEUtil.disableXXE(documentBuilderFactory);

        try{
            return documentBuilderFactory.newDocumentBuilder();
        }catch (ParserConfigurationException e){
            throw new UncheckedXmlParseException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 格式化.
     *
     * @return the string
     */
    public String format(){
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try{
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            //---------------------------------------------------------------

            DOMSource domSource = new DOMSource(document);

            Writer writer = new StringWriter();
            StreamResult streamResult = new StreamResult(writer);

            transformer.transform(domSource, streamResult);

            return writer.toString();
        }catch (TransformerException e){
            throw new UncheckedXmlParseException(e);
        }
    }
}