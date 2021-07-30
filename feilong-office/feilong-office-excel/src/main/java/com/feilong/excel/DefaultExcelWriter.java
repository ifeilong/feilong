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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.excel.util.WorkbookUtil;
import com.feilong.excel.writer.WorkbookWriter;
import com.feilong.io.InputStreamUtil;

/**
 * 默认的excel 写操作.
 * 
 * @see ExcelManipulatorFactory
 */
public class DefaultExcelWriter extends AbstractExcelConfig implements ExcelWriter{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExcelWriter.class);

    //---------------------------------------------------------------

    /** The buffered template. */
    private byte[]              bufferedTemplate;

    //---------------------------------------------------------------

    /**
     * Instantiates a new default excel writer.
     */
    public DefaultExcelWriter(){
    }

    /**
     * Instantiates a new default excel writer.
     *
     * @param excelDefinition
     *            the excel definition
     */
    public DefaultExcelWriter(ExcelDefinition excelDefinition){
        super();
        this.excelDefinition = excelDefinition;
    }

    //---------------------------------------------------------------

    /**
     * Write.
     *
     * @param template
     *            the template
     * @param outputStream
     *            the os
     * @param data
     *            the beans
     */
    @Override
    public void write(String template,OutputStream outputStream,Map<String, Object> data){
        write(InputStreamUtil.getInputStream(template), outputStream, data);
    }

    /**
     * Write.
     *
     * @param outputStream
     *            the os
     * @param data
     *            the beans
     */
    @Override
    public void write(OutputStream outputStream,Map<String, Object> data){
        if (bufferedTemplate == null){
            throw new IllegalArgumentException("bufferedTemplate is null");
        }

        //---------------------------------------------------------------
        InputStream inputStream = new ByteArrayInputStream(bufferedTemplate);
        write(inputStream, outputStream, data);
    }

    /**
     * Write.
     *
     * @param inputStream
     *            the input stream
     * @param outputStream
     *            the output stream
     * @param data
     *            the data
     */
    private void write(InputStream inputStream,OutputStream outputStream,Map<String, Object> data){
        Workbook workbook = WorkbookUtil.create(inputStream);
        WorkbookWriter.write(workbook, outputStream, excelDefinition, data);
    }

    //---------------------------------------------------------------

    /**
     * Write per sheet.
     *
     * @param template
     *            the template
     * @param os
     *            the os
     * @param beansList
     *            the beans list
     */
    @Override
    public void writePerSheet(String template,OutputStream os,List<Map<String, Object>> beansList){
        writePerSheet(InputStreamUtil.getInputStream(template), os, beansList);
    }

    /**
     * Write per sheet.
     *
     * @param outputStream
     *            the os
     * @param beansList
     *            the beans list
     */
    @Override
    public void writePerSheet(OutputStream outputStream,List<Map<String, Object>> beansList){
        if (bufferedTemplate == null){
            throw new IllegalArgumentException("bufferedTemplate is null");
        }
        //---------------------------------------------------------------
        InputStream inputStream = new ByteArrayInputStream(bufferedTemplate);
        writePerSheet(inputStream, outputStream, beansList);
    }

    /**
     * Write per sheet.
     *
     * @param inputStream
     *            the input stream
     * @param outputStream
     *            the output stream
     * @param beansList
     *            the beans list
     */
    @Override
    public void writePerSheet(InputStream inputStream,OutputStream outputStream,List<Map<String, Object>> beansList){
        try (Workbook workbook = WorkbookUtil.create(inputStream)){
            WorkbookWriter.writePerSheet(workbook, outputStream, excelDefinition, beansList);
        }catch (Exception e){
            throw new DefaultRuntimeException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Inits the buffered template.
     *
     * @param inputStream
     *            the is
     */
    public void initBufferedTemplate(InputStream inputStream){
        try{
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int b = inputStream.read(buf);
            while (b != -1){
                bos.write(buf, 0, b);
                b = inputStream.read(buf);
            }
            bufferedTemplate = bos.toByteArray();
        }catch (IOException e){
            LOGGER.error("Init Write Template Error", e);
        }
    }

}
