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

import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.feilong.core.date.DateUtil;
import com.feilong.lib.lang3.time.DateUtils;
import com.feilong.net.mail.entity.ICalendar;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.template.TemplateUtil;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.2
 */
public class IcsBuilder{

    private static final String templateInClassPath = "ics.vm";

    /**
     * @param mailSendRequest
     * @return
     * @since 1.10.2
     */
    static String buildIcs1(MailSendRequest mailSendRequest){
        //T代表后面跟着时间，Z代表UTC统一时间

        ICalendar getiCalendar = mailSendRequest.getiCalendar();
        Map<String, Object> map = newHashMap();
        map.put("uuid", UUID.randomUUID().toString());
        map.put("beginDate", toUTC(getiCalendar.getBeginDate()));
        map.put("endDate", toUTC(getiCalendar.getEndDate()));
        map.put("description", getiCalendar.getDescription());
        map.put("location", getiCalendar.getLocation());
        map.put("now", toUTC(now()));
        map.put("summary", getiCalendar.getSummary());

        return TemplateUtil.parseTemplate(templateInClassPath, map);
    }

    public static String buildIcs(MailSendRequest mailSendRequest){
        ICalendar icalendar = mailSendRequest.getiCalendar();

        StringBuffer buffer = new StringBuffer();
        buffer.append(
                        "BEGIN:VCALENDAR\n" //
                                        + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" //
                                        + "VERSION:2.0\n" //

                                        + "METHOD:REQUEST\n" //
                                        + "BEGIN:VEVENT\n" //

                                        + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + mailSendRequest.getTos()[0] + "\n" //
                                        + "ORGANIZER:MAILTO:" + mailSendRequest.getTos()[0] + "\n" //

                                        + "DTSTART:20200322T060000Z\n" //
                                        + "DTEND:20200322T070000Z\n" //
                                        + "LOCATION:" + icalendar.getLocation() + "\n" //

                                        + "UID:" + UUID.randomUUID().toString() + "\n"//如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。  // 
                                        + "CATEGORIES:SuccessCentral Reminder\n" //
                                        + "DESCRIPTION:" + icalendar.getDescription() + "" //
                                        + "SUMMARY:" + icalendar.getSummary() + "\n" //
                                        + "PRIORITY:5\n" //
                                        + "CLASS:PUBLIC\n"//

                                        + "BEGIN:VALARM\n" // 
                                        + "TRIGGER:-PT15M\n"//
                                        + "ACTION:DISPLAY\n" //
                                        + "DESCRIPTION:Reminder\n"//
                                        + "END:VALARM\n" //

                                        + "END:VEVENT\n"//

                                        + "END:VCALENDAR");

        return buffer.toString();
    }

    /**
     * @param datePattern_utc
     * @param date
     * @return
     * @since 1.10.2
     */
    static String toUTC(Date date){
        Calendar calendar = DateUtils.toCalendar(date);

        // 取得时间偏移量：
        int zoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        // 取得夏令时差：
        int dstOffset = calendar.get(Calendar.DST_OFFSET);

        // 从本地时间里扣除这些差量，即可以取得UTC时间：
        calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

        //UTC(世界标准时间)协调世界时，又称世界标准时间或世界协调时间，简称UTC（从英文“Coordinated Universal Time”／法文“Temps Universel Coordonné”而来），
        //是最主要的世界时间标准，其以原子时秒长为基础，在时刻上尽量接近于格林尼治标准时间。
        String datePattern_utc = "yyyyMMdd'T'HHmmss'Z'";
        return DateUtil.toString(calendar.getTime(), datePattern_utc);
    }
}
