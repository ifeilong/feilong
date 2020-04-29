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

import static com.feilong.office.excel.ExcelExceptionBuilder.build;

import com.feilong.office.excel.definition.ExcelCell;

/**
 * The Class LongConvertor.
 */
public class LongConvertor extends AbstractChoiceConvertor<Long>{

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
     * @return the long
     */
    @Override
    protected Long convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition){
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (cellDefinition.isMandatory()){
                    throw build(WRONG_DATA_NULL, sheetNo, cellIndex, value, cellDefinition);
                }
                return null;
            }

            try{
                Long v = Long.parseLong((String) value);
                return v;
            }catch (NumberFormatException e){

                throw build(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, cellDefinition);
            }
        }else if (value instanceof Double){
            return Math.round((Double) value);
        }
        throw build(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, cellDefinition);
    }

    //---------------------------------------------------------------

    /**
     * Gets the data type abbr.
     *
     * @return the data type abbr
     */
    @Override
    public String getDataTypeAbbr(){
        return "long";
    }

    /**
     * Support class.
     *
     * @return the class
     */
    @Override
    public Class<Long> supportClass(){
        return Long.class;
    }

}
