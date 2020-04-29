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
package com.feilong.office.excel.convertor;

import com.feilong.office.excel.definition.ExcelCell;

/**
 * The Interface DataConvertor.
 *
 * @param <T>
 *            the generic type
 */
public interface DataConvertor<T> {

    /**
     * Support class.
     *
     * @return the class
     */
    Class<T> supportClass();

    //---------------------------------------------------------------

    /**
     * Gets the data type abbr.
     *
     * @return the data type abbr
     */
    String getDataTypeAbbr();

    //---------------------------------------------------------------

    /**
     * Convert.
     *
     * @param value
     *            the value
     * @param sheetNo
     *            the sheet no
     * @param cellIndex
     *            the cell index
     * @param cellDefinition
     *            the cell definition
     * @return the t
     */
    T convert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition);
}
