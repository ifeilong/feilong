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
package com.feilong.io;

import java.io.Serializable;

/**
 * 读取文件的配置参数.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.10
 */
public class ReaderConfig implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long        serialVersionUID = -6044017657112874891L;

    /**
     * 默认的读取配置,忽略空白行,且去空格.
     * 
     * @since 1.14.0
     */
    // the static instance works for all types
    public static final ReaderConfig DEFAULT          = new ReaderConfig();

    //---------------------------------------------------------------

    /** 是否忽略空白行. */
    private boolean                  ignoreBlankLine  = true;

    /** 是否去空格. */
    private boolean                  isTrim           = true;

    //---------------------------------------------------------------

    /** 符合正则的内容才抓取,不符合的跳过. */
    private String                   regexPattern;

    //---------------------------------------------------------------
    /**
     * Instantiates a new reader config.
     */
    public ReaderConfig(){
        super();
    }

    /**
     * Instantiates a new reader config.
     *
     * @param regexPattern
     *            符合正则的内容才抓取,不符合的跳过.
     */
    public ReaderConfig(String regexPattern){
        super();
        this.regexPattern = regexPattern;
    }

    /**
     * Instantiates a new reader config.
     *
     * @param ignoreBlankLine
     *            是否忽略空白行
     * @param isTrim
     *            是否去空格
     */
    public ReaderConfig(boolean ignoreBlankLine, boolean isTrim){
        super();
        this.ignoreBlankLine = ignoreBlankLine;
        this.isTrim = isTrim;
    }

    /**
     * Instantiates a new reader config.
     *
     * @param ignoreBlankLine
     *            是否忽略空白行
     * @param isTrim
     *            是否去空格
     * @param regexPattern
     *            符合正则的内容才抓取,不符合的跳过.
     */
    public ReaderConfig(boolean ignoreBlankLine, boolean isTrim, String regexPattern){
        super();
        this.ignoreBlankLine = ignoreBlankLine;
        this.isTrim = isTrim;
        this.regexPattern = regexPattern;
    }

    //---------------------------------------------------------------

    /**
     * 获得 是否忽略空白行.
     *
     * @return the ignoreBlankLine
     */
    public boolean getIgnoreBlankLine(){
        return ignoreBlankLine;
    }

    /**
     * 设置 是否忽略空白行.
     *
     * @param ignoreBlankLine
     *            the ignoreBlankLine to set
     */
    public void setIgnoreBlankLine(boolean ignoreBlankLine){
        this.ignoreBlankLine = ignoreBlankLine;
    }

    /**
     * 获得 是否去空格.
     *
     * @return the isTrim
     */
    public boolean getIsTrim(){
        return isTrim;
    }

    /**
     * 设置 是否去空格.
     *
     * @param isTrim
     *            the isTrim to set
     */
    public void setIsTrim(boolean isTrim){
        this.isTrim = isTrim;
    }

    /**
     * 获得 符合正则的内容才抓取,不符合的跳过.
     *
     * @return the regexPattern
     */
    public String getRegexPattern(){
        return regexPattern;
    }

    /**
     * 设置 符合正则的内容才抓取,不符合的跳过.
     *
     * @param regexPattern
     *            the regexPattern to set
     */
    public void setRegexPattern(String regexPattern){
        this.regexPattern = regexPattern;
    }

}
