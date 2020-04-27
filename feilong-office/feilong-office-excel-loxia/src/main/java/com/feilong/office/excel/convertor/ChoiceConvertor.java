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

import java.util.List;

import com.feilong.office.excel.ErrorCode;
import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

/**
 * The Class ChoiceConvertor.
 *
 * @param <T>
 *            the generic type
 */
public abstract class ChoiceConvertor<T> implements DataConvertor<T>{

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
     * @throws ExcelManipulateException
     *             the excel manipulate exception
     */
    @Override
    public T convert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        T convertedValue = convertValue(value, sheetNo, cellIndex, cellDefinition);

        List<? extends T> choices = getChoices(cellDefinition);
        if (convertedValue == null || choices == null || choices.contains(convertedValue)){
            return convertedValue;
        }
        throw new ExcelManipulateException(
                        ErrorCode.OUT_OF_CHOICES,
                        new Object[] {
                                       sheetNo + 1,
                                       cellIndex,
                                       convertedValue,
                                       cellDefinition.getPattern(),
                                       cellDefinition.getChoiceString() });
    }

    /**
     * 获得 choices.
     *
     * @param cellDefinition
     *            the cell definition
     * @return the choices
     */
    protected abstract List<? extends T> getChoices(ExcelCell cellDefinition);

    /**
     * Convert value.
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
     * @throws ExcelManipulateException
     *             the excel manipulate exception
     */
    protected abstract T convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException;

}
