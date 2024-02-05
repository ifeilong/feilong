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

import static com.feilong.core.util.MapUtil.newHashMap;
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

/**
 * The Class MapUtilGetSubMapTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class GetSubMapTest{

    /**
     * Test get sub map.
     */
    @Test
    public void testGetSubMap(){
        Map<String, Integer> map = newHashMap();
        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3001);
        map.put("d", 3003);
        Map<String, Integer> subMap = MapUtil.getSubMap(map, "a", "c");
        assertThat(subMap, allOf(hasEntry("a", 3007), hasEntry("c", 3001), not(hasKey("b")), not(hasKey("d"))));
    }

    /**
     * Test get sub map 1.
     */
    @Test
    public void testGetSubMap1(){
        Map<String, Integer> map = newHashMap();
        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3001);
        map.put("d", 3003);
        Map<String, Integer> subMap = MapUtil.getSubMap(map, "a", "c", "f");
        assertThat(subMap, allOf(hasEntry("a", 3007), hasEntry("c", 3001), not(hasKey("b")), not(hasKey("d"))));
    }

    /**
     * Test get sub map null keys.
     */
    @Test
    public void testGetSubMapNullKeys(){
        Map<String, Integer> map = newHashMap();
        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3001);
        map.put("d", 3003);
        assertEquals(map, MapUtil.getSubMap(map, (String[]) null));
    }

    /**
     * Test get sub map empty keys.
     */
    @Test
    public void testGetSubMapEmptyKeys(){
        Map<String, Integer> map = newHashMap();
        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3001);
        map.put("d", 3003);
        assertEquals(map, MapUtil.getSubMap(map));
    }

    /**
     * Test get sub map null map.
     */
    @Test
    public void testGetSubMapNullMap(){
        assertEquals(emptyMap(), MapUtil.getSubMap(null, "a", "c"));
    }

    /**
     * Test get sub map empty map.
     */
    @Test
    public void testGetSubMapEmptyMap(){
        assertEquals(emptyMap(), MapUtil.getSubMap(new HashMap<>(), "a", "c"));
        assertEquals(emptyMap(), MapUtil.getSubMap(emptyMap(), "a", "c"));
    }
}
