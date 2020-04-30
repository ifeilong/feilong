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
package com.feilong.office.excel;

import static com.feilong.core.date.DateUtil.formatDuration;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.office.excel.definition.ExcelSheet;
import com.feilong.office.excel.utils.CloneUtil;

/**
 * A factory for creating ExcelManipulator objects.
 */
public class ExcelManipulatorFactory{

    /** The Constant log. */
    private static final Logger           LOGGER           = LoggerFactory.getLogger(ExcelManipulatorFactory.class);

    //---------------------------------------------------------------

    /** The Constant RULE_FILE. */
    private static final String           RULE_FILE        = "config/loxia/excel-definition-rule.xml";

    /** The sheet definitions. */
    //---------------------------------------------------------------
    private final Map<String, ExcelSheet> sheetDefinitions = new HashMap<>();

    /** The Constant BLANK_SHEET. */
    private static final ExcelSheet       BLANK_SHEET      = new ExcelSheet();

    //---------------------------------------------------------------

    /**
     * Sets the config.
     *
     * @param configurations
     *            the new config
     */
    public void setConfig(String...configurations){
        Validate.notEmpty(configurations, "configurations can't be null/empty!");

        //---------------------------------------------------------------

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        Digester digester = DigesterLoader.createDigester(new InputSource(contextClassLoader.getResourceAsStream(RULE_FILE)));
        digester.setValidating(false);

        //---------------------------------------------------------------

        for (String configuration : configurations){
            Validate.notBlank(configuration, "config can't be blank!");
            try{
                Date beginDate = new Date();
                List<ExcelSheet> excelSheetList = (List<ExcelSheet>) digester.parse(contextClassLoader.getResourceAsStream(configuration));

                for (ExcelSheet excelSheet : excelSheetList){
                    sheetDefinitions.put(excelSheet.getName(), excelSheet);
                }

                //---------------------------------------------------------------
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug(
                                    "parse config:[{}],list size:[{}],use time: [{}]",
                                    configuration,
                                    CollectionUtils.size(excelSheetList),
                                    formatDuration(beginDate));
                }
            }catch (Exception e){
                throw new DefaultRuntimeException("parse [" + configuration + "] fail", e);
            }
        }
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
            return BLANK_SHEET;
        }
        ExcelSheet excelSheet = sheetDefinitions.get(sheetName);
        Validate.notNull(excelSheet, "No sheet defintion found with name: " + sheetName);
        return CloneUtil.cloneSheet(excelSheet);
    }
}
