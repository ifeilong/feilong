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
import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.NumberUtil;
import com.feilong.test.Abstract3ParamsAndResultParameterizedTest;

/**
 * The Class NumberUtilGetMultiplyValueParameterizedTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class GetMultiplyValueParameterizedTest
                extends Abstract3ParamsAndResultParameterizedTest<Number, Number, Integer, BigDecimal>{

    /**
     * Test get multiply value.
     */
    @Test
    public void testGetMultiplyValue(){
        assertEquals(expectedValue, NumberUtil.getMultiplyValue(input1, input2, input3));
    }

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}:NumberUtil.getMultiplyValue({0}, {1}, {2})={3}")
    public static Iterable<Object[]> data(){
        return toList(
                        ConvertUtil.<Object> toArray(new BigDecimal(6.25), 1.17, 5, toBigDecimal("7.31250")),
                        toArray(5, 2, 5, toBigDecimal("10.00000")),

                        toArray(5, 2, 0, toBigDecimal("10")),
                        toArray(9.86, 100, 0, toBigDecimal("986"))
        //  
        );
    }

}
