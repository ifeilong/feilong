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
package com.feilong.office.excel.definition;

import com.feilong.office.excel.utils.CellReferenceUtil;

/**
 * The Class ExcelCellConditionStyle.
 */
public class ExcelCellConditionStyle{

    /** The start row. */
    private int    startRow = 0;

    /** The start col. */
    private int    startCol = 0;

    /** The end row. */
    private int    endRow   = 0;

    /** The end col. */
    private int    endCol   = 0;

    //---------------------------------------------------------------

    /** The condition. */
    private String condition;

    /** The cell index. */
    private String cellIndex;

    //---------------------------------------------------------------

    /**
     * Gets the condition.
     *
     * @return the condition
     */
    public String getCondition(){
        return condition;
    }

    /**
     * Sets the condition.
     *
     * @param condition
     *            the new condition
     */
    public void setCondition(String condition){
        this.condition = condition;
    }

    /**
     * Gets the cell index.
     *
     * @return the cell index
     */
    public String getCellIndex(){
        return cellIndex;
    }

    /**
     * Sets the cell index.
     *
     * @param cellIndex
     *            the new cell index
     */
    public void setCellIndex(String cellIndex){
        this.cellIndex = cellIndex;
    }

    /**
     * Gets the start row.
     *
     * @return the start row
     */
    public int getStartRow(){
        return startRow;
    }

    /**
     * Sets the start row.
     *
     * @param startRow
     *            the new start row
     */
    public void setStartRow(int startRow){
        this.startRow = startRow;
    }

    /**
     * Gets the start col.
     *
     * @return the start col
     */
    public int getStartCol(){
        return startCol;
    }

    /**
     * Sets the start col.
     *
     * @param startCol
     *            the new start col
     */
    public void setStartCol(int startCol){
        this.startCol = startCol;
    }

    /**
     * Gets the end row.
     *
     * @return the end row
     */
    public int getEndRow(){
        return endRow;
    }

    /**
     * Sets the end row.
     *
     * @param endRow
     *            the new end row
     */
    public void setEndRow(int endRow){
        this.endRow = endRow;
    }

    /**
     * Gets the end col.
     *
     * @return the end col
     */
    public int getEndCol(){
        return endCol;
    }

    /**
     * Sets the end col.
     *
     * @param endCol
     *            the new end col
     */
    public void setEndCol(int endCol){
        this.endCol = endCol;
    }

    /**
     * Gets the start cell index.
     *
     * @return the start cell index
     */
    public String getStartCellIndex(){
        return CellReferenceUtil.getCellRef(startRow, startCol);
    }

    /**
     * Sets the start cell index.
     *
     * @param startCellIndex
     *            the new start cell index
     */
    public void setStartCellIndex(String startCellIndex){
        int[] value = CellReferenceUtil.getCellPosition(startCellIndex);
        this.startRow = value[0];
        this.startCol = value[1];
    }

    /**
     * Gets the end cell index.
     *
     * @return the end cell index
     */
    public String getEndCellIndex(){
        return CellReferenceUtil.getCellRef(endRow, endCol);
    }

    /**
     * Sets the end cell index.
     *
     * @param endCellIndex
     *            the new end cell index
     */
    public void setEndCellIndex(String endCellIndex){
        int[] value = CellReferenceUtil.getCellPosition(endCellIndex);
        this.endRow = value[0];
        this.endCol = value[1];
    }

    /**
     * Clone style.
     *
     * @return the excel cell condition style
     */
    public ExcelCellConditionStyle cloneStyle(){
        ExcelCellConditionStyle style = new ExcelCellConditionStyle();
        style.setCellIndex(cellIndex);
        style.setCondition(condition);
        style.setEndCol(endCol);
        style.setEndRow(endRow);
        style.setStartCol(startCol);
        style.setStartRow(startRow);
        return style;
    }
}
