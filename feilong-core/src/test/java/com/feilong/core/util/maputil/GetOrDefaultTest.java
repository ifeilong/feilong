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
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.MapUtil;

public class GetOrDefaultTest{

    @Test
    public void test(){
        Map<String, String> map = newLinkedHashMap();
        map.put("name", "jim");
        map.put("address", "shanghai");
        map.put("age", "18");

        assertEquals("feilong", MapUtil.getOrDefault(map, "name1", "feilong"));

    }

    @Test
    public void testGetNullMap(){
        assertEquals((Object) null, MapUtil.getOrDefault(null, "name", "jim"));
    }
}
