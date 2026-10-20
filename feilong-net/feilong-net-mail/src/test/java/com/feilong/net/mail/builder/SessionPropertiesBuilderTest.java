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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Test;

import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.test.AbstractTest;

/**
 * {@link SessionPropertiesBuilder} 的单元测试, 不依赖网络.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class SessionPropertiesBuilderTest extends AbstractTest{

    /** 默认情况下, 只需要 host, 端口是25, 并且开启身份认证. */
    @Test
    public void testBuildDefault(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");

        Properties properties = SessionPropertiesBuilder.build(sessionConfig);

        assertEquals("smtp.126.com", properties.get("mail.smtp.host"));
        assertEquals("25", properties.get("mail.smtp.port"));
        assertEquals("true", properties.get("mail.smtp.auth"));
    }

    /** 端口是null或者empty的时候, 不输出 mail.smtp.port, 让 javax.mail 用默认值. */
    @Test
    public void testBuildServerPortIsNullOrEmpty(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");
        sessionConfig.setServerPort("");

        assertNull(SessionPropertiesBuilder.build(sessionConfig).get("mail.smtp.port"));
    }

    /** 不需要身份认证的时候, mail.smtp.auth 是 false. */
    @Test
    public void testBuildNotValidate(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");
        sessionConfig.setIsValidate(false);

        assertEquals("false", SessionPropertiesBuilder.build(sessionConfig).get("mail.smtp.auth"));
    }

    /** serverHost 前后的空格会被去掉, 参见 BaseConfig#getServerHost. */
    @Test
    public void testBuildServerHostTrim(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost(" smtp.126.com ");

        assertEquals("smtp.126.com", SessionPropertiesBuilder.build(sessionConfig).get("mail.smtp.host"));
    }

    @Test
    public void testBuildSmtpSSLEnable(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");
        sessionConfig.setIsSmtpSSLEnable(true);

        Properties properties = SessionPropertiesBuilder.build(sessionConfig);

        assertEquals("true", properties.get("mail.smtp.ssl.enable"));
        assertEquals("javax.net.ssl.SSLSocketFactory", properties.get("mail.smtp.socketFactory.class"));
    }

    @Test
    public void testBuildSmtpStarttlsEnable(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");
        sessionConfig.setIsSmtpStarttlsEnable(true);

        assertEquals("true", SessionPropertiesBuilder.build(sessionConfig).get("mail.smtp.starttls.enable"));
    }

    /** serverHost 是null或者empty的时候, 直接抛 IllegalArgumentException, 而不是等到发信时才报错. */
    @Test
    public void testBuildServerHostIsNullOrEmpty(){
        try{
            SessionPropertiesBuilder.build(new MailSendConnectionConfig());
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){
            assertTrue(e.getMessage().contains("serverHost can't be null/empty!"));
        }
    }

}
