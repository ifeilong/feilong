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
package com.feilong.excel.reader;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.excel.util.CellReferenceUtil.getCellRef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.feilong.excel.ExcelException;
import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.definition.LoopBreakCondition;
import com.feilong.excel.util.CellReferenceUtil;
import com.feilong.lib.excel.ognl.OgnlStack;

/**
 * The Class BlockReader.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@lombok.extern.slf4j.Slf4j
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
class BlockReader{

    /** The Constant STATUS_SETTING_ERROR. */
    private static final int STATUS_SETTING_ERROR         = 2;

    /** The Constant STATUS_SYSTEM_ERROR. */
    private static final int STATUS_SYSTEM_ERROR          = 5;

    /** The Constant STATUS_DATA_COLLECTION_ERROR. */
    private static final int STATUS_DATA_COLLECTION_ERROR = 10;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private BlockReader(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Read loop block.
     *
     * @param workbook
     *            the wb
     * @param sheetNo
     *            the sheet no
     * @param excelBlock
     *            the block definition
     * @param ognlStack
     *            the stack
     * @param readStatus
     *            the read status
     */
    static void readLoopBlock(Workbook workbook,int sheetNo,ExcelBlock excelBlock,OgnlStack ognlStack,ReadStatus readStatus){
        //Loop Block will only care about row loop
        String dataName = excelBlock.getDataName();
        if (isNullOrEmpty(dataName)){
            readStatus.setStatus(STATUS_SETTING_ERROR);
            readStatus.setMessage("dataName for block[" + excelBlock.toString() + "] is not set");
            return;
        }

        //---------------------------------------------------------------

        try{
            Object obj = ognlStack.getValue(dataName);
            Collection dataList;
            if (obj == null){
                dataList = new ArrayList<>();
                ognlStack.setValue(dataName, dataList);
            }else if (!(obj instanceof Collection)){
                readStatus.setStatus(STATUS_SETTING_ERROR);
                readStatus.setMessage("Property " + dataName + " is not a Collection");
                return;
            }else{
                dataList = (Collection) obj;
            }

            //---------------------------------------------------------------

            int startRow = excelBlock.getStartRow();
            int step = excelBlock.getEndRow() - excelBlock.getStartRow() + 1;

            LoopBreakCondition loopBreakCondition = excelBlock.getBreakCondition();
            int startCol = excelBlock.getStartCol();
            while (!BlockReaderLoopBreaker.checkBreak(workbook.getSheetAt(sheetNo), startRow, startCol, loopBreakCondition)){
                Object value = readBlock(workbook, sheetNo, excelBlock, startRow, readStatus);
                dataList.add(value);
                startRow += step;
            }
        }catch (Exception e){
            log.error("", e);
            readStatus.setStatus(STATUS_SYSTEM_ERROR);
            readStatus.setMessage(e.getMessage());
        }
    }

    //---------------------------------------------------------------
    static Object readBlock(Workbook workbook,int sheetNo,ExcelBlock excelBlock,int startRow,ReadStatus readStatus) throws Exception{
        Sheet sheet = workbook.getSheetAt(sheetNo);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        if (excelBlock.getLoopClass() == null){
            Map<String, Object> result = new HashMap<>();

            for (ExcelCell cellDefinition : excelBlock.getCells()){
                int rowOffSet = cellDefinition.getRow() - excelBlock.getStartRow();
                Row row = sheet.getRow(startRow + rowOffSet);
                Cell cell = row == null ? null : row.getCell(cellDefinition.getCol());
                try{
                    Object value = CellValueGetter.get(cell, formulaEvaluator);
                    value = CellValueConverter.convert(
                                    sheetNo,
                                    getCellRef(startRow + rowOffSet, cellDefinition.getCol()),
                                    value,
                                    cellDefinition,
                                    PropertyTypeDetector.detect(result, cellDefinition));

                    //---------------------------------------------------------------
                    if (log.isDebugEnabled()){
                        log.debug("{}[Checked]:{}", getCellRef(startRow + rowOffSet, cellDefinition.getCol()), value);
                    }
                    //---------------------------------------------------------------
                    result.put(cellDefinition.getDataName(), value);
                }catch (ExcelException e){
                    if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS){
                        readStatus.setStatus(STATUS_DATA_COLLECTION_ERROR);
                    }
                    readStatus.addException(e);
                }
            }
            return result;
        }

        //---------------------------------------------------------------
        Object result = excelBlock.getLoopClass().newInstance();

        OgnlStack ognlStack = new OgnlStack(result);
        for (ExcelCell excelCell : excelBlock.getCells()){
            int rowOffSet = excelCell.getRow() - excelBlock.getStartRow();
            Row row = sheet.getRow(startRow + rowOffSet);
            int col = excelCell.getCol();

            Cell cell = row == null ? null : row.getCell(col);
            try{
                Object value = CellValueGetter.get(cell, formulaEvaluator);
                value = CellValueConverter.convert(
                                sheetNo,
                                CellReferenceUtil.getCellRef(startRow + rowOffSet, col),
                                value,
                                excelCell,
                                PropertyTypeDetector.detect(result, excelCell));
                if (log.isTraceEnabled()){
                    log.trace("{}[Checked]:{}", CellReferenceUtil.getCellRef(startRow + rowOffSet, col), value);
                }
                ognlStack.setValue(excelCell.getDataName(), value);
            }catch (ExcelException e){
                if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS){
                    readStatus.setStatus(STATUS_DATA_COLLECTION_ERROR);
                }
                readStatus.addException(e);
            }
        }
        return result;
    }

    /**
     * Read simple block.
     *
     * @param workbook
     *            the wb
     * @param sheetNo
     *            the sheet no
     * @param excelBlock
     *            the block definition
     * @param ognlStack
     *            the stack
     * @param readStatus
     *            the read status
     */
    static void readSimpleBlock(Workbook workbook,int sheetNo,ExcelBlock excelBlock,OgnlStack ognlStack,ReadStatus readStatus){
        //Simple Block will only care about cells in these Block
        Sheet sheet = workbook.getSheetAt(sheetNo);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        //---------------------------------------------------------------
        for (ExcelCell excelCell : excelBlock.getCells()){
            Row row = sheet.getRow(excelCell.getRow());
            Cell cell = row == null ? null : row.getCell(excelCell.getCol());
            try{
                Object value = CellValueGetter.get(cell, formulaEvaluator);

                Class<?> propertyType = PropertyTypeDetector.detect(ognlStack.peek(), excelCell);
                value = CellValueConverter
                                .convert(sheetNo, getCellRef(excelCell.getRow(), excelCell.getCol()), value, excelCell, propertyType);

                //---------------------------------------------------------------
                if (log.isDebugEnabled()){
                    log.debug("{}[Checked]:{}", CellReferenceUtil.getCellRef(excelCell.getRow(), excelCell.getCol()), value);
                }
                //---------------------------------------------------------------
                ognlStack.setValue(excelCell.getDataName(), value);
            }catch (ExcelException e){
                if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS){
                    readStatus.setStatus(STATUS_DATA_COLLECTION_ERROR);
                }
                readStatus.addException(e);
            }catch (Exception e){
                log.error("", e);
                readStatus.setStatus(STATUS_SYSTEM_ERROR);
                readStatus.setMessage(e.getMessage());
            }
        }
    }

}
