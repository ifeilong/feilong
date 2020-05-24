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
package com.feilong.component.download;

import static com.feilong.core.date.DateUtil.nowTimestamp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.feilong.context.RequestDataBuilder;
import com.feilong.context.filecreator.RequestFileCreator;
import com.feilong.core.lang.SystemUtil;
import com.feilong.excel.ExcelWriteUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 基于request 来生成 excel文件.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class ExcelRequestFileCreator implements RequestFileCreator{

    /** excel template location. */
    private String             templateLocation;

    /** The sheet definition location. */
    private String             sheetDefinitionLocation;

    private String             sheetName;

    //---------------------------------------------------------------

    /** The request data builder. */
    private RequestDataBuilder requestDataBuilder;

    //---------------------------------------------------------------

    /**
     * 创建.
     *
     * @param request
     *            the request
     * @return the string
     */
    @Override
    public String create(HttpServletRequest request){
        String outputFileName = Slf4jUtil.format(
                        SystemUtil.USER_HOME + "/feilong/excel/{}{}.{}",
                        sheetName,
                        nowTimestamp(),
                        FilenameUtil.getExtension(templateLocation));

        //---------------------------------------------------------------
        Map<String, Object> data = requestDataBuilder.build(request);

        ExcelWriteUtil.write(templateLocation, sheetDefinitionLocation, sheetName, data, outputFileName);
        return outputFileName;
    }
    //---------------------------------------------------------------

    /**
     * Sets the template location.
     *
     * @param templateLocation
     *            the templateLocation to set
     */
    public void setTemplateLocation(String templateLocation){
        this.templateLocation = templateLocation;
    }

    /**
     * Sets the sheet definition location.
     *
     * @param sheetDefinitionLocation
     *            the sheetDefinitionLocation to set
     */
    public void setSheetDefinitionLocation(String sheetDefinitionLocation){
        this.sheetDefinitionLocation = sheetDefinitionLocation;
    }

    /**
     * Sets the request data builder.
     *
     * @param requestDataBuilder
     *            the requestDataBuilder to set
     */
    public void setRequestDataBuilder(RequestDataBuilder requestDataBuilder){
        this.requestDataBuilder = requestDataBuilder;
    }

    /**
     * @param sheetName
     *            the sheetName to set
     */
    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

}
