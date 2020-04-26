package loxia.support.excel.impl;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loxia.support.excel.ExcelReader;
import loxia.support.excel.ExcelUtil;
import loxia.support.excel.ReadStatus;
import loxia.support.excel.convertor.DataConvertor;
import loxia.support.excel.convertor.DataConvertorConfigurator;
import loxia.support.excel.definition.ExcelBlock;
import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.definition.ExcelManipulatorDefinition;
import loxia.support.excel.definition.ExcelSheet;
import loxia.support.excel.definition.LoopBreakCondition;
import loxia.support.excel.exception.ErrorCode;
import loxia.support.excel.exception.ExcelManipulateException;
import loxia.utils.OgnlStack;
import ognl.OgnlRuntime;

public class DefaultExcelReader implements ExcelReader,Serializable{

    /**
     * 
     */
    private static final long          serialVersionUID = -8375024150155894874L;

    static final Logger                logger           = LoggerFactory.getLogger(DefaultExcelReader.class);

    private ExcelManipulatorDefinition definition;

    private boolean                    skipErrors       = true;

    @Override
    public ReadStatus readAll(InputStream is,Map<String, Object> beans){
        ReadStatus readStatus = new DefaultReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);
        try{
            Workbook wb = WorkbookFactory.create(is);
            if (definition.getExcelSheets().size() == 0 || wb.getNumberOfSheets() < definition.getExcelSheets().size()){
                readStatus.setStatus(ReadStatus.STATUS_SETTING_ERROR);
                readStatus.setMessage("No sheet definition found or Sheet Number in definition is more than number in file.");
            }else{
                OgnlStack stack = new OgnlStack(beans);
                for (int i = 0; i < definition.getExcelSheets().size(); i++){
                    readSheet(wb, i, definition.getExcelSheets().get(i), stack, readStatus);
                }
            }
        }catch (IOException e){
            readStatus.setStatus(ReadStatus.STATUS_READ_FILE_ERROR);
        }catch (InvalidFormatException e){
            readStatus.setStatus(ReadStatus.STATUS_READ_FILE_ERROR);
        }
        return readStatus;
    }

    @Override
    public ReadStatus readAllPerSheet(InputStream is,Map<String, Object> beans){
        ReadStatus readStatus = new DefaultReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);
        try{
            Workbook wb = WorkbookFactory.create(is);
            if (definition.getExcelSheets().size() == 0){
                readStatus.setStatus(ReadStatus.STATUS_SETTING_ERROR);
                readStatus.setMessage("No sheet definition found");
            }else{
                //Only first ExcelSheet Definition will be used
                ExcelSheet sheetDefinition = definition.getExcelSheets().iterator().next();

                Map<String, List<Object>> cacheMap = new HashMap<String, List<Object>>();
                for (String key : beans.keySet()){
                    if (beans.get(key) != null)
                        cacheMap.put(key, new ArrayList<Object>());
                }
                for (int i = 0; i < wb.getNumberOfSheets(); i++){
                    Map<String, Object> clonedBeans = cloneMap(beans);
                    readSheet(wb, i, sheetDefinition, new OgnlStack(clonedBeans), readStatus);
                    for (String key : clonedBeans.keySet())
                        cacheMap.get(key).add(clonedBeans.get(key));
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
            readStatus.setStatus(ReadStatus.STATUS_READ_FILE_ERROR);
        }catch (InvalidFormatException e){
            readStatus.setStatus(ReadStatus.STATUS_READ_FILE_ERROR);
        }catch (InstantiationException e){
            e.printStackTrace();
            readStatus.setStatus(ReadStatus.STATUS_SYSTEM_ERROR);
            readStatus.setMessage("New Instance Error");
        }catch (IllegalAccessException e){
            e.printStackTrace();
            readStatus.setStatus(ReadStatus.STATUS_SYSTEM_ERROR);
            readStatus.setMessage("New Instance Error");
        }
        return readStatus;
    }

    @Override
    public ReadStatus readSheet(InputStream is,int sheetNo,Map<String, Object> beans){
        ReadStatus readStatus = new DefaultReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);
        OgnlStack stack = new OgnlStack(beans);
        try{
            Workbook wb = WorkbookFactory.create(is);
            readSheet(wb, sheetNo, definition.getExcelSheets().iterator().next(), stack, readStatus);
        }catch (IOException e){
            readStatus.setStatus(ReadStatus.STATUS_READ_FILE_ERROR);
        }catch (InvalidFormatException e){
            readStatus.setStatus(ReadStatus.STATUS_READ_FILE_ERROR);
        }
        return readStatus;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> cloneMap(Map<String, Object> map) throws InstantiationException,IllegalAccessException{
        Map<String, Object> result = map.getClass().newInstance();
        for (String key : map.keySet()){
            Object obj = map.get(key);
            if (obj == null)
                continue;
            if (obj instanceof Map){
                result.put(key, cloneMap((Map<String, Object>) obj));
            }else
                result.put(key, obj.getClass().newInstance());
        }
        return result;
    }

    private void readSheet(Workbook wb,int sheetNo,ExcelSheet sheetDefinition,OgnlStack stack,ReadStatus readStatus){
        //In Read Operation only the first loopBlock will be read
        int loopBlock = 0;

        for (ExcelBlock blockDefinition : sheetDefinition.getExcelBlocks()){
            if (((skipErrors && readStatus.getStatus() == ReadStatus.STATUS_DATA_COLLECTION_ERROR)
                            || readStatus.getStatus() == ReadStatus.STATUS_SUCCESS) && (loopBlock < 1 || !blockDefinition.isLoop())){
                if (blockDefinition.isLoop()){
                    loopBlock++;
                    readLoopBlock(wb, sheetNo, blockDefinition, stack, readStatus);
                }else
                    readSimpleBlock(wb, sheetNo, blockDefinition, stack, readStatus);
            }
        }
    }

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
                logger.debug("{}[Checked]:{}", ExcelUtil.getCellIndex(cellDefinition.getRow(), cellDefinition.getCol()), value);
                stack.setValue(cellDefinition.getDataName(), value);
            }catch (ExcelManipulateException e){
                if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS)
                    readStatus.setStatus(ReadStatus.STATUS_DATA_COLLECTION_ERROR);
                readStatus.addException(e);
            }catch (Exception e){
                e.printStackTrace();
                readStatus.setStatus(ReadStatus.STATUS_SYSTEM_ERROR);
                readStatus.setMessage(e.getMessage());
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void readLoopBlock(Workbook wb,int sheetNo,ExcelBlock blockDefinition,OgnlStack stack,ReadStatus readStatus){
        //Loop Block will only care about row loop
        String dataName = blockDefinition.getDataName();
        if (dataName == null || dataName.length() == 0){
            readStatus.setStatus(ReadStatus.STATUS_SETTING_ERROR);
            readStatus.setMessage("dataName for block[" + blockDefinition.toString() + "] is not set");
            return;
        }

        try{
            Object obj = stack.getValue(dataName);
            Collection dataList;
            if (obj == null){
                dataList = new ArrayList();
                stack.setValue(dataName, dataList);
            }else if (!(obj instanceof Collection)){
                readStatus.setStatus(ReadStatus.STATUS_SETTING_ERROR);
                readStatus.setMessage("Property " + dataName + " is not a Collection");
                return;
            }else{
                dataList = (Collection) obj;
            }

            int startRow = blockDefinition.getStartRow();
            int step = blockDefinition.getEndRow() - blockDefinition.getStartRow() + 1;
            while (!checkBreak(wb.getSheetAt(sheetNo), startRow, blockDefinition.getStartCol(), blockDefinition.getBreakCondition())){
                Object value = readBlock(wb, sheetNo, blockDefinition, startRow, readStatus);
                dataList.add(value);
                startRow += step;
            }
        }catch (Exception e){
            e.printStackTrace();
            readStatus.setStatus(ReadStatus.STATUS_SYSTEM_ERROR);
            readStatus.setMessage(e.getMessage());
        }
    }

    /**
     * Read Block in loop condition
     * 
     * @param <T>
     * @param wb
     * @param sheetNo
     * @param blockDefinition
     * @param startRow
     * @param needCreate
     * @param readStatus
     * @return
     * @throws Exception
     */
    private Object readBlock(Workbook wb,int sheetNo,ExcelBlock blockDefinition,int startRow,ReadStatus readStatus) throws Exception{
        Sheet sheet = wb.getSheetAt(sheetNo);
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

        if (blockDefinition.getLoopClass() == null){
            Map<String, Object> result = new HashMap<String, Object>();

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
                    logger.debug("{}[Checked]:{}", ExcelUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()), value);
                    result.put(cellDefinition.getDataName(), value);
                }catch (ExcelManipulateException e){
                    if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS)
                        readStatus.setStatus(ReadStatus.STATUS_DATA_COLLECTION_ERROR);
                    readStatus.addException(e);
                }
            }
            return result;
        }else{
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
                    logger.debug("{}[Checked]:{}", ExcelUtil.getCellIndex(startRow + rowOffSet, cellDefinition.getCol()), value);
                    ognlStack.setValue(cellDefinition.getDataName(), value);
                }catch (ExcelManipulateException e){
                    if (readStatus.getStatus() == ReadStatus.STATUS_SUCCESS)
                        readStatus.setStatus(ReadStatus.STATUS_DATA_COLLECTION_ERROR);
                    readStatus.addException(e);
                }
            }
            return result;
        }
    }

    private Object checkValue(int sheetNo,String cellIndex,Object value,ExcelCell cellDefinition,Class<? extends Object> clazz)
                    throws ExcelManipulateException{
        DataConvertor<?> dc = DataConvertorConfigurator.getInstance().getConvertor(clazz);
        //primitive type should be mandatory
        if (clazz.isPrimitive())
            cellDefinition.setMandatory(true);
        if (dc == null){
            throw new ExcelManipulateException(
                            ErrorCode.UNSUPPORTING_DATA_TYPE,
                            new Object[] { sheetNo + 1, cellIndex, null, cellDefinition.getPattern(), cellDefinition.getChoiceString() });
        }
        return dc.convert(value, sheetNo, cellIndex, cellDefinition);
    }

    private Object getCellValue(Cell cell,FormulaEvaluator evaluator) throws ExcelManipulateException{
        if (cell == null)
            return null;
        //log.debug("Read Value for: " + ExcelUtil.getCellIndex(cell.getRowIndex(), cell.getColumnIndex()));
        Object value = null;
        CellValue cellValue = evaluator.evaluate(cell);
        if (cellValue == null){
            logger.debug("{}: null", ExcelUtil.getCellIndex(cell.getRowIndex(), cell.getColumnIndex()));
            return null;
        }
        switch (cellValue.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
            case Cell.CELL_TYPE_ERROR:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cellValue.getBooleanValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    value = DateUtil.getJavaDate(cellValue.getNumberValue());
                }else
                    value = cellValue.getNumberValue();
                break;
            case Cell.CELL_TYPE_STRING:
                value = cellValue.getStringValue();
        }
        logger.debug("{}: {}", ExcelUtil.getCellIndex(cell.getRowIndex(), cell.getColumnIndex()), value);
        return value;
    }

    private boolean checkBreak(Sheet sheet,int row,int col,LoopBreakCondition condition){
        //no break condition defined		
        if (sheet.getLastRowNum() < row)
            return true;
        if (condition != null){
            Row hrow = sheet.getRow(row + condition.getRowOffset());
            if (hrow == null)
                return false;
            Cell cell = hrow.getCell(col + condition.getColOffset());
            if (cell == null || cell.getCellType() != HSSFCell.CELL_TYPE_STRING)
                return false;
            if (condition.getFlagString().equals(cell.getRichStringCellValue().getString()))
                return true;
        }
        return false;
    }

    private Class<? extends Object> getPropertyType(Object object,ExcelCell cellDefinition) throws Exception{
        Class<?> clazz = DataConvertorConfigurator.getInstance().getSupportedClass(cellDefinition.getType());
        if (clazz != null){
            return clazz;
        }
        return getPropertyType(object, cellDefinition.getDataName());
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Object> getPropertyType(Object object,String dataName) throws Exception{
        //log.debug("Get Class for '" + dataName +"' in " + object.getClass());
        if (object instanceof Map){
            logger.debug("getPropertyType for Map[{}] with Key {}.", object, dataName);
            if (object == null)
                return null;
            Map<String, Object> map = (Map<String, Object>) object;
            int delim = dataName.indexOf('.');
            if (delim > 0){
                return getPropertyType(map.get(dataName.substring(0, delim)), dataName.substring(delim + 1));
            }else{
                return map.get(dataName).getClass();
            }
        }else{
            logger.debug("getPropertyType for Object[{}] with property {}.", object, dataName);
            return getPropertyTypeWithClass(object.getClass(), dataName);
        }
    }

    private Class<? extends Object> getPropertyTypeWithClass(Class<? extends Object> clazz,String dataName) throws Exception{
        if (clazz == null)
            throw new IllegalArgumentException();
        int delim = dataName.indexOf('.');
        if (delim < 0){
            PropertyDescriptor pd = OgnlRuntime.getPropertyDescriptor(clazz, dataName);
            if (pd == null)
                throw new IllegalArgumentException();
            return pd.getPropertyType();
        }else{
            PropertyDescriptor pd = OgnlRuntime.getPropertyDescriptor(clazz, dataName.substring(0, delim));
            if (pd == null)
                throw new IllegalArgumentException();
            return getPropertyTypeWithClass(pd.getPropertyType(), dataName.substring(delim + 1));
        }
    }

    @Override
    public ExcelManipulatorDefinition getDefinition(){
        return definition;
    }

    @Override
    public void setDefinition(ExcelManipulatorDefinition definition){
        this.definition = definition;
    }

    public boolean isSkipErrors(){
        return skipErrors;
    }

    public void setSkipErrors(boolean skipErrors){
        this.skipErrors = skipErrors;
    }

}
