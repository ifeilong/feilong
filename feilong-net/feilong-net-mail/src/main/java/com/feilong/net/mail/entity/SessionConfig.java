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
package com.feilong.net.mail.entity;

/**
 * 和 session 相关的配置.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.3
 */
public interface SessionConfig{

    /**
     * 获得 登录邮件发送服务器的用户名.
     *
     * @return the userName
     */
    String getUserName();

    //---------------------------------------------------------------

    /**
     * 获得 登录邮件发送服务器的密码.
     *
     * @return the password
     */
    String getPassword();

    //---------------------------------------------------------------

    /**
     * 获得 发送邮件的服务器的IP.
     *
     * @return the mailServerHost
     * @since 1.13.2 rename from mailServerHost
     */
    String getServerHost();

    /**
     * 获得 邮件服务的端口 默认25.
     *
     * @return the mailServerPort
     * @since 1.13.2 rename from mailServerPort
     */
    String getServerPort();

    //---------------------------------------------------------------

    /**
     * 获得 是否debug 输出.
     *
     * @return the isDebug
     */
    boolean getIsDebug();

    /**
     * 获得 是否需要身份验证,默认 true.
     *
     * @return the isValidate
     */
    boolean getIsValidate();

}
