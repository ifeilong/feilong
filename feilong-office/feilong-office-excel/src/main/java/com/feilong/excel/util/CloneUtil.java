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
package com.feilong.excel.util;

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.definition.ExcelCellConditionStyle;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.excel.definition.LoopBreakCondition;

/**
 * The Class CloneUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.0
 */
public class CloneUtil{

    /** Don't let anyone instantiate this class. */
    private CloneUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Clone block.
     *
     * @param aexcelBlock
     *            the aexcel block
     * @return the excel block
     */
    private static ExcelBlock cloneBlock(ExcelBlock aexcelBlock){
        if (null == aexcelBlock){
            return null;
        }
        //---------------------------------------------------------------
        ExcelBlock excelBlock = new ExcelBlock();
        excelBlock.setBreakCondition(cloneLoopBreakCondition(aexcelBlock.getBreakCondition()));
        excelBlock.setChild(aexcelBlock.isChild());
        excelBlock.setDataName(aexcelBlock.getDataName());
        excelBlock.setEndCol(aexcelBlock.getEndCol());
        excelBlock.setEndRow(aexcelBlock.getEndRow());
        excelBlock.setLoop(aexcelBlock.isLoop());
        excelBlock.setDirection(aexcelBlock.getDirection());
        excelBlock.setLoopClass(aexcelBlock.getLoopClass());
        excelBlock.setStartCol(aexcelBlock.getStartCol());
        excelBlock.setStartRow(aexcelBlock.getStartRow());

        //---------------------------------------------------------------
        List<ExcelCellConditionStyle> styles = newArrayList();
        for (ExcelCellConditionStyle excelCellConditionStyle : aexcelBlock.getStyles()){
            styles.add(cloneStyle(excelCellConditionStyle));
        }
        excelBlock.setStyles(styles);

        for (ExcelCell excelCell : aexcelBlock.getCells()){
            excelBlock.addCell(cloneCell(excelCell));
        }
        excelBlock.setChildBlock(cloneBlock(aexcelBlock.getChildBlock()));
        return excelBlock;
    }

    /**
     * Clone condition.
     *
     * @param loopBreakCondition
     *            the loop break condition
     * @return the loop break condition
     */
    private static LoopBreakCondition cloneLoopBreakCondition(LoopBreakCondition loopBreakCondition){
        if (null == loopBreakCondition){
            return null;
        }

        //---------------------------------------------------------------
        LoopBreakCondition condition = new LoopBreakCondition();
        condition.setRowOffset(loopBreakCondition.getRowOffset());
        condition.setColOffset(loopBreakCondition.getColOffset());
        condition.setFlagString(loopBreakCondition.getFlagString());
        return condition;
    }

    public static ExcelSheet cloneSheet(ExcelSheet aexcelSheet){
        ExcelSheet excelSheet = new ExcelSheet();
        excelSheet.setName(aexcelSheet.getName());
        excelSheet.setDisplayName(aexcelSheet.getDisplayName());
        for (ExcelBlock block : aexcelSheet.getSortedExcelBlocks()){
            excelSheet.addExcelBlock(cloneBlock(block));
        }
        return excelSheet;
    }

    /**
     * Clone map.
     *
     * @param map
     *            the map
     * @return the map
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    public static Map<String, Object> cloneMap(Map<String, Object> map) throws InstantiationException,IllegalAccessException{
        Map<String, Object> result = map.getClass().newInstance();
        for (String key : map.keySet()){
            Object obj = map.get(key);
            if (obj == null){
                continue;
            }
            if (obj instanceof Map){
                result.put(key, cloneMap((Map<String, Object>) obj));
            }else{
                result.put(key, obj.getClass().newInstance());
            }
        }
        return result;
    }

    /**
     * Clone cell.
     *
     * @param excelCell
     *            the excel cell
     * @return the excel cell
     */
    private static ExcelCell cloneCell(ExcelCell excelCell){
        ExcelCell cell = new ExcelCell();

        String[] availableChoices = excelCell.getAvailableChoices();
        cell.setAvailableChoices(availableChoices == null ? null : Arrays.asList(availableChoices).toArray(new String[0]));
        cell.setCol(excelCell.getCol());
        cell.setDataExpr(excelCell.getDataExpr());
        cell.setDataName(excelCell.getDataName());
        cell.setMandatory(excelCell.isMandatory());
        cell.setPattern(excelCell.getPattern());
        cell.setRow(excelCell.getRow());
        cell.setType(excelCell.getType());

        for (ExcelCellConditionStyle style : excelCell.getStyles()){
            cell.addStyle(cloneStyle(style));
        }
        return cell;
    }

    private static ExcelCellConditionStyle cloneStyle(ExcelCellConditionStyle excelCellConditionStyle){
        ExcelCellConditionStyle style = new ExcelCellConditionStyle();
        style.setCellIndex(excelCellConditionStyle.getCellIndex());
        style.setCondition(excelCellConditionStyle.getCondition());

        style.setEndRow(excelCellConditionStyle.getEndRow());
        style.setEndCol(excelCellConditionStyle.getEndCol());
        style.setStartRow(excelCellConditionStyle.getStartRow());
        style.setStartCol(excelCellConditionStyle.getStartCol());
        return style;
    }

}
