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
 * 数组型 请求参数构造器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @see RequestParamsBuilder
 * @since 1.17.0
 */
public interface RequestArrayParamsBuilder<T> {

    /**
     * 用来基于 <code>request</code> 构造 Object 数组的方法.
     * 
     * <p>
     * 常用于 {@link com.feilong.context.invoker.jaxws.JaxWsDynamicResponseStringBuilder}
     * </p>
     *
     * @param request
     *            the refund request
     * @return the object[]
     */
    Object[] build(T request);
}
