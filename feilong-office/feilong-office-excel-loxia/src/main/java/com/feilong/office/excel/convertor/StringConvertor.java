package com.feilong.office.excel.convertor;

import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.definition.ExcelCell;

public class StringConvertor extends AbstractChoiceConvertor<String>{

    @Override
    protected String convertValue(Object value,int sheetNo,String cellIndex,ExcelCell cellDefinition) throws ExcelManipulateException{
        String str = (value == null ? null : value.toString());
        if (str != null && str.length() == 0){
            str = null;
        }
        //remove .0 for Integer Data
        if (value instanceof Double && str.length() > 2 && str.lastIndexOf(".0") == (str.length() - 2)){
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    //---------------------------------------------------------------

    @Override
    public String getDataTypeAbbr(){
        return "string";
    }

    @Override
    public Class<String> supportClass(){
        return String.class;
    }
}
