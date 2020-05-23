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
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.definition.ExcelCellConditionStyle;
import com.feilong.lib.ognl.OgnlStack;

class RowWriter{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RowWriter.class);

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private RowWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    static void write(
                    Sheet sheet,
                    ExcelBlock excelBlock,
                    int rowOffset,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap,
                    OgnlStack ognlStack){
        if (rowOffset > 0){
            int startRow = excelBlock.getStartRow();
            int startCol = excelBlock.getStartCol();
            int endRow = excelBlock.getEndRow();
            int endCol = excelBlock.getEndCol();
            BlockCopyer.copy(sheet, startRow, startCol, endRow, endCol, rowOffset, 0, mergedRegions);
        }
        //---------------------------------------------------------------
        if (isNotNullOrEmpty(styleMap)){
            setStyle(sheet, excelBlock, rowOffset, styleMap, ognlStack);
        }

        //---------------------------------------------------------------
        writeCells(sheet, excelBlock, rowOffset, styleMap, ognlStack);

        //---------------------------------------------------------------
        ExcelBlock childBlock = excelBlock.getChildBlock();
        if (childBlock != null){
            writeChildBlock(sheet, childBlock, rowOffset, mergedRegions, styleMap, ognlStack);
        }
    }

    private static void writeCells(Sheet sheet,ExcelBlock excelBlock,int rowOffset,Map<String, CellStyle> styleMap,OgnlStack ognlStack){
        Date beginDate = new Date();

        int rowIndex = 0;
        for (ExcelCell excelCell : excelBlock.getCells()){
            String dataName = buildDataName(rowOffset, excelCell);

            rowIndex = excelCell.getRow() + rowOffset;
            int col = excelCell.getCol();
            CellValueSetter.set(sheet, rowIndex, col, dataName, ognlStack);

            setCellStyle(sheet, styleMap, ognlStack, excelCell, rowIndex, col);
        }
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("write row:[{}] over,rowOffset:[{}],use time: [{}]", rowIndex, rowOffset, formatDuration(beginDate));
        }
    }

    private static void setCellStyle(
                    Sheet sheet,
                    Map<String, CellStyle> styleMap,
                    OgnlStack ognlStack,
                    ExcelCell excelCell,
                    int rowIndex,
                    int col){
        if (isNullOrEmpty(styleMap)){
            return;
        }
        for (ExcelCellConditionStyle style : excelCell.getStyles()){
            Object obj = ognlStack.getValue(style.getCondition());
            if (obj == null || !(obj instanceof Boolean)){
                continue;
            }
            if (((Boolean) obj).booleanValue()){
                CellStyleSetter.set(sheet, rowIndex, col, styleMap.get(style.getCellIndex()));
            }
        }
    }

    private static String buildDataName(int rowOffset,ExcelCell excelCell){
        String dataName = defaultIfNullOrEmpty(excelCell.getDataExpr(), excelCell.getDataName());
        if (dataName.startsWith("=")){
            return FormulaEvaluatorUtil.offsetFormula(dataName, rowOffset, 0);
        }
        return dataName;
    }

    private static void setStyle(Sheet sheet,ExcelBlock excelBlock,int rowOffset,Map<String, CellStyle> styleMap,OgnlStack ognlStack){
        for (ExcelCellConditionStyle style : excelBlock.getStyles()){
            Object obj = ognlStack.getValue(style.getCondition());
            if (obj == null || !(obj instanceof Boolean)){
                continue;
            }
            if (((Boolean) obj).booleanValue()){
                int startRow = style.getStartRow();
                int endRow = style.getEndRow();
                int startCol = style.getStartCol();
                int endCol = style.getEndCol();
                String cellIndex = style.getCellIndex();
                BlockStyleSetter.set(sheet, startRow + rowOffset, endRow + rowOffset, startCol, endCol, cellIndex, styleMap);
            }
        }
    }

    private static void writeChildBlock(
                    Sheet sheet,
                    ExcelBlock childBlock,
                    int rowOffset,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap,
                    OgnlStack ognlStack){
        Object colValue = ognlStack.getValue(childBlock.getDataName());
        if (colValue == null){
            return;
        }

        //---------------------------------------------------------------
        Collection<?> listValue = DataToCollectionUtil.convert(colValue);
        List<CellRangeAddress> childMergedRegions = null;
        int startCol = childBlock.getStartCol();
        int endCol = childBlock.getEndCol();
        if (mergedRegions != null){
            childMergedRegions = new ArrayList<>();
            for (CellRangeAddress cellRangeAddress : mergedRegions){
                int firstRow = cellRangeAddress.getFirstRow();
                int firstColumn = cellRangeAddress.getFirstColumn();
                int lastRow = cellRangeAddress.getLastRow();
                int lastColumn = cellRangeAddress.getLastColumn();
                int startRow = childBlock.getStartRow();
                int endRow = childBlock.getEndRow();
                if (firstRow >= startRow && firstColumn >= startCol && lastRow <= endRow && lastColumn <= endCol){
                    childMergedRegions.add(cellRangeAddress);
                }
            }
        }
        int colStep = 0;
        Object preObj = null;
        for (Object obj : listValue){
            ognlStack.push(obj);
            ognlStack.addContext("preColumn", preObj);
            ognlStack.addContext("columnNum", colStep);

            int colOffset = colStep * (endCol - startCol + 1);
            ColumnWriter.write(sheet, childBlock, ognlStack, rowOffset, colOffset, childMergedRegions, styleMap);
            colStep++;
            preObj = ognlStack.pop();
        }
        ognlStack.removeContext("preColumn");
        ognlStack.removeContext("columnNum");
    }

}
