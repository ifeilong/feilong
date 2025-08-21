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

/**
 * // 合作方实现接口签名生成算法
 * // 有些接口需要合作方按照规范要求实现，比如专辑/声音上下架推送接口，这些接口的签名生成算法如下：
 * //
 * // (1) 将除了sig以外的所有其他请求参数的原始值按照参数名的字典序排序
 * // (2) 将排序后的参数键值对用&拼接，即拼接成key1=val1&key2=val2这样的形式
 * // (3) 将上一步得到的字符串后拼上app_secret，比如app_secret为abc，那么现在就得到key1=val1&key2=val2&app_secret=abc
 * // (4) 对上一步得到的字符串进行MD5运算得到32位小写字符串，即为sig
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.8
 */
public class DefaultMapSigner extends AbstractMapSigner{

    /**
     * @param mapSignConfig
     */
    public DefaultMapSigner(MapSignConfig mapSignConfig){
        super(mapSignConfig);
    }
}
