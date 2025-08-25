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
package com.feilong.context.codecreator;

import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

import com.feilong.core.lang.StringUtil;
import com.feilong.lib.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class CodeCreatorHelper.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeCreatorHelper{

    /**
     * Debug.
     *
     * @param isDebug
     *            the is debug
     * @return the string
     */
    public static String debugSeparator(boolean isDebug){
        return isDebug ? SPACE : EMPTY;
    }

    /**
     * 显示长度.
     *
     * @param sb
     *            the sb
     * @param isDebug
     *            the is debug
     * @return the string
     */
    public static String debugLength(StringBuilder sb,boolean isDebug){
        return isDebug ? " (" + (sb.length() - StringUtils.countMatches(sb.toString(), " ")) + ")" : "";
    }

    //---------------------------------------------------------------

    /**
     * 格式化值.
     * 
     * @param value
     *            the value
     * @param lastLength
     *            the last length
     * @return
     *         <ul>
     *         <li>如果 value 的长度{@code >} lastLength,那么截取后lastLength 位数</li>
     *         <li>如果 value 的长度{@code =} lastLength,那么直接返回value字符串格式</li>
     *         <li>如果 value 的长度{@code <} lastLength,那么数字前面补0返回</li>
     *         </ul>
     */
    public static String formatLastValue(Long value,int lastLength){
        String valueString = value.toString();
        int valueLength = valueString.length();

        if (valueLength > lastLength){
            return StringUtil.substringLast(valueString, lastLength);
        }
        if (valueLength == lastLength){
            return valueString;
        }
        //valueLength < lastLength
        return StringUtil.format("%0" + lastLength + "d", value);
    }

}
