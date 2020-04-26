package loxia.support.excel.convertor;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.DateUtil;

import loxia.support.LoxiaSupportConstants;
import loxia.support.LoxiaSupportSettings;
import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.exception.ErrorCode;
import loxia.support.excel.exception.ExcelManipulateException;

public class DateConvertor implements DataConvertor<Date> {
	
	private String datePattern = LoxiaSupportSettings.getInstance().
		get(LoxiaSupportConstants.DATE_PATTERN);
	
	public String getDatePattern() {
		return datePattern;
	}
	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	@Override
    public Date convert(Object value, int sheetNo, String cellIndex, 
			ExcelCell cellDefinition) throws ExcelManipulateException {
		if(value == null && cellDefinition.isMandatory())
			throw new ExcelManipulateException(ErrorCode.WRONG_DATA_NULL,
					new Object[]{sheetNo + 1, cellIndex,
					null,cellDefinition.getPattern(),
					cellDefinition.getChoiceString()});
		if(value == null) return null;
		if(value instanceof String){
			String str = (String) value;
			if(str.length() == 0){
				if(cellDefinition.isMandatory())
					throw new ExcelManipulateException(ErrorCode.WRONG_DATA_NULL,
							new Object[]{sheetNo + 1, cellIndex,
							null,cellDefinition.getPattern(),
							cellDefinition.getChoiceString()});
				else
					return null;
			}else{
				String pattern = cellDefinition.getPattern() == null ? 
						datePattern : cellDefinition.getPattern();
				try {
					DateFormat df = new SimpleDateFormat(pattern);
					return df.parse((String)value);
				} catch (ParseException e) {
					throw new ExcelManipulateException(ErrorCode.WRONG_DATA_TYPE_DATE,
							new Object[]{sheetNo + 1, cellIndex,
							value,cellDefinition.getPattern(),
							cellDefinition.getChoiceString()});
				}
			}
		}else if(value instanceof Date){
			return (Date)value;
		}else if(value instanceof Double){
			return DateUtil.getJavaDate((Double)value);
		}else
			throw new ExcelManipulateException(ErrorCode.WRONG_DATA_TYPE_DATE,
					new Object[]{sheetNo + 1, cellIndex,
					value,cellDefinition.getPattern(),
					cellDefinition.getChoiceString()});
	}

	@Override
    public String getDataTypeAbbr() {
		return "date";
	}

	@Override
    public Class<Date> supportClass() {
		return Date.class;
	}

}
