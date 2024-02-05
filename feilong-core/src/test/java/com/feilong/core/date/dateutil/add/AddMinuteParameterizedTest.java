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
package com.feilong.core.date.dateutil.add;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.addMinute;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import com.feilong.test.Abstract2ParamsAndResultParameterizedTest;

/**
 * The Class DateUtilAddMinuteParameterizedTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class AddMinuteParameterizedTest extends Abstract2ParamsAndResultParameterizedTest<String, Integer, String>{

    /**
     * Data.
     *
     * @return the iterable
     */
    @Parameters(name = "index:{index}: DateUtil.addMinute({0},{1})={2}")
    public static Iterable<Object[]> data(){
        Object[][] objects = new Object[][] {
                                              { "2016-08-16 17:52:00", 0, "2016-08-16 17:52:00" },
                                              { "2016-08-16 17:52:00", 180, "2016-08-16 20:52:00" },
                                              { "2016-08-16 17:52:00", -180, "2016-08-16 14:52:00" },
                                              { "2016-08-16 23:59:59", 5, "2016-08-17 00:04:59" },
                                              { "2016-08-16 00:00:01", -5, "2016-08-15 23:55:01" },
                //
        };
        return toList(objects);
    }

    /**
     * Test add minute.
     */
    @Test
    public void testAddMinute(){
        Date date = toDate(input1, COMMON_DATE_AND_TIME);
        assertEquals(toDate(expectedValue, COMMON_DATE_AND_TIME), addMinute(date, input2));
    }

}