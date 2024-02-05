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
package com.feilong.context.signer;

import java.util.Map;

/**
 * 常用于签名,验签签名器.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
@FunctionalInterface // 该注解加不加,对于接口是不是函数式接口没有影响;只是提醒编译器去检查该接口是否仅包含一个抽象方法
public interface MapSigner{

    /**
     * Sign.
     *
     * @param map
     *            需要被签名的map
     * @return 如果 <code>map</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>map</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     */
    String sign(Map<String, String> map);

}