package com.feilong.office.excel.loxia.convertor;

import static com.feilong.core.bean.ConvertUtil.toInteger;
import static com.feilong.office.excel.loxia.ExcelManipulateExceptionBuilder.build;

import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.exception.ErrorCode;
import loxia.support.excel.exception.ExcelManipulateException;

/**
 * 解决 12344.0 转成 int 的问题
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class IntegerConvertor extends loxia.support.excel.convertor.IntegerConvertor{

    @Override
    protected Integer convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        if (value == null && cellDefinition.isMandatory()){
            throw build(null, sheetNo, cellIndex, cellDefinition, ErrorCode.WRONG_DATA_NULL);
        }
        if (value == null){
            return null;
        }
        //---------------------------------------------------------------
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (!cellDefinition.isMandatory()){
                    return null;
                }
                throw build(null, sheetNo, cellIndex, cellDefinition, ErrorCode.WRONG_DATA_NULL);
            }
            //---------------------------------------------------------------
            try{
                return toInteger(value);
            }catch (Exception e){
                throw build(value, sheetNo, cellIndex, cellDefinition, ErrorCode.WRONG_DATA_TYPE_NUMBER);
            }
        }else if (value instanceof Double){
            return (int) Math.rint((Double) value);
        }
        throw build(value, sheetNo, cellIndex, cellDefinition, ErrorCode.WRONG_DATA_TYPE_NUMBER);
    }

}
