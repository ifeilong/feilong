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

import com.feilong.office.excel.utils.WorkbookUtil;
import com.feilong.office.excel.writer.WorkbookWriter;

/**
 * The Class DefaultExcelWriter.
 */
public class DefaultExcelWriter extends AbstractExcelConfig implements ExcelWriter{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExcelWriter.class);

    //---------------------------------------------------------------

    /** The buffered template. */
    private byte[]              bufferedTemplate;

    //---------------------------------------------------------------

    /**
     * Write.
     *
     * @param template
     *            the template
     * @param os
     *            the os
     * @param beans
     *            the beans
     * @return the write status
     */
    @Override
    public void write(String template,OutputStream os,Map<String, Object> beans){
        write(build(template), os, beans);
    }

    /**
     * Write.
     *
     * @param outputStream
     *            the os
     * @param beans
     *            the beans
     * @return the write status
     */
    @Override
    public void write(OutputStream outputStream,Map<String, Object> beans){
        if (bufferedTemplate == null){
            throw new IllegalArgumentException("bufferedTemplate is null");
        }

        //---------------------------------------------------------------
        InputStream inputStream = new ByteArrayInputStream(bufferedTemplate);
        write(inputStream, outputStream, beans);
    }

    @Override
    public void write(InputStream inputStream,OutputStream outputStream,Map<String, Object> beans){
        Workbook workbook = WorkbookUtil.create(inputStream);
        WorkbookWriter.write(workbook, outputStream, excelManipulatorDefinition, beans);
    }

    /**
     * Write per sheet.
     *
     * @param template
     *            the template
     * @param os
     *            the os
     * @param beansList
     *            the beans list
     * @return the write status
     */
    @Override
    public void writePerSheet(String template,OutputStream os,List<Map<String, Object>> beansList){
        writePerSheet(build(template), os, beansList);
    }

    /**
     * @param template
     * @return
     * @since 3.0.0
     * @deprecated use feilong
     */
    @Deprecated
    private static InputStream build(String template){
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(template);
    }

    /**
     * Write per sheet.
     *
     * @param os
     *            the os
     * @param beansList
     *            the beans list
     * @return the write status
     */
    @Override
    public void writePerSheet(OutputStream os,List<Map<String, Object>> beansList){
        if (bufferedTemplate == null){
            throw new IllegalArgumentException("bufferedTemplate is null");
        }
        //---------------------------------------------------------------
        InputStream inputStream = new ByteArrayInputStream(bufferedTemplate);
        writePerSheet(inputStream, os, beansList);
    }

    @Override
    public void writePerSheet(InputStream inputStream,OutputStream outputStream,List<Map<String, Object>> beansList){
        Workbook workbook = WorkbookUtil.create(inputStream);
        WorkbookWriter.writePerSheet(workbook, outputStream, excelManipulatorDefinition, beansList);
    }

    //---------------------------------------------------------------

    /**
     * Inits the buffered template.
     *
     * @param is
     *            the is
     */
    public void initBufferedTemplate(InputStream is){
        try{
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int b = is.read(buf);
            while (b != -1){
                bos.write(buf, 0, b);
                b = is.read(buf);
            }
            bufferedTemplate = bos.toByteArray();
        }catch (IOException e){
            LOGGER.error("Init Write Template Error", e);
        }
    }

}
