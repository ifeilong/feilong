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
package com.feilong.core.util.maputil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.util.MapUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class GetDefaultEmptyStringIfNullTest extends Abstract2ParamsAndResultParameterizedTest<Map<Object, Object>, String, String>{

    /**
     * Test get value.
     */
    @Test
    public void testGetValue(){
        assertEquals(expectedValue, MapUtil.getDefaultEmptyStringIfNull(input1, input2));
    }

    @Parameters(name = "index:{index}:MapUtil.getDefaultEmptyStringIfNull(\"{0}\",\"{1}\")={2}")
    public static Iterable<Object[]> data(){
        return toList(//
                        ConvertUtil.<Object> toArray(null, "config_test_array", EMPTY),
                        toArray(toMap("name", "jim"), "config_test_array", EMPTY),
                        toArray(toMap("name", 7777), "name", "7777"),
                        toArray(toMap("name", 7777L), "name", "7777"),
                        toArray(toMap("name", "jim"), "name", "jim")
        //  
        );
    }
}
