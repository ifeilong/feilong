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

import java.util.ArrayList;
import java.util.List;

import com.feilong.office.excel.utils.CellReferenceUtil;

/**
 * The Class ExcelBlock.
 */
public class ExcelBlock implements Comparable<ExcelBlock>{

    /** The Constant LOOP_DIRECTION_HORIZONAL. */
    public static final String            LOOP_DIRECTION_HORIZONAL = "horizontal";

    /** The Constant LOOP_DIRECTION_VERTICAL. */
    public static final String            LOOP_DIRECTION_VERTICAL  = "vertical";

    //---------------------------------------------------------------

    /** The start row. */
    private int                           startRow                 = 0;

    /** The start col. */
    private int                           startCol                 = 0;

    /** The end row. */
    private int                           endRow                   = 0;

    /** The end col. */
    private int                           endCol                   = 0;

    //---------------------------------------------------------------

    /** The data name. */
    private String                        dataName;

    /** The is loop. */
    private boolean                       isLoop                   = false;

    /** The direction. */
    private String                        direction                = LOOP_DIRECTION_HORIZONAL;

    /** The break condition. */
    private LoopBreakCondition            breakCondition;

    //---------------------------------------------------------------

    /** The loop class. */
    private Class<? extends Object>       loopClass;

    /** The cells. */
    private List<ExcelCell>               cells                    = new ArrayList<>();

    /** The styles. */
    private List<ExcelCellConditionStyle> styles                   = new ArrayList<>();

    /** The is child block. */
    private boolean                       isChildBlock             = false;

    //---------------------------------------------------------------

    /** The child block. */
    private ExcelBlock                    childBlock;

    //---------------------------------------------------------------

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

    //---------------------------------------------------------------

    /**
     * Checks if is loop.
     *
     * @return true, if is loop
     */
    public boolean isLoop(){
        return isLoop;
    }

    /**
     * Sets the loop.
     *
     * @param isLoop
     *            the new loop
     */
    public void setLoop(boolean isLoop){
        this.isLoop = isLoop;
    }

    //---------------------------------------------------------------

    /**
     * Gets the direction.
     *
     * @return the direction
     */
    public String getDirection(){
        return direction;
    }

    /**
     * Sets the direction.
     *
     * @param direction
     *            the new direction
     */
    public void setDirection(String direction){
        this.direction = direction;
    }

    //---------------------------------------------------------------

    /**
     * 获得 loop class.
     *
     * @return the loop class
     */
    public Class<? extends Object> getLoopClass(){
        return loopClass;
    }

    /**
     * Sets the loop class.
     *
     * @param loopClass
     *            the new loop class
     */
    public void setLoopClass(Class<? extends Object> loopClass){
        this.loopClass = loopClass;
    }

    //    /**
    //     * Gets the cell.
    //     *
    //     * @param position
    //     *            the position
    //     * @return the cell
    //     */
    //    public ExcelCell getCell(String position){
    //        if (cells.size() == 0){
    //            return null;
    //        }
    //        for (ExcelCell cell : cells){
    //            if (ExcelUtil.getCellIndex(cell.getRow(), cell.getCol()).equalsIgnoreCase(position.trim())){
    //                return cell;
    //            }
    //        }
    //        return null;
    //    }

    /**
     * Gets the cells.
     *
     * @return the cells
     */
    public List<ExcelCell> getCells(){
        return cells;
    }

    /**
     * Sets the cells.
     *
     * @param cells
     *            the new cells
     */
    public void setCells(List<ExcelCell> cells){
        this.cells = cells;
    }

    /**
     * 添加 cell.
     *
     * @param cell
     *            the cell
     */
    public void addCell(ExcelCell cell){
        this.cells.add(cell);
    }

    /**
     * Gets the child block.
     *
     * @return the child block
     */
    public ExcelBlock getChildBlock(){
        return childBlock;
    }

    /**
     * Sets the child block.
     *
     * @param childBlock
     *            the new child block
     */
    public void setChildBlock(ExcelBlock childBlock){
        if (childBlock == null){
            this.childBlock = null;
            return;
        }

        //---------------------------------------------------------------
        childBlock.setLoop(true);
        childBlock.setEndCellIndex(this.getEndCellIndex());
        this.childBlock = childBlock;
    }

    /**
     * Gets the break condition.
     *
     * @return the break condition
     */
    public LoopBreakCondition getBreakCondition(){
        return breakCondition;
    }

    /**
     * Sets the break condition.
     *
     * @param breakCondition
     *            the new break condition
     */
    public void setBreakCondition(LoopBreakCondition breakCondition){
        this.breakCondition = breakCondition;
    }

    /**
     * Checks if is child.
     *
     * @return true, if is child
     */
    public boolean isChild(){
        return isChildBlock;
    }

    /**
     * Sets the child.
     *
     * @param isChildBlock
     *            the new child
     */
    public void setChild(boolean isChildBlock){
        this.isChildBlock = isChildBlock;
    }

    /**
     * Sets the loop class by class name.
     *
     * @param className
     *            the new loop class by class name
     */
    public void setLoopClassByClassName(String className){
        try{
            setLoopClass(Class.forName(className));
        }catch (ClassNotFoundException e){
            throw new RuntimeException(className + " is not found.");
        }
    }

    //---------------------------------------------------------------

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString(){
        return "ExcelBlock[" + CellReferenceUtil.getCellRef(startRow, startCol) + ":" + getEndCellIndex() + "]";
    }

    /**
     * Equals.
     *
     * @param obj
     *            the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj){
        if (obj == null){
            return false;
        }
        if (this == obj){
            return true;
        }
        if (!(obj instanceof ExcelBlock)){
            return false;
        }
        ExcelBlock eb = (ExcelBlock) obj;
        return this.toString().equals(eb.toString());
    }

    //---------------------------------------------------------------

    /**
     * Compare to.
     *
     * @param excelBlock
     *            the eb
     * @return the int
     */
    @Override
    public int compareTo(ExcelBlock excelBlock){
        int result = excelBlock.getStartRow() - this.getStartRow();
        if (result == 0){
            return excelBlock.getStartCol() - this.getStartCol();
        }
        return result;
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

}
