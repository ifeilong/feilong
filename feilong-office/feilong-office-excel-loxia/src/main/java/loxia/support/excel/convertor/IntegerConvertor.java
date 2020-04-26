package loxia.support.excel.convertor;

import java.util.ArrayList;
import java.util.List;

import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.exception.ErrorCode;
import loxia.support.excel.exception.ExcelManipulateException;


public class IntegerConvertor extends ChoiceConvertor<Integer> {

	@Override
	protected Integer convertValue(Object value, int sheetNo, String cellIndex, ExcelCell cellDefinition) throws ExcelManipulateException {
		if(value == null && cellDefinition.isMandatory())
			throw new ExcelManipulateException(ErrorCode.WRONG_DATA_NULL,
					new Object[]{sheetNo + 1, cellIndex,
					null,cellDefinition.getPattern(),
					cellDefinition.getChoiceString()});
		if(value == null) return null;
		if(value instanceof String){
			String str = (String) value;
			str = str.trim();
			if(str.length() == 0){
				if(cellDefinition.isMandatory())
					throw new ExcelManipulateException(ErrorCode.WRONG_DATA_NULL,
							new Object[]{sheetNo + 1, cellIndex,
							null,cellDefinition.getPattern(),
							cellDefinition.getChoiceString()});
				else
					return null;
			}else{
				try {
					Integer v = Integer.parseInt((String)value);
					return v;
				} catch (NumberFormatException e) {
					throw new ExcelManipulateException(ErrorCode.WRONG_DATA_TYPE_NUMBER,
							new Object[]{sheetNo + 1, cellIndex,
							value,cellDefinition.getPattern(),
							cellDefinition.getChoiceString()});
				}
			}
		}else if(value instanceof Double){
			return (int)Math.rint((Double)value);
		}else
			throw new ExcelManipulateException(ErrorCode.WRONG_DATA_TYPE_NUMBER,
					new Object[]{sheetNo + 1, cellIndex,
					value,cellDefinition.getPattern(),
					cellDefinition.getChoiceString()});
	}

	@Override
	protected List<? extends Integer> getChoices(ExcelCell cellDefinition) {
		if(cellDefinition.getAvailableChoices() == null ||
				cellDefinition.getAvailableChoices().length == 0)
			return null;
		List<Integer> result = new ArrayList<Integer>();
		for(String str: cellDefinition.getAvailableChoices())
			result.add(Integer.parseInt(str));
		return result;
	}

	@Override
    public String getDataTypeAbbr() {
		return "integer";
	}

	@Override
    public Class<Integer> supportClass() {
		return Integer.class;
	}

}
