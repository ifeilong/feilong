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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.util.Config;

/**
 * The Class DateConvertor.
 */
public class DateConvertor extends AbstractDataConvertor<Date>{

    private static final int WRONG_DATA_TYPE_DATE = 13;

    //---------------------------------------------------------------

    /** The date pattern. */
    private String           datePattern          = Config.get("date.pattern");

    //---------------------------------------------------------------

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
    protected Date handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        if (value instanceof String){
            String str = (String) value;
            if (str.length() == 0){
                if (excelCell.isMandatory()){
                    throw new ExcelException(WRONG_DATA_NULL, sheetNo, cellIndex, value, excelCell);
                }
                return null;
            }
            String pattern = excelCell.getPattern() == null ? datePattern : excelCell.getPattern();
            try{
                DateFormat df = new SimpleDateFormat(pattern);
                return df.parse((String) value);
            }catch (ParseException e){
                throw new ExcelException(WRONG_DATA_TYPE_DATE, sheetNo, cellIndex, value, excelCell);
            }
        }
        if (value instanceof Date){
            return (Date) value;
        }
        if (value instanceof Double){
            return DateUtil.getJavaDate((Double) value);
        }

        throw new ExcelException(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, excelCell);
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
