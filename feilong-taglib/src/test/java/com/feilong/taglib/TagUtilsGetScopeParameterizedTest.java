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
package com.feilong.taglib;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class TagUtilsGetScopeParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, Integer>{

    @Parameters(name = "index:{index}: TagUtils.getScope({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "request", 2 },
                                              { "session", 3 },
                                              { "application", 4 },
                                              { "page", 1 },

                                              //---------------------------------------------------------------
                                              { "Request", 2 },
                                              { "Session", 3 },
                                              { "Application", 4 },
                                              { "Page", 1 },

                                              //---------------------------------------------------------------
                                              { "REQUEST", 2 },
                                              { "SESSION", 3 },
                                              { "APPLICATION", 4 },
                                              { "PAGE", 1 },

                                              //---------------------------------------------------------------
                                              { "PAGE111111", 1 },

                //---------------------------------------------------------------
                //
        };

        return toList(objects);
    }

    @Test
    public void testBuild(){
        assertEquals(expectedValue.intValue(), TagUtils.getScope(input1));
    }

}