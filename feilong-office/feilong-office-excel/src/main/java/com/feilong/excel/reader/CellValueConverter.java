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
package com.feilong.excel.reader;

import com.feilong.excel.DataConvertorConfig;
import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.lib.excel.convertor.DataConvertor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class CellValueConverter{

    /** The Constant UNSUPPORTING_DATA_TYPE. */
    private static final int UNSUPPORTING_DATA_TYPE = 2;

    //---------------------------------------------------------------
    static Object convert(int sheetNo,String cellIndex,Object value,ExcelCell excelCell,Class<? extends Object> propertyClass){
        //primitive type should be mandatory
        if (propertyClass.isPrimitive()){
            excelCell.setMandatory(true);
        }

        //---------------------------------------------------------------
        DataConvertor<?> dataConvertor = DataConvertorConfig.getInstance().getConvertor(propertyClass);
        if (dataConvertor == null){
            throw new ExcelException(UNSUPPORTING_DATA_TYPE, sheetNo, cellIndex, value, excelCell);
        }
        return dataConvertor.convert(value, sheetNo, cellIndex, excelCell);
    }
}
