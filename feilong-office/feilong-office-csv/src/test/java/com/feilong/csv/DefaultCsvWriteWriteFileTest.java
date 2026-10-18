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
package com.feilong.csv;

import static com.feilong.core.CharsetType.GBK;
import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.bean.ConvertUtil.toList;
import static java.lang.System.lineSeparator;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.feilong.csv.entity.BeanCsvConfig;
import com.feilong.csv.entity.CsvConfig;

/**
 * {@link DefaultCsvWrite} 的单元测试.
 *
 * <p>
 * 和 {@link CsvArrayWriteTest} / {@link CsvBeanWriteTest} 不同, 本类写到<b>临时文件</b>之后会读回来做断言, 不依赖桌面环境和人工查看.
 * </p>
 *
 * <p>
 * 覆盖: 数组写法(带/不带标题行, 自定义分隔符, 编码, 转义) / bean 写法(自动配置, exclude, include, 不打印标题行) / 参数校验
 * </p>
 */
public class DefaultCsvWriteWriteFileTest{

    /** The csv write. */
    private final CsvWrite csvWrite = new DefaultCsvWrite();

    /** 临时文件路径. */
    private String          path;

    @Before
    public void before(){
        path = new File(System.getProperty("java.io.tmpdir"), "feilong-csv-test-" + System.nanoTime() + ".csv").getAbsolutePath();
    }

    @After
    public void after(){
        new File(path).delete();
    }

    //---------------------------------------------------------------
    //数组写法
    //---------------------------------------------------------------

    @Test
    public void testWriteArrayWithHeaderLine() throws IOException{
        String[] columnTitles = { "a", "b" };
        List<Object[]> dataList = toLines(new Object[] { "1", "2" }, new Object[] { "3", "4" });

        csvWrite.write(path, columnTitles, dataList, new CsvConfig(UTF8));

        assertEquals(
                        "\"a\",\"b\"" + lineSeparator() + "\"1\",\"2\"" + lineSeparator() + "\"3\",\"4\"",
                        readFile(path, UTF8));
    }

    @Test
    public void testWriteArrayWithoutHeaderLine() throws IOException{
        String[] columnTitles = { "a", "b" };
        List<Object[]> dataList = toLines(new Object[] { "1", "2" });

        CsvConfig csvConfig = new CsvConfig(UTF8);
        csvConfig.setIsPrintHeaderLine(false);
        csvWrite.write(path, columnTitles, dataList, csvConfig);

        assertEquals("\"1\",\"2\"", readFile(path, UTF8));
    }

    @Test
    public void testWriteArrayOnlyDataList() throws IOException{
        //没有标题行
        csvWrite.write(path, null, toLines(new Object[] { "1", "2" }), new CsvConfig(UTF8));

        assertEquals("\"1\",\"2\"", readFile(path, UTF8));
    }

    @Test
    public void testWriteArrayEscapeAndCustomSeparator() throws IOException{
        csvWrite.write(path, null, toLines(new Object[] { "a\"b", "c,d" }), new CsvConfig(UTF8, ';'));

        assertEquals("\"a\"\"b\";\"c,d\"", readFile(path, UTF8));
    }

    @Test
    public void testWriteArrayWithGBK() throws IOException{
        csvWrite.write(path, null, toLines(new Object[] { "张三" }), new CsvConfig(GBK));

        assertEquals("\"张三\"", readFile(path, GBK));
    }

    @Test
    public void testWriteArrayUseDefaultCsvConfig() throws IOException{
        //不传 csvConfig, 使用默认的 UTF8 + 逗号 + 打印标题行
        csvWrite.write(path, new String[] { "a" }, toLines(new Object[] { "1" }));

        assertEquals("\"a\"" + lineSeparator() + "\"1\"", readFile(path, UTF8));
    }

    //---------------------------------------------------------------
    //bean 写法
    //---------------------------------------------------------------

    @Test
    public void testWriteBean() throws IOException{
        List<User> users = toList(new User("zhangsan", "张三", 18), new User("lisi", "李四", 28));

        //不传 BeanCsvConfig, 自动根据第一个元素构建
        csvWrite.write(path, users);

        //列顺序按照 @CsvColumn 的 order: username(5) -> age(10) -> name(16)
        assertEquals(
                        "\"usern ame\",\"a ge\",\"名字\"" + lineSeparator() + "\"zhangsan\",\"18\",\"张三\"" + lineSeparator()
                                        + "\"lisi\",\"28\",\"李四\"",
                        readFile(path, UTF8));
    }

    @Test
    public void testWriteBeanWithExclude() throws IOException{
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setExcludePropertyNames("age", "name");

        csvWrite.write(path, toList(new User("zhangsan", "张三", 18)), beanCsvConfig);

        assertEquals("\"usern ame\"" + lineSeparator() + "\"zhangsan\"", readFile(path, UTF8));
    }

    @Test
    public void testWriteBeanWithIncludeAndNoHeader() throws IOException{
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setIncludePropertyNames("名字");
        beanCsvConfig.setIsPrintHeaderLine(false);

        csvWrite.write(path, toList(new User("zhangsan", "张三", 18)), beanCsvConfig);

        assertEquals("\"张三\"", readFile(path, UTF8));
    }

    @Test
    public void testWriteBeanNullValue() throws IOException{
        BeanCsvConfig<User> beanCsvConfig = new BeanCsvConfig<>(User.class);
        beanCsvConfig.setIncludePropertyNames("名字");

        csvWrite.write(path, toList(new User("zhangsan", null, 18)), beanCsvConfig);

        //null 输出成空字符串
        assertEquals("\"名字\"" + lineSeparator() + "\"\"", readFile(path, UTF8));
    }

    //---------------------------------------------------------------
    //参数校验
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testWriteArrayFileNameIsNull(){
        csvWrite.write(null, new String[] { "a" }, toLines(new Object[] { "1" }), new CsvConfig(UTF8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteArrayFileNameIsBlank(){
        csvWrite.write(" ", new String[] { "a" }, toLines(new Object[] { "1" }), new CsvConfig(UTF8));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteColumnTitlesAndDataListAllNull(){
        csvWrite.write(path, null, null, new CsvConfig(UTF8));
    }

    @Test(expected = NullPointerException.class)
    public void testWriteBeanFileNameIsNull(){
        csvWrite.write(null, toList(new User("zhangsan", "张三", 18)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteBeanFileNameIsBlank(){
        csvWrite.write(" ", toList(new User("zhangsan", "张三", 18)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWriteBeanIterableIsEmpty(){
        csvWrite.write(path, new ArrayList<User>());
    }

    @Test(expected = NullPointerException.class)
    public void testWriteBeanBeanClassIsNull(){
        csvWrite.write(path, toList(new User("zhangsan", "张三", 18)), new BeanCsvConfig<User>(null));
    }

    //---------------------------------------------------------------

    /**
     * 读文件内容.
     *
     * @param filePath
     *            the file path
     * @param charsetName
     *            the charset name
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static String readFile(String filePath,String charsetName) throws IOException{
        return new String(Files.readAllBytes(Paths.get(filePath)), Charset.forName(charsetName));
    }

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
