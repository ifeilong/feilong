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
import static com.feilong.core.lang.ObjectUtil.defaultFalseIfNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class DefaultFalseIfNullParameterizedTest extends Abstract1ParamAndResultParameterizedTest<Boolean, Boolean>{

    @Parameters(name = "index:{index}: ObjectUtil.defaultFalseIfNull({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] { //
                                new Object[] { null, false },
                                new Object[] { true, true },
                                new Object[] { false, false }

        };
    }

    @Test
    public void test(){
        assertEquals(expectedValue, defaultFalseIfNull(input1));
    }

}