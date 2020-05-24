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
package com.feilong.excel.definition;

import static com.feilong.excel.util.CellReferenceUtil.getCellRef;

import java.util.ArrayList;
import java.util.List;

import com.feilong.excel.util.CellReferenceUtil;

/**
 * The Class ExcelCell.
 */
public class ExcelCell{

    /** The row. */
    private int                           row;

    /** The col. */
    private int                           col;

    //---------------------------------------------------------------

    /** The data name. */
    private String                        dataName;

    /** The data expr. */
    private String                        dataExpr;

    /** The type. */
    private String                        type;

    /** The is mandatory. */
    private boolean                       isMandatory;

    //---------------------------------------------------------------

    /** The available choices. */
    private String[]                      availableChoices;

    /** The pattern. */
    private String                        pattern;

    //---------------------------------------------------------------

    /** The styles. */
    private List<ExcelCellConditionStyle> styles = new ArrayList<>();

    //---------------------------------------------------------------

    /**
     * Gets the row.
     *
     * @return the row
     */
    public int getRow(){
        return row;
    }

    /**
     * Sets the row.
     *
     * @param row
     *            the new row
     */
    public void setRow(int row){
        this.row = row;
    }

    /**
     * Gets the col.
     *
     * @return the col
     */
    public int getCol(){
        return col;
    }

    /**
     * Sets the col.
     *
     * @param col
     *            the new col
     */
    public void setCol(int col){
        this.col = col;
    }

    /**
     * Gets the data name.
     *
     * @return the data name
     */
    public String getDataName(){
        return dataName;
    }

    /**
     * Sets the data name.
     *
     * @param dataName
     *            the new data name
     */
    public void setDataName(String dataName){
        this.dataName = dataName;
    }

    /**
     * Checks if is mandatory.
     *
     * @return true, if is mandatory
     */
    public boolean isMandatory(){
        return isMandatory;
    }

    /**
     * Sets the mandatory.
     *
     * @param isMandatory
     *            the new mandatory
     */
    public void setMandatory(boolean isMandatory){
        this.isMandatory = isMandatory;
    }

    /**
     * Gets the available choices.
     *
     * @return the available choices
     */
    public String[] getAvailableChoices(){
        return availableChoices;
    }

    /**
     * Sets the available choices.
     *
     * @param availableChoices
     *            the new available choices
     */
    public void setAvailableChoices(String[] availableChoices){
        this.availableChoices = availableChoices;
    }

    /**
     * Gets the pattern.
     *
     * @return the pattern
     */
    public String getPattern(){
        return pattern;
    }

    /**
     * Sets the pattern.
     *
     * @param pattern
     *            the new pattern
     */
    public void setPattern(String pattern){
        this.pattern = pattern;
    }

    //---------------------------------------------------------------

    /**
     * Sets the choice string.
     *
     * @param choiceString
     *            the new choice string
     */
    public void setChoiceString(String choiceString){
        if (choiceString != null && choiceString.length() > 0){
            setAvailableChoices(choiceString.split(","));
        }
    }

    /**
     * Gets the cell index.
     *
     * @return the cell index
     */
    public String getCellIndex(){
        return getCellRef(row, col);
    }

    /**
     * Sets the cell index.
     *
     * @param cellIndex
     *            the new cell index
     */
    public void setCellIndex(String cellIndex){
        int[] value = CellReferenceUtil.getCellPosition(cellIndex);
        this.row = value[0];
        this.col = value[1];
    }

    /**
     * Gets the styles.
     *
     * @return the styles
     */
    public List<ExcelCellConditionStyle> getStyles(){
        return styles;
    }

    /**
     * Sets the styles.
     *
     * @param styles
     *            the new styles
     */
    public void setStyles(List<ExcelCellConditionStyle> styles){
        this.styles = styles;
    }

    /**
     * 添加 style.
     *
     * @param style
     *            the style
     */
    public void addStyle(ExcelCellConditionStyle style){
        this.styles.add(style);
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType(){
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Gets the data expr.
     *
     * @return the data expr
     */
    public String getDataExpr(){
        return dataExpr;
    }

    /**
     * Sets the data expr.
     *
     * @param dataExpr
     *            the new data expr
     */
    public void setDataExpr(String dataExpr){
        if (dataExpr == null || dataExpr.trim().length() == 0){
            this.dataExpr = null;
        }else{
            this.dataExpr = dataExpr;
        }
    }
}
