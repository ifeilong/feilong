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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.feilong.excel.consultant.ConsultantReadTest;
import com.feilong.excel.loxia.convertor.IntegerConvertorTest;
import com.feilong.excel.utils.CellReferenceUtilTest;
import com.feilong.excel.销售数据.Write审计Test;

@RunWith(Suite.class)
@SuiteClasses({ //

                IntegerConvertorTest.class,

                CellReferenceUtilTest.class,

                Write审计Test.class,
                ConsultantReadTest.class,
        //
})
public class SuiteTests{

}
