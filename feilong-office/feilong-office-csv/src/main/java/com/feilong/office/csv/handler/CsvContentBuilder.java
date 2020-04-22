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
package com.feilong.office.csv.handler;

import static java.lang.System.lineSeparator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.office.csv.entity.CsvConfig;

/**
 * 构造 csv 输出内容.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.1
 * @since 1.10.7 rename
 */
public class CsvContentBuilder{

    /** The Constant log. */
    private static final Logger LOGGER                  = LoggerFactory.getLogger(CsvContentBuilder.class);

    //---------------------------------------------------------------

    /** 转义引号用的字符 ". */
    private static final char   ESCAPE_CHARACTER        = '"';

    /** 默认的引号字符 "引号. */
    private static final char   DEFAULT_QUOTE_CHARACTER = '"';

    /**
     * \\u转义字符的意思是"\\u后面的1-4位16进制数表示的Unicode码对应的汉字",而Unicode 0000 代表的字符是 NUL,也就是空的意思,<br>
     * 如果把这个字符输出到控制台,显示为空格.
     */
    private static final char   NO_QUOTE_CHARACTER      = '\u0000';

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private CsvContentBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Writes the entire list to a CSV file. The list is assumed to be a String[]
     * 
     * @param allLines
     *            a List of String[], with each String[] representing a line of the file.
     * @param csvConfig
     *            the csv params
     * @return the write content
     */
    public static final String build(List<Object[]> allLines,CsvConfig csvConfig){
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = allLines.size(); i < j; ++i){
            sb.append(buildLine(allLines.get(i), csvConfig));
            if (i != j - 1){
                sb.append(lineSeparator());
            }
        }

        //---------------------------------------------------------------

        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("{}{}", lineSeparator(), sb);
        }
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * Writes the next line to the file.
     * 
     * @param lineColumns
     *            the line
     * @param csvConfig
     *            the csv params
     * @return the write content line
     * @see com.feilong.core.bean.ConvertUtil#toString(Object)
     * @see org.apache.commons.lang3.StringEscapeUtils#escapeCsv(String)
     */
    private static final StringBuilder buildLine(Object[] lineColumns,CsvConfig csvConfig){
        StringBuilder sb = new StringBuilder();
        for (int i = 0, lineLength = lineColumns.length; i < lineLength; ++i){
            // 分隔符,列为空也要表达其存在.
            if (i != 0){
                sb.append(csvConfig.getSeparator());
            }

            //---------------------------------------------------------------
            if (null != lineColumns[i]){
                appendColumnValue(sb, ConvertUtil.toString(lineColumns[i]), DEFAULT_QUOTE_CHARACTER);
            }
        }
        return sb;
    }

    /**
     * Append.
     *
     * @param sb
     *            the sb
     * @param columnValue
     *            the column value
     * @param quotechar
     *            the quotechar
     * @since 1.7.1
     */
    private static void appendColumnValue(StringBuilder sb,String columnValue,char quotechar){
        if (quotechar != NO_QUOTE_CHARACTER){
            sb.append(quotechar);
        }
        //---------------------------------------------------------------

        appendByChar(sb, columnValue, quotechar);

        //---------------------------------------------------------------
        if (quotechar != NO_QUOTE_CHARACTER){
            sb.append(quotechar);
        }
    }

    //---------------------------------------------------------------

    /**
     * Append by char.
     *
     * @param sb
     *            the sb
     * @param columnValue
     *            the column value
     * @param quotechar
     *            the quotechar
     * @since 1.7.1
     */
    private static void appendByChar(StringBuilder sb,String columnValue,char quotechar){
        for (int j = 0, length = columnValue.length(); j < length; ++j){
            char currentChar = columnValue.charAt(j);
            if (currentChar == quotechar || currentChar == ESCAPE_CHARACTER){
                sb.append(ESCAPE_CHARACTER);
            }
            sb.append(currentChar);
        }
    }
}
