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
package com.feilong.excel.util;

import org.apache.poi.ss.util.CellReference;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CellReferenceUtil{

    /**
     * Gets the cell index.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     * @return the cell index
     */
    public static String getCellRef(int row,int col){
        CellReference cellReference = new CellReference(row, col);
        return cellReference.formatAsString().replaceAll("\\$", "");
    }

    //---------------------------------------------------------------

    /**
     * Offset cell index.
     *
     * @param cellRef
     *            the cell index
     * @param rowOffset
     *            the row offset
     * @param colOffset
     *            the col offset
     * @return the string
     */
    public static String getCellRef(String cellRef,int rowOffset,int colOffset){
        CellReference cellReference = new CellReference(cellRef);

        int pRow = cellReference.getRow() + rowOffset;
        int pCol = cellReference.getCol() + colOffset;
        return getCellRef(pRow, pCol);
    }

    //---------------------------------------------------------------

    /**
     * Gets the cell position.
     *
     * @param cellRef
     *            the cell index
     * @return the cell position
     */
    public static int[] getCellPosition(String cellRef){
        CellReference cellReference = new CellReference(cellRef);
        return new int[] { cellReference.getRow(), cellReference.getCol() };
    }

}
