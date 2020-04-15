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

/**
 * 面包屑常量.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.2.2
 */
public final class BreadCrumbConstants{

    /** 默认面包屑使用的vm 脚本 <code>{@value}</code>. */
    public static final String DEFAULT_TEMPLATE_IN_CLASSPATH = "velocity/feilong-default-breadCrumb.vm";

    /** 连接符,默认 <code>{@value}</code>. */
    public static final String DEFAULT_CONNECTOR             = ">";

    /** Don't let anyone instantiate this class. */
    private BreadCrumbConstants(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
}
