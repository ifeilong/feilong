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

import static com.feilong.core.bean.ConvertUtil.toArray;

import org.apache.commons.lang3.StringUtils;

import com.feilong.core.lang.EnumUtil;

/**
 * 敏感字类型.
 * 
 * <p>
 * 0513-8677****
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.1
 */
public enum SensitiveType{

    /** 手机号码. */
    MOBILE("mobile"){

        @Override
        protected Integer[] getLeftAndRightNoMaskLengths(String value){
            //前三位,后四位,其他隐藏<例子:138******1234>
            return toArray(3, 4);
        }
    },

    /** 中文姓名. */
    CHINESENAME("CHINESENAME"){

        @Override
        protected Integer[] getLeftAndRightNoMaskLengths(String value){
            return toArray(1, 0);
        }

        @Override
        protected boolean isNoNeedMask(String value){
            return value.length() == 1;
        }
    },

    /** 家庭地址. */
    //家庭地址 只显示到地区,不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
    ADDRESS("ADDRESS"){

        @Override
        protected Integer[] getLeftAndRightNoMaskLengths(String value){
            return toArray(6, 0);
        }

        @Override
        protected boolean isNoNeedMask(String value){
            return value.length() <= 6;
        }
    },

    /** 邮箱. */
    //邮箱 邮箱前缀仅显示第一个字母,前缀其他隐藏,用星号代替,@及后面的地址显示<例子:g**@163.com>
    EMAIL("EMAIL"){

        //ve*****on@163.com
        @Override
        protected Integer[] getLeftAndRightNoMaskLengths(String value){
            int length = value.length();
            int index = StringUtils.indexOf(value, '@');

            //vv@  两个字符
            if (2 <= index && index <= 4){
                return toArray(1, length - index);
            }

            //vvVVv@163.com
            //vvVVvvv@163.com
            if (5 <= index && index <= 6){
                return toArray(1, length - index + 1);
            }

            return toArray(2, length - index + 2);
        }

        @Override
        protected boolean isNoNeedMask(String value){
            //如果找不到 @ 或者@前面没有字符,那么直接输出 value
            int index = StringUtils.indexOf(value, '@');
            return index == -1 || index == 0 || index == 1;
        }
    };

    //---------------------------------------------------------------

    /** The type. */
    private String type;

    /**
     * Instantiates a new sensitive type.
     *
     * @param type
     *            the type
     */
    private SensitiveType(String type){
        this.type = type;
    }

    //---------------------------------------------------------------
    /**
     * 获得左侧以及右侧不需要mask 字符长度.
     *
     * @param value
     *            the value
     * @return the left and right no mask lengths
     */
    protected abstract Integer[] getLeftAndRightNoMaskLengths(String value);

    /**
     * 判断的条件,判断是否不进行mask.
     *
     * @param value
     *            the value
     * @return 不mask 返回 true ; 否则返回 false
     */
    @SuppressWarnings("static-method")
    protected boolean isNoNeedMask(@SuppressWarnings("unused") String value){
        return false;
    }

    //---------------------------------------------------------------

    /**
     * Gets the sensitive type.
     *
     * @param type
     *            the type
     * @return the sensitive type
     */
    public static SensitiveType toSensitiveType(String type){
        return EnumUtil.getEnumByPropertyValueIgnoreCase(SensitiveType.class, "type", type);
    }

    /**
     * 获得 type.
     *
     * @return the type
     */
    public String getType(){
        return type;
    }

}
