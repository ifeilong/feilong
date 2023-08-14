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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.lang.StringUtil;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.reader.ReadStatus;
import com.feilong.io.InputStreamUtil;
import com.feilong.json.JsonUtil;

/**
 * 专门用来读取 excel 的.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.0
 */
public class ExcelReaderUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReaderUtil.class);

    /** Don't let anyone instantiate this class. */
    private ExcelReaderUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param excelLocation
     *            比如 "E:\\Workspaces\\train\\Java培训报名12.xls"
     * @param sheetDefinitionLocation
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            sheet名字 比如 trainCourseSheet
     * @param dataName
     *            configuration 里面名字是 trainCourseSheet的, 配置的dataName,比如 trainSignUpSheet
     * @param sheetNo
     *            第几个sheet,从0开始,比如1
     * @return the list
     */
    public static <T> List<T> read(
                    String excelLocation,
                    String sheetDefinitionLocation,//
                    String sheetName,
                    String dataName,
                    int sheetNo){
        return read(excelLocation, toArray(sheetDefinitionLocation), sheetName, dataName, sheetNo);
    }

    /**
     * 读取数据.
     *
     * @param excelLocation
     *            比如 "E:\\Workspaces\\train\\Java培训报名12.xls"
     * @param sheetDefinitionLocation
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            sheet名字 比如 trainCourseSheet
     * @param sheetNo
     *            第几个sheet,从0开始,比如1
     * @return the map
     */
    public static Map<String, Object> readData(
                    String excelLocation,
                    String sheetDefinitionLocation,//
                    String sheetName,
                    int sheetNo){
        ExcelReader excelReader = build(sheetDefinitionLocation, sheetName);

        InputStream inputStream = InputStreamUtil.getInputStream(excelLocation);
        return readData(excelReader, inputStream, sheetNo);
    }

    //---------------------------------------------------------------

    private static ExcelReader build(String sheetDefinitionLocation,String sheetName){
        Map<String, ExcelSheet> sheetDefinitions = ExcelSheetMapBuilder.build(sheetDefinitionLocation);
        return buildExcelReader(sheetDefinitions, sheetName);
    }

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param excelLocation
     *            the file name
     * @param sheetDefinitionLocations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            sheet 比如 trainCourseSheet
     * @param dataName
     *            configuration 里面名字是 trainCourseSheet的, 配置的dataName,比如 trainSignUpSheet
     * @param sheetNo
     *            the sheet no
     * @return the list
     */
    private static <T> List<T> read(String excelLocation,String[] sheetDefinitionLocations,String sheetName,String dataName,int sheetNo){
        Map<String, ExcelSheet> sheetDefinitions = ExcelSheetMapBuilder.build(sheetDefinitionLocations);
        ExcelReader excelReader = buildExcelReader(sheetDefinitions, sheetName);

        InputStream inputStream = InputStreamUtil.getInputStream(excelLocation);
        return read(excelReader, inputStream, dataName, sheetNo);
    }

    //---------------------------------------------------------------

    /**
     * 获得 excel reader.
     *
     * @param sheetDefinitionLocations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            the sheet
     * @return the excel reader
     * @since 1.0.9
     */
    private static ExcelReader buildExcelReader(Map<String, ExcelSheet> sheetDefinitions,String...sheetNames){
        ExcelDefinition build = ExcelDefinitionBuilder.build(sheetDefinitions, sheetNames);
        return new DefaultExcelReader(build);

    }

    //---------------------------------------------------------------

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param excelReader
     *            the excel reader
     * @param inputStream
     *            the input stream
     * @param dataName
     *            the data name
     * @param sheetNo
     *            the sheet no
     * @return the list< t>
     * @since 1.0.9
     */
    private static <T> List<T> read(ExcelReader excelReader,InputStream inputStream,String dataName,int sheetNo){
        long beginTimeMillis = System.currentTimeMillis();

        Map<String, Object> beans = readData(excelReader, inputStream, sheetNo);
        //--------------------------------------------------------------- 
        List<T> list = loadData(beans, dataName);
        if (LOGGER.isInfoEnabled()){
            Map<String, Object> map = newLinkedHashMap();
            map.put("dataName", dataName);
            map.put("sheetNo", sheetNo);
            map.put("list size", size(list));
            map.put("use time", formatDurationUseBeginTimeMillis(beginTimeMillis));
            LOGGER.info("use time: [{}]", JsonUtil.toString(map));
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> loadData(Map<String, Object> beans,String dataName){
        if (isNotNullOrEmpty(dataName)){
            return (List<T>) beans.get(dataName);
        }
        if (size(beans) == 1){
            return (List<T>) (toList(beans.values()).get(0));
        }
        throw new DefaultRuntimeException("dataName:[{}],keys:[{}] can't be null/empty!", dataName, beans.keySet());
    }

    //---------------------------------------------------------------

    /**
     * Read data.
     *
     * @param excelReader
     *            the excel reader
     * @param inputStream
     *            the input stream
     * @param sheetNo
     *            the sheet no
     * @return the map
     */
    private static Map<String, Object> readData(ExcelReader excelReader,InputStream inputStream,int sheetNo){
        Map<String, Object> returnBeanMap = newLinkedHashMap();
        ReadStatus readStatus = excelReader.readSheet(inputStream, sheetNo, returnBeanMap);

        int status = readStatus.getStatus();
        if (status == ReadStatus.STATUS_SUCCESS){
            return returnBeanMap;
        }

        //---------------------------------------------------------------
        List<Exception> exceptions = readStatus.getExceptions();
        String pattern = "read excel exception,readStatus:[{}],getMessage:[{}],and exceptions size is:[{}],first exception is:\n{}";
        throw new DefaultRuntimeException(
                        StringUtil.formatPattern(
                                        pattern,
                                        status,
                                        readStatus.getMessage(), //
                                        exceptions.size(),
                                        exceptions.get(0).getStackTrace()));
    }
}
