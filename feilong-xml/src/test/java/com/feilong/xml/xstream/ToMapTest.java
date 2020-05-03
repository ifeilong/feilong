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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import com.feilong.xml.XmlUtil;

public class ToMapTest{

    private static final String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code>\r\n"
                    + "<return_msg><![CDATA[OK]]></return_msg>\r\n" + "<appid><![CDATA[wx2cc3b3d8bb8df520]]></appid>\r\n"
                    + "<mch_id><![CDATA[1239453402]]></mch_id>\r\n" + "<sub_mch_id><![CDATA[]]></sub_mch_id>\r\n"
                    + "<nonce_str><![CDATA[I1dy6p9hOy324Q2M]]></nonce_str>\r\n"
                    + "<sign><![CDATA[288AE0E455273102147B9CF95F43D222]]></sign>\r\n" + "<result_code><![CDATA[SUCCESS]]></result_code>\r\n"
                    + "</xml>";

    @Test
    public void test(){
        Map<String, String> map = XmlUtil.toMap(xml, "xml");
        assertThat(
                        map,
                        allOf(
                                        hasEntry("return_code", "SUCCESS"),
                                        hasEntry("return_msg", "OK"),
                                        hasEntry("appid", "wx2cc3b3d8bb8df520"),
                                        hasEntry("mch_id", "1239453402"),
                                        hasEntry("sub_mch_id", ""),
                                        hasEntry("nonce_str", "I1dy6p9hOy324Q2M"),
                                        hasEntry("sign", "288AE0E455273102147B9CF95F43D222"),
                                        hasEntry("result_code", "SUCCESS")));
    }

    //---------------------------------------------------------------

    @Test
    public void test1(){
        assertNull(XmlUtil.toMap(null, "xml"));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testFromXmlMapTestNull(){
        XmlUtil.toMap(xml, (String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromXmlMapTestEmpty(){
        XmlUtil.toMap(xml, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromXmlMapTestBlank(){
        XmlUtil.toMap(xml, " ");
    }

}