package com.feilong.office.excel.convertor;

import static com.feilong.office.excel.ExcelManipulateExceptionBuilder.build;

import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

public class DoubleConvertor extends AbstractDataConvertor<Double>{

    @Override
    protected Double handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (cellDefinition.isMandatory()){
                    throw build(null, sheetNo, cellIndex, cellDefinition, WRONG_DATA_NULL);
                }
                return null;
            }
            try{
                return new Double((String) value);
            }catch (NumberFormatException e){
                throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_NUMBER);
            }
        }else if (value instanceof Double){
            return (Double) value;
        }

        throw build(value, sheetNo, cellIndex, cellDefinition, WRONG_DATA_TYPE_NUMBER);
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
