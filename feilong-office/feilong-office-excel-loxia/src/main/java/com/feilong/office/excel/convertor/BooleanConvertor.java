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

import org.apache.commons.lang3.BooleanUtils;

import com.feilong.office.excel.definition.ExcelCell;

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
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0.8 2014年9月20日 下午8:54:47
 * @since 1.0.8
 */
public class BooleanConvertor extends AbstractDataConvertor<Boolean>{

    private static final int WRONG_DATA_FORMAT = 50;

    //---------------------------------------------------------------

    @Override
    protected Boolean handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition){
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
        throw build(WRONG_DATA_FORMAT, sheetNo, cellIndex, value, cellDefinition);
    }
    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see loxia.support.excel.convertor.DataConvertor#getDataTypeAbbr()
     */
    @Override
    public String getDataTypeAbbr(){
        return "boolean";
    }

    /*
     * (non-Javadoc)
     * 
     * @see loxia.support.excel.convertor.DataConvertor#supportClass()
     */
    @Override
    public Class<Boolean> supportClass(){
        return Boolean.class;
    }

}
