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
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class DecodeDefaultUtf8ParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: URIUtil.decode({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] {
                                { "%E9%A3%9E%E5%A4%A9%E5%A5%94%E6%9C%88", "飞天奔月" },
                                { null, EMPTY },
                                { EMPTY, EMPTY },
                                { SPACE, SPACE },

                                { "+", SPACE },

                                { "%24", "$" },
                                { "%26", "&" },
                                { "%2B", "+" },
                                { "%2C", "," },
                                { "%3A", ":" },
                                { "%3B", ";" },
                                { "%3D", "=" },
                                { "%3F", "?", },
                                { "%40", "@", },

                                { "fei+long", "fei long", },

                //
        };
    }

    @Test
    public void test(){
        assertEquals(expectedValue, URIUtil.decode(input1));
    }

}
