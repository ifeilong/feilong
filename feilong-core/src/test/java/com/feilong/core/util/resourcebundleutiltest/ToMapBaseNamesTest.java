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
package com.feilong.core.util.resourcebundleutiltest;

import static com.feilong.core.util.ResourceBundleUtil.toMap;
import static com.feilong.lib.lang3.ArrayUtils.EMPTY_STRING_ARRAY;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

import com.feilong.core.lang.ClassUtil;

public class ToMapBaseNamesTest{

    @Test
    public void testToMapCoverNotExsit(){
        Map<String, String> map = toMap("messages/memcached", "messages/memcached-cover111");
        assertEquals("false", map.get("memcached.alivecheck"));
        assertTrue(ClassUtil.isInstance(map, TreeMap.class));
    }

    @Test
    public void testToMapCover(){
        Map<String, String> map = toMap("messages/memcached", "messages/memcached-cover");
        assertEquals("true", map.get("memcached.alivecheck"));
    }

    @Test
    public void testToMapCover1(){
        Map<String, String> map = toMap("messages/memcached-cover", "messages/memcached");
        assertEquals("false", map.get("memcached.alivecheck"));
    }

    //---------------------------------------------------------------

    @Test
    public void testToMap(){
        Map<String, String> map = toMap("messages/memcached");
        assertEquals("false", map.get("memcached.alivecheck"));
    }

    /**
     * Test to map empty.
     */
    @Test
    public void testToMapEmpty(){
        Map<String, String> map = toMap("messages/empty");
        assertEquals(emptyMap(), map);
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testToMapBaseNamesTestNull(){
        toMap((String[]) null);
    }

    //---------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testToMapBaseNamesTestEmpty2222(){
        toMap(EMPTY_STRING_ARRAY);
    }

    @Test(expected = NullPointerException.class)
    public void testToMapBaseNamesTestNullElement(){
        toMap((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToMapBaseNamesTestEmpty(){
        toMap("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToMapBaseNamesTestBlank(){
        toMap(" ");
    }

}
