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
import static com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY;
import static org.junit.Assert.assertArrayEquals;

import java.io.Serializable;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.lang.StringUtil;
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class TokenizeToStringArrayDefaultDelimitersParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String[]>{

    @Parameters(name = "index:{index}:StringUtil.tokenizeToStringArray({0})={1}")
    public static Iterable<Object[]> data(){
        Serializable[] array = toArray(null, EMPTY_STRING_ARRAY);
        Serializable[] array2 = toArray("", EMPTY_STRING_ARRAY);
        return toList(//
                        array,
                        array2,
                        toArray("   ", EMPTY_STRING_ARRAY),
                        toArray("jin.xin  feilong ,jinxin;venusdrogon;jim ", toArray("jin.xin", "feilong", "jinxin", "venusdrogon", "jim")),
                        toArray(
                                        "jin.xin  feilong ,jinxin;1@1,venusdrogon;jim ",
                                        toArray("jin.xin", "feilong", "jinxin", "1@1", "venusdrogon", "jim"))

        //           
        );
    }

    @Test
    public void tokenizeToStringArray(){
        assertArrayEquals(expectedValue, StringUtil.tokenizeToStringArray(input1));
    }

}