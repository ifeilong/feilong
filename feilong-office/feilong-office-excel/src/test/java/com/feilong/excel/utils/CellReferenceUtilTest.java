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
package com.feilong.excel.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.excel.util.CellReferenceUtil;
import com.feilong.test.AbstractTest;

public class CellReferenceUtilTest extends AbstractTest{

    @Test
    public void test(){
        assertEquals("B3", CellReferenceUtil.getCellRef(2, 1));
        assertEquals("A1", CellReferenceUtil.getCellRef(0, 0));
    }

    @Test
    public void test1(){
        assertEquals("C5", CellReferenceUtil.getCellRef("B3", 2, 1));
        assertEquals("A1", CellReferenceUtil.getCellRef("A1", 0, 0));
    }

    @Test
    public void test21(){
        int[] cellPosition = CellReferenceUtil.getCellPosition("B3");
        assertEquals(2, cellPosition[0]);
        assertEquals(1, cellPosition[1]);
    }
}
