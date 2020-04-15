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

import static com.feilong.core.date.DateUtil.nowTimestamp;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.junit.Test;

import com.feilong.coreextension.awt.DesktopUtil;

/**
 * 生成 excel 测试.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Feb 10, 2014 2:33:52 AM
 */
public class ExcelCreateUtilTest2{

    @Test
    public void createExcel(){
        final int columnSize = 5;
        final int dataSize = 2;

        //---------------------------------------------------------------
        String[] columnsTitle = buildColumns(columnSize);
        List<String[]> list = buildData(columnSize, dataSize);

        //---------------------------------------------------------------
        ExcelConfigEntity excelEntity = new ExcelConfigEntity();
        //        excelEntity.setSpecialString("G");
        //        excelEntity.setIsHasSpecialStringToAddStyle(true);
        //        excelEntity.setIsRowChangeColor(false);
        //---------------------------------------------------------------
        String fileName = SystemUtils.USER_HOME + "/feilong/" + nowTimestamp() + ".xls";

        ExcelCreateUtil.create(columnsTitle, list, fileName, excelEntity);

        DesktopUtil.open(fileName);
    }

    private String[] buildColumns(final int size){
        String[] columnsTitle = new String[size];
        columnsTitle[0] = "姓名";
        columnsTitle[1] = "地址";
        columnsTitle[2] = "email";

        for (int i = 3; i < size; i++){
            columnsTitle[i] = "呵呵" + i;
        }
        return columnsTitle;
    }

    private List<String[]> buildData(final int size,final int dataSize){
        List<String[]> list = newArrayList();
        String[] temp = null;

        for (int i = 0; i < dataSize; i++){
            temp = new String[size];
            temp[0] = "" + i;
            temp[1] = "GABC22222";
            temp[2] = "xy2venus@163.com";
            for (int j = 3; j < size; j++){
                temp[j] = "呵呵" + j;
            }
            list.add(temp);
        }
        return list;
    }
}
