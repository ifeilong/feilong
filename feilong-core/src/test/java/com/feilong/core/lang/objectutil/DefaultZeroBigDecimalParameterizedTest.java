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
package com.feilong.core.lang.objectutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.lang.ObjectUtil;
import com.feilong.test.Abstract1ParamAndResultParameterizedTest;

public class DefaultZeroBigDecimalParameterizedTest extends Abstract1ParamAndResultParameterizedTest<BigDecimal, BigDecimal>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: ObjectUtil.defaultZero({0})={1}")
    public static Iterable<Object[]> data(){
        Object[][] objects = build();
        return toList(objects);
    }

    private static Object[][] build(){
        //-0.00 的 unscaled value 是0, compareTo(BigDecimal.ZERO) 是0, 不算负数, 原样返回(scale 保留)
        return new Object[][] { //
                                new Object[] { null, BigDecimal.ZERO },
                                new Object[] { new BigDecimal("-1.00"), BigDecimal.ZERO },
                                new Object[] { new BigDecimal("12.30"), new BigDecimal("12.30") },

                                new Object[] { new BigDecimal("-0.00"), new BigDecimal("0.00") },
                                new Object[] { new BigDecimal("-99999999999999999999999999999999.99"), BigDecimal.ZERO },
                                new Object[] { new BigDecimal("99999999999999999999999999999999.99"),
                                                new BigDecimal("99999999999999999999999999999999.99") } };
    }

    @Test
    public void test(){
        assertEquals(expectedValue, ObjectUtil.defaultZero(input1));
    }

}
