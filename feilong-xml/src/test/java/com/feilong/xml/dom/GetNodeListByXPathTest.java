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

import java.util.Map;

import org.junit.Test;

import com.feilong.json.JsonUtil;
import com.feilong.test.AbstractTest;
import com.feilong.xml.XmlUtil;

public class GetNodeListByXPathTest extends AbstractTest{

    @Test
    public void test(){
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

        Map<String, String> map = XmlUtil.getNodeNameAndStringValueMap(xmlString, "/RESULT/INPOLIST/*");
        LOGGER.debug(JsonUtil.format(map));
    }

}
