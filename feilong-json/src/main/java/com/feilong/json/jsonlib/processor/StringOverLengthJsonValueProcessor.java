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
package com.feilong.json.jsonlib.processor;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.Validate;

import com.feilong.core.lang.StringUtil;

import net.sf.json.JsonConfig;

/**
 * 如果字符串格式jsonvalue的值超过指定的长度 <code>maxLength</code>,将省略显示 <code>overLengthMaskString</code>,以控制输出的字符串长度.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.7
 */
public class StringOverLengthJsonValueProcessor extends AbstractJsonValueProcessor{

    /** <code>{@value}</code>. */
    private static final String DEFAULT_OVERLENGTH_MASKSTRING = "......";

    /** 最大长度,默认 500. */
    private int                 maxLength                     = 500;

    /** 超长截取拼接的字符串. */
    private String              overLengthMaskString          = DEFAULT_OVERLENGTH_MASKSTRING;

    //---------------------------------------------------------------

    /**
     * Instantiates a new string over length json value processor.
     */
    public StringOverLengthJsonValueProcessor(){
    }

    /**
     * Instantiates a new string over length json value processor.
     *
     * @param maxLength
     *            the max length
     */
    public StringOverLengthJsonValueProcessor(int maxLength){
        Validate.isTrue(maxLength > 0, "maxLength:[%s] must > 0", maxLength);
        this.maxLength = maxLength;
    }

    /**
     * Instantiates a new string over length json value processor.
     *
     * @param maxLength
     *            最大长度,默认 500
     * @param overLengthMaskString
     *            超长截取拼接的字符串,默认是 ..... ,你也可以自定义
     */
    public StringOverLengthJsonValueProcessor(int maxLength, String overLengthMaskString){
        Validate.isTrue(maxLength > 0, "maxLength:[%s] must > 0", maxLength);
        this.maxLength = maxLength;
        this.overLengthMaskString = overLengthMaskString;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.json.jsonlib.processor.AbstractJsonValueProcessor#processValue(java.lang.Object, net.sf.json.JsonConfig)
     */
    @Override
    protected Object processValue(Object value,JsonConfig jsonConfig){
        return format(value, maxLength, overLengthMaskString);
    }

    //---------------------------------------------------------------

    /**
     * Format.
     *
     * @param value
     *            the value
     * @param maxLength
     *            the max length
     * @return 如果 <code>value</code> 是null,返回 null<br>
     *         如果 maxLength{@code <}0 是null,抛出 {@link NullPointerException}<br>
     *         如果 maxLength{@code <}0 是empty,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>value</code> 是null,使用默认值 {@link #DEFAULT_OVERLENGTH_MASKSTRING} <br>
     */
    public static Object format(Object value,int maxLength){
        return format(value, maxLength, DEFAULT_OVERLENGTH_MASKSTRING);
    }

    /**
     * Format.
     *
     * @param value
     *            the value
     * @param maxLength
     *            the max length
     * @param overLengthMaskString
     *            the over length mask string
     * @return 如果 <code>value</code> 是null,返回 null<br>
     *         如果 maxLength{@code <}0 是null,抛出 {@link NullPointerException}<br>
     *         如果 maxLength{@code <}0 是empty,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>value</code> 是null,使用默认值 {@link #DEFAULT_OVERLENGTH_MASKSTRING} <br>
     */
    public static Object format(Object value,int maxLength,String overLengthMaskString){
        if (null == value){
            return EMPTY;
        }

        Validate.isTrue(maxLength > 0, "maxLength:[%s] must > 0", maxLength);

        String useOverLengthMaskString = defaultIfNull(overLengthMaskString, DEFAULT_OVERLENGTH_MASKSTRING);

        //---------------------------------------------------------------

        String string = value.toString();

        if (string.length() <= maxLength){
            return string;
        }

        //---------------------------------------------------------------
        return StringUtil.substring(string, 0, maxLength) + useOverLengthMaskString;
    }

    //---------------------------------------------------------------

    /**
     * 获得 最大长度,默认 500.
     *
     * @return the maxLength
     */
    public int getMaxLength(){
        return maxLength;
    }

    /**
     * 设置 最大长度,默认 500.
     *
     * @param maxLength
     *            the maxLength to set
     */
    public void setMaxLength(int maxLength){
        this.maxLength = maxLength;
    }

    /**
     * 获得 超长截取拼接的字符串.
     *
     * @return the overLengthMaskString
     */
    public String getOverLengthMaskString(){
        return overLengthMaskString;
    }

    /**
     * 设置 超长截取拼接的字符串.
     *
     * @param overLengthMaskString
     *            the overLengthMaskString to set
     */
    public void setOverLengthMaskString(String overLengthMaskString){
        this.overLengthMaskString = overLengthMaskString;
    }

}
