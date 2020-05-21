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

import com.feilong.core.Validate;
import com.feilong.xml.XmlUtil;

/**
 * 提取xml node属性值和string value 填充到bean里面去.
 * 
 * <pre class="code">
 *  {@code
 *      
 *  <wddxPacket version="1.0">
 *  <header/>
 *  <data>
 *      <struct>
 *          <var name="TRANSACTIONID">
 *              <string>868BBC35-A5D1-FCBF-0453F134C99B5553</string>
 *          </var>
 *          <var name="ACQUIRERRESPONSECODE">
 *              <string>000</string>
 *          </var>
 *          <var name="SCRUBMESSAGE">
 *              <string/>
 *          </var>
 *          <var name="AMOUNT">
 *              <number>9011999.0</number>
 *          </var>
 *          <var name="SERVICEVERSION">
 *              <string>2.0</string>
 *          </var>
 *          <var name="TRANSACTIONSCRUBCODE">
 *              <string/>
 *          </var>
 *          <var name="MERCHANTTRANSACTIONID">
 *              <string>010003170001</string>
 *          </var>
 *          <var name="CURRENCY">
 *              <string>IDR</string>
 *          </var>
 *          <var name="TRANSACTIONSTATUS">
 *              <string>APPROVED</string>
 *          </var>
 *          <var name="SITEID">
 *              <string>Blanja2</string>
 *          </var>
 *          <var name="TRANSACTIONDATE">
 *              <string>2014-04-23 15:19:27</string>
 *          </var>
 *          <var name="ACQUIRERCODE">
 *              <string>AUTH20140423152019PM</string>
 *          </var>
 *          <var name="SCRUBCODE">
 *              <string/>
 *          </var>
 *          <var name="TRANSACTIONSCRUBMESSAGE">
 *              <string/>
 *          </var>
 *          <var name="ACQUIRERAPPROVALCODE">
 *              <string>298883</string>
 *          </var>
 *          <var name="TRANSACTIONTYPE">
 *              <string>AUTHORIZATION</string>
 *          </var>
 *      </struct>
 *  </data>
 * </wddxPacket>
 *  }
 * </pre>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.2
 */
public class XmlNodeAttributeValueAndValueMapBuilder implements NameAndValueMapBuilder{

    /** The xpath expression. */
    private String xpathExpression;

    /** The nodeAttributeName. */
    private String nodeAttributeName;

    //---------------------------------------------------------------

    /**
     * Instantiates a new xml node attribute value and value map builder.
     */
    public XmlNodeAttributeValueAndValueMapBuilder(){
        super();
    }

    /**
     * Instantiates a new xml node attribute value and value map builder.
     *
     * @param xpathExpression
     *            the xpath expression
     * @param nodeAttributeName
     *            the node attribute name
     */
    public XmlNodeAttributeValueAndValueMapBuilder(String xpathExpression, String nodeAttributeName){
        super();
        this.xpathExpression = xpathExpression;
        this.nodeAttributeName = nodeAttributeName;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.bind.builder.NameAndValueMapBuilder#buildNameAndValueMap(java.lang.String)
     */
    @Override
    public Map<String, String> build(String xml){
        Validate.notBlank(xml, "xml can't be blank!");
        return XmlUtil.getNodeAttributeValueAndStringValueMap(xml, xpathExpression, nodeAttributeName);
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

    /**
     * 获得 nodeAttributeName.
     *
     * @return the nodeAttributeName
     */
    public String getNodeAttributeName(){
        return nodeAttributeName;
    }

    /**
     * 设置 nodeAttributeName.
     *
     * @param nodeAttributeName
     *            the nodeAttributeName to set
     */
    public void setNodeAttributeName(String nodeAttributeName){
        this.nodeAttributeName = nodeAttributeName;
    }

}
