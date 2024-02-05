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
package com.feilong.core.lang.enumutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.entity.HttpMethodTestType;
import com.feilong.core.lang.EnumUtil;
import com.feilong.test.Abstract3ParamsAndResultParameterizedTest;

/**
 * The Class EnumUtilGetEnumByPropertyValueParameterizedTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @param <E>
 *            the element type
 */
public class GetEnumByPropertyValueParameterizedTest<T, E extends Enum<?>>
                extends Abstract3ParamsAndResultParameterizedTest<Class<E>, String, T, E>{

    /**
     * Test get enum by property value.
     */
    @Test
    public void testGetEnumByPropertyValue(){
        assertEquals(expectedValue, EnumUtil.getEnumByPropertyValue(input1, input2, input3));
    }

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}:EnumUtil.getEnumByPropertyValue({0},{1},{2})={3}")
    public static Iterable<Object[]> data(){
        return toList(//
                        ConvertUtil.<Object> toArray(HttpMethodTestType.class, "method", "get", HttpMethodTestType.GET),
                        toArray(HttpMethodTestType.class, "method", "post", HttpMethodTestType.POST),

                        toArray(HttpMethodTestType.class, "method", "pOst", null),
                        toArray(HttpMethodTestType.class, "method", "POST", null),
                        toArray(HttpMethodTestType.class, "method", "posT", null),

                        toArray(HttpMethodTestType.class, "method", "gEt", null),
                        toArray(HttpMethodTestType.class, "method", "geT", null),
                        toArray(HttpMethodTestType.class, "method", "GET", null),

                        toArray(HttpMethodTestType.class, "method", "post111", null),
                        toArray(HttpMethodTestType.class, "method", "", null),
                        toArray(HttpMethodTestType.class, "method", null, null)
        //  
        );
    }
}
