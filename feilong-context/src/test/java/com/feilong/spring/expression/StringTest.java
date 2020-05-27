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
package com.feilong.spring.expression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class StringTest{

    /**
     * 获得 value.
     */
    @Test
    public void getValue(){
        String ex = "'Hello,World'";

        assertEquals("Hello,World", SpelUtil.getValue(ex));

        Object value = SpelUtil.getValue(ex + ".length()");
        assertEquals(11, value);

        assertEquals("Hello,World!", SpelUtil.getValue(ex + ".concat('!')"));

        assertEquals(String.class, SpelUtil.getValue(ex + ".class"));

        Object value2 = SpelUtil.getValue(ex + ".bytes.length");
        assertEquals(11, value2);

        assertEquals("HELLO,WORLD", SpelUtil.getValue("new String(" + ex + ").toUpperCase()"));
    }

}
