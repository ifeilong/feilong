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
package com.feilong.json.tobean;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.feilong.core.DatePattern;
import com.feilong.core.date.DateUtil;
import com.feilong.json.JsonToJavaConfig;
import com.feilong.json.JsonUtil;
import com.feilong.store.member.User;

public class ToBeanIntegerToDateTest{

    @Test
    public void testToBean(){

        JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(User.class);
        jsonToJavaConfig.setClassMap(toMap("date", Date.class));

        User user = JsonUtil.toBean("{'date':1609948800}", jsonToJavaConfig);
        Date date = user.getDate();

        //bean:[User User [id=0, name=feilong, age=null, ageInt=0, password=null, loves=null, date=null, money=null, attrMap=null, nickNames=null, userInfo=com.feilong.store.member.UserInfo@7203c7ff, userAddresses=null, userAddresseList=[]]],key:[date],value:[1609948800] Cannot invoke com.feilong.store.member.User.setDate on bean class 'class com.feilong.store.member.User' - argument type mismatch - had objects of type "java.lang.Integer" but expected signature "java.util.Date"
        //java.lang.IllegalArgumentException:

        assertEquals("2021-01-07 00:00:00", DateUtil.toString(date, DatePattern.COMMON_DATE_AND_TIME));

    }

}
