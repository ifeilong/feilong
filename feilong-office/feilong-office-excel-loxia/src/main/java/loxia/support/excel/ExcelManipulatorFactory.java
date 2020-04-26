package loxia.support.excel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.support.excel.definition.ExcelManipulatorDefinition;
import loxia.support.excel.definition.ExcelSheet;
import loxia.support.excel.impl.DefaultExcelReader;
import loxia.support.excel.impl.DefaultExcelWriter;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ExcelManipulatorFactory {
	static final Logger logger = LoggerFactory.getLogger(ExcelManipulatorFactory.class);
	
	public static final String RULE_FILE = "loxia/support/excel/excelcontent-definition-rule.xml";
	
	private Map<String, ExcelSheet> sheetDefinitions = new HashMap<String, ExcelSheet>();	
	
	private static final String BLANK_SHEET_DEF = "blank";
	private static final ExcelSheet BLANK_SHEET = new ExcelSheet();
	
	@SuppressWarnings("unchecked")
	public void setConfig(String... configurations){
		for(String config: configurations){
			Digester digester = DigesterLoader.createDigester(
					new InputSource(Thread.currentThread().getContextClassLoader()
							.getResourceAsStream(RULE_FILE)));
			digester.setValidating(false);
			try {
				List<ExcelSheet> list =
					(List<ExcelSheet>)digester.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(config));
				for(ExcelSheet es: list)
					sheetDefinitions.put(es.getName(), es);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Read excel config failed.");
			} catch (SAXException e) {
				e.printStackTrace();
				throw new RuntimeException("Read excel config failed.");
			}
		}
	}
	public ExcelWriter createExcelWriter(Integer styleSheetPosition, Class<? extends ExcelWriter> clazz, String[] sheets){
		ExcelWriter excelWriter = createExcelWriterInner(clazz, null, sheets);
		excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
		return excelWriter;
	}
	
	public ExcelWriter createExcelWriter(Integer styleSheetPosition, String[] sheets){
		ExcelWriter excelWriter = createExcelWriterInner(null, null, sheets);
		excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
		return excelWriter;
	}
	
	public ExcelWriter createExcelWriter(Integer styleSheetPosition, String writeTemplateName, String[] sheets){
		ExcelWriter excelWriter = createExcelWriterInner(null, writeTemplateName, sheets);
		excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
		return excelWriter;
	}
	
	public ExcelWriter createExcelWriter(Class<? extends ExcelWriter> clazz, String[] sheets){
		ExcelWriter excelWriter = createExcelWriterInner(clazz, null, sheets);
		return excelWriter;
	}
	
	public ExcelWriter createExcelWriter(String writeTemplateName, String[] sheets){
		ExcelWriter excelWriter = createExcelWriterInner(null, writeTemplateName, sheets);
		return excelWriter;
	}
	
	public ExcelWriter createExcelWriter(String... sheets){
		return createExcelWriterInner(null, null, sheets);
	}
	
	private ExcelWriter createExcelWriterInner(Class<? extends ExcelWriter> clazz, String writeTemplateName, String... sheets){
		ExcelWriter excelWriter = null;
		if(clazz == null)
			excelWriter = new DefaultExcelWriter();
		else{
			try {				
				excelWriter = (ExcelWriter)clazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Initiate ExcelWriter[" + clazz + "] failure");
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Initiate ExcelWriter[" + clazz + "] failure");
			}
		}
		ExcelManipulatorDefinition definition = new ExcelManipulatorDefinition();
		for(String sheet: sheets){
			ExcelSheet sheetDefinition = getExcelSheet(sheet);			
			definition.getExcelSheets().add(sheetDefinition);			
		}
		excelWriter.setDefinition(definition);
		if(writeTemplateName != null){
			if(excelWriter instanceof DefaultExcelWriter){
				DefaultExcelWriter dew = (DefaultExcelWriter)excelWriter;
				dew.initBufferedTemplate(Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(writeTemplateName));
			}{
				//for other customizations
			}
		}
		return excelWriter;
	}
	
	public ExcelReader createExcelReader(String... sheets){
		return createExcelReader(null, sheets);
	}
	
	public ExcelReader createExcelReader(Class<? extends ExcelReader> clazz, String... sheets){
		ExcelReader excelReader = null;
		if(clazz == null)
			excelReader = new DefaultExcelReader();
		else{
			try {
				excelReader = (ExcelReader)clazz.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("Initiate ExcelReader[" + clazz + "] failure");
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Initiate ExcelReader[" + clazz + "] failure");
			}
		}
		ExcelManipulatorDefinition definition = new ExcelManipulatorDefinition();
		for(String sheet: sheets){
			ExcelSheet sheetDefinition = getExcelSheet(sheet);			
			definition.getExcelSheets().add(sheetDefinition);			
		}
		excelReader.setDefinition(definition);
		return excelReader;
	}
	
	private ExcelSheet getExcelSheet(String sheet){
		if(BLANK_SHEET_DEF.equalsIgnoreCase(sheet)) return BLANK_SHEET;
		ExcelSheet sheetDefinition = sheetDefinitions.get(sheet);
		if(sheetDefinition == null)
			throw new RuntimeException("No sheet defintion found with name: " + sheet);
		return sheetDefinition.cloneSheet();
	}
}
