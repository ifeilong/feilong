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
package com.feilong.net.mail.entity;

import java.util.Date;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.2
 * @deprecated 还不work
 */
@Deprecated
public class ICalendar{

    /**
     * 开始时间
     */
    private Date   beginDate;

    /**
     * 结束时间
     */
    private Date   endDate;

    /**
     * 描述
     */
    private String description;

    /**
     * 地点
     */
    private String location;

    /**
     * summary
     * 
     */
    private String summary;

    //---------------------------------------------------------------

    /**
     * @return the beginDate
     */
    public Date getBeginDate(){
        return beginDate;
    }

    /**
     * @param beginDate
     *            the beginDate to set
     */
    public void setBeginDate(Date beginDate){
        this.beginDate = beginDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate(){
        return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    /**
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation(){
        return location;
    }

    /**
     * @param location
     *            the location to set
     */
    public void setLocation(String location){
        this.location = location;
    }

    /**
     * @return the summary
     */
    public String getSummary(){
        return summary;
    }

    /**
     * @param summary
     *            the summary to set
     */
    public void setSummary(String summary){
        this.summary = summary;
    }

}
