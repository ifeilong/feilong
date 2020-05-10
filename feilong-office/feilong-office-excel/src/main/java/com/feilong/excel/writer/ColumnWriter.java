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
package com.feilong.excel.writer;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.definition.ExcelCellConditionStyle;
import com.feilong.lib.loxia.util.OgnlStack;

class ColumnWriter{

    /** Don't let anyone instantiate this class. */
    private ColumnWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static void write(
                    Sheet sheet,
                    ExcelBlock excelBlock,
                    OgnlStack ognlStack,
                    int rowOffset,
                    int colOffset,

                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap){
        if (rowOffset > 0 || colOffset > 0){
            BlockCopyer.copy(
                            sheet,
                            excelBlock.getStartRow(),
                            excelBlock.getStartCol(),
                            excelBlock.getEndRow(),
                            excelBlock.getEndCol(),
                            rowOffset,
                            colOffset,
                            mergedRegions);
        }

        //---------------------------------------------------------------
        if (styleMap.keySet().size() > 0){
            for (ExcelCellConditionStyle excelCellConditionStyle : excelBlock.getStyles()){
                Object obj = ognlStack.getValue(excelCellConditionStyle.getCondition());
                if (obj == null || !(obj instanceof Boolean)){
                    continue;
                }

                //---------------------------------------------------------------
                if (((Boolean) obj).booleanValue()){
                    BlockStyleSetter.set(
                                    sheet,
                                    excelCellConditionStyle.getStartRow() + rowOffset,
                                    excelCellConditionStyle.getEndRow() + rowOffset,
                                    excelCellConditionStyle.getStartCol() + colOffset,
                                    excelCellConditionStyle.getEndCol() + colOffset,
                                    excelCellConditionStyle.getCellIndex(),
                                    styleMap);
                }
            }
        }

        //---------------------------------------------------------------
        for (ExcelCell excelCell : excelBlock.getCells()){
            String dataExpr = excelCell.getDataExpr();
            String dataName = dataExpr == null ? excelCell.getDataName() : dataExpr;
            if (dataName.startsWith("=")){
                dataName = FormulaEvaluatorUtil.offsetFormula(dataName, rowOffset, colOffset);
            }
            int row = excelCell.getRow();
            int col = excelCell.getCol();

            //---------------------------------------------------------------

            CellValueSetter.set(sheet, row + rowOffset, col + colOffset, dataName, ognlStack);
            if (styleMap.keySet().size() > 0){
                for (ExcelCellConditionStyle excelCellConditionStyle : excelCell.getStyles()){
                    Object obj = ognlStack.getValue(excelCellConditionStyle.getCondition());
                    if (obj == null || !(obj instanceof Boolean)){
                        continue;
                    }
                    if (((Boolean) obj).booleanValue()){
                        CellStyleSetter.set(sheet, row + rowOffset, col + colOffset, styleMap.get(excelCellConditionStyle.getCellIndex()));
                    }
                }
            }
        }
    }
}
