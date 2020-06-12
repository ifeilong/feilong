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
package com.feilong.excel;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.reader.ReadStatus;
import com.feilong.excel.reader.SheetReader;
import com.feilong.excel.util.CloneUtil;
import com.feilong.lib.excel.ognl.OgnlStack;

/**
 * The Class DefaultExcelReader.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see ExcelManipulatorFactory
 * @since 3.0.0
 */
public class DefaultExcelReader extends AbstractExcelConfig implements ExcelReader{

    /** The skip errors. */
    private boolean skipErrors = true;

    //---------------------------------------------------------------

    /**
     * Instantiates a new default excel reader.
     */
    public DefaultExcelReader(){
    }

    /**
     * Instantiates a new default excel reader.
     *
     * @param excelDefinition
     *            the excel definition
     */
    public DefaultExcelReader(ExcelDefinition excelDefinition){
        super();
        this.excelDefinition = excelDefinition;
    }

    //---------------------------------------------------------------

    /**
     * Read all.
     *
     * @param inputStream
     *            the input stream
     * @param beans
     *            the beans
     * @return the read status
     */
    @Override
    public ReadStatus readAll(InputStream inputStream,Map<String, Object> beans){
        ReadStatus readStatus = new ReadStatus();
        readStatus.setStatus(ReadStatus.STATUS_SUCCESS);

        try (Workbook workbook = WorkbookFactory.create(inputStream)){
            List<ExcelSheet> excelSheets = excelDefinition.getExcelSheets();
            int size = excelSheets.size();
            Validate.isTrue(
                            size > 0 && workbook.getNumberOfSheets() >= size,
                            "No sheet definition found or Sheet Number in definition is more than number in file.");

            OgnlStack ognlStack = new OgnlStack(beans);
            for (int i = 0; i < size; i++){
                SheetReader.readSheet(workbook, i, excelSheets.get(i), ognlStack, readStatus, skipErrors);
            }
            return readStatus;
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }

    }

    @Override
    public ReadStatus readAllPerSheet(InputStream inputStream,Map<String, Object> data){
        try (Workbook workbook = WorkbookFactory.create(inputStream)){
            List<ExcelSheet> excelSheets = excelDefinition.getExcelSheets();
            int size = excelSheets.size();
            Validate.isTrue(size > 0, "No sheet definition found");

            //Only first ExcelSheet Definition will be used
            ExcelSheet excelSheet = excelSheets.iterator().next();

            Map<String, List<Object>> cacheMap = new HashMap<>();

            for (Map.Entry<String, Object> entry : data.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value != null){
                    cacheMap.put(key, new ArrayList<>());
                }

            }

            //---------------------------------------------------------------
            ReadStatus readStatus = new ReadStatus();
            readStatus.setStatus(ReadStatus.STATUS_SUCCESS);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++){
                Map<String, Object> clonedData = CloneUtil.cloneMap(data);
                SheetReader.readSheet(workbook, i, excelSheet, new OgnlStack(clonedData), readStatus, skipErrors);

                for (Map.Entry<String, Object> entry : clonedData.entrySet()){
                    String key = entry.getKey();

                    List<Object> list = cacheMap.get(key);
                    list.add(entry.getValue());
                }
            }
            for (String key : data.keySet()){
                if (cacheMap.containsKey(key)){
                    data.put(key, cacheMap.get(key));
                }else{
                    data.put(key, null);
                }
            }
            return readStatus;
        }catch (Exception e){
            throw new DefaultRuntimeException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Read sheet.
     *
     * @param inputStream
     *            the input stream
     * @param sheetNo
     *            the sheet no
     * @param beans
     *            the beans
     * @return the read status
     */
    @Override
    public ReadStatus readSheet(InputStream inputStream,int sheetNo,Map<String, Object> beans){
        OgnlStack ognlStack = new OgnlStack(beans);

        try (Workbook workbook = WorkbookFactory.create(inputStream)){

            List<ExcelSheet> excelSheets = excelDefinition.getExcelSheets();

            ReadStatus readStatus = new ReadStatus();
            readStatus.setStatus(ReadStatus.STATUS_SUCCESS);

            SheetReader.readSheet(workbook, sheetNo, excelSheets.iterator().next(), ognlStack, readStatus, skipErrors);
            return readStatus;
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }

    }

    //---------------------------------------------------------------

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
