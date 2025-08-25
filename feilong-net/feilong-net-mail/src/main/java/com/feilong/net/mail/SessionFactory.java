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
package com.feilong.net.mail;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;

import com.feilong.core.Validate;
import com.feilong.net.mail.builder.AuthenticatorBuilder;
import com.feilong.net.mail.builder.SessionPropertiesBuilder;
import com.feilong.net.mail.entity.SessionConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 根据邮件会话属性和密码验证器构造邮件的{@link javax.mail.Session}.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.9
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SessionFactory{

    /**
     * 根据邮件会话属性和密码验证器构造邮件的{@link javax.mail.Session}.
     * 
     * @param sessionConfig
     *            mailSenderInfo
     * @return Session
     * @see javax.mail.Session#getDefaultInstance(Properties, Authenticator)
     */
    public static Session createSession(SessionConfig sessionConfig){
        //since 1.13.2
        Validate.notNull(sessionConfig, "sessionConfig can't be null!");

        //---------------------------------------------------------------
        Authenticator authenticator = AuthenticatorBuilder.build(sessionConfig);
        Properties properties = SessionPropertiesBuilder.build(sessionConfig);

        //---------------------------------------------------------------

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = Session.getDefaultInstance(properties, authenticator);
        session.setDebug(sessionConfig.getIsDebug());
        return session;
    }
}
