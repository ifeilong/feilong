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
package com.feilong.office.excel.reader;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.ExcelManipulateException;
import com.feilong.office.excel.utils.CellReferenceUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class CellValueGetter{

    private static final Logger LOGGER = LoggerFactory.getLogger(CellValueGetter.class);

    /**
     * Gets the cell value.
     *
     * @param cell
     *            the cell
     * @param formulaEvaluator
     *            the evaluator
     * @return the cell value
     * @throws ExcelManipulateException
     *             the excel manipulate exception
     */
    static Object get(Cell cell,FormulaEvaluator formulaEvaluator){
        if (cell == null){
            return null;
        }
        //---------------------------------------------------------------
        Object value = null;
        CellValue cellValue = formulaEvaluator.evaluate(cell);
        if (cellValue == null){
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("{}: null", CellReferenceUtil.getCellIndex(cell.getRowIndex(), cell.getColumnIndex()));
            }
            return null;
        }

        //---------------------------------------------------------------
        switch (cellValue.getCellType()) {
            case BLANK:
            case ERROR:
                break;
            case BOOLEAN:
                value = cellValue.getBooleanValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    value = DateUtil.getJavaDate(cellValue.getNumberValue());
                }else{
                    value = cellValue.getNumberValue();
                }
                break;
            case STRING:
                value = cellValue.getStringValue();
        }

        //---------------------------------------------------------------
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("{}: {}", CellReferenceUtil.getCellIndex(cell.getRowIndex(), cell.getColumnIndex()), value);
        }

        return value;
    }
}
