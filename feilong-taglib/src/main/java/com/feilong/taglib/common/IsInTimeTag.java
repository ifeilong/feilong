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
package com.feilong.taglib.common;

import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.date.DateUtil.toDate;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.Date;

import com.feilong.core.DatePattern;
import com.feilong.core.date.DateUtil;
import com.feilong.taglib.AbstractConditionalTag;

/**
 * 判断一个日期,是否在一个时间区间内.
 * 
 * <p>
 * 注:
 * </p>
 * <ul>
 * <li>要么设置 beginDate+endDate</li>
 * <li>要么设置 beginDateString+endDateString+pattern</li>
 * </ul>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 */
public class IsInTimeTag extends AbstractConditionalTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7116181842162212131L;

    /** 一个时间,如果为null,则使用当前时间. */
    private Date              date             = null;

    //---------------------------------------------------------------

    /** 开始时间,beginDate和 beginDateString 二者只能选择其一. */
    private Date              beginDate;

    /** 结束时间,endDate和 endDateString 二者只能选择其一. */
    private Date              endDate;

    //---------------------------------------------------------------

    /** 开始时间,beginDate和 beginDateString 二者只能选择其一. */
    private String            beginDateString;

    /** 结束时间,endDate和 endDateString 二者只能选择其一. */
    private String            endDateString;

    /** 如果 使用的 beginDateString 和 endDateString ,其format模式. */
    private String            pattern;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.base.AbstractConditionalTag#condition()
     */
    @Override
    public boolean condition(){
        // 不能直接使用 date ,全局变量 一旦赋值 不会变化
        Date compareDate = defaultIfNull(date, now());
        return null != beginDate ? DateUtil.isInTime(compareDate, beginDate, endDate)
                        : isInTime(compareDate, beginDateString, endDateString, pattern);
    }

    //---------------------------------------------------------------

    /**
     * 判断指定日期 <code>date</code>是否在两个时间之间.
     * 
     * <pre class="code">
     * DateUtil.isInTime("2012-10-16 23:00:02", "2012-10-10 22:59:00", "2012-10-18 22:59:00", DatePattern.commonWithTime) = true
     * </pre>
     * 
     * @param date
     *            需要判断的日期
     * @param beginTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param datePattern
     *            开始时间和结束时间的格式{@link DatePattern}
     * @return 如果 指定日期 <code>date</code> after <code>beginTime</code>, 并且 指定日期 <code>date</code> before <code>endTime</code>,返回true
     */
    private static boolean isInTime(Date date,String beginTime,String endTime,String datePattern){
        Date beginTimeDate = toDate(beginTime, datePattern);
        Date endTimeDate = toDate(endTime, datePattern);
        return DateUtil.isInTime(date, beginTimeDate, endTimeDate);
    }

    //---------------------------------------------------------------

    /**
     * Sets the 一个时间,如果为null,则使用当前时间.
     * 
     * @param date
     *            the date to set
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Sets the 开始时间,beginDate和 beginDateString 二者只能选择其一.
     * 
     * @param beginDate
     *            the beginDate to set
     */
    public void setBeginDate(Date beginDate){
        this.beginDate = beginDate;
    }

    /**
     * Sets the 结束时间,endDate和 endDateString 二者只能选择其一.
     * 
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    /**
     * Sets the 开始时间,beginDate和 beginDateString 二者只能选择其一.
     * 
     * @param beginDateString
     *            the beginDateString to set
     */
    public void setBeginDateString(String beginDateString){
        this.beginDateString = beginDateString;
    }

    /**
     * Sets the 结束时间,endDate和 endDateString 二者只能选择其一.
     * 
     * @param endDateString
     *            the endDateString to set
     */
    public void setEndDateString(String endDateString){
        this.endDateString = endDateString;
    }

    /**
     * Sets the 如果 使用的 beginDateString 和 endDateString ,其format模式.
     * 
     * @param pattern
     *            the pattern to set
     */
    public void setPattern(String pattern){
        this.pattern = pattern;
    }
}
