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

import com.feilong.office.excel.definition.ExcelBlock;
import com.feilong.office.excel.definition.ExcelCell;
import com.feilong.office.excel.definition.ExcelCellConditionStyle;
import com.feilong.office.excel.utils.OgnlStack;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
class RowWriter{

    /** Don't let anyone instantiate this class. */
    private RowWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------
    /**
     * Write row.
     *
     * @param sheet
     *            the sheet
     * @param excelBlock
     *            the block definition
     * @param ognlStack
     *            the stack
     * @param rowOffset
     *            the row offset
     * @param mergedRegions
     *            the merged regions
     * @param styleMap
     *            the style map
     * @throws Exception
     *             the exception
     */
    static void write(
                    Sheet sheet,
                    ExcelBlock excelBlock,
                    OgnlStack ognlStack,
                    int rowOffset,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap) throws Exception{
        if (rowOffset > 0){
            BlockCopyer.copy(
                            sheet,
                            excelBlock.getStartRow(),
                            excelBlock.getStartCol(),
                            excelBlock.getEndRow(),
                            excelBlock.getEndCol(),
                            rowOffset,
                            0,
                            mergedRegions);
        }

        if (styleMap.keySet().size() > 0){
            for (ExcelCellConditionStyle style : excelBlock.getStyles()){
                Object obj = ognlStack.getValue(style.getCondition());
                if (obj == null || !(obj instanceof Boolean)){
                    continue;
                }
                if (((Boolean) obj).booleanValue()){
                    BlockStyleSetter.set(
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

        //---------------------------------------------------------------
        for (ExcelCell excelCell : excelBlock.getCells()){
            String dataName = excelCell.getDataExpr() == null ? excelCell.getDataName() : excelCell.getDataExpr();
            if (dataName.startsWith("=")){
                dataName = FormulaEvaluatorUtil.offsetFormula(dataName, rowOffset, 0);
            }
            CellValueSetter.set(sheet, excelCell.getRow() + rowOffset, excelCell.getCol(), dataName, ognlStack);
            if (styleMap.keySet().size() > 0){
                for (ExcelCellConditionStyle style : excelCell.getStyles()){
                    Object obj = ognlStack.getValue(style.getCondition());
                    if (obj == null || !(obj instanceof Boolean)){
                        continue;
                    }
                    if (((Boolean) obj).booleanValue()){
                        CellStyleSetter.set(sheet, excelCell.getRow() + rowOffset, excelCell.getCol(), styleMap.get(style.getCellIndex()));
                    }
                }
            }
        }

        //---------------------------------------------------------------
        ExcelBlock childBlock = excelBlock.getChildBlock();
        if (childBlock != null){
            Object colValue = ognlStack.getValue(childBlock.getDataName());
            if (colValue == null){
                return;
            }
            Collection<? extends Object> listValue;
            if (!(colValue instanceof Collection)){
                if (colValue.getClass().isArray()){
                    listValue = Arrays.asList(colValue);
                }else{
                    ArrayList<Object> list = new ArrayList<Object>();
                    list.add(colValue);
                    listValue = list;
                }
            }else{
                listValue = (Collection<? extends Object>) colValue;
            }
            List<CellRangeAddress> childMergedRegions = null;
            if (mergedRegions != null){
                childMergedRegions = new ArrayList<>();
                for (CellRangeAddress cellRangeAddress : mergedRegions){
                    if (cellRangeAddress.getFirstRow() >= childBlock.getStartRow()
                                    && cellRangeAddress.getFirstColumn() >= childBlock.getStartCol()
                                    && cellRangeAddress.getLastRow() <= childBlock.getEndRow()
                                    && cellRangeAddress.getLastColumn() <= childBlock.getEndCol()){
                        childMergedRegions.add(cellRangeAddress);
                    }
                }
            }
            int colStep = 0;
            Object preObj = null;
            for (Object obj : listValue){
                ognlStack.push(obj);
                ognlStack.addContext("preColumn", preObj);
                ognlStack.addContext("columnNum", colStep);
                ColumnWriter.write(
                                sheet,
                                childBlock,
                                ognlStack,
                                rowOffset,
                                colStep * (childBlock.getEndCol() - childBlock.getStartCol() + 1),
                                childMergedRegions,
                                styleMap);
                colStep++;
                preObj = ognlStack.pop();
            }
            ognlStack.removeContext("preColumn");
            ognlStack.removeContext("columnNum");
        }
    }
}
