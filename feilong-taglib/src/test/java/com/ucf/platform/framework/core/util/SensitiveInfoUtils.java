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
package com.ucf.platform.framework.core.util;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class SensitiveInfoUtils.
 *
 * @author liuwentao@ucfgroup.com
 * @Title: SensitiveInfoUtils.java
 * @Copyright: Copyright (c) 2011
 * @Description: <br>
 *               敏感信息屏蔽工具<br>
 * @Company: ucfgroup.com
 * @Created on 2014-11-18 下午3:50:15
 */
public final class SensitiveInfoUtils{

    /**
     * [身份证号] 显示最后四位,其他隐藏。共计18位或者15位。<例子：*************5762>.
     *
     * @param id
     *            the id
     * @return the string
     */
    public static String idCardNum(String id){
        if (StringUtils.isBlank(id)){
            return "";
        }
        String num = StringUtils.right(id, 4);
        return StringUtils.leftPad(num, StringUtils.length(id), "*");
    }

    /**
     * [固定电话] 后四位,其他隐藏<例子：****1234>.
     *
     * @param num
     *            the num
     * @return the string
     */
    public static String fixedPhone(String num){
        if (StringUtils.isBlank(num)){
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, 4), StringUtils.length(num), "*");
    }

    /**
     * [银行卡号] 前六位,后四位,其他用星号隐藏每位1个星号<例子:6222600**********1234>.
     *
     * @param cardNum
     *            the card num
     * @return the string
     */
    public static String bankCard(String cardNum){
        if (StringUtils.isBlank(cardNum)){
            return "";
        }
        return StringUtils.left(cardNum, 6).concat(
                        StringUtils.removeStart(
                                        StringUtils.leftPad(StringUtils.right(cardNum, 4), StringUtils.length(cardNum), "*"),
                                        "******"));
    }

    /**
     * [公司开户银行联号] 公司开户银行联行号,显示前两位,其他用星号隐藏,每位1个星号<例子:12********>.
     *
     * @param code
     *            the code
     * @return the string
     */
    public static String cnapsCode(String code){
        if (StringUtils.isBlank(code)){
            return "";
        }
        return StringUtils.rightPad(StringUtils.left(code, 2), StringUtils.length(code), "*");
    }

    /**
     * [银行卡有效期] 前1位,后1位,其他隐藏<例子:“0**6”>.
     *
     * @param date
     *            the date
     * @return the string
     */
    public static String cardValidDate(String date){
        if (StringUtils.isBlank(date)){
            return "";
        }
        return StringUtils.left(date, 1).concat(
                        StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(date, 1), StringUtils.length(date), "*"), "*"));
    }

    /**
     * 全部隐藏.
     *
     * @param date
     *            the date
     * @return the string
     */
    public static String all(String date){
        if (StringUtils.isBlank(date)){
            return "";
        }
        return StringUtils.repeat("*", StringUtils.length(date));
    }

    /**
     * The Enum SensitiveType.
     */
    public static enum SensitiveType{

        /** 中文名. */
        CHINESE_NAME,

        /** 身份证号. */
        ID_CARD,

        /** 座机号. */
        FIXED_PHONE,

        /** 手机号. */
        MOBILE_PHONE,

        /** 银行卡. */
        BANK_CARD,

        /** 公司开户银行联号. */
        CNAPS_CODE,

        /** 银行卡有效期. */
        BANK_CARD_DATE,

        /** 全部隐藏. */
        ALL;
    }
}
