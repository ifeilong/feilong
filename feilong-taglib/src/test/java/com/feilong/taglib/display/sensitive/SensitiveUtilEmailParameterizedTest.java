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
package com.feilong.taglib.display.sensitive;

import static com.feilong.taglib.display.sensitive.SensitiveUtil.parse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

import static com.feilong.core.bean.ConvertUtil.toList;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.1
 */
public class SensitiveUtilEmailParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    private static final SensitiveConfig SENSITIVECONFIG = new SensitiveConfig(SensitiveType.EMAIL);

    @Parameters(name = "index:{index}: SensitiveUtil.parse(\"{0}\",new SensitiveConfig(SensitiveType.EMAIL))=\"{1}\"")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] {
                                              { "v", "v" },
                                              { "@163.com", "@163.com" },
                                              { "v@163.com", "v@163.com" },

                                              { "vv@163.com", "v*@163.com" },
                                              { "vvv@163.com", "v**@163.com" },
                                              { "vvVV@163.com", "v***@163.com" },

                                              { "vvVVv@163.com", "v***v@163.com" },
                                              { "vvVVvv@163.com", "v****v@163.com" },

                                              { "vvVVvvv@163.com", "vv***vv@163.com" },

                                              { "venus666drogon12345@163.com", "ve***************45@163.com" },
                //
        };
        return toList(objects);
    }

    @Test
    public void testParse(){
        assertEquals(expectedValue, parse(input1, SENSITIVECONFIG));
    }

}
