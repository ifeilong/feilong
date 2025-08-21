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

    private MailSendConnectionConfig mailSendConnectionConfig;

    //---------------------------------------------------------------
    /**
     * 
     */
    public DefaultMailSender(){
    }

    /**
     * @param mailSendConnectionConfig
     */
    public DefaultMailSender(MailSendConnectionConfig mailSendConnectionConfig){
        this.mailSendConnectionConfig = mailSendConnectionConfig;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.mail.MailSender#sendMail(com.feilong.tools.mail.entity.MailSenderConfig)
     */
    @Override
    public void send(MailSendRequest mailSendRequest){
        Validate.notNull(mailSendRequest, "mailSenderConfig can't be null!");

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
     * @param mailSendConnectionConfig
     *            the mailSendConnectionConfig to set
     */
    public void setMailSendConnectionConfig(MailSendConnectionConfig mailSendConnectionConfig){
        this.mailSendConnectionConfig = mailSendConnectionConfig;
    }

}