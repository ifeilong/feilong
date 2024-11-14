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
package com.feilong.core.lang.objectutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.lang.ObjectUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class DefaultCountIfNullZeroOrGreaterThanParameterizedTest
                extends Abstract2ParamsAndResultParameterizedTest<Integer, Integer, Integer>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: ObjectUtil.defaultCountIfNullZeroOrGreaterThan({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] {
                                new Object[] { null, null, null },
                                new Object[] { null, 10, 10 },
                                new Object[] { -1, 2, 2 },

                                new Object[] { 0, 2, 2 },
                                new Object[] { 1, 2, 1 },
                                new Object[] { 3, 2, 2 },

                                new Object[] { 8, 2, 2 },
                                new Object[] { 8, 10, 8 }

        };
    }

    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(null, null)      = null
    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(null, 10)        = 10
    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(-1, 2)        = 2
    //    * 
    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(0, 2)        = 2
    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(1, 2)        = 1
    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(3, 2)        = 2
    //    * 
    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(8, 2) = 2
    //    * ObjectUtil.defaultCountIfNullZeroOrGreaterThan(8, 10) = 8

    @Test
    public void test(){
        assertEquals(expectedValue, ObjectUtil.defaultCountIfNullZeroOrGreaterThan(input1, input2));
    }

}