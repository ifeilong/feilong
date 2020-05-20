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

import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.definition.ExcelCellConditionStyle;
import com.feilong.lib.loxia.util.OgnlStack;

class BlockSimpleWriter{

    /** Don't let anyone instantiate this class. */
    private BlockSimpleWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    static void write(Sheet sheet,ExcelBlock blockDefinition,OgnlStack stack,Map<String, CellStyle> styleMap){
        //block style
        if (isNotNullOrEmpty(styleMap)){
            for (ExcelCellConditionStyle excelCellConditionStyle : blockDefinition.getStyles()){
                Object obj = stack.getValue(excelCellConditionStyle.getCondition());
                if (obj == null || !(obj instanceof Boolean)){
                    continue;
                }
                if (((Boolean) obj).booleanValue()){
                    int startRow = excelCellConditionStyle.getStartRow();
                    int endRow = excelCellConditionStyle.getEndRow();
                    int startCol = excelCellConditionStyle.getStartCol();
                    int endCol = excelCellConditionStyle.getEndCol();
                    String cellIndex = excelCellConditionStyle.getCellIndex();
                    BlockStyleSetter.set(sheet, startRow, endRow, startCol, endCol, cellIndex, styleMap);
                }
            }
        }

        //---------------------------------------------------------------
        for (ExcelCell excelCell : blockDefinition.getCells()){
            String dataExpr = excelCell.getDataExpr();
            int row = excelCell.getRow();
            String dataName = excelCell.getDataName();
            int col = excelCell.getCol();
            CellValueSetter.set(sheet, row, col, dataExpr == null ? dataName : dataExpr, stack);

            //---------------------------------------------------------------
            if (isNotNullOrEmpty(styleMap)){
                for (ExcelCellConditionStyle excelCellConditionStyle : excelCell.getStyles()){
                    Object obj = stack.getValue(excelCellConditionStyle.getCondition());
                    if (obj == null || !(obj instanceof Boolean)){
                        continue;
                    }
                    if (((Boolean) obj).booleanValue()){
                        CellStyleSetter.set(sheet, row, col, styleMap.get(excelCellConditionStyle.getCellIndex()));
                    }
                }
            }
        }
    }

}
