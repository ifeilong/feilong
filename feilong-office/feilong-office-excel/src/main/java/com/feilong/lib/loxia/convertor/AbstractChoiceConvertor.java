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
package com.feilong.lib.loxia.convertor;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.lib.lang3.ArrayUtils;

/**
 * The Class ChoiceConvertor.
 *
 * @param <T>
 *            the generic type
 */
public abstract class AbstractChoiceConvertor<T> extends AbstractDataConvertor<T>{

    private static final int OUT_OF_CHOICES = 3;

    //---------------------------------------------------------------

    @Override
    protected T handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        T t = convertValue(value, sheetNo, cellIndex, excelCell);

        T[] choices = getChoices(excelCell);
        if (t == null || choices == null || ArrayUtils.contains(choices, t)){
            return t;
        }

        throw new ExcelException(OUT_OF_CHOICES, sheetNo, cellIndex, value, excelCell);
    }

    //---------------------------------------------------------------

    /**
     * 获得 choices.
     *
     * @param cellDefinition
     *            the cell definition
     * @return the choices
     */
    private T[] getChoices(ExcelCell cellDefinition){
        String[] availableChoices = cellDefinition.getAvailableChoices();
        if (isNullOrEmpty(availableChoices)){
            return null;
        }
        return toArray(availableChoices, supportClass());
    }

    protected abstract T convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition);

}
