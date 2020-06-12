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
package com.feilong.io.ioreaderutil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;

import java.util.Set;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;

public class ReadToSetTest extends AbstractReadFileToStringTest{

    /**
     * Test read file path and reader config test null.
     */
    @Test(expected = NullPointerException.class)
    public void testReadFilePathAndReaderConfigTestNull(){
        IOReaderUtil.readToSet((String) null);
    }

    /**
     * Test read file path and reader config test empty.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadFilePathAndReaderConfigTestEmpty(){
        IOReaderUtil.readToSet("");
    }

    /**
     * Test read file path and reader config test blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadFilePathAndReaderConfigTestBlank(){
        IOReaderUtil.readToSet(" ");
    }

    //---------------------------------------------------------------

    /**
     * Test resolver reader and reader config test 2.
     */
    @Test
    public void testResolverReaderAndReaderConfigTest2(){
        Set<String> codes = IOReaderUtil.readToSet(floderPath + "src/test/resources/readtest.txt");
        assertThat(codes, allOf(hasItem("123456"), hasItem("23456")));
    }

}
