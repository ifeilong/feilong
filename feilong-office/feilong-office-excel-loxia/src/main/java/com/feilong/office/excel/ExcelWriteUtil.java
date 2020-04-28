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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.feilong.core.lang.ClassUtil;
import com.feilong.io.FileUtil;
import com.feilong.json.jsonlib.JsonUtil;

/**
 * 使用 loxia 来进行 excel 输出的工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 */
public class ExcelWriteUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriteUtil.class);

    /** Don't let anyone instantiate this class. */
    private ExcelWriteUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * Write.
     * 
     * @param excelTemplateLocation
     *            excel模板location,
     * 
     *            <ol>
     *            <li>支持fully qualified URLs(完全合格的url),比如 "file:/Users/feilong/workspace/loxia/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持classpath pseudo-URLs(伪url), 比如 "classpath:loxia/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持relative file paths(相对路径), 比如 "WEB-INF/TradeData-list-export.xlsx".</li>
     *            </ol>
     * @param xmlSheetConfigurations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            the sheet
     * @param beans
     *            the beans
     * @param outputFileName
     *            输出目录,注意是绝对地址
     */
    public static void write(
                    String excelTemplateLocation,
                    String xmlSheetConfigurations,
                    String sheetName,
                    Map<String, Object> beans,
                    String outputFileName){
        write(excelTemplateLocation, xmlSheetConfigurations, toArray(sheetName), beans, outputFileName);
    }

    /**
     * Write.
     * 
     * @param excelTemplateLocation
     *            excel模板location,
     * 
     *            <ol>
     *            <li>支持fully qualified URLs(完全合格的url),比如 "file:/Users/feilong/workspace/loxia/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持classpath pseudo-URLs(伪url), 比如 "classpath:loxia/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持relative file paths(相对路径), 比如 "WEB-INF/TradeData-list-export.xlsx".</li>
     *            </ol>
     * @param xmlSheetConfigurations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetNames
     *            the sheet names
     * @param beans
     *            the beans
     * @param outputFileName
     *            输出文件名字(全路径)
     */
    public static void write(
                    String excelTemplateLocation,
                    String xmlSheetConfigurations,
                    String[] sheetNames,
                    Map<String, Object> beans,
                    String outputFileName){
        Date beginDate = new Date();

        InputStream inputStream = getInputStream(excelTemplateLocation);
        write(inputStream, xmlSheetConfigurations, sheetNames, beans, outputFileName);

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            Map<String, Object> map = buildMap(excelTemplateLocation, xmlSheetConfigurations, sheetNames, beans, outputFileName, beginDate);
            LOGGER.info("write excel [SUCCESS],params info:[{}]", JsonUtil.format(map));
        }
    }

    //---------------------------------------------------------------

    /**
     * Write.
     * 
     * @param excelTemplateLocationInputStream
     *            the input stream template file name
     * @param xmlSheetConfigurations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            the sheet
     * @param beans
     *            the beans
     * @param outputFileName
     *            输出文件名字(全路径)
     * @since 1.2.1
     */
    public static void write(
                    InputStream excelTemplateLocationInputStream,
                    String xmlSheetConfigurations,
                    String sheetName,
                    Map<String, Object> beans,
                    String outputFileName){
        write(excelTemplateLocationInputStream, xmlSheetConfigurations, toArray(sheetName), beans, outputFileName);
    }

    /**
     * Write.
     * 
     * @param excelTemplateLocationInputStream
     *            the input stream template file name
     * @param xmlSheetConfigurations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            the sheet name
     * @param beans
     *            the beans
     * @param outputFileName
     *            输出文件名字(全路径)
     */
    public static void write(
                    InputStream excelTemplateLocationInputStream,
                    String xmlSheetConfigurations,
                    String[] sheetName,
                    Map<String, Object> beans,
                    String outputFileName){
        Date beginDate = new Date();

        OutputStream outputStream = FileUtil.getFileOutputStream(outputFileName);
        write(excelTemplateLocationInputStream, xmlSheetConfigurations, sheetName, beans, outputStream);

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            Map<String, Object> map = build(
                            excelTemplateLocationInputStream,
                            xmlSheetConfigurations,
                            sheetName,
                            beans,
                            outputFileName,
                            beginDate);
            LOGGER.debug("write excel [SUCCESS],params info:[{}]", JsonUtil.format(map));
        }
    }

    //---------------------------------------------------------------

    /**
     * Write.
     * 
     * @param excelTemplateLocationInputStream
     *            the input stream template file name
     * @param xmlSheetConfigurations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            sheetName
     * @param beans
     *            the beans
     * @param outputFileOutputStream
     *            the output file output stream
     * @since 1.2.1
     */
    public static void write(
                    InputStream excelTemplateLocationInputStream,
                    String xmlSheetConfigurations,
                    String sheetName,
                    Map<String, Object> beans,
                    OutputStream outputFileOutputStream){
        write(excelTemplateLocationInputStream, xmlSheetConfigurations, toArray(sheetName), beans, outputFileOutputStream);
    }

    /**
     * Write.
     * 
     * @param excelTemplateLocationInputStream
     *            the input stream template file name
     * @param xmlSheetConfigurations
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetNames
     *            the sheet names
     * @param beans
     *            the beans
     * @param outputStream
     *            输出文件流
     */
    public static void write(
                    InputStream excelTemplateLocationInputStream,
                    String xmlSheetConfigurations,
                    String[] sheetNames,
                    Map<String, Object> beans,
                    OutputStream outputStream){
        Date beginDate = new Date();

        Validate.notNull(excelTemplateLocationInputStream, "excelTemplateLocationInputStream can't be null!");
        Validate.notNull(outputStream, "outputStream can't be null!");

        Validate.notBlank(xmlSheetConfigurations, "xmlSheetConfigurations can't be blank!");

        //---------------------------------------------------------------
        ExcelManipulatorFactory excelManipulatorFactory = new ExcelManipulatorFactory();
        excelManipulatorFactory.setConfig(xmlSheetConfigurations);

        //---------------------------------------------------------------
        ExcelWriter excelWriter = excelManipulatorFactory.createExcelWriter(sheetNames);
        excelWriter.write(excelTemplateLocationInputStream, outputStream, beans);

        //---------------------------------------------------------------
        Map<String, Object> map = build(
                        excelTemplateLocationInputStream,
                        xmlSheetConfigurations,
                        sheetNames,
                        beans,
                        outputStream,
                        beginDate);
        LOGGER.debug("write excel [SUCCESS],params info:[{}]", JsonUtil.format(map));
    }

    //---------------------------------------------------------------

    private static Map<String, Object> build(
                    InputStream excelTemplateLocationInputStream,
                    String xmlSheetConfigurations,
                    String[] sheetNames,
                    Map<String, Object> beans,
                    OutputStream outputStream,
                    Date beginDate){
        Map<String, Object> map = newLinkedHashMap();
        map.put("excelTemplateLocationInputStream class", excelTemplateLocationInputStream.getClass());
        map.put("xmlSheetConfigurations", xmlSheetConfigurations);
        map.put("sheetNames", sheetNames);

        map.put("outputFileOutputStream class", outputStream.getClass());
        map.put("data info", toDataInfo(beans));
        map.put("useTime", formatDuration(beginDate));
        return map;
    }

    /**
     * @param beans
     * @return
     * @since 3.0.0
     */
    private static Map<String, Integer> toDataInfo(Map<String, Object> beans){
        Map<String, Integer> map = newLinkedHashMap();

        if (isNotNullOrEmpty(beans)){
            for (Map.Entry<String, Object> entry : beans.entrySet()){
                String key = entry.getKey();
                Object value = entry.getValue();
                if (ClassUtil.isInstance(value, Collection.class)){
                    map.put(key + " size", CollectionUtils.size(value));
                }
            }
        }
        return map;
    }

    //---------------------------------------------------------------

    private static Map<String, Object> buildMap(
                    String excelTemplateLocation,
                    String xmlSheetConfigurations,
                    String[] sheetNames,
                    Map<String, Object> beans,
                    String outputFileName,
                    Date beginDate){
        Map<String, Object> map = newLinkedHashMap();
        map.put("excelTemplateLocation", excelTemplateLocation);
        map.put("xmlSheetConfigurations", xmlSheetConfigurations);
        map.put("sheetName", sheetNames);
        map.put("outputFileName", outputFileName);
        map.put("data info", toDataInfo(beans));
        map.put("useTime", formatDuration(beginDate));
        return map;
    }

    private static Map<String, Object> build(
                    InputStream excelTemplateLocationInputStream,
                    String xmlSheetConfigurations,
                    String[] sheetName,
                    Map<String, Object> beans,
                    String outputFileName,
                    Date beginDate){
        Map<String, Object> map = newLinkedHashMap();
        map.put("excelTemplateLocationInputStream class", excelTemplateLocationInputStream.getClass());
        map.put("xmlSheetConfigurations", xmlSheetConfigurations);
        map.put("sheetName", sheetName);
        map.put("outputFileName", outputFileName);
        map.put("data info", toDataInfo(beans));
        map.put("useTime", formatDuration(beginDate));
        return map;
    }

    //---------------------------------------------------------------

    /**
     * Return a Resource handle for the specified resource location.
     * <p>
     * The handle should always be a reusable resource descriptor,allowing for multiple {@link Resource#getInputStream()} calls.
     * <p>
     * <ul>
     * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".</li>
     * <li>Must support classpath pseudo-URLs, e.g. "classpath:test.dat".</li>
     * <li>Should support relative file paths, e.g. "WEB-INF/test.dat".</li>
     * (This will be implementation-specific, typically provided by an ApplicationContext implementation.)
     * </ul>
     * <p>
     * Note that a Resource handle does not imply an existing resource; you need to invoke {@link Resource#exists} to check for existence.
     *
     * @param location
     *            the url or path
     * @return the resource
     * @see org.springframework.core.io.DefaultResourceLoader#DefaultResourceLoader()
     * @see org.springframework.core.io.ResourceLoader
     */
    private static Resource getResource(String location){
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        return resourceLoader.getResource(location);
    }

    /**
     * Return a Resource handle for the specified resource location.
     * <p>
     * The handle should always be a reusable resource descriptor,allowing for multiple {@link Resource#getInputStream()} calls.
     * <p>
     * <ul>
     * <li>Must support fully qualified URLs, e.g. "file:C:/test.dat".</li>
     * <li>Must support classpath pseudo-URLs, e.g. "classpath:test.dat".</li>
     * <li>Should support relative file paths, e.g. "WEB-INF/test.dat".</li>
     * (This will be implementation-specific, typically provided by an ApplicationContext implementation.)
     * </ul>
     * <p>
     * Note that a Resource handle does not imply an existing resource; you need to invoke {@link Resource#exists} to check for existence.
     *
     *
     * @param location
     *            the url or path
     * @return the input stream
     * @since 4.2.0
     */
    private static InputStream getInputStream(String location){
        Resource resource = getResource(location);
        try{
            return resource.getInputStream();
        }catch (IOException e){
            throw new UncheckedIOException("location:[" + location + "]", e);
        }
    }

}
