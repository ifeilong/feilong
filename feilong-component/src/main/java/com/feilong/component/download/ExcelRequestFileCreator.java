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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.feilong.context.RequestDataBuilder;
import com.feilong.context.filecreator.RequestFileCreator;
import com.feilong.excel.ExcelWriteUtil;

/**
 * 基于request 来生成 excel文件.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class ExcelRequestFileCreator implements RequestFileCreator{

    /** excel template location. */
    private String             templateLocation;

    /** The sheet definition location. */
    private String             sheetDefinitionLocation;

    //---------------------------------------------------------------

    @Deprecated
    private String             sheetName;

    private String             outputFileName;

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
        Map<String, Object> data = requestDataBuilder.build(request);

        return ExcelWriteUtil.write(templateLocation, sheetDefinitionLocation, sheetName, data, outputFileName);
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
     * @Deprecated
     */
    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

    /**
     * @param outputFileName
     *            the outputFileName to set
     */
    public void setOutputFileName(String outputFileName){
        this.outputFileName = outputFileName;
    }

}
