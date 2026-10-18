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
import static com.feilong.core.date.DateUtil.getLastDateOfThisWeekUseSunday;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * {@link com.feilong.core.date.DateUtil#getLastDateOfThisWeekUseSunday(java.util.Date)} 的单元测试.
 *
 * <p>
 * 2016-08-22 是周一, 2016-08-28 是周日, 2014-12-31 是周三
 * </p>
 */
public class GetLastDateOfThisWeekUseSundayTest{

    @Test
    public void testGetLastDateOfThisWeekUseSunday(){
        assertEquals(
                     toDate("2026-10-25 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                     getLastDateOfThisWeekUseSunday(toDate("2026-10-19 01:00:00", COMMON_DATE_AND_TIME)));
        //周一, 后面还有6天到周日
        assertEquals(
                     toDate("2016-08-28 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                     getLastDateOfThisWeekUseSunday(toDate("2016-08-22 01:00:00", COMMON_DATE_AND_TIME)));

        //2026-10-19 也是周一, 所在周是 2026-10-19(周一) ~ 2026-10-25(周日)
        assertEquals(
                     toDate("2026-10-25 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                     getLastDateOfThisWeekUseSunday(toDate("2026-10-19 23:59:59", COMMON_DATE_AND_TIME)));

        //当天就是周日
        assertEquals(
                     toDate("2016-08-28 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                     getLastDateOfThisWeekUseSunday(toDate("2016-08-28 01:00:00", COMMON_DATE_AND_TIME)));

        //2016-10-19 是周三, 所在周是 2016-10-17(周一) ~ 2016-10-23(周日)
        assertEquals(
                     toDate("2016-10-23 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                     getLastDateOfThisWeekUseSunday(toDate("2016-10-19 23:59:59", COMMON_DATE_AND_TIME)));

        //跨年
        assertEquals(
                     toDate("2015-01-04 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND),
                     getLastDateOfThisWeekUseSunday(toDate("2014-12-31 05:00:00", COMMON_DATE_AND_TIME)));
    }

    /**
     * Test get last date of this week use sunday null.
     */
    @Test(expected = NullPointerException.class)
    public void testGetLastDateOfThisWeekUseSundayNull(){
        getLastDateOfThisWeekUseSunday(null);
    }
}
