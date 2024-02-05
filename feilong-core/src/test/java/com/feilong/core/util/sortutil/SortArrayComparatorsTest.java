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
package com.feilong.core.util.sortutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY;
import static com.feilong.core.util.SortUtil.sortArray;
import static org.junit.Assert.assertArrayEquals;

import java.util.Comparator;

import org.junit.Test;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.lib.collection4.ComparatorUtils;

/**
 * The Class SortUtilSortArrayComparatorsTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class SortArrayComparatorsTest{

    /**
     * Test sort array comparators test.
     */
    @Test
    public void testSortArrayComparatorsTest(){
        String[] arrays = { "almn", "fba", "cba" };

        Comparator<String> comparator = new Comparator<String>(){

            @Override
            public int compare(String s1,String s2){
                Integer length = s1.length();
                Integer length2 = s2.length();

                //先判断长度,长度比较
                int compareTo = length.compareTo(length2);

                //如果长度相等,那么比较自己本身的顺序
                if (0 == compareTo){
                    compareTo = s1.compareTo(s2);
                }
                return compareTo;
            }
        };
        sortArray(arrays, comparator);

        assertArrayEquals(toArray("cba", "fba", "almn"), arrays);
    }

    @Test
    public void testSortArrayNullArray(){
        String[] arrays = null;
        assertArrayEquals(EMPTY_STRING_ARRAY, sortArray(arrays, ComparatorUtils.<String> naturalComparator()));
    }

    /**
     * Test sort array null comparator.
     */
    @Test
    public void testSortArrayNullComparator(){
        assertArrayEquals(toArray(1, 2, 3), sortArray(toArray(1, 2, 3), null));
    }

    /**
     * Test sort array empty comparator.
     */
    @Test
    public void testSortArrayEmptyComparator(){
        assertArrayEquals(toArray(1, 2, 3), sortArray(toArray(1, 2, 3), ConvertUtil.<Comparator> toArray()));
    }
}
