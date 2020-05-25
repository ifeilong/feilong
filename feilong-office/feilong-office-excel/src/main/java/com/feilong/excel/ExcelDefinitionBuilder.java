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

import static com.feilong.core.bean.ConvertUtil.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.util.SheetNamesUtil;
import com.feilong.lib.collection4.MapUtils;

/**
 * The Class ExcelDefinitionBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class ExcelDefinitionBuilder{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDefinitionBuilder.class);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private ExcelDefinitionBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * To excel manipulator definition.
     *
     * @param sheetDefinitionMap
     *            the sheet definitions
     * @param sheetNames
     *            the sheets
     * @return the excel manipulator definition
     */
    static ExcelDefinition build(Map<String, ExcelSheet> sheetDefinitionMap,String...sheetNames){
        ExcelDefinition excelDefinition = new ExcelDefinition();

        List<ExcelSheet> excelSheets = new ArrayList<>();

        //如果sheetNames 没有配置, 且sheetDefinitions 只有1个
        if (SheetNamesUtil.isEmptyOrNullElement(sheetNames) && MapUtils.size(sheetDefinitionMap) == 1){
            LOGGER.debug("sheetNames isEmptyOrNullElement,and sheetDefinitionMap is only 1,use ExcelSheet");
            excelSheets.addAll(sheetDefinitionMap.values());
        }else{
            for (String sheetName : sheetNames){
                excelSheets.add(toExcelSheet(sheetDefinitionMap, sheetName));
            }
        }
        //---------------------------------------------------------------
        excelDefinition.setExcelSheets(toList(sheetDefinitionMap.values()));
        return excelDefinition;
    }

    //---------------------------------------------------------------

    /**
     * Gets the excel sheet.
     *
     * @param sheetDefinitions
     *            the sheet definitions
     * @param sheetName
     *            the sheet
     * @return the excel sheet
     */
    private static ExcelSheet toExcelSheet(Map<String, ExcelSheet> sheetDefinitions,String sheetName){
        if ("blank".equalsIgnoreCase(sheetName)){
            return new ExcelSheet();
        }
        //---------------------------------------------------------------
        ExcelSheet excelSheet = sheetDefinitions.get(sheetName);
        Validate.notNull(excelSheet, "No sheet defintion found with name: %s", sheetName);
        return excelSheet;
    }
}
