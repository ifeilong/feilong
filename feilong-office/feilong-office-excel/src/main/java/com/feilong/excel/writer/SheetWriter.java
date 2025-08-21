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
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.excel.util.CellReferenceUtil.getCellRef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.json.JsonUtil;
import com.feilong.lib.excel.ognl.OgnlStack;

@lombok.extern.slf4j.Slf4j
class SheetWriter{

    private static final String SHEET_NAME = "sheetName";
    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SheetWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    public static void write(Sheet sheet,ExcelSheet excelSheet,Map<String, CellStyle> styleMap,Object ognlData){
        long beginTimeMillis = System.currentTimeMillis();

        OgnlStack ognlStack = new OgnlStack(ognlData);
        setSheetName(sheet, excelSheet, ognlStack);
        //---------------------------------------------------------------
        List<ExcelBlock> sortedExcelBlocks = excelSheet.getSortedExcelBlocks();
        Map<ExcelBlock, List<CellRangeAddress>> mergedRegionsMap = buildMergedRegions(sheet, sortedExcelBlocks);
        //---------------------------------------------------------------
        for (ExcelBlock excelBlock : sortedExcelBlocks){
            write(sheet, excelSheet, excelBlock, styleMap, mergedRegionsMap, ognlStack);
        }
        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("writeSheet:[{}], use time: [{}]", excelSheet.getName(), formatDurationUseBeginTimeMillis(beginTimeMillis));
        }
    }

    private static void write(
                    Sheet sheet,
                    ExcelSheet excelSheet,
                    ExcelBlock excelBlock,

                    Map<String, CellStyle> styleMap,
                    Map<ExcelBlock, List<CellRangeAddress>> mergedRegionsMap,
                    OgnlStack ognlStack){
        if (log.isDebugEnabled()){
            log.debug("excelBlock:{}", JsonUtil.toString(excelBlock));
        }
        //---------------------------------------------------------------
        long beginTimeMillis = System.currentTimeMillis();
        //循环
        if (excelBlock.isLoop()){
            List<CellRangeAddress> cellRangeAddressList = mergedRegionsMap.get(excelBlock);
            boolean isHorizontal = excelBlock.getDirection().equalsIgnoreCase(ExcelBlock.LOOP_DIRECTION_HORIZONAL);
            if (isHorizontal){
                BlockLoopHorizontalWriter.write(sheet, excelBlock, ognlStack, cellRangeAddressList, styleMap);
            }else{
                BlockLoopVerticalWriter.write(sheet, excelBlock, ognlStack, cellRangeAddressList, styleMap);
            }
        }else{
            BlockSimpleWriter.write(sheet, excelBlock, ognlStack, styleMap);
        }
        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            String pattern = "write sheet block:[{}]-[{}], use time: [{}]";
            log.debug(pattern, excelSheet.getName(), excelBlock.getDataName(), formatDurationUseBeginTimeMillis(beginTimeMillis));
        }
    }

    private static Map<ExcelBlock, List<CellRangeAddress>> buildMergedRegions(Sheet sheet,List<ExcelBlock> sortedExcelBlocks){
        int numMergedRegions = sheet.getNumMergedRegions();

        Map<ExcelBlock, List<CellRangeAddress>> mergedRegions = new HashMap<>();
        for (int i = 0; i < numMergedRegions; i++){
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            int firstRow = cellRangeAddress.getFirstRow();
            int firstColumn = cellRangeAddress.getFirstColumn();

            int lastRow = cellRangeAddress.getLastRow();
            int lastColumn = cellRangeAddress.getLastColumn();

            //---------------------------------------------------------------
            if (log.isTraceEnabled()){
                log.trace("Merged Region:[{}-{}]", getCellRef(firstRow, firstColumn), getCellRef(lastRow, lastColumn));
            }

            //---------------------------------------------------------------
            for (ExcelBlock blockDefinition : sortedExcelBlocks){
                int startRow = blockDefinition.getStartRow();
                int endRow = blockDefinition.getEndRow();
                int startCol = blockDefinition.getStartCol();
                int endCol = blockDefinition.getEndCol();

                if (firstRow >= startRow && firstColumn >= startCol && lastRow <= endRow && lastColumn <= endCol){
                    List<CellRangeAddress> cellRangeAddressList = mergedRegions.get(blockDefinition);
                    if (cellRangeAddressList == null){
                        cellRangeAddressList = new ArrayList<>();
                        mergedRegions.put(blockDefinition, cellRangeAddressList);
                    }
                    cellRangeAddressList.add(cellRangeAddress);
                }
            }
        }
        return mergedRegions;
    }

    private static void setSheetName(Sheet sheet,ExcelSheet excelSheet,OgnlStack ognlStack){
        String sheetName = getSheetName(excelSheet, ognlStack);
        if (sheetName != null){
            Workbook workbook = sheet.getWorkbook();
            int sheetIndex = workbook.getSheetIndex(sheet);

            log.debug("set workbook sheet:[{}] name:[{}]", sheetIndex, sheetName);
            workbook.setSheetName(sheetIndex, sheetName);
        }
    }

    private static String getSheetName(ExcelSheet excelSheet,OgnlStack ognlStack){
        String sheetNameStack = defaultIfNullOrEmpty(//
                        (String) ognlStack.getValue(SHEET_NAME),
                        EMPTY);
        String sheetDisplayName = excelSheet.getDisplayName();

        log.debug("excelSheet DisplayName:[{}],stack value:[{}]", sheetDisplayName, sheetNameStack);
        return defaultIfNullOrEmpty(sheetNameStack, sheetDisplayName);
    }
}
