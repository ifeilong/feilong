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
package com.feilong.office.excel.subscribtion;

import static com.feilong.core.bean.ConvertUtil.toMap;
import static com.feilong.core.date.DateUtil.nowTimestamp;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.io.FileUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.office.excel.ExcelWriter;
import com.feilong.tools.slf4j.Slf4jUtil;

@ContextConfiguration(locations = { "classpath*:loxia/subscribtion/spring-excel-2sheet.xml" })
public class SpringLoxiaExcelWriteTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    @Qualifier("all订阅Writer")
    private ExcelWriter allWriter;

    //---------------------------------------------------------------

    @Test
    public void test(){
        String templateFileName = "loxia/subscribtion/template-list-2sheet.xlsx";

        List<SubscribtionCommand> alist = buildList(2);
        List<SubscribtionCommand> blist = alist;

        String outputFileName = Slf4jUtil
                        .format("/Users/feilong/feilong/excel/{}{}.{}", "111", nowTimestamp(), FilenameUtil.getExtension(templateFileName));

        Map<String, Object> beans = toMap(//
                        "alist",
                        (Object) alist,
                        "blist",
                        blist);

        allWriter.write(templateFileName, FileUtil.getFileOutputStream(outputFileName), beans);
    }

    //---------------------------------------------------------------

    private List<SubscribtionCommand> buildList(int length){
        List<SubscribtionCommand> list = newArrayList();

        for (int i = 1; i <= length; ++i){
            SubscribtionCommand subscribtionCommand = new SubscribtionCommand();
            subscribtionCommand.setEmail("xin.jin" + i + "@**.com");
            subscribtionCommand.setMobile("15001841318" + i);
            list.add(subscribtionCommand);
        }
        return list;
    }
}
