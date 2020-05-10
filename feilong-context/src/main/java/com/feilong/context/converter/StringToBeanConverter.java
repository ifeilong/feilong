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
package com.feilong.context.converter;

/**
 * 将字符串转成 bean 的转换器.
 * 
 * <p>
 * 功能和{@link org.apache.commons.collections4.Transformer} 类似,但是更专业和具体
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @param <T>
 *            the generic type
 * @see com.feilong.lib.beanutils.Converter
 * @see org.apache.commons.collections4.Transformer
 * @see "org.springframework.core.convert.converter.Converter"
 * @see "org.springframework.core.convert.support.StringToEnumConverterFactory"
 * @since 1.8.3
 * @since 1.11.2 rename from AbstractParse
 */
public interface StringToBeanConverter<T> {

    /**
     * 转换.
     *
     * @param value
     *            the value
     * @return 如果 <code>value</code> 是null或者empty,返回 null<br>
     */
    T convert(String value);

}
