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
package com.feilong.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.AbstractBooleanParameterizedTest;

public class ValidatorIsNullOrZeroParameterizedTest extends AbstractBooleanParameterizedTest<Number, Boolean>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: Validator.isNullOrZero({0})={1}")
    public static Iterable<Object[]> data(){
        return ValidatorIsNullOrZeroData.buildData();
    }

    /**
     * Test is null or empty.
     */
    @Test
    public void testIsNullOrEmpty(){
        assertEquals(expectedValue, Validator.isNullOrZero(input));
    }
}