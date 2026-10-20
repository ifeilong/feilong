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
import static org.junit.Assert.fail;

import org.junit.Test;

import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.test.AbstractTest;

/**
 * {@link DefaultMailSender} 的单元测试, 只覆盖参数校验, 不依赖网络.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see com.feilong.net.mail.DefaultMailSender#send(MailSendRequest)
 */
public class DefaultMailSenderTest extends AbstractTest{

    @Test
    public void testSendMailSendRequestIsNull(){
        DefaultMailSender mailSender = new DefaultMailSender(new MailSendConnectionConfig());

        try{
            mailSender.send(null);
            fail("should throw NullPointerException");
        }catch (NullPointerException e){
            assertEquals("mailSenderConfig can't be null!", e.getMessage());
        }
    }

    /** 没有绑定连接配置的时候, 应该抛带说明的 NullPointerException, 而不是在解引用时抛无上下文的NPE. */
    @Test
    public void testSendMailSendConnectionConfigIsNull(){
        DefaultMailSender mailSender = new DefaultMailSender();

        try{
            mailSender.send(new MailSendRequest());
            fail("should throw NullPointerException");
        }catch (NullPointerException e){
            assertEquals("mailSendConnectionConfig can't be null!", e.getMessage());
        }
    }

}
