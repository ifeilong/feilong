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
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.MapUtil.newHashMap;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.MapUtil;

public class GetSubSumValueTest{

    @Test
    public void getSubSumValue(){
        Map<String, Integer> map = newHashMap();
        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3001);
        map.put("d", 3003);

        assertEquals((Object) 6008, MapUtil.getSubSumValue(map, toList("a", "c"), Integer.class));
        assertEquals((Object) 6008L, MapUtil.getSubSumValue(map, toList("a", "c"), Long.class));
    }

    @Test
    public void getSubSumValue1(){
        Map<String, Integer> map = newHashMap();
        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3001);
        map.put("c1", null);
        map.put("d", 3003);

        assertEquals((Object) 6008, MapUtil.getSubSumValue(map, toList("a", "c", "c1"), Integer.class));
        assertEquals((Object) 6008L, MapUtil.getSubSumValue(map, toList("a", "c", "c1"), Long.class));
    }

    @Test
    public void getSubSumValue1Not(){
        Map<String, Integer> map = newHashMap();
        map.put("a", 3007);
        map.put("b", 3001);
        map.put("c", 3001);
        map.put("c1", null);
        map.put("d", 3003);

        assertEquals((Object) 6008, MapUtil.getSubSumValue(map, toList("a", "c", "c1", "x"), Integer.class));
        assertEquals((Object) 6008L, MapUtil.getSubSumValue(map, toList("a", "c", "c1", "x"), Long.class));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testGetSubMapNullMap(){
        MapUtil.getSubSumValue(toMap("age", 24), toList("zhangfei", "guanyu"), null);
    }

    @Test
    public void getSubSumValue0(){
        assertEquals((Object) 0, MapUtil.getSubSumValue(emptyMap(), emptyList(), Integer.class));

        assertEquals((Object) 0, MapUtil.getSubSumValue(emptyMap(), toList("zhangfei", "guanyu"), Integer.class));

        assertEquals((Object) 0, MapUtil.getSubSumValue(toMap("age", 24), emptyList(), Integer.class));
    }

}
