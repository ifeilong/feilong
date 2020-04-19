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
package com.feilong.json.jsonlib;

import java.util.Vector;

import org.junit.Test;

import com.feilong.json.AbstractJsonTest;

public class FormatListTest extends AbstractJsonTest{

    @Test
    @SuppressWarnings("static-method")
    public void testVector(){
        Vector<Integer> vector = new Vector<>();
        vector.add(1);
        vector.add(2222);
        vector.add(3333);
        vector.add(55555);
        LOGGER.debug("vector:{}", JsonUtil.format(vector));
        LOGGER.debug("" + vector.get(0));
    }
}
