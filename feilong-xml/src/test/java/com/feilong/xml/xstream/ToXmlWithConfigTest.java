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

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.store.member.User;
import com.feilong.test.AbstractTest;
import com.feilong.xml.XmlUtil;

public class ToXmlWithConfigTest extends AbstractTest{

    @Test
    public void name2(){
        //        <userList>
        //        <user>
        //          <id>1</id>
        //          <name>feilong</name>
        //          <ageInt>0</ageInt>
        //          <userInfo/>
        //          <userAddresseList/>
        //        </user>
        //      </userList>

        List<User> list = newArrayList();

        User user = new User(1L);
        list.add(user);

        XStreamConfig toXmlConfig = new XStreamConfig();
        Map<String, Class<?>> aliasMap = toXmlConfig.getAliasMap();
        aliasMap.put("userList", List.class);
        aliasMap.put("user", User.class);

        LOGGER.debug(XmlUtil.toXML(list, toXmlConfig));
    }

    /**
     * Test to xm l2.
     */
    @Test
    public void testToXML2(){

        //        <user>
        //        <id>1</id>
        //        <name>feilong</name>
        //        <ageInt>0</ageInt>
        //        <userInfo/>
        //      </user>

        User user = new User(1L);

        XStreamConfig toXmlConfig = new XStreamConfig();
        toXmlConfig.getAliasMap().put("user", User.class);
        toXmlConfig.getImplicitCollectionMap().put("userAddresseList", User.class);

        LOGGER.debug(XmlUtil.toXML(user, toXmlConfig));
    }

    @Test
    public void testToXML3445(){
        //        <xml>
        //        <id>1</id>
        //        <name>feilong</name>
        //        <ageInt>0</ageInt>
        //        <userInfo/>
        //        <userAddresseList/>
        //      </xml>

        User user = new User(1L);

        XStreamConfig toXmlConfig = new XStreamConfig();
        toXmlConfig.getAliasMap().put("xml", user.getClass());

        LOGGER.debug(XmlUtil.toXML(user, toXmlConfig));
    }
}