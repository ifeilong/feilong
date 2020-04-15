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
package com.feilong.office.excel.loxia;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.util.MapUtil.newHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.io.FileUtil;
import com.feilong.office.excel.loxia.convertor.BigDecimalConvertor;
import com.feilong.office.excel.loxia.convertor.BooleanConvertor;
import com.feilong.office.excel.loxia.convertor.IntegerConvertor;
import com.feilong.tools.slf4j.Slf4jUtil;

import loxia.support.excel.ExcelManipulatorFactory;
import loxia.support.excel.ExcelReader;
import loxia.support.excel.ReadStatus;
import loxia.support.excel.convertor.DataConvertorConfigurator;

/**
 * 专门用来读取 excel 的.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 */
public class LoxiaExcelReaderUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoxiaExcelReaderUtil.class);

    /** Don't let anyone instantiate this class. */
    private LoxiaExcelReaderUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static{
        //处理boolean 类型
        DataConvertorConfigurator.getInstance().registerDataConvertor(new BooleanConvertor());
        DataConvertorConfigurator.getInstance().registerDataConvertor(new BigDecimalConvertor());
        DataConvertorConfigurator.getInstance().registerDataConvertor(new IntegerConvertor());
    }

    //---------------------------------------------------------------

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param configuration
     *            配置文件, 如 sheets/train-course.xml
     * @param sheet
     *            sheet 比如 trainCourseSheet
     * @param dataName
     *            configuration 里面名字是 trainCourseSheet的, 配置的dataName,比如 trainSignUpSheet
     * @param fileName
     *            比如 "E:\\Workspaces\\train\\Java培训报名12.xls"
     * @param sheetNo
     *            第几个sheet,从0开始,比如1
     * @return the list
     */
    public static <T> List<T> getList(String configuration,String sheet,String dataName,String fileName,int sheetNo){
        return getList(toArray(configuration), sheet, dataName, fileName, sheetNo);
    }

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param configuration
     *            配置文件, 如 sheets/train-course.xml
     * @param sheet
     *            sheet 比如 trainCourseSheet
     * @param dataName
     *            configuration 里面名字是 trainCourseSheet的, 配置的dataName,比如 trainSignUpSheet
     * @param inputStream
     *            the input stream
     * @param sheetNo
     *            the sheet no
     * @return the list
     */
    public static <T> List<T> getList(String configuration,String sheet,String dataName,InputStream inputStream,int sheetNo){
        ExcelReader excelReader = getExcelReader(toArray(configuration), sheet);
        return getList(excelReader, dataName, inputStream, sheetNo);
    }

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param configurations
     *            配置文件, 如 {"sheets/train-course.xml"}
     * @param sheet
     *            sheet 比如 trainCourseSheet
     * @param dataName
     *            configuration 里面名字是 trainCourseSheet的, 配置的dataName,比如 trainSignUpSheet
     * @param fileName
     *            the file name
     * @param sheetNo
     *            the sheet no
     * @return the list
     */
    public static <T> List<T> getList(String[] configurations,String sheet,String dataName,String fileName,int sheetNo){
        ExcelReader excelReader = getExcelReader(configurations, sheet);
        return getList(excelReader, dataName, fileName, sheetNo);
    }

    //---------------------------------------------------------------

    /**
     * 获得 excel reader.
     *
     * @param configurations
     *            the configurations
     * @param sheet
     *            the sheet
     * @return the excel reader
     * @since 1.0.9
     */
    private static ExcelReader getExcelReader(String[] configurations,String sheet){
        ExcelManipulatorFactory excelManipulatorFactory = new ExcelManipulatorFactory();
        excelManipulatorFactory.setConfig(configurations);
        return excelManipulatorFactory.createExcelReader(sheet);
    }

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param excelReader
     *            the excel reader
     * @param dataName
     *            the data name
     * @param fileName
     *            the file name
     * @param sheetNo
     *            the sheet no
     * @return the list
     */
    private static <T> List<T> getList(ExcelReader excelReader,String dataName,String fileName,int sheetNo){
        InputStream inputStream = FileUtil.getFileInputStream(fileName);
        return getList(excelReader, dataName, inputStream, sheetNo);
    }

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param excelReader
     *            the excel reader
     * @param dataName
     *            the data name
     * @param inputStream
     *            the input stream
     * @param sheetNo
     *            the sheet no
     * @return the list< t>
     * @since 1.0.9
     */
    @SuppressWarnings("unchecked")
    private static <T> List<T> getList(ExcelReader excelReader,String dataName,InputStream inputStream,int sheetNo){
        Map<String, Object> beans = newHashMap();
        beans.put(dataName, new ArrayList<T>());

        ReadStatus readStatus = excelReader.readSheet(inputStream, sheetNo, beans);

        int status = readStatus.getStatus();
        if (status == ReadStatus.STATUS_SUCCESS){
            return (List<T>) beans.get(dataName);
        }

        //---------------------------------------------------------------
        List<Exception> exceptions = readStatus.getExceptions();

        String pattern = "read excel exception,readStatus:[{}],getMessage:[{}],and exceptions size is:[{}],first exception is:\n{}";
        String message = Slf4jUtil.format(
                        pattern,
                        readStatus.getStatus(),
                        readStatus.getMessage(),
                        exceptions.size(),
                        exceptions.get(0).getStackTrace());
        LOGGER.error(message);
        throw new UncheckedIOException(new IOException(message));
    }

}
