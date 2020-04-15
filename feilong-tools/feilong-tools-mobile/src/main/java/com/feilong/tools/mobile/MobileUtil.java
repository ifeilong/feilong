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
package com.feilong.tools.mobile;

import org.apache.commons.lang3.Validate;

import com.feilong.core.lang.StringUtil;

/**
 * 手机号码操作.
 * 
 * <h3>MSISDN</h3>
 * 
 * <blockquote>
 * 
 * <p>
 * MSISDN:Mobile Subscriber International ISDN/PSTN number (ISDN即是综合业务数字网,是Integrated Service Digital Network 的简称)
 * </p>
 * 
 * <pre class="code">
 * 我们的手机号码被称为MSISDN
 * MSISDN＝CC(国家码)＋NDC(7位)(国内目的码)＋SN(4位)(用户号码)
 * 
 * 若将国家码CC去除,就成了移动台国内号码,也就是我们平时所讲的手机号.
 * 
 * NDC包括接入号和HLR的识别号,
 * 接入号就是我们平时所讲的139,138,137......剩下的就是HLR识别号,表示用户归属的HLR,也表示移动业务本地网号.
 * 
 * 前三位没什么区别,130-133是联通的,134-139还有159是移动的,接下来的4位有特殊含义了,与地区有关,每个地区都有自己的号段,现在为什么有些软件可以查手机的归属地,就是靠这4位,最后4位可以重复,用与区分个人!
 * </pre>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.0.2
 */
public final class MobileUtil{

    /** The Constant MASK. */
    private static final String DEFAULT_MASK = "*";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private MobileUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 输入11位手机号码,返回 中间4位或者n位数字为星(*)的号码,默认4个星星.
     * 
     * <p>
     * Examples:
     * 
     * <pre class="code">
     * MobileUtil.getMobileNumberHided("15000001318") returns 150****1318
     * </pre>
     * 
     * @param mobileNumber
     *            11位mobileNumber
     * @return see javadoc
     * 
     * @see org.apache.commons.lang3.StringUtils#abbreviateMiddle(String, String, int)
     */
    public static String getMobileNumberHided(String mobileNumber){
        return getMobileNumberHided(mobileNumber, 4);
    }

    //---------------------------------------------------------------

    /**
     * 输入 11位手机号码,返回 中间4位或者n位数字为星(*)的号码.
     * 
     * <pre class="code">
     * Examples:
     * MobileUtil.getMobileNumberHided("15000001318",5) = 150*****318
     * </pre>
     * 
     * @param mobileNo
     *            11位mobileNumber,不能为空
     * @param count
     *            Segment后面几个电话数需要隐藏,必须>=0
     * @return <blockquote>
     *         <table border="1" cellspacing="0" cellpadding="4" summary="">
     *         <tr>
     *         <th>描述</th>
     *         <th>返回</th>
     *         </tr>
     *         <tr>
     *         <td>isNullOrEmpty(mobileNumber)</td>
     *         <td>throw {@link NullPointerException}</td>
     *         </tr>
     *         <tr>
     *         <td>count &lt; 0</td>
     *         <td>throw {@link IllegalArgumentException}</td>
     *         </tr>
     *         <tr>
     *         <td>mobileNumber.length()<= 3</td>
     *         <td>mobileNumber</td>
     *         </tr>
     *         <tr>
     *         <td>0 == count</td>
     *         <td>mobileNumber</td>
     *         </tr>
     *         <tr>
     *         <td>else</td>
     *         <td>中间4位或者n位数字为星(*)的号码</td>
     *         </tr>
     *         </table>
     *         </blockquote>
     * 
     * @see org.apache.commons.lang3.StringUtils#abbreviateMiddle(String, String, int)
     */
    public static String getMobileNumberHided(String mobileNo,int count){
        Validate.notBlank(mobileNo, "mobileNo can't be null/empty!");
        Validate.isTrue(count >= 0, "the param count must >=0");

        //---------------------------------------------------------------
        if (mobileNo.length() <= 3){
            return mobileNo;
        }
        if (0 == count){
            return mobileNo;
        }

        //---------------------------------------------------------------
        StringBuilder sb = new StringBuilder();
        // 运营商编码
        sb.append(getMobileNumberSegment(mobileNo));

        //---------------------------------------------------------------
        for (int i = 0; i < count; ++i){
            sb.append(DEFAULT_MASK);
        }
        //---------------------------------------------------------------
        int lastLenth = 8 - count;
        sb.append(StringUtil.substringLast(mobileNo, lastLenth));
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * 获得手机的号码段.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * MobileUtil.getMobileNumberNumberSegment("15000001318") returns 150
     * </pre>
     * 
     * </blockquote>
     * 
     * @param mobileNumber
     *            11位mobileNumber
     * @return 如果 <code>mobileNumber</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>mobileNumber</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static String getMobileNumberSegment(String mobileNumber){
        Validate.notBlank(mobileNumber, "mobileNumber can't be blank!");
        return mobileNumber.substring(0, 3);
    }
}
