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
package com.feilong.core.util.sortutiltest;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.util.SortUtil.sortArray;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import com.feilong.lib.lang3.ArrayUtils;

/**
 * The Class SortUtilSortArrayTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class SortArrayTest{

    @Test
    public void testSortArrayNullArray(){
        assertArrayEquals(ArrayUtils.EMPTY_OBJECT_ARRAY, sortArray((Object[]) null));
    }

    /**
     * Test sort array null.
     */
    @Test
    public void testSortArrayNull(){
        assertArrayEquals(toArray((String) null), sortArray(toArray((String) null)));
    }

    /**
     * Test sort T array.
     */
    @Test
    public void testSortTArray(){
        Integer[] array = toArray(100, 2, 200, 1, 500);
        sortArray(array);
        assertArrayEquals(toArray(1, 2, 100, 200, 500), array);
    }
}
