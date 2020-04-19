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

import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.entity.BeanIntIgnoreNull;
import com.feilong.json.jsonlib.entity.BeanIntIgnoreNullNests;
import com.feilong.json.jsonlib.entity.BeanIntIgnoreNullParent;

import net.sf.json.JSONObject;

public class FormatBeanIgnoreNullTest{

    /** The Constant log. */
    private static final Logger     LOGGER            = LoggerFactory.getLogger(FormatBeanIgnoreNullTest.class);

    private final BeanIntIgnoreNull beanIntIgnoreNull = new BeanIntIgnoreNull(16, null);

    //---------------------------------------------------------------
    @Test
    public void test0(){
        assertEquals("{\"name\":\"\",\"age\":16}", JsonUtil.format(new BeanIntIgnoreNull(16, null), 0, 0));
    }

    @Test
    public void test(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIsIgnoreNullValueElement(true);

        assertEquals("{\"age\": 16}", JsonUtil.format(new BeanIntIgnoreNull(16, null), javaToJsonConfig));
    }

    @Test
    public void testAnd(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIsIgnoreNullValueElement(true);
        javaToJsonConfig.setIncludes("name", "age");

        assertEquals("{\"age\":16}", JsonUtil.format(new BeanIntIgnoreNull(16, null), javaToJsonConfig, 0, 0));
    }

    @Test
    public void testAnd2(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIsIgnoreNullValueElement(true);
        javaToJsonConfig.setIncludes("name");

        assertEquals("{}", JsonUtil.format(new BeanIntIgnoreNull(16, null), javaToJsonConfig, 0, 0));
    }

    @Test
    public void testAnd233(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIsIgnoreNullValueElement(true);

        assertEquals("{}", JsonUtil.format(new BeanIntIgnoreNullParent(null, null), javaToJsonConfig, 0, 0));
    }

    @Test
    public void testAnd23333(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIsIgnoreNullValueElement(true);

        assertEquals(
                        "{\"beanIntIgnoreNullNests\":{}}",
                        JsonUtil.format(new BeanIntIgnoreNullParent(null, new BeanIntIgnoreNullNests()), javaToJsonConfig, 0, 0));
    }

    @Test
    public void test1222(){
        JavaToJsonConfig javaToJsonConfig = new JavaToJsonConfig();
        javaToJsonConfig.setIsIgnoreNullValueElement(true);

        assertEquals("{}", JsonUtil.format(new BeanIntIgnoreNull(null, null), javaToJsonConfig));
    }

    @Test
    public void test1(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("age", 16);
        jsonObject.put("name", null);
        LOGGER.debug(jsonObject.toString());
    }

    @Test
    public void test12(){
        JSONObject jsonObject = JSONObject.fromObject(beanIntIgnoreNull);
        LOGGER.debug(jsonObject.toString());
    }

    @Test
    public void test12MAP(){
        JSONObject jsonObject = JSONObject.fromObject(toMap("age", 16, "name", null));
        LOGGER.debug(jsonObject.toString());
    }

}
