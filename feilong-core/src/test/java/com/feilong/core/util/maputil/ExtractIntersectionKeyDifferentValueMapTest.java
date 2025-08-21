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

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.MapUtil;

@lombok.extern.slf4j.Slf4j

public class ExtractIntersectionKeyDifferentValueMapTest{

    /**
     * Test extract sub map 3.
     */
    @Test
    public void extractIntersectionKeyDifferentValueMap(){
        Map<Long, Long> targetMap = newLinkedHashMap();
        targetMap.put(1L, 88L);
        targetMap.put(2L, 99L);

        Map<Long, Integer> toBeComparedMap = newLinkedHashMap();
        toBeComparedMap.put(1L, 66);
        toBeComparedMap.put(2L, 99);
        toBeComparedMap.put(8L, 55);

        Map<Long, Long> extractSubMap = MapUtil.extractIntersectionKeyDifferentValueMap(targetMap, toBeComparedMap);
        assertThat(extractSubMap, allOf(hasEntry(1L, 88L), not(hasEntry(2L, 99L)), not(hasEntry(8L, 55L))));
        assertTrue(size(extractSubMap) == 1);
    }

    @Test
    public void extractIntersectionKeyDifferentValueMap2(){
        Map<Long, Long> targetMap = newLinkedHashMap();
        targetMap.put(1L, 88L);
        targetMap.put(2L, 99L);
        targetMap.put(20L, null);
        targetMap.put(30L, 30L);
        targetMap.put(50L, 50L);
        targetMap.put(40L, null);

        Map<Long, Integer> toBeComparedMap = newLinkedHashMap();
        toBeComparedMap.put(1L, 66);
        toBeComparedMap.put(2L, 99);
        toBeComparedMap.put(8L, 55);
        toBeComparedMap.put(30L, null);
        toBeComparedMap.put(40L, 1000);

        Map<Long, Long> extractSubMap = MapUtil.extractIntersectionKeyDifferentValueMap(targetMap, toBeComparedMap);

        //---------------------------------------------------------------

        if (log.isDebugEnabled()){
            log.debug(extractSubMap.toString());
        }

        //---------------------------------------------------------------

        assertThat(
                        extractSubMap,
                        allOf(
                                        hasEntry(1L, 88L),
                                        hasEntry(40L, null),
                                        hasEntry(30L, 30L),

                                        not(hasEntry(20L, null)),
                                        not(hasEntry(2L, 99L)),
                                        not(hasEntry(50L, 50L)),
                                        not(hasEntry(8L, 55L))));
        assertTrue(size(extractSubMap) == 3);
    }

    //---------------------------------------------------------------

    /**
     * Test extract sub map.
     */
    @Test
    public void testExtractSubMapNullMap(){
        assertEquals(emptyMap(), MapUtil.extractIntersectionKeyDifferentValueMap(null, null));
    }

    @Test
    public void testExtractSubMapNullMap1(){
        assertEquals(emptyMap(), MapUtil.extractIntersectionKeyDifferentValueMap(emptyMap(), null));
    }

    @Test
    public void testExtractSubMapNullMap21(){
        assertEquals(emptyMap(), MapUtil.extractIntersectionKeyDifferentValueMap(toMap(1, 2), null));
    }

    @Test
    public void testExtractSubMapNullMap212(){
        assertEquals(emptyMap(), MapUtil.extractIntersectionKeyDifferentValueMap(toMap(1, 2), emptyMap()));
    }

}
