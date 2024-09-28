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
import static org.hamcrest.Matchers.not;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;
import com.feilong.io.ReaderConfig;

public class ReadToListFileAndReaderConfigTest extends AbstractReadFileToStringTest{

    /** The file path. */
    private final File file = new File(floderPath + "src/test/resources/readtest.txt");

    //---------------------------------------------------------------

    /**
     * Test read file path and reader config test null.
     */
    @Test(expected = NullPointerException.class)
    public void testReadFilePathAndReaderConfigTestNull(){
        IOReaderUtil.readToList((File) null, new ReaderConfig());
    }

    //---------------------------------------------------------------

    /**
     * Test resolver reader and reader config test 1.
     */
    @Test
    public void testResolverReaderAndReaderConfigTest1(){
        List<String> codes = IOReaderUtil.readToList(file, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
        assertThat(codes, allOf(hasItem("123456"), not(hasItem("23456"))));
    }

    /**
     * Test resolver reader and reader config test 2.
     */
    @Test
    public void testResolverReaderAndReaderConfigTest2(){
        List<String> codes = IOReaderUtil.readToList(file, new ReaderConfig());
        assertThat(codes, allOf(hasItem("123456"), hasItem("23456")));
    }

    @Test
    public void testResolverReaderAndReaderConfigTest2333(){
        List<String> codes = IOReaderUtil.readToList(file, null);
        assertThat(codes, allOf(hasItem("123456"), hasItem("23456")));
    }

    // \n\n123456 \nA\n23456

    /**
     * Test resolver reader and reader config test 3.
     */
    @Test
    public void testResolverReaderAndReaderConfigTest3(){
        List<String> codes = IOReaderUtil.readToList(file, new ReaderConfig(true, false));
        assertThat(codes, allOf(hasItem("123456 "), hasItem("A"), hasItem("23456"), not(hasItem(" "))));
    }

    /**
     * Test resolver reader and reader config test 4.
     */
    @Test
    public void testResolverReaderAndReaderConfigTest4(){
        List<String> codes = IOReaderUtil.readToList(file, new ReaderConfig(false, false));
        assertThat(codes, allOf(hasItem(" "), hasItem(""), hasItem("123456 "), hasItem("A"), hasItem("23456")));
    }

    /**
     * Test resolver reader and reader config test 5.
     */
    // \n\n123456 \nA\n23456
    @Test
    public void testResolverReaderAndReaderConfigTest5(){
        List<String> codes = IOReaderUtil.readToList(file, new ReaderConfig(false, true));
        assertThat(codes, allOf(hasItem(""), hasItem(""), hasItem("123456"), hasItem("A"), hasItem("23456")));
    }
}
