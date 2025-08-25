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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class BlockCopyer{

    static void copy(
                    Sheet sheet,

                    int startRow,
                    int startCol,
                    int endRow,
                    int endCol,

                    int rowOffset,
                    int colOffset,
                    List<CellRangeAddress> mergedRegions){

        //---------------------------------------------------------------
        if (log.isTraceEnabled()){
            log.trace(
                            "will copy block,startRow:[{}],startCol:[{}],endRow:[{}],endCol:[{}],rowOffset:[{}],colOffset:[{}]",
                            startRow,
                            startCol,
                            endRow,
                            endCol,
                            rowOffset,
                            colOffset);
        }
        //---------------------------------------------------------------
        //will copy block,startRow:[3],startCol:[0],endRow:[3],endCol:[8],rowOffset:[23],colOffset:[0]
        for (int row = startRow; row <= endRow; row++){
            Row oldRow = sheet.getRow(row);
            if (oldRow == null){
                continue;
            }

            //---------------------------------------------------------------
            int newRowIndex = row + rowOffset;
            Row newRow = buildNewRow(sheet, oldRow, newRowIndex);
            if (log.isTraceEnabled()){
                log.trace("copy row [{}] to [{}],Set new row height :{}", row, newRowIndex, newRow.getHeightInPoints());
            }

            //---------------------------------------------------------------
            for (int col = startCol; col <= endCol; col++){
                Cell oldCell = oldRow.getCell(col);
                if (oldCell == null){
                    continue;
                }

                int newCellIndex = col + colOffset;
                Cell newCell = buildNewCell(newRow, newCellIndex);
                CellCoper.copy(oldCell, newCell, rowOffset, colOffset);
            }
        }

        //---------------------------------------------------------------
        for (int col = startCol; col <= endCol; col++){
            if (sheet.getColumnWidth(col) >= 0){
                sheet.setColumnWidth(col + colOffset, sheet.getColumnWidth(col));
            }
        }

        //---------------------------------------------------------------
        if (mergedRegions != null){
            for (CellRangeAddress cellRangeAddress : mergedRegions){
                sheet.addMergedRegion(buildCellRangeAddress(rowOffset, colOffset, cellRangeAddress));
            }
        }
    }

    private static Cell buildNewCell(Row newRow,int newCellIndex){
        Cell newCell = newRow.getCell(newCellIndex);
        if (newCell == null){
            return newRow.createCell(newCellIndex);
        }
        return newCell;
    }

    private static Row buildNewRow(Sheet sheet,Row oldRow,int newRowIndex){
        Row newRow = sheet.getRow(newRowIndex);
        if (newRow == null){
            newRow = sheet.createRow(newRowIndex);
        }
        if (oldRow.getHeight() >= 0){
            newRow.setHeight(oldRow.getHeight());
        }

        return newRow;
    }

    private static CellRangeAddress buildCellRangeAddress(int rowOffset,int colOffset,CellRangeAddress cellRangeAddress){
        return new CellRangeAddress(
                        cellRangeAddress.getFirstRow() + rowOffset,
                        cellRangeAddress.getLastRow() + rowOffset,

                        cellRangeAddress.getFirstColumn() + colOffset,
                        cellRangeAddress.getLastColumn() + colOffset);
    }
}
