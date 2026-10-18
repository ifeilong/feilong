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
import static com.feilong.core.date.DateUtil.getFirstDateOfThisWeekUseMonday;
import static com.feilong.core.date.DateUtil.getLastDateOfThisWeekUseSunday;
import static com.feilong.core.date.DateUtil.getWeekStartAndEndPairMondayFirst;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.date.DateUtil.toDate;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.feilong.lib.lang3.tuple.Pair;

/**
 * {@link com.feilong.core.date.DateUtil#getWeekStartAndEndPairMondayFirst(java.util.Date)} 的单元测试.
 *
 * <p>
 * 按照中国制, 周一是开始时间, 周日是结束时间; 2012-10-16 是周二, 所在周是 2012-10-15(周一) ~ 2012-10-21(周日)
 * </p>
 */
public class GetWeekStartAndEndPairTest{

    @Test
    public void testGetWeekStartAndEndPairMondayFirst(){
        Date NOW = now();
        Pair<Date, Date> weekStartAndEndPair = getWeekStartAndEndPairMondayFirst();

        assertEquals(getFirstDateOfThisWeekUseMonday(NOW), weekStartAndEndPair.getLeft());
        assertEquals(getLastDateOfThisWeekUseSunday(NOW), weekStartAndEndPair.getRight());
    }

    //---------------------------------------------------------------

    @Test
    public void testGetWeekStartAndEndPairMondayFirst1(){
        Date date = toDate("2012-10-16 22:18:34", COMMON_DATE_AND_TIME);
        Pair<Date, Date> weekStartAndEndPair = getWeekStartAndEndPairMondayFirst(date);

        assertEquals(toDate("2012-10-15 00:00:00", COMMON_DATE_AND_TIME), weekStartAndEndPair.getLeft());
        assertEquals(toDate("2012-10-21 23:59:59.999", COMMON_DATE_AND_TIME_WITH_MILLISECOND), weekStartAndEndPair.getRight());
    }

    @Test(expected = NullPointerException.class)
    public void testGetWeekStartAndEndPairMondayFirstNullDate(){
        getWeekStartAndEndPairMondayFirst(null);
    }
}
