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
        try{
            for (String config : configurations){
                Date beginDate = new Date();

                Digester digester = DigesterLoader.createDigester(
                                new InputSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(RULE_FILE)));
                digester.setValidating(false);

                List<ExcelSheet> list = (List<ExcelSheet>) digester
                                .parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(config));

                for (ExcelSheet excelSheet : list){
                    sheetDefinitions.put(excelSheet.getName(), excelSheet);
                }

                //---------------------------------------------------------------
                if (LOGGER.isDebugEnabled()){
                    LOGGER.debug(
                                    "parse config:[{}],list size:[{}],use time: [{}]",
                                    config,
                                    CollectionUtils.size(list),
                                    formatDuration(beginDate));
                }

            }
        }catch (Exception e){
            throw new RuntimeException("Read excel config failed.", e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param styleSheetPosition
     *            the style sheet position
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(Integer styleSheetPosition,String[] sheets){
        ExcelWriter excelWriter = createExcelWriterInner(null, sheets);
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
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(Integer styleSheetPosition,String writeTemplateName,String[] sheets){
        ExcelWriter excelWriter = createExcelWriterInner(writeTemplateName, sheets);
        excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
        return excelWriter;
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param writeTemplateName
     *            the write template name
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(String writeTemplateName,String[] sheets){
        return createExcelWriterInner(writeTemplateName, sheets);
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

    private ExcelWriter createExcelWriterInner(String writeTemplateName,String...sheets){
        ExcelWriter excelWriter = new DefaultExcelWriter();
        excelWriter.setDefinition(toExcelManipulatorDefinition(sheets));

        if (writeTemplateName != null){
            DefaultExcelWriter dew = (DefaultExcelWriter) excelWriter;
            dew.initBufferedTemplate(Thread.currentThread().getContextClassLoader().getResourceAsStream(writeTemplateName));

        }
        return excelWriter;
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param sheets
     *            the sheets
     * @return the excel reader
     */
    public ExcelReader createExcelReader(String...sheets){
        ExcelReader excelReader = new DefaultExcelReader();
        excelReader.setDefinition(toExcelManipulatorDefinition(sheets));
        return excelReader;
    }

    private ExcelManipulatorDefinition toExcelManipulatorDefinition(String...sheets){
        ExcelManipulatorDefinition excelManipulatorDefinition = new ExcelManipulatorDefinition();
        for (String sheet : sheets){
            ExcelSheet sheetDefinition = toExcelSheet(sheet);
            excelManipulatorDefinition.getExcelSheets().add(sheetDefinition);
        }
        return excelManipulatorDefinition;
    }

    //---------------------------------------------------------------

    /**
     * Gets the excel sheet.
     *
     * @param sheet
     *            the sheet
     * @return the excel sheet
     */
    private ExcelSheet toExcelSheet(String sheet){
        if ("blank".equalsIgnoreCase(sheet)){
            return BLANK_SHEET;
        }
        ExcelSheet excelSheet = sheetDefinitions.get(sheet);
        Validate.notNull(excelSheet, "No sheet defintion found with name: " + sheet);
        return CloneUtil.cloneSheet(excelSheet);
    }
}
