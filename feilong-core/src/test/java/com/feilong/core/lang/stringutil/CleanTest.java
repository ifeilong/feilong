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
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.lang.StringUtil;
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class CleanTest extends Abstract1ParamAndResultParameterizedTest<CharSequence, String>{

    @Parameters(name = "index:{index}:StringUtil.clean({0})={1}")
    public static Iterable<Object[]> data(){
        return toList(//
                        toArray((String) null, EMPTY),
                        toArray(EMPTY, EMPTY),

                        toArray("   ", EMPTY),

                        toArray("a\bc", "ac"), //backspace

                        toArray("feil ong", "feilong"), //不间断空格 \u00A0
                        toArray("fei long", "feilong"), //不间断空格 \u00A0
                        toArray("fe ilong", "feilong"), //\u2007
                        toArray("feilo ng", "feilong"), //\u2007
                        toArray("feilong", "feilong"), //\u202F
                        toArray("feil ong", "feilong"), //\u202F
                        toArray("fe ilong", "feilong"), //\u202F

                        toArray("feil ong", "feil ong"), //普通空格
                        toArray("feil ong ", "feil ong"), //普通空格
                        toArray(" feil ong ", "feil ong"), //普通空格
                        toArray("feilong", "feilong")

        //                        " ", //不间断空格 \u00A0
        //                        " ", //\u2007
        //                        " ", //\u202F
        );
    }

    @Test
    public void test(){
        assertEquals(expectedValue, StringUtil.clean(input1));
    }
}