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
package com.feilong.csv;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.csv.entity.CsvConfigTest;
import com.feilong.csv.handler.CsvColumnEntityBuilderTest;
import com.feilong.csv.handler.CsvColumnEntityListBuilderTest;
import com.feilong.csv.handler.CsvContentBuilderTest;
import com.feilong.csv.handler.DataListBuilderTest;

@RunWith(Suite.class)
@SuiteClasses({ //

                //参数校验
                DefaultCsvWriteWriteDataTest.class,
                DefaultCsvWriteWriteDataWithBeanCsvConfigTest.class,

                //核心逻辑
                CsvContentBuilderTest.class,
                CsvColumnEntityBuilderTest.class,
                CsvColumnEntityListBuilderTest.class,
                DataListBuilderTest.class,

                //配置
                CsvConfigTest.class,

                //写文件之后读回来断言
                DefaultCsvWriteWriteFileTest.class
        //
})
public class SuiteTests{

}
