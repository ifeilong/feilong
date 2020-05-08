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
package com.feilong.core.date.dateutiltest;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;
import static com.feilong.core.date.DateUtil.addDay;
import static com.feilong.core.date.DateUtil.addHour;
import static com.feilong.core.date.DateUtil.addMillisecond;
import static com.feilong.core.date.DateUtil.addMinute;
import static com.feilong.core.date.DateUtil.addMonth;
import static com.feilong.core.date.DateUtil.addSecond;
import static com.feilong.core.date.DateUtil.addWeek;
import static com.feilong.core.date.DateUtil.addYear;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

/**
 * The Class DateUtilAddTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class AddTest{
    //********com.feilong.core.date.DateUtil.addYear(Date, int)****************************

    @Test(expected = NullPointerException.class)
    public void testAddYearNullDate(){
        addYear(null, 5);
    }

    /**
     * Test add year 3.
     */
    @Test
    public void testAddYear3(){
        Date date = toDate("0001-01-01 01:21:00", COMMON_DATE_AND_TIME);
        Date addYear = addYear(date, -3);
        assertEquals(toDate("公元前 0003-01-01 01:21:00", "G yyyy-MM-dd HH:mm:ss"), addYear);
    }

    //**********com.feilong.core.date.DateUtil.addMonth(Date, int)********************
    @Test(expected = NullPointerException.class)
    public void testAddMonthNullDate(){
        addMonth(null, 5);
    }

    //************com.feilong.core.date.DateUtil.addWeek(Date, int)****************************

    @Test(expected = NullPointerException.class)
    public void testAddWeekNullDate(){
        addWeek(null, 5);
    }

    //**************com.feilong.core.date.DateUtil.addDay(Date, int)******************************
    @Test(expected = NullPointerException.class)
    public void testAddDayNullDate(){
        addDay(null, 5);
    }

    //**********com.feilong.core.date.DateUtil.addHour(Date, int)**********
    @Test(expected = NullPointerException.class)
    public void testAddHourNullDate(){
        addHour(null, 5);
    }

    //***********com.feilong.core.date.DateUtil.addMinute(Date, int)*****************

    @Test(expected = NullPointerException.class)
    public void testAddMinuteNullDate(){
        addMinute(null, 5);
    }

    //*************com.feilong.core.date.DateUtil.addSecond(Date, int)********************

    @Test(expected = NullPointerException.class)
    public void testAddSecondNullDate(){
        addSecond(null, 5);
    }

    //*************com.feilong.core.date.DateUtil.addMillisecond(Date, int)******************

    @Test(expected = NullPointerException.class)
    public void testAddMillisecondNullDate(){
        addMillisecond(null, 5);
    }

}