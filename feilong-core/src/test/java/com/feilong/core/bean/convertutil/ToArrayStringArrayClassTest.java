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
package com.feilong.core.bean.convertutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.Serializable;

import org.junit.Test;

import com.feilong.core.bean.ConvertUtil;

public class ToArrayStringArrayClassTest{

    @Test
    public void testConvert1(){
        assertEquals((Serializable) null, ConvertUtil.toArray((String[]) null, Serializable.class));
    }

    @Test(expected = NullPointerException.class)
    public void testConvert3(){
        String[] strings = toArray("");
        ConvertUtil.toArray(strings, null);
    }

    @Test
    public void testConvertArray(){
        String[] ss = { "2", "1" };
        assertArrayEquals(new Long[] { 2L, 1L }, toArray(ss, Long.class));
    }
}
