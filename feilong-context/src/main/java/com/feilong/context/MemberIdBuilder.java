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
package com.feilong.context;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户的id构造器.
 * 
 * <p>
 * 支持构造游客的member Id
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @param <T>
 *            the generic type
 * @since 1.11.3
 * @since 1.12.0 rename to MemberIdBuilder
 */
public interface MemberIdBuilder<T> {

    /**
     * 基于request得到当前用户的id.
     * 
     * <p>
     * 支持构造游客的member Id,比如有些商城固定member id 是 1
     * </p>
     *
     * @param request
     *            the request
     * @return the long
     */
    T build(HttpServletRequest request);
}
