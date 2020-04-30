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

import static com.feilong.core.CharsetType.UTF8;

/**
 * CSV参数.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 */
public class CsvConfig{

    /** 编码. */
    private String  encode            = UTF8;

    /** field 分隔符 默认,逗号. */
    private char    separator         = ',';

    /**
     * 是否输出 header line,如果是false,那么csv文件不会输出header line,默认是true.
     *
     * @since 1.7.1
     */
    private boolean isPrintHeaderLine = true;

    //---------------------------------------------------------------

    /**
     * The Constructor.
     */
    public CsvConfig(){
        //default Constructor
    }

    /**
     * The Constructor.
     *
     * @param encode
     *            the encode
     */
    public CsvConfig(String encode){
        this.encode = encode;
    }

    /**
     * The Constructor.
     *
     * @param encode
     *            the encode
     * @param separator
     *            the separator
     */
    public CsvConfig(String encode, char separator){
        this.separator = separator;
        this.encode = encode;
    }

    /**
     * Gets the 编码.
     * 
     * @return the encode
     */
    public String getEncode(){
        return encode;
    }

    /**
     * Sets the 编码.
     * 
     * @param encode
     *            the encode to set
     */
    public void setEncode(String encode){
        this.encode = encode;
    }

    /**
     * Gets the field 分隔符 默认,逗号.
     * 
     * @return the separator
     */
    public char getSeparator(){
        return separator;
    }

    /**
     * Sets the field 分隔符 默认,逗号.
     * 
     * @param separator
     *            the separator to set
     */
    public void setSeparator(char separator){
        this.separator = separator;
    }

    /**
     * 获得 是否输出 header line,如果是false,那么csv文件不会输出header line,默认是true.
     *
     * @return the isPrintHeaderLine
     */
    public boolean getIsPrintHeaderLine(){
        return isPrintHeaderLine;
    }

    /**
     * 设置 是否输出 header line,如果是false,那么csv文件不会输出header line,默认是true.
     *
     * @param isPrintHeaderLine
     *            the isPrintHeaderLine to set
     */
    public void setIsPrintHeaderLine(boolean isPrintHeaderLine){
        this.isPrintHeaderLine = isPrintHeaderLine;
    }

}