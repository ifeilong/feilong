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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

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
            int startRow = excelBlock.getStartRow();
            int startCol = excelBlock.getStartCol();
            int endRow = excelBlock.getEndRow();
            int endCol = excelBlock.getEndCol();
            BlockCopyer.copy(sheet, startRow, startCol, endRow, endCol, rowOffset, colOffset, mergedRegions);
        }

        //---------------------------------------------------------------
        if (isNotNullOrEmpty(styleMap)){
            for (ExcelCellConditionStyle style : excelBlock.getStyles()){
                Object obj = ognlStack.getValue(style.getCondition());
                if (obj == null || !(obj instanceof Boolean)){
                    continue;
                }

                //---------------------------------------------------------------
                if (((Boolean) obj).booleanValue()){
                    int startRowIndex = style.getStartRow() + rowOffset;
                    int endRowIndex = style.getEndRow() + rowOffset;
                    int startColumnIndex = style.getStartCol() + colOffset;
                    int endColumnIndex = style.getEndCol() + colOffset;
                    String cellIndex = style.getCellIndex();
                    BlockStyleSetter.set(sheet, startRowIndex, endRowIndex, startColumnIndex, endColumnIndex, cellIndex, styleMap);
                }
            }
        }

        //---------------------------------------------------------------
        for (ExcelCell excelCell : excelBlock.getCells()){
            String dataName = buildDataName(rowOffset, colOffset, excelCell);
            int row = excelCell.getRow();
            int col = excelCell.getCol();

            //---------------------------------------------------------------

            CellValueSetter.set(sheet, row + rowOffset, col + colOffset, dataName, ognlStack);
            if (isNotNullOrEmpty(styleMap)){
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

    private static String buildDataName(int rowOffset,int colOffset,ExcelCell excelCell){
        String dataName = defaultIfNullOrEmpty(excelCell.getDataExpr(), excelCell.getDataName());
        if (dataName.startsWith("=")){
            return FormulaEvaluatorUtil.offsetFormula(dataName, rowOffset, colOffset);
        }
        return dataName;
    }
}
