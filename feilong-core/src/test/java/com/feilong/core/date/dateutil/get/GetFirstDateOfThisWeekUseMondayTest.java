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
import static com.feilong.core.date.DateUtil.getDayOfWeek;
import static com.feilong.core.date.DateUtil.getFirstDateOfThisWeekUseMonday;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

/**
 * {@link com.feilong.core.date.DateUtil#getFirstDateOfThisWeekUseMonday(java.util.Date)} 的单元测试.
 *
 * <p>
 * 2016-08-22 是周一, 2026-10-19 是周一, 2016-08-28 是周日, 2014-01-01 是周三
 * </p>
 */
public class GetFirstDateOfThisWeekUseMondayTest{

    @Test
    public void testGetFirstDateOfThisWeekUseMonday(){
        //当天就是周一
        assertEquals(
                     toDate("2016-08-22 00:00:00", COMMON_DATE_AND_TIME),
                     getFirstDateOfThisWeekUseMonday(toDate("2016-08-22 01:00:00", COMMON_DATE_AND_TIME)));

        //2026-10-19 就是周一, 所以返回当天, 只把时分秒归零
        assertEquals(
                     toDate("2026-10-19 00:00:00", COMMON_DATE_AND_TIME),
                     getFirstDateOfThisWeekUseMonday(toDate("2026-10-19 23:59:59", COMMON_DATE_AND_TIME)));

        //周日, 所在周是 2016-08-22 ~ 2016-08-28
        assertEquals(
                     toDate("2016-08-22 00:00:00", COMMON_DATE_AND_TIME),
                     getFirstDateOfThisWeekUseMonday(toDate("2016-08-28 23:59:59", COMMON_DATE_AND_TIME)));

        //跨年
        assertEquals(
                     toDate("2013-12-30 00:00:00", COMMON_DATE_AND_TIME),
                     getFirstDateOfThisWeekUseMonday(toDate("2014-01-01 05:00:00", COMMON_DATE_AND_TIME)));
    }

    /**
     * Test get first date of this week use monday null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetFirstDateOfThisWeekUseMondayNull(){
        getFirstDateOfThisWeekUseMonday(null);
    }
}
