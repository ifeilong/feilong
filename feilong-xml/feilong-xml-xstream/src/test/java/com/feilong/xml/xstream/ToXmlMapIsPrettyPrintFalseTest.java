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

import static com.feilong.core.util.MapUtil.newHashMap;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Ignore;
import org.junit.Test;

public class ToXmlMapIsPrettyPrintFalseTest{

    @Test
    @Ignore
    public void testToXMLConcurrentHashMap(){
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("out_trade_no", "112122212");
        map.put("total_fee", "125.00");
        map.put("call_back_url", "");
        map.put("notify_url", "");

        assertEquals(
                        "<xml><out_trade_no>112122212</out_trade_no><call_back_url></call_back_url><notify_url></notify_url><total_fee>125.00</total_fee></xml>",
                        XStreamUtil.toXML(map, "xml", false));
    }

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
                        XStreamUtil.toXML(map, "xml", false));
    }

    @Test()
    @Ignore
    public void testToXMLHashMap(){
        Map<String, String> map = newHashMap();
        map.put("out_trade_no", "112122212");
        map.put("total_fee", "125.00");
        map.put("call_back_url", "");
        map.put("notify_url", "");

        assertEquals(
                        "<xml><call_back_url></call_back_url><total_fee>125.00</total_fee><notify_url></notify_url><out_trade_no>112122212</out_trade_no></xml>",
                        XStreamUtil.toXML(map, "xml", false));
    }
}