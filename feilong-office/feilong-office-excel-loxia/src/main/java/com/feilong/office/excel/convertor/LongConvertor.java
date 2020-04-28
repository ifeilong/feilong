package com.feilong.office.excel.convertor;

import static com.feilong.office.excel.ExcelManipulateExceptionBuilder.build;

import java.util.ArrayList;
import java.util.List;

import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

public class LongConvertor extends ChoiceConvertor<Long>{

    @Override
    protected Long convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (cellDefinition.isMandatory()){
                    throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_NULL);
                }
                return null;
            }

            try{
                Long v = Long.parseLong((String) value);
                return v;
            }catch (NumberFormatException e){

                throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_NUMBER);
            }
        }else if (value instanceof Double){
            return Math.round((Double) value);
        }
        throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_NUMBER);
    }

    //---------------------------------------------------------------

    @Override
    protected List<? extends Long> getChoices(ExcelCell cellDefinition){
        if (cellDefinition.getAvailableChoices() == null || cellDefinition.getAvailableChoices().length == 0){
            return null;
        }
        List<Long> result = new ArrayList<>();
        for (String str : cellDefinition.getAvailableChoices()){
            result.add(Long.parseLong(str));
        }
        return result;
    }

    //---------------------------------------------------------------

    @Override
    public String getDataTypeAbbr(){
        return "long";
    }

    @Override
    public Class<Long> supportClass(){
        return Long.class;
    }

}
