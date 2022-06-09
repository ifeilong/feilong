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
package com.feilong.core.lang.objectutil;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.core.lang.ObjectUtil;

public class EqualsAnyTest{

    @Test
    public void testNumber(){
        assertEquals(false, ObjectUtil.equalsAny(5L, 6, 5));
        assertEquals(true, ObjectUtil.equalsAny(5L, 5L, 6L));
        assertEquals(false, ObjectUtil.equalsAny(5L, 7L, 6L));
        assertEquals(true, ObjectUtil.equalsAny(5, 6, 5));
        assertEquals(true, ObjectUtil.equalsAny(5, null, 5));
        assertEquals(false, ObjectUtil.equalsAny(5, 6, 8));
        assertEquals(false, ObjectUtil.equalsAny(5, 6, null));
        assertEquals(false, ObjectUtil.equalsAny(5, 6, null, 9));
        assertEquals(false, ObjectUtil.equalsAny(5, 6, null, 9, 19, 20));
    }

    @Test
    public void testString(){
        assertEquals(false, ObjectUtil.equalsAny("abc", "ABC", "DEF"));
        assertEquals(true, ObjectUtil.equalsAny("abc", "abc", "def"));

        assertEquals(false, ObjectUtil.equalsAny("abc", null, "def"));
        assertEquals(false, ObjectUtil.equalsAny(null, "abc", "def"));

    }

    @Test
    public void testNull(){
        assertEquals(true, ObjectUtil.equalsAny(null, 1, null));
        assertEquals(true, ObjectUtil.equalsAny(null, null, null));
        assertEquals(false, ObjectUtil.equalsAny(null, null));
    }

}