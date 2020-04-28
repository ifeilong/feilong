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

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelCell;
import com.feilong.office.excel.definition.ExcelCellConditionStyle;
import com.feilong.office.excel.utils.OgnlStack;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
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
                    ExcelBlock blockDefinition,
                    OgnlStack stack,
                    int rowOffset,
                    int colOffset,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap) throws Exception{
        if (rowOffset > 0 || colOffset > 0){
            BlockCopyer.copy(
                            sheet,
                            blockDefinition.getStartRow(),
                            blockDefinition.getStartCol(),
                            blockDefinition.getEndRow(),
                            blockDefinition.getEndCol(),
                            rowOffset,
                            colOffset,
                            mergedRegions);
        }
        if (styleMap.keySet().size() > 0){
            for (ExcelCellConditionStyle style : blockDefinition.getStyles()){
                Object obj = stack.getValue(style.getCondition());
                if (obj == null || !(obj instanceof Boolean)){
                    continue;
                }
                if (((Boolean) obj).booleanValue()){
                    BlockStyleSetter.set(
                                    sheet,
                                    style.getStartRow() + rowOffset,
                                    style.getEndRow() + rowOffset,
                                    style.getStartCol() + colOffset,
                                    style.getEndCol() + colOffset,
                                    style.getCellIndex(),
                                    styleMap);
                }
            }
        }
        for (ExcelCell excelCell : blockDefinition.getCells()){
            String dataExpr = excelCell.getDataExpr();
            String dataName = dataExpr == null ? excelCell.getDataName() : dataExpr;
            if (dataName.startsWith("=")){
                dataName = ExcelUtil.offsetFormula(dataName, rowOffset, colOffset);
            }
            int row = excelCell.getRow();
            int col = excelCell.getCol();

            CellValueSetter.set(sheet, row + rowOffset, col + colOffset, dataName, stack);
            if (styleMap.keySet().size() > 0){
                for (ExcelCellConditionStyle excelCellConditionStyle : excelCell.getStyles()){
                    Object obj = stack.getValue(excelCellConditionStyle.getCondition());
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
