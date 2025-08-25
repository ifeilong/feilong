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

import static com.feilong.excel.util.CellReferenceUtil.getCellRef;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.definition.ExcelCellConditionStyle;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.util.CellReferenceUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ExcelCellConditionStyleInitializer{

    static Map<String, CellStyle> init(Sheet sheet,ExcelSheet excelSheet){
        Map<String, CellStyle> returnMap = new HashMap<>();

        List<ExcelBlock> excelBlocks = excelSheet.getExcelBlocks();
        for (ExcelBlock excelBlock : excelBlocks){
            init(sheet, excelBlock, returnMap);

            ExcelBlock childBlock = excelBlock.getChildBlock();
            if (childBlock != null){
                init(sheet, childBlock, returnMap);
            }
        }
        return returnMap;
    }

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

    //---------------------------------------------------------------

    private static void init(Sheet sheet,ExcelCellConditionStyle excelCellConditionStyle,Map<String, CellStyle> styleMap){
        //ignore existed style
        int startRow = excelCellConditionStyle.getStartRow();
        int endRow = excelCellConditionStyle.getEndRow();

        int startCol = excelCellConditionStyle.getStartCol();
        int endCol = excelCellConditionStyle.getEndCol();

        String cellIndex = excelCellConditionStyle.getCellIndex();

        if (startRow == 0 && endRow == 0 && startCol == 0 && endCol == 0 && styleMap.containsKey(cellIndex)){
            return;
        }

        //---------------------------------------------------------------
        int[] position = CellReferenceUtil.getCellPosition(cellIndex);
        int rows = endRow - startRow;
        int cols = endCol - startCol;

        //---------------------------------------------------------------
        for (int i = position[0]; i <= position[0] + rows; i++){
            Row row = sheet.getRow(i);
            if (row == null){
                return;
            }

            //---------------------------------------------------------------
            for (int j = position[1]; j <= position[1] + cols; j++){
                Cell cell = row.getCell(j);
                if (cell == null){
                    return;
                }

                String cellIndex2 = getCellRef(i, j);
                styleMap.put(cellIndex2, cell.getCellStyle());

                log.debug("Condition Style [{}]", cellIndex2);
            }
        }
    }
}
