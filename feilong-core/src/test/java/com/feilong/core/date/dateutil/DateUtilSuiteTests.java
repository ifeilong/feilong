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

import com.feilong.core.date.dateutil.add.AddDayParameterizedTest;
import com.feilong.core.date.dateutil.add.AddHourParameterizedTest;
import com.feilong.core.date.dateutil.add.AddMillisecondParameterizedTest;
import com.feilong.core.date.dateutil.add.AddMinuteParameterizedTest;
import com.feilong.core.date.dateutil.add.AddMonthParameterizedTest;
import com.feilong.core.date.dateutil.add.AddSecondParameterizedTest;
import com.feilong.core.date.dateutil.add.AddTest;
import com.feilong.core.date.dateutil.add.AddWeekParameterizedTest;
import com.feilong.core.date.dateutil.add.AddYearParameterizedTest;
import com.feilong.core.date.dateutil.format.FormatDurationBeginAndEndDateTest;
import com.feilong.core.date.dateutil.format.FormatDurationDateTest;
import com.feilong.core.date.dateutil.format.FormatDurationLongTest;
import com.feilong.core.date.dateutil.get.GetDayOfMonthParameterizedTest;
import com.feilong.core.date.dateutil.get.GetDayOfMonthTest;
import com.feilong.core.date.dateutil.get.GetDayOfWeekParameterizedTest;
import com.feilong.core.date.dateutil.get.GetDayOfWeekTest;
import com.feilong.core.date.dateutil.get.GetDayOfYearParameterizedTest;
import com.feilong.core.date.dateutil.get.GetDayOfYearTest;
import com.feilong.core.date.dateutil.get.GetDayStartAndEndPairTest;
import com.feilong.core.date.dateutil.get.GetFirstDateOfThisDayTest;
import com.feilong.core.date.dateutil.get.GetFirstDateOfThisMonthTest;
import com.feilong.core.date.dateutil.get.GetFirstDateOfThisWeekTest;
import com.feilong.core.date.dateutil.get.GetFirstDateOfThisYearTest;
import com.feilong.core.date.dateutil.get.GetHourOfDayTest;
import com.feilong.core.date.dateutil.get.GetHourOfYearTest;
import com.feilong.core.date.dateutil.get.GetLastDateOfThisDayTest;
import com.feilong.core.date.dateutil.get.GetLastDateOfThisMonthTest;
import com.feilong.core.date.dateutil.get.GetLastDateOfThisWeekTest;
import com.feilong.core.date.dateutil.get.GetLastDateOfThisYearTest;
import com.feilong.core.date.dateutil.get.GetMinuteTest;
import com.feilong.core.date.dateutil.get.GetMonthParameterizedTest;
import com.feilong.core.date.dateutil.get.GetMonthStartAndEndPairTest;
import com.feilong.core.date.dateutil.get.GetMonthTest;
import com.feilong.core.date.dateutil.get.GetSecondOfDayTest;
import com.feilong.core.date.dateutil.get.GetSecondOfHourTest;
import com.feilong.core.date.dateutil.get.GetSecondTest;
import com.feilong.core.date.dateutil.get.GetTimeTest;
import com.feilong.core.date.dateutil.get.GetWeekOfYearParameterizedTest;
import com.feilong.core.date.dateutil.get.GetWeekOfYearTest;
import com.feilong.core.date.dateutil.get.GetYearParameterizedTest;
import com.feilong.core.date.dateutil.get.GetYearStartAndEndPairTest;
import com.feilong.core.date.dateutil.get.GetYearTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalDayParameterizedTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalDayTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalHourParameterizedTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalHourTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalMinuteParameterizedTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalMinuteTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalMonthParameterizedTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalMonthTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalSecondParameterizedTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalSecondTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalTimeParameterizedTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalTimeTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalWeekParameterizedTest;
import com.feilong.core.date.dateutil.getinterval.GetIntervalWeekTest;
import com.feilong.core.date.dateutil.is.IsAfterTest;
import com.feilong.core.date.dateutil.is.IsBeforeTest;
import com.feilong.core.date.dateutil.is.IsEqualsTest;
import com.feilong.core.date.dateutil.is.IsInTimeDateStringTest;
import com.feilong.core.date.dateutil.is.IsInTimeStringTest;
import com.feilong.core.date.dateutil.is.IsInTimeTest;
import com.feilong.core.date.dateutil.is.IsLeapYearParameterizedTest;
import com.feilong.core.date.dateutil.is.IsNotInTimeDateStringTest;
import com.feilong.core.date.dateutil.is.IsNotInTimeStringTest;
import com.feilong.core.date.dateutil.is.IsNotInTimeTest;
import com.feilong.core.date.dateutil.is.IsTodayTest;

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

                IsNotInTimeTest.class,
                IsNotInTimeDateStringTest.class,
                IsNotInTimeStringTest.class,

                IsBeforeTest.class,
                IsAfterTest.class,
                IsEqualsTest.class,
                IsTodayTest.class,

                ToDateTest.class,
                ToDateLongTest.class,
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
