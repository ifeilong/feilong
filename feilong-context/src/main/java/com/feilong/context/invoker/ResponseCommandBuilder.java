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
 * 响应结果对象构造器.
 * 
 * <p>
 * 顶级接口
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <R>
 *            请求
 * @param <T>
 *            响应的字符串转换成的对象
 * @since 1.11.3
 */
public interface ResponseCommandBuilder<R extends InvokerRequest, T extends ResponseCommand> {

    /**
     * 通过发送请求,得到响应的对象.
     *
     * @param request
     *            the request
     * @return the string
     */
    T build(R request);

}
