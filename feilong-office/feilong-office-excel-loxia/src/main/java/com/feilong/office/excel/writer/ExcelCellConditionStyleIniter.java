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
package com.feilong.office.excel.writer;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelCell;
import com.feilong.office.excel.definition.ExcelCellConditionStyle;
import com.feilong.office.excel.definition.ExcelSheet;
import com.feilong.office.excel.utils.CellReferenceUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class ExcelCellConditionStyleIniter{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelCellConditionStyleIniter.class);

    /** Don't let anyone instantiate this class. */
    private ExcelCellConditionStyleIniter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * Inits the conditional style.
     *
     * @param sheet
     *            the style sheet
     * @param excelSheet
     *            the sheet definition
     * @param styleMap
     *            the style map
     */
    static void init(Sheet sheet,ExcelSheet excelSheet,Map<String, CellStyle> styleMap){
        for (ExcelBlock excelBlock : excelSheet.getExcelBlocks()){
            init(sheet, excelBlock, styleMap);

            if (excelBlock.getChildBlock() != null){
                init(sheet, excelBlock.getChildBlock(), styleMap);
            }
        }
    }

    /**
     * Inits the conditional style.
     *
     * @param sheet
     *            the style sheet
     * @param excelBlock
     *            the block definition
     * @param styleMap
     *            the style map
     */
    private static void init(Sheet sheet,ExcelBlock excelBlock,Map<String, CellStyle> styleMap){
        for (ExcelCellConditionStyle excelCellConditionStyle : excelBlock.getStyles()){
            init(sheet, excelCellConditionStyle, styleMap);
        }
        for (ExcelCell excelCell : excelBlock.getCells()){
            for (ExcelCellConditionStyle excelCellConditionStyle : excelCell.getStyles()){
                init(sheet, excelCellConditionStyle, styleMap);
            }
        }
    }

    /**
     * Inits the conditional style.
     *
     * @param sheet
     *            the style sheet
     * @param excelCellConditionStyle
     *            the style
     * @param styleMap
     *            the style map
     */
    private static void init(Sheet sheet,ExcelCellConditionStyle excelCellConditionStyle,Map<String, CellStyle> styleMap){
        //ignore existed style
        int startRow = excelCellConditionStyle.getStartRow();
        int endRow = excelCellConditionStyle.getEndRow();
        int startCol = excelCellConditionStyle.getStartCol();
        String cellIndex = excelCellConditionStyle.getCellIndex();
        int endCol = excelCellConditionStyle.getEndCol();

        if (startRow == 0 && endRow == 0 && startCol == 0 && endCol == 0 && styleMap.containsKey(cellIndex)){
            return;
        }

        //---------------------------------------------------------------
        int[] position = CellReferenceUtil.getCellPosition(cellIndex);
        int rows = endRow - startRow;
        int cols = endCol - startCol;
        for (int i = position[0]; i <= position[0] + rows; i++){
            Row row = sheet.getRow(i);
            if (row == null){
                return;
            }
            for (int j = position[1]; j <= position[1] + cols; j++){
                Cell cell = row.getCell(j);
                if (cell == null){
                    return;
                }

                String cellIndex2 = CellReferenceUtil.getCellIndex(i, j);
                styleMap.put(cellIndex2, cell.getCellStyle());
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("Condition Style [{}]", cellIndex2);
                }
            }
        }
    }
}
