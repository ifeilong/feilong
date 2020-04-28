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
import java.util.Collections;
import java.util.List;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.office.excel.CellReferenceUtil;

/**
 * The Class ExcelSheet.
 */
public class ExcelSheet{

    /** The name. */
    private String           name;

    /** The display name. */
    private String           displayName;

    /** The excel blocks. */
    private List<ExcelBlock> excelBlocks = new ArrayList<>();

    /** The is ordered. */
    private Boolean          isOrdered   = false;

    //---------------------------------------------------------------

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName(){
        return displayName;
    }

    /**
     * Sets the display name.
     *
     * @param displayName
     *            the new display name
     */
    public void setDisplayName(String displayName){
        this.displayName = displayName;
    }

    /**
     * Gets the excel blocks.
     *
     * @return the excel blocks
     */
    public List<ExcelBlock> getExcelBlocks(){
        return excelBlocks;
    }

    /**
     * Sets the excel blocks.
     *
     * @param excelBlocks
     *            the new excel blocks
     */
    public void setExcelBlocks(List<ExcelBlock> excelBlocks){
        synchronized (isOrdered){
            this.excelBlocks = excelBlocks;
            isOrdered = false;
        }
    }

    /**
     * Gets the excel block.
     *
     * @param start
     *            the start
     * @param end
     *            the end
     * @return the excel block
     */
    public ExcelBlock getExcelBlock(String start,String end){
        if (excelBlocks.size() == 0){
            return null;
        }
        for (ExcelBlock excelBlock : excelBlocks){
            if (CellReferenceUtil.getCellIndex(excelBlock.getStartRow(), excelBlock.getStartCol()).equalsIgnoreCase(start.trim())
                            && CellReferenceUtil.getCellIndex(excelBlock.getEndRow(), excelBlock.getEndCol()).equalsIgnoreCase(end.trim())){
                return excelBlock;
            }
        }
        return null;
    }

    /**
     * Gets the excel block.
     *
     * @return the excel block
     */
    public ExcelBlock getExcelBlock(){
        if (excelBlocks.size() == 0){
            return null;
        }
        return excelBlocks.iterator().next();
    }

    /**
     * Sets the excel block.
     *
     * @param excelBlock
     *            the new excel block
     */
    public void setExcelBlock(ExcelBlock excelBlock){
        synchronized (isOrdered){
            excelBlocks.clear();
            excelBlocks.add(excelBlock);
            isOrdered = false;
        }
    }

    /**
     * 添加 excel block.
     *
     * @param excelBlock
     *            the excel block
     */
    public void addExcelBlock(ExcelBlock excelBlock){
        synchronized (isOrdered){
            excelBlocks.add(excelBlock);
            isOrdered = false;
        }
    }

    /**
     * Gets the sorted excel blocks.
     *
     * @return the sorted excel blocks
     */
    public List<ExcelBlock> getSortedExcelBlocks(){
        synchronized (isOrdered){
            if (!isOrdered){
                Collections.sort(excelBlocks);
                isOrdered = true;
            }
        }
        return excelBlocks;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString(){
        return "ExcelSheet [name=" + name + ", excelBlocks=\r\n\t" + ConvertUtil.toString(excelBlocks, "\r\n\t") + "]";
    }
}
