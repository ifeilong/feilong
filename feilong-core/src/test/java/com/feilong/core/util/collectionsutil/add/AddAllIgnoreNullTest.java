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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class AddAllIgnoreNullTest{

    @Test(expected = NullPointerException.class)
    public void testAddAllIgnoreNullNullObjectCollection(){
        CollectionsUtil.addAllIgnoreNull(null, null);
    }

    /**
     * Test add all ignore null.
     */
    @Test
    public void testAddAllIgnoreNullNullIterable(){
        List<String> list = toList("xinge", "feilong1");
        assertEquals(false, CollectionsUtil.addAllIgnoreNull(list, null));
        assertThat(list, contains("xinge", "feilong1"));
    }

    /**
     * Test add all ignore null2.
     */
    @Test
    public void testAddAllIgnoreNull2(){
        List<String> list = toList("xinge", "feilong1");
        boolean addAllIgnoreNull = CollectionsUtil.addAllIgnoreNull(list, toList("xinge", "feilong1"));
        assertEquals(true, addAllIgnoreNull);
        assertThat(list, contains("xinge", "feilong1", "xinge", "feilong1"));
    }
}
