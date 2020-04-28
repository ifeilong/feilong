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

import java.io.InputStream;
import java.util.Map;

import com.feilong.office.excel.definition.ExcelManipulatorDefinition;

/**
 * The Interface ExcelReader.
 */
public interface ExcelReader{

    /**
     * Read All sheets infos into one beans scope with multiple sheet definitions.
     *
     * @param is
     *            the is
     * @param beans
     *            the beans
     * @return the read status
     */
    ReadStatus readAll(InputStream is,Map<String, Object> beans);

    /**
     * Read All sheets using one sheet definition to get colletion infos.
     *
     * @param is
     *            the is
     * @param beans
     *            the beans
     * @return the read status
     */
    ReadStatus readAllPerSheet(InputStream is,Map<String, Object> beans);

    /**
     * Read specific sheet using one sheet definition.
     *
     * @param is
     *            the is
     * @param sheetNo
     *            the sheet no
     * @param beans
     *            the beans
     * @return the read status
     */
    ReadStatus readSheet(InputStream is,int sheetNo,Map<String, Object> beans);

    //---------------------------------------------------------------

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    ExcelManipulatorDefinition getDefinition();

    /**
     * Sets the definition.
     *
     * @param definition
     *            the new definition
     */
    void setDefinition(ExcelManipulatorDefinition definition);
}
