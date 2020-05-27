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
 * 签名器.
 * 
 * <p>
 * 一些get请求（不限get），如果只是简单的基本参数，容易被黑客或者用户频繁刷新连接，如果这个连接会有数据库操作的话，会对db产生压力<br>
 * 故，增加特殊参数判断.
 * </p>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.1
 */
public interface Signer{

    /**
     * Sign.
     *
     * @param strs
     *            需要被签名的字符串
     * @return the string
     */
    String sign(CharSequence...strs);

}