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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Map;

import org.junit.Test;

import com.feilong.store.member.User;
import com.feilong.test.AbstractTest;
import com.feilong.xml.XmlUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;

@lombok.extern.slf4j.Slf4j
public class ToXmlTest extends AbstractTest{

    @Test
    public void name(){
        User user = new User(1L);

        XStream xStream = new XStream(new DomDriver(UTF8, new NoNameCoder()));
        xStream.alias("user", User.class);

        log.debug(xStream.toXML(user));
    }

    //---------------------------------------------------------------

    @Test
    public void name1(){
        //        <com.feilong.store.member.User>
        //        <id>1</id>
        //        <name>feilong</name>
        //        <ageInt>0</ageInt>
        //        <userInfo/>
        //        <userAddresseList/>
        //      </com.feilong.store.member.User>

        User user = new User(1L);
        log.debug(XmlUtil.toXML(user, null));
    }

    @Test
    public void name222(){
        //        <list>
        //        <com.feilong.store.member.User>
        //          <id>1</id>
        //          <name>feilong</name>
        //          <ageInt>0</ageInt>
        //          <userInfo/>
        //          <userAddresseList/>
        //        </com.feilong.store.member.User>
        //      </list>

        log.debug(XmlUtil.toXML(toList(new User(1L)), null));
    }

    @Test
    public void testToXML3(){
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
        log.debug(XmlUtil.toXML(map, (XStreamConfig) null));
    }

    @Test
    public void testToXML(){
        User user = new User(1L);

        XStreamConfig toXmlConfig = new XStreamConfig();
        toXmlConfig.getAliasMap().put("user", User.class);

        log.debug(XmlUtil.toXML(user, toXmlConfig));
        log.debug(XmlUtil.toXML(user, null));
    }
}