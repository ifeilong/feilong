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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.taglib.display.sensitive.SensitiveUtil.parse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.1
 */
public class SensitiveUtilMobile33ParameterizedTest extends Abstract1ParamAndResultParameterizedTest<String, String>{

    private static final SensitiveConfig SENSITIVECONFIG = new SensitiveConfig("mobile33");

    @Parameters(name = "index:{index}: SensitiveUtil.parse(\"{0}\",new SensitiveConfig(SensitiveType.MOBILE))=\"{1}\"")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] { //
                                              { "15001231318", "150*****318" },
                //
        };
        return toList(objects);
    }

    @Test
    public void testParse(){
        assertEquals(expectedValue, parse(input1, SENSITIVECONFIG));
    }
}
