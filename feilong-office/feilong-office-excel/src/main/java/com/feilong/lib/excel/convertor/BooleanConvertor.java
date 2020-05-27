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

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.lib.lang3.BooleanUtils;

/**
 * boolean转型转换器,用于loxia excel 类型转换注册使用 ,转换Boolean 类型的值<br>
 * 
 * <p>
 * 使用方式:
 * </p>
 * 
 * <pre class="code">
 * DataConvertorConfigurator.getInstance().registerDataConvertor(new BooleanConvertor());
 * </pre>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.8 2014年9月20日 下午8:54:47
 * @since 1.0.8
 */
public class BooleanConvertor extends AbstractDataConvertor<Boolean>{

    private static final int WRONG_DATA_FORMAT = 50;

    //---------------------------------------------------------------

    @Override
    protected Boolean handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        if (value instanceof Boolean){
            return (Boolean) value;
        }
        if (value instanceof String){
            String str = (String) value;
            //TODO 字符串是 1
            return BooleanUtils.toBooleanObject(str);
        }
        if (value instanceof Number){
            Number value2 = (Number) value;
            return BooleanUtils.toBooleanObject(value2.intValue());
        }
        throw new ExcelException(WRONG_DATA_FORMAT, sheetNo, cellIndex, value, excelCell);
    }
    //---------------------------------------------------------------

    @Override
    public String getDataTypeAbbr(){
        return "boolean";
    }

    @Override
    public Class<Boolean> supportClass(){
        return Boolean.class;
    }

}
