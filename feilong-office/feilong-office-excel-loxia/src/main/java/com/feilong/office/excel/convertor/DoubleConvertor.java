package com.feilong.office.excel.convertor;

import static com.feilong.office.excel.ExcelExceptionBuilder.build;

import com.feilong.office.excel.definition.ExcelCell;

public class DoubleConvertor extends AbstractDataConvertor<Double>{

    @Override
    protected Double handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition){
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (cellDefinition.isMandatory()){
                    throw build(WRONG_DATA_NULL, null, sheetNo, cellIndex, cellDefinition);
                }
                return null;
            }
            try{
                return new Double((String) value);
            }catch (NumberFormatException e){
                throw build(WRONG_DATA_TYPE_NUMBER, value, sheetNo, cellIndex, cellDefinition);
            }
        }else if (value instanceof Double){
            return (Double) value;
        }

        throw build(WRONG_DATA_TYPE_NUMBER, value, sheetNo, cellIndex, cellDefinition);
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
