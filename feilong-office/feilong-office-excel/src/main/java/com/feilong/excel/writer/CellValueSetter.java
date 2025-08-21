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

import java.math.BigDecimal;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.feilong.excel.util.Config;
import com.feilong.lib.excel.ognl.OgnlStack;

@lombok.extern.slf4j.Slf4j
class CellValueSetter{

    /** Don't let anyone instantiate this class. */
    private CellValueSetter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 设置 cell value.
     *
     * @param sheet
     *            某个sheet
     * @param rowIndex
     *            第几行
     * @param cellIndex
     *            第几列
     * @param dataName
     *            数据dataName
     * @param stack
     *            the stack
     */
    static void set(Sheet sheet,int rowIndex,int cellIndex,String dataName,OgnlStack stack){
        if (dataName.equals("#")){
            return;
        }

        //---------------------------------------------------------------
        Row row = sheet.getRow(rowIndex);
        if (row == null){
            row = sheet.createRow(rowIndex);
        }
        Cell cell = row.getCell(cellIndex);
        if (cell == null){
            cell = row.createCell(cellIndex);
        }

        //---------------------------------------------------------------
        if (dataName.startsWith("=")){
            //formula
            cell.setCellFormula(dataName.substring(1));
            return;
        }

        //---------------------------------------------------------------
        //data
        try{
            Object value = stack.getValue(dataName);
            set(cell, value);
        }catch (Exception e){
            //do nothing now
            log.error("", e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 cell value.
     *
     * @param cell
     *            the cell
     * @param value
     *            the value
     */
    private static void set(Cell cell,Object value){
        if (value == null){
            cell.setCellValue((String) null);
            return;
        }

        //---------------------------------------------------------------
        CellType cellType = cell.getCellType();
        if (value instanceof Date){
            //if current cell do not formatted as date, set is as one date          
            if (cellType != CellType.NUMERIC){
                cell.setCellType(CellType.NUMERIC);
            }
            if (!DateUtil.isCellDateFormatted(cell)){
                Workbook workbook = cell.getSheet().getWorkbook();

                CellStyle cellStyle = workbook.createCellStyle();
                if (cell.getCellStyle() == null){
                    cellStyle.cloneStyleFrom(cell.getCellStyle());
                }
                cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(Config.get("date.pattern")));
                cell.setCellStyle(cellStyle);
            }
        }

        //---------------------------------------------------------------
        if (cellType == CellType.BLANK || cellType == CellType.ERROR || cellType == CellType.FORMULA || cellType == CellType.NUMERIC){
            //set cell value without data type transform            
            if (value instanceof Integer){
                cell.setCellValue(new BigDecimal((Integer) value).doubleValue());
            }else if (value instanceof Long){
                cell.setCellValue(new BigDecimal((Long) value).doubleValue());
            }else if (value instanceof Double){
                cell.setCellValue((Double) value);
            }else if (value instanceof BigDecimal){
                cell.setCellValue(((BigDecimal) value).doubleValue());
            }else if (value instanceof Date){
                cell.setCellValue((Date) value);
            }else{
                if (cellType == CellType.NUMERIC){
                    try{
                        cell.setCellValue(Double.parseDouble(value.toString()));
                    }catch (NumberFormatException e){
                        //do nothing
                    }
                }else{
                    cell.setCellValue(value.toString());
                }
            }
            return;
        }

        //---------------------------------------------------------------
        if (cellType == CellType.BOOLEAN){
            if (value instanceof Boolean){
                cell.setCellValue((Boolean) value);
            }
            return;
        }

        //---------------------------------------------------------------
        cell.setCellValue(value.toString());
    }
}
