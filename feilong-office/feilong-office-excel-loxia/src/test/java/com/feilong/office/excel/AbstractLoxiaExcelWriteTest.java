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

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateUtil.nowTimestamp;

import java.util.List;
import java.util.Map;

import com.feilong.coreextension.awt.DesktopUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.office.excel.loxia.LoxiaExcelWriteUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

public abstract class AbstractLoxiaExcelWriteTest{

    private static String templateFloder = "/Users/feilong/workspace/feilong/feilong/feilong-office/feilong-office-excel-loxia/src/test/resources/loxia/";

    protected static <T> void build(String templateFileName,String configurations,String sheetName,String dataName,List<T> list){
        build(templateFloder, templateFileName, configurations, sheetName, dataName, list);
    }

    protected static void build(String templateFileName,String configurations,String[] sheetNames,Map<String, Object> beans){
        build(templateFloder, templateFileName, configurations, sheetNames, beans);
    }
    //---------------------------------------------------------------

    private static <T> void build(
                    String templateFloder,
                    String templateFileName,
                    String configurations,
                    String sheetName,
                    String dataName,
                    List<T> list){
        Map<String, Object> beans = toMap(dataName, (Object) list);

        build(templateFloder, templateFileName, configurations, sheetName, beans);
    }

    //---------------------------------------------------------------

    private static void build(
                    String templateFloder,
                    String templateFileName,
                    String configurations,
                    String sheetName,
                    Map<String, Object> beans){

        build(templateFloder, templateFileName, configurations, toArray(sheetName), beans);
    }

    private static void build(
                    String templateFloder,
                    String templateFileName,
                    String configurations,
                    String[] sheetNames,
                    Map<String, Object> beans){

        String outputFileName = Slf4jUtil.format(
                        "/Users/feilong/Downloads/{}{}.{}",
                        sheetNames,
                        nowTimestamp(),
                        FilenameUtil.getExtension(templateFileName));

        LoxiaExcelWriteUtil.write(templateFloder + templateFileName, configurations, sheetNames, beans, outputFileName);

        DesktopUtil.open(outputFileName);
    }

}
