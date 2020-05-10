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

import com.feilong.excel.definition.ExcelCell;

/**
 * The Class StringConvertor.
 */
public class StringConvertor extends AbstractChoiceConvertor<String>{

    @Override
    protected String convertValue(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        String str = (value == null ? null : value.toString());
        if (str != null && str.length() == 0){
            str = null;
        }

        //---------------------------------------------------------------
        if (null != str){
            //remove .0 for Integer Data
            if (value instanceof Double //
                            && str.length() > 2//
                            && str.lastIndexOf(".0") == (str.length() - 2)){
                str = str.substring(0, str.length() - 2);
            }
        }
        return str;
    }

    //---------------------------------------------------------------

    /**
     * Gets the data type abbr.
     *
     * @return the data type abbr
     */
    @Override
    public String getDataTypeAbbr(){
        return "string";
    }

    /**
     * Support class.
     *
     * @return the class
     */
    @Override
    public Class<String> supportClass(){
        return String.class;
    }
}
