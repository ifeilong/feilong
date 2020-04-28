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

import static com.feilong.office.excel.ExcelManipulateExceptionBuilder.build;

import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

/**
 * The Class AbstractDataConvertor.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 * @since 3.0.0
 */
public abstract class AbstractDataConvertor<T> implements DataConvertor<T>{

    protected static final int WRONG_DATA_NULL        = 1;

    protected static final int WRONG_DATA_TYPE_NUMBER = 11;

    @Override
    public T convert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        if (value == null && cellDefinition.isMandatory()){
            throw build(null, sheetNo, cellIndex, cellDefinition, WRONG_DATA_NULL);
        }
        if (value == null){
            return null;
        }

        //---------------------------------------------------------------
        T t = handleConvert(value, sheetNo, cellIndex, cellDefinition);

        return t;
    }

    protected abstract T handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException;
}
