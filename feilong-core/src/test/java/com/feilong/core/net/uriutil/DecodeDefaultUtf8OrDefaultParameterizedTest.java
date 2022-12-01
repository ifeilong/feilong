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
package com.feilong.core.net.uriutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.lang.StringUtil.SPACE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.net.URIUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class DecodeDefaultUtf8OrDefaultParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, String, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: URIUtil.decodeIfExceptionDefault({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] {
                                { "%E9%A3%9E%E5%A4%A9%E5%A5%94%E6%9C%88", "defaultValue", "飞天奔月" },
                                { null, "defaultValue", EMPTY },
                                { EMPTY, "defaultValue", EMPTY },
                                { SPACE, "defaultValue", SPACE },

                                { "+", "defaultValue", SPACE },

                                { "%24", "defaultValue", "$" },
                                { "%26", "defaultValue", "&" },
                                { "%2B", "defaultValue", "+" },
                                { "%2C", "defaultValue", "," },
                                { "%3A", "defaultValue", ":" },
                                { "%3B", "defaultValue", ";" },
                                { "%3D", "defaultValue", "=" },
                                { "%3F", "defaultValue", "?", },
                                { "%40", "defaultValue", "@", },
                                { "fei+long", "defaultValue", "fei long", },

                                //异常

                                { "%", "defaultValue", "defaultValue", },
                                { "%c", "defaultValue", "defaultValue", },
                                { "%E9%A3%9E%E5%A4%A9%E5%A5%94%E6%9C%88%", "defaultValue", "defaultValue", },

                                // java.lang.IllegalArgumentException: URLDecoder: Illegal hex characters in escape (%) pattern - For input string: "ch"
                                { "aaaaa%chu111", "defaultValue", "defaultValue", },

                //
        };
    }

    @Test
    public void test(){
        assertEquals(expectedValue, URIUtil.decodeIfExceptionDefault(input1, input2));
    }

}
