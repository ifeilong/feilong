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

import com.feilong.context.converter.StringToBeanConverter;

/**
 * 默认的响应结果对象构造器.
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
 * @since 1.11.3
 * @since 4.1.0 remove T extends ResponseCommand
 */
public class DefaultResponseCommandBuilder<R extends InvokerRequest, T> extends AbstractResponseCommandBuilder<R, T>{

    /** 响应结果字符串构造器. */
    private ResponseStringBuilder<R> responseStringBuilder;

    /** 字符串转换成bean 转换器. */
    private StringToBeanConverter<T> stringToBeanConverter;

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
     * 设置 字符串转换成bean 转换器.
     *
     * @param stringToBeanConverter
     *            the stringToBeanConverter to set
     */
    public void setStringToBeanConverter(StringToBeanConverter<T> stringToBeanConverter){
        this.stringToBeanConverter = stringToBeanConverter;
    }

    //---------------------------------------------------------------

    /**
     * 获得 响应结果字符串构造器.
     *
     * @return the 响应结果字符串构造器
     */
    @Override
    protected ResponseStringBuilder<R> createResponseStringBuilder(){
        return responseStringBuilder;
    }

    /**
     * 获得 字符串转换成bean 转换器.
     *
     * @return the 字符串转换成bean 转换器
     * @since 4.1.0 add param responseString
     */
    @Override
    protected StringToBeanConverter<T> createStringToBeanConverter(String responseString){
        return stringToBeanConverter;
    }

}
