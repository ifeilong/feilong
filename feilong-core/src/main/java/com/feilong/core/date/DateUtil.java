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
package com.feilong.core.date;

import static com.feilong.core.TimeInterval.MILLISECOND_PER_DAY;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_HOUR;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_MINUTE;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_MONTH;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;
import static com.feilong.core.TimeInterval.MILLISECOND_PER_WEEK;
import static com.feilong.core.TimeInterval.SECONDS_PER_HOUR;
import static com.feilong.core.TimeInterval.SECONDS_PER_MINUTE;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.CalendarUtil.resetDayBegin;
import static com.feilong.core.date.CalendarUtil.resetDayEnd;
import static com.feilong.core.date.CalendarUtil.resetYearEnd;
import static com.feilong.core.lang.StringUtil.formatPattern;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.JANUARY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SATURDAY;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.SUNDAY;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import com.feilong.core.DatePattern;
import com.feilong.core.TimeInterval;
import com.feilong.core.Validate;
import com.feilong.lib.lang3.time.DateFormatUtils;
import com.feilong.lib.lang3.time.DateUtils;
import com.feilong.lib.lang3.tuple.Pair;

/**
 * {@link java.util.Date}操作工具类(feilong-core核心类之一).
 * 
 * <h3>常用方法:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>字符串转日期</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#toDate(String, String...)}</li>
 * </ul>
 * </td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>日期转字符串</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#toString(Date, String)}</li>
 * </ul>
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>日期加减</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#addDay(Date, int)}</li>
 * <li>{@link DateUtil#addHour(Date, int)}</li>
 * <li>{@link DateUtil#addMinute(Date, int)}</li>
 * <li>{@link DateUtil#addMonth(Date, int)}</li>
 * <li>{@link DateUtil#addSecond(Date, int)}</li>
 * <li>{@link DateUtil#addWeek(Date, int)}</li>
 * <li>{@link DateUtil#addYear(Date, int)}</li>
 * </ul>
 * </td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>获得日期某部值</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#getDayOfMonth(Date)}</li>
 * <li>{@link DateUtil#getDayOfWeek(Date)}</li>
 * <li>{@link DateUtil#getDayOfYear(Date)}</li>
 * <li>{@link DateUtil#getHourOfDay(Date)}</li>
 * <li>{@link DateUtil#getHourOfYear(Date)}</li>
 * <li>{@link DateUtil#getMinute(Date)}</li>
 * <li>{@link DateUtil#getMonth(Date)}</li>
 * <li>{@link DateUtil#getSecond(Date)}</li>
 * <li>{@link DateUtil#getYear(Date)}</li>
 * <li>{@link DateUtil#getTime(Date)}</li>
 * </ul>
 * </td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>判断闰年</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#isLeapYear(int)}</li>
 * </ul>
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>判断相等</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#isEquals(Date, Date, String)}</li>
 * </ul>
 * </td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>判断早晚</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#isBefore(Date, Date)}</li>
 * <li>{@link DateUtil#isBefore(Date, Date)}</li>
 * </ul>
 * </td>
 * </tr>
 * <tr valign="top">
 * <td>判断日期区间</td>
 * <td>
 * <ul>
 * <li>{@link DateUtil#isInTime(Date, Date, Date)}</li>
 * </ul>
 * </td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * <h3>通过这个类,还可以获得以下数据:</h3>
 * 
 * <blockquote>
 * <table border="1" cellspacing="0" cellpadding="4" summary="">
 * <tr style="background-color:#ccccff">
 * <th align="left">字段</th>
 * <th align="left">说明</th>
 * </tr>
 * <tr valign="top">
 * <td>获得下一周的第一天时间</td>
 * <td>DateUtil.<b>getFirstDateOfThisWeek</b>(DateUtil.addDay(date, 7));</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>获得下一周的最后一天时间</td>
 * <td>DateUtil.<b>getLastDateOfThisWeek</b>(DateUtil.addDay(date, 7));</td>
 * </tr>
 * <tr valign="top">
 * <td>获得上一周的第一天时间</td>
 * <td>DateUtil.<b>getFirstDateOfThisWeek</b>(DateUtil.addDay(date, -7));</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>获得上一周的最后一天时间</td>
 * <td>DateUtil.<b>getLastDateOfThisWeek</b>(DateUtil.addDay(date, -7));</td>
 * </tr>
 * <tr valign="top">
 * <td>获得下个月第一天</td>
 * <td>DateUtil.<b>getFirstDateOfThisMonth</b>(DateUtil.addMonth(now, +1)));</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>获得下个月最后一天</td>
 * <td>DateUtil.<b>getLastDateOfThisMonth</b>(DateUtil.addMonth(now, +1)));</td>
 * </tr>
 * <tr valign="top">
 * <td>获得上个月第一天</td>
 * <td>DateUtil.<b>getFirstDateOfThisMonth</b>(DateUtil.addMonth(now, -1)));</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>获得上个月最后一天</td>
 * <td>DateUtil.<b>getLastDateOfThisMonth</b>(DateUtil.addMonth(now, -1)));</td>
 * </tr>
 * <tr valign="top">
 * <td>获得去年第一天</td>
 * <td>DateUtil.<b>getFirstDateOfThisYear</b>(DateUtil.addYear(now, -1));</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>获得去年最后一天</td>
 * <td>DateUtil.<b>getLastDateOfThisYear</b>(DateUtil.addYear(now, -1)));</td>
 * </tr>
 * <tr valign="top">
 * <td>获得明年第一天</td>
 * <td>DateUtil.<b>getFirstDateOfThisYear</b>(DateUtil.addYear(now, +1));</td>
 * </tr>
 * <tr valign="top" style="background-color:#eeeeff">
 * <td>获得明年最后一天</td>
 * <td>DateUtil.<b>getLastDateOfThisYear</b>(DateUtil.addYear(now, +1)));</td>
 * </tr>
 * </table>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see CalendarUtil
 * @see DatePattern
 * @see com.feilong.lib.lang3.time.DateUtils
 * @since 1.0.0
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
public final class DateUtil{

    /**
     * 生成间隔时间等的单位配置文件
     * 
     * @since 3.0.0
     */
    private static final Map<String, String> UNIT_CONFIG_MAP = toMap(getResourceBundle("config/feilong-dateutil"));

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private DateUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 此时此刻的 {@link Date}.
     * 
     * <p>
     * 使用静态导入 static import,开发效率要高于自己写 new Date()
     * </p>
     *
     * @return the date
     * @since 1.14.0
     */
    public static Date now(){
        return new Date();
    }

    /**
     * 将此时此刻转成时间字符串.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.toString(new Date(), COMMON_DATE_AND_TIME)
     * </pre>
     * 
     * 可以简写(使用静态导入 static import更精简)
     * 
     * <pre class="code">
     * DateUtil.nowString(COMMON_DATE_AND_TIME)
     * </pre>
     * 
     * 尤其
     * 
     * <pre class="code">
     * DateUtil.toString(Calendar.getInstance().getTime(), DatePattern.COMMON_DATE_AND_TIME)
     * </pre>
     * 
     * 可以简写成(使用静态导入 static import更精简):
     * 
     * <pre class="code">
     * DateUtil.nowString(COMMON_DATE_AND_TIME)
     * </pre>
     * 
     * </blockquote>
     *
     * @param datePattern
     *            the date pattern
     * @return 如果 <code>datePattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>datePattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.14.0
     */
    public static String nowString(String datePattern){
        return toString(now(), datePattern);
    }

    /**
     * 将此时此刻转成 TIMESTAMP 时间字符串,(常用于生成文件名字).
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.toString(new Date(), TIMESTAMP)
     * </pre>
     * 
     * 可以简写(使用静态导入 static import更精简)
     * 
     * <pre class="code">
     * DateUtil.nowString(TIMESTAMP)
     * </pre>
     * 
     * 尤其
     * 
     * <pre class="code">
     * DateUtil.toString(Calendar.getInstance().getTime(), DatePattern.TIMESTAMP)
     * </pre>
     * 
     * 可以简写成(使用静态导入 static import更精简):
     * 
     * <pre class="code">
     * DateUtil.nowTimestamp()
     * </pre>
     * 
     * </blockquote>
     *
     * @return the string
     * @since 2.1.0
     */
    public static String nowTimestamp(){
        return nowString(DatePattern.TIMESTAMP);
    }

    /**
     * 将 自 1970年1月1 日 00:00:00 GMT 以来,此 此时此刻 对象表示的毫秒数.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * new Date().getTime()
     * </pre>
     * 
     * 可以简写(使用静态导入 static import更精简)
     * 
     * <pre class="code">
     * DateUtil.nowTime()
     * </pre>
     * 
     * </blockquote>
     *
     * @return 自 1970年1月1 日 00:00:00 GMT 以来,此 此时此刻 对象表示的毫秒数.
     * @since 3.3.1
     */
    public static long nowTime(){
        return getTime(now());
    }

    //------------------------day---------------------------------------

    /**
     * 获得指定日期<code>date</code>的 <code>00:00:00.000</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getFirstDateOfThisDay(2011-01-01 10:20:20)  =2011-01-01 00:00:00
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @see com.feilong.lib.lang3.time.DateUtils#truncate(Date, int)
     * @since 1.5.0
     */
    public static Date getFirstDateOfThisDay(Date date){
        Calendar calendar = toCalendar(date);
        return CalendarUtil.toDate(resetDayBegin(calendar));
    }

    /**
     * 获得今天的 <code>00:00:00.000</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * //如果今天是 2011-01-01 10:20:20
     * DateUtil.getFirstDateOfToday()  =2011-01-01 00:00:00
     * </pre>
     * 
     * </blockquote>
     * 
     * @return 今天的 <code>00:00:00.000</code>时间.
     * @see com.feilong.lib.lang3.time.DateUtils#truncate(Date, int)
     * @since 3.1.1
     */
    public static Date getFirstDateOfToday(){
        return getFirstDateOfThisDay(now());
    }

    /**
     * 获得昨天的 <code>00:00:00.000</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * //如果今天是 2011-01-02 10:20:20
     * DateUtil.getFirstDateOfYesterday()  =2011-01-01 00:00:00
     * </pre>
     * 
     * </blockquote>
     * 
     * @return 昨天的 <code>00:00:00.000</code>时间.
     * @see com.feilong.lib.lang3.time.DateUtils#truncate(Date, int)
     * @since 3.1.1
     */
    public static Date getFirstDateOfYesterday(){
        return getFirstDateOfThisDay(addDay(now(), -1));
    }

    /**
     * 获得明天的 <code>00:00:00.000</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * //如果今天是 2011-01-02 10:20:20
     * DateUtil.getFirstDateOfYesterday()  =2011-01-03 00:00:00
     * </pre>
     * 
     * </blockquote>
     * 
     * @return 明天的 <code>00:00:00.000</code>时间.
     * @see com.feilong.lib.lang3.time.DateUtils#truncate(Date, int)
     * @since 3.1.1
     */
    public static Date getFirstDateOfTomorrow(){
        return getFirstDateOfThisDay(addDay(now(), 1));
    }

    /**
     * 获得指定日期<code>date</code>的 <code>23:59:59.999</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getLastDateOfThisDay(2011-01-01 10:20:20)=2011-01-01 23:59:59.999
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @since 1.5.0
     */
    public static Date getLastDateOfThisDay(Date date){
        Calendar calendar = toCalendar(date);
        return CalendarUtil.toDate(resetDayEnd(calendar));
    }

    /**
     * 获得昨天的 <code>23:59:59.999</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     *  // 如果今天是 2011-01-02 10:20:20
     * DateUtil.getLastDateOfYesterday()=2011-01-01 23:59:59.999
     * </pre>
     * 
     * </blockquote>
     * 
     * @return 今天的 <code>23:59:59.999</code>时间.
     * @since 3.1.1
     */
    public static Date getLastDateOfYesterday(){
        return getLastDateOfThisDay(addDay(now(), -1));
    }

    /**
     * 获得明天的 <code>23:59:59.999</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     *  // 如果今天是 2011-01-01 10:20:20
     * DateUtil.getLastDateOfTomorrow()=2011-01-02 23:59:59.999
     * </pre>
     * 
     * </blockquote>
     * 
     * @return 今天的 <code>23:59:59.999</code>时间.
     * @since 3.1.1
     */
    public static Date getLastDateOfTomorrow(){
        return getLastDateOfThisDay(addDay(now(), 1));
    }

    /**
     * 获得今天的 <code>23:59:59.999</code>时间.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     *  // 如果今天是 2011-01-01 10:20:20
     * DateUtil.getLastDateOfThisDay()=2011-01-01 23:59:59.999
     * </pre>
     * 
     * </blockquote>
     * 
     * @return 今天的 <code>23:59:59.999</code>时间.
     * @since 3.1.1
     */
    public static Date getLastDateOfToday(){
        return getLastDateOfThisDay(now());
    }

    //--------------------------week-------------------------------------
    /**
     * 获得指定日期所在的<span style="color:red">星期第一天(周日)</span> <code>00:00:00.000</code> 到毫秒.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getFirstDateOfThisWeek(2012-10-11 17:10:30.701)  =2012-10-07 00:00:00.000
     * DateUtil.getFirstDateOfThisWeek(2014-01-01 05:00:00)      =2013-12-29 00:00:00.000 //跨年
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>按照外国制,<span style="color:red">周日为一个星期第一天,周六为最后一天</span></li>
     * <li>会自动跨月,跨年操作</li>
     * </ol>
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @see #toCalendar(Date)
     * @see Calendar#set(int, int)
     * @see CalendarUtil#resetDayBegin(Calendar)
     * @see Calendar#getTime()
     */
    public static Date getFirstDateOfThisWeek(Date date){
        Calendar calendar = toCalendar(date);
        calendar.set(DAY_OF_WEEK, SUNDAY);
        return CalendarUtil.toDate(resetDayBegin(calendar));
    }

    /**
     * 获得指定日期所在<span style="color:red">星期的最后一天(周六)</span> <code>23:59:59.999</code> 到毫秒.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getLastDateOfThisWeek(2012-10-11 17:10:30.701)   =2012-10-13 23:59:59.999
     * DateUtil.getLastDateOfThisWeek(2014-12-31 05:00:00)       =2015-01-03 23:59:59.999 //跨年
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>按照外国制,<span style="color:red">周日为一个星期第一天,周六为最后一天</span></li>
     * <li>会自动跨月,跨年操作</li>
     * </ol>
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @see #toCalendar(Date)
     * @see Calendar#set(int, int)
     * @see CalendarUtil#resetDayEnd(Calendar)
     * @see Calendar#getTime()
     * @since 1.0.1
     */
    public static Date getLastDateOfThisWeek(Date date){
        Calendar calendar = toCalendar(date);
        calendar.set(DAY_OF_WEEK, SATURDAY);
        return CalendarUtil.toDate(resetDayEnd(calendar));
    }

    //---------------------------月 年------------------------------------

    /**
     * 获得指定日期<code>date</code>所在月的第一天,<code>00:00:00.000</code>到毫秒.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getFirstDateOfThisMonth(2012-10-11 17:10:30.701)=2012-10-01 00:00:00.000
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @see #toCalendar(Date)
     * @see Calendar#set(int, int)
     * @see CalendarUtil#resetDayBegin(Calendar)
     * @see Calendar#getTime()
     */
    public static Date getFirstDateOfThisMonth(Date date){
        Calendar calendar = toCalendar(date);
        calendar.set(DAY_OF_MONTH, 1);
        return CalendarUtil.toDate(resetDayBegin(calendar));
    }

    /**
     * 获得指定日期<code>date</code><span style="color:red">所在月的最后一天</span>,<code>23:59:59.999</code> 到毫秒.
     * 
     * <p>
     * 以指定日期 <code>date</code> 月的实际天数为准,也就是说,2月会<b>自动区分闰年</b> 是28天还是29天
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getLastDateOfThisMonth(2012-10-11 17:10:30.701)=2012-10-31 23:59:59.999
     * 
     * DateUtil.getLastDateOfThisMonth(2016-02-22 01:00:00)=2016-02-29 23:59:59.999
     * DateUtil.getLastDateOfThisMonth(2015-02-22 01:00:00)=2015-02-28 23:59:59.999
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @see #toCalendar(Date)
     * @see Calendar#set(int, int)
     * @see CalendarUtil#resetDayEnd(Calendar)
     * @see Calendar#getTime()
     */
    public static Date getLastDateOfThisMonth(Date date){
        Calendar calendar = toCalendar(date);
        calendar.set(DAY_OF_MONTH, calendar.getActualMaximum(DAY_OF_MONTH));
        return CalendarUtil.toDate(resetDayEnd(calendar));
    }

    /**
     * 获得指定日期<code>date</code><span style="color:red">所在年的第一天</span>,<code>00:00:00.000</code> 到毫秒.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getFirstDateOfThisYear(2012-10-11 17:10:30.701)=2012-01-01 00:00:00.000
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @see #toCalendar(Date)
     * @see Calendar#set(int, int)
     * @see CalendarUtil#resetDayBegin(Calendar)
     * @see Calendar#getTime()
     */
    public static Date getFirstDateOfThisYear(Date date){
        Calendar calendar = toCalendar(date);
        calendar.set(MONTH, JANUARY);
        calendar.set(DAY_OF_MONTH, 1);
        return CalendarUtil.toDate(resetDayBegin(calendar));
    }

    /**
     * 获得指定日期<code>date</code><span style="color:red">所在年的最后一天</span> <code>23:59:59.999</code> 到毫秒.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getLastDateOfThisYear(2012-10-11 17:10:30.701)=2012-12-31 23:59:59.999
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}
     * @see #toCalendar(Date)
     * @see Calendar#set(int, int)
     * @see CalendarUtil#resetDayEnd(Calendar)
     * @see Calendar#getTime()
     */
    public static Date getLastDateOfThisYear(Date date){
        Calendar calendar = toCalendar(date);
        return CalendarUtil.toDate(resetYearEnd(calendar));
    }

    // [start]operate 时间操作(加减)--------------------------------------------------------------------------

    /**
     * 指定日期 <code>date</code>,加减年份.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addYear(2012-06-29 00:33:05,5)   =20<span style="color:red">17</span>-06-29 00:33:05
     * DateUtil.addYear(2012-06-29 00:33:05,-5)  =20<span style="color:red">07</span>-06-29 00:33:05
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param year
     *            加减年数 ,<span style="color:red">可以是负数</span>,表示前面多少<br>
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>year==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see Calendar#YEAR
     * @see com.feilong.lib.lang3.time.DateUtils#addYears(Date, int)
     */
    public static Date addYear(Date date,int year){
        return DateUtils.addYears(date, year);
    }

    /**
     * 指定日期 <code>date</code>加减月份.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addMonth(2012-10-16 23:12:43,5)  =2013-03-16 23:12:43.932
     * DateUtil.addMonth(2012-10-16 23:12:43,-5) =2012-05-16 23:12:43.943
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param month
     *            加减月份, <span style="color:red">可以是负数</span>,表示前面多少<br>
     *            比如-3 表示 3个月之前
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>month==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see Calendar#MONTH
     * @see com.feilong.lib.lang3.time.DateUtils#addMonths(Date, int)
     */
    public static Date addMonth(Date date,int month){
        return DateUtils.addMonths(date, month);
    }

    /**
     * 指定日期 <code>date</code>加减天数.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addDay(2012-06-29 00:42:26,5)    =2012-07-04 00:42:26
     * DateUtil.addDay(2012-06-29 00:42:26,-5)   =2012-06-24 00:42:26
     * DateUtil.addDay(2014-12-31 02:10:05,5)    =2015-01-05 02:10:05.000
     * DateUtil.addDay(2014-01-01 02:10:05,-5)   =2013-12-27 02:10:05.000
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param day
     *            需要加减的天数,<span style="color:red">可以是负数</span>,表示前面多少<br>
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>day==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see Calendar#DAY_OF_MONTH
     * @see com.feilong.lib.lang3.time.DateUtils#addDays(Date, int)
     */
    public static Date addDay(Date date,int day){
        // Calendar.DAY_OF_MONTH 它与 Calendar.DATE 是同义词.一个月中第一天的值为 1.
        return DateUtils.addDays(date, day);
    }

    /**
     * 指定日期 <code>date</code>加减星期 .
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addWeek(2012-06-29 00:45:18,5)   =2012-08-03 00:45:18
     * DateUtil.addWeek(2012-06-29 00:45:18,-5)  =2012-05-25 00:45:18
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param week
     *            需要加减的星期数,<span style="color:red">可以是负数</span>,表示前面多少<br>
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>week==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see com.feilong.lib.lang3.time.DateUtils#addWeeks(Date, int)
     */
    public static Date addWeek(Date date,int week){
        return DateUtils.addWeeks(date, week);
    }

    /**
     * 指定日期 <code>date</code>加减小时.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addHour(2012-06-29 00:46:24,5)   =2012-06-29 05:46:24
     * DateUtil.addHour(2012-06-29 00:46:24,-5)  =2012-06-28 19:46:24
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param hour
     *            加减小时数,<span style="color:red">可以是负数</span>,表示前面多少<br>
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>hour==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see com.feilong.lib.lang3.time.DateUtils#addHours(Date, int)
     */
    public static Date addHour(Date date,int hour){
        return DateUtils.addHours(date, hour);
    }

    /**
     * 指定日期 <code>date</code>加减分钟.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addMinute(2012-10-16 23:20:33,180)   =2012-10-17 02:20:33.669
     * DateUtil.addMinute(2012-10-16 23:20:33,-180)  =2012-10-16 20:20:33.669
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param minute
     *            加减分钟数,<span style="color:red">可以是负数</span>,表示前面多少<br>
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>minute==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see com.feilong.lib.lang3.time.DateUtils#addMinutes(Date, int)
     */
    public static Date addMinute(Date date,int minute){
        return DateUtils.addMinutes(date, minute);
    }

    /**
     * 指定日期 <code>date</code>加减秒.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addSecond(2012-10-16 23:22:02,180)   = 2012-10-16 23:25:02.206
     * DateUtil.addSecond(2012-10-16 23:22:02,-180)  = 2012-10-16 23:19:02.206
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param second
     *            加减秒,<span style="color:red">可以是负数</span>,表示前面多少<br>
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>second==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see com.feilong.lib.lang3.time.DateUtils#addSeconds(Date, int)
     */
    public static Date addSecond(Date date,int second){
        return DateUtils.addSeconds(date, second);
    }

    /**
     * 指定日期 <code>date</code>加减毫秒.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>结果会自动跨月,跨年计算.</li>
     * <li>传入的参数 <code>date</code> 不会改变</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.addMillisecond(2015-09-07 13:35:02.769,5000)     =2015-09-07 13:35:07.769
     * DateUtil.addMillisecond(2015-09-07 13:35:02.769,-5000)    =2015-09-07 13:34:57.769
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param millisecond
     *            加减毫秒,<span style="color:red">可以是负数</span>,表示前面多少<br>
     * @return 如果 <code>date</code>是null,抛出 {@link java.lang.IllegalArgumentException}<br>
     *         如果 <code>millisecond==0</code>,那么什么都不做,返回 <code>date</code>,参见 {@link GregorianCalendar#add(int, int)}
     * @throws NullPointerException
     *             如果 <code>date</code> 是<code>null</code>
     * @see com.feilong.lib.lang3.time.DateUtils#addMilliseconds(Date, int)
     * @since 1.4.1
     */
    public static Date addMillisecond(Date date,int millisecond){
        return DateUtils.addMilliseconds(date, millisecond);
    }

    // [end]

    // [start]fieldValue获得日期中的某属性字段
    /**
     * 获得指定日期 <code>date</code>中的<b>年份</b>.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getYear(toDate("2012-06-29 00:26:53", COMMON_DATE_AND_TIME))    = 2012
     * DateUtil.getYear(toDate("2016-07-16", COMMON_DATE))                      = 2016
     * DateUtil.getYear(toDate("2016-13-16", COMMON_DATE))                      = 2017
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#YEAR
     */
    public static int getYear(Date date){
        return CalendarUtil.getFieldValue(date, YEAR);
    }

    /**
     * 获得指定日期 <code>date</code>中的<b>月份</b> <span style="color:red">(已经+1处理)</span>.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getMonth(<code>2012-06-29</code>)    =6
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#MONTH
     */
    public static int getMonth(Date date){
        return 1 + CalendarUtil.getFieldValue(date, MONTH);
    }

    /**
     * 指定日期 <code>date</code>年中的<b>星期数</b>.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getWeekOfYear(2014-06-03)    =23
     * DateUtil.getWeekOfYear(2014-01-01)    =1
     * DateUtil.getWeekOfYear(2014-12-29)    =1
     * DateUtil.getWeekOfYear(2014-12-20)    =51
     * DateUtil.getWeekOfYear(2014-12-26)    =52
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>注意:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>一年中第一个星期的值为 1,一年52(365/7=52.14)个星期</li>
     * <li>2014年的1-1 1-2 1-3 1-4 得出的{@link Calendar#WEEK_OF_YEAR} 是1; <br>
     * 2014年的12-28 12-29 12-30 12-31 得出的{@link Calendar#WEEK_OF_YEAR} 也是1</li>
     * 
     * <li>{@link Calendar#setMinimalDaysInFirstWeek(int)} 可以来修改第一周最小天数,但是如果设置为7的话
     * 
     * <pre class="code">
     * DateUtil.getWeekOfYear(2014-01-01)    =52
     * DateUtil.getWeekOfYear(2014-12-31)    =52
     * </pre>
     * 
     * 可以看出,如果从1月1号算开始第一周的话,这年第一周时间不够我们设置的7天,那么1月1号算上一年的星期</li>
     * </ol>
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#WEEK_OF_YEAR
     * @see Calendar#getFirstDayOfWeek()
     * @see Calendar#getMinimalDaysInFirstWeek()
     * @see Calendar#setMinimalDaysInFirstWeek(int)
     * @since 1.0.7
     */
    public static int getWeekOfYear(Date date){
        return CalendarUtil.getFieldValue(date, WEEK_OF_YEAR);
    }

    /**
     * 获得指定日期 <code>date</code>是当年中的第几天.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getDayOfYear(<code>2013-01-01</code>)    =1
     * DateUtil.getDayOfYear(<code>2013-01-05</code>)    =5
     * DateUtil.getDayOfYear(<code>2016-12-31</code>)    =366
     * DateUtil.getDayOfYear(<code>2016-02-01</code>)    =32
     * </pre>
     * 
     * </blockquote>
     *
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see Calendar#DAY_OF_YEAR
     * @since 1.0.2
     */
    public static int getDayOfYear(Date date){
        return CalendarUtil.getFieldValue(date, DAY_OF_YEAR);
    }

    /**
     * 获得指定日期 <code>date</code>是当前月的几号.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getDayOfMonth(<code>2012-06-29</code>)    =29
     * DateUtil.getDayOfMonth(<code>2013-01-05</code>)    =5
     * DateUtil.getDayOfMonth(<code>2016-12-31</code>)    =31
     * DateUtil.getDayOfMonth(<code>2016-02-01</code>)    =1
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#DAY_OF_MONTH
     */
    public static int getDayOfMonth(Date date){
        return CalendarUtil.getFieldValue(date, DAY_OF_MONTH);
    }

    /**
     * 获得指定日期<code>date</code><b>在当前星期是星期几</b>.
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>从星期天开始,并且星期天是1</li>
     * <li>{@link Calendar#SUNDAY SUNDAY}、{@link Calendar#MONDAY MONDAY}、{@link Calendar#TUESDAY TUESDAY}、{@link Calendar#WEDNESDAY
     * WEDNESDAY}、 {@link Calendar#THURSDAY THURSDAY}、{@link Calendar#FRIDAY FRIDAY} 和 {@link Calendar#SATURDAY SATURDAY} ,分别对应1-7</li>
     * <li>强烈建议拿上述常量来比较判断,而不是拿数字来比较</li>
     * </ol>
     * </blockquote>
     * 
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getDayOfWeek(2012-6-29)  =6  是 {@link Calendar#FRIDAY FRIDAY} 星期5
     * DateUtil.getDayOfWeek(2016-08-16)  ={@link Calendar#TUESDAY}
     * DateUtil.getDayOfWeek(2016-12-31)  ={@link Calendar#SATURDAY}
     * DateUtil.getDayOfWeek(2016-02-01)  ={@link Calendar#MONDAY}
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see Calendar#SUNDAY
     * @see Calendar#MONDAY
     * @see Calendar#TUESDAY
     * @see Calendar#WEDNESDAY
     * @see Calendar#THURSDAY
     * @see Calendar#FRIDAY
     * @see Calendar#SATURDAY
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#DAY_OF_WEEK
     */
    public static int getDayOfWeek(Date date){
        return CalendarUtil.getFieldValue(date, DAY_OF_WEEK);
    }

    /**
     * 获得指定日期<code>date</code>在它<b>一年中的小时数</b>.
     * 
     * <p>
     * max value: 8784.
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getHourOfYear(2013-01-01 00:00:05)   =0
     * DateUtil.getHourOfYear(2013-01-01 01:00:05)   =1
     * DateUtil.getHourOfYear(2013-01-05 12:00:05)   =108
     * DateUtil.getHourOfYear(2013-09-09 17:28)      =6041
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.0.2
     */
    public static int getHourOfYear(Date date){
        return (getDayOfYear(date) - 1) * 24 + CalendarUtil.getFieldValue(date, HOUR_OF_DAY);
    }

    /**
     * 获得指定日期<code>date</code>中的<b>小时</b>(24小时制).
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getHourOfDay(toDate("2012-06-29 00:26:53", COMMON_DATE_AND_TIME)) =0
     * DateUtil.getHourOfDay(toDate("2016-07-16 22:34:00", COMMON_DATE_AND_TIME)) =22
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#HOUR_OF_DAY
     */
    public static int getHourOfDay(Date date){
        return CalendarUtil.getFieldValue(date, HOUR_OF_DAY);
    }

    /**
     * 获得指定日期<code>date</code>中的<b>分钟</b>.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getMinute(2012-6-29 00:26:53)    =26
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#MINUTE
     */
    public static int getMinute(Date date){
        return CalendarUtil.getFieldValue(date, MINUTE);
    }

    /**
     * 获得指定日期<code>date</code>中的<b>秒</b>.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getSecond(2012-6-29 00:26:53)    =53
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see CalendarUtil#getFieldValue(Date, int)
     * @see Calendar#SECOND
     */
    public static int getSecond(Date date){
        return CalendarUtil.getFieldValue(date, SECOND);
    }

    /**
     * 获得指定日期<code>date</code>在<b>当天中的秒数</b>,最大值86400.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getSecondOfDay(2013-09-09 16:42:41)= 60161
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see TimeInterval#SECONDS_PER_DAY
     * @see TimeInterval#SECONDS_PER_HOUR
     * @see #getSecondOfHour(Date)
     * @since 1.0.2
     */
    public static int getSecondOfDay(Date date){
        int hour = getHourOfDay(date);
        return hour * SECONDS_PER_HOUR + getSecondOfHour(date);
    }

    /**
     * 获得指定日期<code>date</code>在<b>当前小时中的秒数</b>,最大值3600 {@link TimeInterval#SECONDS_PER_HOUR}.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getSecondOfHour(2013-09-15 01:15:23)= 923
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see TimeInterval#SECONDS_PER_MINUTE
     * @see TimeInterval#SECONDS_PER_HOUR
     * @since 1.0.2
     */
    public static int getSecondOfHour(Date date){
        int minute = getMinute(date);
        int second = getSecond(date);
        return second + minute * SECONDS_PER_MINUTE;
    }

    /**
     * 返回自 <code>1970年1月1 日 00:00:00 GMT</code> 以来,此 Date 对象表示的<b>毫秒</b>数.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getTime(2012-06-29 00:28:00)= 1340900880000L
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>重构:</h3>
     * 
     * <blockquote>
     * <p>
     * 对于以下代码:
     * </p>
     * 
     * <pre class="code">
     * Date multiSingleCreateAt = singleBookPo.getMultiSingleCreateAt();
     * bookVo.setCreateAtTimestamp(null == multiSingleCreateAt ? null : DateUtil.getTime(multiSingleCreateAt));
     * 
     * </pre>
     * 
     * <b>可以重构成:</b>
     * 
     * <pre class="code">
     * bookVo.setCreateAtTimestamp(DateUtil.getTime(singleBookPo.getMultiSingleCreateAt());
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @return {@link Date#getTime()} <br>
     *         如果 <code>date</code> 是null,返回null<br>
     * @since 3.3.4 如果 <code>date</code> 是null,返回null
     */
    public static Long getTime(Date date){
        return null == date ? null : date.getTime();
    }

    // [end]

    // [start]toString/toDate 类型转换

    /**
     * 将指定日期 <code>date</code>转换成特殊格式 <code>datePattern</code> 的字符串.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.toString(Tue Oct 16 23:49:21 CST 2012,DatePattern.COMMON_DATE_AND_TIME_WITH_MILLISECOND) =2012-10-16 23:49:21.525
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            任意时间
     * @param datePattern
     *            模式 {@link DatePattern}
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>pattern</code> 是 null,抛出 {@link NullPointerException}<br>
     *         如果 <code>pattern</code> 是 blank,抛出 {@link IllegalArgumentException}<br>
     * @see com.feilong.lib.lang3.time.DateFormatUtils#format(Date, String)
     * @see "org.joda.time.base.AbstractDateTime#toString(String)"
     * @see <a href="http://stackoverflow.com/questions/5683728/convert-java-util-date-to-string">convert-java-util-date-to-string</a>
     * @see <a href="http://stackoverflow.com/questions/4772425/change-date-format-in-a-java-string">change-date-format-in-a-java-string</a>
     * @since 1.6.0
     */
    public static String toString(Date date,String datePattern){
        Validate.notNull(date, "date can't be null!");
        Validate.notBlank(datePattern, "datePattern can't be blank!");

        return DateFormatUtils.format(date, datePattern);
    }

    /**
     * 将一个 <code>oldPattern</code> 格式日期 <code>dateString</code> 字符串 使用新格式 <code>newPattern</code> 转成新的字符串.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.toString("2020-01-06", "yyyy-MM-dd", "yyyy.MM.dd")="2020.01.06"
     * DateUtil.toString("2020-01-06", "yyyy-MM-dd", "yyyy年MM月dd日")="2020年01月06日"
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * 
     * <h3>重构:</h3>
     * 
     * <blockquote>
     * <p>
     * 对于以下代码:
     * </p>
     * 
     * <pre class="code">
     * 
     * Date format = new SimpleDateFormat("yyyyMMdd").parse(libBorrowUserPo.getSurplusTime());
     * String surplusTime = new SimpleDateFormat("yyyy-MM-dd").format(format);
     * 
     * libBorrowUserPo.setSurplusTime(surplusTime);
     * 
     * </pre>
     * 
     * <b>可以重构成:</b>
     * 
     * <pre class="code">
     * libBorrowUserPo.setSurplusTime(DateUtil.toString(libBorrowUserPo.getSurplusTime(), "yyyyMMdd", "yyyy-MM-dd"));
     * </pre>
     * 
     * </blockquote>
     *
     * @param dateString
     *            日期字符串 比如 20221010
     * @param oldPattern
     *            老的格式 比如 yyyyMMdd
     * @param newPattern
     *            新的格式 比如 yyyy-MM-dd
     * @return 如果 <code>dateString</code> 是null或者empty,返回 null<br>
     *         如果 <code>oldPattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>oldPattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>newPattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>newPattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 2.1.0
     */
    public static String toString(String dateString,String oldPattern,String newPattern){
        if (isNullOrEmpty(dateString)){
            return null;
        }

        Validate.notBlank(oldPattern, "oldPattern can't be blank!");
        Validate.notBlank(newPattern, "newPattern can't be blank!");

        //---------------------------------------------------------------
        return toString(toDate(dateString, oldPattern), newPattern);
    }

    /**
     * 将时间字符串 <code>dateString</code> 使用<b>一个或者多个</b>不同的 <code>datePattern</code> 模式按照顺序转换成date类型.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.toDate("2016-02-33", DatePattern.COMMON_DATE)                   = 2016-03-04
     * DateUtil.toDate("2016-06-28T01:21:12-0800", "yyyy-MM-dd'T'HH:mm:ssZ")    = 2016-06-28 17:21:12
     * DateUtil.toDate("2016-06-28T01:21:12+0800", "yyyy-MM-dd'T'HH:mm:ssZ")    = 2016-06-28 01:21:12
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>注意:</h3>
     * <blockquote>
     * <ol>
     * <li>转换的时候,使用日历的<b>宽松模式</b>,参见 {@link java.text.DateFormat#setLenient(boolean)},即支持传入"2016-02-33",会转换成 2016-03-04</li>
     * <li>如果能解析所有的字符串,那么视为成功</li>
     * <li>如果没有任何的模式匹配,将会抛出异常</li>
     * <li>如果转换有异常,会将 {@link ParseException} 转成 {@link IllegalArgumentException} 返回,是 UnCheckedException异常 ,不需要强制catch处理</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * 经常我们会看到小伙伴写出下面的代码:
     * 
     * <pre class="code">
     * SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
     * Date publishTimeDate = null;
     * try{
     *     publishTimeDate = format.parse(publishTime);
     * }catch (ParseException e1){
     *     e1.printStackTrace();
     * }
     * </pre>
     * 
     * <p>
     * 可以看到直接使用 {@code SimpleDateFormat} 来写代码的话,代码行数较多,并且还需要自行处理 ParseException checkedException异常, 而且catch里面一般都是写的废话
     * </p>
     * 
     * <p>
     * 此时你可以一行代码搞定:
     * </p>
     * 
     * <pre class="code">
     * 
     * Date publishTimeDate = DateUtil.toDate(publishTime, DatePattern.COMMON_DATE_AND_TIME_WITHOUT_SECOND);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param dateString
     *            时间字符串
     * @param datePatterns
     *            模式,时间字符串的模式{@link DatePattern}
     * @return 如果 <code>dateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>dateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>datePatterns</code> 是 null,抛出 {@link NullPointerException}<br>
     *         如果 <code>datePatterns</code> 是 empty,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>datePatterns</code> 有元素是 null,抛出 {@link IllegalArgumentException}<br>
     * @see com.feilong.lib.lang3.time.DateUtils#parseDate(String, String...)
     * @see <a href="http://stackoverflow.com/questions/4216745/java-string-to-date-conversion/">java-string-to-date-conversion</a>
     * @see <a href="http://stackoverflow.com/questions/4216745/java-string-to-date-conversion/22180505#22180505">java-string-to-date-
     *      conversion/22180505#22180505</a>
     * @see <a href="http://stackoverflow.com/questions/2735023/convert-string-to-java-util-date">convert-string-to-java-util-date</a>
     * @since 1.7.3 change param to datePatterns array
     */
    public static Date toDate(String dateString,String...datePatterns){
        Validate.notBlank(dateString, "dateString can't be blank!");

        Validate.notEmpty(datePatterns, "datePatterns can't be null!");
        Validate.noNullElements(datePatterns, "datePatterns can't has null datePattern");

        //---------------------------------------------------------------

        try{
            return DateUtils.parseDate(dateString, datePatterns);
        }catch (ParseException e){
            String pattern = "dateString:[{}],use patterns:[{}],parse to date exception,message:[{}]";
            throw new IllegalArgumentException(formatPattern(pattern, dateString, datePatterns, e.getMessage()), e);
        }
    }

    /**
     * 将 <code>time</code> 使用 new Date 成date类型.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Date date = toDate(1682787143647L); //北京时间 2023-04-30 00:52:23
     * </pre>
     * 
     * </blockquote>
     * 
     * @param time
     *            自1970年1月1日00:00:00 GMT以来的毫秒。
     * @return 如果 <code>time</code> 是null,返回null<br>
     *         否则返回 new Date(time)
     * @since 3.5.0
     */
    public static Date toDate(Long time){
        return null == time ? null : new Date(time);
    }

    /**
     * 将时间字符串 <code>dateString</code> 使用 {@link DatePattern#COMMON_DATE} 默认模式转换成date类型.
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * Date date = toDate("2022-09-01");
     * </pre>
     * 
     * </blockquote>
     * 
     * @param dateString
     *            时间字符串
     * @return 如果 <code>dateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>dateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see com.feilong.lib.lang3.time.DateUtils#parseDate(String, String...)
     * @since 3.2.1
     */
    public static Date toDate(String dateString){
        return toDate(dateString, DatePattern.COMMON_DATE);
    }

    // [end]

    // [start]toCalendar

    /**
     * 将 {@link Date} 转成 {@link Calendar},调用 {@link GregorianCalendar}.
     * 
     * <p>
     * {@link Calendar#getInstance()}方法,返回用默认的地区和时区的当前日期和当前时间所初始化的GregorianCalendar(标准日历),<br>
     * 最终会调用 java.util.Calendar.createCalendar(TimeZone, Locale) 方法,<br>
     * 该方法会判断Locale(日本和泰国),其他国家最终会调用 {@link GregorianCalendar#GregorianCalendar(java.util.TimeZone, java.util.Locale)} 方法
     * </p>
     * 
     * <h3>{@link GregorianCalendar}</h3>
     * 
     * <blockquote>
     * <p>
     * 标准阳历格列高利历/公历,现在的公历是根据罗马人的"儒略历"改编而
     * </p>
     * </blockquote>
     *
     * @param date
     *            任意时间
     * @return 如果date 是null,抛出 {@link NullPointerException}
     * @see Calendar#getInstance()
     * @see GregorianCalendar
     * @see Calendar#setTime(Date)
     * @see Calendar#setTimeInMillis(long)
     * @see com.feilong.lib.lang3.time.DateUtils#toCalendar(Date)
     * @since 1.8.3 remove public
     */
    static Calendar toCalendar(Date date){
        Validate.notNull(date, "date can't be null!");
        return DateUtils.toCalendar(date);
    }

    // [end]

    // [start]isBefore 时间早晚

    /**
     * 判断指定日期 <code>date</code>是否 在 <code>whenDate</code>时间之前.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * isBefore(null, toDate("2011-03-10", COMMON_DATE))                                =   false
     * isBefore(toDate("2011-05-01", COMMON_DATE), toDate("2011-04-01", COMMON_DATE))   =   false
     * isBefore(toDate("2011-03-05", COMMON_DATE), toDate("2011-03-10", COMMON_DATE))   =   true
     * </pre>
     * 
     * </blockquote>
     *
     * @param date
     *            指定日期
     * @param whenDate
     *            比照日期
     * @return 如果 <code>whenDate</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date</code> 是null,返回false<br>
     *         否则返回 <code>date.before(whenDate)</code>
     * @see java.util.Date#before(Date)
     * @since 1.2.2
     */
    public static boolean isBefore(Date date,Date whenDate){
        Validate.notNull(whenDate, "whenDate can't be null!");
        return null != date && date.before(whenDate);
    }

    /**
     * 判断指定日期 <code>date</code>是否在 <code>whenDate</code>时间之后.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * isAfter(null,toDate("2011-05-01", COMMON_DATE))                                 =   false
     * isAfter(toDate("2011-04-01", COMMON_DATE),toDate("2011-05-01", COMMON_DATE))    =   false
     * isAfter(toDate("2011-03-10", COMMON_DATE),toDate("2011-03-05", COMMON_DATE))    =   true
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            指定的日期
     * @param whenDate
     *            比照日期
     * @return 如果 <code>whenDate</code> 是null,抛出 {@link NullPointerException} <br>
     *         如果 <code>date</code> 是null,返回false<br>
     *         否则返回 <code>date.after(whenDate)</code>
     * @see java.util.Date#after(Date)
     * @since 1.2.2
     */
    public static boolean isAfter(Date date,Date whenDate){
        Validate.notNull(whenDate, "whenDate can't be null!");
        return null != date && date.after(whenDate);
    }

    // [end]

    // [start]isInTime 时间区间内

    /**
     * 判断当前时间 是否在格式是<code>pattern</code>的 <code>beginDate</code> 和 <code>endDate</code>两个时间之间.
     * 
     * <p>
     * 判断逻辑 <span style="color:green">date{@code >=}beginDate &&date {@code <=}endDate</span>
     * </p>
     * 
     * <h3>使用场景:</h3>
     * <blockquote>
     * 比如当日达,判断下单的时间是否是 08:00-16:00 之间, 超过这个时间段的订单不能下
     * </blockquote>
     *
     * @param beginDateString
     *            开始时间
     * @param endDateString
     *            结束时间
     * @param datePattern
     *            the date pattern
     * @return 如果 <code>beginDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beginDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>endDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>endDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>datePattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>datePattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #isInTime(Date, String, String, String)
     * @since 2.1.0
     */
    public static boolean isInTime(String beginDateString,String endDateString,String datePattern){
        Date now = now();
        return isInTime(now, beginDateString, endDateString, datePattern);
    }

    /**
     * 判断指定时间 <code>date</code>, 是否在格式是<code>pattern</code>的 <code>beginDate</code> 和 <code>endDate</code>两个时间之间.
     * 
     * <p>
     * 判断逻辑 <span style="color:green">date{@code >=}beginDate &&date {@code <=}endDate</span>
     * </p>
     * 
     * 
     * <h3>使用场景:</h3>
     * <blockquote>
     * 比如当日达,判断下单的时间是否是 08:00-16:00 之间, 超过这个时间段的订单不能下
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.isInTime(toDate("2020-01-06 10:00:00", COMMON_DATE_AND_TIME), "08:00:00", "16:00:00", COMMON_TIME) = true
     * </pre>
     * 
     * </blockquote>
     *
     * @param date
     *            the date
     * @param beginDateString
     *            the begin date string
     * @param endDateString
     *            the end date string
     * @param datePattern
     *            the date pattern
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beginDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beginDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>endDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>endDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>datePattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>datePattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #isInTime(Date, Date, Date)
     * @since 2.1.0
     */
    public static boolean isInTime(Date date,String beginDateString,String endDateString,String datePattern){
        Validate.notNull(date, "date can't be null!");
        Validate.notBlank(beginDateString, "beginDateString can't be blank!");
        Validate.notBlank(endDateString, "endDateString can't be blank!");
        Validate.notBlank(datePattern, "datePattern can't be blank!");

        //---------------------------------------------------------------

        //大家使用同样的格式进行比较
        Date compareDate = toDate(toString(date, datePattern), datePattern);
        Date beginDate = toDate(beginDateString, datePattern);
        Date endDate = toDate(endDateString, datePattern);

        return isInTime(compareDate, beginDate, endDate);
    }

    /**
     * 判断指定日期 <code>date</code> 是否在 <code>beginDate</code> 和 <code>endDate</code>两个时间之间.
     * 
     * <p>
     * 判断逻辑 <span style="color:green">date{@code >=}beginDate &&date {@code <=}endDate</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.isInTime("2012-10-16 23:00:02", "2012-10-10 22:59:00", "2012-10-18 22:59:00") = true
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date
     *            指定日期
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return 如果 <code>date</code> 在 <code>beginDate</code>之后, 并且指定日期 <code>date</code> 在 <code>endDate</code>之前,返回true<br>
     * @throws NullPointerException
     *             如果 <code>date</code> 是null,或者 <code>beginDate</code> 是null 或者 <code>endDate</code> 是null
     * @see Date#after(Date)
     * @see Date#before(Date)
     */
    public static boolean isInTime(Date date,Date beginDate,Date endDate){
        Validate.notNull(date, "date can't be null!");
        Validate.notNull(beginDate, "beginDate can't be null!");
        Validate.notNull(endDate, "endDate can't be null!");
        //底层 用的是  date> beginDate && date <endDate
        //return date.after(beginDate) && date.before(endDate);

        //date>=beginDate &&date <=endDate
        return (date.compareTo(beginDate) == 0 || date.compareTo(beginDate) == 1)
                        && (date.compareTo(endDate) == 0 || date.compareTo(endDate) == -1);
    }

    //---------------------------------------------------------------

    /**
     * 判断当前时间 不在<code>pattern</code>的 <code>beginDate</code> 和 <code>endDate</code>两个时间之间.
     * 
     * <h3>使用场景:</h3>
     * <blockquote>
     * 比如判断图书馆不在有效期,如果不在有效期前端会给出提示
     * </blockquote>
     *
     * @param beginDateString
     *            开始时间
     * @param endDateString
     *            结束时间
     * @param datePattern
     *            the date pattern
     * @return 如果 <code>beginDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beginDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>endDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>endDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>datePattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>datePattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #isInTime(Date, String, String, String)
     * @since 3.3.9
     */
    public static boolean isNotInTime(String beginDateString,String endDateString,String datePattern){
        return !isInTime(beginDateString, endDateString, datePattern);
    }

    /**
     * 判断指定时间 <code>date</code>, 不在格式是<code>pattern</code>的 <code>beginDate</code> 和 <code>endDate</code>两个时间之间.
     * 
     * <h3>使用场景:</h3>
     * <blockquote>
     * 比如判断图书馆不在有效期,如果不在有效期前端会给出提示
     * </blockquote>
     *
     * @param date
     *            the date
     * @param beginDateString
     *            开始时间
     * @param endDateString
     *            结束时间
     * @param datePattern
     *            the date pattern
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beginDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>beginDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>endDateString</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>endDateString</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果 <code>datePattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>datePattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #isInTime(Date, Date, Date)
     * @since 3.3.9
     */
    public static boolean isNotInTime(Date date,String beginDateString,String endDateString,String datePattern){
        return !isInTime(date, beginDateString, endDateString, datePattern);
    }

    /**
     * 判断指定日期 <code>date</code> 不在 <code>beginDate</code> 和 <code>endDate</code>两个时间之间.
     * 
     * @param date
     *            指定日期
     * @param beginDate
     *            开始时间
     * @param endDate
     *            结束时间
     * @return 如果 <code>date</code> 不在 <code>beginDate</code>和 <code>endDate</code>之间,返回true<br>
     * @throws NullPointerException
     *             如果 <code>date</code> 是null,或者 <code>beginDate</code> 是null 或者 <code>endDate</code> 是null
     * @see Date#after(Date)
     * @see Date#before(Date)
     * @since 3.3.9
     */
    public static boolean isNotInTime(Date date,Date beginDate,Date endDate){
        return !isInTime(date, beginDate, endDate);
    }

    //---------------------------------------------------------------

    /**
     * 判断指定的日期 <code>date</code>,是不是今天的日期.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.isToday(new Date()) = true
     * 
     * <span style="color:green">// 如果今天 是2017年12月14日</span>
     * DateUtil.isToday(toDate("2016-06-16 22:59:00", COMMON_DATE_AND_TIME)) = false
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>性能对比:</h3>
     * 
     * <blockquote>
     * 
     * 1000000 循环,
     * 
     * <ul>
     * <li>DateUtils#isSameDay(Date, Date) ,893毫秒;</li>
     * <li>isEquals(date, new Date(), DatePattern.COMMON_DATE),1秒335毫秒</li>
     * <li>DateUtil.getDayStartAndEndPair 1秒185毫秒</li>
     * </ul>
     * </blockquote>
     *
     * @param date
     *            指定的日期
     * @return 如果指定的日期是今天,那么返回true,否则返回false <br>
     *         如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     * @see DateUtils#isSameDay(Date, Date)
     * @since 1.10.6
     */
    public static boolean isToday(Date date){
        Validate.notNull(date, "date can't be null!");
        return DateUtils.isSameDay(date, now());
    }

    // [end]

    // [start]isEquals
    /**
     * 在相同格式下 <code>datePattern</code>,将两个日期转成字符串判断是否相等.
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.isEquals(toDate("2016-06-16 22:59:00", COMMON_DATE_AND_TIME), toDate("2016-06-16", COMMON_DATE), COMMON_DATE) = true
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>常用于判断两个时间是否是同一个时间段,比如相同day,相同小时,相同年等等</li>
     * </ol>
     * </blockquote>
     * 
     * @param date1
     *            日期1
     * @param date2
     *            日期2
     * @param datePattern
     *            格式 {@link DatePattern}
     * @return 相等返回true,不相等则为false<br>
     *         如果 <code>date1 == date2</code> 直接返回true<br>
     *         如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>pattern</code> 是 null,抛出 {@link NullPointerException}<br>
     *         如果 <code>pattern</code> 是 blank,抛出 {@link IllegalArgumentException}<br>
     * @see #toString(Date, String)
     * @see com.feilong.lib.lang3.time.DateUtils#isSameDay(Date, Date)
     * @since 1.0.5 change name from isEqual to isEquals
     */
    public static boolean isEquals(Date date1,Date date2,String datePattern){
        Validate.notNull(date1, "date1 can't be null!");
        Validate.notNull(date2, "date2 can't be null!");

        Validate.notBlank(datePattern, "datePattern can't be blank!");

        return date1 == date2 || toString(date1, datePattern).equals(toString(date2, datePattern));
    }

    // [end]

    // [start]isLeapYear 闰年

    /**
     * 判断指定年 <code>year</code> 是否为闰年 .
     * 
     * <p>
     * 规则: {@code (year % 4 == 0 && year % 100 != 0) || year % 400 == 0}
     * </p>
     * 
     * <h3>闰年原因:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 地球绕太阳运行周期为<b>365天5小时48分46秒</b>(合365.24219天)即一回归年(tropical year).<br>
     * 公历的平年只有365日,比回归年短约0.2422日,所余下的时间约为四年累计一天,故四年于2月加1天,使当年的历年长度为366日,这一年就为闰年.<br>
     * </p>
     * 
     * <p>
     * 现行公历中每400年有97个闰年.按照每四年一个闰年计算,平均每年就要多算出0.0078天,这样经过四百年就会多算出大约3天来,<br>
     * 因此,每四百年中要减少三个闰年.
     * </p>
     * 
     * <p>
     * 所以规定,公历年份是整百数的,必须是400的倍数的才是闰年,不是400的倍数的,虽然是100的倍数,也是平年,<br>
     * 这就是通常所说的:<b>四年一闰,百年不闰,四百年再闰</b>.<br>
     * 
     * 例如,2000年是闰年,1900年则是平年.<br>
     * </p>
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>需要注意的是,有些文章说3200 是平年, 参见<a href="https://baike.baidu.com/item/%E9%97%B0%E5%B9%B4/27098#6">精确计算方法</a>,但是 jdk中 判定3200 依然是闰年,并且
     * <a href="https://en.wikipedia.org/wiki/Leap_year">wiki</a> 也没有提及3200是平年的故事</li>
     * <li>在 {@link java.util.GregorianCalendar#gregorianCutoverYearJulian 1582}年之前是四年一闰,
     * {@link java.util.GregorianCalendar#gregorianCutoverYearJulian 1582}年之后 四年一闰,百年不闰,四百年再闰</li>
     * </ol>
     * </blockquote>
     * 
     * @param year
     *            年份
     * @return 四年一闰,百年不闰,四百年再闰
     * @see GregorianCalendar#isLeapYear(int)
     * @see <a href="https://en.wikipedia.org/wiki/Leap_year">Leap_year</a>
     */
    public static boolean isLeapYear(int year){
        return new GregorianCalendar().isLeapYear(year);
    }

    // [end]

    //---------------------------------------------------------------

    // [start] 获得时间 /时间数组,可以用于sql查询

    /**
     * 获得 今天的开始时间 <code>00:00:00.000</code> 及今天的结束时间 <code>23:59:59.999</code>.
     * 
     * <p>
     * 一般用于统计当天数据,between ... and ...
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如今天的是 2012-10-16 22:18:34
     * </p>
     * 
     * <b>返回:</b> 2012-10-16 00:00:00.000, 2012-10-16 23:59:59.999
     * 
     * </blockquote>
     *
     * @return 左边,今天的开始时间 <code>00:00:00.000</code> <br>
     *         右边,今天的结束时间 <code>23:59:59.999</code> <br>
     * @see "java.time.LocalDate#atStartOfDay()"
     * @see "java.time.LocalDate#atStartOfDay()"
     * @since 1.10.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static Pair<Date, Date> getTodayStartAndEndPair(){
        Date date = now();
        return getDayStartAndEndPair(date);
    }

    /**
     * 获得 昨天的开始时间 <code>00:00:00.000</code> 及昨天的结束时间 <code>23:59:59.999</code>.
     * 
     * <p>
     * 一般用于统计昨天数据,between ... and ...
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如今天的是 2012-10-16 22:18:34
     * </p>
     * 
     * <b>返回:</b> 2012-10-15 00:00:00.000, 2012-10-15 23:59:59.999
     * 
     * </blockquote>
     *
     * @return 左边,昨天的开始时间 <code>00:00:00.000</code> <br>
     *         右边,昨天的结束时间 <code>23:59:59.999</code> <br>
     * @since 1.10.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static Pair<Date, Date> getYesterdayStartAndEndPair(){
        Date date = now();
        Date yesteday = addDay(date, -1);
        return getDayStartAndEndPair(yesteday);
    }

    /**
     * 获得 指定日的开始时间 <code>00:00:00.000</code> 及指定日的结束时间 <code>23:59:59.999</code>.
     * 
     * <p>
     * 一般用于统计指定日期数据,between ... and ...
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如传入的是 2012-10-16 22:18:34
     * </p>
     * 
     * <b>返回:</b> 2012-10-16 00:00:00.000, 2012-10-16 23:59:59.999
     * 
     * </blockquote>
     * 
     * @param date
     *            the date
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     *         左边,指定日的开始时间 <code>00:00:00.000</code> <br>
     *         右边,指定日的结束时间 <code>23:59:59.999</code> <br>
     * @since 1.10.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static Pair<Date, Date> getDayStartAndEndPair(Date date){
        Validate.notNull(date, "date can't be null!");
        return Pair.of(getFirstDateOfThisDay(date), getLastDateOfThisDay(date));
    }

    //---------------------------------------------------------------

    /**
     * 获得 当前月的开始时间 <code>00:00:00.000</code> 及当前月结束时间 <code>23:59:59.999</code>.
     * 
     * <p>
     * 一般用于统计当前月的数据,between ... and ...
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如传入的是 2012-10-16 22:18:34
     * </p>
     * 
     * <b>返回:</b> 2012-10-01 00:00:00.000, 2012-10-31 23:59:59.999
     * 
     * </blockquote>
     * 
     * @return 左边,当前月的第一天 <code>00:00:00.000</code> <br>
     *         右边,当前月最后一天 <code>23:59:59.999</code> <br>
     * @since 1.10.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static Pair<Date, Date> getMonthStartAndEndPair(){
        return getMonthStartAndEndPair(now());
    }

    /**
     * 获得 指定月的开始时间 <code>00:00:00.000</code> 及指定月的结束时间 <code>23:59:59.999</code>.
     * 
     * <p>
     * 一般用于统计指定月数据,between ... and ...
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如传入的是 2012-10-16 22:18:34
     * </p>
     * 
     * <b>返回:</b> 2012-10-01 00:00:00.000, 2012-10-31 23:59:59.999
     * 
     * </blockquote>
     * 
     * @param date
     *            the date
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     *         左边,当前月的第一天 <code>00:00:00.000</code> <br>
     *         右边,当前月最后一天 <code>23:59:59.999</code> <br>
     * @since 1.10.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static Pair<Date, Date> getMonthStartAndEndPair(Date date){
        Validate.notNull(date, "date can't be null!");
        return Pair.of(getFirstDateOfThisMonth(date), getLastDateOfThisMonth(date));
    }

    /**
     * 获得 当前年的开始时间 <code>00:00:00.000</code> 及当前年的结束时间 <code>23:59:59.999</code>.
     * 
     * <p>
     * 一般用于统计当前年数据,between ... and ...
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如传入的是 2012-10-16 22:18:34
     * </p>
     * 
     * <b>返回:</b> 2012-01-01 00:00:00.000, 2012-12-31 23:59:59.999
     * 
     * </blockquote>
     * 
     * @return 左边,当前年的第一天 <code>00:00:00.000</code> <br>
     *         右边,当前年最后一天 <code>23:59:59.999</code> <br>
     * @since 1.10.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static Pair<Date, Date> getYearStartAndEndPair(){
        return getYearStartAndEndPair(now());
    }

    /**
     * 获得 指定年的开始时间 <code>00:00:00.000</code> 及指定年的结束时间 <code>23:59:59.999</code>.
     * 
     * <p>
     * 一般用于统计指定年数据,between ... and ...
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 比如传入的是 2012-10-16 22:18:34
     * </p>
     * 
     * <b>返回:</b> 2012-01-01 00:00:00.000, 2012-12-31 23:59:59.999
     * 
     * </blockquote>
     * 
     * @param date
     *            the date
     * @return 如果 <code>date</code> 是null,抛出 {@link NullPointerException}<br>
     *         左边,当前年的第一天 <code>00:00:00.000</code> <br>
     *         右边,当前年最后一天 <code>23:59:59.999</code> <br>
     * @since 1.10.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static Pair<Date, Date> getYearStartAndEndPair(Date date){
        Validate.notNull(date, "date can't be null!");

        return Pair.of(getFirstDateOfThisYear(date), getLastDateOfThisYear(date));
    }

    // [end]

    //---------------------------------------------------------------

    /**
     * 将开始时间 <code>beginTimeMillis</code> 到当前时间
     * <code>System.currentTimeMillis()</code>,两日期之间的<span style="color:red">绝对值</span>间隔,格式化成直观的表示方式.
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>常用于日志输出一段代码执行时长</li>
     * <li>计算的是开始时间 <code>beginTimeMillis</code> 到当前时间 <code>System.currentTimeMillis()</code> 绝对值间隔时间,也就是说不care 时间先后顺序</li>
     * <li>间隔时间转成 <b>天,小时,分钟,秒,毫秒</b> 中文文字,月和较大的格式不使用</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * long t1 = System.currentTimeMillis();
     * 
     * <span style="color:green">// do some logic</span>
     * <span style="color:green">// balabala logic</span>
     * 
     * log.info("use time: [{}]", DateUtil.formatDurationUseBeginTimeMillis(t1));
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>和 "org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 的区别:
     * </h3>
     * <blockquote>
     * <ol>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 显示的是英文,该方法显示的是中文</li>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 最小单位是秒,该方法最小单位是毫秒</li>
     * </ol>
     * </blockquote>
     * 
     * @param beginTimeMillis
     *            开始时间
     * @return 比如 6天13小时3分钟53秒259毫秒
     * @see <a href="http://stackoverflow.com/questions/266825/how-to-format-a-duration-in-java-e-g-format-hmmss">how-to-format-a-duration-
     *      in-java-e-g-format-hmmss</a>
     * @since 3.5.1
     */
    public static String formatDurationUseBeginTimeMillis(long beginTimeMillis){
        return formatDuration(System.currentTimeMillis() - beginTimeMillis);
    }

    /**
     * 将开始时间 <code>beginDate</code> 到当前时间 <code>now()</code>,两日期之间的<span style="color:red">绝对值</span>间隔,格式化成直观的表示方式.
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>常用于日志输出一段代码执行时长</li>
     * <li>计算的是开始时间 <code>beginDate</code> 到当前时间 <code>now()</code> 绝对值间隔时间,也就是说不care 时间先后顺序</li>
     * <li>间隔时间转成 <b>天,小时,分钟,秒,毫秒</b> 中文文字,月和较大的格式不使用</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * Date beginDate = now();
     * 
     * <span style="color:green">// do some logic</span>
     * <span style="color:green">// balabala logic</span>
     * 
     * log.info("use time: [{}]", DateUtil.formatDuration(beginDate));
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * <p>
     * 如果当前时间是 2016-07-09 13:03:53.259
     * </p>
     * 
     * <pre class="code">
     * Date date = toDate("2016-07-03 00:00:00", COMMON_DATE_AND_TIME);
     * log.debug(DateUtil.formatDuration(date));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 6天13小时3分钟53秒259毫秒
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>和 "org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 的区别:
     * </h3>
     * <blockquote>
     * <ol>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 显示的是英文,该方法显示的是中文</li>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 最小单位是秒,该方法最小单位是毫秒</li>
     * </ol>
     * </blockquote>
     * 
     * @param beginDate
     *            开始日期
     * @return 如果 <code>beginDate</code> 是null,抛出 {@link NullPointerException}<br>
     * @see #formatDuration(Date, Date)
     * @see <a href="http://stackoverflow.com/questions/266825/how-to-format-a-duration-in-java-e-g-format-hmmss">how-to-format-a-duration-
     *      in-java-e-g-format-hmmss</a>
     * @since 1.8.4 change name from getIntervalForView
     * @since 3.0.0 move from DateExtensionUtil
     * @deprecated since 3.5.1 ,pls use {@link #formatDurationUseBeginTimeMillis(long)},免掉date开销,性能稍微高
     */
    @Deprecated
    public static String formatDuration(Date beginDate){
        return formatDuration(beginDate, now());
    }

    /**
     * 将<code>beginDate</code>和 <code>endDate</code> 两日期之间的<span style="color:red">绝对值</span>间隔,格式化成直观的表示方式.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>常用于日志输出一段代码执行时长</li>
     * <li>计算的是开始时间 <code>beginDate</code> 到结束时间 <code>endDate</code> 绝对值间隔时间,也就是说不care 时间先后顺序</li>
     * <li>间隔时间转成 <b>天,小时,分钟,秒,毫秒</b> 中文文字,月和较大的格式不使用</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * Date beginDate = now();
     * 
     * <span style="color:green">// do some logic</span>
     * <span style="color:green">// balabala logic</span>
     * 
     * log.info("use time: [{}]", DateUtil.formatDuration(beginDate, now()));
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.formatDuration(2011-05-19 8:30:40,2011-05-19 11:30:24)             = 2小时59分44秒
     * DateUtil.formatDuration(2011-05-19 11:31:25.456,2011-05-19 11:30:24.895)    = 1分钟1秒
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>和 "org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 的区别:
     * </h3>
     * <blockquote>
     * <ol>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 显示的是英文,该方法显示的是中文</li>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 最小单位是秒,该方法最小单位是毫秒</li>
     * </ol>
     * </blockquote>
     * 
     * @param beginDate
     *            开始日期
     * @param endDate
     *            结束日期
     * @return 如果 <code>beginDate</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>endDate</code> 是null,抛出 {@link NullPointerException}
     * @see #formatDuration(long)
     * @see #getIntervalTime(Date, Date)
     * @see <a href="http://stackoverflow.com/questions/266825/how-to-format-a-duration-in-java-e-g-format-hmmss">how-to-format-a-duration-
     *      in-java-e-g-format-hmmss</a>
     * @since 1.8.4 change name from getIntervalForView
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static String formatDuration(Date beginDate,Date endDate){
        return formatDuration(getIntervalTime(beginDate, endDate));
    }

    /**
     * 将间隔毫秒数 <code>spaceMilliseconds</code>,格式化成直观的表示方式.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>常用于日志输出一段代码执行时长</li>
     * <li>间隔时间转成 <b>天,小时,分钟,秒,毫秒</b> 中文文字,月和较大的格式不使用</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.formatDuration(13516)    = 13秒516毫秒
     * DateUtil.formatDuration(0)        = 0
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>和 "org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 的区别:
     * </h3>
     * <blockquote>
     * <ol>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 显示的是英文,该方法显示的是中文</li>
     * <li>"org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)" 最小单位是秒,该方法最小单位是毫秒</li>
     * </ol>
     * </blockquote>
     * 
     * @param spaceMilliseconds
     *            总共相差的毫秒数
     * @return 如果 spaceMilliseconds 是0 直接返回0<br>
     *         如果 {@code spaceMilliseconds < 0},抛出 {@link IllegalArgumentException}
     * @see #getIntervalDay(long)
     * @see #getIntervalHour(long)
     * @see #getIntervalMinute(long)
     * @see #getIntervalSecond(long)
     * @see "org.apache.commons.lang3.time.DurationFormatUtils#formatDurationWords(long, boolean, boolean)"
     * @see <a href="http://stackoverflow.com/questions/266825/how-to-format-a-duration-in-java-e-g-format-hmmss">how-to-format-a-duration-
     *      in-java-e-g-format-hmmss</a>
     * @since 1.8.4 change name from getIntervalForView
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static String formatDuration(long spaceMilliseconds){
        Validate.isTrue(spaceMilliseconds >= 0, "spaceMilliseconds can't <0");

        if (0 == spaceMilliseconds){
            return "0";
        }
        //---------------------------------------------------------------

        // 间隔天数
        long spaceDay = getIntervalDay(spaceMilliseconds);
        // 间隔小时 减去间隔天数后,
        long spaceHour = getIntervalHour(spaceMilliseconds) - spaceDay * 24;
        // 间隔分钟 减去间隔天数及间隔小时后,
        long spaceMinute = getIntervalMinute(spaceMilliseconds) - (spaceDay * 24 + spaceHour) * 60;
        // 间隔秒 减去间隔天数及间隔小时,间隔分钟后,
        long spaceSecond = getIntervalSecond(spaceMilliseconds) - ((spaceDay * 24 + spaceHour) * 60 + spaceMinute) * 60;
        // 间隔毫秒 减去间隔天数及间隔小时,间隔分钟,间隔秒后,
        long spaceMillisecond = spaceMilliseconds - (((spaceDay * 24 + spaceHour) * 60 + spaceMinute) * 60 + spaceSecond) * 1000;

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();
        if (0 != spaceDay){
            sb.append(spaceDay + UNIT_CONFIG_MAP.get("unitname.day"));
        }
        if (0 != spaceHour){
            sb.append(spaceHour + UNIT_CONFIG_MAP.get("unitname.hour"));
        }
        if (0 != spaceMinute){
            sb.append(spaceMinute + UNIT_CONFIG_MAP.get("unitname.minute"));
        }
        if (0 != spaceSecond){
            sb.append(spaceSecond + UNIT_CONFIG_MAP.get("unitname.second"));
        }
        if (0 != spaceMillisecond){
            sb.append(spaceMillisecond + UNIT_CONFIG_MAP.get("unitname.millisecond"));
        }
        return sb.toString();
    }

    //---------------------------------------------------------------
    // [start]interval时间间隔

    /**
     * 获得相差的月数(<span style="color:red">绝对值</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <ul>
     * <li>值=两个时间相差毫秒的绝对值/{@link TimeInterval#MILLISECOND_PER_MONTH}</li>
     * <li>当两者时间差不足1个月,返回0</li>
     * <li>此方法1个月按照30天计算的</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getIntervalMonth(
     *      toDate("2014-01-01 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2014-02-01 00:00:00",COMMON_DATE_AND_TIME)) = 1
     * 
     * DateUtil.getIntervalMonth(
     *      toDate("2016-08-01",COMMON_DATE),
     *      toDate("2016-08-07",COMMON_DATE)) = 0
     * 
     * DateUtil.getIntervalMonth(
     *      toDate("2016-08-21",COMMON_DATE),
     *      toDate("2016-08-21",COMMON_DATE)) = 0
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date1
     *            第一个时间
     * @param date2
     *            第二个时间
     * @return 如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}
     * @see #getIntervalMonth(long)
     * @since 3.1.1
     */
    public static int getIntervalMonth(Date date1,Date date2){
        return getIntervalMonth(getIntervalTime(date1, date2));
    }

    /**
     * 获得相差的月数.
     *
     * @param spaceTime
     *            the space time
     * @return the interval week
     * @see com.feilong.core.TimeInterval#MILLISECOND_PER_MONTH
     * @since 3.1.1
     */
    private static int getIntervalMonth(long spaceTime){
        return (int) (spaceTime / (MILLISECOND_PER_MONTH));
    }

    /**
     * 获得相差的星期数(<span style="color:red">绝对值</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <ul>
     * <li>值=两个时间相差毫秒的绝对值/{@link TimeInterval#MILLISECOND_PER_WEEK}</li>
     * <li>当两者时间差不足1星期,返回0</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getIntervalWeek(
     *      toDate("2014-01-01 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2014-02-01 00:00:00",COMMON_DATE_AND_TIME)) = 4
     * 
     * DateUtil.getIntervalWeek(
     *      toDate("2016-08-01",COMMON_DATE),
     *      toDate("2016-08-07",COMMON_DATE)) = 0
     * 
     * DateUtil.getIntervalWeek(
     *      toDate("2016-08-01",COMMON_DATE),
     *      toDate("2016-08-08",COMMON_DATE)) = 1
     *      
     * DateUtil.getIntervalWeek(
     *      toDate("2016-08-21",COMMON_DATE),
     *      toDate("2016-08-21",COMMON_DATE)) = 0
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date1
     *            第一个时间
     * @param date2
     *            第二个时间
     * @return 如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}
     * @see #getIntervalWeek(long)
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static int getIntervalWeek(Date date1,Date date2){
        return getIntervalWeek(getIntervalTime(date1, date2));
    }

    /**
     * 获得相差的星期数.
     *
     * @param spaceTime
     *            the space time
     * @return the interval week
     * @see com.feilong.core.TimeInterval#SECONDS_PER_WEEK
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    private static int getIntervalWeek(long spaceTime){
        return (int) (spaceTime / (MILLISECOND_PER_WEEK));
    }

    //---------------------------------------------------------------

    /**
     * 计算两个时间相差的的天数(<span style="color:red">绝对值</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <ul>
     * <li>值=两个时间相差毫秒的绝对值/{@link TimeInterval#MILLISECOND_PER_DAY}</li>
     * <li>当两者时间差不足1天,返回0</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getIntervalDay(
     *      toDate("2008-08-24",COMMON_DATE),
     *      toDate("2008-08-27",COMMON_DATE)) = 3
     * 
     * DateUtil.getIntervalDay(
     *      toDate("2016-08-21 12:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2016-08-22 11:00:00",COMMON_DATE_AND_TIME)) = 0
     * 
     * DateUtil.getIntervalDay(
     *      toDate("2016-08-21",COMMON_DATE),
     *      toDate("2016-08-22",COMMON_DATE)) = 1
     *      
     * DateUtil.getIntervalDay(
     *      toDate("2016-02-28",COMMON_DATE),
     *      toDate("2016-03-02",COMMON_DATE)) = 3
     * 
     * DateUtil.getIntervalDay(
     *      toDate("2016-08-31",COMMON_DATE),
     *      toDate("2016-09-02",COMMON_DATE)) = 2
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date1
     *            第一个时间
     * @param date2
     *            第二个时间
     * @return 如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}
     * @see #getIntervalTime(Date, Date)
     * @see #getIntervalDay(long)
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static int getIntervalDay(Date date1,Date date2){
        return getIntervalDay(getIntervalTime(date1, date2));
    }

    /**
     * 两个时间相差的天数.
     * 
     * @param spaceMilliseconds
     *            间隔毫秒
     * @return 相差的天数
     * @see TimeInterval#SECONDS_PER_DAY
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    private static int getIntervalDay(long spaceMilliseconds){
        return (int) (spaceMilliseconds / (MILLISECOND_PER_DAY));
    }

    //---------------------------------------------------------------

    /**
     * 两个时间相差的的小时数(<span style="color:red">绝对值</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <ul>
     * <li>值=两个时间相差毫秒的绝对值/{@link TimeInterval#MILLISECOND_PER_HOUR}</li>
     * <li>当两者时间差不足1小时,返回0</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getIntervalHour(
     *      toDate("2014-01-01 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2014-01-01 01:00:00",COMMON_DATE_AND_TIME)) = 1
     * 
     * DateUtil.getIntervalHour(
     *      toDate("2014-01-01 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2014-01-01 00:59:00",COMMON_DATE_AND_TIME)) = 0
     * 
     * DateUtil.getIntervalHour(
     *      toDate("2014-01-01 00:59:00",COMMON_DATE_AND_TIME),
     *      toDate("2014-01-01 00:00:00",COMMON_DATE_AND_TIME)) = 0
     *      
     * DateUtil.getIntervalHour(
     *      toDate("2016-08-21 23:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2016-08-22 01:00:00",COMMON_DATE_AND_TIME)) = 2
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date1
     *            第一个时间
     * @param date2
     *            第二个时间
     * @return 如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}
     * @see #getIntervalTime(Date, Date)
     * @see #getIntervalHour(long)
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static int getIntervalHour(Date date1,Date date2){
        return getIntervalHour(getIntervalTime(date1, date2));
    }

    /**
     * 两个时间相差的小时数.
     * 
     * @param spaceMilliseconds
     *            间隔毫秒
     * @return 相差的小时数
     * @see TimeInterval#MILLISECOND_PER_HOUR
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    private static int getIntervalHour(long spaceMilliseconds){
        return (int) (spaceMilliseconds / (MILLISECOND_PER_HOUR));
    }

    //---------------------------------------------------------------

    /**
     * 两个时间相差的分钟(<span style="color:red">绝对值</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <ul>
     * <li>值=两个时间相差毫秒的绝对值/{@link TimeInterval#MILLISECOND_PER_MINUTE}</li>
     * <li>当两者时间差不足1分钟,返回0</li>
     * </ul>
     * 
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getIntervalMinute(
     *      toDate("2008-08-24 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2008-08-24 01:00:00",COMMON_DATE_AND_TIME)) = 60
     * 
     * DateUtil.getIntervalMinute(
     *      toDate("2008-08-24 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2008-08-24 00:00:00",COMMON_DATE_AND_TIME)) = 0
     * 
     * DateUtil.getIntervalMinute(
     *      toDate("2008-08-24 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2008-08-24 00:00:50",COMMON_DATE_AND_TIME)) = 0
     * 
     * DateUtil.getIntervalMinute(
     *      toDate("2008-08-24 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2008-08-23 00:00:00",COMMON_DATE_AND_TIME)) = SECONDS_PER_DAY / 60
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date1
     *            第一个时间
     * @param date2
     *            第二个时间
     * @return 如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}
     * @see #getIntervalTime(Date, Date)
     * @see #getIntervalMinute(long)
     * @since 1.8.6
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static int getIntervalMinute(Date date1,Date date2){
        return getIntervalMinute(getIntervalTime(date1, date2));
    }

    /**
     * 两个时间相差的分钟.
     * 
     * @param spaceMilliseconds
     *            间隔毫秒
     * @return 相差的分钟
     * @see TimeInterval#MILLISECOND_PER_MINUTE
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    private static int getIntervalMinute(long spaceMilliseconds){
        return (int) (spaceMilliseconds / (MILLISECOND_PER_MINUTE));
    }

    //---------------------------------------------------------------

    /**
     * 两个时间相差的秒数(<span style="color:red">绝对值</span>).
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * 
     * <ul>
     * <li>值=两个时间相差毫秒的绝对值/{@link TimeInterval#MILLISECOND_PER_SECONDS}</li>
     * <li>不足1秒返回0</li>
     * </ul>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getIntervalSecond(
     *      toDate("2016-08-22 00:00:00",COMMON_DATE_AND_TIME),
     *      toDate("2016-08-22 00:00:08",COMMON_DATE_AND_TIME)) = 8
     * 
     * DateUtil.getIntervalSecond(
     *      toDate("2016-08-21 23:59:20",COMMON_DATE_AND_TIME),
     *      toDate("2016-08-22 00:00:20",COMMON_DATE_AND_TIME)) = 60
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date1
     *            第一个时间
     * @param date2
     *            第二个时间
     * @return 如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}
     * @see #getIntervalTime(Date, Date)
     * @see #getIntervalSecond(long)
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static int getIntervalSecond(Date date1,Date date2){
        return getIntervalSecond(getIntervalTime(date1, date2));
    }

    /**
     * 两个时间相差的秒数.
     * 
     * <p>
     * 不足1秒返回0
     * </p>
     * 
     * @param spaceMilliseconds
     *            间隔毫秒
     * @return 相差的秒数
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    private static int getIntervalSecond(long spaceMilliseconds){
        return (int) (spaceMilliseconds / MILLISECOND_PER_SECONDS);
    }

    //---------------------------------------------------------------

    /**
     * 两个时间相差的<b>毫秒数</b> (<span style="color:red">绝对值</span>).
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * DateUtil.getIntervalTime(
     *      toDate("2016-07-16 15:21:00",COMMON_DATE_AND_TIME),
     *      toDate("2016-07-16 15:21:01",COMMON_DATE_AND_TIME)) = 1000
     * 
     * DateUtil.getIntervalTime(
     *      toDate("2016-07-16 15:21:00",COMMON_DATE_AND_TIME),
     *      toDate("2016-07-16 15:22:00",COMMON_DATE_AND_TIME)) = 60000
     * </pre>
     * 
     * </blockquote>
     * 
     * @param date1
     *            第一个时间
     * @param date2
     *            第二个时间
     * @return 如果 <code>date1</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>date2</code> 是null,抛出 {@link NullPointerException}
     * @see DateUtil#getTime(Date)
     * @see Math#abs(long)
     * @since 1.6.0
     * @since 3.0.0 move from DateExtensionUtil
     */
    public static long getIntervalTime(Date date1,Date date2){
        Validate.notNull(date1, "date1 can't be null!");
        Validate.notNull(date2, "date2 can't be null!");
        return Math.abs(getTime(date2) - getTime(date1));
    }

    // [end]

}