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
 * 抽象的响应结果对象构造器.
 * 
 * <h3>代码流程:</h3>
 * <blockquote>
 * <ol>
 * <li>第1步,通过调用 {@link ResponseStringBuilder} 来得到响应字符串</li>
 * <li>第2步,将得到的字符串,使用 {@link StringToBeanConverter} 来转换成对象</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <R>
 *            请求
 * @param <T>
 *            响应的字符串转换成的对象
 * @see DefaultRequestResultInvoker
 * @since 3.4.1
 */
public abstract class AbstractResponseCommandBuilder<R extends InvokerRequest, T extends ResponseCommand>
                implements ResponseCommandBuilder<R, T>{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResponseCommandBuilder.class);

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.context.invoker.ResponseCommandBuilder#build(com.feilong.context.invoker.InvokerRequest)
     */
    @Override
    public T build(R request){
        ResponseStringBuilder<R> responseStringBuilder = createResponseStringBuilder();
        String responseString = responseStringBuilder.build(request);
        //如果得到不响应的字符串, 将抛出异常
        if (isNullOrEmpty(responseString)){
            throw new InvokerResponseBlankException("responseString can't be null/empty!,request:[{}]", JsonUtil.toString(request));
        }

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("requestDataInfo:[{}],responseString:[{}]", JsonUtil.toString(request), responseString);
        }

        StringToBeanConverter<T> stringToBeanConverter = createStringToBeanConverter();
        return stringToBeanConverter.convert(responseString);
    }

    //---------------------------------------------------------------

    protected abstract ResponseStringBuilder<R> createResponseStringBuilder();

    protected abstract StringToBeanConverter<T> createStringToBeanConverter();

}
