/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.excel;

import java.util.HashMap;
import java.util.Map;

import com.feilong.core.Validate;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.util.CloneUtil;

/**
 * A factory for creating ExcelManipulator objects.
 */
public class ExcelManipulatorFactory{

    /**
     * key是sheet Name
     */
    private Map<String, ExcelSheet> sheetDefinitions = new HashMap<>();

    //---------------------------------------------------------------

    /**
     * Sets the config.
     *
     * @param configurations
     *            the new config
     */
    public void setConfig(String...configurations){
        sheetDefinitions = ExcelSheetMapBuilder.build(configurations);
    }

    //---------------------------------------------------------------

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param styleSheetPosition
     *            the style sheet position
     * @param sheetNames
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(Integer styleSheetPosition,String[] sheetNames){
        ExcelWriter excelWriter = createExcelWriterInner(null, sheetNames);
        excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
        return excelWriter;
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param styleSheetPosition
     *            the style sheet position
     * @param writeTemplateName
     *            the write template name
     * @param sheetNames
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(Integer styleSheetPosition,String writeTemplateName,String[] sheetNames){
        ExcelWriter excelWriter = createExcelWriterInner(writeTemplateName, sheetNames);
        excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
        return excelWriter;
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param writeTemplateName
     *            the write template name
     * @param sheetNames
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(String writeTemplateName,String[] sheetNames){
        return createExcelWriterInner(writeTemplateName, sheetNames);
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(String...sheets){
        return createExcelWriterInner(null, sheets);
    }

    //---------------------------------------------------------------

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param writeTemplateName
     *            the write template name
     * @param sheetNames
     *            the sheets
     * @return the excel writer
     */
    private ExcelWriter createExcelWriterInner(String writeTemplateName,String...sheetNames){
        ExcelWriter excelWriter = new DefaultExcelWriter();
        excelWriter.setDefinition(toExcelDefinition(sheetNames));

        if (writeTemplateName != null){
            DefaultExcelWriter dew = (DefaultExcelWriter) excelWriter;
            dew.initBufferedTemplate(Thread.currentThread().getContextClassLoader().getResourceAsStream(writeTemplateName));

        }
        return excelWriter;
    }

    //---------------------------------------------------------------

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param sheetNames
     *            the sheets
     * @return the excel reader
     */
    public ExcelReader createExcelReader(String...sheetNames){
        ExcelReader excelReader = new DefaultExcelReader();
        excelReader.setDefinition(toExcelDefinition(sheetNames));
        return excelReader;
    }

    //---------------------------------------------------------------

    /**
     * To excel manipulator definition.
     *
     * @param sheetNames
     *            the sheets
     * @return the excel manipulator definition
     */
    private ExcelDefinition toExcelDefinition(String...sheetNames){
        ExcelDefinition excelDefinition = new ExcelDefinition();
        for (String sheetName : sheetNames){
            ExcelSheet excelSheet = toExcelSheet(sheetName);
            excelDefinition.getExcelSheets().add(excelSheet);
        }
        return excelDefinition;
    }

    //---------------------------------------------------------------

    /**
     * Gets the excel sheet.
     *
     * @param sheetName
     *            the sheet
     * @return the excel sheet
     */
    private ExcelSheet toExcelSheet(String sheetName){
        if ("blank".equalsIgnoreCase(sheetName)){
            return new ExcelSheet();
        }

        ExcelSheet excelSheet = sheetDefinitions.get(sheetName);
        Validate.notNull(excelSheet, "No sheet defintion found with name: " + sheetName);
        return CloneUtil.cloneSheet(excelSheet);
    }
}
