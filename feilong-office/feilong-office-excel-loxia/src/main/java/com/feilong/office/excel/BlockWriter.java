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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelCell;
import com.feilong.office.excel.definition.ExcelCellConditionStyle;
import com.feilong.office.excel.utils.OgnlStack;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class BlockWriter{

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockWriter.class);

    /** Don't let anyone instantiate this class. */
    private BlockWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Write simple block.
     *
     * @param sheet
     *            the sheet
     * @param blockDefinition
     *            the block definition
     * @param stack
     *            the stack
     * @param styleMap
     *            the style map
     */
    static void writeSimpleBlock(Sheet sheet,ExcelBlock blockDefinition,OgnlStack stack,Map<String, CellStyle> styleMap){
        //block style
        if (styleMap.keySet().size() > 0){
            for (ExcelCellConditionStyle excelCellConditionStyle : blockDefinition.getStyles()){
                Object obj = stack.getValue(excelCellConditionStyle.getCondition());
                if (obj == null || !(obj instanceof Boolean)){
                    continue;
                }
                if (((Boolean) obj).booleanValue()){
                    BlockStyleSetter.set(
                                    sheet,
                                    excelCellConditionStyle.getStartRow(),
                                    excelCellConditionStyle.getEndRow(),
                                    excelCellConditionStyle.getStartCol(),
                                    excelCellConditionStyle.getEndCol(),
                                    excelCellConditionStyle.getCellIndex(),
                                    styleMap);
                }
            }
        }
        for (ExcelCell excelCell : blockDefinition.getCells()){
            String dataExpr = excelCell.getDataExpr();
            CellValueSetter.set(
                            sheet,
                            excelCell.getRow(),
                            excelCell.getCol(),
                            dataExpr == null ? excelCell.getDataName() : dataExpr,
                            stack);
            if (styleMap.keySet().size() > 0){
                for (ExcelCellConditionStyle excelCellConditionStyle : excelCell.getStyles()){
                    Object obj = stack.getValue(excelCellConditionStyle.getCondition());
                    if (obj == null || !(obj instanceof Boolean)){
                        continue;
                    }
                    if (((Boolean) obj).booleanValue()){
                        CellStyleSetter.set(
                                        sheet,
                                        excelCell.getRow(),
                                        excelCell.getCol(),
                                        styleMap.get(excelCellConditionStyle.getCellIndex()));
                    }
                }
            }
        }
    }

    /**
     * Write loop block.
     *
     * @param sheet
     *            the sheet
     * @param excelBlock
     *            the block definition
     * @param ognlStack
     *            the stack
     * @param mergedRegions
     *            the merged regions
     * @param styleMap
     *            the style map
     */
    static void writeLoopBlock(
                    Sheet sheet,
                    ExcelBlock excelBlock,
                    OgnlStack ognlStack,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap){

        boolean isHorizontal = excelBlock.getDirection().equalsIgnoreCase(ExcelBlock.LOOP_DIRECTION_HORIZONAL);
        if (isHorizontal){
            try{
                Object value = ognlStack.getValue(excelBlock.getDataName());
                if (value == null){
                    //if no data, just remove the dummy data
                    for (int i = excelBlock.getEndRow(); i >= excelBlock.getStartRow(); i--){
                        sheet.removeRow(sheet.getRow(i));
                    }
                }
                Collection<? extends Object> listValue;
                if (!(value instanceof Collection)){
                    if (value.getClass().isArray()){
                        listValue = Arrays.asList(value);
                    }else{
                        ArrayList<Object> list = new ArrayList<>();
                        list.add(value);
                        listValue = list;
                    }
                }else{
                    listValue = (Collection<? extends Object>) value;
                }

                //---------------------------------------------------------------

                int step = 1;
                Object preObj = null;
                for (Object obj : listValue){
                    ognlStack.push(obj);
                    ognlStack.addContext("preLine", preObj);
                    ognlStack.addContext("lineNum", step - 1);
                    //shiftrow and prepare write new row
                    int nextStartRow = excelBlock.getStartRow() + step * (excelBlock.getEndRow() - excelBlock.getStartRow() + 1);
                    if (nextStartRow <= sheet.getLastRowNum()){
                        sheet.shiftRows(
                                        nextStartRow,
                                        sheet.getLastRowNum(),
                                        excelBlock.getEndRow() - excelBlock.getStartRow() + 1,
                                        true,
                                        false);
                    }

                    RowWriter.write(
                                    sheet,
                                    excelBlock,
                                    ognlStack,
                                    step * (excelBlock.getEndRow() - excelBlock.getStartRow() + 1),
                                    mergedRegions,
                                    styleMap);
                    step++;
                    preObj = ognlStack.pop();
                }
                ognlStack.removeContext("preLine");
                ognlStack.removeContext("lineNum");

                //if no data, just remove the dummy data        
                //[2013-2-26]it seems that we also need to remove all merged regions for shift      
                for (int i = sheet.getNumMergedRegions() - 1; i >= 0; i--){
                    CellRangeAddress cellRangeAddress = sheet.getMergedRegion(i);
                    if (cellRangeAddress.getFirstRow() >= excelBlock.getStartRow()
                                    && cellRangeAddress.getFirstColumn() >= excelBlock.getStartCol()
                                    && cellRangeAddress.getLastRow() <= excelBlock.getEndRow()
                                    && cellRangeAddress.getLastColumn() <= excelBlock.getEndCol()){
                        sheet.removeMergedRegion(i);
                        LOGGER.debug(
                                        "Removed Merged Region:[{}-{}]",
                                        CellReferenceUtil.getCellIndex(cellRangeAddress.getFirstRow(), cellRangeAddress.getFirstColumn()),
                                        CellReferenceUtil.getCellIndex(cellRangeAddress.getLastRow(), cellRangeAddress.getLastColumn()));
                    }
                }
                //[2013-2-22]if with data, still need to remove dummy one, otherwise xlsx will have error if there are formulas in block.
                for (int i = excelBlock.getEndRow(); i >= excelBlock.getStartRow(); i--){
                    sheet.removeRow(sheet.getRow(i));
                }
                if (listValue.size() > 0){
                    sheet.shiftRows(
                                    excelBlock.getEndRow() + 1,
                                    sheet.getLastRowNum(),
                                    excelBlock.getStartRow() - excelBlock.getEndRow() - 1,
                                    true,
                                    false);
                }
            }catch (Exception e){
                LOGGER.error("", e);
            }
        }else{
            try{
                Object value = ognlStack.getValue(excelBlock.getDataName());
                if (value == null){
                    return;
                }

                //---------------------------------------------------------------
                Collection<? extends Object> listValue;
                if (!(value instanceof Collection)){
                    if (value.getClass().isArray()){
                        listValue = Arrays.asList(value);
                    }else{
                        ArrayList<Object> list = new ArrayList<>();
                        list.add(value);
                        listValue = list;
                    }
                }else{
                    listValue = (Collection<? extends Object>) value;
                }

                //---------------------------------------------------------------

                int step = 0;
                Object preObj = null;
                for (Object obj : listValue){
                    ognlStack.push(obj);
                    ognlStack.addContext("preLine", preObj);
                    ognlStack.addContext("lineNum", step - 1);

                    ColumnWriter.write(
                                    sheet,
                                    excelBlock,
                                    ognlStack,
                                    0,
                                    step * (excelBlock.getEndCol() - excelBlock.getStartCol() + 1),
                                    mergedRegions,
                                    styleMap);
                    step++;
                    preObj = ognlStack.pop();
                }
                ognlStack.removeContext("preLine");
                ognlStack.removeContext("lineNum");
            }catch (Exception e){
                LOGGER.error("", e);
            }
        }
    }

}
