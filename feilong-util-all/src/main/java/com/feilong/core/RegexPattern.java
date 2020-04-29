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
package com.feilong.core;

import com.feilong.validator.ValidatorUtil;

/**
 * 常用的正则表达式模式.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.0
 * @deprecated pls use {@link com.feilong.validator.RegexPattern}
 */
@Deprecated
public final class RegexPattern{

    /**
     * 大陆的电话号码 <code>{@value}</code>.
     * 
     * @deprecated pls use {@link ValidatorUtil#isTelephone(String)}
     */
    @Deprecated
    public static final String TELEPHONE               = com.feilong.validator.RegexPattern.TELEPHONE;

    /**
     * 大陆的电话号码(必须要有区号) <code>{@value}</code>.
     * 
     * @deprecated pls use {@link ValidatorUtil#isTelephoneMustAreacode(String)}
     */
    @Deprecated
    public static final String TELEPHONE_MUST_AREACODE = com.feilong.validator.RegexPattern.TELEPHONE_MUST_AREACODE;

    //---------------------------------------------------------------

    /**
     * 手机号码 <code>{@value}</code>.
     * 
     * @deprecated 请直接调用 {@link com.feilong.validator.ValidatorUtil#isMobile(String)}<br>
     */
    @Deprecated
    public static final String MOBILEPHONE             = com.feilong.validator.RegexPattern.MOBILEPHONE;
    //---------------------------------------------------------------

    /**
     * 邮政编码 <code>{@value}</code>.
     * 
     * @deprecated pls use {@link ValidatorUtil#isZipcode(String)}
     */
    @Deprecated
    public static final String ZIPCODE                 = com.feilong.validator.RegexPattern.ZIPCODE;

    //---------------------------------------------------------------
    /**
     * 两位数小数 <code>{@value}</code>
     * 
     * <p>
     * 可以是200 也可以是200.00 不可以是 200.0
     * </p>
     */
    public static final String DECIMAL_TWO_DIGIT       = com.feilong.validator.RegexPattern.DECIMAL_TWO_DIGIT;

    //---------------------------------------------------------------
    /**
     * email 的正则表达式 <code>{@value}</code>.
     * 
     * @deprecated 请直接调用 {@link com.feilong.validator.ValidatorUtil#isEmail(String)},<br>
     *             验证更加完善,分别会对user 和domain再次校验
     */
    @Deprecated
    public static final String EMAIL                   = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    //"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$",

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private RegexPattern(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
}