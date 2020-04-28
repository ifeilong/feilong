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

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelSheet;
import com.feilong.office.excel.utils.OgnlStack;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class WorkbookWriter{

    private static final Logger LOGGER               = LoggerFactory.getLogger(WorkbookWriter.class);

    //---------------------------------------------------------------

    /** The Constant STATUS_SETTING_ERROR. */
    private static final int    STATUS_SETTING_ERROR = 2;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private WorkbookWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    static WriteStatus writePerSheetNative(
                    Workbook workbook,
                    OutputStream outputStream,
                    List<Map<String, Object>> beansList,
                    ExcelManipulatorDefinition excelManipulatorDefinition){

        List<ExcelSheet> excelSheets = excelManipulatorDefinition.getExcelSheets();
        int numberOfSheets = workbook.getNumberOfSheets();
        int size = excelSheets.size();
        Validate.isTrue(
                        size > 0 && numberOfSheets >= size,
                        "No sheet definition found or Sheet Number in definition is more than number in template file.");
        //---------------------------------------------------------------

        Map<String, CellStyle> styleMap = new HashMap<>();
        if (excelManipulatorDefinition.getStyleSheetPosition() != null){
            if (excelManipulatorDefinition.getStyleSheetPosition().intValue() < size){
                return new WriteStatus(STATUS_SETTING_ERROR, "Style Sheet can not be one Template Sheet.");
            }
            ExcelCellConditionStyleIniter
                            .init(workbook.getSheetAt(excelManipulatorDefinition.getStyleSheetPosition()), excelSheets.get(0), styleMap);
            workbook.removeSheetAt(excelManipulatorDefinition.getStyleSheetPosition());
        }
        //remove sheets except the first one
        for (int i = numberOfSheets - 1; i > 0; i--){
            workbook.removeSheetAt(i);
        }
        for (int i = 0; i < beansList.size(); i++){
            Sheet newSheet = workbook.createSheet("Auto Generated Sheet " + i);
            ExcelUtil.copySheet(workbook.getSheetAt(0), newSheet);
            writeSheet(newSheet, excelSheets.iterator().next(), new OgnlStack(beansList.get(i)), styleMap);
        }

        //---------------------------------------------------------------
        //remove template sheet
        workbook.removeSheetAt(0);

        return pack(workbook, outputStream);
    }

    static WriteStatus writeNative(
                    Workbook workbook,
                    OutputStream outputStream,
                    Map<String, Object> beans,
                    ExcelManipulatorDefinition excelManipulatorDefinition){
        List<ExcelSheet> excelSheets = excelManipulatorDefinition.getExcelSheets();
        int size = excelSheets.size();
        int numberOfSheets = workbook.getNumberOfSheets();

        Validate.isTrue(
                        size > 0 && numberOfSheets >= size,
                        "No sheet definition found or Sheet Number in definition is more than number in template file.");

        //---------------------------------------------------------------
        Map<String, CellStyle> styleMap = new HashMap<>();
        Integer styleSheetPosition = excelManipulatorDefinition.getStyleSheetPosition();

        if (styleSheetPosition != null){
            if (styleSheetPosition.intValue() < size){
                return new WriteStatus(STATUS_SETTING_ERROR, "Style Sheet can not be one Template Sheet.");
            }
            for (int i = 0; i < size; i++){
                ExcelCellConditionStyleIniter.init(workbook.getSheetAt(styleSheetPosition), excelSheets.get(i), styleMap);
            }
            workbook.removeSheetAt(styleSheetPosition);
            LOGGER.debug("{} styles found", styleMap.keySet().size());
        }

        //---------------------------------------------------------------
        for (int i = 0; i < size; i++){
            writeSheet(workbook.getSheetAt(i), excelSheets.get(i), new OgnlStack(beans), styleMap);
        }
        return pack(workbook, outputStream);
    }

    /**
     * @param workbook
     * @param outputStream
     * @return
     * @throws UncheckedIOException
     * @since 3.0.0
     */
    private static WriteStatus pack(Workbook workbook,OutputStream outputStream) throws UncheckedIOException{
        FormulaEvaluatorUtil.reCalculate(workbook);
        workbook.setActiveSheet(0);
        try{
            workbook.write(outputStream);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
        return new WriteStatus(WriteStatus.STATUS_SUCCESS);
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
    private static void writeSheet(Sheet sheet,ExcelSheet excelSheet,OgnlStack ognlStack,Map<String, CellStyle> styleMap){
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

            LOGGER.debug(
                            "Merged Region:[{}-{}]",
                            ExcelUtil.getCellIndex(firstRow, firstColumn),
                            ExcelUtil.getCellIndex(lastRow, lastColumn));

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
            if (excelBlock.isLoop()){
                BlockWriter.writeLoopBlock(sheet, excelBlock, ognlStack, mergedRegions.get(excelBlock), styleMap);
            }else{
                BlockWriter.writeSimpleBlock(sheet, excelBlock, ognlStack, styleMap);
            }
        }
    }

}
