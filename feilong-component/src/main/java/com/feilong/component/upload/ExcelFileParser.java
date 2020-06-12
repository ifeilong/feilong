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
package com.feilong.component.upload;

import java.util.Map;

import com.feilong.context.DataHandler;
import com.feilong.context.fileparser.FileParser;
import com.feilong.excel.ExcelReaderUtil;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class ExcelFileParser implements FileParser{

    private String      sheetDefinitionLocation;

    private String      sheetName;

    /**
     * 如果只有1个sheet ,且只有1个dataName 可以省略
     */
    private String      dataName;

    //---------------------------------------------------------------

    private DataHandler dataHandler;

    //---------------------------------------------------------------

    @Override
    public void parse(String fileLocation){
        Map<String, Object> readData = ExcelReaderUtil.readData(fileLocation, sheetDefinitionLocation, sheetName, 0);
        dataHandler.handle(readData);
    }

    //---------------------------------------------------------------

    /**
     * @param sheetDefinitionLocation
     *            the sheetDefinitionLocation to set
     */
    public void setSheetDefinitionLocation(String sheetDefinitionLocation){
        this.sheetDefinitionLocation = sheetDefinitionLocation;
    }

    /**
     * @param sheetName
     *            the sheetName to set
     */
    public void setSheetName(String sheetName){
        this.sheetName = sheetName;
    }

    /**
     * @param dataName
     *            the dataName to set
     */
    public void setDataName(String dataName){
        this.dataName = dataName;
    }

    /**
     * @param dataHandler
     *            the dataHandler to set
     */
    public void setDataHandler(DataHandler dataHandler){
        this.dataHandler = dataHandler;
    }

}
