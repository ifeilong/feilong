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

import com.feilong.context.Data;

/**
 * 查询数据以及查询结果处理器.
 * 
 * <p>
 * 通常用于 诸如 定时查询交易的任务, 然后拿到结果后更新数据库
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <D>
 *            the generic type
 * @param <I>
 *            the generic type
 * @since 2.0.0
 */
public interface DataAndInvokerResultHandler<D extends Data, I extends InvokerResult> {

    /**
     * Handler.
     * 
     * @param data
     *            the no paid trade data
     * @param invokerResult
     *            the query result
     */
    void handle(D data,I invokerResult);
}
