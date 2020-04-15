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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.feilong.core.lang.StringUtil;
import com.feilong.core.util.RegexUtil;

/**
 * 路径提取器.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public class ItemSrcExtractor{

    /** Don't let anyone instantiate this class. */
    private ItemSrcExtractor(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 解析 <code>item</code> 里面的内容.
     * 
     * <h3>示例1:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * item     =   {@code <script type="text/javascript" src="scripts/pdp/sub_salesProperties.js?2015"></script>}
     * domain   =   http://css.feilong.com:8888
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {@code scripts/pdp/sub_salesProperties.js}
     * </pre>
     * 
     * </blockquote>
     * 
     * 
     * <h3>示例2:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * item     =   {@code <link rel="stylesheet" href="http://css.feilong.com:8888/res/feilong/css/feilong-all.css?version=12345666" type=
    "text/css"></link>}
     * domain   =   http://css.feilong.com:8888/
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * {@code res/feilong/css/feilong-all.css}
     * </pre>
     * 
     * </blockquote>
     *
     * @param item
     *            the item
     * @param domain
     *            the domain
     * @return 如果 <code>item</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>item</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>item</code> 以 {@code "<link "} 开头,那么将提取href 里面的内容,并且去除 domain, 去除 {@code ?} 后面的部分内容<br>
     *         如果 <code>item</code> 以 {@code "<script "} 开头,那么将提取src 里面的内容,并且去除 domain, 去除 {@code ?} 后面的部分内容<br>
     *         如果都不是,那么直接 trim <code>item</code> 并返回<br>
     * 
     * @since 1.11.1 remove type param
     */
    static String extract(String item,String domain){
        Validate.notBlank(item, "item can't be blank!");

        //---------------------------------------------------------------
        String workItem = item.trim();

        //如果以 <link 开头
        if (workItem.startsWith("<link ")){
            return pickUp(workItem, ".*?href=\"(.*?)\".*?", domain);
        }

        //---------------------------------------------------------------
        //如果以 <script 开头
        if (workItem.startsWith("<script ")){
            return pickUp(workItem, ".*?src=\"(.*?)\".*?", domain);
        }

        return workItem;
    }

    //---------------------------------------------------------------

    /**
     * 基于正则表达式来提取内部的路径地址.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ul>
     * <li>如果提取出来的内容是以 {@code domain} 开头的,将会去除</li>
     * <li>如果提取出来的内容有 {@code ?} 部分,将会去除</li>
     * </ul>
     * </blockquote>
     *
     * @param workItem
     *            the work item
     * @param regexPattern
     *            the regex pattern
     * @param domain
     *            the domain
     * @return 如果 <code>workItem</code> 不符合regexPattern,那么返回 {@link StringUtils#EMPTY}<br>
     */
    private static String pickUp(String workItem,String regexPattern,String domain){
        String value = RegexUtil.group(regexPattern, workItem, 1);
        if (isNullOrEmpty(value)){
            return EMPTY;
        }

        //-------------------------------------------------------------
        value = value.trim(); //去空

        //------------去除 domain----------------------------------------
        if (value.startsWith(domain)){
            value = StringUtils.substringAfter(value, domain);
        }

        //------------去除 ?---------------------------------------------
        if (value.contains("?")){
            value = StringUtils.substringBefore(value, "?");
        }

        //---------------------------------------------------------------
        //since 1.12.8
        //前面有值
        //如果是以 itemSrc 单斜杆开头,去掉开头的单斜杆
        if (isNotNullOrEmpty(domain) && value.startsWith("/")){
            return StringUtil.substring(value, 1);
        }

        return value;
    }
}
