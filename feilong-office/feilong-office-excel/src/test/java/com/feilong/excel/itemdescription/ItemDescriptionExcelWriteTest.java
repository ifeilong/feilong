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
package com.feilong.excel.itemdescription;

import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;

import org.junit.Test;

import com.feilong.excel.AbstractLoxiaWriteTest;

public class ItemDescriptionExcelWriteTest extends AbstractLoxiaWriteTest{

    @Test
    public void test(){
        String configurations = "loxia/ItemDescription/feilong-sheets-productList2.xml";
        String templateFileName = "ItemDescription/itemdescription-export-2.xlsx";

        String sheetName = "secondItemDescriptionExport";
        //---------------------------------------------------------------

        String dataName = "secondItemDescriptionList";
        build(templateFileName, configurations, sheetName, dataName, buildList());
    }

    private List<ItemDescriptionCommand> buildList(){
        List<ItemDescriptionCommand> list = newArrayList();

        for (int i = 0; i < 2; ++i){
            ItemDescriptionCommand command = new ItemDescriptionCommand();
            //            itemUpdatePriceCommand.setCode(i + "");
            //            itemUpdatePriceCommand.setItemId((long) i);
            //            itemUpdatePriceCommand.setItemListPrice(toBigDecimal(2000));
            //            itemUpdatePriceCommand.setItemSalePrice(toBigDecimal(20));
            command.setStyle("810966G0033" + i);
            //  itemUpdatePriceCommand.setStyle("810966G0033" + i);
            command.setItemCode("哈哈哈" + i);
            command.setBullet("Bullet" + i);
            command.setImgUrl("啦啦啦啦" + i);
            list.add(command);
        }
        return list;
    }
}
