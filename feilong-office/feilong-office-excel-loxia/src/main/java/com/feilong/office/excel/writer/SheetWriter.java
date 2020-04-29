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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelSheet;
import com.feilong.office.excel.utils.CellReferenceUtil;
import com.feilong.office.excel.utils.OgnlStack;

/**
 * The Class SheetWriter.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class SheetWriter{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SheetWriter.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private SheetWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    /**
     * Write sheet.
     *
     * @param sheet
     *            the sheet
     * @param excelSheet
     *            the sheet definition
     * @param ognlStack
     *            the stack
     * @param styleMap
     *            the style map
     */
    public static void write(Sheet sheet,ExcelSheet excelSheet,OgnlStack ognlStack,Map<String, CellStyle> styleMap){
        Date beginDate = new Date();

        //---------------------------------------------------------------

        Workbook workbook = sheet.getWorkbook();
        String sheetName = excelSheet.getDisplayName();
        Object value = ognlStack.getValue("sheetName");

        if (value != null){
            sheetName = (String) value;
        }
        if (sheetName != null){
            workbook.setSheetName(workbook.getSheetIndex(sheet), sheetName);
        }

        //---------------------------------------------------------------
        Map<ExcelBlock, List<CellRangeAddress>> mergedRegions = new HashMap<>();
        List<ExcelBlock> sortedExcelBlocks = excelSheet.getSortedExcelBlocks();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++){
            CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
            int firstRow = cellRangeAddress.getFirstRow();
            int firstColumn = cellRangeAddress.getFirstColumn();

            int lastRow = cellRangeAddress.getLastRow();
            int lastColumn = cellRangeAddress.getLastColumn();

            //---------------------------------------------------------------
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace(
                                "Merged Region:[{}-{}]",
                                CellReferenceUtil.getCellIndex(firstRow, firstColumn),
                                CellReferenceUtil.getCellIndex(lastRow, lastColumn));
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

        //---------------------------------------------------------------

        for (ExcelBlock excelBlock : sortedExcelBlocks){
            Date BlockBeginDate = new Date();

            if (excelBlock.isLoop()){
                BlockWriter.writeLoopBlock(sheet, excelBlock, ognlStack, mergedRegions.get(excelBlock), styleMap);
            }else{
                BlockWriter.writeSimpleBlock(sheet, excelBlock, ognlStack, styleMap);
            }

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("writeBlock:[{}] use time: [{}]", excelBlock.getDataName(), formatDuration(BlockBeginDate));
            }
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("writeSheet [{}] use time: [{}]", excelSheet.getName(), formatDuration(beginDate));
        }

    }
}
