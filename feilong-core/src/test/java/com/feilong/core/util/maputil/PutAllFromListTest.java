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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.MapUtil;

public class PutAllFromListTest{

    @Test
    public void testPutAllFromListIfNotNullNullValue(){
        Map<Long, Long> map = newLinkedHashMap();

        MapUtil.putAllFromList(map, toList(123234L, 345123L), toList(1L, 2L));

        assertThat(map, hasEntry(123234L, 1L));
        assertThat(map, hasEntry(345123L, 2L));
        assertThat(map.keySet(), hasSize(2));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testPutAllFromListNullMap(){
        MapUtil.putAllFromList(null, toList("123234", "345123"), toList("1", "2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAllFromListNullMap1(){
        Map<String, Long> map = newLinkedHashMap();
        MapUtil.putAllFromList(map, toList("123234", "345123"), toList(1L));
    }

    @Test
    public void testPutAllFromListNullKeyList(){
        Map<String, String> map = newLinkedHashMap();
        MapUtil.putAllFromList(map, null, toList("1", "2"));
        assertThat(map.keySet(), hasSize(0));
    }

    @Test
    public void testPutAllFromListEmptyKeyList(){
        Map<String, String> map = newLinkedHashMap();
        MapUtil.putAllFromList(map, emptyList(), toList("1", "2"));
        assertThat(map.keySet(), hasSize(0));
    }

    //---------------------------------------------------------------
    @Test
    public void testPutAllFromListNullValueList(){
        Map<String, String> map = newLinkedHashMap();
        MapUtil.putAllFromList(map, toList("123234", "345123"), null);
        assertThat(map.keySet(), hasSize(0));
    }

    @Test
    public void testPutAllFromListEmptyValueList(){
        Map<String, String> map = newLinkedHashMap();
        MapUtil.putAllFromList(map, toList("123234", "345123"), emptyList());
        assertThat(map.keySet(), hasSize(0));
    }

}
