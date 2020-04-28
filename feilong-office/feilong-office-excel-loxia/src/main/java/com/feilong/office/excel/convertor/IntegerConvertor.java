package com.feilong.office.excel.convertor;

import static com.feilong.core.bean.ConvertUtil.toInteger;
import static com.feilong.office.excel.ExcelManipulateExceptionBuilder.build;

import java.util.ArrayList;
import java.util.List;

import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

/**
 * 解决 12344.0 转成 int 的问题
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class IntegerConvertor extends ChoiceConvertor<Integer>{

    @Override
    protected Integer convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (!cellDefinition.isMandatory()){
                    return null;
                }
                throw build(null, sheetNo, cellIndex, cellDefinition, WRONG_DATA_NULL);
            }
            //---------------------------------------------------------------
            try{
                return toInteger(value);
            }catch (Exception e){
                throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_NUMBER);
            }
        }else if (value instanceof Double){
            return (int) Math.rint((Double) value);
        }
        throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_NUMBER);
    }

    @Override
    protected List<? extends Integer> getChoices(ExcelCell cellDefinition){
        if (cellDefinition.getAvailableChoices() == null || cellDefinition.getAvailableChoices().length == 0){
            return null;
        }
        List<Integer> result = new ArrayList<>();
        for (String str : cellDefinition.getAvailableChoices()){
            result.add(Integer.parseInt(str));
        }
        return result;
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
