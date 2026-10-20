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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.junit.Test;

import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.test.AbstractTest;

/**
 * {@link MessageBuilder} 的单元测试, 不依赖网络(只构造 MimeMessage, 不发送).
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
public class MessageBuilderTest extends AbstractTest{

    @Test
    public void testBuild() throws MessagingException,IOException{
        MailSendRequest mailSendRequest = new MailSendRequest();
        mailSendRequest.setFromAddress("from@126.com");
        mailSendRequest.setSubject("测试邮件");
        mailSendRequest.setContent("<b>hello</b>");
        mailSendRequest.setTos("to@126.com");
        mailSendRequest.setCcs("cc@126.com");
        mailSendRequest.setBccs("bcc@126.com");

        Message message = MessageBuilder.build(mailSendRequest, buildConnectionConfig());

        assertNotNull(message);
        assertEquals("测试邮件", message.getSubject());
        assertNotNull(message.getSentDate());

        //from
        Address[] from = message.getFrom();
        assertEquals(1, from.length);
        assertEquals("from@126.com", ((InternetAddress) from[0]).getAddress());

        //to
        Address[] to = message.getRecipients(Message.RecipientType.TO);
        assertEquals(1, to.length);
        assertEquals("to@126.com", ((InternetAddress) to[0]).getAddress());

        //cc: 默认会把自己(发件人)抄送进去, 避免163邮箱的 554 DT:SPM 问题
        Address[] cc = message.getRecipients(Message.RecipientType.CC);
        assertEquals(2, cc.length);
        assertEquals("cc@126.com", ((InternetAddress) cc[0]).getAddress());
        assertEquals("from@126.com", ((InternetAddress) cc[1]).getAddress());

        //bcc
        Address[] bcc = message.getRecipients(Message.RecipientType.BCC);
        assertEquals(1, bcc.length);
        assertEquals("bcc@126.com", ((InternetAddress) bcc[0]).getAddress());

        //body
        assertTrue(message.getContent() instanceof MimeMultipart);
        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
        assertEquals(1, mimeMultipart.getCount());
        assertEquals("<b>hello</b>", mimeMultipart.getBodyPart(0).getContent());
    }

    /** isDefaultCcSelf 是false的时候, 不会自动把自己加到抄送里. */
    @Test
    public void testBuildNotDefaultCcSelf() throws MessagingException{
        MailSendRequest mailSendRequest = new MailSendRequest();
        mailSendRequest.setFromAddress("from@126.com");
        mailSendRequest.setSubject("测试邮件");
        mailSendRequest.setTos("to@126.com");
        mailSendRequest.setCcs("cc@126.com");
        mailSendRequest.setIsDefaultCcSelf(false);

        Message message = MessageBuilder.build(mailSendRequest, buildConnectionConfig());

        Address[] cc = message.getRecipients(Message.RecipientType.CC);
        assertEquals(1, cc.length);
        assertEquals("cc@126.com", ((InternetAddress) cc[0]).getAddress());
    }

    //---------------------------------------------------------------

    private static MailSendConnectionConfig buildConnectionConfig(){
        MailSendConnectionConfig mailSendConnectionConfig = new MailSendConnectionConfig();
        mailSendConnectionConfig.setServerHost("smtp.126.com");
        mailSendConnectionConfig.setUserName("feilong");
        mailSendConnectionConfig.setPassword("123456");
        return mailSendConnectionConfig;
    }

}
