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
package com.feilong.json.processor;

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.json.JavaToJsonConfig;
import com.feilong.json.JsonUtil;
import com.feilong.lib.json.processors.JsonValueProcessor;
import com.feilong.store.member.User;
import com.feilong.test.AbstractTest;

public class BigDecimalJsonValueProcessorTest extends AbstractTest{

    @Test
    public void test(){
        User user = new User("feilong1", 24);
        user.setMoney(toBigDecimal("99999999.00"));

        JavaToJsonConfig jsonFormatConfig = new JavaToJsonConfig();
        jsonFormatConfig.setIncludes("money");

        assertEquals("{\"money\": \"99999999.00\"}", JsonUtil.format(user, jsonFormatConfig));
    }

    //---------------------------------------------------------------

    @Test
    public void test2(){
        User user = new User("feilong1", 24);
        user.setMoney(toBigDecimal("99999999.00"));

        assertThat(JsonUtil.format(user, build()), containsString("\"money\": \"99999999.00\""));
    }

    @Test
    public void test3(){
        User user = new User("feilong1", 24);
        user.setMoney(toBigDecimal("99999999.10"));

        assertThat(JsonUtil.format(user, build()), containsString("\"money\": \"99999999.10\""));
    }

    @Test
    public void test5(){
        User user = new User("feilong1", 24);
        user.setMoney(toBigDecimal("99999999.109"));

        assertThat(JsonUtil.format(user, build()), containsString("\"money\": \"99999999.11\""));
    }

    //---------------------------------------------------------------

    private static JavaToJsonConfig build(){
        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();
        propertyNameAndJsonValueProcessorMap.put("money", BigDecimalJsonValueProcessor.DEFAULT_INSTANCE);

        JavaToJsonConfig jsonFormatConfig = new JavaToJsonConfig();
        jsonFormatConfig.setPropertyNameAndJsonValueProcessorMap(propertyNameAndJsonValueProcessorMap);
        jsonFormatConfig.setIncludes("name", "age", "money");
        return jsonFormatConfig;
    }
}
