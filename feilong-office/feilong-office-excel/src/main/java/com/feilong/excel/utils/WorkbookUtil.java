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
package com.feilong.excel.utils;

import static com.feilong.core.date.DateUtil.formatDuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class WorkbookUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class WorkbookUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkbookUtil.class);

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private WorkbookUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 创建 wook book.
     *
     * @param inputStream
     *            the is
     * @return the workbook
     */
    public static Workbook create(InputStream inputStream){
        Validate.notNull(inputStream, "inputStream can't be null!");

        //---------------------------------------------------------------
        try{
            Date beginDate = new Date();
            Workbook workbook = WorkbookFactory.create(inputStream);
            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("create workbook from [{}] use time: [{}]", inputStream.toString(), formatDuration(beginDate));
            }
            return workbook;
        }catch (EncryptedDocumentException e){
            throw new RuntimeException(e);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Write.
     *
     * @param workbook
     *            the workbook
     * @param outputStream
     *            the output stream
     * @throws UncheckedIOException
     *             the unchecked IO exception
     * @since 3.0.0
     */
    public static void write(Workbook workbook,OutputStream outputStream){
        Validate.notNull(workbook, "workbook can't be null!");
        Validate.notNull(outputStream, "outputStream can't be null!");

        //---------------------------------------------------------------
        try{
            Date beginDate = new Date();
            workbook.write(outputStream);
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("write workbook to outputStream use time: [{}]", formatDuration(beginDate));
            }
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(outputStream);
        }
    }
}
