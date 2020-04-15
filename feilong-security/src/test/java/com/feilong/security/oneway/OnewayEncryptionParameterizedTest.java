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
import static com.feilong.security.oneway.OnewayType.MD5;
import static com.feilong.security.oneway.OnewayType.SHA;
import static com.feilong.security.oneway.OnewayType.SHA1;
import static com.feilong.security.oneway.OnewayType.SHA256;
import static com.feilong.security.oneway.OnewayType.SHA384;
import static com.feilong.security.oneway.OnewayType.SHA512;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class OnewayEncryptionParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<OnewayType, String, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: MD5Util.encode({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { MD5, "你好", "7eca689f0d3389d9dea66ae112e5cfd7" },
                                              { SHA, "你好", "440ee0853ad1e99f962b63e459ef992d7c211722" },
                                              { SHA1, "你好", "440ee0853ad1e99f962b63e459ef992d7c211722" },
                                              { SHA256, "你好", "670d9743542cae3ea7ebe36af56bd53648b0a1126162e78d81a32934a711302e" },
                                              {
                                                SHA384,
                                                "你好",
                                                "05f076c7d180e91d80a56d70b226fca01e2353554c315ac1e8caaaeca2ce0dc0d9d84e206a2bf1143a0ae1b9be9bcfa8" },
                                              {
                                                SHA512,
                                                "你好",
                                                "5232181bc0d9888f5c9746e410b4740eb461706ba5dacfbc93587cecfc8d068bac7737e92870d6745b11a25e9cd78b55f4ffc706f73cfcae5345f1b53fb8f6b5" },

                //
        };
        return toList(objects);
    }

    /**
     * Test to big decimal.
     */
    @Test
    public void testToBigDecimal(){
        assertEquals(expectedValue, OnewayEncryption.encode(input1, input2));
    }
}
