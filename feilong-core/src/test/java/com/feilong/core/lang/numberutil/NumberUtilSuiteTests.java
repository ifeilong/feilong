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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
                NumberUtilTest.class,

                SetScaleTest.class,
                SetScaleParameterizedTest.class,
                SetScaleRoundingModeParameterizedTest.class,

                GetAddValueTest.class,
                GetAddValueParameterizedTest.class,

                GetSubtractValueTest.class,
                GetSubtractValueParameterizedTest.class,
                GetSubtractValueWithScaleTest.class,
                GetSubtractValueWithScaleParameterizedTest.class,

                GetMultiplyValueTest.class,
                GetMultiplyValueParameterizedTest.class,

                GetDivideValueTest.class,
                GetDivideValueParameterizedTest.class,

                //-------------GetProgress--------------------------------------------------

                GetProgressTest.class,
                GetProgressParameterizedTest.class,

                GetProgressDefaultPatternTest.class,
                GetProgressDefaultPatternParameterizedTest.class,

                //---------------------------------------------------------------

                ToStringTest.class,
                ToStringParameterizedTest.class,

                IsGreaterThanOrEqualsTest.class,
                IsGreaterThanOrEqualsParameterizedTest.class,
                IsGreaterThanTest.class,
                IsGreaterThanParameterizedTest.class,
                IsLessThanTest.class,
                IsLessThanParameterizedTest.class,
                IsLessThanOrEqualsTest.class,
                IsLessThanOrEqualsParameterizedTest.class,
                IsEqualsTest.class,
                IsEqualsParameterizedTest.class,
//
})
public class NumberUtilSuiteTests{

}
