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

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.jsonlib.JsonUtil;
import com.feilong.net.mail.builer.MessageBuilder;
import com.feilong.net.mail.entity.MailSenderConfig;
import com.feilong.net.mail.util.MessageSendUtil;

/**
 * 邮件发送器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.mail.Message
 * @see javax.mail.Session
 * @see "org.springframework.mail.MailSender"
 * @see "org.springframework.mail.javamail.JavaMailSenderImpl"
 * @see javax.mail.Transport#send(javax.mail.Message)
 * @since 1.1.1
 */
public final class DefaultMailSender implements MailSender{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMailSender.class);

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.mail.MailSender#sendMail(com.feilong.tools.mail.entity.MailSenderConfig)
     */
    @Override
    public void sendMail(MailSenderConfig mailSenderConfig){
        Validate.notNull(mailSenderConfig, "mailSenderConfig can't be null!");

        Validate.notBlank(mailSenderConfig.getUserName(), "mailSenderConfig.getUserName() can't be null!");
        Validate.notBlank(mailSenderConfig.getPassword(), "mailSenderConfig.getPassword() can't be null!");
        Validate.notEmpty(mailSenderConfig.getTos(), "mailSenderConfig.getTos() can't be null!");
        Validate.notBlank(mailSenderConfig.getFromAddress(), "mailSenderConfig.getFromAddress() can't be null!");
        Validate.notBlank(mailSenderConfig.getSubject(), "mailSenderConfig.getSubject() can't be null!");

        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("mailSenderConfig:{}", JsonUtil.format(mailSenderConfig));
        }

        //---------------------------------------------------------------
        Message message = MessageBuilder.build(mailSenderConfig);
        MessageSendUtil.send(message);
    }

}