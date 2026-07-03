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
package com.feilong.core.lang.stringutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.CollectionsUtil.size;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.lang.StringUtil;

public class ToMultiValueMapTest{

    @Test
    public void toMultiValueMap(){
        assertEquals(emptyMap(), StringUtil.toMultiValueMap(""));
        assertEquals(emptyMap(), StringUtil.toMultiValueMap(" "));
        assertEquals(emptyMap(), StringUtil.toMultiValueMap(null));
    }

    //---------------------------------------------------------------

    @Test
    public void toMultiValueMap123(){
        Map<String, List<String>> multiValueMap = StringUtil.toMultiValueMap("1=2");

        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()){
            String key = entry.getKey();
            List<String> value = entry.getValue();

            assertEquals(key, "1");
            assertThat(value, allOf(hasItem("2")));
        }
    }

    @Test
    public void toMultiValueMap1223(){
        Map<String, List<String>> multiValueMap = StringUtil.toMultiValueMap("1=2;");

        for (Map.Entry<String, List<String>> entry : multiValueMap.entrySet()){
            String key = entry.getKey();
            List<String> value = entry.getValue();

            assertEquals(key, "1");
            assertThat(value, allOf(hasItem("2")));
        }
    }

    @Test
    public void toMultiValueMap1333223Integer222(){
        Map<String, List<String>> multiValueMap = StringUtil.toMultiValueMap("1=2;89=100 ,99;200=230, 999;300");

        assertTrue(3 == size(multiValueMap));

        assertThat(
                        multiValueMap,
                        allOf(//
                                        hasEntry("1", toList("2")),
                                        hasEntry("89", toList("100", "99")),
                                        hasEntry("200", toList("230", "999"))));
    }
}