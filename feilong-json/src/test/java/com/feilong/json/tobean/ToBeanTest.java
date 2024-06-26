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

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;
import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.date.DateUtil.toDate;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.json.JsonToJavaException;
import com.feilong.json.JsonUtil;
import com.feilong.store.member.Person;
import com.feilong.store.member.User;

public class ToBeanTest{

    @Test
    public void testToBean(){
        User user = JsonUtil.toBean("{'password':'123456','name':'feilong','money':'99999999.00','loves':['桔子', '香蕉']}", User.class);
        user.setId(10L);

        assertThat(
                        user,
                        allOf(//
                                        hasProperty("id", is(10L)),
                                        hasProperty("password", is("123456")),
                                        hasProperty("name", is("feilong")),
                                        hasProperty("money", is(toBigDecimal("99999999.00"))),
                                        hasProperty("loves", arrayContaining("桔子", "香蕉"))
                        //  
                        ));
    }

    /**
     * Test to bean 1.
     */
    @Test
    public void testToBean1(){
        String json = "{'name':'get','dateAttr':'2009-11-12'}";
        Person person = JsonUtil.toBean(json, Person.class);

        assertThat(
                        person,
                        allOf(
                                        hasProperty("name", is("get")),
                                        hasProperty("dateAttr", is(toDate("2009-11-12 00:00:00", COMMON_DATE_AND_TIME)))));
    }

    //---------------------------------------------------------------
    /**
     * Test to bean null json.
     */
    @Test
    public void testToBeanNullJson(){
        assertEquals(null, JsonUtil.toBean(null, Person.class));
    }

    @Test
    public void testToBeanNullJson1(){
        assertEquals(null, JsonUtil.toBean("", Person.class));
    }

    @Test
    public void testToBeanNullJson12(){
        assertEquals(null, JsonUtil.toBean(" ", Person.class));
    }

    @Test(expected = JsonToJavaException.class)
    public void testToBean11(){
        String json = "2{'name11':'get'},{'nam112e':'set'}";
        JsonUtil.toBean(json, Person.class);
    }

    @Test(expected = NullPointerException.class)
    public void testToBeanNullRootClass(){
        String json = "{'name':'get','dateAttr':'2009-11-12'}";
        JsonUtil.toBean(json, (Class<?>) null);
    }
}
