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

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.excel.AbstractLoxiaReadTest;
import com.feilong.lib.lang3.SystemUtils;

public class ConsultantReadTest extends AbstractLoxiaReadTest{

    @Test
    public void test(){
        String xmlSheetConfiguration = "loxia/consultant/feilong-sheets-Consultant.xml";
        String sheetName = "consultantExport";
        String dataName = "consultantList";
        String fileName = SystemUtils.USER_HOME
                        + "/workspace/feilong/feilong/feilong-office/feilong-office-excel/src/test/resources/loxia/consultant/read20200428214903.xlsx";

        //---------------------------------------------------------------
        List<ConsultantCommand> list = build(xmlSheetConfiguration, sheetName, dataName, fileName);
        assertEquals(300, list.size());
    }

}
