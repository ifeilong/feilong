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

import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.feilong.excel.ExcelDefinition;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.util.WorkbookUtil;
import com.feilong.lib.loxia.util.OgnlStack;

/**
 * The Class WorkbookWriter.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class WorkbookWriter{

    /** Don't let anyone instantiate this class. */
    private WorkbookWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * Write per sheet.
     *
     * @param workbook
     *            the workbook
     * @param outputStream
     *            the output stream
     * @param definition
     *            the definition
     * @param beansList
     *            the beans list
     */
    public static void writePerSheet(
                    Workbook workbook,
                    OutputStream outputStream,
                    ExcelDefinition definition,
                    List<Map<String, Object>> beansList){
        List<ExcelSheet> excelSheets = definition.getExcelSheets();
        int numberOfSheets = workbook.getNumberOfSheets();
        int excelSheetsSize = excelSheets.size();
        validate(excelSheetsSize, numberOfSheets);

        Map<String, CellStyle> styleMap = StyleMapBuilder.build(workbook, definition, excelSheets);

        //---------------------------------------------------------------
        //remove sheets except the first one
        for (int i = numberOfSheets - 1; i > 0; i--){
            workbook.removeSheetAt(i);
        }
        for (int i = 0; i < beansList.size(); i++){
            Sheet newSheet = workbook.createSheet("Auto Generated Sheet " + i);
            SheetCopyer.copy(workbook.getSheetAt(0), newSheet);
            SheetWriter.write(newSheet, excelSheets.iterator().next(), new OgnlStack(beansList.get(i)), styleMap);
        }
        //---------------------------------------------------------------
        //remove template sheet
        workbook.removeSheetAt(0);
        pack(workbook, outputStream);
    }

    /**
     * Write.
     *
     * @param workbook
     *            the workbook
     * @param outputStream
     *            the output stream
     * @param excelDefinition
     *            the definition
     * @param beans
     *            the beans
     */
    public static void write(Workbook workbook,OutputStream outputStream,ExcelDefinition excelDefinition,Map<String, Object> beans){
        List<ExcelSheet> excelSheets = excelDefinition.getExcelSheets();
        int excelSheetsSize = excelSheets.size();
        int numberOfSheets = workbook.getNumberOfSheets();
        validate(excelSheetsSize, numberOfSheets);

        Map<String, CellStyle> styleMap = StyleMapBuilder.build(workbook, excelDefinition, excelSheets);
        for (int i = 0; i < excelSheetsSize; i++){
            SheetWriter.write(workbook.getSheetAt(i), excelSheets.get(i), new OgnlStack(beans), styleMap);
        }
        pack(workbook, outputStream);
    }

    //---------------------------------------------------------------

    /**
     * Validate.
     *
     * @param excelSheetsSize
     *            the excel sheets size
     * @param numberOfSheets
     *            the number of sheets
     */
    private static void validate(int excelSheetsSize,int numberOfSheets){
        Validate.isTrue(
                        excelSheetsSize > 0 && numberOfSheets >= excelSheetsSize,
                        "No sheet definition found or Sheet Number in definition is more than number in template file.");
    }

    //---------------------------------------------------------------

    /**
     * Pack.
     *
     * @param workbook
     *            the workbook
     * @param outputStream
     *            the output stream
     * @throws UncheckedIOException
     *             the unchecked IO exception
     */
    private static void pack(Workbook workbook,OutputStream outputStream){
        FormulaEvaluatorUtil.reCalculate(workbook);
        workbook.setActiveSheet(0);
        WorkbookUtil.write(workbook, outputStream);
    }

}
