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

import static com.feilong.core.util.MapUtil.newHashMap;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.json.JsonToJavaConfig;
import com.feilong.json.JsonUtil;
import com.feilong.json.entity.MyBean;
import com.feilong.store.member.Person;

public class ToMapWithJsonToJavaConfigTest{

    @Test
    public void toMap(){
        String json = "{'data1':{'name':'get'}}";
        Map<String, Person> map = JsonUtil.toMap(json, new JsonToJavaConfig(Person.class));

        assertThat(map, allOf(hasEntry(is("data1"), hasProperty("name", is("get")))));
    }

    @Test
    public void toMap1(){
        String json = "{'data1':{'name':'get'},'data2':{'name':'set'}}";
        Map<String, Person> map = JsonUtil.toMap(json, new JsonToJavaConfig(Person.class));

        assertThat(
                        map,
                        allOf(//
                                        hasEntry(is("data1"), hasProperty("name", is("get"))),
                                        hasEntry(is("data2"), hasProperty("name", is("set")))));
    }

    @Test
    public void toMap3(){
        String json = "{'mybean':{'data':[{'name':'get'}]}}";
        Map<String, Class<?>> classMap = newHashMap();
        classMap.put("data", Person.class);

        JsonToJavaConfig jsonToJavaConfig = new JsonToJavaConfig(MyBean.class);
        jsonToJavaConfig.setClassMap(classMap);

        Map<String, MyBean> map = JsonUtil.toMap(json, jsonToJavaConfig);

        MyBean myBean = map.get("mybean");
        List<Object> data = myBean.getData();

        Object object = data.get(0);
        assertThat(object, hasProperty("name", is("get")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToCommonString(){
        JsonUtil.toMap("<aaaaaa>", new JsonToJavaConfig(MyBean.class));
    }

    //---------------------------------------------------------------

    /**
     * Test to map null json.
     */
    @Test
    public void testToMapNullJson(){
        assertEquals(null, JsonUtil.toMap(null, new JsonToJavaConfig(Person.class)));
    }

    /**
     * Test to map empty json.
     */
    @Test
    public void testToMapEmptyJson(){
        assertEquals(null, JsonUtil.toMap("", new JsonToJavaConfig(Person.class)));
    }

    @Test
    public void testToMapBlankJson(){
        assertEquals(null, JsonUtil.toMap(" ", new JsonToJavaConfig(Person.class)));
    }

    /**
     * Test to map blank json 1.
     */
    @Test
    public void testToMapBlankJson1(){
        assertEquals(emptyMap(), JsonUtil.toMap("{}", new JsonToJavaConfig(Person.class)));
    }

}
