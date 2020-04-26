package loxia.support.excel;

import java.io.InputStream;
import java.util.Map;

import loxia.support.excel.definition.ExcelManipulatorDefinition;

public interface ExcelReader {
	
	/**
	 * Read All sheets infos into one beans scope with multiple sheet definitions
	 * @param is
	 * @param beans
	 * @return
	 */
	ReadStatus readAll(InputStream is, Map<String, Object> beans);
	
	/**
	 * Read All sheets using one sheet definition to get colletion infos
	 * @param is
	 * @param beans
	 * @return
	 */
	ReadStatus readAllPerSheet(InputStream is, Map<String,Object> beans);
	
	/**
	 * Read specific sheet using one sheet definition
	 * @param is
	 * @param sheetNo
	 * @param beans
	 * @return
	 */
	ReadStatus readSheet(InputStream is, int sheetNo, Map<String,Object> beans);
	ExcelManipulatorDefinition getDefinition();
	void setDefinition(ExcelManipulatorDefinition definition);
}
