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

import com.feilong.excel.definition.ExcelSheet;

/**
 * A factory for creating ExcelManipulator objects.
 * 
 * @see DefaultExcelWriter
 * @see DefaultExcelReader
 * @deprecated 将会删除
 */
@Deprecated
public class ExcelManipulatorFactory{

    /** key是sheet Name. */
    private Map<String, ExcelSheet> sheetDefinitions = new HashMap<>();

    //---------------------------------------------------------------

    /**
     * Sets the config.
     *
     * @param sheetDefinitionLocations
     *            the new config
     */
    @Deprecated
    public void setConfig(String...sheetDefinitionLocations){
        sheetDefinitions = ExcelSheetMapBuilder.build(sheetDefinitionLocations);
    }

    //---------------------------------------------------------------
    //
    //    /**
    //     * Creates a new ExcelManipulator object.
    //     *
    //     * @param sheetNames
    //     *            the sheet names
    //     * @return the excel writer
    //     */
    //    public ExcelWriter createExcelWriter(String...sheetNames){
    //        return createExcelWriter(null, null, sheetNames);
    //    }

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
    public ExcelWriter createExcelWriter(Integer styleSheetPosition,String writeTemplateName,String...sheetNames){
        ExcelDefinition excelDefinition = ExcelDefinitionBuilder.build(sheetDefinitions, sheetNames);
        if (null != styleSheetPosition){
            excelDefinition.setStyleSheetPosition(styleSheetPosition);
        }

        //---------------------------------------------------------------
        ExcelWriter excelWriter = new DefaultExcelWriter();
        excelWriter.setDefinition(excelDefinition);

        //---------------------------------------------------------------
        if (writeTemplateName != null){
            DefaultExcelWriter defaultExcelWriter = (DefaultExcelWriter) excelWriter;
            defaultExcelWriter.initBufferedTemplate(Thread.currentThread().getContextClassLoader().getResourceAsStream(writeTemplateName));
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
        excelReader.setDefinition(ExcelDefinitionBuilder.build(sheetDefinitions, sheetNames));
        return excelReader;
    }

    /**
     * @param sheetDefinitions
     *            the sheetDefinitions to set
     */
    public void setSheetDefinitions(Map<String, ExcelSheet> sheetDefinitions){
        this.sheetDefinitions = sheetDefinitions;
    }

}
