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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.InputSource;

import com.feilong.office.excel.definition.ExcelManipulatorDefinition;
import com.feilong.office.excel.definition.ExcelSheet;

/**
 * A factory for creating ExcelManipulator objects.
 */
public class ExcelManipulatorFactory{

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
    @SuppressWarnings("unchecked")
    public void setConfig(String...configurations){
        try{
            for (String config : configurations){
                Digester digester = DigesterLoader.createDigester(
                                new InputSource(Thread.currentThread().getContextClassLoader().getResourceAsStream(RULE_FILE)));
                digester.setValidating(false);

                List<ExcelSheet> list = (List<ExcelSheet>) digester
                                .parse(Thread.currentThread().getContextClassLoader().getResourceAsStream(config));
                for (ExcelSheet es : list){
                    sheetDefinitions.put(es.getName(), es);
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
     * @param clazz
     *            the clazz
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(Integer styleSheetPosition,Class<? extends ExcelWriter> clazz,String[] sheets){
        ExcelWriter excelWriter = createExcelWriterInner(clazz, null, sheets);
        excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
        return excelWriter;
    }

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
        ExcelWriter excelWriter = createExcelWriterInner(null, null, sheets);
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
        ExcelWriter excelWriter = createExcelWriterInner(null, writeTemplateName, sheets);
        excelWriter.getDefinition().setStyleSheetPosition(styleSheetPosition);
        return excelWriter;
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param clazz
     *            the clazz
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(Class<? extends ExcelWriter> clazz,String[] sheets){
        ExcelWriter excelWriter = createExcelWriterInner(clazz, null, sheets);
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
        ExcelWriter excelWriter = createExcelWriterInner(null, writeTemplateName, sheets);
        return excelWriter;
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    public ExcelWriter createExcelWriter(String...sheets){
        return createExcelWriterInner(null, null, sheets);
    }

    //---------------------------------------------------------------

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param clazz
     *            the clazz
     * @param writeTemplateName
     *            the write template name
     * @param sheets
     *            the sheets
     * @return the excel writer
     */
    private ExcelWriter createExcelWriterInner(Class<? extends ExcelWriter> clazz,String writeTemplateName,String...sheets){
        ExcelWriter excelWriter = null;
        if (clazz == null){
            excelWriter = new DefaultExcelWriter();
        }else{
            try{
                excelWriter = clazz.newInstance();
            }catch (InstantiationException e){
                throw new RuntimeException("Initiate ExcelWriter[" + clazz + "] failure");
            }catch (IllegalAccessException e){
                throw new RuntimeException("Initiate ExcelWriter[" + clazz + "] failure");
            }
        }

        //---------------------------------------------------------------
        ExcelManipulatorDefinition definition = new ExcelManipulatorDefinition();
        for (String sheet : sheets){
            ExcelSheet sheetDefinition = getExcelSheet(sheet);
            definition.getExcelSheets().add(sheetDefinition);
        }
        excelWriter.setDefinition(definition);
        if (writeTemplateName != null){
            if (excelWriter instanceof DefaultExcelWriter){
                DefaultExcelWriter dew = (DefaultExcelWriter) excelWriter;
                dew.initBufferedTemplate(Thread.currentThread().getContextClassLoader().getResourceAsStream(writeTemplateName));
            }

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
        return createExcelReader(null, sheets);
    }

    /**
     * Creates a new ExcelManipulator object.
     *
     * @param clazz
     *            the clazz
     * @param sheets
     *            the sheets
     * @return the excel reader
     */
    public ExcelReader createExcelReader(Class<? extends ExcelReader> clazz,String...sheets){
        ExcelReader excelReader = null;
        if (clazz == null){
            excelReader = new DefaultExcelReader();
        }else{
            try{
                excelReader = clazz.newInstance();
            }catch (InstantiationException e){
                throw new RuntimeException("Initiate ExcelReader[" + clazz + "] failure");
            }catch (IllegalAccessException e){
                throw new RuntimeException("Initiate ExcelReader[" + clazz + "] failure");
            }
        }

        //---------------------------------------------------------------
        ExcelManipulatorDefinition definition = new ExcelManipulatorDefinition();
        for (String sheet : sheets){
            ExcelSheet sheetDefinition = getExcelSheet(sheet);
            definition.getExcelSheets().add(sheetDefinition);
        }
        excelReader.setDefinition(definition);
        return excelReader;
    }

    //---------------------------------------------------------------

    /**
     * Gets the excel sheet.
     *
     * @param sheet
     *            the sheet
     * @return the excel sheet
     */
    private ExcelSheet getExcelSheet(String sheet){
        if ("blank".equalsIgnoreCase(sheet)){
            return BLANK_SHEET;
        }
        ExcelSheet sheetDefinition = sheetDefinitions.get(sheet);
        if (sheetDefinition == null){
            throw new RuntimeException("No sheet defintion found with name: " + sheet);
        }
        return sheetDefinition.cloneSheet();
    }
}
