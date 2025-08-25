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
package com.feilong.taglib.display.breadcrumb.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 面包屑常量.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.2.2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BreadCrumbConstants{

    /** 默认面包屑使用的vm 脚本 <code>{@value}</code>. */
    public static final String DEFAULT_TEMPLATE_IN_CLASSPATH = "velocity/feilong-default-breadCrumb.vm";

    /** 连接符,默认 <code>{@value}</code>. */
    public static final String DEFAULT_CONNECTOR             = ">";

}
