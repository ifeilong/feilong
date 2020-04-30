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
package com.feilong.context.invoker;

import static com.feilong.core.Validator.isNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.context.converter.StringToBeanConverter;
import com.feilong.json.JsonUtil;

/**
 * 默认的处理请求处理器.
 * 
 * <h3>代码流程:</h3>
 * <blockquote>
 * <ol>
 * <li>第1步,通过 调用 {@link ResponseStringBuilder} 来得到响应字符串</li>
 * <li>第2步,将得到的字符串,使用 {@link StringToBeanConverter} 来转换成对象</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <R>
 *            请求
 * @see DefaultRequestResultInvoker
 * @since 2.1.0
 */
public class DefaultResponseHandler<R extends InvokerRequest> implements ResponseHandler<R>{

    /** The Constant LOGGER. */
    private static final Logger        LOGGER = LoggerFactory.getLogger(DefaultResponseHandler.class);

    //---------------------------------------------------------------

    /** 响应结果字符串构造器. */
    protected ResponseStringBuilder<R> responseStringBuilder;

    //---------------------------------------------------------------

    /** 处理响应字符串和请求对象. */
    protected ResponseStringHandler<R> responseStringHandler;

    //---------------------------------------------------------------

    /**
     * Handle.
     *
     * @param request
     *            the request
     */
    @Override
    public void handle(R request){
        String invokerResponseString = responseStringBuilder.build(request);
        //如果得到不响应的字符串, 将抛出异常
        if (isNullOrEmpty(invokerResponseString)){
            throw new InvokerResponseBlankException("invokerResponse can't be null/empty!,request:[{}]", JsonUtil.format(request));
        }

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("NetpayRequest:[{}],invokerResponse:[{}]", JsonUtil.format(request), invokerResponseString);
        }
        responseStringHandler.handle(invokerResponseString, request);
    }

    //---------------------------------------------------------------

    /**
     * 设置 响应结果字符串构造器.
     *
     * @param responseStringBuilder
     *            the responseStringBuilder to set
     */
    public void setResponseStringBuilder(ResponseStringBuilder<R> responseStringBuilder){
        this.responseStringBuilder = responseStringBuilder;
    }

    /**
     * 设置 处理响应字符串和请求对象.
     *
     * @param responseStringHandler
     *            the responseStringHandler to set
     */
    public void setResponseStringHandler(ResponseStringHandler<R> responseStringHandler){
        this.responseStringHandler = responseStringHandler;
    }

}
