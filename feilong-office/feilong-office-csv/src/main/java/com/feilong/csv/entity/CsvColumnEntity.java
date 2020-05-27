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
package com.feilong.csv.entity;

import com.feilong.lib.lang3.builder.ToStringBuilder;
import com.feilong.lib.lang3.builder.ToStringStyle;

/**
 * The Class CsvColumnEntity.
 * 
 * <p>
 * 实体需要声明成 public的 ,否则反射取不到
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see com.feilong.csv.entity.CsvColumn
 * @since 1.7.1
 */
public class CsvColumnEntity{

    /** 属性名字. */
    private String propertyName;

    /**
     * The Column name.
     * 
     * @see CsvColumn#name()
     */
    private String name;

    /** The order. */
    private int    order;

    //---------------------------------------------------------------

    /**
     * Instantiates a new csv column entity.
     *
     * @param name
     *            the name
     * @param propertyName
     *            the property name
     * @param order
     *            the order
     */
    public CsvColumnEntity(String name, String propertyName, int order){
        super();
        this.name = name;
        this.propertyName = propertyName;
        this.order = order;
    }

    /**
     * 获得 the name.
     *
     * @return the name
     * @see CsvColumn#name()
     */
    public String getName(){
        return name;
    }

    /**
     * 设置 the name.
     *
     * @param name
     *            the name to set
     * @see CsvColumn#name()
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 获得 the order.
     *
     * @return the order
     */
    public int getOrder(){
        return order;
    }

    /**
     * 设置 the order.
     *
     * @param order
     *            the order to set
     */
    public void setOrder(int order){
        this.order = order;
    }

    /**
     * 获得 属性名字.
     *
     * @return the propertyName
     */
    public String getPropertyName(){
        return propertyName;
    }

    /**
     * 设置 属性名字.
     *
     * @param propertyName
     *            the propertyName to set
     */
    public void setPropertyName(String propertyName){
        this.propertyName = propertyName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
