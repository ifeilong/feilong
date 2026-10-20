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
package com.feilong.net.mail.builder.setter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.Test;

import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.net.mail.entity.Priority;
import com.feilong.test.AbstractTest;

/**
 * {@link HeaderSetter} 的单元测试, 不依赖网络.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class HeaderSetterTest extends AbstractTest{

    @Test
    public void testSetHeaders() throws MessagingException{
        Message message = newMimeMessage();

        MailSendRequest mailSendRequest = new MailSendRequest();
        mailSendRequest.setPriority(Priority.HIGHEST);
        mailSendRequest.setIsNeedReturnReceipt(true);

        HeaderSetter.setHeaders(message, mailSendRequest);

        assertEquals(Priority.HIGHEST.getLevelValue(), message.getHeader("X-Priority")[0]);
        assertEquals("1", message.getHeader("Disposition-Notification-To")[0]);
        assertTrue(message.getHeader("X-mailer")[0].startsWith("FeiLong Mail Api"));
    }

    /** 没有设置优先级、也不需要回执的时候, 只输出邮件客户端版本. */
    @Test
    public void testSetHeadersDefault() throws MessagingException{
        Message message = newMimeMessage();

        HeaderSetter.setHeaders(message, new MailSendRequest());

        assertNull(message.getHeader("X-Priority"));
        assertNull(message.getHeader("Disposition-Notification-To"));
        assertNotNull(message.getHeader("X-mailer"));
    }

    //---------------------------------------------------------------

    private static Message newMimeMessage(){
        return new MimeMessage(Session.getInstance(new Properties()));
    }

}
