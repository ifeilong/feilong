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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;
import static java.util.Collections.emptyMap;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.xml.xstream.XStreamConfig;
import com.feilong.xml.xstream.XStreamConfigBuilder;
import com.feilong.xml.xstream.converters.SimpleMapConverter;
import com.thoughtworks.xstream.XStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * xml 工具.
 * 
 * <p>
 * Convenience methods for working with the DOM API, in particular for working with DOM Nodes and DOM Elements.
 * </p>
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>本类有使用xstream解析,首先自己项目需要依赖 xstream,<span style="color:green">版本号自行使用最新的版本</span>
 * 
 * <pre class="code">
 * {@code
 <dependency>
        <groupId>com.thoughtworks.xstream</groupId>
        <artifactId>xstream</artifactId>
        <version>1.4.20</version> 
      </dependency>
 * }
 * </pre>
 * 
 * </li>
 * </ol>
 * </blockquote>
 * 
 * 
 * 
 * 将object转成xml字符串.
 * 
 * <h3>使用示例:</h3>
 * 
 * <blockquote>
 * <p>
 * 对于将下面的对象,转成XML<br>
 * User user = new User(1L);
 * </p>
 * <p>
 * <b>1.不带XStreamConfig参数</b> <br>
 * 使用 {@code com.feilong.xml.XmlUtil.toXML(user, null)},则返回
 * 
 * <pre class="code">
 * {@code
 *  <com.feilong.test.User>
 *   <name>feilong</name>
 *   <id>1</id>
 *   <userInfo/>
 *   <userAddresseList/>
 * </com.feilong.test.User>
 * }
 * </pre>
 * 
 * </p>
 * 
 * 
 * <p>
 * 
 * <b>2.设置 Alias 参数</b> <br>
 * 可以看到上面的结果中,XML Root元素名字是 com.feilong.test.User,如果只是显示成 {@code <user>}怎么做呢？<br>
 * 使用
 * 
 * <code>
 * <pre class="code">
 *     User user = new User(1L);
 *     XStreamConfig xStreamConfig = new XStreamConfig();
 *     xStreamConfig.getAliasMap().put(&quot;user&quot;, User.class);
 *     log.info(XmlUtil.toXML(user, xStreamConfig));
 * </pre>
 * </code>
 * 
 * ,则返回
 * 
 * <pre class="code">
 * {@code
 * <user>
 *   <name>feilong</name>
 *   <id>1</id>
 *   <userInfo/>
 *   <userAddresseList/>
 * </user>
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * 
 * <b>3.设置 ImplicitCollection 参数</b><br>
 * 如果我在结果不想出现 {@code <userAddresseList/>}怎么做呢？<br>
 * 使用
 * 
 * <code>
 * <pre class="code">
 *     User user = new User(1L);
 *     XStreamConfig xStreamConfig = new XStreamConfig();
 *     xStreamConfig.getAliasMap().put(&quot;user&quot;, User.class);
 *     xStreamConfig.getImplicitCollectionMap().put(&quot;userAddresseList&quot;, User.class);
 *     log.info(XmlUtil.toXML(user, xStreamConfig));
 * </pre>
 * </code>
 * 
 * ,则返回
 * 
 * <pre class="code">
 * {@code
 *  <user>
 *   <name>feilong</name>
 *   <id>1</id>
 *   <userInfo/>
 * </user>
 * }
 * </pre>
 * 
 * </p>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://x-stream.github.io/faq.html">Frequently Asked Questions</a>
 * @see <a href="https://x-stream.github.io/news.html">News</a>
 * @see "org.apache.solr.common.util.DOMUtil"
 * @see "org.springframework.util.xml.DomUtils"
 * @since 3.0.0
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XmlUtil{

    /**
     * 根据xpath表达式, 来获得节点的名字和string value map.
     * 
     * <p>
     * 按照顺序返回
     * </p>
     *
     * @param xml
     *            the xml
     * @param xpathExpression
     *            the xpath expression
     * @return 如果 <code>xml</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 3.0.0
     */
    public static Map<String, String> getNodeNameAndStringValueMap(Object xml,String xpathExpression){
        return getNodeMap(
                        xml,
                        xpathExpression, //
                        (map,node) -> map.put(node.getNodeName(), node.getTextContent()));
    }

    /**
     * 获得 node attribute value and string value map.
     *
     * @param xml
     *            the xml
     * @param xpathExpression
     *            the xpath expression
     * @param nodeAttributeName
     *            the node attribute name
     * @return 如果 <code>xml</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 3.0.0
     */
    public static Map<String, String> getNodeAttributeValueAndStringValueMap(
                    Object xml,
                    String xpathExpression,
                    final String nodeAttributeName){
        return getNodeMap(
                        xml,
                        xpathExpression, //
                        (map,node) -> map.put(getAttributeValue(node, nodeAttributeName), node.getTextContent()));

    }

    /**
     * Gets the node map.
     *
     * @param xml
     *            the xml
     * @param xpathExpression
     *            the xpath expression
     * @param hook
     *            the hook
     * @return 如果 <code>xml</code> 是null,抛出 {@link NullPointerException}<br>
     */
    private static Map<String, String> getNodeMap(Object xml,String xpathExpression,Hook hook){
        Document document = FeilongDocumentBuilder.buildDocument(xml);
        NodeList nodeList = XPathUtil.evaluate(document, xpathExpression, XPathConstants.NODESET);
        if (null == nodeList || 0 == nodeList.getLength()){
            if (log.isInfoEnabled()){
                log.info("use xpathExpression:[{}],from xml:[{}], can not find Node,return emptyMap", xpathExpression, format(document));
            }
            return emptyMap();
        }
        //---------------------------------------------------------------
        Map<String, String> map = newLinkedHashMap();
        for (int i = 0, j = nodeList.getLength(); i < j; ++i){
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()){
                hook.hook(map, node);
            }
        }
        //---------------------------------------------------------------
        if (log.isTraceEnabled()){
            log.trace("nameAndValueMap:{}", formatToSimpleTable(map));
        }
        return map;
    }

    //---------------------------------------------------------------

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
    private static String getAttributeValue(Node node,String attributeName){
        Validate.notNull(node, "node can't be null!");
        Validate.notBlank(attributeName, "attributeName can't be null/empty!");

        //---------------------------------------------------------------
        NamedNodeMap namedNodeMap = node.getAttributes();
        Node currentNode = namedNodeMap.getNamedItem(attributeName);
        if (null != currentNode){
            return currentNode.getNodeValue();
        }
        return EMPTY;
    }

    //---------------------------------------------------------------

    /**
     * Format.
     *
     * @param xml
     *            the xml
     * @return the string
     * @since 3.0.0
     */
    public static String format(String xml){
        Node node = FeilongDocumentBuilder.buildDocument(xml);
        return format(node);
    }

    /**
     * Format.
     *
     * @param node
     *            the node
     * @return the string
     * @throws UncheckedXmlParseException
     *             the unchecked xml parse exception
     * @since 3.0.0
     */
    private static String format(Node node){
        try{
            Writer writer = new StringWriter();
            StreamResult streamResult = new StreamResult(writer);

            TransformerBuilder.DEFAULT_TRANSFORMER.transform(//
                            new DOMSource(node),
                            streamResult);

            return writer.toString();
        }catch (TransformerException e){
            throw new UncheckedXmlParseException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 将 map 转成 xml string.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * Map{@code <String, String>} map = new HashMap{@code <>}();
     * map.put("out_trade_no", "112122212");
     * map.put("total_fee", "125.00");
     * map.put("call_back_url", "");
     * map.put("notify_url", "");
     * 
     * log.debug(XmlUtil.toXML(map, "xml"));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {@code 
     *     <xml>
     *       <call_back_url></call_back_url>
     *       <total_fee>125.00</total_fee>
     *       <notify_url></notify_url>
     *       <out_trade_no>112122212</out_trade_no>
     *     </xml>
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map
     * @param rootElementName
     *            根元素名字
     * @return 如果 <code>rootName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>rootName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>map</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>map</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>map</code> 有 null key,将抛出 异常<br>
     *         如果 <code>map</code> 有 null value,将转成 {@link StringUtils#EMPTY}代替<br>
     * 
     * @see SimpleMapConverter
     * @since 1.10.7
     */
    public static <K, V> String toXML(Map<K, V> map,String rootElementName){
        return toXML(map, rootElementName, true);
    }

    /**
     * 将 map 转成 xml 字符串.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>首先自己项目需要依赖 xstream,<span style="color:green">版本号自行使用最新的版本</span>
     * 
     * <pre class="code">
     * {@code
     <dependency>
            <groupId>com.thoughtworks.xstream</groupId>
            <artifactId>xstream</artifactId>
            <version>1.4.20</version> 
          </dependency>
     * }
     * </pre>
     * 
     * </li>
     * </ol>
     * </blockquote>
     * 
     * 
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * Map{@code <String, String>} map = new HashMap{@code <>}();
     * map.put("out_trade_no", "112122212");
     * map.put("total_fee", "125.00");
     * map.put("call_back_url", "");
     * map.put("notify_url", "");
     * 
     * log.debug(XmlUtil.toXML(map, "xml",true));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {@code 
     *     <xml>
     *       <call_back_url></call_back_url>
     *       <total_fee>125.00</total_fee>
     *       <notify_url></notify_url>
     *       <out_trade_no>112122212</out_trade_no>
     *     </xml>
     * }
     * </pre>
     * 
     * 如果此时,使用 <code>isPrettyPrint</code> 为 <span style="color:red">false</span> 参数
     * 
     * <pre class="code">
     * Map{@code <String, String>} map = new HashMap{@code <>}();
     * map.put("out_trade_no", "112122212");
     * map.put("total_fee", "125.00");
     * map.put("call_back_url", "");
     * map.put("notify_url", "");
     * 
     * log.debug(XmlUtil.toXML(map, "xml",false));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {@code 
    <xml><call_back_url></call_back_url><total_fee>125.00</total_fee><notify_url></notify_url><out_trade_no>112122212</out_trade_no></xml>
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param <K>
     *            the key type
     * @param <V>
     *            the value type
     * @param map
     *            the map
     * @param rootElementName
     *            根元素名字
     * @param isPrettyPrint
     *            是否格式化输出
     * @return 如果 <code>rootName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>rootName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>map</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>map</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>map</code> 有 null key,将抛出 异常<br>
     *         如果 <code>map</code> 有 null value,将转成 {@link StringUtils#EMPTY}代替<br>
     * @see SimpleMapConverter
     */
    public static <K, V> String toXML(Map<K, V> map,String rootElementName,boolean isPrettyPrint){
        Validate.notEmpty(map, "map can't be null/empty!");
        Validate.notBlank(rootElementName, "rootName can't be blank!");

        XStreamConfig xStreamConfig = XStreamConfigBuilder.buildSimpleMapXStreamConfig(rootElementName, isPrettyPrint);
        // xStreamConfig.getDefaultImplementationMap().put(map.getClass(), Map.class)

        if (map.getClass() != HashMap.class){
            xStreamConfig.getAliasMap().put(rootElementName, map.getClass());
        }
        return toXML(map, xStreamConfig);
    }

    /**
     * 将object转成xml字符串.
     * 
     * <h3>使用示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 对于将下面的对象,转成XML<br>
     * User user = new User(1L);
     * </p>
     * 
     * <p>
     * <b>1.不带XStreamConfig参数</b> <br>
     * 使用 {@code com.feilong.xml.XmlUtil#toXML(user, null)},则返回
     * 
     * <pre class="code">
     * {@code
     *  <com.feilong.test.User>
     *   <name>feilong</name>
     *   <id>1</id>
     *   <userInfo/>
     *   <userAddresseList/>
     * </com.feilong.test.User>
     * }
     * </pre>
     * 
     * </p>
     * 
     * 
     * <p>
     * 
     * <b>2.设置 Alias 参数</b> <br>
     * 可以看到上面的结果中,XML Root元素名字是 com.feilong.test.User,如果只是显示成 {@code <user>}怎么做呢？<br>
     * 使用
     * 
     * <code>
     * <pre class="code">
     *     User user = new User(1L);
     *     XStreamConfig xStreamConfig = new XStreamConfig();
     *     xStreamConfig.getAliasMap().put(&quot;user&quot;, User.class);
     *     log.info(XmlUtil.toXML(user, xStreamConfig));
     * </pre>
     * </code>
     * 
     * ,则返回
     * 
     * <pre class="code">
     * {@code
     * <user>
     *   <name>feilong</name>
     *   <id>1</id>
     *   <userInfo/>
     *   <userAddresseList/>
     * </user>
     * }
     * </pre>
     * 
     * </p>
     * <p>
     * 
     * <b>3.设置 ImplicitCollection 参数</b><br>
     * 如果我在结果不想出现 {@code <userAddresseList/>}怎么做呢？<br>
     * 使用
     * 
     * <code>
     * <pre class="code">
     *     User user = new User(1L);
     *     XStreamConfig xStreamConfig = new XStreamConfig();
     *     xStreamConfig.getAliasMap().put(&quot;user&quot;, User.class);
     *     xStreamConfig.getImplicitCollectionMap().put(&quot;userAddresseList&quot;, User.class);
     *     log.info(XmlUtil.toXML(user, xStreamConfig));
     * </pre>
     * </code>
     * 
     * ,则返回
     * 
     * <pre class="code">
     * {@code
     *  <user>
     *   <name>feilong</name>
     *   <id>1</id>
     *   <userInfo/>
     * </user>
     * }
     * </pre>
     * 
     * </p>
     * </blockquote>
     * 
     * @param bean
     *            the obj
     * @param xStreamConfig
     *            the to xml config
     * @return 如果 <code>bean</code> 是null,抛出 {@link NullPointerException}<br>
     * @see XStream#toXML(Object)
     * @see XStream#alias(String, Class)
     * @see XStream#addImplicitCollection(Class, String)
     */
    public static String toXML(Object bean,XStreamConfig xStreamConfig){
        Validate.notNull(bean, "bean can't be null!");

        if (log.isDebugEnabled()){
            String pattern = "class:[{}],bean:[{}],xStreamConfig:[{}]";
            log.debug(pattern, bean.getClass().getSimpleName(), JsonUtil.toString(bean), JsonUtil.toString(xStreamConfig));
        }
        return XStreamUtil.toXML(bean, xStreamConfig);
    }

    //---------------------------------------------------------------

    /**
     * 将{@code xml}字符串转成 Map.
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>
     * 首先自己项目需要依赖 xstream,<span style="color:green">版本号自行使用最新的版本</span>
     * 
     * <pre class="code">
     * {@code
     <dependency>
            <groupId>com.thoughtworks.xstream</groupId>
            <artifactId>xstream</artifactId>
            <version>1.4.20</version> 
          </dependency>
     * }
     * </pre>
     * 
     * </li>
     * <li>内部封装了 {@link XStream#ignoreUnknownElements()} 忽略了不存在的字段</li>
     * </ol>
     * </blockquote>
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * String xml = "{@code <xml><return_code><![CDATA[SUCCESS]]></return_code>\r\n" + "<return_msg><![CDATA[OK]]></return_msg>\r\n"
     *                 + "<appid><![CDATA[wx2cc3b3d8bb8df520]]></appid>\r\n" + "<mch_id><![CDATA[1239453402]]></mch_id>\r\n"
     *                 + "<sub_mch_id><![CDATA[]]></sub_mch_id>\r\n" + "<nonce_str><![CDATA[I1dy6p9hOy324Q2M]]></nonce_str>\r\n"
     *                 + "<sign><![CDATA[288AE0E455273102147B9CF95F43D222]]></sign>\r\n" + "<result_code><![CDATA[SUCCESS]]></result_code>\r\n"
     *                 + "</xml>}";
     * 
     * Map{@code <String, String>} map = XmlUtil.fromXML(xml, "xml");
     * log.debug(JsonUtil.format(map));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    {
            "result_code": "SUCCESS",
            "sign": "288AE0E455273102147B9CF95F43D222",
            "mch_id": "1239453402",
            "sub_mch_id": "",
            "return_msg": "OK",
            "appid": "wx2cc3b3d8bb8df520",
            "nonce_str": "I1dy6p9hOy324Q2M",
            "return_code": "SUCCESS"
        }
     * </pre>
     * 
     * </blockquote>
     *
     * @param xml
     *            the xml
     * @param rootElementName
     *            the root element name
     * @return 如果 <code>rootElementName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>rootElementName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>xml</code> 是null,返回 null<br>
     * @since 3.0.0 change name from fromXMl
     */
    public static Map<String, String> toMap(String xml,String rootElementName){
        Validate.notBlank(rootElementName, "rootElementName can't be blank!");
        if (isNullOrEmpty(xml)){
            return null;
        }
        return toBean(xml, XStreamConfigBuilder.buildSimpleMapXStreamConfig(rootElementName));
    }

    /**
     * 将 <code>xml</code> 字符串,转成带注解的 <code>processAnnotationsType</code> 对象.
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>内部封装了 {@link XStream#ignoreUnknownElements()} 忽略了不存在的字段</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * 已知从微信得到下列xml
     * 
     * <pre class="code">
     * {@code 
     <xml>
         <return_code><![CDATA[SUCCESS]]></return_code>
         <return_msg><![CDATA[OK]]></return_msg>
         <appid><![CDATA[wx2cc3b3d8333bb8df520]]></appid>
         <mch_id><![CDATA[1239453333402]]></mch_id>
         <sub_mch_id><![CDATA[]]></sub_mch_id>
         <nonce_str><![CDATA[I1dy6333p9hOy324Q2M]]></nonce_str>
         <sign><![CDATA[288AE0E455333273102147B9CF95F43D222]]></sign>
         <result_code><![CDATA[SUCCESS]]></result_code>
     </xml>
     }
     * </pre>
     * 
     * 需要转换成下列的 WechatCloseResponse 对象
     * 
     * <pre>
    
    {@code @XStreamAlias("xml")}
    public class WechatCloseResponse{
    
        <span style="color:green">//** 返回状态码 return_code 是 String(16) SUCCESS SUCCESS/FAIL. </span>
        {@code @XStreamAlias("return_code")}
        private String return_code;
    
        <span style="color:green">//** 返回信息 return_msg 否 String(128).</span>
        {@code @XStreamAlias("return_msg")}
        private String return_msg;
    
        <span style="color:green">//** 公众账号ID appid 是 String(32) wx8888888888888888 微信分配的公众账号ID. </span>
        {@code @XStreamAlias("appid")}
        private String appid;
    
       <span style="color:green">//** 商户号 mch_id 是 String(32) 1900000109 微信支付分配的商户号. </span>
        {@code @XStreamAlias("mch_id")}
        private String mch_id;
    
        <span style="color:green">//** 随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 随机字符串，不长于32位。推荐随机数生成算法.</span>
        {@code @XStreamAlias("nonce_str")}
        private String nonce_str;
    
        <span style="color:green">//** 签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 签名，验证签名算.</span>
        //@XStreamAsAttribute
        {@code @XStreamAlias("sign")}
        private String sign;
    
        <span style="color:green">//** 业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL. </span>
        {@code @XStreamAlias("result_code")}
        private String result_code;
    
        <span style="color:green">//** 业务结果描述 result_msg 是 String(32) OK 对于业务执行的详细描述. </span>
        {@code @XStreamAlias("result_msg")}
        private String result_msg;
    
        <span style="color:green">//** 错误代码 err_code 否 String(32) SYSTEMERROR 详细参见第6节错误列表.</span>
        {@code @XStreamAlias("err_code")}
        private String err_code;
    
        <span style="color:green">//** 错误代码描述 err_code_des 否 String(128) 系统错误 结果信息描述. </span>
        {@code @XStreamAlias("err_code_des")}
        private String err_code_des;
        
        // setter/getter 省略
        
    }
     * </pre>
     * 
     * 你可以
     * 
     * <pre>
     * String xml = {@code "<xml><return_code><![CDATA[SUCCESS]]></return_code>\r\n" + "<return_msg><![CDATA[OK]]></return_msg>\r\n"
     *                     + "<appid><![CDATA[wx2cc3b3d8bb8df520]]></appid>\r\n" + "<mch_id><![CDATA[1239453402]]></mch_id>\r\n"
     *                     + "<sub_mch_id><![CDATA[]]></sub_mch_id>\r\n" + "<nonce_str><![CDATA[I1dy6p9hOy324Q2M]]></nonce_str>\r\n"
     *                     + "<sign><![CDATA[288AE0E455273102147B9CF95F43D222]]></sign>\r\n"
     *                     + "<result_code><![CDATA[SUCCESS]]></result_code>\r\n" + "</xml>";}
     * 
     * <span style="color:red">WechatCloseResponse map = XmlUtil.fromXML(xml, WechatCloseResponse.class);</span>
     * log.debug("{}", JsonUtil.format(map));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {
     * "sign": "288AE0E455273102147B9CF95F43D222",
     * "result_code": "SUCCESS",
     * "mch_id": "1239453402",
     * "err_code": "",
     * "result_msg": "",
     * "return_msg": "OK",
     * "err_code_des": "",
     * "appid": "wx2cc3b3d8bb8df520",
     * "return_code": "SUCCESS",
     * "nonce_str": "I1dy6p9hOy324Q2M"
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param xml
     *            the xml
     * @param processAnnotationsType
     *            the process annotations type
     * @return 如果 <code>xml</code> 是null,返回 null<br>
     *         如果 <code>processAnnotationsType</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 3.0.0 change name from fromXMl
     */
    public static <T> T toBean(String xml,Class<T> processAnnotationsType){
        Validate.notNull(processAnnotationsType, "processAnnotationsType can't be blank!");
        if (isNullOrEmpty(xml)){
            return null;
        }
        return toBean(xml, new XStreamConfig(processAnnotationsType));
    }

    /**
     * From xml.
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>内部封装了 {@link XStream#ignoreUnknownElements()} 忽略了不存在的字段</li>
     * </ol>
     * </blockquote>
     * 
     * @param <T>
     *            the generic type
     * @param xml
     *            the xml
     * @param xStreamConfig
     *            the x stream config
     * @return 如果 <code>xml</code> 是null或者是empty,返回 null<br>
     * @since 3.0.0 change name from fromXMl
     */
    public static <T> T toBean(String xml,XStreamConfig xStreamConfig){
        if (log.isDebugEnabled()){
            log.debug("input params info,xml:[{}],xStreamConfig:[{}]", xml, JsonUtil.toString(xStreamConfig));
        }
        //---------------------------------------------------------------
        if (isNullOrEmpty(xml)){
            return null;
        }
        return XStreamUtil.toBean(xml, xStreamConfig);
    }
}
