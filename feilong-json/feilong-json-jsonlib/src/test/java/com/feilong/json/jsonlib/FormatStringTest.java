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

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.json.AbstractJsonTest;

public class FormatStringTest extends AbstractJsonTest{

    @Test
    public void formatArray2(){
        String json = "[{'name':'get'},{'name':'set'}]";
        LOGGER.debug(JsonUtil.format(json));
    }

    @Test
    public void formatCommonString(){
        String json = "feilong";
        assertEquals("feilong", JsonUtil.format(json));
    }

    @Test
    public void formatEmpty(){
        assertEquals(EMPTY, JsonUtil.format(""));
        assertEquals(" ", JsonUtil.format(" "));
    }

    @Test
    public void formatNull(){
        assertEquals(EMPTY, JsonUtil.format(null));
    }
}
