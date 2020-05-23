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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.lib.ognl.OgnlStack;

@SuppressWarnings("squid:S1192") //String literals should not be duplicated
class BlockLoopHorizontalWriter{

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockLoopHorizontalWriter.class);

    /** Don't let anyone instantiate this class. */
    private BlockLoopHorizontalWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static void write(
                    Sheet sheet,
                    ExcelBlock excelBlock,
                    OgnlStack ognlStack,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap){
        Object value = ognlStack.getValue(excelBlock.getDataName());

        int startRow = excelBlock.getStartRow();
        int endRow = excelBlock.getEndRow();

        if (value == null){
            //if no data, just remove the dummy data
            for (int i = endRow; i >= startRow; i--){
                sheet.removeRow(sheet.getRow(i));
            }
            return;
        }
        //---------------------------------------------------------------
        Collection<?> listValue = DataToCollectionUtil.convert(value);
        //---------------------------------------------------------------

        writeData(sheet, excelBlock, ognlStack, mergedRegions, styleMap, listValue);
        removeMergedRegion(sheet, excelBlock, startRow, endRow);

        //---------------------------------------------------------------
        //[2013-2-22]if with data, still need to remove dummy one, otherwise xlsx will have error if there are formulas in block.
        for (int i = excelBlock.getEndRow(); i >= excelBlock.getStartRow(); i--){
            Row row = sheet.getRow(i);
            sheet.removeRow(row);
        }

        //---------------------------------------------------------------
        if (listValue.size() > 0){
            sheet.shiftRows(endRow + 1, sheet.getLastRowNum(), startRow - endRow - 1, true, false);
        }
    }

    private static void writeData(
                    Sheet sheet,
                    ExcelBlock excelBlock,
                    OgnlStack ognlStack,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap,
                    Collection<?> listValue){
        int startRow = excelBlock.getStartRow();
        int endRow = excelBlock.getEndRow();

        Object preObj = null;

        int lastRowNum = sheet.getLastRowNum();
        int step = 1;
        for (Object obj : listValue){
            ognlStack.push(obj);
            ognlStack.addContext("preLine", preObj);
            ognlStack.addContext("lineNum", step - 1);

            //shiftrow and prepare write new row
            int rowOffset = step * (endRow - startRow + 1);
            //---------------------------------------------------------------
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("startRow:[{}],endRow:[{}],step:[{}],rowOffset:[{}]", startRow, endRow, step, rowOffset);
            }
            //---------------------------------------------------------------
            int nextStartRow = startRow + rowOffset;
            if (nextStartRow <= lastRowNum){
                sheet.shiftRows(nextStartRow, lastRowNum, endRow - startRow + 1, true, false);
            }
            //---------------------------------------------------------------
            RowWriter.write(sheet, excelBlock, rowOffset, mergedRegions, styleMap, ognlStack);
            //---------------------------------------------------------------
            step++;
            preObj = ognlStack.pop();
        }

        //---------------------------------------------------------------
        ognlStack.removeContext("preLine");
        ognlStack.removeContext("lineNum");
    }

    private static void removeMergedRegion(Sheet sheet,ExcelBlock excelBlock,int startRow,int endRow){
        //if no data, just remove the dummy data        
        //[2013-2-26]it seems that we also need to remove all merged regions for shift      
        for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--){
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            int lastRow = cellRangeAddress.getLastRow();
            int firstRow = cellRangeAddress.getFirstRow();
            int firstColumn = cellRangeAddress.getFirstColumn();
            int lastColumn = cellRangeAddress.getLastColumn();

            int startCol = excelBlock.getStartCol();
            int endCol = excelBlock.getEndCol();
            //---------------------------------------------------------------
            if (firstRow >= startRow && firstColumn >= startCol && lastRow <= endRow && lastColumn <= endCol){
                sheet.removeMergedRegion(i);

                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug("Removed Merged Region:[{}-{}]", getCellRef(firstRow, firstColumn), getCellRef(lastRow, lastColumn));
                }
            }
        }
    }

}
