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
package com.feilong.core.util.collectionsutil.add;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toSet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class AddIgnoreNullOrEmptyIterableTest{

    @Test
    public void testAddIgnoreNullOrEmpty2(){
        List<String> list = toList("xinge", "feilong1");

        assertEquals(true, CollectionsUtil.addIgnoreNullOrEmpty(list, toList("xinge")));
        assertThat(list, contains("xinge", "feilong1", "xinge"));
    }
    //*************CollectionsUtil.addIgnoreNullOrEmpty(Collection<Object>, Object)**********

    /**
     * Test add ignore null or empty.
     */
    @Test(expected = NullPointerException.class)
    public void testAddIgnoreNullOrEmptyNullObjectCollection(){
        CollectionsUtil.addIgnoreNullOrEmpty(null, (List<String>) null);
    }

    /**
     * Test add ignore null or empty empty element.
     */
    @Test
    public void testAddIgnoreNullOrEmptyEmptyElement(){
        List<String> list = toList("xinge", "feilong1");

        assertEquals(false, CollectionsUtil.addIgnoreNullOrEmpty(list, toList("  ")));

        assertThat(list, contains("xinge", "feilong1"));
    }

    /**
     * Test add ignore null or empty empty element 1.
     */
    @Test
    public void testAddIgnoreNullOrEmptyEmptyElement1(){
        List<String> list = toList("xinge", "feilong1");
        assertEquals(false, CollectionsUtil.addIgnoreNullOrEmpty(list, toList("")));
        assertThat(list, contains("xinge", "feilong1"));
    }

    @Test
    public void testAddIgnoreNullOrEmptyEmptyElement12(){
        List<String> list = toList("xinge", "feilong1");

        assertEquals(true, CollectionsUtil.addIgnoreNullOrEmpty(list, toSet("44", null)));

        assertThat(list, contains("xinge", "feilong1", "44"));
    }

    @Test
    public void testAddIgnoreNullOrEmptyEmptyElementList(){
        List<String> list = toList("xinge", "feilong1");
        assertEquals(true, CollectionsUtil.addIgnoreNullOrEmpty(list, toList("44", null)));
        assertThat(list, contains("xinge", "feilong1", "44"));
    }

}
