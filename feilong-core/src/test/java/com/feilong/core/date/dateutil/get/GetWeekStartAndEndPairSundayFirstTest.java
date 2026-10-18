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
import static com.feilong.core.date.DateUtil.getFirstDateOfThisWeek;
import static com.feilong.core.date.DateUtil.getLastDateOfThisWeek;
import static com.feilong.core.date.DateUtil.getWeekStartAndEndPairSundayFirst;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.feilong.lib.lang3.tuple.Pair;

/**
 * {@link com.feilong.core.date.DateUtil#getWeekStartAndEndPairSundayFirst(java.util.Date)} 的单元测试.
 *
 * <p>
 * 按照外国制, 周日为第一天, 周六为最后一天.<br>
 * 这里不写死具体日期, 而是和 {@code getFirstDateOfThisWeek} / {@code getLastDateOfThisWeek} 对比,<br>
 * 避免因为机器默认 {@link java.util.Locale} 的 {@link java.util.Calendar#getFirstDayOfWeek()} 不同而失败.
 * </p>
 */
public class GetWeekStartAndEndPairSundayFirstTest{

    @Test
    public void testGetWeekStartAndEndPairSundayFirst(){
        Date NOW = now();
        Pair<Date, Date> weekStartAndEndPair = getWeekStartAndEndPairSundayFirst();

        assertEquals(getFirstDateOfThisWeek(NOW), weekStartAndEndPair.getLeft());
        assertEquals(getLastDateOfThisWeek(NOW), weekStartAndEndPair.getRight());
    }

    //---------------------------------------------------------------

    @Test
    public void testGetWeekStartAndEndPairSundayFirst1(){
        Date date = toDate("2012-10-16 22:18:34", COMMON_DATE_AND_TIME);
        Pair<Date, Date> weekStartAndEndPair = getWeekStartAndEndPairSundayFirst(date);

        assertEquals(getFirstDateOfThisWeek(date), weekStartAndEndPair.getLeft());
        assertEquals(getLastDateOfThisWeek(date), weekStartAndEndPair.getRight());
    }

    @Test(expected = NullPointerException.class)
    public void testGetWeekStartAndEndPairSundayFirstNullDate(){
        getWeekStartAndEndPairSundayFirst(null);
    }
}
