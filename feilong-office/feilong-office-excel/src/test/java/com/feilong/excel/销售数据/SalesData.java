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
package com.feilong.excel.销售数据;

import java.math.BigDecimal;

/**
 * The Class SalesData.
 */
public class SalesData{

    /** 年份 *. */
    private int        year;

    /** 月份 *. */
    private int        month;

    /** 总金额. */
    private BigDecimal total;

    //---------------------------------------------------------------
    /**
     * 
     */
    public SalesData(){
        super();
    }

    /**
     * @param year
     * @param month
     * @param total
     */
    public SalesData(int year, int month, BigDecimal total){
        super();
        this.year = year;
        this.month = month;
        this.total = total;
    }

    /**
     * 获得 年份 *.
     *
     * @return the year
     */
    public int getYear(){
        return year;
    }

    /**
     * 设置 年份 *.
     *
     * @param year
     *            the year to set
     */
    public void setYear(int year){
        this.year = year;
    }

    /**
     * 获得 月份 *.
     *
     * @return the month
     */
    public int getMonth(){
        return month;
    }

    /**
     * 设置 月份 *.
     *
     * @param month
     *            the month to set
     */
    public void setMonth(int month){
        this.month = month;
    }

    /**
     * 获得 总金额.
     *
     * @return the total
     */
    public BigDecimal getTotal(){
        return total;
    }

    /**
     * 设置 总金额.
     *
     * @param total
     *            the total to set
     */
    public void setTotal(BigDecimal total){
        this.total = total;
    }

}
