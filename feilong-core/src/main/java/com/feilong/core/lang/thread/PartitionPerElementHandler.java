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
package com.feilong.core.lang.thread;

import java.util.Map;

/**
 * 分区中的每个线程中的每个元素执行.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @see com.feilong.core.lang.thread.DefaultPartitionRunnableBuilder
 * @since 4.0.6
 */
@FunctionalInterface // 该注解加不加,对于接口是不是函数式接口没有影响;只是提醒编译器去检查该接口是否仅包含一个抽象方法
public interface PartitionPerElementHandler<T> {

    /**
     * Handle.
     *
     * @param element
     *            分区中的每个线程中的每个元素执行.
     * @param partitionThreadEntity
     *            线程执行此组list 的时候,可以使用的 thread参数信息
     * @param paramsMap
     *            自定义的参数map
     */
    void handle(T element,PartitionThreadEntity partitionThreadEntity,Map<String, ?> paramsMap);
}
