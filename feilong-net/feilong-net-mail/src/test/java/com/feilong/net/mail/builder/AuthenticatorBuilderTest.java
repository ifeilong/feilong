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
package com.feilong.net.mail.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.test.AbstractTest;

/**
 * {@link AuthenticatorBuilder} 的单元测试, 不依赖网络.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class AuthenticatorBuilderTest extends AbstractTest{

    /** 需要身份认证的时候, 返回密码验证器. */
    @Test
    public void testBuildNeedValidate(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");
        sessionConfig.setUserName("feilong");
        sessionConfig.setPassword("123456");

        assertNotNull(AuthenticatorBuilder.build(sessionConfig));
    }

    /** 不需要身份认证的时候, 返回null. */
    @Test
    public void testBuildNotNeedValidate(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");
        sessionConfig.setIsValidate(false);

        assertNull(AuthenticatorBuilder.build(sessionConfig));
    }

    @Test(expected = NullPointerException.class)
    public void testBuildNullSessionConfig(){
        AuthenticatorBuilder.build(null);
    }

}
