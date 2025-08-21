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

import com.feilong.context.InputParamsValidateable;
import com.feilong.context.converter.StringToBeanConverter;
import com.feilong.json.JsonUtil;

/**
 * 默认的 {@link RequestResultInvoker}.
 * 
 * <p>
 * 这个类通常用于feilong支付框架,一般直接使用 {@link DefaultResponseCommandBuilder}就够用了
 * </p>
 * 
 * <h3>代码流程:</h3>
 * <blockquote>
 * <ol>
 * <li>第1步,如果<code>invokerRequestValidator</code> 不是null,那么校验 request</li>
 * <li>第2步,通过调用 {@link ResponseStringBuilder} 来得到响应字符串</li>
 * <li>第3步,将得到的字符串,使用 {@link StringToBeanConverter} 来转换成对象</li>
 * <li>第4步,使用 {@link InvokerResultConverter} 来将 request, invokerResponse字符串以及对象转换成另外一个对象</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @param <R>
 *            the generic type
 * @param <N>
 *            the number type
 * @see DefaultResponseCommandBuilder
 * @since 1.11.2
 */
@lombok.extern.slf4j.Slf4j
public class DefaultRequestResultInvoker<T extends ResponseCommand, R extends InvokerRequest, N extends InvokerResult>
                implements RequestResultInvoker<R, N>,InputParamsValidateable{

    /** 请求参数自定义校验. */
    protected InvokerRequestValidator<R>      invokerRequestValidator;

    //---------------------------------------------------------------

    //本来这两个属性 可以使用 DefaultResponseCommandBuilder , 但是invokerResultConverter 需要  invokerResponse 参数

    /**
     * 调用结果构造器.
     * 
     * @since 1.11.2
     */
    protected ResponseStringBuilder<R>        responseStringBuilder;

    /** 字符串转换成bean 转换器. */
    protected StringToBeanConverter<T>        stringToBeanConverter;

    //---------------------------------------------------------------

    /** 将对象转换成 {@link InvokerResult}. */
    protected InvokerResultConverter<R, T, N> invokerResultConverter;
    //---------------------------------------------------------------

    /**
     * Invoke.
     *
     * @param queryRequest
     *            the query request
     * @return the n
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.netpay.handler.query.QueryHandler#handler(com.feilong.netpay.handler.query.QueryRequest)
     */
    @Override
    public N invoke(R queryRequest){
        if (null != invokerRequestValidator){
            invokerRequestValidator.validate(queryRequest);
        }
        return doHandler(queryRequest);
    }

    //---------------------------------------------------------------

    /**
     * Do handler.
     *
     * @param request
     *            the query request
     * @return the query result
     * @since 1.11.2
     */
    public N doHandler(R request){
        String invokerResponse = responseStringBuilder.build(request);
        if (isNullOrEmpty(invokerResponse)){
            throw new InvokerResponseBlankException("invokerResponse can't be null/empty!,request:[{}]", JsonUtil.toString(request));
        }

        //---------------------------------------------------------------
        if (log.isInfoEnabled()){
            log.info("NetpayRequest:[{}],invokerResponse:[{}]", JsonUtil.toString(request), invokerResponse);
        }

        //---------------------------------------------------------------
        T t = stringToBeanConverter.convert(invokerResponse);

        //---------------------------------------------------------------

        //因为此处要传递 invokerResponse
        return invokerResultConverter.convert(request, invokerResponse, t);
    }

    //---------------------------------------------------------------
    /**
     * 设置 请求参数自定义校验.
     *
     * @param invokerRequestValidator
     *            the invokerRequestValidator to set
     */
    public void setInvokerRequestValidator(InvokerRequestValidator<R> invokerRequestValidator){
        this.invokerRequestValidator = invokerRequestValidator;
    }

    /**
     * 设置 调用结果构造器.
     *
     * @param responseStringBuilder
     *            the responseStringBuilder to set
     */
    public void setResponseStringBuilder(ResponseStringBuilder<R> responseStringBuilder){
        this.responseStringBuilder = responseStringBuilder;
    }

    /**
     * 设置 字符串转换成bean 转换器.
     *
     * @param stringToBeanConverter
     *            the stringToBeanConverter to set
     */
    public void setStringToBeanConverter(StringToBeanConverter<T> stringToBeanConverter){
        this.stringToBeanConverter = stringToBeanConverter;
    }

    /**
     * 设置 将对象转换成 {@link InvokerResult}.
     *
     * @param invokerResultConverter
     *            the invokerResultConverter to set
     */
    public void setInvokerResultConverter(InvokerResultConverter<R, T, N> invokerResultConverter){
        this.invokerResultConverter = invokerResultConverter;
    }
}
