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
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class FormatPatternParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, Object[], String>{

    @Test
    public void test(){
        assertEquals(expectedValue, StringUtil.formatPattern(input1, input2));
    }

    @Parameters(name = "index:{index}:StringUtil.formatPattern(\"{0}\", {1})=\"{2}\"")
    public static Iterable<Object[]> data(){
        Object[] arrays = new Object[] { null, toArray(), EMPTY };
        return toList(
                        arrays,
                        toArray(null, toArray("今天", "aaaa"), EMPTY), // null
                        toArray("", (Object) null, EMPTY), // null

                        toArray("{},{}", toArray("今天", "aaaa"), "今天,aaaa"),
                        toArray("", toArray("今天", "aaaa"), "")

        //        
        );
    }
}