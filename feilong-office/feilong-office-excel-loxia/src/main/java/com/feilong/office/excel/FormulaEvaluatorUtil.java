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

import static com.feilong.core.date.DateUtil.formatDuration;

import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class FormulaEvaluatorUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FormulaEvaluatorUtil.class);

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private FormulaEvaluatorUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Re calculate workbook.
     *
     * @param workbook
     *            the wb
     */
    static void reCalculate(Workbook workbook){
        Date beginDate = new Date();

        FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
        for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++){
            Sheet sheet = workbook.getSheetAt(sheetNum);
            for (Row row : sheet){
                for (Cell cell : row){
                    if (cell.getCellType() == CellType.FORMULA){
                        formulaEvaluator.evaluateFormulaCell(cell);
                    }
                }
            }
        }

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("reCalculate use time: [{}]", formatDuration(beginDate));
        }

    }
}
