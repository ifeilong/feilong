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
package com.feilong.context.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.14.3
 */
public class JsonStringFormatterTest{

    private static final StringFormatter instance = JsonStringFormatter.INSTANCE;

    @Test
    public void test(){
        assertNull(instance.format(null));
    }

    @Test
    public void testEmpty(){
        assertEquals("", instance.format(""));
    }

    @Test
    public void testBlank(){
        assertEquals(" ", instance.format(" "));
    }
}
