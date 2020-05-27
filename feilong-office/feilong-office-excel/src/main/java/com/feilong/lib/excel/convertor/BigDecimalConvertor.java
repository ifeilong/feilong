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

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;

import java.math.BigDecimal;

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;

/**
 * 解决 loxia 中 将 236796.83 解析成 236796.82999999998719431459903717041015625
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.2
 */
public class BigDecimalConvertor extends AbstractDataConvertor<BigDecimal>{

    @Override
    protected BigDecimal handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (!excelCell.isMandatory()){
                    return null;
                }
                throw new ExcelException(WRONG_DATA_NULL, sheetNo, cellIndex, null, excelCell);
            }
            try{
                return toBigDecimal(value);
            }catch (Exception e){
                throw new ExcelException(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, excelCell);
            }
        }else if (value instanceof Double){
            return toBigDecimal(value);
        }
        throw new ExcelException(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, excelCell);
    }

    //---------------------------------------------------------------

    @Override
    public String getDataTypeAbbr(){
        return "bigdecimal";
    }

    @Override
    public Class<BigDecimal> supportClass(){
        return BigDecimal.class;
    }

}
