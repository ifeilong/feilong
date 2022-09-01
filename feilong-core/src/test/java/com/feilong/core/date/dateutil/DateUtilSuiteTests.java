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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * The Class FeiLongDateUtilSuiteTests.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@RunWith(Suite.class)
@SuiteClasses({
                GetFirstDateOfThisYearTest.class,
                GetFirstDateOfThisMonthTest.class,
                GetFirstDateOfThisWeekTest.class,
                GetFirstDateOfThisDayTest.class,

                GetLastDateOfThisYearTest.class,
                GetLastDateOfThisMonthTest.class,
                GetLastDateOfThisWeekTest.class,
                GetLastDateOfThisDayTest.class,

                //---------------------------------------------------------------
                GetYearTest.class,
                GetMonthTest.class,
                GetWeekOfYearTest.class,

                GetDayOfYearTest.class,
                GetDayOfMonthTest.class,
                GetDayOfWeekTest.class,

                GetHourOfYearTest.class,
                GetHourOfDayTest.class,
                GetMinuteTest.class,
                GetSecondOfDayTest.class,
                GetSecondOfHourTest.class,
                GetSecondTest.class,
                GetTimeTest.class,

                //---------------------------------------------------------------
                GetYearParameterizedTest.class,
                GetMonthParameterizedTest.class,
                GetWeekOfYearParameterizedTest.class,
                GetDayOfYearParameterizedTest.class,
                GetDayOfMonthParameterizedTest.class,
                GetDayOfWeekParameterizedTest.class,

                AddTest.class,

                AddYearParameterizedTest.class,
                AddMonthParameterizedTest.class,
                AddWeekParameterizedTest.class,
                AddDayParameterizedTest.class,

                AddHourParameterizedTest.class,
                AddMinuteParameterizedTest.class,
                AddSecondParameterizedTest.class,
                AddMillisecondParameterizedTest.class,

                IsLeapYearParameterizedTest.class,

                IsInTimeTest.class,
                IsInTimeStringTest.class,
                IsInTimeDateStringTest.class,

                IsBeforeTest.class,
                IsAfterTest.class,
                IsEqualsTest.class,
                IsTodayTest.class,

                ToDateTest.class,
                ToDateNoPatternTest.class,

                ToStringTest.class,
                ToStringParameterizedTest.class,

                ToStringOldNewPatternParameterizedTest.class,
                ToStringOldNewPatternTest.class,

                NowStringTest.class,

                //---------------------------------------------------------------

                GetDayStartAndEndPairTest.class,
                GetMonthStartAndEndPairTest.class,
                GetYearStartAndEndPairTest.class,

                FormatDurationBeginAndEndDateTest.class,
                FormatDurationLongTest.class,
                FormatDurationDateTest.class,

                GetIntervalMonthTest.class,
                GetIntervalMonthParameterizedTest.class,

                GetIntervalWeekTest.class,
                GetIntervalWeekParameterizedTest.class,

                GetIntervalDayTest.class,
                GetIntervalDayParameterizedTest.class,

                GetIntervalHourTest.class,
                GetIntervalHourParameterizedTest.class,

                GetIntervalMinuteTest.class,
                GetIntervalMinuteParameterizedTest.class,

                GetIntervalSecondTest.class,
                GetIntervalSecondParameterizedTest.class,

                GetIntervalTimeTest.class,
                GetIntervalTimeParameterizedTest.class,

//                
})
public class DateUtilSuiteTests{

}
