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
package com.feilong.formatter.builder;

import java.util.Map;

import com.feilong.core.lang.ClassUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * {@link FormatterBuilder} 工厂.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.10.4
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FormatterBuilderFactory{

    /** map类型的格式化创造器. */
    private static final MapTypeFormatterBuilder  MAP_DATA_BUILDER  = new MapTypeFormatterBuilder();

    /** bean类型的格式化创造器. */
    private static final BeanTypeFormatterBuilder BEAN_DATA_BUILDER = new BeanTypeFormatterBuilder();

    //---------------------------------------------------------------

    /**
     * 不同的bean类型,返回不同的创造器.
     * 
     * <p>
     * 如果<code>bean</code>是 map 类型, 那么返回 {@link MapTypeFormatterBuilder},否则返回 {@link BeanTypeFormatterBuilder}.
     * </p>
     *
     * @param <T>
     *            the generic type
     * @param bean
     *            the bean
     * @return 如果<code>bean</code>是 map 类型, 那么返回 {@link MapTypeFormatterBuilder},否则返回 {@link BeanTypeFormatterBuilder}.
     */
    public static <T> FormatterBuilder create(T bean){
        if (ClassUtil.isInstance(bean, Map.class)){
            return MAP_DATA_BUILDER;
        }
        return BEAN_DATA_BUILDER;
    }
}
