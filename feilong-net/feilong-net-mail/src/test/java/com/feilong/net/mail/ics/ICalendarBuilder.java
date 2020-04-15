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
package com.feilong.net.mail.ics;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND;
import static com.feilong.core.date.DateUtil.getTime;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.date.DateUtil.toDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.mail.entity.ICalendar;

/**
 * The Class ICalendarBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.2
 */
public class ICalendarBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ICalendarBuilder.class);

    /**
     * Builds the.
     *
     * @return the i calendar
     * @since 1.10.2
     */
    public static ICalendar build(){
        ICalendar iCalendar = new ICalendar();

        iCalendar.setBeginDate(toDate("2017-03-13 20:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND));
        iCalendar.setEndDate(toDate("2017-03-13 21:00:00.000", COMMON_DATE_AND_TIME_WITH_MILLISECOND));
        iCalendar.setDescription("测试" + getTime(now()));
        iCalendar.setLocation("云立方9楼水星");
        iCalendar.setSummary("培训测试");
        return iCalendar;
    }
}
