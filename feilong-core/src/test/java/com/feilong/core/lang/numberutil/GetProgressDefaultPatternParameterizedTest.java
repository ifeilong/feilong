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

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.core.lang.NumberUtil;
import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

public class GetProgressDefaultPatternParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<Number, Number, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}:NumberUtil.getProgress({0}, {1})=\"{2}\"")
    public static Iterable<Object[]> data(){
        return toList(
                        ConvertUtil.<Object> toArray(5, 5, "100.00%"),

                        toArray(5, 10, "50.00%"),

                        toArray(3, 10, "30.00%"),

                        toArray(1, 3, "33.33%"),
                        toArray(2, 3, "66.67%"),

                        toArray(1L, toBigDecimal(3), "33.33%"),

                        toArray(1L, 3L, "33.33%"),
                        toArray(1L, 3, "33.33%"),
                        toArray(1L, 3f, "33.33%"),
                        toArray(1d, 3f, "33.33%")
        //  
        );
    }

    /**
     * Test get progress.
     */
    @Test
    public void testGetProgress(){
        assertEquals(expectedValue, NumberUtil.getProgress(input1, input2));
    }
}
