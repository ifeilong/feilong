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
     * @param messagePattern
     *            the message pattern
     * @param params
     *            the params
     * @return the string
     * @since 4.0.6
     */
    public static String autoRequestInfo(String messagePattern,Object...params){
        String message = formatPattern(messagePattern, params);

        StringBuilder sb = new StringBuilder(getRequestInfo());
        if (isNotNullOrEmpty(message)){
            sb.append(",");
            sb.append(message);
        }
        return sb.toString();
    }

    /**
     * Gets the request info.
     *
     * @return the request info
     */
    private static String getRequestInfo(){
        HttpServletRequest request = WebSpringUtil.getRequest();
        return formatPattern(
                        "api:[{}],queryString:[{}]",
                        null == request ? EMPTY : RequestUtil.getRequestURL(request), //request.getPathInfo(),//可能是null
                        null == request ? EMPTY : request.getQueryString());
    }

}
