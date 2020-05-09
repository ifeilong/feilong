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
package com.feilong.template;

import static com.feilong.core.util.MapUtil.newHashMap;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.feilong.template.VelocityUtil;

public class ParseStringTest{

    @Test
    public void stringResourceLoader2(){
        Map<String, Object> map = newHashMap();
        map.put("memberId1", "5");

        String vmContent = "${memberId},${memberId1},feilong";

        assertEquals("${memberId},5,feilong", VelocityUtil.INSTANCE.parseString(vmContent, map));
    }

    /**
     * String resource loader1.
     */
    @Test
    public void stringResourceLoader1(){
        Map<String, Object> map = newHashMap();
        map.put("memberId", "5");

        String vmContent = "${memberId},feilong";

        assertEquals("5,feilong", VelocityUtil.INSTANCE.parseString(vmContent, map));
    }

    /**
     * String resource loader.
     */
    @Test
    public void stringResourceLoader(){
        Properties properties = new Properties();
        properties.put("name", "jinxin");

        Map<String, Object> map = newHashMap();
        map.put("global", properties);

        String vmContent = "${global.get('name')}";
        assertEquals("jinxin", VelocityUtil.INSTANCE.parseString(vmContent, map));
    }

    //---------------------------------------------------------------

    @Test
    public void testParseStringTestNull(){
        assertEquals(null, VelocityUtil.INSTANCE.parseString(null, null));
    }

    @Test
    public void testParseStringTestEmpty(){
        assertEquals("", VelocityUtil.INSTANCE.parseString("", null));
    }

    @Test
    public void testParseStringTestBlank(){
        assertEquals(" ", VelocityUtil.INSTANCE.parseString(" ", null));
    }

}
