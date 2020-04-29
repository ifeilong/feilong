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
package com.feilong.office.excel.reader;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.DataConvertorConfigurator;
import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.ReadStatus;
import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelCell;
import com.feilong.office.excel.utils.CellReferenceUtil;
import com.feilong.office.excel.utils.OgnlStack;

import ognl.OgnlRuntime;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class BlockReader{

    private static final Logger LOGGER                       = LoggerFactory.getLogger(BlockReader.class);

    /** The Constant STATUS_SETTING_ERROR. */
    private static final int    STATUS_SETTING_ERROR         = 2;

    /** The Constant STATUS_SYSTEM_ERROR. */
    private static final int    STATUS_SYSTEM_ERROR          = 5;

    /** The Constant STATUS_DATA_COLLECTION_ERROR. */
    private static final int    STATUS_DATA_COLLECTION_ERROR = 10;

    /**
     * Read loop block.
     *
     * @param workbook
     *            the wb
     * @param sheetNo
     *            the sheet no
     * @param excelBlock
     *            the block definition
     * @param stack
     *            the stack
     * @param readStatus
     *            the read status
     */
    static void readLoopBlock(Workbook workbook,int sheetNo,ExcelBlock excelBlock,OgnlStack stack,ReadStatus readStatus){
        //Loop Block will only care about row loop
        String dataName = excelBlock.getDataName();
        if (dataName == null || dataName.length() == 0){
            readStatus.setStatus(STATUS_SETTING_ERROR);
            readStatus.setMessage("dataName for block[" + excelBlock.toString() + "] is not set");
            return;
        }

        //---------------------------------------------------------------

        try{
            Object obj = stack.getValue(dataName);
            Collection dataList;
            if (obj == null){
                dataList = new ArrayList();
                stack.setValue(dataName, dataList);
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
            while (!BlockReaderLoopBreaker
                            .checkBreak(workbook.getSheetAt(sheetNo), startRow, excelBlock.getStartCol(), excelBlock.getBreakCondition())){
                Object value = readBlock(workbook, sheetNo, excelBlock, startRow, readStatus);
                dataList.add(value);
                startRow += step;
            }
        }catch (Exception e){
            LOGGER.error("", e);
            readStatus.setStatus(STATUS_SYSTEM_ERROR);
            readStatus.setMessage(e.getMessage());
        }
    }

    /**
     * Read Block in loop condition.
     *
     * @param workbook
     *            the wb
     * @param sheetNo
     *            the sheet no
     * @param blockDefinition
     *            the block definition
     * @param startRow
     *            the start row
     * @param readStatus
     *            the read status
     * @return the object
     * @throws Exception
     *             the exception
     */
    static Object readBlock(Workbook workbook,int sheetNo,ExcelBlock blockDefinition,int startRow,ReadStatus readStatus) throws Exception{
        Sheet sheet = workbook.getSheetAt(sheetNo);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        if (blockDefinition.getLoopClass() == null){
            Map<String, Object> result = new HashMap<>();

            for (ExcelCell cellDefinition : blockDefinition.getCells()){
                int rowOffSet = cellDefinition.getRow() - blockDefinition.getStartRow();
                Row row = sheet.getRow(startRow + rowOffSet);
                Cell cell = row == null ? null : row.getCell(cellDefinition.getCol());
                try{
                    Object value = CellValueGetter.get(cell, formulaEvaluator);
                    value = CellValueChecker.check(
                                    sheetNo,
                                    CellReferenceUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()),
                                    value,
                                    cellDefinition,
                                    getPropertyType(result, cellDefinition));
                    LOGGER.debug("{}[Checked]:{}", CellReferenceUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()), value);
                    result.put(cellDefinition.getDataName(), value);
                }catch (ExcelManipulateException e){
                    if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS){
                        readStatus.setStatus(STATUS_DATA_COLLECTION_ERROR);
                    }
                    readStatus.addException(e);
                }
            }
            return result;
        }

        //---------------------------------------------------------------
        Object result = blockDefinition.getLoopClass().newInstance();

        OgnlStack ognlStack = new OgnlStack(result);
        for (ExcelCell excelCell : blockDefinition.getCells()){
            int rowOffSet = excelCell.getRow() - blockDefinition.getStartRow();
            Row row = sheet.getRow(startRow + rowOffSet);
            int col = excelCell.getCol();

            Cell cell = row == null ? null : row.getCell(col);
            try{
                Object value = CellValueGetter.get(cell, formulaEvaluator);
                value = CellValueChecker.check(
                                sheetNo,
                                CellReferenceUtil.getCellIndex(startRow + rowOffSet, col),
                                value,
                                excelCell,
                                getPropertyType(result, excelCell));
                if (LOGGER.isTraceEnabled()){
                    LOGGER.trace("{}[Checked]:{}", CellReferenceUtil.getCellIndex(startRow + rowOffSet, col), value);
                }
                ognlStack.setValue(excelCell.getDataName(), value);
            }catch (ExcelManipulateException e){
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
     * @param blockDefinition
     *            the block definition
     * @param stack
     *            the stack
     * @param readStatus
     *            the read status
     */
    static void readSimpleBlock(Workbook workbook,int sheetNo,ExcelBlock blockDefinition,OgnlStack stack,ReadStatus readStatus){
        //Simple Block will only care about cells in these Block
        Sheet sheet = workbook.getSheetAt(sheetNo);
        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

        for (ExcelCell cellDefinition : blockDefinition.getCells()){
            Row row = sheet.getRow(cellDefinition.getRow());
            Cell cell = row == null ? null : row.getCell(cellDefinition.getCol());
            try{
                Object value = CellValueGetter.get(cell, formulaEvaluator);
                value = CellValueChecker.check(
                                sheetNo,
                                CellReferenceUtil.getCellIndex(cellDefinition.getRow(), cellDefinition.getCol()),
                                value,
                                cellDefinition,
                                getPropertyType(stack.peek(), cellDefinition));
                LOGGER.debug("{}[Checked]:{}", CellReferenceUtil.getCellIndex(cellDefinition.getRow(), cellDefinition.getCol()), value);
                stack.setValue(cellDefinition.getDataName(), value);
            }catch (ExcelManipulateException e){
                if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS){
                    readStatus.setStatus(STATUS_DATA_COLLECTION_ERROR);
                }
                readStatus.addException(e);
            }catch (Exception e){
                LOGGER.error("", e);
                readStatus.setStatus(STATUS_SYSTEM_ERROR);
                readStatus.setMessage(e.getMessage());
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 获得 property type.
     *
     * @param object
     *            the object
     * @param excelCell
     *            the cell definition
     * @return the property type
     * @throws Exception
     *             the exception
     */
    private static Class<? extends Object> getPropertyType(Object object,ExcelCell excelCell) throws Exception{
        Class<?> clazz = DataConvertorConfigurator.getInstance().getSupportedClass(excelCell.getType());
        if (clazz != null){
            return clazz;
        }
        return getPropertyType(object, excelCell.getDataName());
    }

    /**
     * 获得 property type.
     *
     * @param object
     *            the object
     * @param dataName
     *            the data name
     * @return the property type
     * @throws Exception
     *             the exception
     */
    private static Class<? extends Object> getPropertyType(Object object,String dataName) throws Exception{
        if (object instanceof Map){
            LOGGER.debug("getPropertyType for Map[{}] with Key {}.", object, dataName);
            if (object == null){
                return null;
            }
            Map<String, Object> map = (Map<String, Object>) object;
            int delim = dataName.indexOf('.');
            if (delim > 0){
                return getPropertyType(map.get(dataName.substring(0, delim)), dataName.substring(delim + 1));
            }
            return map.get(dataName).getClass();
        }

        //---------------------------------------------------------------
        LOGGER.trace("getPropertyType [{}] property {}.", object, dataName);

        return getPropertyTypeWithClass(object.getClass(), dataName);
    }

    //---------------------------------------------------------------

    /**
     * 获得 property type with class.
     *
     * @param clazz
     *            the clazz
     * @param dataName
     *            the data name
     * @return the property type with class
     * @throws Exception
     *             the exception
     */
    private static Class<? extends Object> getPropertyTypeWithClass(Class<? extends Object> clazz,String dataName) throws Exception{
        Validate.notNull(clazz, "clazz can't be null!");

        int delim = dataName.indexOf('.');
        if (delim < 0){
            PropertyDescriptor propertyDescriptor = OgnlRuntime.getPropertyDescriptor(clazz, dataName);
            Validate.notNull(propertyDescriptor, "propertyDescriptor can't be null!");

            return propertyDescriptor.getPropertyType();
        }
        PropertyDescriptor propertyDescriptor = OgnlRuntime.getPropertyDescriptor(clazz, dataName.substring(0, delim));
        Validate.notNull(propertyDescriptor, "propertyDescriptor can't be null!");

        Class<?> propertyType = propertyDescriptor.getPropertyType();
        return getPropertyTypeWithClass(propertyType, dataName.substring(delim + 1));
    }
}
