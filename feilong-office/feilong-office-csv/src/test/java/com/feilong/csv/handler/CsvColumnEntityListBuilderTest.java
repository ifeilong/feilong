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

import java.util.List;

import org.junit.Test;

import com.feilong.csv.User;
import com.feilong.csv.entity.BeanCsvConfig;
import com.feilong.csv.entity.CsvColumnEntity;

/**
 * {@link CsvColumnEntityListBuilder} 的单元测试.
 *
 * <p>
 * 覆盖: {@link com.feilong.csv.entity.CsvColumn} 的 name(含 trim) / order 排序 / include / exclude / 没有注解的字段
 * </p>
 */
public class CsvColumnEntityListBuilderTest{

    @Test
    public void testBuild(){
        List<CsvColumnEntity> csvColumnEntityList = CsvColumnEntityListBuilder.build(new BeanCsvConfig<>(User.class));

        assertEquals(3, csvColumnEntityList.size());

        //order: username=5 < age=10 < name=16
        assertEquals("username", csvColumnEntityList.get(0).getPropertyName());
        assertEquals("usern ame", csvColumnEntityList.get(0).getName());
        assertEquals(5, csvColumnEntityList.get(0).getOrder());

        assertEquals("age", csvColumnEntityList.get(1).getPropertyName());
        assertEquals("a ge", csvColumnEntityList.get(1).getName());
        assertEquals(10, csvColumnEntityList.get(1).getOrder());

        assertEquals("name", csvColumnEntityList.get(2).getPropertyName());
        assertEquals("名字", csvColumnEntityList.get(2).getName());
        assertEquals(16, csvColumnEntityList.get(2).getOrder());
    }

    //---------------------------------------------------------------

    @Test
    public void testBuildWithInclude(){
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);
        //include 匹配的是 @CsvColumn(name) 的名字, 不是字段名
        beanCsvConfig.setIncludePropertyNames("名字");

        List<CsvColumnEntity> csvColumnEntityList = CsvColumnEntityListBuilder.build(beanCsvConfig);

        assertEquals(1, csvColumnEntityList.size());
        assertEquals("name", csvColumnEntityList.get(0).getPropertyName());
    }

    @Test
    public void testBuildWithExclude(){
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);
        //exclude 匹配的是字段名
        beanCsvConfig.setExcludePropertyNames("name", "age");

        List<CsvColumnEntity> csvColumnEntityList = CsvColumnEntityListBuilder.build(beanCsvConfig);

        assertEquals(1, csvColumnEntityList.size());
        assertEquals("username", csvColumnEntityList.get(0).getPropertyName());
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullBeanCsvConfig(){
        CsvColumnEntityListBuilder.build(null);
    }

    //---------------------------------------------------------------

    @Test
    public void testBuildNoAnnotation(){
        List<CsvColumnEntity> csvColumnEntityList = CsvColumnEntityListBuilder.build(new BeanCsvConfig<>(NoAnnotationBean.class));

        assertEquals(1, csvColumnEntityList.size());
        //没有注解, name 就是字段名, order 是 0
        assertEquals("stringValue", csvColumnEntityList.get(0).getName());
        assertEquals("stringValue", csvColumnEntityList.get(0).getPropertyName());
        assertEquals(0, csvColumnEntityList.get(0).getOrder());
    }

    @Test
    public void testBuildSortByOrderThenPropertyName(){
        //两个字段都没有注解, order 都是 0, 那么按照 propertyName 升序
        List<CsvColumnEntity> csvColumnEntityList = CsvColumnEntityListBuilder.build(new BeanCsvConfig<>(TwoFieldBean.class));

        assertEquals(2, csvColumnEntityList.size());
        assertEquals("a", csvColumnEntityList.get(0).getPropertyName());
        assertEquals("b", csvColumnEntityList.get(1).getPropertyName());
    }

    @Test
    public void testBuildExcludeAll(){
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setExcludePropertyNames("username", "name", "age");

        assertEquals(0, CsvColumnEntityListBuilder.build(beanCsvConfig).size());
    }

    //---------------------------------------------------------------

    /** 没有 {@link com.feilong.csv.entity.CsvColumn} 注解的 bean. */
    public static class NoAnnotationBean{

        /** The string value. */
        private String stringValue;

        /**
         * Gets the string value.
         *
         * @return the string value
         */
        public String getStringValue(){
            return stringValue;
        }

        /**
         * Sets the string value.
         *
         * @param stringValue
         *            the string value
         */
        public void setStringValue(String stringValue){
            this.stringValue = stringValue;
        }
    }

    /** 两个字段都没有注解的 bean. */
    public static class TwoFieldBean{

        /** The b. */
        private String b;

        /** The a. */
        private String a;

        /**
         * Gets the a.
         *
         * @return the a
         */
        public String getA(){
            return a;
        }

        /**
         * Sets the a.
         *
         * @param a
         *            the a
         */
        public void setA(String a){
            this.a = a;
        }

        /**
         * Gets the b.
         *
         * @return the b
         */
        public String getB(){
            return b;
        }

        /**
         * Sets the b.
         *
         * @param b
         *            the b
         */
        public void setB(String b){
            this.b = b;
        }
    }
}
