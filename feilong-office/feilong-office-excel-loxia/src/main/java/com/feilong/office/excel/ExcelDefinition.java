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

import java.util.ArrayList;
import java.util.List;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.office.excel.definition.ExcelSheet;

/**
 * The Class ExcelManipulatorDefinition.
 */
public class ExcelDefinition{

    /** The style sheet position. */
    private Integer          styleSheetPosition;

    /** The excel sheets. */
    private List<ExcelSheet> excelSheets = new ArrayList<>();

    //---------------------------------------------------------------

    /**
     * Gets the excel sheets.
     *
     * @return the excel sheets
     */
    public List<ExcelSheet> getExcelSheets(){
        return excelSheets;
    }

    /**
     * Sets the excel sheets.
     *
     * @param excelSheets
     *            the new excel sheets
     */
    public void setExcelSheets(List<ExcelSheet> excelSheets){
        this.excelSheets = excelSheets;
    }

    //---------------------------------------------------------------

    /**
     * Gets the style sheet position.
     *
     * @return the style sheet position
     */
    public Integer getStyleSheetPosition(){
        return styleSheetPosition;
    }

    /**
     * Sets the style sheet position.
     *
     * @param styleSheetPosition
     *            the new style sheet position
     */
    public void setStyleSheetPosition(Integer styleSheetPosition){
        this.styleSheetPosition = styleSheetPosition;
    }

    //---------------------------------------------------------------

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString(){
        return "ExcelManipulatorDefinition [styleSheetPosition=" + (styleSheetPosition == null ? "Undefined" : styleSheetPosition)
                        + ",\r\n excelSheets=\r\n\t" + ConvertUtil.toString(excelSheets, "\r\n\t") + "]";
    }

}
