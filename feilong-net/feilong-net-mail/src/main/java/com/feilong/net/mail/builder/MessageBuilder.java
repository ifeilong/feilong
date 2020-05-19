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

import static com.feilong.core.date.DateUtil.now;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.feilong.json.JsonUtil;
import com.feilong.net.mail.SessionFactory;
import com.feilong.net.mail.builder.setter.BodySetter;
import com.feilong.net.mail.builder.setter.HeaderSetter;
import com.feilong.net.mail.builder.setter.RecipientsSetter;
import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.net.mail.exception.MailException;
import com.feilong.net.mail.util.InternetAddressUtil;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 专门用来构造 {@link Message}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class MessageBuilder{

    /** Don't let anyone instantiate this class. */
    private MessageBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 构造Message.
     * 
     * <p>
     * 基于 mailSenderConfig 构建 通用的 MimeMessage,然后设置message公共属性(含发件人 收件人等信息)
     * </p>
     *
     * @param mailSendRequest
     *            the mail sender config
     * @param mailSendConnectionConfig
     *            the mail send connection config
     * @return the message
     * @since 1.10.2
     */
    public static Message build(MailSendRequest mailSendRequest,MailSendConnectionConfig mailSendConnectionConfig){
        // 根据session创建一个邮件消息

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = SessionFactory.createSession(mailSendConnectionConfig);

        //---------------------------------------------------------------
        // 根据session创建一个邮件消息
        Message message = new MimeMessage(session);
        try{
            //mail.smtp.from  String  Email address to use for SMTP MAIL command. 
            //This sets the envelope return address. 
            //Defaults to msg.getFrom() or InternetAddress.getLocalAddress(). 
            //NOTE: mail.smtp.user was previously used for this.
            message.setFrom(InternetAddressUtil.buildFromAddress(mailSendRequest.getPersonal(), mailSendRequest.getFromAddress()));

            setMessageAttribute(message, mailSendRequest);

            BodySetter.setBody(message, mailSendRequest);
        }catch (MessagingException e){
            //since 1.13.2 update exception message
            throw new MailException(Slf4jUtil.format("mailSenderConfig:[{}]", JsonUtil.format(mailSendRequest)), e);
        }
        return message;
    }

    //---------------------------------------------------------------

    /**
     * 设置message公共属性.
     * 
     * <h3>公共属性</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>设置发送人</li>
     * <li>设置邮件接受人群(to cc bcc)</li>
     * <li>设置邮件消息的主题</li>
     * <li>header信息</li>
     * </ol>
     * </blockquote>
     *
     * @param message
     *            the message
     * @param mailSendRequest
     *            the mail sender config
     * @throws MessagingException
     *             the messaging exception
     * @see RecipientsSetter#setRecipients(Message, MailSendRequest)
     * @see HeaderSetter#setHeaders(Message, MailSendRequest)
     */
    private static void setMessageAttribute(Message message,MailSendRequest mailSendRequest) throws MessagingException{
        // 设置邮件接受人群
        // 支持 to cc bcc
        RecipientsSetter.setRecipients(message, mailSendRequest);

        //---------------------------------------------------------------
        // 设置邮件消息的主题
        message.setSubject(mailSendRequest.getSubject());

        //header信息
        HeaderSetter.setHeaders(message, mailSendRequest);

        // 设置邮件消息发送的时间
        message.setSentDate(now());
    }
}
