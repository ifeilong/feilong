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
package com.feilong.tools.emailaddress;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.ResourceBundleUtil.getResourceBundle;
import static com.feilong.core.util.ResourceBundleUtil.toMap;
import static com.feilong.core.util.SortUtil.sortListByPropertyNamesValue;
import static com.feilong.formatter.FormatterUtil.formatToSimpleTable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.StringUtil;

/**
 * 解析邮箱地址工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://en.wikipedia.org/wiki/Email_address">Email_address</a>
 * @since 1.0.1
 */
public final class EmailAddressUtil{

    /** The Constant log. */
    private static final Logger               LOGGER                    = LoggerFactory.getLogger(EmailAddressUtil.class);

    //---------------------------------------------------------------

    /** The Constant AT. */
    private static final String               AT                        = "@";

    /** 值得分隔符. */
    private static final String               DELIMITERS                = ",;";

    /** 配置文件地址. */
    static final Map<String, String>          EMAIL_PROVIDER_MAP        = toMap(getResourceBundle("config/feilong-emailProvider"));

    //---------------------------------------------------------------

    /** key 为 domain. */
    private static Map<String, EmailProvider> domainAndEmailProviderMap = new ConcurrentHashMap<>();

    //---------------------------------------------------------------

    static{
        init();
    }

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private EmailAddressUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 基于 <code>emailAddress</code> 获得 {@link EmailProvider} .
     * 
     * <h3>原理:</h3>
     * <blockquote>
     * <p>
     * 先基于 <code>emailAddress</code>,解析获得 <code>domain</code>,然后转成小写,从 {@link #domainAndEmailProviderMap}中取到对应的value
     * </p>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * <blockquote>
     * 
     * <pre class="code">
     * EmailProvider emailProvider = EmailAddressUtil.getEmailProvider("feilong@163.com");
     * LOGGER.debug(JsonUtil.format(emailProvider));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {
     * "webSite": "http://mail.163.com/",
     * "name": "网易163邮箱",
     * "domain": "163.com"
     * }
     * </pre>
     * 
     * </blockquote>
     *
     * @param emailAddress
     *            邮箱地址
     * @return 如果 <code>emailAddress</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>emailAddress</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #getDomain(String)
     * @since 1.5.3
     */
    public static EmailProvider getEmailProvider(String emailAddress){
        Validate.notBlank(emailAddress, "emailAddress can't be blank!");

        String domain = getDomain(emailAddress);
        return isNullOrEmpty(domain) ? null : domainAndEmailProviderMap.get(domain.toLowerCase());
    }

    /**
     * 获得邮件地址的后缀名.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * EmailAddressUtil.getDomain("feilong@163.com") = 163.com
     * </pre>
     * 
     * </blockquote>
     * 
     * @param emailAddress
     *            邮件地址
     * @return 如果 <code>emailAddress</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>emailAddress</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.5.3
     */
    public static String getDomain(String emailAddress){
        Validate.notBlank(emailAddress, "emailAddress can't be blank!");
        return StringUtils.substringAfterLast(emailAddress, AT);
    }

    /**
     * 获得邮件地址的前缀.
     * 
     * <pre class="code">
     * EmailAddressUtil.getUserName("feilong@163.com") = feilong
     * </pre>
     *
     * @param emailAddress
     *            邮箱地址
     * @return 如果 <code>emailAddress</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>emailAddress</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.5.3
     */
    public static String getUser(String emailAddress){
        Validate.notBlank(emailAddress, "emailAddress can't be blank!");
        return StringUtil.split(emailAddress, AT)[0];
    }

    //---------------------------------------------------------------

    /**
     * 初始化.
     *
     * @since 1.5.3
     */
    private static void init(){
        Validate.notEmpty(EMAIL_PROVIDER_MAP, "EmailServiceProvider properties can't be null/empty!");

        //---------------------------------------------------------------

        for (Map.Entry<String, String> entry : EMAIL_PROVIDER_MAP.entrySet()){
            String key = entry.getKey();
            //统统转成小写
            domainAndEmailProviderMap.put(key.toLowerCase(), buildEmailProvider(key, entry.getValue()));
        }

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            //Caused by: java.lang.ClassCastException: java.util.concurrent.ConcurrentHashMap$Values cannot be cast to java.util.List
            //ConcurrentHashMap$Values  是自定义的类
            List<EmailProvider> iterable = sortListByPropertyNamesValue(toList(domainAndEmailProviderMap.values()), "domain");
            LOGGER.debug("init [data] over,{}", formatToSimpleTable(iterable, "domain", "webSite", "name"));
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the email provider.
     *
     * @param key
     *            the key
     * @param value
     *            the value
     * @return the email provider
     * @since 1.10.4
     */
    private static EmailProvider buildEmailProvider(String key,String value){
        String[] tokenizeToStringArray = StringUtil.tokenizeToStringArray(value, DELIMITERS);
        return new EmailProvider(key, tokenizeToStringArray[0], tokenizeToStringArray[1]);
    }
}