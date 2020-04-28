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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class BlockCopyer{

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockCopyer.class);

    /** Don't let anyone instantiate this class. */
    private BlockCopyer(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static void copy(
                    Sheet sheet,
                    int startRow,
                    int startCol,
                    int endRow,
                    int endCol,

                    int rowOffset,
                    int colOffset,
                    List<CellRangeAddress> mergedRegions){
        for (int row = startRow; row <= endRow; row++){
            Row oldRow = sheet.getRow(row);
            if (oldRow == null){
                continue;
            }
            Row newRow = sheet.getRow(row + rowOffset);
            if (newRow == null){
                newRow = sheet.createRow(row + rowOffset);
            }
            if (oldRow.getHeight() >= 0){
                newRow.setHeight(oldRow.getHeight());
            }
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("copy row [{}] to [{}],Set row height :{}", row, row + rowOffset, newRow.getHeightInPoints());
            }
            for (int col = startCol; col <= endCol; col++){
                Cell oldCell = oldRow.getCell(col);
                if (oldCell == null){
                    continue;
                }
                Cell newCell = newRow.getCell(col + colOffset);
                if (newCell == null){
                    newCell = newRow.createCell(col + colOffset);
                }
                CellCoper.copy(oldCell, newCell, rowOffset, colOffset);
            }
        }
        for (int col = startCol; col <= endCol; col++){
            if (sheet.getColumnWidth(col) >= 0){
                sheet.setColumnWidth(col + colOffset, sheet.getColumnWidth(col));
            }
        }
        if (mergedRegions != null){
            for (CellRangeAddress cellRangeAddress : mergedRegions){
                CellRangeAddress craNew = new CellRangeAddress(
                                cellRangeAddress.getFirstRow() + rowOffset,
                                cellRangeAddress.getLastRow() + rowOffset,
                                cellRangeAddress.getFirstColumn() + colOffset,
                                cellRangeAddress.getLastColumn() + colOffset);
                sheet.addMergedRegion(craNew);
            }
        }
    }
}
