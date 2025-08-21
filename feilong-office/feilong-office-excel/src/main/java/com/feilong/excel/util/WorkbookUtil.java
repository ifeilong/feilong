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
package com.feilong.excel.util;

import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.io.IOUtil;

/**
 * {@link Workbook} 工具类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
@lombok.extern.slf4j.Slf4j
public class WorkbookUtil{

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
        long beginTimeMillis = System.currentTimeMillis();
        try{
            Workbook workbook = WorkbookFactory.create(inputStream);
            if (log.isDebugEnabled()){
                log.debug("create workbook from [{}],use time: [{}]", inputStream, formatDurationUseBeginTimeMillis(beginTimeMillis));
            }
            return workbook;
        }catch (EncryptedDocumentException e){
            throw new DefaultRuntimeException(e);
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
        long beginTimeMillis = System.currentTimeMillis();
        try{
            workbook.write(outputStream);
            if (log.isInfoEnabled()){
                log.info("write workbook to outputStream use time: [{}]", formatDurationUseBeginTimeMillis(beginTimeMillis));
            }
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            IOUtil.closeQuietly(workbook, outputStream);
        }
    }
}
