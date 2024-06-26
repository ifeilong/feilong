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
package com.feilong.core.lang.annotation;

import java.lang.annotation.Annotation;

/**
 * 将 {@link Annotation} 转成 {@link String} 的接口.
 *
 * @param <T>
 *            the generic type
 * 
 * @see com.feilong.lib.lang3.AnnotationUtils#toString(Annotation)
 * @see com.feilong.lib.lang3.builder.Builder
 * @see com.feilong.lib.lang3.builder.ToStringBuilder
 * @since 1.10.4
 */
@FunctionalInterface // 该注解加不加,对于接口是不是函数式接口没有影响;只是提醒编译器去检查该接口是否仅包含一个抽象方法
public interface AnnotationToStringBuilder<T extends Annotation> {

    /**
     * 将制定的 {@link Annotation} 转成 {@link String}.
     *
     * @param annotation
     *            the annotation
     * @return 如果 <code>annotation</code> 是null,返回 EMPTY String<br>
     */
    String build(T annotation);
}