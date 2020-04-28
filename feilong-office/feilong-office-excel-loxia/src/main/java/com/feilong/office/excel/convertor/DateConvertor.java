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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;

import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.LoxiaSupportConstants;
import com.feilong.office.excel.LoxiaSupportSettings;
import com.feilong.office.excel.definition.ExcelCell;

/**
 * The Class DateConvertor.
 */
public class DateConvertor extends AbstractDataConvertor<Date>{

    private static final int WRONG_DATA_TYPE_DATE = 13;

    //---------------------------------------------------------------

    /** The date pattern. */
    private String           datePattern          = LoxiaSupportSettings.getInstance().get(LoxiaSupportConstants.DATE_PATTERN);

    /**
     * Gets the date pattern.
     *
     * @return the date pattern
     */
    public String getDatePattern(){
        return datePattern;
    }

    /**
     * Sets the date pattern.
     *
     * @param datePattern
     *            the new date pattern
     */
    public void setDatePattern(String datePattern){
        this.datePattern = datePattern;
    }

    //---------------------------------------------------------------
    @Override
    protected Date handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        if (value instanceof String){
            String str = (String) value;
            if (str.length() == 0){
                if (cellDefinition.isMandatory()){
                    throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_NULL);
                }
                return null;
            }
            String pattern = cellDefinition.getPattern() == null ? datePattern : cellDefinition.getPattern();
            try{
                DateFormat df = new SimpleDateFormat(pattern);
                return df.parse((String) value);
            }catch (ParseException e){
                throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_DATE);
            }
        }
        if (value instanceof Date){
            return (Date) value;
        }
        if (value instanceof Double){
            return DateUtil.getJavaDate((Double) value);
        }

        throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_NUMBER);
    }

    //---------------------------------------------------------------

    /**
     * Gets the data type abbr.
     *
     * @return the data type abbr
     */
    @Override
    public String getDataTypeAbbr(){
        return "date";
    }

    /**
     * Support class.
     *
     * @return the class
     */
    @Override
    public Class<Date> supportClass(){
        return Date.class;
    }

}
