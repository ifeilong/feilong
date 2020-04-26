package loxia.support.excel.convertor;

import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.exception.ExcelManipulateException;


public interface DataConvertor<T> {
	Class<T> supportClass();
	String getDataTypeAbbr();
	T convert(Object value, int sheetNo, String cellIndex, ExcelCell cellDefinition)
		throws ExcelManipulateException;
}
