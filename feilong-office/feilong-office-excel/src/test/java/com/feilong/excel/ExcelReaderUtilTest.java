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
package com.feilong.excel;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.size;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;
import static com.feilong.lib.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.feilong.core.Validate;
import com.feilong.excel.consultant.ConsultantCommand;
import com.feilong.excel.definition.ExcelBlock;
import com.feilong.excel.definition.ExcelCell;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.io.InputStreamUtil;
import com.feilong.json.JsonUtil;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.2.1
 */
@lombok.extern.slf4j.Slf4j
public class ExcelReaderUtilTest{

    @Test
    public void test(){
        Map<String, String> map = toMap("A4", "itemCode", "B4", "itemName");

        map.put("C4", "memberName");
        map.put("D4", "content");
        map.put("E4", "createTime");
        map.put("F4", "resolveNote");

        map.put("G4", "resolveTime");
        map.put("H4", "resolveName");
        map.put("I4", "publishMark");

        List<ConsultantCommand> read = read(CLASSPATH_URL_PREFIX + "consultant/read202507101214.xlsx", ConsultantCommand.class, map);
        log.info("size:[{}] {}", size(read), JsonUtil.format(read));
    }

    /**
     * 获得 list.
     *
     * @param <T>
     *            the generic type
     * @param excelLocation
     *            比如 "E:\\Workspaces\\train\\Java培训报名12.xls"
     * @param loopClass
     *            要循环的列类
     * @param cellPositionMapping
     *            比如A1 隐射为name , B1隐射为age
     * @return the list
     * @since 4.3.1
     */
    public static <T> List<T> read(String excelLocation,Class<T> loopClass,Map<String, String> cellPositionMapping){
        Map<String, ExcelSheet> sheetDefinitions = build(loopClass, cellPositionMapping);
        ExcelReader excelReader = ExcelReaderUtil.buildExcelReader(sheetDefinitions);

        InputStream inputStream = InputStreamUtil.getInputStream(excelLocation);
        return ExcelReaderUtil.read(excelReader, inputStream, null, 0);
    }

    /**
     * 
     * @param sheetDefinitionLocations
     * @return key是sheet Name
     */
    static Map<String, ExcelSheet> build(Class<?> loopClass,Map<String, String> cellPositionMapping){
        Validate.notEmpty(cellPositionMapping, "cellPositionMapping can't be null/empty!");

        Map<String, ExcelSheet> sheetDefinitionsMap = newLinkedHashMap();

        //---------------------------------------------------------------
        ExcelBlock excelBlock = new ExcelBlock();

        excelBlock.setStartCellIndex("A4");
        excelBlock.setEndCellIndex("I4");

        excelBlock.setLoopClass(loopClass);
        excelBlock.setDataName("feilongTempBlockDataName");
        excelBlock.setLoop(true);

        List<ExcelCell> cells = newArrayList();
        for (Map.Entry<String, String> entry : cellPositionMapping.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            ExcelCell excelCell = new ExcelCell();
            excelCell.setCellIndex(key);
            excelCell.setDataName(value);
            cells.add(excelCell);
        }

        excelBlock.setCells(cells);

        //        excelBlock.setStartCol(0)

        //---------------------------------------------------------------
        ExcelSheet excelSheet = new ExcelSheet();
        excelSheet.setName("feilongTempSheet");
        excelSheet.setDisplayName("feilongTempSheet");
        excelSheet.setExcelBlock(excelBlock);

        sheetDefinitionsMap.put("feilongTempSheet", excelSheet);
        //---------------------------------------------------------------
        return sheetDefinitionsMap;
    }
}
