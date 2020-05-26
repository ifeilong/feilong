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
package com.feilong.excel.consultant;

import static com.feilong.lib.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.excel.AbstractReadTest;

@SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
public class ConsultantReadTest extends AbstractReadTest{

    @Test
    public void test(){
        String excelLocation = CLASSPATH_URL_PREFIX + "consultant/read20200428214903.xlsx";
        String sheetDefinitionLocation = CLASSPATH_URL_PREFIX + "consultant/sheets-definition.xml";
        String sheetName = "consultantExport";
        String dataName = "consultantList";

        //---------------------------------------------------------------
        List<ConsultantCommand> list = read(excelLocation, sheetDefinitionLocation, sheetName, dataName);
        assertEquals(300, list.size());
    }

}
