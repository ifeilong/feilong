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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.feilong.excel.definition.ExcelBlock;
import com.feilong.lib.excel.ognl.OgnlStack;

@lombok.extern.slf4j.Slf4j
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
class BlockLoopVerticalWriter{

    /** Don't let anyone instantiate this class. */
    private BlockLoopVerticalWriter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    static void write(
                    Sheet sheet,
                    ExcelBlock excelBlock,
                    OgnlStack ognlStack,
                    List<CellRangeAddress> mergedRegions,
                    Map<String, CellStyle> styleMap){
        try{
            Object value = ognlStack.getValue(excelBlock.getDataName());
            if (value == null){
                return;
            }
            //---------------------------------------------------------------
            Collection<?> listValue = DataToCollectionUtil.convert(value);
            int endCol = excelBlock.getEndCol();
            int startCol = excelBlock.getStartCol();

            int step = 0;
            Object preObj = null;
            for (Object obj : listValue){
                ognlStack.push(obj);
                ognlStack.addContext("preLine", preObj);
                ognlStack.addContext("lineNum", step - 1);

                int colOffset = step * (endCol - startCol + 1);
                ColumnWriter.write(sheet, excelBlock, ognlStack, 0, colOffset, mergedRegions, styleMap);
                step++;
                preObj = ognlStack.pop();
            }
            ognlStack.removeContext("preLine");
            ognlStack.removeContext("lineNum");
        }catch (Exception e){
            log.error("", e);
        }
    }

}
