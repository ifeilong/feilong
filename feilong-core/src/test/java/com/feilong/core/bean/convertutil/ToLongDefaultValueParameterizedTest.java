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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toLong;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class ToLongDefaultValueParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<Object, Long, Long>{

    @Parameters(name = "index:{index}: ConvertUtil.toLong({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { null, null, null },
                                              { null, 1L, 1L },
                                              { "aaaa", 1L, 1L },

                                              { "1", 2L, 1L },
                                              { 8, 1L, 8L },
                                              { "8", 1L, 8L },
                                              { new BigDecimal("8"), 1L, 8L },

                                              { new String[] { "1", "2", "3" }, 2L, 1L },
                                              { new String[] { "1", null, "2", "3" }, 2L, 1L },

                                              { toList("1", "2"), 5L, 1L },

                                              { "1,2,3", 5L, 5L },

                //
        };
        return toList(objects);
    }

    @Test
    public void test(){
        assertEquals(expectedValue, toLong(input1, input2));
    }

}
