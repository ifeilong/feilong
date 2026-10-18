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

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.csv.User;
import com.feilong.csv.entity.CsvColumnEntity;

/**
 * {@link DataListBuilder} 的单元测试.
 *
 * <p>
 * 覆盖: 列顺序 / 值转字符串 / null 值转空字符串
 * </p>
 */
public class DataListBuilderTest{

    @Test
    public void testBuild(){
        List<CsvColumnEntity> csvColumnEntityList = toList(
                        new CsvColumnEntity("usern ame", "username", 5),
                        new CsvColumnEntity("a ge", "age", 10),
                        new CsvColumnEntity("名字", "name", 16));

        List<Object[]> dataList = DataListBuilder.build(toList(new User("zs", "张三", 18), new User("ls", "李四", 28)), csvColumnEntityList);

        //---------------------------------------------------------------
        assertEquals(2, dataList.size());

        //值的顺序按照 csvColumnEntityList 的属性顺序
        assertArrayEquals(new Object[] { "zs", "18", "张三" }, dataList.get(0));
        assertArrayEquals(new Object[] { "ls", "28", "李四" }, dataList.get(1));
    }

    @Test
    public void testBuildColumnOrder(){
        //只有一列, 并且顺序和传入的一致
        List<CsvColumnEntity> csvColumnEntityList = toList(new CsvColumnEntity("名字", "name", 16));

        List<Object[]> dataList = DataListBuilder.build(toList(new User("zs", "张三", 18)), csvColumnEntityList);

        assertEquals(1, dataList.size());
        assertEquals(1, dataList.get(0).length);
        assertEquals("张三", dataList.get(0)[0]);
    }

    @Test
    public void testBuildNullValueToEmpty(){
        List<CsvColumnEntity> csvColumnEntityList = toList(new CsvColumnEntity("名字", "name", 16));

        //name 是 null, 转换成空字符串
        assertArrayEquals(new Object[] { "" }, DataListBuilder.build(toList(new User("zs", null, 18)), csvColumnEntityList).get(0));
    }

    @Test
    public void testBuildEmptyIterable(){
        List<CsvColumnEntity> csvColumnEntityList = toList(new CsvColumnEntity("名字", "name", 16));

        assertEquals(0, DataListBuilder.build(toList(), csvColumnEntityList).size());
    }
}
