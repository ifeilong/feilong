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
package com.feilong.context.log;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static com.feilong.core.lang.StringUtil.formatPattern;

import javax.servlet.http.HttpServletRequest;

import com.feilong.servlet.http.RequestUtil;
import com.feilong.spring.web.util.WebSpringUtil;

/**
 * 专门用来创建request log的.
 * 
 * @since 4.0.6
 */
public final class RequestLogHelper{

    /**
     * 自动追加request简单信息.
     * 
     * <p>
     * 自动获取请求的路径和请求参数,前置追加类似于这种字符串
     * api:[http://mpay.feilong.com/subscribe/h5],paramString:[app_key=876*******14ec5f3c&third_party_order_no=XML*******95315&timestamp=1733******111&sig=145ad********f84013467
     * </p>
     *
     * @param messagePattern
     *            the message pattern
     * @param params
     *            the params
     * @return the string
     * @since 4.0.6
     */
    public static String autoRequestInfo(String messagePattern,Object...params){
        HttpServletRequest request = WebSpringUtil.getRequest();
        return autoRequestInfo(request, messagePattern, params);
    }

    //---------------------------------------------------------------

    /**
     * 显性传入HttpServletRequest参数, 自动追加request简单信息.
     * 
     * <p>
     * 此方法非常适用于filter里面, 因为filter先于springmvc无法自动获取HttpServletRequest对象
     * </p>
     * 
     * <p>
     * 自动获取请求的路径和请求参数,前置追加类似于这种字符串
     * api:[http://mpay.feilong.com/subscribe/h5],paramString:[app_key=876*******14ec5f3c&third_party_order_no=XML*******95315&timestamp=1733******111&sig=145ad********f84013467
     * </p>
     *
     * @param request
     *            the request
     * @param messagePattern
     *            the message pattern
     * @param params
     *            the params
     * @return the string
     * @since 4.2.1
     */
    public static String autoRequestInfo(HttpServletRequest request,String messagePattern,Object...params){
        String message = formatPattern(messagePattern, params);

        StringBuilder sb = new StringBuilder(getRequestInfo(request));
        if (isNotNullOrEmpty(message)){
            sb.append(",");
            sb.append(message);
        }
        return sb.toString();
    }

    //---------------------------------------------------------------

    /**
     * 自动生成api地址以及请求参数的log 字符串
     * 
     * @param request
     *            the request
     * @return the request info
     */
    private static String getRequestInfo(HttpServletRequest request){
        return formatPattern(
                        "api:[{}],paramString:[{}]",
                        null == request ? EMPTY : RequestUtil.getRequestURL(request), //request.getPathInfo(),//可能是null
                        RequestUtil.parseParamsToQueryString(request));
    }

}
