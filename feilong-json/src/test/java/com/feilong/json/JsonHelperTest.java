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
package com.feilong.json;

import static com.feilong.core.bean.ConvertUtil.toSet;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.feilong.core.bean.PropertyUtil;
import com.feilong.json.builder.JsonConfigBuilder;
import com.feilong.json.entity.HttpMethodTestType;
import com.feilong.json.entity.MyBean;
import com.feilong.lib.json.JSON;
import com.feilong.lib.json.JSONObject;
import com.feilong.lib.json.JsonConfig;
import com.feilong.store.member.Person;
import com.feilong.test.AbstractTest;

public class JsonHelperTest extends AbstractTest{

    @Test
    public void name(){
        String json_test = "{name=\"json\",bool:true,int:1,double:2.2,array:[1,2]}";

        JSONObject jsonObject = JSONObject.fromObject(json_test);
        Object bean = JSONObject.toBean(jsonObject);

        assertEquals(jsonObject.get("name"), PropertyUtil.getProperty(bean, "name"));
        assertEquals(jsonObject.get("bool"), PropertyUtil.getProperty(bean, "bool"));
        assertEquals(jsonObject.get("int"), PropertyUtil.getProperty(bean, "int"));
        assertEquals(jsonObject.get("double"), PropertyUtil.getProperty(bean, "double"));
        //Collection<?> expected = JSONArray.toCollection(jsonObject.getJSONArray("array"));
        //assertEquals(expected, PropertyUtil.getProperty(bean, "array"));
    }

    /**
     * To bean n ull.
     */
    @Test
    public void toBeanNUll(){
        LOGGER.debug(toJSON(null).toString(4, 4));
    }

    /**
     * To json.
     */
    @Test
    public void toJSON(){
        LOGGER.debug(toJSON(HttpMethodTestType.GET).toString(4, 4));
    }

    /**
     * Name.
     */
    @Test
    public void name1(){
        Map<String, Object> map = newHashMap();

        Map<String, Object> map1 = newHashMap();

        String[] aStrings = { "aaaa", "bbbb" };
        map1.put("b", aStrings);
        map1.put("bb", "2");
        map1.put("bbb", "3");

        map.put("a", map1);
        map.put("aa", map1);
        map.put("aaa", map1);
        LOGGER.debug(toJSON(map).toString(4, 4));
    }

    /**
     * 实体Bean转json串 void.
     */
    @Test
    public void testgetJsonStr1(){
        Person ps = new Person();
        ps.setDateAttr(now());
        ps.setName("get");
        MyBean myBean = new MyBean();
        List<Object> list = newArrayList();
        list.add(ps);

        myBean.setData(list);
        // print: {"data":[{"dateAttr":"2009-09-12 07:24:54","name":"get"}]}
        LOGGER.debug("" + toJSON(myBean));
    }

    /**
     * list转json串 void.
     */
    @Test
    public void testgetJsonStr4(){
        Person ps = new Person();
        ps.setDateAttr(now());
        ps.setName("get");
        List<Person> list = newArrayList();
        list.add(ps);

        // print: [{"dateAttr":"2009-09-12 07:22:49","name":"get"}]
        LOGGER.debug("" + toJSON(list));

        Set<Person> set = toSet(ps);

        // print: [{"dateAttr":"2009-09-12 07:22:16","name":"get"}]
        LOGGER.debug("" + toJSON(set));

        Person[] personArr = new Person[1];
        personArr[0] = ps;
        // print: [{"dateAttr":"2009-09-12 07:23:54","name":"get"}]
        LOGGER.debug("" + toJSON(personArr));

        Map<String, Person> map = new LinkedHashMap<>();
        map.put("person1", ps);

        // print: {"person1":{"dateAttr":"2009-09-12 07:24:27","name":"get"}}
        LOGGER.debug("" + toJSON(map));
    }

    /**
     * 把实体Bean、Map对象、数组、列表集合转换成Json串.
     *
     * @param obj
     *            the obj
     * @return the jSON
     * @see #toJSON(Object, JsonConfig)
     */
    static JSON toJSON(Object obj){
        return JsonHelper.toJSON(obj, JsonConfigBuilder.DEFAULT_JAVA_TO_JSON_CONFIG);
    }
}
