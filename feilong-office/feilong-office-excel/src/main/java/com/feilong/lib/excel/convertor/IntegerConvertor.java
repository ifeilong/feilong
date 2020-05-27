package com.feilong.lib.excel.convertor;

import static com.feilong.core.bean.ConvertUtil.toInteger;

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelCell;

/**
 * 解决 12344.0 转成 int 的问题
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.2
 */
public class IntegerConvertor extends AbstractChoiceConvertor<Integer>{

    @Override
    protected Integer convertValue(int sheetNo,String cellIndex,ExcelCell excelCell,Object value){
        if (value instanceof String){
            String str = (String) value;
            str = str.trim();
            if (str.length() == 0){
                if (!excelCell.isMandatory()){
                    return null;
                }
                throw new ExcelException(WRONG_DATA_NULL, sheetNo, cellIndex, null, excelCell);
            }
            //---------------------------------------------------------------
            try{
                return toInteger(value);
            }catch (Exception e){
                throw new ExcelException(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, excelCell);
            }
        }else if (value instanceof Double){
            return (int) Math.rint((Double) value);
        }
        throw new ExcelException(WRONG_DATA_TYPE_NUMBER, sheetNo, cellIndex, value, excelCell);
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
