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
package com.feilong.accessor;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 存取器.
 * 
 * <p>
 * 可以想象为超时寄存包裹的地方,它的凭据是超市柜子出来的小票,该小票类似于 {@link Accessor} 相关地方使用的key
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.5
 */
public interface Accessor{

    /**
     * 获取.
     *
     * @param <T>
     *            the generic type
     * @param key
     *            标识,用什么存就用什么取
     * @param request
     *            the request
     * @return the barcode contents and config
     */
    <T extends Serializable> T get(String key,HttpServletRequest request);

    /**
     * 删除保存的key.
     *
     * @param key
     *            the key
     * @param request
     *            the request
     */
    void remove(String key,HttpServletRequest request);
}
