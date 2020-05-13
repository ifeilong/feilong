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
package com.feilong.security.oneway;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.lib.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class MD5UtilParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: MD5Util.encode({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "123456", "e10adc3949ba59abbe56e057f20f883e" },

                                              { "abcdefgh", "e8dc4081b13434b45189a720b77b6818" },
                                              { "www.e-lining.com", "06800efdece5a6dfda22174730f24477" },
                                              { "WWW.RICARD.CN", "6f7c5c877d2b4ae3384b4daad366c19b" },
                                              { "liushuwen@ricard.cn", "efa0699c3f05371d32ec5023d9e55ac0" },
                                              { "sfexpress!@#$", "5d4db27d92073e85bfb307cf42738308" },
                                              { EMPTY, "d41d8cd98f00b204e9800998ecf8427e" },
                                              { "admin123456", "a66abb5684c45962d887564f08346e8d" },
                                              { "你好", "7eca689f0d3389d9dea66ae112e5cfd7" },
                                              { "000000", "670b14728ad9902aecba32e22fa4f6bd" },
                //
        };
        return toList(objects);
    }

    //---------------------------------------------------------------

    /**
     * Test to big decimal.
     */
    @Test
    public void testToBigDecimal(){
        assertEquals(expectedValue, MD5Util.encode(input1));
    }
}
