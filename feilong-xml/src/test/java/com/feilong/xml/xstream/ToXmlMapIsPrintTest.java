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
package com.feilong.xml.xstream;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.xml.XmlUtil;

public class ToXmlMapIsPrintTest{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ToXmlMapIsPrintTest.class);

    @Test
    public void testToXML(){

        //        <map>
        //        <entry>
        //          <string>call_back_url</string>
        //          <string></string>
        //        </entry>
        //        <entry>
        //          <string>total_fee</string>
        //          <string>125.00</string>
        //        </entry>
        //        <entry>
        //          <string>notify_url</string>
        //          <string></string>
        //        </entry>
        //        <entry>
        //          <string>out_trade_no</string>
        //          <string>112122212</string>
        //        </entry>
        //      </map>

        Map<String, String> map = newHashMap();
        map.put("out_trade_no", "112122212");
        map.put("total_fee", "125.00");
        map.put("call_back_url", "");
        map.put("notify_url", "");

        XStreamConfig toXmlConfig = new XStreamConfig();
        // toXmlConfig.getAliasMap().put("xml", Map.class);
        toXmlConfig.getAliasMap().put("xml", map.getClass());

        //  toXmlConfig.setHierarchicalStreamDriver(new DomJDriver());

        LOGGER.debug(XmlUtil.toXML(map, toXmlConfig));
    }

    @Test
    public void testToXML1Null(){

        //        <xml>
        //        <call_back_url></call_back_url>
        //        <total_fee>&lt;name&gt;</total_fee>
        //        <notify_url></notify_url>
        //        <out_trade_no>112122212</out_trade_no>
        //      </xml>

        Map<String, String> map = newHashMap();
        map.put("out_trade_no", "112122212");
        map.put("total_fee", "<name>");
        map.put("call_back_url", "");
        map.put("notify_url", null);

        LOGGER.debug(XmlUtil.toXML(map, "xml"));
    }

    @Test
    public void testToXML1Null2(){

        //        <xml>
        //        <call_back_url></call_back_url>
        //        <total_fee>125.00</total_fee>
        //        <notify_url>{name=name}</notify_url>
        //        <out_trade_no>112122212</out_trade_no>
        //      </xml>

        Map<String, Object> map = newHashMap();
        map.put("out_trade_no", "112122212");
        map.put("total_fee", "125.00");
        map.put("call_back_url", "");
        map.put("notify_url", toMap("name", "name"));

        LOGGER.debug(XmlUtil.toXML(map, "xml"));
    }
}