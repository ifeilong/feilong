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
package com.feilong.net.mail.builer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.mail.SessionFactory;
import com.feilong.net.mail.entity.MailSenderConfig;
import com.feilong.net.mail.exception.MailSenderException;
import com.feilong.net.mail.setter.BodySetter;
import com.feilong.net.mail.setter.HeaderSetter;
import com.feilong.net.mail.setter.RecipientsSetter;
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
     * @param mailSenderConfig
     *            the mail sender config
     * @return the message
     * @since 1.10.2
     */
    public static Message build(MailSenderConfig mailSenderConfig){
        // 根据session创建一个邮件消息

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = SessionFactory.createSession(mailSenderConfig);

        //---------------------------------------------------------------

        // 根据session创建一个邮件消息
        Message message = new MimeMessage(session);

        try{
            setMessageAttribute(message, mailSenderConfig);

            BodySetter.setBody(message, mailSenderConfig);
        }catch (MessagingException e){
            //since 1.13.2 update exception message
            throw new MailSenderException(Slf4jUtil.format("mailSenderConfig:[{}]", JsonUtil.format(mailSenderConfig)), e);
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
     * @param mailSenderConfig
     *            the mail sender config
     * @throws MessagingException
     *             the messaging exception
     * @see RecipientsSetter#setRecipients(Message, MailSenderConfig)
     * @see HeaderSetter#setHeaders(Message, MailSenderConfig)
     */
    private static void setMessageAttribute(Message message,MailSenderConfig mailSenderConfig) throws MessagingException{
        //mail.smtp.from  String  Email address to use for SMTP MAIL command. 
        //This sets the envelope return address. 
        //Defaults to msg.getFrom() or InternetAddress.getLocalAddress(). 
        //NOTE: mail.smtp.user was previously used for this.
        message.setFrom(InternetAddressUtil.buildFromAddress(mailSenderConfig.getPersonal(), mailSenderConfig.getFromAddress()));

        // 设置邮件接受人群
        // 支持 to cc bcc
        RecipientsSetter.setRecipients(message, mailSenderConfig);

        //---------------------------------------------------------------
        // 设置邮件消息的主题
        message.setSubject(mailSenderConfig.getSubject());

        //header信息
        HeaderSetter.setHeaders(message, mailSenderConfig);
    }

}
