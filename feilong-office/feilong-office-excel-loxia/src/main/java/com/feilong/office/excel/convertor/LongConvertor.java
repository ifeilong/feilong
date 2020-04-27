package com.feilong.office.excel.convertor;

import java.util.ArrayList;
import java.util.List;

import com.feilong.office.excel.ErrorCode;
import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

public class LongConvertor extends ChoiceConvertor<Long>{

    @Override
    protected Long convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        if (value == null && cellDefinition.isMandatory()){
            throw new ExcelManipulateException(
                            ErrorCode.WRONG_DATA_NULL,
                            new Object[] { sheetNo + 1, cellIndex, null, cellDefinition.getPattern(), cellDefinition.getChoiceString() });
        }
        if (value == null){
            return null;
        }
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (cellDefinition.isMandatory()){
                    throw new ExcelManipulateException(
                                    ErrorCode.WRONG_DATA_NULL,
                                    new Object[] {
                                                   sheetNo + 1,
                                                   cellIndex,
                                                   null,
                                                   cellDefinition.getPattern(),
                                                   cellDefinition.getChoiceString() });
                }else{
                    return null;
                }
            }else{
                try{
                    Long v = Long.parseLong((String) value);
                    return v;
                }catch (NumberFormatException e){
                    throw new ExcelManipulateException(
                                    ErrorCode.WRONG_DATA_TYPE_NUMBER,
                                    new Object[] {
                                                   sheetNo + 1,
                                                   cellIndex,
                                                   value,
                                                   cellDefinition.getPattern(),
                                                   cellDefinition.getChoiceString() });
                }
            }
        }else if (value instanceof Double){
            return Math.round((Double) value);
        }else{
            throw new ExcelManipulateException(
                            ErrorCode.WRONG_DATA_TYPE_NUMBER,
                            new Object[] { sheetNo + 1, cellIndex, value, cellDefinition.getPattern(), cellDefinition.getChoiceString() });
        }
    }

    @Override
    protected List<? extends Long> getChoices(ExcelCell cellDefinition){
        if (cellDefinition.getAvailableChoices() == null || cellDefinition.getAvailableChoices().length == 0){
            return null;
        }
        List<Long> result = new ArrayList<Long>();
        for (String str : cellDefinition.getAvailableChoices()){
            result.add(Long.parseLong(str));
        }
        return result;
    }

    @Override
    public String getDataTypeAbbr(){
        return "long";
    }

    @Override
    public Class<Long> supportClass(){
        return Long.class;
    }

}
