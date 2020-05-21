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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.io.FilenameUtil;
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class GetExtensionParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: FilenameUtil.getExtension({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "a.A", "A" },
                                              { "a.a", "a" },
                                              { "苍老师.avi", "avi" },
                                              { "苍老师.aVi", "aVi" },
                                              { "苍老师", EMPTY },
                                              { ".苍老师", "苍老师" },
                                              { "F:/pie2.png", "png" },
                                              { null, EMPTY },
                //
        };
        return toList(objects);
    }

    @Test
    public void test(){
        assertEquals(expectedValue, FilenameUtil.getExtension(input1));
    }

}