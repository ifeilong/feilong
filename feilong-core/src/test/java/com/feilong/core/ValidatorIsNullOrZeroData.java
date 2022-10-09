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

import java.math.BigDecimal;

import com.feilong.test.TestUtil;

class ValidatorIsNullOrZeroData{

    public static Iterable<Object[]> buildData(){
        Object[] nullOrZeroElement = {
                                       null,
                                       0, //
                                       0.0f,
                                       0.0d,
                                       0L,

                                       new Integer(0),
                                       new Long(0),
                                       new Float(0),
                                       new Double(0),
                                       new Short("0"),

                                       new BigDecimal("0"),

                                       BigDecimal.ZERO

        };

        //---------------------------------------------------------------

        Object[] notNullOrZeroElement = { //
                                          0.1f,
                                          0.1d,
                                          0.001f,
                                          0.001d,
                                          -0.1d,
                                          -0.1f,
                                          -0.001d,
                                          -0.001f,
                                          1,
                                          1L,
                                          1f,
                                          1d,
                                          new BigDecimal("1") };

        return TestUtil.toDataList(nullOrZeroElement, notNullOrZeroElement);
    }

}
