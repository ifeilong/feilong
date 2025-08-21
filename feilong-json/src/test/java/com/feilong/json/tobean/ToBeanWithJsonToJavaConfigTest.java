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
import static com.feilong.json.transformer.UncapitalizeJavaIdentifierTransformer.UNCAPITALIZE;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.json.JsonToJavaConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.CrmMemberInfoCommand;
import com.feilong.json.entity.MyBean;
import com.feilong.json.transformer.SeparatorToCamelCaseJavaIdentifierTransformer;
import com.feilong.lib.json.util.JavaIdentifierTransformer;
import com.feilong.store.member.Person;
import com.feilong.store.member.User;

public class ToBeanWithJsonToJavaConfigTest{

    @Test
    public void test(){
        String json = "{'data':[{'name':'get'}]}";

        MyBean myBeans = JsonUtil.toBean(
                        json, //
                        new JsonToJavaConfig(MyBean.class, toMap("data", Person.class)));

        assertThat(myBeans.getData().get(0), allOf(hasProperty("name", is("get"))));
    }

    @Test
    public void testToBeanWithUncapitalizeJavaIdentifierTransformer1(){
        String json = "{'member_no':'11105000009'}";

        CrmMemberInfoCommand crmMemberInfoCommand = JsonUtil.toBean(
                        json,
                        new JsonToJavaConfig(CrmMemberInfoCommand.class, SeparatorToCamelCaseJavaIdentifierTransformer.INSTANCE));

        assertThat(
                        crmMemberInfoCommand,
                        allOf(//
                                        hasProperty("memberNo", is("11105000009"))
                        //        
                        ));
    }

    @Test
    public void testToBeanWithUncapitalizeJavaIdentifierTransformer12(){
        String json = "{'member@no':'11105000009'}";

        CrmMemberInfoCommand crmMemberInfoCommand = JsonUtil.toBean(
                        json,
                        new JsonToJavaConfig(CrmMemberInfoCommand.class, new SeparatorToCamelCaseJavaIdentifierTransformer('@')));

        assertThat(
                        crmMemberInfoCommand,
                        allOf(//
                                        hasProperty("memberNo", is("11105000009"))
                        //        
                        ));
    }

    //---------------------------------------------------------------

    @Test
    public void testToBeanWithUncapitalizeJavaIdentifierTransformer(){
        String json = "{'MemberNo':'11105000009','Name':null,'Gender':'','Phone':'15036334567','Email':null,'Birthday':''}";

        CrmMemberInfoCommand crmMemberInfoCommand = JsonUtil.toBean(json, new JsonToJavaConfig(CrmMemberInfoCommand.class, UNCAPITALIZE));

        assertThat(
                        crmMemberInfoCommand,
                        allOf(//
                                        hasProperty("memberNo", is("11105000009")),
                                        hasProperty("name", is(nullValue())),
                                        hasProperty("gender", is("")),
                                        hasProperty("phone", is("15036334567")),
                                        hasProperty("email", is(nullValue())),
                                        hasProperty("birthday", is(""))
                        //        
                        ));
    }

    /**
     * Test to bean with camelcase java identifier transformer.
     */
    @Test
    public void testToBeanWithCamelcaseJavaIdentifierTransformer(){
        String json = "{'member No':'11105000009','name':null,'gender':'','phone':'15036334567','email':null,'birthday':''}";

        CrmMemberInfoCommand crmMemberInfoCommand = JsonUtil
                        .toBean(json, new JsonToJavaConfig(CrmMemberInfoCommand.class, JavaIdentifierTransformer.CAMEL_CASE));

        assertThat(
                        crmMemberInfoCommand,
                        allOf(//
                                        hasProperty("memberNo", is("11105000009")),
                                        hasProperty("name", is(nullValue())),
                                        hasProperty("gender", is("")),
                                        hasProperty("phone", is("15036334567")),
                                        hasProperty("email", is(nullValue())),
                                        hasProperty("birthday", is(""))
                        //        
                        ));
    }

    //---------------------------------------------------------------

    @Test
    public void testToBean(){
        String json = "{'data':[{'name':'get'},{'name':'set'}],'id':5}";

        MyBean myBean = JsonUtil.toBean(json, new JsonToJavaConfig(MyBean.class, toMap("data", Person.class)));

        assertThat(myBean, allOf(hasProperty("id", is(5L))));

        List<Object> data = myBean.getData();
        assertThat(data.get(0), allOf(hasProperty("name", is("get"))));
        assertThat(data.get(1), allOf(hasProperty("name", is("set"))));

    }
    //
    //    /**
    //     * Test to bean 3.
    //     */
    //    @Test
    //    public void testToBean3(){
    //        Map<String, Class<?>> classMap = new HashMap<>();
    //        classMap.put("userAddresseList", UserAddress.class);
    //
    //        User user = JsonUtil.toBean(USER_JSON_STRING, new JsonToJavaConfig(User.class, classMap));
    //        log.debug(JsonUtil.format(user));
    //    }

    //---------------------------------------------------------------
    /**
     * Test to bean null json.
     */
    @Test
    public void testToBeanNullJson(){
        User user = JsonUtil.toBean(null, new JsonToJavaConfig(User.class));
        assertEquals(null, user);
    }

    @Test
    public void testToBeanNullJson2(){
        User user = JsonUtil.toBean("", new JsonToJavaConfig(User.class));
        assertEquals(null, user);
    }

    @Test
    public void testToBeanNullJson23(){
        User user = JsonUtil.toBean(" ", new JsonToJavaConfig(User.class));
        assertEquals(null, user);
    }

    /**
     * Test to bean null json to java config.
     */
    @Test(expected = NullPointerException.class)
    public void testToBeanNullJsonToJavaConfig(){
        String json = "{'data':[{'name':'get'},{'name':'set'}],'id':5}";
        JsonUtil.toBean(json, (JsonToJavaConfig) null);
    }

    /**
     * Test to bean null root class.
     */
    @Test(expected = NullPointerException.class)
    public void testToBeanNullRootClass(){
        String json = "{'data':[{'name':'get'},{'name':'set'}],'id':5}";
        JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig();
        JsonUtil.toBean(json, jsonToJavaConfig);
    }
}
