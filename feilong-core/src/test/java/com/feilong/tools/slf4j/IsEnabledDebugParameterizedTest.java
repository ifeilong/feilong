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
package com.feilong.tools.slf4j;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class IsEnabledDebugParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, Boolean>{

    @Parameters(name = "index:{index}: Slf4jUtil.isEnabledDebug({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { null, false },
                                              { "", false },
                                              { " ", false },

                                              { "debug", true },
                                              { "deBug", true },
                                              { "debug ", true },
                                              { " debug ", true },
                                              { " DEBUG ", true },

                                              { "track", true },
                                              { "trAck", true },
                                              { "track ", true },
                                              { " track", true },
                                              { "TRACK", true },
                                              { " TRACK ", true },

                                              { "info", false },
                                              { " INFO ", false },

                                              { "error", false },
                                              { " ERROR ", false },

                                              { " TRACK1111 ", false },
                                              { " DEBUG2222 ", false },

                //
        };
        return toList(objects);
    }

    @Test
    public void test(){
        assertEquals(expectedValue, Slf4jUtil.isEnabledDebug(input1));
    }

}
