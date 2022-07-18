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
package com.feilong.json.format.calendar;

import static com.feilong.core.DatePattern.COMMON_DATE_AND_TIME;
import static com.feilong.core.date.DateUtil.toDate;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.Calendar;
import java.util.List;

public class DateUtils{

    public static List<Calendar> build(String begin,String end){
        Calendar beginDate = Calendar.getInstance();
        beginDate.setTime(toDate(begin, COMMON_DATE_AND_TIME));

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(toDate(end, COMMON_DATE_AND_TIME));

        List<Calendar> list = newArrayList();

        while (!beginDate.after(endDate)){
            beginDate.set(Calendar.MINUTE, 0);
            beginDate.set(Calendar.SECOND, 0);
            beginDate.set(Calendar.MILLISECOND, 0);

            list.add(beginDate);

            beginDate.add(Calendar.HOUR_OF_DAY, 1);

            if (beginDate.get(Calendar.MONTH) == endDate.get(Calendar.MONTH) && //
                            beginDate.get(Calendar.DATE) == endDate.get(Calendar.DATE) && //
                            beginDate.get(Calendar.HOUR_OF_DAY) == endDate.get(Calendar.HOUR_OF_DAY)){
                break;
            }
        }
        return list;
    }
}
