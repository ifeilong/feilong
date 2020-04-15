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
package com.feilong.servlet.http;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.feilong.core.net.ParamUtil;

import static com.feilong.core.URIComponents.FRAGMENT;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.4.0
 */
public class RequestUtilTemp{

    /**
     * 原样获得参数值.
     * 
     * <p>
     * <span style="color:red">注:url参数是什么,取到的就是什么,不经过处理</span>
     * </p>
     * 
     * <p>
     * 注:({@link javax.servlet.ServletRequest#getParameter(String)}函数时,会自动进行一次URI的解码过程,调用时内置的解码过程会导致乱码出现)
     * </p>
     * 
     * @param request
     *            请求
     * @param paramName
     *            参数名称
     * @return 原样获得参数值
     * @deprecated 有使用场景吗?
     */
    @Deprecated
    public static String getParameterAsItIsDecode(HttpServletRequest request,String paramName){
        String returnValue = null;
        String queryString = request.getQueryString();
        if (isNotNullOrEmpty(queryString)){
            Map<String, String> map = ParamUtil.toSingleValueMap(queryString, null);
            return map.get(paramName);
        }
        return returnValue;
    }

    /**
     * 取到参数值,没有返回null,有去除空格返回.
     * 
     * @param request
     *            当前请求
     * @param paramName
     *            the param name
     * @return 取到参数值,没有返回null,有去除空格返回
     * @deprecated 不推荐使用
     */
    @Deprecated
    public static String getParameterWithTrim(HttpServletRequest request,String paramName){
        String returnValue = RequestUtil.getParameter(request, paramName);
        if (isNotNullOrEmpty(returnValue)){
            return returnValue.trim();
        }
        return returnValue;
    }

    /**
     * 参数值去除井号,一般用于sendDirect 跳转中带有#标签,参数值取不准确的问题.
     * 
     * @param request
     *            the request
     * @param paramName
     *            the param name
     * @return 参数值去除井号,一般用于sendDirect 跳转中带有#标签,参数值取不准确的问题
     * @deprecated 将来会重构
     */
    @Deprecated
    public static String getParameterWithoutSharp(HttpServletRequest request,String paramName){
        String returnValue = RequestUtil.getParameter(request, paramName);
        if (isNotNullOrEmpty(returnValue)){
            if (StringUtils.contains(returnValue, FRAGMENT)){
                returnValue = substring(returnValue, null, FRAGMENT);
            }
        }
        return returnValue;
    }

    /**
     * [截取]:从开始的字符串 <code>startString</code> 到结束的字符串 <code>endString</code> 中间的字符串.
     * 
     * <p>
     * 包含开始的字符串<code>startString</code>,不包含结束的<code>endString</code>.
     * </p>
     * 
     * @param text
     *            文字
     * @param startString
     *            开始的字符串,null表示从开头开始截取
     * @param endString
     *            结束的字符串
     * @return 如果 <code>text</code> 是null或者empty,返回 {@link StringUtils#EMPTY}<br>
     *         如果isNullOrEmpty(startString),返回 text.substring(0, text.indexOf(endString))
     * @see org.apache.commons.lang3.StringUtils#substringBetween(String, String, String)
     */
    public static String substring(final String text,final String startString,final String endString){
        return isNullOrEmpty(text) ? EMPTY
                        : text.substring(isNullOrEmpty(startString) ? 0 : text.indexOf(startString), text.indexOf(endString));
    }
}
