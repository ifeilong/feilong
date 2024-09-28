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

import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import com.feilong.core.lang.StringUtil;

public class ToSingleValueMapConvertTest{

    @Test
    public void toSingleValueMap(){
        assertEquals(emptyMap(), StringUtil.toSingleValueMap("", String.class, String.class));
        assertEquals(emptyMap(), StringUtil.toSingleValueMap(" ", String.class, String.class));
        assertEquals(emptyMap(), StringUtil.toSingleValueMap(null, String.class, String.class));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void toSingleValueMap1(){
        StringUtil.toSingleValueMap("1=2", null, String.class);
    }

    @Test(expected = NullPointerException.class)
    public void toSingleValueMap12(){
        StringUtil.toSingleValueMap("1=2", String.class, (Class) null);
    }

    //---------------------------------------------------------------

    @Test
    public void toSingleValueMap123(){
        Map<String, String> map = StringUtil.toSingleValueMap("1=2", String.class, String.class);
        assertThat(map, allOf(hasEntry("1", "2")));
    }

    @Test
    public void toSingleValueMap1223(){
        Map<String, String> map = StringUtil.toSingleValueMap("1=2;", String.class, String.class);
        assertThat(map, allOf(hasEntry("1", "2")));
    }

    @Test
    public void toSingleValueMap1333223(){
        Map<Long, Long> map = StringUtil.toSingleValueMap("1=2;", Long.class, Long.class);

        assertThat(map, allOf(hasEntry(1L, 2L)));
    }

    @Test
    public void toSingleValueMap1333223Integer(){
        Map<Integer, Integer> map = StringUtil.toSingleValueMap("1=2;", Integer.class, Integer.class);

        assertThat(map, allOf(hasEntry(1, 2)));
    }

    @Test
    public void toSingleValueMap1333223Integer222(){
        Map<Integer, Integer> map = StringUtil.toSingleValueMap("1=2;89=100;200=230", Integer.class, Integer.class);

        assertThat(
                        map,
                        allOf(//
                                        hasEntry(1, 2),
                                        hasEntry(89, 100),
                                        hasEntry(200, 230)));
    }
}