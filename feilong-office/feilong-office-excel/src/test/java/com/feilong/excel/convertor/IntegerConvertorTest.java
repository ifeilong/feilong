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
package com.feilong.excel.convertor;

import static com.feilong.core.bean.ConvertUtil.toInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.feilong.excel.definition.ExcelCell;
import com.feilong.lib.excel.convertor.IntegerConvertor;
import com.feilong.test.AbstractTest;

public class IntegerConvertorTest extends AbstractTest{

    private final ExcelCell cellDefinition          = new ExcelCell();

    IntegerConvertor        feilongIntegerConvertor = new IntegerConvertor();
    {
        cellDefinition.setMandatory(true);
    }

    //---------------------------------------------------------------

    @Test
    public void testIntegerConvertorDouble(){
        Integer convert = feilongIntegerConvertor.convert(1.0, 1, "F2", cellDefinition);
        assertEquals(toInteger(1), convert);
    }

    //---------------------------------------------------------------

    @Test
    public void testString(){
        Integer convert = feilongIntegerConvertor.convert("1.0", 1, "F2", cellDefinition);
        assertNull(convert);
    }

    //---------------------------------------------------------------

    @Test
    public void testIntegerConvertorTest1(){
        assertNull(toInteger("1.0"));
    }
}
