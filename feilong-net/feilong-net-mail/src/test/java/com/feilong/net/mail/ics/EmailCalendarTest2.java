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
package com.feilong.net.mail.ics;

import java.util.UUID;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.junit.After;
import org.junit.Test;

import com.feilong.net.mail.AbstractMailSenderTest;
import com.feilong.net.mail.SessionFactory;

public class EmailCalendarTest2 extends AbstractMailSenderTest{

    @Test
    public void send() throws Exception{
        mailSendRequest.setContent("hello hahaha");

        //---------------------------------------------------------------
        String fromEmail = "feilongtestemail@163.com";
        Session session = SessionFactory.createSession(mailSendConnectionConfig);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject("Outlook Meeting Request Using JavaMail");

        //---------------------------------------------------------------
        BodyPart messageBodyPart = new MimeBodyPart();
        // 测试下来如果不这么转换的话，会以纯文本的形式发送过去，  
        //如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求  
        messageBodyPart.setDataHandler(
                        new DataHandler(new ByteArrayDataSource(buildContent(toEmail), "text/calendar;method=REQUEST;charset=\"UTF-8\"")));

        Multipart multipart = new MimeMultipart();

        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart bodyPart = new MimeBodyPart();
        // 设置HTML内容
        // 设置邮件消息的主要内容
        bodyPart.setContent(mailSendRequest.getContent(), mailSendRequest.getContentMimeType());
        multipart.addBodyPart(bodyPart);

        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        //---------------------------------------------------------------
        Transport.send(message);
    }

    //---------------------------------------------------------------

    private String buildContent(String toEmail){
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                        "BEGIN:VCALENDAR\n" + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" + "VERSION:2.0\n"
                                        + "METHOD:REQUEST\n" + "BEGIN:VEVENT\n" + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"
                                        + toEmail + "\n" + "ORGANIZER:MAILTO:" + toEmail + "\n" + "DTSTART:20180302T060000Z\n"
                                        + "DTEND:20180302T070000Z\n" + "LOCATION:Conference room\n" + "UID:" + UUID.randomUUID().toString()
                                        + "\n"//如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。  
                                        + "CATEGORIES:SuccessCentral Reminder\n"
                                        + "DESCRIPTION:This the description of the meeting.<br>asd;flkjasdpfi\n\n"
                                        + "SUMMARY:Test meeting request\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
                                        + "TRIGGER:-PT15M\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:Reminder\n" + "END:VALARM\n"
                                        + "END:VEVENT\n" + "END:VCALENDAR");
        return buffer.toString();
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.mail.AbstractMailSenderTest#after()
     */
    @Override
    @After
    public void after(){
    }
}