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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class ExcelUtil.
 */
public class ExcelUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    /** Don't let anyone instantiate this class. */
    private ExcelUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /** The Constant DYNAMIC_CELL_PATTREN. */
    private static final Pattern DYNAMIC_CELL_PATTREN = Pattern.compile("[A-Z][A-Z]?\\d+");

    /**
     * Gets the cell index.
     *
     * @param row
     *            the row
     * @param col
     *            the col
     * @return the cell index
     */
    public static String getCellIndex(int row,int col){
        CellReference cell = new CellReference(row, col);
        return cell.formatAsString().replaceAll("\\$", "");
    }

    //---------------------------------------------------------------

    /**
     * Gets the cell position.
     *
     * @param cellIndex
     *            the cell index
     * @return the cell position
     */
    public static int[] getCellPosition(String cellIndex){
        CellReference cell = new CellReference(cellIndex);
        return new int[] { cell.getRow(), cell.getCol() };
    }

    /**
     * Offset cell index.
     *
     * @param cellIndex
     *            the cell index
     * @param rowOffset
     *            the row offset
     * @param colOffset
     *            the col offset
     * @return the string
     */
    static String offsetCellIndex(String cellIndex,int rowOffset,int colOffset){
        CellReference cell = new CellReference(cellIndex);
        CellReference newCell = new CellReference(cell.getRow() + rowOffset, cell.getCol() + colOffset);
        return newCell.formatAsString().replaceAll("\\$", "");
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
        StringBuffer sb = new StringBuffer();
        Matcher matcher = DYNAMIC_CELL_PATTREN.matcher(formula);
        int head = 0, start = 0, end = -1;
        while (matcher.find()){
            start = matcher.start();
            end = matcher.end();
            sb.append(formula.substring(head, start));
            sb.append(offsetCellIndex(formula.substring(start, end), rowOffset, colOffset));
            head = end;
        }
        sb.append(formula.substring(head));
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * Copy sheet.
     *
     * @param sheet
     *            the sheet
     * @param newSheet
     *            the new sheet
     */
    static void copySheet(Sheet sheet,Sheet newSheet){
        int maxCol = 0;
        for (int row = 0; row <= sheet.getLastRowNum(); row++){
            Row oldRow = sheet.getRow(row);
            if (oldRow == null){
                continue;
            }
            Row newRow = newSheet.getRow(row);
            if (newRow == null){
                newRow = newSheet.createRow(row);
            }
            if (oldRow.getHeight() >= 0){
                newRow.setHeight(oldRow.getHeight());
            }
            maxCol = (maxCol >= oldRow.getLastCellNum() - 1 ? maxCol : oldRow.getLastCellNum() - 1);
            for (int col = 0; col < oldRow.getLastCellNum(); col++){
                Cell oldCell = oldRow.getCell(col);
                if (oldCell == null){
                    continue;
                }
                Cell newCell = newRow.getCell(col);
                if (newCell == null){
                    newCell = newRow.createCell(col);
                }
                copyCell(oldCell, newCell, true);
            }
        }
        for (int col = 0; col <= maxCol; col++){
            if (sheet.getColumnWidth(col) >= 0){
                newSheet.setColumnWidth(col, sheet.getColumnWidth(col));
            }
        }
        for (int i = 0; i < sheet.getNumMergedRegions(); i++){
            CellRangeAddress cra = sheet.getMergedRegion(i);
            newSheet.addMergedRegion(cra);
        }
    }

    //---------------------------------------------------------------

    /**
     * Copy block.
     *
     * @param sheet
     *            the sheet
     * @param startRow
     *            the start row
     * @param startCol
     *            the start col
     * @param endRow
     *            the end row
     * @param endCol
     *            the end col
     * @param copyStyle
     *            the copy style
     * @param rowOffset
     *            the row offset
     * @param colOffset
     *            the col offset
     * @param mergedRegions
     *            the merged regions
     */
    static void copyBlock(
                    Sheet sheet,
                    int startRow,
                    int startCol,
                    int endRow,
                    int endCol,
                    boolean copyStyle,
                    int rowOffset,
                    int colOffset,
                    List<CellRangeAddress> mergedRegions){
        for (int row = startRow; row <= endRow; row++){
            Row oldRow = sheet.getRow(row);
            if (oldRow == null){
                continue;
            }
            Row newRow = sheet.getRow(row + rowOffset);
            if (newRow == null){
                newRow = sheet.createRow(row + rowOffset);
            }
            if (oldRow.getHeight() >= 0){
                newRow.setHeight(oldRow.getHeight());
            }
            if (LOGGER.isTraceEnabled()){
                LOGGER.trace("copy row [{}] to [{}],Set row height :{}", row, row + rowOffset, newRow.getHeightInPoints());
            }
            for (int col = startCol; col <= endCol; col++){
                Cell oldCell = oldRow.getCell(col);
                if (oldCell == null){
                    continue;
                }
                Cell newCell = newRow.getCell(col + colOffset);
                if (newCell == null){
                    newCell = newRow.createCell(col + colOffset);
                }
                copyCell(oldCell, newCell, copyStyle, rowOffset, colOffset);
            }
        }
        for (int col = startCol; col <= endCol; col++){
            if (sheet.getColumnWidth(col) >= 0){
                sheet.setColumnWidth(col + colOffset, sheet.getColumnWidth(col));
            }
        }
        if (mergedRegions != null){
            for (CellRangeAddress cra : mergedRegions){
                CellRangeAddress craNew = new CellRangeAddress(
                                cra.getFirstRow() + rowOffset,
                                cra.getLastRow() + rowOffset,
                                cra.getFirstColumn() + colOffset,
                                cra.getLastColumn() + colOffset);
                sheet.addMergedRegion(craNew);
            }
        }
    }

    /**
     * Copy cell.
     *
     * @param oldCell
     *            the old cell
     * @param newCell
     *            the new cell
     * @param copyStyle
     *            the copy style
     * @param rowOffset
     *            the row offset
     * @param colOffset
     *            the col offset
     */
    static void copyCell(Cell oldCell,Cell newCell,boolean copyStyle,int rowOffset,int colOffset){
        if (copyStyle){
            newCell.setCellStyle(oldCell.getCellStyle());
        }
        switch (oldCell.getCellType()) {
            case STRING:
                newCell.setCellValue(oldCell.getRichStringCellValue());
                break;
            case NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case BLANK:
                newCell.setCellType(CellType.BLANK);
                break;
            case BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(offsetFormula(oldCell.getCellFormula(), rowOffset, colOffset));
                break;
            default:
                break;
        }
    }

    //---------------------------------------------------------------

    /**
     * Copy cell.
     *
     * @param oldCell
     *            the old cell
     * @param newCell
     *            the new cell
     * @param copyStyle
     *            the copy style
     */
    public static void copyCell(Cell oldCell,Cell newCell,boolean copyStyle){
        if (copyStyle){
            newCell.setCellStyle(oldCell.getCellStyle());
        }
        switch (oldCell.getCellType()) {
            case STRING:
                newCell.setCellValue(oldCell.getRichStringCellValue());
                break;
            case NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case BLANK:
                newCell.setCellType(CellType.BLANK);
                break;
            case BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            default:
                break;
        }
    }
}
