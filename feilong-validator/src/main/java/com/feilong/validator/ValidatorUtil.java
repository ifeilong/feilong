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
package com.feilong.validator;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.validator.RegexPattern.MOBILEPHONE;
import static com.feilong.validator.RegexPattern.TELEPHONE;
import static com.feilong.validator.RegexPattern.TELEPHONE_MUST_AREACODE;
import static com.feilong.validator.RegexPattern.ZIPCODE;

import com.feilong.core.util.RegexUtil;
import com.feilong.core.Validate;
import com.feilong.lib.validator.EmailValidator;

/**
 * 校验的工具类.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.6
 */
public final class ValidatorUtil{

    /** Don't let anyone instantiate this class. */
    private ValidatorUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 判断字符串是否是大陆的电话号码.
     *
     * @param telephone
     *            the telephone
     * @return 如果 <code>telephone</code> 是null或者empty,返回 false<br>
     */
    public static boolean isTelephone(String telephone){
        return isMatches(TELEPHONE, telephone);
    }

    /**
     * 判断字符串是否是大陆的电话号码(必须要有区号).
     *
     * @param telephoneMustAreacode
     *            the telephone must areacode
     * @return 如果 <code>telephoneMustAreacode</code> 是null或者empty,返回 false<br>
     */
    public static boolean isTelephoneMustAreacode(String telephoneMustAreacode){
        return isMatches(TELEPHONE_MUST_AREACODE, telephoneMustAreacode);
    }

    //---------------------------------------------------------------

    /**
     * 判断字符串是否是邮编.
     *
     * @param zipcode
     *            邮政编码
     * @return 如果 <code>zipcode</code> 是null或者empty,返回 false<br>
     */
    public static boolean isZipcode(String zipcode){
        return isMatches(ZIPCODE, zipcode);
    }

    /**
     * 判断字符串是否是手机号码.
     *
     * @param mobile
     *            the mobile
     * @return 如果 <code>mobile</code> 是null或者empty,返回 false<br>
     */
    public static boolean isMobile(String mobile){
        return isMatches(MOBILEPHONE, mobile);
    }

    //---------------------------------------------------------------

    /**
     * 判断给定的email 字符串是不是邮箱.
     *
     * @param email
     *            the email
     * @return 如果 <code>email</code> 是null或者empty,返回 false<br>
     *         如果 <code>email</code> 以. 结尾,返回 false<br>
     *         如果 <code>email</code> 不匹配 {@link EmailValidator#EMAIL_PATTERN} ,返回 false<br>
     *         <code>email</code> 提取 user 部分值,如果不是有效的user,{@link EmailValidator#isValidUser(String)},返回 false<br>
     *         <code>email</code> 提取 domain 部分值,如果不是有效的domain {@link EmailValidator#isValidDomain(String)},返回 false<br>
     *         其余返回true<br>
     * 
     * @see com.feilong.lib.validator.EmailValidator
     * @see <a href="http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/">how-to-validate-email
     *      -address-with-regular-expression</a>
     * @see <a href="https://en.wikipedia.org/wiki/Email_address">Email_address</a>
     * 
     */
    public static boolean isEmail(String email){
        if (isNullOrEmpty(email)){
            return false;
        }

        EmailValidator emailValidator = com.feilong.lib.validator.EmailValidator.getInstance();
        return emailValidator.isValid(email);
    }

    //---------------------------------------------------------------

    /**
     * 编译给定正则表达式 regexPattern ,并尝试将给定输入 input 与其匹配.
     * 
     * @param pattern
     *            正则表达式
     * @param input
     *            输入的文本
     * @return 如果 <code>pattern</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>pattern</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>input</code> 是null 或者 是blank,返回 false<br>
     * @since 1.13.2 if input is blank not redirect return false
     * @see <a href="https://github.com/venusdrogon/feilong-platform/issues/289">#289</a>
     */
    public static boolean isMatches(String pattern,String input){
        Validate.notBlank(pattern, "pattern can't be blank!");
        return RegexUtil.matches(pattern, input);
    }
}
