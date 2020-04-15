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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.feilong.json.jsonlib.JsonUtil;

public class GetNodeListByXPathTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GetNodeListByXPathTest.class);

    @Test
    public void testGetNodeByXPath22(){
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + "<RESULT>\r\n" + "    <INPOLIST>\r\n"
                        + "        <TXN_ID>2014000000000004</TXN_ID>\r\n" + "        <TYPE1>ACC</TYPE1>\r\n"
                        + "        <TYPE2>001</TYPE2>\r\n" + "        <TXN_DT>2014-06-30</TXN_DT>\r\n"
                        + "        <TXN_AMT>129.86</TXN_AMT>\r\n" + "        <TXN_STATCD>003</TXN_STATCD>\r\n"
                        + "        <TXN_CHNL_CD>DAX1</TXN_CHNL_CD>\r\n" + "        <TXN_CHNL_NM />\r\n" + "        <TXN_ETC1 />\r\n"
                        + "        <TXN_ETC2 />\r\n" + "        <TXN_ETC3 />\r\n" + "        <TXN_ETC4 />\r\n" + "        <TXN_ETC5 />\r\n"
                        + "    </INPOLIST>\r\n" + "    <INPOLIST>\r\n" + "        <TXN_ID>2014000000000005</TXN_ID>\r\n"
                        + "        <TYPE1>ACC</TYPE1>\r\n" + "        <TYPE2>001</TYPE2>\r\n" + "        <TXN_DT>2014-06-30</TXN_DT>\r\n"
                        + "        <TXN_AMT>10250.88</TXN_AMT>\r\n" + "        <TXN_STATCD>003</TXN_STATCD>\r\n"
                        + "        <TXN_CHNL_CD>DAX1</TXN_CHNL_CD>\r\n" + "        <TXN_CHNL_NM />\r\n" + "        <TXN_ETC1 />\r\n"
                        + "        <TXN_ETC2 />\r\n" + "        <TXN_ETC3 />\r\n" + "        <TXN_ETC4 />\r\n" + "        <TXN_ETC5 />\r\n"
                        + "    </INPOLIST>    \r\n" + "</RESULT>";
        DomParser domUtil2 = new DomParser(xmlString, UTF8);
        NodeList replacesNodeList = domUtil2.getNodeListByXPath("/RESULT/INPOLIST");

        Map<String, String> _replaceMap = newLinkedHashMap();
        for (int i = 0, j = replacesNodeList.getLength(); i < j; ++i){
            Node node = replacesNodeList.item(i);

            if (Node.ELEMENT_NODE == node.getNodeType()){
                _replaceMap.put(node.getNodeName(), node.getTextContent());
            }
        }

        LOGGER.debug(JsonUtil.format(_replaceMap));
        LOGGER.debug(JsonUtil.format(domUtil2.getNodeNameAndStringValueMap("/RESULT/INPOLIST/*")));
    }
}
