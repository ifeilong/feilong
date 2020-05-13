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
package com.feilong.context.converter.builder;

import java.util.Map;

import com.feilong.lib.lang3.Validate;
import com.feilong.xml.XmlUtil;

/**
 * 标准的Xpath表达式 xml解析器.
 * 
 * <p>
 * key/value 取自 每个 Node 的Name/Value;
 * </p>
 * 
 * <h3>about xpathExpression:</h3>
 * 
 * 示例:
 * <blockquote>
 * 
 * <pre class="code">
 * {@code
 * <item>
 *  <categoryId/>
 *  <title><![CDATA[test pro<duct name]]></title>
 *  <price>0</price>
 *  <listPrice>200000</listPrice>
 *  <state>10</state>
 *  <city>1010</city>
 *  <district>101004</district>
 *  <weight>1.2</weight>
 *  <length>23</length>
 *  <width>56</width>
 *  <height>5</height>
 *  <buyerObtainPoint>20</buyerObtainPoint>
 *  <freightPayer>buyer</freightPayer>
 *  <postageId>-1</postageId>
 *  <expressFee>5</expressFee>
 *  <description>test product</description>
 *  <properties>1004:2586,12536:15248,58746:2546</properties>
 *  <supportPOD>false</supportPOD>
 *  <podFee/>
 *  <timing/>
 *  <listTime/>
 * </item>
 * }
 * </pre>
 * 
 * if String xpathExpression = "/item/*";
 * 
 * 会取到下面所有的元素节点
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.2
 */
public class XmlNodeNameAndValueMapBuilder implements NameAndValueMapBuilder{

    /** The xpath expression . */
    private String xpathExpression;

    //---------------------------------------------------------------

    /**
     * Instantiates a new xml node name and value map builder.
     */
    public XmlNodeNameAndValueMapBuilder(){
        super();
    }

    /**
     * Instantiates a new xml node name and value map builder.
     *
     * @param xpathExpression
     *            the xpath expression
     */
    public XmlNodeNameAndValueMapBuilder(String xpathExpression){
        super();
        this.xpathExpression = xpathExpression;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.bind.builder.NameAndValueMapBuilder#buildNameAndValueMap(java.lang.String)
     */
    @Override
    public Map<String, String> build(String inputString){
        Validate.notBlank(inputString, "xml can't be blank!");
        return XmlUtil.getNodeNameAndStringValueMap(inputString, xpathExpression);
    }

    //---------------------------------------------------------------

    /**
     * 获得 xpath expression .
     *
     * @return the xpathExpression
     */
    public String getXpathExpression(){
        return xpathExpression;
    }

    /**
     * 设置 xpath expression .
     *
     * @param xpathExpression
     *            the xpathExpression to set
     */
    public void setXpathExpression(String xpathExpression){
        this.xpathExpression = xpathExpression;
    }

}
