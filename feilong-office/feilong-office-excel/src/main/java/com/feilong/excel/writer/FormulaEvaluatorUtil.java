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
package com.feilong.excel.writer;

import static com.feilong.core.date.DateUtil.formatDurationUseBeginTimeMillis;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.feilong.excel.util.CellReferenceUtil;

@lombok.extern.slf4j.Slf4j
public class FormulaEvaluatorUtil{

    /** The Constant DYNAMIC_CELL_PATTREN. */
    private static final Pattern DYNAMIC_CELL_PATTREN = Pattern.compile("[A-Z][A-Z]?\\d+");

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
    public static void reCalculate(Workbook workbook){
        long beginTimeMillis = System.currentTimeMillis();

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

        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("reCalculate workbook use time: [{}]", formatDurationUseBeginTimeMillis(beginTimeMillis));
        }

    }

    //---------------------------------------------------------------

    /**
     * Offset formula.
     *
     * @param formula
     *            the formula
     * @param rowOffset
     *            the row offset
     * @param colOffset
     *            the col offset
     * @return the string
     */
    static String offsetFormula(String formula,int rowOffset,int colOffset){
        StringBuilder sb = new StringBuilder();
        Matcher matcher = DYNAMIC_CELL_PATTREN.matcher(formula);
        int head = 0;
        int start = 0;
        int end = -1;
        while (matcher.find()){
            start = matcher.start();
            end = matcher.end();

            sb.append(formula.substring(head, start));
            sb.append(CellReferenceUtil.getCellRef(formula.substring(start, end), rowOffset, colOffset));
            head = end;
        }
        sb.append(formula.substring(head));
        return sb.toString();
    }
}
