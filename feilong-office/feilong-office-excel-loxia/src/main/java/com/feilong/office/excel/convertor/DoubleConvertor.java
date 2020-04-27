package com.feilong.office.excel.convertor;

import com.feilong.office.excel.ErrorCode;
import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

public class DoubleConvertor implements DataConvertor<Double>{

    @Override
    public Double convert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
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
                    return new Double((String) value);
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
            return (Double) value;
        }else{
            throw new ExcelManipulateException(
                            ErrorCode.WRONG_DATA_TYPE_NUMBER,
                            new Object[] { sheetNo + 1, cellIndex, value, cellDefinition.getPattern(), cellDefinition.getChoiceString() });
        }
    }

    @Override
    public String getDataTypeAbbr(){
        return "double";
    }

    @Override
    public Class<Double> supportClass(){
        return Double.class;
    }

}
