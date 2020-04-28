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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelCell;
import com.feilong.office.excel.definition.ExcelCellConditionStyle;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class ExcelBlockClone{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelBlockClone.class);

    /** Don't let anyone instantiate this class. */
    private ExcelBlockClone(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Clone block.
     *
     * @return the excel block
     */
    static ExcelBlock cloneBlock(ExcelBlock aexcelBlock){
        if (null == aexcelBlock){
            return null;
        }
        //---------------------------------------------------------------
        ExcelBlock excelBlock = new ExcelBlock();
        excelBlock.setBreakCondition(LoopBreakConditionClone.clone(aexcelBlock.getBreakCondition()));
        excelBlock.setChild(aexcelBlock.isChild());
        excelBlock.setDataName(aexcelBlock.getDataName());
        excelBlock.setEndCol(aexcelBlock.getEndCol());
        excelBlock.setEndRow(aexcelBlock.getEndRow());
        excelBlock.setLoop(aexcelBlock.isLoop());
        excelBlock.setDirection(aexcelBlock.getDirection());
        excelBlock.setLoopClass(aexcelBlock.getLoopClass());
        excelBlock.setStartCol(aexcelBlock.getStartCol());
        excelBlock.setStartRow(aexcelBlock.getStartRow());

        //---------------------------------------------------------------
        for (ExcelCellConditionStyle excelCellConditionStyle : aexcelBlock.getStyles()){
            excelBlock.addStyle(excelCellConditionStyle.cloneStyle());
        }
        for (ExcelCell excelCell : aexcelBlock.getCells()){
            excelBlock.addCell(excelCell.cloneCell());
        }
        excelBlock.setChildBlock(cloneBlock(aexcelBlock.getChildBlock()));
        return excelBlock;
    }
}
