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
package com.feilong.office.excel;

import java.io.Serializable;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 * excel个性化配置.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Feb 10, 2014 2:51:13 AM
 */
public class ExcelConfigEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID             = 6706011848777526457L;

    //---------------------------------------------------------------

    /** 是否隔行变色 默认是true. */
    private boolean           isRowChangeColor             = true;

    /** 当字段带有特殊字符串的时候是否添加样式? 默认是false. */
    private boolean           isHasSpecialStringToAddStyle = false;

    /** 是否冻结窗口 默认是true. */
    private boolean           isFreezePane                 = true;

    //---------------------------------------------------------------

    /** 特殊字符串标识. */
    private String            specialString;

    //---------------------------------------------------------------

    /** 高亮. */
    private HSSFCellStyle     hightLightCellStyle;

    /** 隔行变色. */
    private HSSFCellStyle     changeColorRowCellStyle;

    /** 隔行且变色. */
    private HSSFCellStyle     changeColorRowAndHightLightCellStyle;

    //---------------------------------------------------------------

    /**
     * Gets the 是否隔行变色 默认是true.
     * 
     * @return the isRowChangeColor
     */
    public boolean getIsRowChangeColor(){
        return isRowChangeColor;
    }

    /**
     * Sets the 是否隔行变色 默认是true.
     * 
     * @param isRowChangeColor
     *            the isRowChangeColor to set
     */
    public void setIsRowChangeColor(boolean isRowChangeColor){
        this.isRowChangeColor = isRowChangeColor;
    }

    /**
     * Gets the 当字段带有特殊字符串的时候是否添加样式? 默认是false.
     * 
     * @return the isHasSpecialStringToAddStyle
     */
    public boolean getIsHasSpecialStringToAddStyle(){
        return isHasSpecialStringToAddStyle;
    }

    /**
     * Sets the 当字段带有特殊字符串的时候是否添加样式? 默认是false.
     * 
     * @param isHasSpecialStringToAddStyle
     *            the isHasSpecialStringToAddStyle to set
     */
    public void setIsHasSpecialStringToAddStyle(boolean isHasSpecialStringToAddStyle){
        this.isHasSpecialStringToAddStyle = isHasSpecialStringToAddStyle;
    }

    /**
     * Gets the 是否冻结窗口 默认是true.
     * 
     * @return the isFreezePane
     */
    public boolean getIsFreezePane(){
        return isFreezePane;
    }

    /**
     * Sets the 是否冻结窗口 默认是true.
     * 
     * @param isFreezePane
     *            the isFreezePane to set
     */
    public void setIsFreezePane(boolean isFreezePane){
        this.isFreezePane = isFreezePane;
    }

    /**
     * Gets the 特殊字符串标识.
     * 
     * @return the specialString
     */
    public String getSpecialString(){
        return specialString;
    }

    /**
     * Sets the 特殊字符串标识.
     * 
     * @param specialString
     *            the specialString to set
     */
    public void setSpecialString(String specialString){
        this.specialString = specialString;
    }

    /**
     * Gets the 高亮.
     * 
     * @return the cellStyle_hightLight
     */
    public HSSFCellStyle getHightLightCellStyle(){
        return hightLightCellStyle;
    }

    /**
     * Sets the 高亮.
     * 
     * @param cellStyle_hightLight
     *            the cellStyle_hightLight to set
     */
    public void setHightLightCellStyle(HSSFCellStyle cellStyle_hightLight){
        this.hightLightCellStyle = cellStyle_hightLight;
    }

    /**
     * Gets the 隔行变色.
     * 
     * @return the cellStyle_changeColorRow
     */
    public HSSFCellStyle getChangeColorRowCellStyle(){
        return changeColorRowCellStyle;
    }

    /**
     * Sets the 隔行变色.
     * 
     * @param cellStyle_changeColorRow
     *            the cellStyle_changeColorRow to set
     */
    public void setChangeColorRowCellStyle(HSSFCellStyle cellStyle_changeColorRow){
        this.changeColorRowCellStyle = cellStyle_changeColorRow;
    }

    /**
     * Gets the 隔行且变色.
     * 
     * @return the cellStyle_changeColorRowAndHightLight
     */
    public HSSFCellStyle getChangeColorRowAndHightLightCellStyle(){
        return changeColorRowAndHightLightCellStyle;
    }

    /**
     * Sets the 隔行且变色.
     * 
     * @param cellStyle_changeColorRowAndHightLight
     *            the cellStyle_changeColorRowAndHightLight to set
     */
    public void setChangeColorRowAndHightLightCellStyle(HSSFCellStyle cellStyle_changeColorRowAndHightLight){
        this.changeColorRowAndHightLightCellStyle = cellStyle_changeColorRowAndHightLight;
    }

}
