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
package com.feilong.office.excel.writer;

import static com.feilong.core.date.DateUtil.formatDuration;
import static java.util.Collections.emptyMap;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.ExcelDefinition;
import com.feilong.office.excel.definition.ExcelSheet;

/**
 * The Class StyleMapBuilder.
 */
class StyleMapBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StyleMapBuilder.class);

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
            LOGGER.debug("ExcelDefinition styleSheetPosition is null, renturn empty styleMap");
            return emptyMap();
        }

        //---------------------------------------------------------------
        int excelSheetsSize = excelSheetList.size();
        Validate.isTrue(styleSheetPosition.intValue() >= excelSheetsSize, "Style Sheet can not be one Template Sheet.");

        //---------------------------------------------------------------
        Date beginDate = new Date();

        Map<String, CellStyle> styleMap = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(styleSheetPosition);

        for (int i = 0; i < excelSheetsSize; i++){
            ExcelSheet excelSheet = excelSheetList.get(i);
            styleMap.putAll(ExcelCellConditionStyleInitializer.init(sheet, excelSheet));
        }

        //---------------------------------------------------------------
        workbook.removeSheetAt(styleSheetPosition);

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("buildStyleMap use time: [{}],StyleMap size:[{}]", formatDuration(beginDate), styleMap.size());
        }
        return styleMap;
    }
}
