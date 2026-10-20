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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.mail.Session;

import org.junit.Test;

import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.test.AbstractTest;

/**
 * {@link SessionFactory} 的单元测试, 不依赖网络(只构造 Session, 不建立连接).
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class SessionFactoryTest extends AbstractTest{

    @Test
    public void testCreateSession(){
        MailSendConnectionConfig sessionConfig = new MailSendConnectionConfig();
        sessionConfig.setServerHost("smtp.126.com");
        sessionConfig.setUserName("feilong");
        sessionConfig.setPassword("123456");
        sessionConfig.setIsDebug(true);

        Session session = SessionFactory.createSession(sessionConfig);

        assertNotNull(session);
        //session 是 javax.mail 的全局默认实例, 但 debug 每次调用都会重新设置
        assertTrue(session.getDebug());
    }

    @Test
    public void testCreateSessionNull(){
        try{
            SessionFactory.createSession(null);
            fail("should throw NullPointerException");
        }catch (NullPointerException e){
            assertEquals("sessionConfig can't be null!", e.getMessage());
        }
    }

    /** serverHost 是null或者empty的时候, 在构造 Session 的时候就失败, 而不是等到发信. */
    @Test
    public void testCreateSessionServerHostIsNullOrEmpty(){
        try{
            SessionFactory.createSession(new MailSendConnectionConfig());
            fail("should throw IllegalArgumentException");
        }catch (IllegalArgumentException e){
            assertTrue(e.getMessage().contains("serverHost can't be null/empty!"));
        }
    }

}
