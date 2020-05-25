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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.date.DateUtil.nowTimestamp;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.lang.SystemUtil.USER_HOME;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static java.util.Collections.emptyMap;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.core.lang.ClassUtil;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.util.SheetNamesUtil;
import com.feilong.io.FileUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.collection4.CollectionUtils;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 进行 excel 输出的工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
public class ExcelWriteUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelWriteUtil.class);

    //---------------------------------------------------------------

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
     * @param templateLocation
     *            excel模板location,
     * 
     *            <ol>
     *            <li>支持fully qualified URLs(完全合格的url),比如 "file:/Users/feilong/workspace/excel/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持classpath pseudo-URLs(伪url), 比如 "classpath:excel/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持relative file paths(相对路径), 比如 "WEB-INF/TradeData-list-export.xlsx".</li>
     *            </ol>
     * @param sheetDefinitionLocation
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetName
     *            the sheet
     * @param beans
     *            the beans
     * @param outputFileName
     *            输出文件名字(全路径),<br>
     *            如果是null,默认输出地址是 USER_HOME +/feilong/excel/{sheetNames}{nowTimestamp()}.{FilenameUtil.getExtension(templateLocation)}
     * @return the string
     */
    public static String write(
                    String templateLocation,
                    String sheetDefinitionLocation,
                    String sheetName,

                    Map<String, Object> beans,
                    String outputFileName){

        String[] sheetNames = null == sheetName ? (String[]) null : toArray(sheetName);
        return write(templateLocation, sheetDefinitionLocation, sheetNames, beans, outputFileName);
    }

    /**
     * Write.
     *
     * @param templateLocation
     *            excel模板location,
     * 
     *            <ol>
     *            <li>支持fully qualified URLs(完全合格的url),比如 "file:/Users/feilong/workspace/excel/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持classpath pseudo-URLs(伪url), 比如 "classpath:excel/TradeData/TradeData-list-export.xlsx".</li>
     *            <li>支持relative file paths(相对路径), 比如 "WEB-INF/TradeData-list-export.xlsx".</li>
     *            </ol>
     * @param sheetDefinitionLocation
     *            xml sheet相关配置文件, 如 sheets/train-course.xml,基于class path路径
     * @param sheetNames
     *            the sheet names
     * @param beans
     *            the beans
     * @param outputFileName
     *            输出文件名字(全路径),<br>
     *            如果是null,默认输出地址是 USER_HOME +/feilong/excel/{sheetNames}{nowTimestamp()}.{FilenameUtil.getExtension(templateLocation)}
     * @return 如果 <code>templateLocation</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>templateLocation</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>sheetDefinitionLocation</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>sheetDefinitionLocation</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static String write(
                    String templateLocation,
                    String sheetDefinitionLocation,
                    String[] sheetNames,

                    Map<String, Object> beans,
                    String outputFileName){
        Date beginDate = new Date();

        Validate.notBlank(templateLocation, "templateLocation can't be blank!");
        Validate.notBlank(sheetDefinitionLocation, "sheetDefinitionLocation can't be blank!");

        Map<String, ExcelSheet> sheetDefinitions = ExcelSheetMapBuilder.build(sheetDefinitionLocation);

        String useOutputFileName = defaultIfNullOrEmpty(
                        outputFileName,
                        buildDefaultOutputFileName(templateLocation, sheetNames, sheetDefinitions));

        if (LOGGER.isDebugEnabled()){
            Map<String, Object> map = build(templateLocation, sheetDefinitionLocation, sheetNames, beans, useOutputFileName);
            LOGGER.debug("will write excel,params info:[{}]", JsonUtil.format(map));
        }
        //---------------------------------------------------------------
        OutputStream outputStream = FileUtil.getFileOutputStream(useOutputFileName);
        Validate.notNull(outputStream, "outputStream can't be null!");

        ExcelWriter excelWriter = buildExcelWriter(sheetDefinitions, sheetNames);
        excelWriter.write(templateLocation, outputStream, beans);

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            Map<String, Object> map = buildMap(templateLocation, sheetDefinitionLocation, sheetNames, beans, useOutputFileName, beginDate);
            LOGGER.info("write [SUCCESS],params info:[{}]", JsonUtil.format(map));
        }

        return useOutputFileName;
    }

    private static String buildDefaultOutputFileName(String templateLocation,String[] sheetNames,Map<String, ExcelSheet> sheetDefinitions){
        String pattern = USER_HOME + "/feilong/excel/{}{}.{}";
        return Slf4jUtil.format(
                        pattern,
                        buildFileName(sheetNames, sheetDefinitions),
                        nowTimestamp(),
                        FilenameUtil.getExtension(templateLocation));
    }

    private static String buildFileName(String[] sheetNames,Map<String, ExcelSheet> sheetDefinitions){
        if (!SheetNamesUtil.isEmptyOrNullElement(sheetNames)){
            return sheetNames.toString();
        }
        if (sheetDefinitions.size() == 1){
            List<String> list = toList(sheetDefinitions.keySet());
            String string = list.get(0);
            return defaultIfNullOrEmpty(string, EMPTY);
        }
        return EMPTY;
    }

    /**
     * Builds the excel writer.
     *
     * @param sheetDefinitionLocation
     *            the sheet definition location
     * @param sheetNames
     *            the sheet names
     * @return the excel writer
     */
    private static ExcelWriter buildExcelWriter(Map<String, ExcelSheet> sheetDefinitions,String[] sheetNames){
        ExcelDefinition excelDefinition = ExcelDefinitionBuilder.build(sheetDefinitions, sheetNames);
        return new DefaultExcelWriter(excelDefinition);
    }

    //---------------------------------------------------------------

    /**
     * To data info.
     *
     * @param beans
     *            the beans
     * @return the map
     */
    private static Map<String, Integer> toDataInfo(Map<String, Object> beans){
        if (isNullOrEmpty(beans)){
            return emptyMap();
        }

        //---------------------------------------------------------------
        Map<String, Integer> map = newLinkedHashMap();
        for (Map.Entry<String, Object> entry : beans.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if (ClassUtil.isInstance(value, Collection.class)){
                map.put(key + " size", CollectionUtils.size(value));
            }
        }
        return map;
    }

    //---------------------------------------------------------------

    /**
     * Builds the map.
     *
     * @param templateLocation
     *            the template location
     * @param sheetDefinitionLocation
     *            the sheet definition location
     * @param sheetNames
     *            the sheet names
     * @param beans
     *            the beans
     * @param outputFileName
     *            the output file name
     * @param beginDate
     *            the begin date
     * @return the map
     */
    private static Map<String, Object> buildMap(
                    String templateLocation,
                    String sheetDefinitionLocation,
                    String[] sheetNames,

                    Map<String, Object> beans,
                    String outputFileName,
                    Date beginDate){
        Map<String, Object> map = build(templateLocation, sheetDefinitionLocation, sheetNames, beans, outputFileName);
        map.put("useTime", formatDuration(beginDate));
        return map;
    }

    /**
     * Builds the.
     *
     * @param templateLocation
     *            the template location
     * @param sheetDefinitionLocation
     *            the sheet definition location
     * @param sheetNames
     *            the sheet names
     * @param beans
     *            the beans
     * @param outputFileName
     *            the output file name
     * @return the map
     * @since 3.0.0
     */
    private static Map<String, Object> build(
                    String templateLocation,
                    String sheetDefinitionLocation,
                    String[] sheetNames,

                    Map<String, Object> beans,
                    String outputFileName){
        Map<String, Object> map = newLinkedHashMap();
        map.put("templateLocation", templateLocation);
        map.put("sheetDefinitionLocation", sheetDefinitionLocation);
        map.put("sheetName", sheetNames);
        map.put("outputFileName", outputFileName);
        map.put("data info", toDataInfo(beans));
        return map;
    }

}
