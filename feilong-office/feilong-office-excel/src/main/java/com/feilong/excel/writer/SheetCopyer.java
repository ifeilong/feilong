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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SheetCopyer{

    static void copy(Sheet sheet,Sheet newSheet){
        int maxCol = 0;
        for (int row = 0; row <= sheet.getLastRowNum(); row++){
            Row oldRow = sheet.getRow(row);
            if (oldRow == null){
                continue;
            }
            Row newRow = newSheet.getRow(row);
            if (newRow == null){
                newRow = newSheet.createRow(row);
            }
            if (oldRow.getHeight() >= 0){
                newRow.setHeight(oldRow.getHeight());
            }
            maxCol = (maxCol >= oldRow.getLastCellNum() - 1 ? maxCol : oldRow.getLastCellNum() - 1);
            for (int col = 0; col < oldRow.getLastCellNum(); col++){
                Cell oldCell = oldRow.getCell(col);
                if (oldCell == null){
                    continue;
                }
                Cell newCell = newRow.getCell(col);
                if (newCell == null){
                    newCell = newRow.createCell(col);
                }
                CellCoper.copy(oldCell, newCell);
            }
        }

        //---------------------------------------------------------------
        for (int col = 0; col <= maxCol; col++){
            if (sheet.getColumnWidth(col) >= 0){
                newSheet.setColumnWidth(col, sheet.getColumnWidth(col));
            }
        }
        for (int i = 0; i < sheet.getNumMergedRegions(); i++){
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            newSheet.addMergedRegion(cellRangeAddress);
        }
    }
}
