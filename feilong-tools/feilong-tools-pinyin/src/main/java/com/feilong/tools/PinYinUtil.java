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
package com.feilong.tools;

import static com.feilong.core.Validator.isNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * pinyin4j 工具类.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class PinYinUtil{

    /** The Constant LOGGER. */
    private static final Logger                  LOGGER                             = LoggerFactory.getLogger(PinYinUtil.class);

    //---------------------------------------------------------------

    /**
     * The default hanyu pinyin output format.
     * 
     * @since 1.13.0
     */
    private static final HanyuPinyinOutputFormat DEFAULT_HANYU_PINYIN_OUTPUT_FORMAT = buildDefaultHanyuPinyinOutputFormat();

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private PinYinUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将中文转成拼音.
     * 
     * @param str
     *            the chinese
     * @return 如果 <code>str</code> 是null,返回 <code>str</code><br>
     *         如果 <code>str</code> 是blank,返回 <code>str</code><br>
     */
    public static String toPinYin(String str){
        if (isNullOrEmpty(str)){
            return str;
        }
        return toPinYin(str, DEFAULT_HANYU_PINYIN_OUTPUT_FORMAT);
    }

    //---------------------------------------------------------------

    /**
     * To pin yin.
     *
     * @param str
     *            the str
     * @param hanyuPinyinOutputFormat
     *            the hanyu pinyin output format
     * @return 如果 <code>str</code> 是null,返回 <code>str</code><br>
     *         如果 <code>str</code> 是blank,返回 <code>str</code><br>
     * @since 1.13.0
     */
    public static String toPinYin(String str,HanyuPinyinOutputFormat hanyuPinyinOutputFormat){
        if (isNullOrEmpty(str)){
            return str;
        }

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();
        char[] charArray = str.toCharArray();

        //---------------------------------------------------------------
        try{
            for (int i = 0; i < charArray.length; i++){
                char cha = charArray[i];
                String[] array = PinyinHelper.toHanyuPinyinStringArray(cha, hanyuPinyinOutputFormat);
                sb.append(null == array ? //
                                cha : //  不是汉字的情况
                                array[0] // 这里一般数组长度为1,大于1是因为汉字可能会有多音字
                );
            }
        }catch (Exception e){
            LOGGER.error(e.getClass().getName(), e);
        }

        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * Builds the hanyu pinyin output format.
     *
     * @return the hanyu pinyin output format
     */
    private static HanyuPinyinOutputFormat buildDefaultHanyuPinyinOutputFormat(){
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        // 转化为小写
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带声调
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // ü用v带替
        hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        return hanyuPinyinOutputFormat;
    }
}
