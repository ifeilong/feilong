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
package com.feilong.security.oneway.md5;

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.security.oneway.MD5Util;
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class Md5EncodeUpperCaseParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    @Parameters(name = "index:{index}: MD5Util.encodeUpperCase({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "123456", "E10ADC3949BA59ABBE56E057F20F883E" },

                                              { "abcdefgh", "E8DC4081B13434B45189A720B77B6818" },
                                              { "www.e-lining.com", "06800EFDECE5A6DFDA22174730F24477" },
                                              { "WWW.RICARD.CN", "6F7C5C877D2B4AE3384B4DAAD366C19B" },
                                              { "liushuwen@ricard.cn", "EFA0699C3F05371D32EC5023D9E55AC0" },
                                              { "sfexpress!@#$", "5D4DB27D92073E85BFB307CF42738308" },
                                              { EMPTY, "D41D8CD98F00B204E9800998ECF8427E" },
                                              { "admin123456", "A66ABB5684C45962D887564F08346E8D" },
                                              { "你好", "7ECA689F0D3389D9DEA66AE112E5CFD7" },
                                              { "000000", "670B14728AD9902AECBA32E22FA4F6BD" },
                //
        };
        return toList(objects);
    }

    //---------------------------------------------------------------

    @Test
    public void test(){
        assertEquals(expectedValue, MD5Util.encodeUpperCase(input1));
    }
}
