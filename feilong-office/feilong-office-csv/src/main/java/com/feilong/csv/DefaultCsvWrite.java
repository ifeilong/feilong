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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.util.CollectionsUtil.first;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.core.bean.ConvertUtil;
import com.feilong.csv.entity.BeanCsvConfig;
import com.feilong.csv.entity.CsvColumnEntity;
import com.feilong.csv.entity.CsvConfig;
import com.feilong.csv.handler.CsvColumnEntityListBuilder;
import com.feilong.csv.handler.CsvContentBuilder;
import com.feilong.csv.handler.DataListBuilder;
import com.feilong.io.IOWriteUtil;

/**
 * cvs工具类.
 * 
 * <h3>逗号分隔值(Comma-Separated Values,CSV)</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * (有时也称为字符分隔值,因为分隔字符也可以不是逗号),其文件以纯文本形式存储表格数据(数字和文本)。<br>
 * 纯文本意味着该文件是一个字符序列,不含必须象二进制数字那样被解读的数据。
 * </p>
 * 
 * <p>
 * CSV文件由任意数目的记录组成,记录间以某种换行符分隔;每条记录由字段组成,字段间的分隔符是其它字符或字符串,最常见的是逗号或制表符。<br>
 * 通常,所有记录都有完全相同的字段序列。
 * 
 * CSV文件格式的通用标准并不存在,但是在RFC 4180中有基础性的描述。使用的字符编码同样没有被指定,但是7-bit ASCII是最基本的通用编码。
 * </p>
 * </blockquote>
 * 
 * <h3>基本规则及举例:</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * 存在许多描述“CSV”格式的非正式文件。IETF RFC 4180(如上所述)定义了格式“text/csv”互联网媒体类型,并已注册于IANA。<br>
 * (Shafranovich 2005)另一个相关规范由含字段文本提供。Creativyst (2010)提供了一个概述,说明了在最广泛使用的应用程序中所使用的变体,并解释了CSV怎样才能最好地被使用和支持。
 * </p>
 * 
 * 这些和其它“CSV”规范及实现的典型规则如下:
 * 
 * <ul>
 * <li>CSV是一种被分隔的数据格式,它有被逗号字符分隔的字段/列和以换行结束的记录/行。</li>
 * <li>CSV文件不要求特定的字符编码、字节序或行结束符格式(某些软件不支持所有行结束变体)。</li>
 * <li>一条记录结束于行结束符。然而,行结束符可能被作为数据嵌入到字段中,所以软件必须识别被包裹的行结束符(见下述),以便从可能的多行中正确组装一条完整的记录。</li>
 * <li>所有记录应当有相同数目、相同顺序的字段。</li>
 * <li>字段中的数据被翻译为一系列字符,而不是一系列比特或字节(见RFC 2046,section 4.1)。<br>
 * 例如,数值量65535可以被表现为5个ASCII字符“65535”(或其它形式如“0xFFFF”、“000065535.000E+00”等等);但不会被作为单个二进制整数的2字节序列(而非两个字符)来处理。<br>
 * 如果不遵循这个“纯文本”的惯例, 那么该CSV文件就不能包含足够的信息来正确地翻译它,该CSV文件将不大可能在不同的电脑架构间正确传递,并且将不能与text/csv MIME类型保持一致。</li>
 * <li>相邻字段必须被单个逗号分隔开。然而,“CSV”格式在分隔字符的选择上变化很大。特别是在某些区域设置中逗号被用作小数点,则会使用分号、制表符或其它字符来代替。
 * 
 * <pre class="code">
 * 1997,Ford,E350
 * </pre>
 * 
 * </li>
 * <li>任何字段都可以被包裹(使用双引号字符)。某些字段必须被包裹,详见后续规则。
 * 
 * <pre class="code">
 * "1997","Ford","E350"
 * </pre>
 * 
 * </li>
 * <li>如果字段包含被嵌入的逗号,必须被包裹。
 * 
 * <pre class="code">
 * 1997,Ford,E350,"Super, luxurious truck"
 * </pre>
 * 
 * </li>
 * <li>每个被嵌入的双引号字符必须被表示为两个双引号字符。
 * 
 * <pre class="code">
 * 1997,Ford,E350,"Super, ""luxurious"" truck"
 * </pre>
 * 
 * </li>
 * <li>如果字段包含被嵌入的换行,必须被包裹(然而,许多CSV实现简单地不支持字段内换行)。
 * 
 * <pre class="code">
 * 1997,Ford,E350,"Go get one now
 * they are going fast"
 * </pre>
 * 
 * </li>
 * <li>在某些CSV实现中,起头和结尾的空格和制表符被截掉。这个实践是有争议的,也不符合RFC 4180。RFC 4180声明“空格被看作字段的一部分,不应当被忽略。”。
 * 
 * <pre class="code">
 * 1997, Ford, E350
 * not same as
 * 1997,Ford,E350
 * </pre>
 * 
 * </li>
 * <li>然而,该RFC并没有说当空白字符出现在被包裹的值之外该如何处理。
 * 
 * <pre class="code">
 * 1997, "Ford" ,E350
 * </pre>
 * 
 * </li>
 * <li>在截掉起头和结尾空格的CSV实现中,将这种空格视为有意义数据的字段必须被包裹。 *
 * 
 * <pre class="code">
 * 1997,Ford,E350," Super luxurious truck "
 * </pre>
 * 
 * </li>
 * <li>第一条记录可以是“表头”,它在每个字段中包含列名(并没有可靠途径来告知一个文件是否这样做;然而,在这样的列名中使用字母、数字和下划线以外的字符是罕见的)。
 * 
 * <pre class="code">
 * Year,Make,Model
 * 1997,Ford,E350
 * 2000,Mercury,Cougar
 * </pre>
 * 
 * </li>
 * </ul>
 * 
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://zh.wikipedia.org/wiki/%E9%80%97%E5%8F%B7%E5%88%86%E9%9A%94%E5%80%BC">逗号分隔值</a>
 * @since 1.0.0
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
public class DefaultCsvWrite implements CsvWrite{

    /** The Constant log. */
    private static final Logger    LOGGER             = LoggerFactory.getLogger(DefaultCsvWrite.class);

    /** The Constant DEFAULT_CSV_CONFIG. */
    private static final CsvConfig DEFAULT_CSV_CONFIG = new CsvConfig();

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.office.csv.CsvWrite#write(java.lang.String, java.lang.Iterable)
     */
    @Override
    public <T> void write(String fileName,Iterable<T> iterable){
        Validate.notBlank(fileName, "fileName can't be null/empty!");
        Validate.isTrue(isNotNullOrEmpty(iterable), "iterable can't be null/empty!");
        //---------------------------------------------------------------
        write(fileName, iterable, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.office.csv.CsvWrite#write(java.lang.String, java.lang.Iterable,
     * com.feilong.tools.office.csv.entity.BeanCsvConfig)
     */
    @Override
    public <T> void write(String fileName,Iterable<T> iterable,BeanCsvConfig<T> beanCsvConfig){
        Validate.notBlank(fileName, "fileName can't be null/empty!");
        Validate.isTrue(isNotNullOrEmpty(iterable), "iterable can't be null/empty!");

        //---------------------------------------------------------------
        BeanCsvConfig<T> useBeanCsvConfig = defaultIfNull(beanCsvConfig, buildBeanCsvConfig(iterable));
        Validate.notNull(useBeanCsvConfig.getBeanClass(), "beanCsvConfig.getBeanClass() can't be null!");

        List<CsvColumnEntity> csvColumnEntityList = CsvColumnEntityListBuilder.build(useBeanCsvConfig);

        //---------------------------------------------------------------
        String[] columnTitles = ConvertUtil.toStrings(getPropertyValueList(csvColumnEntityList, "name"));
        List<Object[]> dataList = DataListBuilder.build(iterable, csvColumnEntityList);
        write(fileName, columnTitles, dataList, useBeanCsvConfig);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.office.csv.CsvWrite#write(java.lang.String, java.lang.String[], java.util.List)
     */
    @Override
    public void write(String fileName,String[] columnTitles,List<Object[]> dataList){
        Validate.notBlank(fileName, "fileName can't be null/empty!");
        write(fileName, columnTitles, dataList, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.office.csv.CsvWrite#write(java.lang.String, java.lang.String[], java.util.List,
     * com.feilong.tools.office.csv.entity.CsvConfig)
     */
    @Override
    public void write(String fileName,String[] columnTitles,List<Object[]> dataList,CsvConfig csvConfig){
        Validate.notBlank(fileName, "fileName can't be null/empty!");

        // 标题和内容都是空,没有任何意义,不创建文件
        Validate.isTrue(isNotNullOrEmpty(columnTitles) || isNotNullOrEmpty(dataList), "columnTitles and dataList can't all null!");

        //---------------------------------------------------------------
        CsvConfig useCsvConfig = defaultIfNull(csvConfig, DEFAULT_CSV_CONFIG);

        List<Object[]> allLines = defaultIfNull(dataList, new ArrayList<Object[]>());
        if (isNotNullOrEmpty(columnTitles) && useCsvConfig.getIsPrintHeaderLine()){
            allLines.add(0, columnTitles);
        }

        //---------------------------------------------------------------
        LOGGER.info("begin write file:[{}]", fileName);

        String content = CsvContentBuilder.build(allLines, useCsvConfig);
        IOWriteUtil.writeStringToFile(fileName, content, useCsvConfig.getEncode());
    }

    //---------------------------------------------------------------

    /**
     * Builds the bean csv config.
     *
     * @param <T>
     *            the generic type
     * @param iterable
     *            the iterable
     * @return the bean csv config
     * @since 1.8.1
     */
    private static <T> BeanCsvConfig<T> buildBeanCsvConfig(Iterable<T> iterable){
        T t = first(iterable);
        @SuppressWarnings("unchecked")
        Class<T> klass = (Class<T>) t.getClass();
        return new BeanCsvConfig<>(klass);
    }

}
