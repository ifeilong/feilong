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
package com.feilong.core.util.maputil;

import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.MapUtil;

public class GetSubMapExcludeValuesTest{

    @Test
    public void testGetSubMapExcludeValues(){
        Map<String, String> map = newLinkedHashMap();
        map.put("119072412", "解读");
        map.put("76841752", "泛化");
        map.put("87838803", "不相关");
        map.put("119069161", "解读");
        map.put("91920781", "不相关");
        map.put("92440989", "泛化");

        Map<String, String> subMapExcludeValues = MapUtil.getSubMapExcludeValues(map, "不相关");
        assertThat(
                        subMapExcludeValues,
                        allOf(//
                                        hasEntry("119072412", "解读"),
                                        hasEntry("76841752", "泛化"),
                                        hasEntry("119069161", "解读"),
                                        hasEntry("92440989", "泛化"),

                                        not(hasKey("87838803")),
                                        not(hasKey("91920781")))

        );
    }

    @Test
    public void testGetSubMapExcludeValues1(){
        Map<String, String> map = newLinkedHashMap();
        map.put("119072412", "解读");
        map.put("76841752", "泛化");
        map.put("87838803", "不相关");
        map.put("119069161", "解读");
        map.put("91920781", "不相关");
        map.put("92440989", "泛化");

        Map<String, String> subMapExcludeValues = MapUtil.getSubMapExcludeValues(map, "不相关", "泛化");
        assertThat(
                        subMapExcludeValues,
                        allOf(//
                                        hasEntry("119072412", "解读"),
                                        hasEntry("119069161", "解读"),

                                        not(hasKey("92440989")),
                                        not(hasKey("76841752")),
                                        not(hasKey("87838803")),
                                        not(hasKey("91920781")))

        );
    }

    //---------------------------------------------------------------

    @Test
    public void testGetSubMapNullMap(){
        assertEquals(emptyMap(), MapUtil.getSubMapExcludeValues(null, "a", "c"));
    }

    @Test
    public void testGetSubMapEmptyMap(){
        assertEquals(emptyMap(), MapUtil.getSubMapExcludeValues(new HashMap<>(), "a", "c"));
        assertEquals(emptyMap(), MapUtil.getSubMapExcludeValues(emptyMap(), "a", "c"));
    }

    @Test
    public void testGetSubMapNullExcludeValues(){
        Map<String, Integer> map = newLinkedHashMap();

        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3002);
        map.put("g", -1005);
        assertEquals(map, MapUtil.getSubMapExcludeValues(map, (Integer[]) null));
    }

}
