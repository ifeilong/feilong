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

/**
 * The Class EncodeParameterizedTest.
 */
public class EncodeDefaultUtf8ParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: URIUtil.encode({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        return new Object[][] {
                                { "Lifestyle / Graphic,", "Lifestyle+%2F+Graphic%2C" },
                                { "Lifestyle / Graphic,", "Lifestyle+%2F+Graphic%2C" },
                                { "%", "%25" },
                                { "%25", "%2525" },
                                { null, EMPTY },

                                // $ (Dollar Sign) becomes %24
                                // & (Ampersand) becomes %26
                                // + (Plus) becomes %2B
                                // , (Comma) becomes %2C
                                // : (Colon) becomes %3A
                                // ; (Semi-Colon) becomes %3B
                                // = (Equals) becomes %3D
                                // ? (Question Mark) becomes %3F
                                // @ (Commercial A / At) becomes %40

                                { "$", "%24" },
                                { "&", "%26" },
                                { "+", "%2B" },
                                { ",", "%2C" },
                                { ":", "%3A" },
                                { ";", "%3B" },
                                { "=", "%3D" },
                                { "?", "%3F" },
                                { "@", "%40" },

                                { EMPTY, EMPTY },
                                { SPACE, "+" }, //not %20
                                { "fei long", "fei+long" },

                                {
                                  "白色/黑色/纹理浅麻灰",
                                  "%E7%99%BD%E8%89%B2%2F%E9%BB%91%E8%89%B2%2F%E7%BA%B9%E7%90%86%E6%B5%85%E9%BA%BB%E7%81%B0" },
                //
        };
    }

    @Test
    public void testEncode(){
        assertEquals(expectedValue, URIUtil.encode(input1));
    }

}
