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
package com.feilong.office.excel.loxia.convertor;

import static com.feilong.core.bean.ConvertUtil.toInteger;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import loxia.support.excel.convertor.IntegerConvertor;
import loxia.support.excel.definition.ExcelCell;
import loxia.support.excel.exception.ExcelManipulateException;

public class IntegerConvertorTest{

    private final ExcelCell                                   cellDefinition          = new ExcelCell();

    com.feilong.office.excel.loxia.convertor.IntegerConvertor feilongIntegerConvertor = new com.feilong.office.excel.loxia.convertor.IntegerConvertor();
    {
        cellDefinition.setMandatory(true);
    }

    @Test(expected = ExcelManipulateException.class)
    public void testIntegerConvertorTest() throws ExcelManipulateException{
        IntegerConvertor integerConvertor = new IntegerConvertor();
        integerConvertor.convert("1.0", 1, "F2", cellDefinition);
    }

    //---------------------------------------------------------------

    @Test
    public void testIntegerConvertorDouble() throws ExcelManipulateException{
        Integer convert = feilongIntegerConvertor.convert(1.0, 1, "F2", cellDefinition);
        assertEquals(toInteger(1), convert);
    }

    //---------------------------------------------------------------

    @Test
    public void testIntegerConvertorTest22() throws ExcelManipulateException{
        Integer convert = feilongIntegerConvertor.convert("1.0", 1, "F2", cellDefinition);
        System.out.println(convert);
    }

    //---------------------------------------------------------------

    @Test
    public void testIntegerConvertorTest1(){
        System.out.println(toInteger("1.0"));//TODO:remove
    }
}
