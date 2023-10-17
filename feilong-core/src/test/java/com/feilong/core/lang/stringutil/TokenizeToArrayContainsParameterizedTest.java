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
package com.feilong.core.lang.stringutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.lang.StringUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class TokenizeToArrayContainsParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, Object, Boolean>{

    @Parameters(name = "index:{index}:StringUtil.tokenizeToArrayContains({0}, {1})={2}")
    public static Iterable<Object[]> data(){
        Integer i = 1;
        return toList(//
                        toArray((String) null, null, false),
                        toArray(null, EMPTY, false),
                        toArray(EMPTY, null, false),

                        toArray(EMPTY, EMPTY, false),

                        toArray("   ", EMPTY, false),

                        toArray("1,2,2,,3,4", "3", true),
                        toArray("1,2,2,,3,4", 3, true),

                        toArray("1,2,2,3,4", "1", true),
                        toArray("1,2,2,3,4", 1, true),
                        toArray("1,2,2,3,4", i, true),
                        toArray("1,2,2,3,4", (byte) 1, true),
                        toArray("1,2,2,3,4", 1L, true),
                        toArray("1,2,2,3,4", toBigDecimal(1), true),

                        toArray("1,2,2,3,4", Double.valueOf(1), true)

        //           
        );
    }

    @Test
    public void test(){
        assertEquals(expectedValue, StringUtil.tokenizeToArrayContains(input1, input2));
    }
}