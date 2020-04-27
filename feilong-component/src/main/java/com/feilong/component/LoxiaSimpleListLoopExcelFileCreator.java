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
package com.feilong.component;

import static com.feilong.core.bean.ConvertUtil.toMap;

import java.util.List;
import java.util.Map;

import com.feilong.context.Data;
import com.feilong.context.filecreator.ListDataFileCreator;
import com.feilong.office.excel.LoxiaExcelWriteUtil;

/**
 * The Class LoxiaSimpleListLoopExcelFileCreator.
 *
 * @param <T>
 *            the generic type
 * @since 2.1.0
 */
public class LoxiaSimpleListLoopExcelFileCreator<T extends Data> implements ListDataFileCreator<T>{

    /**
     * excel模板location.
     * 
     * <ol>
     * <li>支持fully qualified URLs(完全合格的url),比如 "file:/Users/feilong/workspace/loxia/TradeData/TradeData-list-export.xlsx".</li>
     * <li>支持classpath pseudo-URLs(伪url), 比如 "classpath:loxia/TradeData/TradeData-list-export.xlsx".</li>
     * <li>支持relative file paths(相对路径), 比如 "WEB-INF/TradeData-list-export.xlsx".</li>
     * </ol>
     */
    private String excelTemplateLocation;

    //---------------------------------------------------------------

    /** The xml sheet configurations. */
    private String xmlSheetConfigurations;

    /** The sheet name. */
    private String sheetName = "sheet";

    /** The data name. */
    private String dataName  = "list";

    //---------------------------------------------------------------

    /** The output file path expression. */
    private String outputFilePathExpression;

    //---------------------------------------------------------------

    /**
     * 创建.
     *
     * @param list
     *            the list
     * @return the string
     */
    @Override
    public String create(List<T> list){
        Map<String, Object> beans = toMap(dataName, (Object) list);

        //---------------------------------------------------------------
        String filePath = SpelUtil.getTemplateValue(outputFilePathExpression);
        LoxiaExcelWriteUtil.write(excelTemplateLocation, xmlSheetConfigurations, sheetName, beans, filePath);

        return filePath;
    }

    //---------------------------------------------------------------

    /**
     * Sets the xml sheet configurations.
     *
     * @param xmlSheetConfigurations
     *            the xmlSheetConfigurations to set
     */
    public void setXmlSheetConfigurations(String xmlSheetConfigurations){
        this.xmlSheetConfigurations = xmlSheetConfigurations;
    }

    /**
     * Sets the sheet name.
     *
     * @param sheetName
     *            the sheetName to set
     */
    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

    /**
     * Sets the data name.
     *
     * @param dataName
     *            the dataName to set
     */
    public void setDataName(String dataName){
        this.dataName = dataName;
    }

    /**
     * Sets the output file path expression.
     *
     * @param outputFilePathExpression
     *            the outputFilePathExpression to set
     */
    public void setOutputFilePathExpression(String outputFilePathExpression){
        this.outputFilePathExpression = outputFilePathExpression;
    }

    /**
     * excel模板location.
     * 
     * <ol>
     * <li>支持fully qualified URLs(完全合格的url),比如 "file:/Users/feilong/workspace/loxia/TradeData/TradeData-list-export.xlsx".</li>
     * <li>支持classpath pseudo-URLs(伪url), 比如 "classpath:loxia/TradeData/TradeData-list-export.xlsx".</li>
     * <li>支持relative file paths(相对路径), 比如 "WEB-INF/TradeData-list-export.xlsx".</li>
     * </ol>
     *
     * @param excelTemplateLocation
     *            the excelTemplateLocation to set
     */
    public void setExcelTemplateLocation(String excelTemplateLocation){
        this.excelTemplateLocation = excelTemplateLocation;
    }
}
