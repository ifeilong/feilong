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
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelSheet;
import com.feilong.office.excel.utils.OgnlStack;

/**
 * The Class DefaultExcelReader.
 */
public class DefaultExcelReader implements ExcelReader{

    /** The Constant log. */
    private static final Logger        LOGGER                       = LoggerFactory.getLogger(DefaultExcelReader.class);

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
        ReadStatus readStatus = new ReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);

        try (Workbook wb = WorkbookFactory.create(is)){
            List<ExcelSheet> excelSheets = definition.getExcelSheets();
            int size = excelSheets.size();
            Validate.isTrue(
                            size > 0 && wb.getNumberOfSheets() >= size,
                            "No sheet definition found or Sheet Number in definition is more than number in file.");
            OgnlStack stack = new OgnlStack(beans);
            for (int i = 0; i < size; i++){
                readSheet(wb, i, excelSheets.get(i), stack, readStatus);
            }
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
        return readStatus;
    }

    /**
     * Read all per sheet.
     *
     * @param inputStream
     *            the is
     * @param beans
     *            the beans
     * @return the read status
     */
    @Override
    public ReadStatus readAllPerSheet(InputStream inputStream,Map<String, Object> beans){
        ReadStatus readStatus = new ReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);

        try (Workbook workbook = WorkbookFactory.create(inputStream)){
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
                for (int i = 0; i < workbook.getNumberOfSheets(); i++){
                    Map<String, Object> clonedBeans = cloneMap(beans);
                    readSheet(workbook, i, sheetDefinition, new OgnlStack(clonedBeans), readStatus);
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
        ReadStatus readStatus = new ReadStatus();
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
                    BlockReader.readLoopBlock(wb, sheetNo, blockDefinition, stack, readStatus);
                }else{
                    BlockReader.readSimpleBlock(wb, sheetNo, blockDefinition, stack, readStatus);
                }
            }
        }
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
