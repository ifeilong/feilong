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

import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.json.JsonUtil;

class ExcelSheetMapBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelSheetMapBuilder.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ExcelSheetMapBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 
     * @param sheetDefinitionLocations
     * @return key是sheet Name
     */
    static Map<String, ExcelSheet> build(String...sheetDefinitionLocations){
        Validate.notEmpty(sheetDefinitionLocations, "sheetDefinitionLocations can't be null/empty!");

        Map<String, ExcelSheet> sheetDefinitionsMap = newLinkedHashMap(sheetDefinitionLocations.length);
        for (String sheetDefinitionPath : sheetDefinitionLocations){
            Validate.notBlank(sheetDefinitionPath, "sheetDefinitionPath can't be blank!");
            try{
                long beginTimeMillis = System.currentTimeMillis();
                List<ExcelSheet> excelSheetList = ExcelSheetListBuilder.build(sheetDefinitionPath);
                for (ExcelSheet excelSheet : excelSheetList){
                    sheetDefinitionsMap.put(defaultIfNullOrEmpty(excelSheet.getName(), EMPTY), excelSheet);
                }
                //---------------------------------------------------------------
                if (LOGGER.isDebugEnabled()){
                    int size = size(excelSheetList);
                    LOGGER.debug(
                                    "parse [{}],sheetList size:[{}],use time: [{}]",
                                    sheetDefinitionPath,
                                    size,
                                    formatDurationUseBeginTimeMillis(beginTimeMillis));
                }
            }catch (Exception e){
                throw new DefaultRuntimeException("parse [" + sheetDefinitionPath + "] fail", e);
            }
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            String pattern = "parse sheetDefinitionLocations:[{}],sheetDefinitionsMap:[{}]";
            LOGGER.debug(pattern, sheetDefinitionLocations, JsonUtil.toString(sheetDefinitionsMap));
        }
        //---------------------------------------------------------------
        return sheetDefinitionsMap;
    }

}
