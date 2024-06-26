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
package com.feilong.core.lang.numberutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.bean.ConvertUtil.toInteger;
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.NumberUtil;
import com.feilong.test.Abstract3ParamsAndResultParameterizedTest;

public class GetSubtractValueWithScaleParameterizedTest
                extends Abstract3ParamsAndResultParameterizedTest<Number, Number, Integer, BigDecimal>{

    @Parameters(name = "index:{index}:NumberUtil.getSubtractValue({0}, {1}, {2})={3}")
    public static Iterable<Object[]> data(){
        return toList(
                        ConvertUtil.<Object> toArray(0, 2, 0, toBigDecimal(-2)),
                        toArray(0, 2, 2, toBigDecimal("-2.00")),

                        toArray(0, null, 0, toBigDecimal(0)),
                        toArray(0, null, 2, toBigDecimal("0.00")),

                        toArray(0, toInteger(5), 0, toBigDecimal(-5)),
                        toArray(0, toInteger(5), 2, toBigDecimal("-5.00")),

                        toArray(1000, 50, 0, toBigDecimal(950)),
                        toArray(-1000, 50, 0, toBigDecimal(-1050)),

                        toArray(2, 1.1, 1, toBigDecimal("0.9")),
                        toArray(2, 1.1, 2, toBigDecimal("0.90"))

        //
        );
    }

    @Test
    public void testGetSubtractValue(){
        assertEquals(expectedValue, NumberUtil.getSubtractValueWithScale(input1, input2, input3));
    }

}
