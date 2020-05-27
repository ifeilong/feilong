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

/**
 * 将 {@link ResponseCommand} 转成 {@link InvokerResult}转换器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <R>
 *            请求的相关属性对象
 * @param <T>
 *            网关响应的字符串转换之后的对象
 * @param <N>
 *            再次需要转换的对象
 * @since 1.11.2
 */
public interface InvokerResultConverter<R extends InvokerRequest, T extends ResponseCommand, N extends InvokerResult> {

    /**
     * 将请求<code>request</code>, 接口响应的字符串 <code>invokerResponse</code>, 以及字符串转后的对象 <code>t</code>, 转成 指定的结果类型.
     *
     * @param request
     *            相关调用请求对象
     * @param invokerResponse
     *            调用结果字符串
     * @param t
     *            the t
     * @return the query result
     */
    N convert(R request,String invokerResponse,T t);
}
