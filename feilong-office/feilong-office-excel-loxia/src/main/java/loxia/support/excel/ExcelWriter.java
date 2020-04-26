package loxia.support.excel;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import loxia.support.excel.definition.ExcelManipulatorDefinition;

public interface ExcelWriter {
	WriteStatus write(OutputStream os, Map<String, Object> beans);
	WriteStatus writePerSheet(OutputStream os, List<Map<String,Object>> beansList);
	
	WriteStatus write(String template, OutputStream os, Map<String, Object> beans);
	WriteStatus write(InputStream is, OutputStream os, Map<String, Object> beans);
	WriteStatus writePerSheet(String template, OutputStream os, List<Map<String,Object>> beansList);
	WriteStatus writePerSheet(InputStream is, OutputStream os, List<Map<String,Object>> beansList);
	ExcelManipulatorDefinition getDefinition();
	void setDefinition(ExcelManipulatorDefinition definition);
}
