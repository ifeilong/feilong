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

import com.feilong.office.excel.utils.CellReferenceUtil;

class BlockStyleSetter{

    /** Don't let anyone instantiate this class. */
    private BlockStyleSetter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 设置 block style.
     *
     * @param sheet
     *            the sheet
     * @param startRowIndex
     *            the start row index
     * @param endRowIndex
     *            the end row index
     * @param startColumnIndex
     *            the start col index
     * @param endColumnIndex
     *            the end col index
     * @param cellIndex
     *            the cell index
     * @param styleMap
     *            the style map
     */
    static void set(
                    Sheet sheet,
                    int startRowIndex,
                    int endRowIndex,

                    int startColumnIndex,
                    int endColumnIndex,

                    String cellIndex,
                    Map<String, CellStyle> styleMap){
        for (int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++){
            Row row = sheet.getRow(rowIndex);
            if (row == null){
                row = sheet.createRow(rowIndex);
            }
            //---------------------------------------------------------------
            for (int columnIndex = startColumnIndex; columnIndex <= endColumnIndex; columnIndex++){
                String cIdx = CellReferenceUtil.getCellRef(cellIndex, rowIndex - startRowIndex, columnIndex - startColumnIndex);
                CellStyle cellStyle = styleMap.get(cIdx);
                if (cellStyle == null){
                    continue;
                }
                Cell cell = row.getCell(columnIndex);
                if (cell == null){
                    cell = row.createCell(columnIndex);
                }
                cell.setCellStyle(cellStyle);
            }
        }
    }
}
