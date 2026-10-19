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
package com.feilong.core.date.dateutil.get;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;
import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND;
import static com.feilong.core.date.DateUtil.getLastDateOfThisWeek;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

/**
 * {@link com.feilong.core.date.DateUtil#getLastDateOfThisWeek(java.util.Date)} 的单元测试.
 *
 * <p>
 * 按照外国制, 周日为一个星期第一天, 周六为最后一天.<br>
 * 用例里的日期: 2016-08-21 是周日, 2016-08-22 是周一, 2016-08-25 是周四, 2016-08-27 是周六, 2016-09-01 是周四
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class GetLastDateOfThisWeekTest{

    /**
     * Test get last date of this week.
     */
    @Test
    public void testGetLastDateOfThisWeek(){
        //周一, 往后推 5 天到周六
        assertEquals(
                        toDate("2016-08-27 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                        getLastDateOfThisWeek(toDate("2016-08-22 01:00:00", COMMON_DATE_AND_TIME)));

        //跨年
        assertEquals(
                        toDate("2015-01-03 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                        getLastDateOfThisWeek(toDate("2014-12-31 05:00:00", COMMON_DATE_AND_TIME)));
    }

    /**
     * 输入就是周六(一周最后一天), 返回当天.
     */
    @Test
    public void testGetLastDateOfThisWeekWhenInputIsSaturday(){
        assertEquals(
                        toDate("2016-08-27 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                        getLastDateOfThisWeek(toDate("2016-08-27 00:00:00", COMMON_DATE_AND_TIME)));
    }

    /**
     * 输入是周日(一周第一天), 要往后推 6 天.
     */
    @Test
    public void testGetLastDateOfThisWeekWhenInputIsSunday(){
        assertEquals(
                        toDate("2016-08-27 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                        getLastDateOfThisWeek(toDate("2016-08-21 12:00:00", COMMON_DATE_AND_TIME)));
    }

    /**
     * 跨月: 2016-09-01 是周四, 所在周是 2016-08-28(周日) ~ 2016-09-03(周六).
     */
    @Test
    public void testGetLastDateOfThisWeekCrossMonth(){
        assertEquals(
                        toDate("2016-09-03 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                        getLastDateOfThisWeek(toDate("2016-09-01 08:30:00", COMMON_DATE_AND_TIME)));
    }

    /**
     * 时分秒毫秒要归到 23:59:59.999.
     */
    @Test
    public void testGetLastDateOfThisWeekResetTime(){
        assertEquals(
                        toDate("2016-08-27 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                        getLastDateOfThisWeek(toDate("2016-08-25 00:00:00", COMMON_DATE_AND_TIME)));
    }

    //---------------------------------------------------------------

    /**
     * 一周从哪一天算起, 不应该影响本方法的结果.
     *
     * <p>
     * 德国等欧洲国家的 {@link java.util.Calendar#getFirstDayOfWeek()} 是周一(周一为第一天);<br>
     * 旧实现使用 <code>Calendar.set(DAY_OF_WEEK, SATURDAY)</code>, 在这种环境下会算到下一周的周六.
     * </p>
     */
    @Test
    public void testGetLastDateOfThisWeekNotAffectedByLocale(){
        Locale defaultLocale = Locale.getDefault();
        try{
            Locale.setDefault(Locale.GERMANY);

            assertEquals(
                            toDate("2016-08-27 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                            getLastDateOfThisWeek(toDate("2016-08-22 01:00:00", COMMON_DATE_AND_TIME)));
        }finally{
            Locale.setDefault(defaultLocale);
        }
    }

    /**
     * Test get last date of this week null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetLastDateOfThisWeekNull(){
        getLastDateOfThisWeek(null);
    }
}
