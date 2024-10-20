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

/**
 * 抽象的父类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
abstract class AbstractExcelConfig implements ExcelConfig{

    /** The definition. */
    protected ExcelDefinition excelDefinition;

    //---------------------------------------------------------------

    /**
     * Gets the definition.
     *
     * @return the definition
     */
    @Override
    public ExcelDefinition getDefinition(){
        return excelDefinition;
    }

    /**
     * Sets the definition.
     *
     * @param definition
     *            the new definition
     */
    @Override
    public void setDefinition(ExcelDefinition definition){
        this.excelDefinition = definition;
    }
}
