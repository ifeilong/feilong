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
package com.feilong.office.excel.read;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.io.FileUtil;
import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.office.excel.ExcelReaderUtil;

/**
 * 解析 excel.
 *
 * @version 2010-7-7 上午11:44:53
 */
public class ExcelReaderUtil2Test2{

    private static final Logger LOGGER     = LoggerFactory.getLogger(ExcelReaderUtil2Test2.class);

    private final String        EXCEL_PATH = "/Users/feilong/Downloads/三国攻略/三国-玩家.xlsx";

    private final int           sheetIndex = 0;

    @Test
    public void parse() throws IOException{
        InputStream inputStream = FileUtil.getFileInputStream(EXCEL_PATH);

        // 2013
        Workbook workbook = new XSSFWorkbook(inputStream);

        Sheet sheet = workbook.getSheetAt(sheetIndex);
        Map<String, Object> map = ExcelReaderUtil.getSheetMapForLog(sheet);
        LOGGER.debug(JsonUtil.format(map));

        //---------------------------------------------------------------
        Row titleRow = sheet.getRow(0);

        Map<String, Object> rowMapLog = ExcelReaderUtil.getRowMapForLog(titleRow);
        LOGGER.debug(JsonUtil.format(rowMapLog));

        //---------------------------------------------------------------
        Cell cell = titleRow.getCell(0);
        String cellValue = ExcelReaderUtil.getCellValue(cell);

        Map<String, Object> cellMapForLog = ExcelReaderUtil.getCellMapForLog(cell);
        //---------------------------------------------------------------

        LOGGER.debug("the param cellMapForLog:{}", cellMapForLog);

        //---------------------------------------------------------------

        LOGGER.debug("getCellMapForLog0:{}", JsonUtil.format(cellMapForLog));
        LOGGER.debug("the param cellValue0:{}", cellValue);
    }
}
