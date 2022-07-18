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
package com.feilong.core.date.dateutil;

import static com.feilong.core.DatePattern.COMMON_DATE;
import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.getIntervalMonth;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.test.Abstract3ParamsAndResultParameterizedTest;

public class GetIntervalMonthParameterizedTest extends Abstract3ParamsAndResultParameterizedTest<String, String, String, Integer>{

    @Parameters(name = "index:{index}:DateUtil.getIntervalMonth(toDate(\"{0}\",\"{2}\"), toDate(\"{1}\",\"{2}\"))={3}")
    public static Iterable<Object[]> data(){
        return toList(//
                        ConvertUtil.<Object> toArray("2020-11-20", "2022-07-18", COMMON_DATE, 20),

                        //0
                        toArray("2017-07-29", "2017-08-19", COMMON_DATE, 0),
                        toArray("2014-01-01 00:00:00", "2014-02-01 00:00:00", COMMON_DATE_AND_TIME, 1),

                        toArray("2017-07-29", "2017-09-19", COMMON_DATE, 1)
        //  
        );
    }

    @Test
    public void test(){
        assertEquals(expectedValue, (Integer) getIntervalMonth(toDate(input1, input3), toDate(input2, input3)));
    }
}