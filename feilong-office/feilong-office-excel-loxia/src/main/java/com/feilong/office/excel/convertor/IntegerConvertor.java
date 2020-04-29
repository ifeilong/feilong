package com.feilong.office.excel.convertor;

import static com.feilong.core.bean.ConvertUtil.toInteger;
import static com.feilong.office.excel.ExcelExceptionBuilder.build;

import com.feilong.office.excel.definition.ExcelCell;

/**
 * 解决 12344.0 转成 int 的问题
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class IntegerConvertor extends AbstractChoiceConvertor<Integer>{

    @Override
    protected Integer convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition){
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (!cellDefinition.isMandatory()){
                    return null;
                }
                throw build(WRONG_DATA_NULL, null, sheetNo, cellIndex, cellDefinition);
            }
            //---------------------------------------------------------------
            try{
                return toInteger(value);
            }catch (Exception e){
                throw build(WRONG_DATA_TYPE_NUMBER, value, sheetNo, cellIndex, cellDefinition);
            }
        }else if (value instanceof Double){
            return (int) Math.rint((Double) value);
        }
        throw build(WRONG_DATA_TYPE_NUMBER, value, sheetNo, cellIndex, cellDefinition);
    }

    //---------------------------------------------------------------

    @Override
    public String getDataTypeAbbr(){
        return "integer";
    }

    @Override
    public Class<Integer> supportClass(){
        return Integer.class;
    }

}
