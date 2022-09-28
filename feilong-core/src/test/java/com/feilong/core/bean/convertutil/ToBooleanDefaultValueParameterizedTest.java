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

import static com.feilong.core.bean.ConvertUtil.toBoolean;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class ToBooleanDefaultValueParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<Object, Boolean, Boolean>{

    @Parameters(name = "index:{index}: ConvertUtil.toBoolean({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { null, null, null },
                                              { null, false, false },
                                              { null, true, true },

                                              { true, null, true },
                                              { true, true, true },
                                              { true, false, true },

                                              { "true1", null, null },
                                              { "true1", false, false },

                                              { "true", null, true },
                                              { "yes", null, true },
                                              { "y", null, true },
                                              { "on", null, true },
                                              { "1", null, true },
                                              { 1L, null, true },

                                              //---------------------------------------------------------------

                                              { "TRUE", null, true },
                                              { "YES", null, true },
                                              { "Y", null, true },
                                              { "ON", null, true },

                                              //---------------------------------------------------------------

                                              { false, null, false },

                                              { "false", null, false },
                                              { "no", null, false },
                                              { "n", null, false },
                                              { "off", null, false },

                                              { "FALSE", null, false },
                                              { "NO", null, false },
                                              { "N", null, false },
                                              { "OFF", null, false },

                                              //---------------------------------------------------------------

                                              { "0", null, false },
                                              { "9", null, null },

                                              { new String[] { "0", "1", "2", "3" }, null, false },
                                              { new String[] { "1", null, "2", "3" }, null, true },
                                              { "1,2,3", null, null },//false
                //
        };
        return toList(objects);
    }

    @Test
    public void test(){
        assertEquals(expectedValue, toBoolean(input1, input2));
    }

}
