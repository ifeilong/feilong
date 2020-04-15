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
package com.feilong.office.excel.itemupdateprice;

import static com.feilong.core.bean.ConvertUtil.toBigDecimal;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;

import org.junit.Test;

import com.feilong.office.excel.AbstractLoxiaExcelWriteTest;

public class ExcelWriteItemUpdatePriceCommandTest extends AbstractLoxiaExcelWriteTest{

    @Test
    public void test(){
        String configurations = "loxia/ItemUpdatePrice/feilong-sheets-productList.xml";
        String templateFileName = "ItemUpdatePrice/template-product-list.xls";

        String sheetName = "productListSheet";
        //---------------------------------------------------------------
        build(templateFileName, configurations, sheetName, "productList", buildList());
    }

    private List<ItemUpdatePriceCommand> buildList(){
        List<ItemUpdatePriceCommand> list = newArrayList();

        for (int i = 0; i < 1; ++i){
            ItemUpdatePriceCommand itemUpdatePriceCommand = new ItemUpdatePriceCommand();
            itemUpdatePriceCommand.setCode(i + "");
            itemUpdatePriceCommand.setItemId((long) i);
            itemUpdatePriceCommand.setItemListPrice(toBigDecimal(2000));
            itemUpdatePriceCommand.setItemSalePrice(toBigDecimal(20));
            itemUpdatePriceCommand.setStyle("810966G0033");
            itemUpdatePriceCommand.setSubTitle("哈哈哈");
            itemUpdatePriceCommand.setTitle("啦啦啦啦");
            list.add(itemUpdatePriceCommand);
        }
        return list;
    }
}
