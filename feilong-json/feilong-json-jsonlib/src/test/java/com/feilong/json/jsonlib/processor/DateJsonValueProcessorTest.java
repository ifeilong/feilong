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
package com.feilong.json.jsonlib.processor;

import static com.feilong.core.DatePattern.COMMON_DATE;
import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;
import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND;
import static com.feilong.core.date.DateUtil.toDate;
import static com.feilong.core.util.MapUtil.newHashMap;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.json.jsonlib.JavaToJsonConfig;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.store.member.User;

import net.sf.json.processors.JsonValueProcessor;

/**
 * The Class DateJsonValueProcessorTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.8.5
 */
public class DateJsonValueProcessorTest{

    /**
     * Test date json value processor.
     */
    @Test
    public void testDateJsonValueProcessor(){
        User user = new User("feilong1", 24);
        user.setDate(toDate("2016-08-15 13:30:00", COMMON_DATE_AND_TIME));

        JavaToJsonConfig jsonFormatConfig = new JavaToJsonConfig();
        jsonFormatConfig.setIncludes("date");

        assertEquals("{\"date\": \"2016-08-15 13:30:00\"}", JsonUtil.format(user, jsonFormatConfig));
    }

    /**
     * Test date json value processor 1.
     */
    @Test
    public void testDateJsonValueProcessor1(){
        User user = new User("feilong1", 24);
        user.setDate(toDate("2016-08-15 13:30:00", COMMON_DATE_AND_TIME));

        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();
        propertyNameAndJsonValueProcessorMap.put("date", DateJsonValueProcessor.DEFAULT_INSTANCE);

        JavaToJsonConfig jsonFormatConfig = new JavaToJsonConfig();
        jsonFormatConfig.setPropertyNameAndJsonValueProcessorMap(propertyNameAndJsonValueProcessorMap);
        jsonFormatConfig.setIncludes("date");

        assertEquals("{\"date\": \"2016-08-15 13:30:00\"}", JsonUtil.format(user, jsonFormatConfig));
    }

    /**
     * Test date json value processor 2.
     */
    @Test
    public void testDateJsonValueProcessor2(){
        User user = new User("feilong1", 24);
        user.setDate(toDate("2016-08-15", COMMON_DATE));

        Map<String, JsonValueProcessor> propertyNameAndJsonValueProcessorMap = newHashMap();
        propertyNameAndJsonValueProcessorMap.put("date", new DateJsonValueProcessor(COMMON_DATE_AND_TIME_WITH_MILLISECOND));

        JavaToJsonConfig jsonFormatConfig = new JavaToJsonConfig();
        jsonFormatConfig.setPropertyNameAndJsonValueProcessorMap(propertyNameAndJsonValueProcessorMap);
        jsonFormatConfig.setIncludes("date");

        assertEquals("{\"date\": \"2016-08-15 00:00:00.000\"}", JsonUtil.format(user, jsonFormatConfig));
    }
}
