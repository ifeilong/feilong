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
 * 指明key的存取器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.5
 */
public interface KeyAccessor extends Accessor{

    /**
     * 保存.
     *
     * @param key
     *            标识,下次就拿这个标识来取就可以了
     * @param serializable
     *            the serializable
     * @param request
     *            the request
     */
    void save(String key,Serializable serializable,HttpServletRequest request);
}
