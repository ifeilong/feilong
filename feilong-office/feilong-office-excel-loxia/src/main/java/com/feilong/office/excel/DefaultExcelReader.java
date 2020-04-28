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

import static com.feilong.office.excel.ExcelManipulateExceptionBuilder.build;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.convertor.DataConvertor;
import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelCell;
import com.feilong.office.excel.definition.ExcelSheet;
import com.feilong.office.excel.definition.LoopBreakCondition;
import com.feilong.office.excel.utils.OgnlStack;

import ognl.OgnlRuntime;

/**
 * The Class DefaultExcelReader.
 */
public class DefaultExcelReader implements ExcelReader{

    /** The Constant log. */
    private static final Logger        LOGGER                       = LoggerFactory.getLogger(DefaultExcelReader.class);

    private static final int           UNSUPPORTING_DATA_TYPE       = 2;

    //---------------------------------------------------------------

    /** The Constant STATUS_READ_FILE_ERROR. */
    private static final int           STATUS_READ_FILE_ERROR       = 1;

    /** The Constant STATUS_SETTING_ERROR. */
    private static final int           STATUS_SETTING_ERROR         = 2;

    /** The Constant STATUS_SYSTEM_ERROR. */
    private static final int           STATUS_SYSTEM_ERROR          = 5;

    /** The Constant STATUS_DATA_COLLECTION_ERROR. */
    private static final int           STATUS_DATA_COLLECTION_ERROR = 10;
    //---------------------------------------------------------------

    /** The definition. */
    private ExcelManipulatorDefinition definition;

    /** The skip errors. */
    private boolean                    skipErrors                   = true;

    //---------------------------------------------------------------

    /**
     * Read all.
     *
     * @param is
     *            the is
     * @param beans
     *            the beans
     * @return the read status
     */
    @Override
    public ReadStatus readAll(InputStream is,Map<String, Object> beans){
        ReadStatus readStatus = new DefaultReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);

        try (Workbook wb = WorkbookFactory.create(is)){
            if (definition.getExcelSheets().size() == 0 || wb.getNumberOfSheets() < definition.getExcelSheets().size()){
                readStatus.setStatus(STATUS_SETTING_ERROR);
                readStatus.setMessage("No sheet definition found or Sheet Number in definition is more than number in file.");
            }else{
                OgnlStack stack = new OgnlStack(beans);
                for (int i = 0; i < definition.getExcelSheets().size(); i++){
                    readSheet(wb, i, definition.getExcelSheets().get(i), stack, readStatus);
                }
            }
        }catch (IOException e){
            readStatus.setStatus(STATUS_READ_FILE_ERROR);
        }
        return readStatus;
    }

    /**
     * Read all per sheet.
     *
     * @param is
     *            the is
     * @param beans
     *            the beans
     * @return the read status
     */
    @Override
    public ReadStatus readAllPerSheet(InputStream is,Map<String, Object> beans){
        ReadStatus readStatus = new DefaultReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);

        try (Workbook wb = WorkbookFactory.create(is)){
            if (definition.getExcelSheets().size() == 0){
                readStatus.setStatus(STATUS_SETTING_ERROR);
                readStatus.setMessage("No sheet definition found");
            }else{
                //Only first ExcelSheet Definition will be used
                ExcelSheet sheetDefinition = definition.getExcelSheets().iterator().next();

                Map<String, List<Object>> cacheMap = new HashMap<>();
                for (String key : beans.keySet()){
                    if (beans.get(key) != null){
                        cacheMap.put(key, new ArrayList<>());
                    }
                }
                for (int i = 0; i < wb.getNumberOfSheets(); i++){
                    Map<String, Object> clonedBeans = cloneMap(beans);
                    readSheet(wb, i, sheetDefinition, new OgnlStack(clonedBeans), readStatus);
                    for (String key : clonedBeans.keySet()){
                        cacheMap.get(key).add(clonedBeans.get(key));
                    }
                }
                for (String key : beans.keySet()){
                    if (cacheMap.containsKey(key)){
                        beans.put(key, cacheMap.get(key));
                    }else{
                        beans.put(key, null);
                    }
                }
            }
        }catch (IOException e){
            readStatus.setStatus(STATUS_READ_FILE_ERROR);
        }catch (InstantiationException e){
            LOGGER.error("", e);
            readStatus.setStatus(STATUS_SYSTEM_ERROR);
            readStatus.setMessage("New Instance Error");
        }catch (IllegalAccessException e){
            LOGGER.error("", e);
            readStatus.setStatus(STATUS_SYSTEM_ERROR);
            readStatus.setMessage("New Instance Error");
        }
        return readStatus;
    }

    //---------------------------------------------------------------

    @Override
    public ReadStatus readSheet(InputStream is,int sheetNo,Map<String, Object> beans){
        ReadStatus readStatus = new DefaultReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);

        OgnlStack ognlStack = new OgnlStack(beans);
        try (Workbook workbook = WorkbookFactory.create(is)){
            readSheet(workbook, sheetNo, definition.getExcelSheets().iterator().next(), ognlStack, readStatus);
        }catch (IOException e){
            readStatus.setStatus(STATUS_READ_FILE_ERROR);
        }
        return readStatus;
    }

    /**
     * Clone map.
     *
     * @param map
     *            the map
     * @return the map
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    private Map<String, Object> cloneMap(Map<String, Object> map) throws InstantiationException,IllegalAccessException{
        Map<String, Object> result = map.getClass().newInstance();
        for (String key : map.keySet()){
            Object obj = map.get(key);
            if (obj == null){
                continue;
            }
            if (obj instanceof Map){
                result.put(key, cloneMap((Map<String, Object>) obj));
            }else{
                result.put(key, obj.getClass().newInstance());
            }
        }
        return result;
    }

    //---------------------------------------------------------------

    /**
     * Read sheet.
     *
     * @param wb
     *            the wb
     * @param sheetNo
     *            the sheet no
     * @param sheetDefinition
     *            the sheet definition
     * @param stack
     *            the stack
     * @param readStatus
     *            the read status
     */
    private void readSheet(Workbook wb,int sheetNo,ExcelSheet sheetDefinition,OgnlStack stack,ReadStatus readStatus){
        //In Read Operation only the first loopBlock will be read
        int loopBlock = 0;

        int status = readStatus.getStatus();
        for (ExcelBlock blockDefinition : sheetDefinition.getExcelBlocks()){
            if (((skipErrors && status == STATUS_DATA_COLLECTION_ERROR) || status == ReadStatus.STATUS_SUCCESS)
                            && (loopBlock < 1 || !blockDefinition.isLoop())){
                if (blockDefinition.isLoop()){
                    loopBlock++;
                    readLoopBlock(wb, sheetNo, blockDefinition, stack, readStatus);
                }else{
                    readSimpleBlock(wb, sheetNo, blockDefinition, stack, readStatus);
                }
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Read simple block.
     *
     * @param wb
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
    private void readSimpleBlock(Workbook wb,int sheetNo,ExcelBlock blockDefinition,OgnlStack stack,ReadStatus readStatus){
        //Simple Block will only care about cells in these Block
        Sheet sheet = wb.getSheetAt(sheetNo);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        for (ExcelCell cellDefinition : blockDefinition.getCells()){
            Row row = sheet.getRow(cellDefinition.getRow());
            Cell cell = row == null ? null : row.getCell(cellDefinition.getCol());
            try{
                Object value = getCellValue(cell, evaluator);
                value = checkValue(
                                sheetNo,
                                ExcelUtil.getCellIndex(cellDefinition.getRow(), cellDefinition.getCol()),
                                value,
                                cellDefinition,
                                getPropertyType(stack.peek(), cellDefinition));
                LOGGER.debug("{}[Checked]:{}", ExcelUtil.getCellIndex(cellDefinition.getRow(), cellDefinition.getCol()), value);
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
     * Read loop block.
     *
     * @param wb
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
    private void readLoopBlock(Workbook wb,int sheetNo,ExcelBlock blockDefinition,OgnlStack stack,ReadStatus readStatus){
        //Loop Block will only care about row loop
        String dataName = blockDefinition.getDataName();
        if (dataName == null || dataName.length() == 0){
            readStatus.setStatus(STATUS_SETTING_ERROR);
            readStatus.setMessage("dataName for block[" + blockDefinition.toString() + "] is not set");
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

            int startRow = blockDefinition.getStartRow();
            int step = blockDefinition.getEndRow() - blockDefinition.getStartRow() + 1;
            while (!checkBreak(wb.getSheetAt(sheetNo), startRow, blockDefinition.getStartCol(), blockDefinition.getBreakCondition())){
                Object value = readBlock(wb, sheetNo, blockDefinition, startRow, readStatus);
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
     * @param wb
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
    private Object readBlock(Workbook wb,int sheetNo,ExcelBlock blockDefinition,int startRow,ReadStatus readStatus) throws Exception{
        Sheet sheet = wb.getSheetAt(sheetNo);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        if (blockDefinition.getLoopClass() == null){
            Map<String, Object> result = new HashMap<>();

            for (ExcelCell cellDefinition : blockDefinition.getCells()){
                int rowOffSet = cellDefinition.getRow() - blockDefinition.getStartRow();
                Row row = sheet.getRow(startRow + rowOffSet);
                Cell cell = row == null ? null : row.getCell(cellDefinition.getCol());
                try{
                    Object value = getCellValue(cell, evaluator);
                    value = checkValue(
                                    sheetNo,
                                    ExcelUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()),
                                    value,
                                    cellDefinition,
                                    getPropertyType(result, cellDefinition));
                    LOGGER.debug("{}[Checked]:{}", ExcelUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()), value);
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
        for (ExcelCell cellDefinition : blockDefinition.getCells()){
            int rowOffSet = cellDefinition.getRow() - blockDefinition.getStartRow();
            Row row = sheet.getRow(startRow + rowOffSet);
            Cell cell = row == null ? null : row.getCell(cellDefinition.getCol());
            try{
                Object value = getCellValue(cell, evaluator);
                value = checkValue(
                                sheetNo,
                                ExcelUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()),
                                value,
                                cellDefinition,
                                getPropertyType(result, cellDefinition));
                LOGGER.debug("{}[Checked]:{}", ExcelUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()), value);
                ognlStack.setValue(cellDefinition.getDataName(), value);
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
     * Check value.
     *
     * @param sheetNo
     *            the sheet no
     * @param cellIndex
     *            the cell index
     * @param value
     *            the value
     * @param cellDefinition
     *            the cell definition
     * @param clazz
     *            the clazz
     * @return the object
     * @throws ExcelManipulateException
     *             the excel manipulate exception
     */
    private static Object checkValue(int sheetNo,String cellIndex,Object value,ExcelCell cellDefinition,Class<? extends Object> clazz)
                    throws ExcelManipulateException{
        DataConvertor<?> dataConvertor = DataConvertorConfigurator.getInstance().getConvertor(clazz);
        //primitive type should be mandatory
        if (clazz.isPrimitive()){
            cellDefinition.setMandatory(true);
        }
        if (dataConvertor == null){
            throw build(value, sheetNo, cellIndex, cellDefinition, UNSUPPORTING_DATA_TYPE);
        }
        return dataConvertor.convert(value, sheetNo, cellIndex, cellDefinition);
    }

    /**
     * Gets the cell value.
     *
     * @param cell
     *            the cell
     * @param evaluator
     *            the evaluator
     * @return the cell value
     * @throws ExcelManipulateException
     *             the excel manipulate exception
     */
    private static Object getCellValue(Cell cell,FormulaEvaluator evaluator) throws ExcelManipulateException{
        if (cell == null){
            return null;
        }
        Object value = null;
        CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null){
            LOGGER.debug("{}: null", ExcelUtil.getCellIndex(cell.getRowIndex(), cell.getColumnIndex()));
            return null;
        }
        switch (cellValue.getCellType()) {
            case BLANK:
            case ERROR:
                break;
            case BOOLEAN:
                value = cellValue.getBooleanValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    value = DateUtil.getJavaDate(cellValue.getNumberValue());
                }else{
                    value = cellValue.getNumberValue();
                }
                break;
            case STRING:
                value = cellValue.getStringValue();
        }
        LOGGER.debug("{}: {}", ExcelUtil.getCellIndex(cell.getRowIndex(), cell.getColumnIndex()), value);
        return value;
    }

    /**
     * Check break.
     *
     * @param sheet
     *            the sheet
     * @param row
     *            the row
     * @param col
     *            the col
     * @param condition
     *            the condition
     * @return true, if successful
     */
    private static boolean checkBreak(Sheet sheet,int row,int col,LoopBreakCondition condition){
        //no break condition defined		
        if (sheet.getLastRowNum() < row){
            return true;
        }

        //---------------------------------------------------------------
        if (condition != null){
            Row hrow = sheet.getRow(row + condition.getRowOffset());
            if (hrow == null){
                return false;
            }
            Cell cell = hrow.getCell(col + condition.getColOffset());
            if (cell == null || cell.getCellType() != CellType.STRING){
                return false;
            }
            if (condition.getFlagString().equals(cell.getRichStringCellValue().getString())){
                return true;
            }
        }
        return false;
    }

    /**
     * 获得 property type.
     *
     * @param object
     *            the object
     * @param cellDefinition
     *            the cell definition
     * @return the property type
     * @throws Exception
     *             the exception
     */
    private Class<? extends Object> getPropertyType(Object object,ExcelCell cellDefinition) throws Exception{
        Class<?> clazz = DataConvertorConfigurator.getInstance().getSupportedClass(cellDefinition.getType());
        if (clazz != null){
            return clazz;
        }
        return getPropertyType(object, cellDefinition.getDataName());
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
    @SuppressWarnings("unchecked")
    private Class<? extends Object> getPropertyType(Object object,String dataName) throws Exception{
        //log.debug("Get Class for '" + dataName +"' in " + object.getClass());
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
        LOGGER.debug("getPropertyType for Object[{}] with property {}.", object, dataName);
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
    private Class<? extends Object> getPropertyTypeWithClass(Class<? extends Object> clazz,String dataName) throws Exception{
        if (clazz == null){
            throw new IllegalArgumentException();
        }
        int delim = dataName.indexOf('.');
        if (delim < 0){
            PropertyDescriptor pd = OgnlRuntime.getPropertyDescriptor(clazz, dataName);
            if (pd == null){
                throw new IllegalArgumentException();
            }
            return pd.getPropertyType();
        }
        PropertyDescriptor pd = OgnlRuntime.getPropertyDescriptor(clazz, dataName.substring(0, delim));
        if (pd == null){
            throw new IllegalArgumentException();
        }
        return getPropertyTypeWithClass(pd.getPropertyType(), dataName.substring(delim + 1));
    }

    //---------------------------------------------------------------

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    @Override
    public ExcelManipulatorDefinition getDefinition(){
        return definition;
    }

    /**
     * Sets the definition.
     *
     * @param definition
     *            the new definition
     */
    @Override
    public void setDefinition(ExcelManipulatorDefinition definition){
        this.definition = definition;
    }

    /**
     * Checks if is skip errors.
     *
     * @return true, if is skip errors
     */
    public boolean isSkipErrors(){
        return skipErrors;
    }

    /**
     * Sets the skip errors.
     *
     * @param skipErrors
     *            the new skip errors
     */
    public void setSkipErrors(boolean skipErrors){
        this.skipErrors = skipErrors;
    }

}
