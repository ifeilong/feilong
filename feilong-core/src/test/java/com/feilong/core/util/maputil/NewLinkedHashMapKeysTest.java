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
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.util.MapUtil;

public class NewLinkedHashMapKeysTest{

    @Test
    public void testNewHashMap233311(){
        List<String> list = toList((String) null);
        Map<String, Boolean> map = MapUtil.newLinkedHashMap(list, true);

        assertThat(map.size(), is(size(list)));
    }

    @Test
    public void testNewHashMap233311222(){
        Map<String, Boolean> map = MapUtil.newLinkedHashMap(emptyList(), true);
        assertThat(map.size(), is(0));
    }

    @Test
    public void testNewHashMap2333(){
        List<String> list = toList("name", "age", null, "address");
        Map<String, Boolean> map = MapUtil.newLinkedHashMap(list, true);

        assertThat(map.size(), is(size(list)));
    }

    @Test
    public void testNewHashMap23332222(){
        List<String> list = toList("name", "age", null, "address");
        Map<String, String> map = MapUtil.newLinkedHashMap(list, "888");

        //        assertThat(map.size(), is(size(list)));

        assertThat(
                        map,
                        allOf(
                                        hasEntry("name", "888"), //
                                        hasEntry("age", "888"),
                                        hasEntry(null, "888"),
                                        hasEntry("address", "888")));
    }

    @Test
    public void testNewHashMap23331(){
        List<String> list = toList("name", "age", "address");
        Map<String, Boolean> map = MapUtil.newLinkedHashMap(list, true);

        assertThat(map.size(), is(3));
    }

    @Test
    public void testNewHashMapTest(){

        assertEquals(emptyMap(), newLinkedHashMap(null, true));
    }

}
