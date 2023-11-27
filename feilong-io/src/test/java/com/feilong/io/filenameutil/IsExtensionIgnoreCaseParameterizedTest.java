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
package com.feilong.io.filenameutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.io.FilenameUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class IsExtensionIgnoreCaseParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, String[], Boolean>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: FilenameUtil.isExtensionIgnoreCase({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "", toArray("avi"), false },
                                              { null, toArray("avi"), false },

                                              { "苍老师.avi", toArray("mp3", "avi"), true },
                                              { "苍老师.avi", toArray("avi"), true },
                                              { "苍老师.avi", toArray("mp3"), false },
                                              { "苍老师.avi", toArray("Avi"), true },
                                              { "苍老师.avI", toArray("Avi"), true },

                                              { "/Users/苍老师", null, true },

                //
        };
        return toList(objects);
    }

    @Test
    public void test(){
        assertEquals(expectedValue, FilenameUtil.isExtensionIgnoreCase(input1, input2));
    }

}