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

import static com.feilong.core.date.DateUtil.formatDuration;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester3.Digester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.lib.collection4.CollectionUtils;

class ExcelSheetMapBuilder{

    private static final Logger   LOGGER    = LoggerFactory.getLogger(ExcelSheetMapBuilder.class);

    /** The Constant RULE_FILE. */
    private static final String   RULE_FILE = "config/excel/definition-rule.xml";

    private static final Digester DIGESTER  = DigesterCreater.create(RULE_FILE);

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
     * @param configurations
     * @return key是sheet Name
     * @since 3.0.0
     */
    static Map<String, ExcelSheet> build(String...configurations){
        Map<String, ExcelSheet> sheetDefinitions = new HashMap<>();

        Validate.notEmpty(configurations, "configurations can't be null/empty!");
        //---------------------------------------------------------------

        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        for (String configuration : configurations){
            Validate.notBlank(configuration, "config can't be blank!");
            try{
                Date beginDate = new Date();
                InputStream inputStream = contextClassLoader.getResourceAsStream(configuration);
                List<ExcelSheet> excelSheetList = DIGESTER.parse(inputStream);
                for (ExcelSheet excelSheet : excelSheetList){
                    sheetDefinitions.put(excelSheet.getName(), excelSheet);
                }
                //---------------------------------------------------------------
                if (LOGGER.isDebugEnabled()){
                    int size = CollectionUtils.size(excelSheetList);
                    LOGGER.debug("parse [{}],list size:[{}],use time: [{}]", configuration, size, formatDuration(beginDate));
                }
            }catch (Exception e){
                throw new DefaultRuntimeException("parse [" + configuration + "] fail", e);
            }
        }
        return sheetDefinitions;
    }

}
