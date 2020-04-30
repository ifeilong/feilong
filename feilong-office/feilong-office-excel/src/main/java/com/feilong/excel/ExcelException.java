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
package com.feilong.excel;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.feilong.excel.definition.ExcelCell;

/**
 * The Class ExcelException.
 */
public class ExcelException extends RuntimeException{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 478553091122313602L;

    //---------------------------------------------------------------

    /** The error code. */
    private final int         errorCode;

    //---------------------------------------------------------------

    /** The sheet no. */
    private final int         sheetNo;

    /** The cell index. */
    private final String      cellIndex;

    /** The value. */
    private final Object      value;

    /** The excel cell. */
    private final ExcelCell   excelCell;

    //---------------------------------------------------------------

    /**
     * Instantiates a new excel exception.
     *
     * @param errorCode
     *            the error code
     * @param sheetNo
     *            the sheet no
     * @param cellIndex
     *            the cell index
     * @param value
     *            the value
     * @param excelCell
     *            the excel cell
     */
    public ExcelException(int errorCode, int sheetNo, String cellIndex, Object value, ExcelCell excelCell){
        super();
        this.errorCode = errorCode;
        this.sheetNo = sheetNo;
        this.cellIndex = cellIndex;
        this.value = value;
        this.excelCell = excelCell;
    }

    //---------------------------------------------------------------
    /**
     * To string.
     *
     * @return the string
     */
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
