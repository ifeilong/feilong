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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;

import com.feilong.core.lang.StringUtil;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.test.AbstractTest;

/**
 * {@link BodySetter} 的单元测试, 不依赖网络.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class BodySetterTest extends AbstractTest{

    /** 正文是null的时候, 会被兜底成空字符串, 而不是抛异常. */
    @Test
    public void testSetBodyContentIsNull() throws MessagingException,IOException{
        Message message = newMimeMessage();

        MailSendRequest mailSendRequest = new MailSendRequest();
        mailSendRequest.setContent(null);

        BodySetter.setBody(message, mailSendRequest);

        assertEquals(StringUtil.EMPTY, mailSendRequest.getContent());
        assertTrue(message.getContent() instanceof MimeMultipart);
    }

    @Test
    public void testSetBody() throws MessagingException,IOException{
        Message message = newMimeMessage();

        MailSendRequest mailSendRequest = new MailSendRequest();
        mailSendRequest.setContent("<b>hello</b>");

        BodySetter.setBody(message, mailSendRequest);

        assertTrue(message.getContent() instanceof MimeMultipart);

        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
        assertEquals(1, mimeMultipart.getCount());
        assertEquals("<b>hello</b>", mimeMultipart.getBodyPart(0).getContent());
    }

    //---------------------------------------------------------------

    private static Message newMimeMessage(){
        return new MimeMessage(Session.getInstance(new Properties()));
    }

}
