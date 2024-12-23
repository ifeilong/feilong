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
package com.feilong.excel.writer;

import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;
import static java.util.Collections.emptyMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.excel.ExcelDefinition;
import com.feilong.excel.definition.ExcelSheet;

/**
 * The Class StyleMapBuilder.
 */
class StyleMapBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleMapBuilder.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private StyleMapBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param workbook
     *            the workbook
     * @param excelDefinition
     *            the excel definition
     * @param excelSheetList
     *            the excel sheets
     * @return the map
     */
    static Map<String, CellStyle> build(Workbook workbook,ExcelDefinition excelDefinition,List<ExcelSheet> excelSheetList){
        Integer styleSheetPosition = excelDefinition.getStyleSheetPosition();
        if (null == styleSheetPosition){
            LOGGER.debug("ExcelDefinition styleSheetPosition is null, return empty styleMap");
            return emptyMap();
        }

        //---------------------------------------------------------------
        int excelSheetsSize = excelSheetList.size();
        Validate.isTrue(styleSheetPosition.intValue() >= excelSheetsSize, "Style Sheet can not be one Template Sheet.");

        //---------------------------------------------------------------
        long beginTimeMillis = System.currentTimeMillis();

        Map<String, CellStyle> styleMap = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(styleSheetPosition);

        for (int i = 0; i < excelSheetsSize; i++){
            ExcelSheet excelSheet = excelSheetList.get(i);
            styleMap.putAll(ExcelCellConditionStyleInitializer.init(sheet, excelSheet));
        }

        //---------------------------------------------------------------
        workbook.removeSheetAt(styleSheetPosition);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug(
                            "buildStyleMap use time: [{}],StyleMap size:[{}]",
                            formatDurationUseBeginTimeMillis(beginTimeMillis),
                            styleMap.size());
        }
        return styleMap;
    }
}
