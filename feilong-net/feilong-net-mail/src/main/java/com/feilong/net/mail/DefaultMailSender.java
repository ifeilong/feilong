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

import javax.mail.Message;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.net.mail.builder.MessageBuilder;
import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.net.mail.util.MessageSendUtil;

/**
 * 邮件发送器.
 * 
 * <p>
 * 这是一个<b>有状态</b>的对象: 连接配置({@link MailSendConnectionConfig})在构造方法或者
 * {@link #setMailSendConnectionConfig(MailSendConnectionConfig)} 里绑定, 绑定之后同一个实例可以反复调用
 * {@link #send(MailSendRequest)} 发送多封邮件, 因此不要在发送过程中修改配置.
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see javax.mail.Message
 * @see javax.mail.Session
 * @see "org.springframework.mail.MailSender"
 * @see "org.springframework.mail.javamail.JavaMailSenderImpl"
 * @see javax.mail.Transport#send(javax.mail.Message)
 * @since 1.1.1
 */
@lombok.extern.slf4j.Slf4j
public final class DefaultMailSender implements MailSender{

    /** 邮件发送连接配置. */
    private MailSendConnectionConfig mailSendConnectionConfig;

    //---------------------------------------------------------------

    /**
     * 构造一个还没有绑定连接配置的发送器 (Spring 的 setter 注入走的就是这个构造方法, 之后需要调用
     * {@link #setMailSendConnectionConfig(MailSendConnectionConfig)}).
     */
    public DefaultMailSender(){
    }

    /**
     * 构造一个绑定好连接配置的发送器.
     *
     * @param mailSendConnectionConfig
     *            the mail send connection config, 可以是null, 但此时调用 {@link #send(MailSendRequest)} 会抛出
     *            {@link NullPointerException}
     */
    public DefaultMailSender(MailSendConnectionConfig mailSendConnectionConfig){
        this.mailSendConnectionConfig = mailSendConnectionConfig;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.mail.MailSender#send(MailSendRequest)
     */
    @Override
    public void send(MailSendRequest mailSendRequest){
        Validate.notNull(mailSendRequest, "mailSenderConfig can't be null!");

        Validate.notNull(mailSendConnectionConfig, "mailSendConnectionConfig can't be null!");
        Validate.notBlank(mailSendConnectionConfig.getUserName(), "mailSenderConfig.getUserName() can't be null!");
        Validate.notBlank(mailSendConnectionConfig.getPassword(), "mailSenderConfig.getPassword() can't be null!");
        Validate.notEmpty(mailSendRequest.getTos(), "mailSenderConfig.getTos() can't be null!");
        Validate.notBlank(mailSendRequest.getFromAddress(), "mailSenderConfig.getFromAddress() can't be null!");
        Validate.notBlank(mailSendRequest.getSubject(), "mailSenderConfig.getSubject() can't be null!");

        //---------------------------------------------------------------

        if (log.isDebugEnabled()){
            log.debug(
                            "mailSenderConfig:{},mailSendConnectionConfig:[{}]",
                            JsonUtil.toString(mailSendRequest),
                            JsonUtil.toString(mailSendConnectionConfig));
        }

        //---------------------------------------------------------------
        Message message = MessageBuilder.build(mailSendRequest, mailSendConnectionConfig);
        MessageSendUtil.send(message);
    }

    //---------------------------------------------------------------

    /**
     * 设置 邮件发送连接配置.
     *
     * @param mailSendConnectionConfig
     *            the mailSendConnectionConfig to set
     */
    public void setMailSendConnectionConfig(MailSendConnectionConfig mailSendConnectionConfig){
        this.mailSendConnectionConfig = mailSendConnectionConfig;
    }

}
