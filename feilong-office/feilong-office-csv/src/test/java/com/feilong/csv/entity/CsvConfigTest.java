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
package com.feilong.csv.entity;

import static com.feilong.core.CharsetType.GBK;
import static com.feilong.core.CharsetType.UTF8;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.feilong.csv.User;

/**
 * {@link CsvConfig} 和 {@link BeanCsvConfig} 的单元测试.
 */
public class CsvConfigTest{

    //---------------------------------------------------------------
    //CsvConfig
    //---------------------------------------------------------------

    @Test
    public void testDefault(){
        CsvConfig csvConfig = new CsvConfig();

        assertEquals(UTF8, csvConfig.getEncode());
        assertEquals(',', csvConfig.getSeparator());
        assertTrue(csvConfig.getIsPrintHeaderLine());
    }

    @Test
    public void testConstructorWithEncode(){
        CsvConfig csvConfig = new CsvConfig(GBK);

        assertEquals(GBK, csvConfig.getEncode());
        assertEquals(',', csvConfig.getSeparator());
        assertTrue(csvConfig.getIsPrintHeaderLine());
    }

    @Test
    public void testConstructorWithEncodeAndSeparator(){
        CsvConfig csvConfig = new CsvConfig(GBK, ';');

        assertEquals(GBK, csvConfig.getEncode());
        assertEquals(';', csvConfig.getSeparator());
    }

    @Test
    public void testSetter(){
        CsvConfig csvConfig = new CsvConfig();

        csvConfig.setEncode(GBK);
        csvConfig.setSeparator('|');
        csvConfig.setIsPrintHeaderLine(false);

        assertEquals(GBK, csvConfig.getEncode());
        assertEquals('|', csvConfig.getSeparator());
        assertFalse(csvConfig.getIsPrintHeaderLine());
    }

    //---------------------------------------------------------------
    //BeanCsvConfig
    //---------------------------------------------------------------

    @Test
    public void testBeanCsvConfigDefault(){
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);

        assertSame(User.class, beanCsvConfig.getBeanClass());
        assertNull(beanCsvConfig.getIncludePropertyNames());
        assertNull(beanCsvConfig.getExcludePropertyNames());

        //继承自 CsvConfig 的默认值
        assertEquals(UTF8, beanCsvConfig.getEncode());
        assertEquals(',', beanCsvConfig.getSeparator());
        assertTrue(beanCsvConfig.getIsPrintHeaderLine());
    }

    @Test
    public void testBeanCsvConfigSetter(){
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);

        beanCsvConfig.setIncludePropertyNames("name");
        beanCsvConfig.setExcludePropertyNames("age", "username");
        beanCsvConfig.setEncode(GBK);
        beanCsvConfig.setSeparator(';');
        beanCsvConfig.setIsPrintHeaderLine(false);

        assertArrayEquals(new String[] { "name" }, beanCsvConfig.getIncludePropertyNames());
        assertArrayEquals(new String[] { "age", "username" }, beanCsvConfig.getExcludePropertyNames());
        assertEquals(GBK, beanCsvConfig.getEncode());
        assertEquals(';', beanCsvConfig.getSeparator());
        assertFalse(beanCsvConfig.getIsPrintHeaderLine());
    }

    @Test
    public void testBeanCsvConfigSetBeanClass(){
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setBeanClass(null);

        assertNull(beanCsvConfig.getBeanClass());
    }
}
