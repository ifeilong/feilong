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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.feilong.io.IOReaderUtil;
import com.feilong.io.ReaderConfig;
import com.feilong.io.ReaderUtil;

public class ReadToStringReaderAndReaderConfigTest{

    private final StringReader STRING_READER = ReaderUtil.newStringReader(" \n\n123456 \nA\n23456");

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testResolverFile(){
        IOReaderUtil.readToSet((Reader) null, new ReaderConfig());
    }

    //---------------------------------------------------------------
    @Test
    public void testResolverReaderAndReaderConfigTest() throws IOException{
        Set<String> codes = new LinkedHashSet<>();

        BufferedReader bufferedReader = new BufferedReader(STRING_READER);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null && lineTxt.trim() != ""){
            codes.add(lineTxt.trim());
        }

        //校验是否和自动生成的code(且未使用)重复、是否含中文，特殊符号，20字符内
        Iterator<String> iterator = codes.iterator();
        while (iterator.hasNext()){
            String code = iterator.next();
            if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
                iterator.remove();
            }
        }
        assertThat(codes, allOf(hasItem("123456"), not(hasItem("23456"))));
    }

    @Test
    public void testResolverReaderAndReaderConfigTest1(){
        Set<String> codes = IOReaderUtil.readToSet(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
        assertThat(codes, allOf(hasItem("123456"), not(hasItem("23456"))));
    }

    @Test
    public void testResolverReaderAndReaderConfigTest2(){
        Set<String> codes = IOReaderUtil.readToSet(STRING_READER, new ReaderConfig());
        assertThat(codes, allOf(hasItem("123456"), hasItem("23456")));
    }

    @Test
    public void testResolverReaderAndReaderConfigTest2333(){
        Set<String> codes = IOReaderUtil.readToSet(STRING_READER, null);
        assertThat(codes, allOf(hasItem("123456"), hasItem("23456")));
    }

    // \n\n123456 \nA\n23456

    @Test
    public void testResolverReaderAndReaderConfigTest3(){
        Set<String> codes = IOReaderUtil.readToSet(STRING_READER, new ReaderConfig(true, false));
        assertThat(codes, allOf(hasItem("123456 "), hasItem("A"), hasItem("23456"), not(hasItem(" "))));
    }

    @Test
    public void testResolverReaderAndReaderConfigTest4(){
        Set<String> codes = IOReaderUtil.readToSet(STRING_READER, new ReaderConfig(false, false));
        assertThat(codes, allOf(hasItem(" "), hasItem(""), hasItem("123456 "), hasItem("A"), hasItem("23456")));
    }

    // \n\n123456 \nA\n23456
    @Test
    public void testResolverReaderAndReaderConfigTest5(){
        Set<String> codes = IOReaderUtil.readToSet(STRING_READER, new ReaderConfig(false, true));
        assertThat(codes, allOf(hasItem(""), hasItem(""), hasItem("123456"), hasItem("A"), hasItem("23456")));
    }
}
