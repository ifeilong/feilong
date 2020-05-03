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
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.feilong.xml.XmlUtil;

public class ToXmlMapIsPrettyPrintFalseTest{

    @Test
    public void testToXMLLinkedHashMap(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("out_trade_no", "112122212");
        map.put("total_fee", "125.00");
        map.put("call_back_url", "");
        map.put("notify_url", "");

        //linked-hash-map

        assertEquals(
                        "<xml><out_trade_no>112122212</out_trade_no><total_fee>125.00</total_fee><call_back_url></call_back_url><notify_url></notify_url></xml>",
                        XmlUtil.toXML(map, "xml", false));
    }

    @Test
    public void testToXMLLinkedHashMap22(){
        Map<String, String> map = new LinkedHashMap<>();
        map.put("out_trade_no", "112122212");
        map.put("total_fee", "125.00");
        map.put("call_back_url", "");
        map.put("notify_url", null);

        //linked-hash-map

        assertEquals(
                        "<xml><out_trade_no>112122212</out_trade_no><total_fee>125.00</total_fee><call_back_url></call_back_url><notify_url></notify_url></xml>",
                        XmlUtil.toXML(map, "xml", false));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testToXmlMapIsPrettyPrintFalseTestNull(){
        XmlUtil.toXML(null, "xml", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToXmlMapIsPrettyPrintFalseTestEmpty(){
        XmlUtil.toXML(emptyMap(), "xml", false);
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testToXmlMapIsPrettyPrintFalseTestNull1(){
        XmlUtil.toXML(toMap("key", "123"), null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToXmlMapIsPrettyPrintFalseTestEmpty22(){
        XmlUtil.toXML(toMap("key", "123"), "", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToXmlMapIsPrettyPrintFalseTestBlank333(){
        XmlUtil.toXML(toMap("key", "123"), "  ", false);
    }

    @Test(expected = Exception.class)
    public void testToXmlMapIsPrettyPrintFalseTestBlank333222(){
        XmlUtil.toXML(toMap(null, "123"), "222", false);
    }

}