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

import static com.feilong.core.CharsetType.UTF8;
import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.feilong.csv.entity.CsvConfig;

/**
 * {@link CsvContentBuilder} 的单元测试.
 *
 * <p>
 * 覆盖: 单行 / 多行 / 空值列 / 空行 / 引号转义 / 自定义分隔符 / 空集合
 * </p>
 */
public class CsvContentBuilderTest{

    /** The csv config. */
    private static final CsvConfig CSV_CONFIG = new CsvConfig();

    //---------------------------------------------------------------

    @Test
    public void testBuildOneLine(){
        assertEquals("\"a\",\"b\"", CsvContentBuilder.build(toLines(new Object[] { "a", "b" }), CSV_CONFIG));
    }

    @Test
    public void testBuildMultiLine(){
        List<Object[]> allLines = toLines(new Object[] { "a", "b" }, new Object[] { "1", "2" }, new Object[] { "3", "4" });

        assertEquals(
                        "\"a\",\"b\"" + lineSeparator() + "\"1\",\"2\"" + lineSeparator() + "\"3\",\"4\"",
                        CsvContentBuilder.build(allLines, CSV_CONFIG));
    }

    @Test
    public void testBuildEmptyLine(){
        assertEquals("", CsvContentBuilder.build(toLines(new Object[0]), CSV_CONFIG));
    }

    @Test
    public void testBuildEmptyList(){
        assertEquals("", CsvContentBuilder.build(new ArrayList<Object[]>(), CSV_CONFIG));
    }

    //---------------------------------------------------------------
    //列值
    //---------------------------------------------------------------

    @Test
    public void testBuildNullColumnValue(){
        //null 的列不会输出引号, 但是分隔符要保留
        assertEquals("\"a\",,\"c\"", CsvContentBuilder.build(toLines(new Object[] { "a", null, "c" }), CSV_CONFIG));
    }

    @Test
    public void testBuildAllNullColumnValue(){
        assertEquals(",", CsvContentBuilder.build(toLines(new Object[] { null, null }), CSV_CONFIG));
    }

    @Test
    public void testBuildEscapeQuote(){
        //字段内嵌的引号, 转义成两个引号
        assertEquals("\"a\"\"b\"", CsvContentBuilder.build(toLines(new Object[] { "a\"b" }), CSV_CONFIG));
    }

    @Test
    public void testBuildCommaNotEscape(){
        //逗号不用特殊处理, 因为每个字段都被引号包裹了
        assertEquals("\"a,b\"", CsvContentBuilder.build(toLines(new Object[] { "a,b" }), CSV_CONFIG));
    }

    //---------------------------------------------------------------

    @Test
    public void testBuildCustomSeparator(){
        List<Object[]> allLines = toLines(new Object[] { "a", "b" }, new Object[] { "1", "2" });

        assertEquals("\"a\";\"b\"" + lineSeparator() + "\"1\";\"2\"", CsvContentBuilder.build(allLines, new CsvConfig(UTF8, ';')));
    }

    //---------------------------------------------------------------

    /**
     * 构造 <code>List&lt;Object[]&gt;</code>.
     *
     * <p>
     * 不用 {@code ConvertUtil.toList(new Object[]{...})} —— 只有一个数组参数的时候, 它会被当成可变参数展开, 得到一个 List&lt;Object&gt;.
     * </p>
     *
     * @param lines
     *            the lines
     * @return the list
     */
    private static List<Object[]> toLines(Object[]...lines){
        List<Object[]> list = new ArrayList<>(lines.length);
        for (Object[] line : lines){
            list.add(line);
        }
        return list;
    }
}
