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
package com.feilong.excel.convertor;

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;

public abstract class AbstractDataConvertor<T> implements DataConvertor<T>{

    protected static final int WRONG_DATA_NULL        = 1;

    protected static final int WRONG_DATA_TYPE_NUMBER = 11;

    //---------------------------------------------------------------

    @Override
    public T convert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        if (value == null && excelCell.isMandatory()){
            throw new ExcelException(WRONG_DATA_NULL, sheetNo, cellIndex, null, excelCell);
        }
        if (value == null){
            return null;
        }

        //---------------------------------------------------------------
        T t = handleConvert(value, sheetNo, cellIndex, excelCell);
        return t;
    }

    protected abstract T handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell);
}
