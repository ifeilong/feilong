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

import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.io.InputStream;
import java.text.Format;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.feilong.core.NumberPattern;
import com.feilong.core.UncheckedIOException;
import com.feilong.core.lang.NumberUtil;

/**
 * 处理Excel文档(POI).
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0.0 2009-5-19下午08:08:37
 * @version 1.0.1 2010年7月7日 12:04:48
 * @version 1.0.2 2014-2-10 02:11
 */
public class ExcelReaderUtil{

    /**
     * 获得单元格值
     * 
     * @param row
     *            HSSFRow
     * @param cellNum
     *            cell索引
     * @return 获得单元格值
     */
    public static String getCellValue(Row row,int cellNum){
        Cell cell = row.getCell(cellNum);
        return getCellValue(cell);
    }

    //---------------------------------------------------------------

    /**
     * 获得单元格值 当null==cell时,返回"".
     * 
     * @param cell
     *            cell
     * @return 获得单元格值
     */
    public static String getCellValue(Cell cell){
        if (null == cell){
            return EMPTY;
        }

        //---------------------------------------------------------------

        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return EMPTY;
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue() + "";
            case Cell.CELL_TYPE_FORMULA:// 公式
                return cell.getCellFormula();
            case Cell.CELL_TYPE_NUMERIC:
                //XXX 重新定义下格式
                double numericCellValue = cell.getNumericCellValue();
                return NumberUtil.toString(numericCellValue, NumberPattern.NO_SCALE);
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString().trim();
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                throw new NotImplementedException("getCellValue:" + cellType + " is not implemented!");
        }
    }

    /**
     * 通过主键值 主键列位置 找到第一个匹配的行.
     * 
     * @param sheet
     *            the sheet
     * @param beginRowIndex
     *            开始行号
     * @param primaryKeyValue
     *            主键值
     * @param primaryKeyColumnNum
     *            主键列位置
     * @return 通过主键值 主键列位置 找到第一个匹配的行
     */
    public static Row getRowByPrimaryKey(Sheet sheet,int beginRowIndex,String primaryKeyValue,int primaryKeyColumnNum){
        int rowCount = getPhysicalNumberOfRows(sheet);
        if (0 == rowCount){
            return null;
        }

        //---------------------------------------------------------------
        for (int i = beginRowIndex; i < rowCount; i++){
            Row row = sheet.getRow(i);
            if (primaryKeyValue.equals(getCellValue(row, primaryKeyColumnNum))){
                return row;
            }
        }
        return null;
    }

    //---------------------------------------------------------------

    /**
     * 创建excel,已经处理了异常.
     * 
     * @param inputStream
     *            inputStream
     * @return HSSFWorkbook
     */
    public HSSFWorkbook createHSSFWorkbook(InputStream inputStream){
        try{
            return new HSSFWorkbook(inputStream);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 获得sheet 总行数.
     * 
     * @param sheet
     *            sheet
     * @return 获得sheet 总行数
     */
    public static int getPhysicalNumberOfRows(Sheet sheet){
        return sheet.getPhysicalNumberOfRows();
    }

    //---------------------------------------------------------------

    /**
     * Gets the cell map for LOGGER.
     * 
     * @param cell
     *            the cell
     * @return the cell map for log
     */
    public static Map<String, Object> getCellMapForLog(Cell cell){
        Map<String, Object> map = newLinkedHashMap();
        map.put("getCellComment", cell.getCellComment());

        int cellType = cell.getCellType();
        map.put("getCellType", cellType);

        map.put("getCellTypeString", CellType.getByValue(cellType));

        //---------------------------------------------------------------
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                break;

            //---------------------------------------------------------------

            case Cell.CELL_TYPE_BOOLEAN:
                map.put("getBooleanCellValue", cell.getBooleanCellValue());//boolean
                break;

            //---------------------------------------------------------------

            case Cell.CELL_TYPE_ERROR:
                map.put("getErrorCellValue", cell.getErrorCellValue());//byte
                break;

            //---------------------------------------------------------------

            case Cell.CELL_TYPE_FORMULA:// 公式
                String cellFormula = cell.getCellFormula();

                int cachedFormulaResultType = cell.getCachedFormulaResultType();
                map.put("getCellFormula", cellFormula);
                map.put("getCachedFormulaResultType", cachedFormulaResultType);

                map.put("cellFormulaResultValue", getCellFormulaResultValue(cell));
                break;

            //---------------------------------------------------------------

            //通过POI取出的数值默认都是double,即使excel单元格中存的是1,取出来的值也是1.0,这就造成了一些问题,
            //如果数据库字段是int,那么就会wrong data type,所以需要对数值类型处理.
            case Cell.CELL_TYPE_NUMERIC:
                map.put("getDateCellValue", cell.getDateCellValue());
                map.put("getNumericCellValue", cell.getNumericCellValue());

                cell.setCellType(Cell.CELL_TYPE_STRING);
                map.put("numericCellValueToString", cell.getStringCellValue());
                //if (HSSFDateUtil.isCellDateFormatted(cell)){
                //}
                break;

            //---------------------------------------------------------------

            case Cell.CELL_TYPE_STRING:
                map.put("getStringCellValue", cell.getStringCellValue());
                map.put("getRichStringCellValue", cell.getRichStringCellValue().getString());
                break;

            default:
                break;
        }
        //---------------------------------------------------------------

        map.put("getColumnIndex", cell.getColumnIndex());
        map.put("getHyperlink", cell.getHyperlink());
        map.put("getRowIndex", cell.getRowIndex());
        return map;
    }

    //---------------------------------------------------------------

    /**
     * 获得公式单元格计算的结果值.
     *
     * @param cell
     *            the cell
     * @return the cell formula result value
     */
    private static String getCellFormulaResultValue(Cell cell){
        int cachedFormulaResultType = cell.getCachedFormulaResultType();
        String cellFormulaResultValue = "";

        //只会有四种类型
        switch (cachedFormulaResultType) {
            case Cell.CELL_TYPE_NUMERIC:// if it is mumeric type
                DataFormatter dataFormatter = new DataFormatter();
                Format format = dataFormatter.createFormat(cell);
                cellFormulaResultValue = format.format(cell.getNumericCellValue());
                break;

            case Cell.CELL_TYPE_STRING:// if it is string type
                RichTextString richTextString = cell.getRichStringCellValue();
                cellFormulaResultValue = richTextString.toString();
                break;

            case Cell.CELL_TYPE_BOOLEAN:// 
                //				 return new DataFormatter().formatCellValue(cell);
                //				break;
            case Cell.CELL_TYPE_ERROR:// 

                //				break;
            default:
                //TODO
                cellFormulaResultValue = "cachedFormulaResultType:" + cachedFormulaResultType + " will impl soon";
                break;
        }
        return cellFormulaResultValue;
    }

    /**
     * Gets the row map for LOGGER.
     * 
     * @param row
     *            the row
     * @return the row map for log
     */
    public static Map<String, Object> getRowMapForLog(Row row){
        Map<String, Object> map = newLinkedHashMap();

        map.put("getFirstCellNum", row.getFirstCellNum());
        map.put("getHeight", row.getHeight());
        map.put("getHeightInPoints", row.getHeightInPoints());
        map.put("getLastCellNum", row.getLastCellNum());
        map.put("getPhysicalNumberOfCells", row.getPhysicalNumberOfCells());
        map.put("getRowNum", row.getRowNum());
        //map.put("getRowStyle", row.getRowStyle());
        map.put("getZeroHeight", row.getZeroHeight());

        return map;
    }

    /**
     * Gets the sheet map for LOGGER.
     * 
     * @param sheet
     *            the sheet
     * @return the sheet map for log
     */
    public static Map<String, Object> getSheetMapForLog(Sheet sheet){
        int lastRowNum = sheet.getLastRowNum();
        int firstRowNum = sheet.getFirstRowNum();
        int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();

        Map<String, Object> map = newLinkedHashMap();

        map.put("getLastRowNum", lastRowNum);
        map.put("getFirstRowNum", firstRowNum);
        map.put("getPhysicalNumberOfRows", physicalNumberOfRows);
        map.put("getAutobreaks", sheet.getAutobreaks());
        map.put("getDefaultColumnWidth", sheet.getDefaultColumnWidth());
        map.put("getDefaultRowHeight", sheet.getDefaultRowHeight());
        map.put("getDefaultRowHeightInPoints", sheet.getDefaultRowHeightInPoints());
        map.put("getDisplayGuts", sheet.getDisplayGuts());
        map.put("getFitToPage", sheet.getFitToPage());
        map.put("getForceFormulaRecalculation", sheet.getForceFormulaRecalculation());
        map.put("getHorizontallyCenter", sheet.getHorizontallyCenter());
        map.put("getLeftCol", sheet.getLeftCol());
        map.put("getNumMergedRegions", sheet.getNumMergedRegions());
        map.put("getPaneInformation", sheet.getPaneInformation());
        map.put("getProtect", sheet.getProtect());
        map.put("getRowSumsBelow", sheet.getRowSumsBelow());
        map.put("getRowSumsRight", sheet.getRowSumsRight());
        map.put("getScenarioProtect", sheet.getScenarioProtect());
        map.put("getSheetName", sheet.getSheetName());
        map.put("getTopRow", sheet.getTopRow());
        map.put("getVerticallyCenter", sheet.getVerticallyCenter());
        return map;
    }

}
