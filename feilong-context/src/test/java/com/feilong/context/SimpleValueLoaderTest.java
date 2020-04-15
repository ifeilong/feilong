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
package com.feilong.context;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class SimpleValueLoaderTest extends Abstract1ParamAndResultParameterizedTest<Object, Object>{

    @Parameters(name = "index:{index}: new SimpleValueLoader<Object>({0}).load()={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { 1, 1 },
                                              { "2", "2" },
                                              { 3.0, 3.0 },

                //
        };
        return toList(objects);
    }

    //---------------------------------------------------------------

    @Test
    public void test(){
        assertEquals(expectedValue, new SimpleValueLoader<Object>(input1).load());
    }
}
