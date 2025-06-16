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
package com.feilong.core.bean.convertutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.Test;

public class ToArrayCollectionClassTest{

    @Test
    public void testToArray0(){
        List<String> list = toList("xinge", "feilong");
        assertArrayEquals(new String[] { "xinge", "feilong" }, toArray(list, String.class));
    }

    @Test(expected = NullPointerException.class)
    public void testToArrayNullType(){
        toArray(toList("xinge", "feilong"), null);
    }

    @Test
    public void testToArrayNullValue(){
        assertArrayEquals(null, toArray((List<String>) null, String.class));
    }
}
