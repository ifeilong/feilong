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
package com.feilong.excel.subscribtion;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;

import org.junit.Test;

import com.feilong.excel.AbstractWriteTest;

public class ExcelWrite订阅Test extends AbstractWriteTest{

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void test(){
        String templateFileName = "classpath:subscribtion/template-list-2sheet.xlsx";
        String sheetDefinitionLocation = "classpath:subscribtion/sheets-definition.xml";

        handle(
                        templateFileName,
                        sheetDefinitionLocation,
                        toArray("aSheet", "bSheet"), //
                        toMap(
                                        "alist",
                                        (Object) buildAList(), //

                                        "blist",
                                        buildBList()));
    }

    //---------------------------------------------------------------

    private List<SubscribtionCommand> buildAList(){
        List<SubscribtionCommand> list = newArrayList();

        for (int i = 1; i <= 100; ++i){
            SubscribtionCommand subscribtionCommand = new SubscribtionCommand();
            subscribtionCommand.setEmail("xin.jin" + i + "@**.com");
            subscribtionCommand.setMobile("15001841318" + i);
            list.add(subscribtionCommand);
        }
        return list;
    }

    private List<SubscribtionCommand> buildBList(){
        List<SubscribtionCommand> list = newArrayList();

        for (int i = 1; i <= 100; ++i){
            SubscribtionCommand subscribtionCommand = new SubscribtionCommand();
            subscribtionCommand.setEmail("xin.jin" + i + "@**.com");
            subscribtionCommand.setMobile("15001841318" + i);
            list.add(subscribtionCommand);
        }
        return list;
    }
}
