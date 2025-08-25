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
package com.feilong.taglib.display.sensitive;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 敏感词工具类.
 * 
 * <h3>关于 type 优先级:</h3>
 * <blockquote>
 * <ol>
 * <li>先判断类型是否在config/sensitive 配置文件中,如果是那么使用</li>
 * <li>如果不是,那么使用 {@link SensitiveType}</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.1
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SensitiveUtil{

    /**
     * 转换.
     *
     * @param value
     *            the value
     * @param sensitiveConfig
     *            the sensitive config
     * @return 如果 <code>value</code> 是null,直接返回<code>value</code><br>
     *         如果 <code>value</code> 是blank,直接返回<code>value</code><br>
     *         如果 <code>sensitiveConfig</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>sensitiveConfig.getType()</code> 是null,抛出 {@link NullPointerException}<br>
     */
    public static String parse(String value,SensitiveConfig sensitiveConfig){
        if (isNullOrEmpty(value)){
            return value;
        }

        //---------------------------------------------------------------
        Validate.notNull(sensitiveConfig, "sensitiveConfig can't be null!");

        //---------------------------------------------------------------
        //since 1.14.0
        String type = sensitiveConfig.getType();
        Validate.notBlank(type, "type can't be blank!");

        char maskChar = sensitiveConfig.getMaskChar();

        //---------------------------------------------------------------
        //配置式
        Integer[] leftAndRightNoMaskLengths = SensitivePropertiesConfig.load(type);
        if (isNotNullOrEmpty(leftAndRightNoMaskLengths)){
            return parse(value, leftAndRightNoMaskLengths, maskChar);
        }

        //---------------------------------------------------------------
        //枚举式
        return parseWithEnum(value, sensitiveConfig, type, maskChar);
    }

    /**
     * 枚举式.
     *
     * @param value
     *            the value
     * @param sensitiveConfig
     *            the sensitive config
     * @param type
     *            the type
     * @param maskChar
     *            the mask char
     * @return the string
     * @since 1.14.0
     */
    private static String parseWithEnum(String value,SensitiveConfig sensitiveConfig,String type,char maskChar){
        SensitiveType sensitiveType = SensitiveType.toSensitiveType(type);
        Validate.notNull(sensitiveType, "sensitiveConfig.getSensitiveType() can't be null!");

        //---------------------------------------------------------------
        if (log.isTraceEnabled()){
            log.trace("value:[{}],sensitiveConfig:{}", value, JsonUtil.toString(sensitiveConfig));
        }

        //---------------------------------------------------------------
        if (sensitiveType.isNoNeedMask(value)){
            return value;
        }
        //---------------------------------------------------------------
        return parse(value, sensitiveType.getLeftAndRightNoMaskLengths(value), maskChar);
    }

    /**
     * Parses the.
     *
     * @param value
     *            the value
     * @param leftAndRightNoMaskLengths
     *            the left and right no mask lengths
     * @param maskChar
     *            the mask char
     * @return the string
     * @since 1.14.0
     */
    private static String parse(String value,Integer[] leftAndRightNoMaskLengths,char maskChar){
        //左边不需要mask的长度
        int leftNoMaskLength = leftAndRightNoMaskLengths[0];

        //右边不需要mask的长度
        int rightNoMaskLength = leftAndRightNoMaskLengths[1];
        //---------------------------------------------------------------
        return parse(value, maskChar, leftNoMaskLength, rightNoMaskLength);
    }

    //---------------------------------------------------------------

    /**
     * Parses the.
     *
     * @param value
     *            the value
     * @param maskChar
     *            the mask char
     * @param leftNoMaskLength
     *            the left no mask length
     * @param rightNoMaskLength
     *            the right no mask length
     * @return the string
     * @since 1.14.0
     */
    public static String parse(String value,char maskChar,int leftNoMaskLength,int rightNoMaskLength){
        if (log.isTraceEnabled()){
            String format = "will parse:[{}],maskChar:[{}],leftNoMaskLength:[{}],rightNoMaskLength:[{}]";
            log.trace(format, value, maskChar, leftNoMaskLength, rightNoMaskLength);
        }

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();

        sb.append(StringUtils.left(value, leftNoMaskLength));
        sb.append(StringUtils.repeat(maskChar, value.length() - leftNoMaskLength - rightNoMaskLength));
        sb.append(StringUtils.right(value, rightNoMaskLength));

        if (log.isDebugEnabled()){
            String format = "parse:[{}],maskChar:[{}],leftNoMaskLength:[{}],rightNoMaskLength:[{}],result:[{}]";
            log.debug(format, value, maskChar, leftNoMaskLength, rightNoMaskLength, sb.toString());
        }

        return sb.toString();
    }
}
