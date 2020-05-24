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
package com.feilong.lib.excel.convertor;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.lib.lang3.ArrayUtils;

public abstract class AbstractChoiceConvertor<T> extends AbstractDataConvertor<T>{

    private static final int OUT_OF_CHOICES = 3;

    @Override
    protected T handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        T t = convertValue(sheetNo, cellIndex, excelCell, value);
        if (t == null){
            return null;
        }

        //---------------------------------------------------------------
        String[] availableChoices = excelCell.getAvailableChoices();
        if (isNullOrEmpty(availableChoices)){
            return t;
        }

        //---------------------------------------------------------------
        T[] choices = toArray(availableChoices, supportClass());
        if (ArrayUtils.contains(choices, t)){
            return t;
        }

        //---------------------------------------------------------------
        throw new ExcelException(OUT_OF_CHOICES, sheetNo, cellIndex, value, excelCell);
    }

    protected abstract T convertValue(int sheetNo,String cellIndex,ExcelCell excelCell,Object value);
}
