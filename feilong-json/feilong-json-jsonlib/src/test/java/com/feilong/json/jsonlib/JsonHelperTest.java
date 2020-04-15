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
package com.feilong.json.jsonlib;

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
import com.feilong.json.AbstractJsonTest;
import com.feilong.json.HttpMethodTestType;
import com.feilong.json.jsonlib.builder.JsonConfigBuilder;
import com.feilong.json.jsonlib.entity.MyBean;
import com.feilong.store.member.Person;
import com.feilong.store.member.User;
import com.feilong.store.member.UserInfo;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonHelperTest extends AbstractJsonTest{

    @Test
    public void isCommonStringTest(){
        String json_test = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";
        assertEquals(false, JsonHelper.isCommonString(json_test));
    }

    @Test
    public void isCommonStringTest1(){
        String json_test = "[name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]]";
        assertEquals(false, JsonHelper.isCommonString(json_test));
    }

    //---------------------------------------------------------------

    @Test
    @SuppressWarnings("static-method")
    public void name(){
        String json_test = "{name=\"json\",bool:true,int:1,double:2.2,func:function(a){ return a; },array:[1,2]}";

        JSONObject jsonObject = JSONObject.fromObject(json_test);
        Object bean = JSONObject.toBean(jsonObject);

        assertEquals(jsonObject.get("name"), PropertyUtil.getProperty(bean, "name"));
        assertEquals(jsonObject.get("bool"), PropertyUtil.getProperty(bean, "bool"));
        assertEquals(jsonObject.get("int"), PropertyUtil.getProperty(bean, "int"));
        assertEquals(jsonObject.get("double"), PropertyUtil.getProperty(bean, "double"));
        assertEquals(jsonObject.get("func"), PropertyUtil.getProperty(bean, "func"));
        List<?> expected = JSONArray.toList(jsonObject.getJSONArray("array"));
        assertEquals(expected, PropertyUtil.getProperty(bean, "array"));
    }

    /**
     * To bean n ull.
     */
    @Test
    @SuppressWarnings("static-method")
    public void toBeanNUll(){
        LOGGER.debug(toJSON(null).toString(4, 4));
        LOGGER.debug(new JSONObject().toString(4));
    }

    /**
     * To json.
     */
    @SuppressWarnings("static-method")
    @Test
    public void toJSON(){
        LOGGER.debug(toJSON(HttpMethodTestType.GET).toString(4, 4));
    }

    /**
     * To bean n ulluser.
     */
    @SuppressWarnings("static-method")
    @Test
    public void toBeanNUlluser(){
        User user = new User();
        user.setId(8L);
        user.setName("feilong");

        JsonConfig jsonConfig = new JsonConfig();

        // String[] excludes = { "userInfo" };
        // jsonConfig.setExcludes(excludes);

        Class<UserInfo> target = UserInfo.class;
        String[] properties = { "age" };
        jsonConfig.registerPropertyExclusions(target, properties);
        LOGGER.debug(JsonHelper.toJSON(user, jsonConfig).toString(4, 4));
    }

    /**
     * Name.
     */
    @SuppressWarnings("static-method")
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
    @SuppressWarnings("static-method")
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
    @SuppressWarnings("static-method")
    public void testgetJsonStr4(){
        Person ps = new Person();
        ps.setDateAttr(now());
        ps.setName("get");
        List<Person> list = newArrayList();
        list.add(ps);

        // print: [{"dateAttr":"2009-09-12 07:22:49","name":"get"}]
        LOGGER.debug("" + toJSON(list));

        Set set = toSet(ps);

        // print: [{"dateAttr":"2009-09-12 07:22:16","name":"get"}]
        LOGGER.debug("" + toJSON(set));

        Person[] personArr = new Person[1];
        personArr[0] = ps;
        // print: [{"dateAttr":"2009-09-12 07:23:54","name":"get"}]
        LOGGER.debug("" + toJSON(personArr));

        Map map = new LinkedHashMap();
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
