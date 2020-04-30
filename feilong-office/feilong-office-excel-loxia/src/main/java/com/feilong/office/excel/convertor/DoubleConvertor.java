package com.feilong.office.excel.convertor;

import com.feilong.office.excel.ExcelException;
import com.feilong.office.excel.definition.ExcelCell;

public class DoubleConvertor extends AbstractDataConvertor<Double>{

    @Override
    protected Double handleConvert(Object value,int sheetNo,String cellIndex,ExcelCell excelCell){
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (excelCell.isMandatory()){
                    throw new ExcelException(WRONG_DATA_NULL, sheetNo, cellIndex, null, excelCell);
                }
                return null;
            }
            try{
                return new Double((String) value);
            }catch (NumberFormatException e){
                throw new ExcelException(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, excelCell);
            }
        }else if (value instanceof Double){
            return (Double) value;
        }

        throw new ExcelException(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, excelCell);
    }

    //---------------------------------------------------------------

    @Override
    public String getDataTypeAbbr(){
        return "double";
    }

    @Override
    public Class<Double> supportClass(){
        return Double.class;
    }

}
