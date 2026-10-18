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
package com.feilong.csv.handler;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Test;

import com.feilong.csv.User;
import com.feilong.csv.entity.CsvColumnEntity;

/**
 * {@link CsvColumnEntityBuilder} 的单元测试.
 *
 * <p>
 * 覆盖: {@link com.feilong.csv.entity.CsvColumn#name()} 的 trim / 没有注解时候取字段名 / {@link com.feilong.csv.entity.CsvColumn#order()}
 * </p>
 */
public class CsvColumnEntityBuilderTest{

    @Test
    public void testBuildCsvColumnEntity() throws Exception{
        Field field = User.class.getDeclaredField("username");

        CsvColumnEntity csvColumnEntity = CsvColumnEntityBuilder.buildCsvColumnEntity(field);

        //@CsvColumn(name = "usern ame ",order = 5), name 会去掉前后空格
        assertEquals("usern ame", csvColumnEntity.getName());
        assertEquals("username", csvColumnEntity.getPropertyName());
        assertEquals(5, csvColumnEntity.getOrder());
    }

    @Test
    public void testBuildCsvColumnEntityWithoutAnnotation() throws Exception{
        //没有 @CsvColumn 注解, name 就是字段名字, order 是 0
        Field field = CsvColumnEntity.class.getDeclaredField("propertyName");

        CsvColumnEntity csvColumnEntity = CsvColumnEntityBuilder.buildCsvColumnEntity(field);

        assertEquals("propertyName", csvColumnEntity.getName());
        assertEquals("propertyName", csvColumnEntity.getPropertyName());
        assertEquals(0, csvColumnEntity.getOrder());
    }
}
