package loxia.support.excel.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loxia.support.LoxiaSupportConstants;
import loxia.support.LoxiaSupportSettings;
import loxia.support.excel.ExcelUtil;
import loxia.support.excel.ExcelWriter;
import loxia.support.excel.WriteStatus;
import loxia.support.excel.definition.ExcelBlock;
import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.definition.ExcelCellConditionStyle;
import loxia.support.excel.definition.ExcelManipulatorDefinition;
import loxia.support.excel.definition.ExcelSheet;
import loxia.utils.OgnlStack;

public class DefaultExcelWriter implements ExcelWriter,Serializable{

    /**
     * 
     */
    private static final long          serialVersionUID = -1482207293275885449L;

    static final Logger                logger           = LoggerFactory.getLogger(DefaultExcelWriter.class);

    private ExcelManipulatorDefinition definition;

    private byte[]                     bufferedTemplate;

    @Override
    public WriteStatus write(OutputStream os,Map<String, Object> beans){
        WriteStatus writeStatus = new DefaultWriteStatus();
        writeStatus.setStatus(WriteStatus.STATUS_SUCCESS);

        if (bufferedTemplate == null){
            writeStatus.setStatus(WriteStatus.STATUS_READ_TEMPLATE_FILE_ERROR);
            return writeStatus;
        }

        Workbook wb = createWookBook(new ByteArrayInputStream(bufferedTemplate), writeStatus);

        if (wb != null){
            writeNative(wb, os, beans, writeStatus);
        }
        return writeStatus;

    }

    @Override
    public WriteStatus write(InputStream is,OutputStream os,Map<String, Object> beans){
        WriteStatus writeStatus = new DefaultWriteStatus();
        writeStatus.setStatus(WriteStatus.STATUS_SUCCESS);

        Workbook wb = createWookBook(is, writeStatus);

        if (wb != null){
            writeNative(wb, os, beans, writeStatus);
        }
        return writeStatus;
    }

    private Workbook createWookBook(InputStream is,WriteStatus writeStatus){
        Workbook wb = null;
        try{
            wb = WorkbookFactory.create(is);
        }catch (IOException e){
            writeStatus.setStatus(WriteStatus.STATUS_READ_TEMPLATE_FILE_ERROR);
        }catch (InvalidFormatException e){
            writeStatus.setStatus(WriteStatus.STATUS_READ_TEMPLATE_FILE_ERROR);
        }
        return wb;
    }

    private void writeNative(Workbook wb,OutputStream os,Map<String, Object> beans,WriteStatus writeStatus){
        if (definition.getExcelSheets().size() == 0 || wb.getNumberOfSheets() < definition.getExcelSheets().size()){
            writeStatus.setStatus(WriteStatus.STATUS_SETTING_ERROR);
            writeStatus.setMessage("No sheet definition found or Sheet Number in definition is more than number in template file.");
        }else{
            Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
            if (definition.getStyleSheetPosition() != null){
                if (definition.getStyleSheetPosition().intValue() < definition.getExcelSheets().size()){
                    writeStatus.setStatus(WriteStatus.STATUS_SETTING_ERROR);
                    writeStatus.setMessage("Style Sheet can not be one Template Sheet.");
                    return;
                }
                for (int i = 0; i < definition.getExcelSheets().size(); i++){
                    initConditionalStyle(wb.getSheetAt(definition.getStyleSheetPosition()), definition.getExcelSheets().get(i), styleMap);
                }
                wb.removeSheetAt(definition.getStyleSheetPosition());
                logger.debug("{} styles found", styleMap.keySet().size());
            }
            for (int i = 0; i < definition.getExcelSheets().size(); i++){
                writeSheet(wb.getSheetAt(i), definition.getExcelSheets().get(i), new OgnlStack(beans), styleMap, writeStatus);
            }
            reCalculateWorkbook(wb);
            wb.setActiveSheet(0);
            try{
                wb.write(os);
            }catch (IOException e){
                writeStatus.setStatus(WriteStatus.STATUS_WRITE_FILE_ERROR);
            }
        }
    }

    @Override
    public WriteStatus write(String template,OutputStream os,Map<String, Object> beans){
        return write(Thread.currentThread().getContextClassLoader().getResourceAsStream(template), os, beans);
    }

    @Override
    public WriteStatus writePerSheet(OutputStream os,List<Map<String, Object>> beansList){
        WriteStatus writeStatus = new DefaultWriteStatus();
        writeStatus.setStatus(WriteStatus.STATUS_SUCCESS);

        if (bufferedTemplate == null){
            writeStatus.setStatus(WriteStatus.STATUS_READ_TEMPLATE_FILE_ERROR);
            return writeStatus;
        }

        Workbook wb = createWookBook(new ByteArrayInputStream(bufferedTemplate), writeStatus);

        if (wb != null){
            writePerSheetNative(wb, os, beansList, writeStatus);
        }
        return writeStatus;
    }

    @Override
    public WriteStatus writePerSheet(InputStream is,OutputStream os,List<Map<String, Object>> beansList){
        WriteStatus writeStatus = new DefaultWriteStatus();
        writeStatus.setStatus(WriteStatus.STATUS_SUCCESS);

        Workbook wb = createWookBook(is, writeStatus);

        if (wb != null){
            writePerSheetNative(wb, os, beansList, writeStatus);
        }
        return writeStatus;
    }

    private void writePerSheetNative(Workbook wb,OutputStream os,List<Map<String, Object>> beansList,WriteStatus writeStatus){
        if (definition.getExcelSheets().size() == 0 || wb.getNumberOfSheets() < 1){
            writeStatus.setStatus(WriteStatus.STATUS_SETTING_ERROR);
            writeStatus.setMessage("No sheet definition found or template file contains no sheet.");
        }else{
            Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>();
            if (definition.getStyleSheetPosition() != null){
                if (definition.getStyleSheetPosition().intValue() < definition.getExcelSheets().size()){
                    writeStatus.setStatus(WriteStatus.STATUS_SETTING_ERROR);
                    writeStatus.setMessage("Style Sheet can not be one Template Sheet.");
                    return;
                }
                initConditionalStyle(wb.getSheetAt(definition.getStyleSheetPosition()), definition.getExcelSheets().get(0), styleMap);
                wb.removeSheetAt(definition.getStyleSheetPosition());
            }
            //remove sheets except the first one
            for (int i = wb.getNumberOfSheets() - 1; i > 0; i--){
                wb.removeSheetAt(i);
            }
            for (int i = 0; i < beansList.size(); i++){
                Sheet newSheet = wb.createSheet("Auto Generated Sheet " + i);
                ExcelUtil.copySheet(wb.getSheetAt(0), newSheet);
                writeSheet(newSheet, definition.getExcelSheets().iterator().next(), new OgnlStack(beansList.get(i)), styleMap, writeStatus);
            }
            //remove template sheet
            wb.removeSheetAt(0);
            reCalculateWorkbook(wb);
            wb.setActiveSheet(0);
            try{
                wb.write(os);
            }catch (IOException e){
                writeStatus.setStatus(WriteStatus.STATUS_WRITE_FILE_ERROR);
            }
        }
    }

    @Override
    public WriteStatus writePerSheet(String template,OutputStream os,List<Map<String, Object>> beansList){
        return writePerSheet(Thread.currentThread().getContextClassLoader().getResourceAsStream(template), os, beansList);
    }

    private void initConditionalStyle(Sheet styleSheet,ExcelSheet sheetDefinition,Map<String, CellStyle> styleMap){
        for (ExcelBlock blockDefinition : sheetDefinition.getExcelBlocks()){
            initConditionalStyle(styleSheet, blockDefinition, styleMap);
            if (blockDefinition.getChildBlock() != null)
                initConditionalStyle(styleSheet, blockDefinition.getChildBlock(), styleMap);
        }
    }

    private void initConditionalStyle(Sheet styleSheet,ExcelBlock blockDefinition,Map<String, CellStyle> styleMap){
        for (ExcelCellConditionStyle style : blockDefinition.getStyles())
            initConditionalStyle(styleSheet, style, styleMap);
        for (ExcelCell cellDefinition : blockDefinition.getCells()){
            for (ExcelCellConditionStyle style : cellDefinition.getStyles()){
                initConditionalStyle(styleSheet, style, styleMap);
            }
        }
    }

    private void initConditionalStyle(Sheet styleSheet,ExcelCellConditionStyle style,Map<String, CellStyle> styleMap){
        //ignore existed style
        if (style.getStartRow() == 0 && style.getEndRow() == 0 && style.getStartCol() == 0 && style.getEndCol() == 0
                        && styleMap.containsKey(style.getCellIndex()))
            return;
        int[] position = ExcelUtil.getCellPosition(style.getCellIndex());
        int rows = style.getEndRow() - style.getStartRow();
        int cols = style.getEndCol() - style.getStartCol();
        for (int i = position[0]; i <= position[0] + rows; i++){
            Row row = styleSheet.getRow(i);
            if (row == null)
                return;
            for (int j = position[1]; j <= position[1] + cols; j++){
                Cell cell = row.getCell(j);
                if (cell == null)
                    return;
                styleMap.put(ExcelUtil.getCellIndex(i, j), cell.getCellStyle());
                if (logger.isDebugEnabled()){
                    logger.debug("Condition Style [{}]", ExcelUtil.getCellIndex(i, j));
                }
            }
        }
    }

    private void reCalculateWorkbook(Workbook wb){
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        for (int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum++){
            Sheet sheet = wb.getSheetAt(sheetNum);
            for (Row r : sheet){
                for (Cell c : r){
                    if (c.getCellType() == Cell.CELL_TYPE_FORMULA){
                        evaluator.evaluateFormulaCell(c);
                    }
                }
            }
        }
    }

    private void writeSheet(Sheet sheet,ExcelSheet sheetDefinition,OgnlStack stack,Map<String, CellStyle> styleMap,WriteStatus writeStatus){
        Workbook wb = sheet.getWorkbook();
        String sheetName = sheetDefinition.getDisplayName();
        if (stack.getValue("sheetName") != null)
            sheetName = (String) stack.getValue("sheetName");
        if (sheetName != null)
            wb.setSheetName(wb.getSheetIndex(sheet), sheetName);

        Map<ExcelBlock, List<CellRangeAddress>> mergedRegions = new HashMap<ExcelBlock, List<CellRangeAddress>>();
        for (int i = 0; i < sheet.getNumMergedRegions(); i++){
            CellRangeAddress cra = sheet.getMergedRegion(i);
            logger.debug(
                            "Merged Region:[{}-{}]",
                            ExcelUtil.getCellIndex(cra.getFirstRow(), cra.getFirstColumn()),
                            ExcelUtil.getCellIndex(cra.getLastRow(), cra.getLastColumn()));
            for (ExcelBlock blockDefinition : sheetDefinition.getSortedExcelBlocks()){
                if (cra.getFirstRow() >= blockDefinition.getStartRow() && cra.getFirstColumn() >= blockDefinition.getStartCol()
                                && cra.getLastRow() <= blockDefinition.getEndRow() && cra.getLastColumn() <= blockDefinition.getEndCol()){
                    List<CellRangeAddress> cras = mergedRegions.get(blockDefinition);
                    if (cras == null){
                        cras = new ArrayList<CellRangeAddress>();
                        mergedRegions.put(blockDefinition, cras);
                    }
                    cras.add(cra);
                }
            }
        }

        for (ExcelBlock blockDefinition : sheetDefinition.getSortedExcelBlocks()){
            if (blockDefinition.isLoop()){
                writeLoopBlock(sheet, blockDefinition, stack, mergedRegions.get(blockDefinition), styleMap, writeStatus);
            }else{
                writeSimpleBlock(sheet, blockDefinition, stack, styleMap, writeStatus);
            }
        }
    }

    private void writeSimpleBlock(
                    Sheet sheet,
                    ExcelBlock blockDefinition,
                    OgnlStack stack,
                    Map<String, CellStyle> styleMap,
                    WriteStatus writeStatus){
        //block style
        if (styleMap.keySet().size() > 0){
            for (ExcelCellConditionStyle style : blockDefinition.getStyles()){
                Object obj = stack.getValue(style.getCondition());
                if (obj == null || !(obj instanceof Boolean))
                    continue;
                if (((Boolean) obj).booleanValue()){
                    setBlockStyle(
                                    sheet,
                                    style.getStartRow(),
                                    style.getEndRow(),
                                    style.getStartCol(),
                                    style.getEndCol(),
                                    style.getCellIndex(),
                                    styleMap);
                }
            }
        }
        for (ExcelCell cellDefinition : blockDefinition.getCells()){
            setCellValue(
                            sheet,
                            cellDefinition.getRow(),
                            cellDefinition.getCol(),
                            cellDefinition.getDataExpr() == null ? cellDefinition.getDataName() : cellDefinition.getDataExpr(),
                            stack);
            if (styleMap.keySet().size() > 0){
                for (ExcelCellConditionStyle style : cellDefinition.getStyles()){
                    Object obj = stack.getValue(style.getCondition());
                    if (obj == null || !(obj instanceof Boolean))
                        continue;
                    if (((Boolean) obj).booleanValue()){
                        setCellStyle(sheet, cellDefinition.getRow(), cellDefinition.getCol(), styleMap.get(style.getCellIndex()));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void writeLoopBlock(
                    Sheet sheet,
                    ExcelBlock blockDefinition,
                    OgnlStack stack,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap,
                    WriteStatus writeStatus){
        if (blockDefinition.getDirection().equalsIgnoreCase(blockDefinition.LOOP_DIRECTION_HORIZONAL)){
            try{
                Object value = stack.getValue(blockDefinition.getDataName());
                if (value == null){
                    //if no data, just remove the dummy data
                    for (int i = blockDefinition.getEndRow(); i >= blockDefinition.getStartRow(); i--)
                        sheet.removeRow(sheet.getRow(i));
                }
                Collection<? extends Object> listValue;
                if (!(value instanceof Collection)){
                    if (value.getClass().isArray())
                        listValue = Arrays.asList(value);
                    else{
                        ArrayList<Object> list = new ArrayList<Object>();
                        list.add(value);
                        listValue = list;
                    }
                }else
                    listValue = (Collection<? extends Object>) value;

                int step = 1;
                Object preObj = null;
                for (Object obj : listValue){
                    stack.push(obj);
                    stack.addContext("preLine", preObj);
                    stack.addContext("lineNum", step - 1);
                    //shiftrow and prepare write new row
                    int nextStartRow = blockDefinition.getStartRow()
                                    + step * (blockDefinition.getEndRow() - blockDefinition.getStartRow() + 1);
                    if (nextStartRow <= sheet.getLastRowNum())
                        sheet.shiftRows(
                                        nextStartRow,
                                        sheet.getLastRowNum(),
                                        blockDefinition.getEndRow() - blockDefinition.getStartRow() + 1,
                                        true,
                                        false);

                    writeRow(
                                    sheet,
                                    blockDefinition,
                                    stack,
                                    step * (blockDefinition.getEndRow() - blockDefinition.getStartRow() + 1),
                                    mergedRegions,
                                    styleMap,
                                    writeStatus);
                    step++;
                    preObj = stack.pop();
                }
                stack.removeContext("preLine");
                stack.removeContext("lineNum");

                //if no data, just remove the dummy data		
                //[2013-2-26]it seems that we also need to remove all merged regions for shift		
                for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--){
                    CellRangeAddress cra = sheet.getMergedRegion(i);
                    if (cra.getFirstRow() >= blockDefinition.getStartRow() && cra.getFirstColumn() >= blockDefinition.getStartCol()
                                    && cra.getLastRow() <= blockDefinition.getEndRow()
                                    && cra.getLastColumn() <= blockDefinition.getEndCol()){
                        sheet.removeMergedRegion(i);
                        logger.debug(
                                        "Removed Merged Region:[{}-{}]",
                                        ExcelUtil.getCellIndex(cra.getFirstRow(), cra.getFirstColumn()),
                                        ExcelUtil.getCellIndex(cra.getLastRow(), cra.getLastColumn()));
                    }
                }
                //[2013-2-22]if with data, still need to remove dummy one, otherwise xlsx will have error if there are formulas in block.
                for (int i = blockDefinition.getEndRow(); i >= blockDefinition.getStartRow(); i--)
                    sheet.removeRow(sheet.getRow(i));
                if (listValue.size() > 0){
                    sheet.shiftRows(
                                    blockDefinition.getEndRow() + 1,
                                    sheet.getLastRowNum(),
                                    blockDefinition.getStartRow() - blockDefinition.getEndRow() - 1,
                                    true,
                                    false);
                }
            }catch (Exception e){
                e.printStackTrace();
                //do nothing
            }
        }else{
            try{
                Object value = stack.getValue(blockDefinition.getDataName());
                if (value == null){
                    return;
                }
                Collection<? extends Object> listValue;
                if (!(value instanceof Collection)){
                    if (value.getClass().isArray())
                        listValue = Arrays.asList(value);
                    else{
                        ArrayList<Object> list = new ArrayList<Object>();
                        list.add(value);
                        listValue = list;
                    }
                }else
                    listValue = (Collection<? extends Object>) value;

                int step = 0;
                Object preObj = null;
                for (Object obj : listValue){
                    stack.push(obj);
                    stack.addContext("preLine", preObj);
                    stack.addContext("lineNum", step - 1);

                    writeCol(
                                    sheet,
                                    blockDefinition,
                                    stack,
                                    0,
                                    step * (blockDefinition.getEndCol() - blockDefinition.getStartCol() + 1),
                                    mergedRegions,
                                    styleMap,
                                    writeStatus);
                    step++;
                    preObj = stack.pop();
                }
                stack.removeContext("preLine");
                stack.removeContext("lineNum");
            }catch (Exception e){
                e.printStackTrace();
                //do nothing
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void writeRow(
                    Sheet sheet,
                    ExcelBlock blockDefinition,
                    OgnlStack stack,
                    int rowOffset,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap,
                    WriteStatus writeStatus) throws Exception{
        if (rowOffset > 0)
            ExcelUtil.copyBlock(
                            sheet,
                            blockDefinition.getStartRow(),
                            blockDefinition.getStartCol(),
                            blockDefinition.getEndRow(),
                            blockDefinition.getEndCol(),
                            true,
                            rowOffset,
                            0,
                            mergedRegions);

        if (styleMap.keySet().size() > 0){
            for (ExcelCellConditionStyle style : blockDefinition.getStyles()){
                Object obj = stack.getValue(style.getCondition());
                if (obj == null || !(obj instanceof Boolean))
                    continue;
                if (((Boolean) obj).booleanValue()){
                    setBlockStyle(
                                    sheet,
                                    style.getStartRow() + rowOffset,
                                    style.getEndRow() + rowOffset,
                                    style.getStartCol(),
                                    style.getEndCol(),
                                    style.getCellIndex(),
                                    styleMap);
                }
            }
        }
        for (ExcelCell cellDefinition : blockDefinition.getCells()){
            String dataName = cellDefinition.getDataExpr() == null ? cellDefinition.getDataName() : cellDefinition.getDataExpr();
            if (dataName.startsWith("="))
                dataName = ExcelUtil.offsetFormula(dataName, rowOffset, 0);
            setCellValue(sheet, cellDefinition.getRow() + rowOffset, cellDefinition.getCol(), dataName, stack);
            if (styleMap.keySet().size() > 0){
                for (ExcelCellConditionStyle style : cellDefinition.getStyles()){
                    Object obj = stack.getValue(style.getCondition());
                    if (obj == null || !(obj instanceof Boolean))
                        continue;
                    if (((Boolean) obj).booleanValue()){
                        setCellStyle(
                                        sheet,
                                        cellDefinition.getRow() + rowOffset,
                                        cellDefinition.getCol(),
                                        styleMap.get(style.getCellIndex()));
                    }
                }
            }
        }
        if (blockDefinition.getChildBlock() != null){
            Object colValue = stack.getValue(blockDefinition.getChildBlock().getDataName());
            if (colValue == null)
                return;
            Collection<? extends Object> listValue;
            if (!(colValue instanceof Collection)){
                if (colValue.getClass().isArray())
                    listValue = Arrays.asList(colValue);
                else{
                    ArrayList<Object> list = new ArrayList<Object>();
                    list.add(colValue);
                    listValue = list;
                }
            }else
                listValue = (Collection<? extends Object>) colValue;
            List<CellRangeAddress> childMergedRegions = null;
            if (mergedRegions != null){
                childMergedRegions = new ArrayList<CellRangeAddress>();
                for (CellRangeAddress cra : mergedRegions){
                    if (cra.getFirstRow() >= blockDefinition.getChildBlock().getStartRow()
                                    && cra.getFirstColumn() >= blockDefinition.getChildBlock().getStartCol()
                                    && cra.getLastRow() <= blockDefinition.getChildBlock().getEndRow()
                                    && cra.getLastColumn() <= blockDefinition.getChildBlock().getEndCol()){
                        childMergedRegions.add(cra);
                    }
                }
            }
            int colStep = 0;
            Object preObj = null;
            for (Object obj : listValue){
                stack.push(obj);
                stack.addContext("preColumn", preObj);
                stack.addContext("columnNum", colStep);
                writeCol(
                                sheet,
                                blockDefinition.getChildBlock(),
                                stack,
                                rowOffset,
                                colStep * (blockDefinition.getChildBlock().getEndCol() - blockDefinition.getChildBlock().getStartCol() + 1),
                                childMergedRegions,
                                styleMap,
                                writeStatus);
                colStep++;
                preObj = stack.pop();
            }
            stack.removeContext("preColumn");
            stack.removeContext("columnNum");
        }
    }

    private void writeCol(
                    Sheet sheet,
                    ExcelBlock blockDefinition,
                    OgnlStack stack,
                    int rowOffset,
                    int colOffset,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap,
                    WriteStatus writeStatus) throws Exception{
        if (rowOffset > 0 || colOffset > 0){
            ExcelUtil.copyBlock(
                            sheet,
                            blockDefinition.getStartRow(),
                            blockDefinition.getStartCol(),
                            blockDefinition.getEndRow(),
                            blockDefinition.getEndCol(),
                            true,
                            rowOffset,
                            colOffset,
                            mergedRegions);
        }
        if (styleMap.keySet().size() > 0){
            for (ExcelCellConditionStyle style : blockDefinition.getStyles()){
                Object obj = stack.getValue(style.getCondition());
                if (obj == null || !(obj instanceof Boolean))
                    continue;
                if (((Boolean) obj).booleanValue()){
                    setBlockStyle(
                                    sheet,
                                    style.getStartRow() + rowOffset,
                                    style.getEndRow() + rowOffset,
                                    style.getStartCol() + colOffset,
                                    style.getEndCol() + colOffset,
                                    style.getCellIndex(),
                                    styleMap);
                }
            }
        }
        for (ExcelCell cellDefinition : blockDefinition.getCells()){
            String dataName = cellDefinition.getDataExpr() == null ? cellDefinition.getDataName() : cellDefinition.getDataExpr();
            if (dataName.startsWith("="))
                dataName = ExcelUtil.offsetFormula(dataName, rowOffset, colOffset);
            setCellValue(sheet, cellDefinition.getRow() + rowOffset, cellDefinition.getCol() + colOffset, dataName, stack);
            if (styleMap.keySet().size() > 0){
                for (ExcelCellConditionStyle style : cellDefinition.getStyles()){
                    Object obj = stack.getValue(style.getCondition());
                    if (obj == null || !(obj instanceof Boolean))
                        continue;
                    if (((Boolean) obj).booleanValue()){
                        setCellStyle(
                                        sheet,
                                        cellDefinition.getRow() + rowOffset,
                                        cellDefinition.getCol() + colOffset,
                                        styleMap.get(style.getCellIndex()));
                    }
                }
            }
        }
    }

    private void setBlockStyle(
                    Sheet sheet,
                    int startRowIndex,
                    int endRowIndex,
                    int startColIndex,
                    int endColIndex,
                    String cellIndex,
                    Map<String, CellStyle> styleMap){
        for (int rowIndex = startRowIndex; rowIndex <= endRowIndex; rowIndex++){
            Row row = sheet.getRow(rowIndex);
            if (row == null)
                row = sheet.createRow(rowIndex);
            for (int c = startColIndex; c <= endColIndex; c++){
                String cIdx = ExcelUtil.offsetCellIndex(cellIndex, rowIndex - startRowIndex, c - startColIndex);
                CellStyle style = styleMap.get(cIdx);
                if (style == null)
                    continue;
                Cell cell = row.getCell(c);
                if (cell == null)
                    cell = row.createCell(c);
                cell.setCellStyle(style);
            }
        }
    }

    private void setCellStyle(Sheet sheet,int rowIndex,int cellIndex,CellStyle style){
        if (style == null)
            return;
        Row row = sheet.getRow(rowIndex);
        if (row == null)
            row = sheet.createRow(rowIndex);
        Cell cell = row.getCell(cellIndex);
        if (cell == null)
            cell = row.createCell(cellIndex);
        if (cell.getCellStyle() == null || (cell.getCellType() != Cell.CELL_TYPE_NUMERIC) || (!DateUtil.isCellDateFormatted(cell))
                        || DateUtil.isADateFormat(style.getDataFormat(), style.getDataFormatString()))
            cell.setCellStyle(style);
        else{
            CellStyle cstyle = sheet.getWorkbook().createCellStyle();
            cstyle.cloneStyleFrom(style);
            cstyle.setDataFormat(cell.getCellStyle().getDataFormat());
            cell.setCellStyle(cstyle);
        }
    }

    private void setCellValue(Sheet sheet,int rowIndex,int cellIndex,String dataName,OgnlStack stack){
        if (dataName.equals("#"))
            return;
        Row row = sheet.getRow(rowIndex);
        if (row == null)
            row = sheet.createRow(rowIndex);
        Cell cell = row.getCell(cellIndex);
        if (cell == null)
            cell = row.createCell(cellIndex);
        if (dataName.startsWith("=")){
            //formula
            cell.setCellFormula(dataName.substring(1));
        }else{
            //data
            try{
                Object value = stack.getValue(dataName);
                setCellValue(cell, value);
            }catch (Exception e){
                //do nothing now
            }
        }
    }

    private void setCellValue(Cell cell,Object value){
        if (value == null){
            cell.setCellValue((String) null);
            return;
        }
        if (value instanceof Date){
            //if current cell do not formatted as date, set is as one date			
            if (cell.getCellType() != Cell.CELL_TYPE_NUMERIC)
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
            if (!DateUtil.isCellDateFormatted(cell)){
                CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
                if (cell.getCellStyle() == null){
                    style.cloneStyleFrom(cell.getCellStyle());
                }
                style.setDataFormat(
                                cell.getSheet().getWorkbook().getCreationHelper().createDataFormat()
                                                .getFormat(LoxiaSupportSettings.getInstance().get(LoxiaSupportConstants.DATE_PATTERN)));
                cell.setCellStyle(style);
            }
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BLANK || cell.getCellType() == Cell.CELL_TYPE_ERROR
                        || cell.getCellType() == Cell.CELL_TYPE_FORMULA){
            //set cell value without data type transform			
            if (value instanceof Integer)
                cell.setCellValue(new BigDecimal((Integer) value).doubleValue());
            else if (value instanceof Long)
                cell.setCellValue(new BigDecimal((Long) value).doubleValue());
            else if (value instanceof Double)
                cell.setCellValue((Double) value);
            else if (value instanceof BigDecimal)
                cell.setCellValue(((BigDecimal) value).doubleValue());
            else if (value instanceof Date)
                cell.setCellValue((Date) value);
            else
                cell.setCellValue(value.toString());
        }else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
            if (value instanceof Boolean){
                cell.setCellValue((Boolean) value);
            }
        }else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
            if (value instanceof Date){
                cell.setCellValue((Date) value);
            }else{
                if (value instanceof Integer)
                    cell.setCellValue(new BigDecimal((Integer) value).doubleValue());
                else if (value instanceof Long)
                    cell.setCellValue(new BigDecimal((Long) value).doubleValue());
                else if (value instanceof Double)
                    cell.setCellValue((Double) value);
                else if (value instanceof BigDecimal)
                    cell.setCellValue(((BigDecimal) value).doubleValue());
                else{
                    try{
                        cell.setCellValue(Double.parseDouble(value.toString()));
                    }catch (NumberFormatException e){
                        //do nothing
                    }
                }
            }
        }else{
            cell.setCellValue(value.toString());
        }
    }

    public void initBufferedTemplate(InputStream is){
        try{
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int b = is.read(buf);
            while (b != -1){
                bos.write(buf, 0, b);
                b = is.read(buf);
            }
            bufferedTemplate = bos.toByteArray();
        }catch (IOException e){
            logger.error("Init Write Template Error", e);
        }
    }

    @Override
    public ExcelManipulatorDefinition getDefinition(){
        return definition;
    }

    @Override
    public void setDefinition(ExcelManipulatorDefinition definition){
        this.definition = definition;
    }
}
