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

import javax.mail.Message;
import javax.mail.MessagingException;

import com.feilong.net.mail.FeiLongMailVersion;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.net.mail.entity.Priority;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 专门用来设置header.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderSetter{

    /** 是否需要回执. */
    private static final String DISPOSITION_NOTIFICATION_TO = "Disposition-Notification-To";

    /** 邮件的优先级. */
    private static final String X_PRIORITY                  = "X-Priority";

    //---------------------------------------------------------------

    /** 邮件客户端 版本. */
    private static final String X_MAILER                    = "X-mailer";

    /** 邮件客户端 版本. */
    private static final String X_MAILER_VALUE              = "FeiLong Mail Api " + FeiLongMailVersion.getVersion();

    //---------------------------------------------------------------

    /**
     * 设置 header 信息.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>邮件的优先级</li>
     * <li>是否需要回执</li>
     * <li>邮件客户端</li>
     * <li>邮件消息发送的时间</li>
     * </ol>
     * </blockquote>
     *
     * @param message
     *            the message
     * @param mailSendRequest
     *            the headers
     * @throws MessagingException
     *             the messaging exception
     * @see javax.mail.Part#addHeader(String, String)
     * @see javax.mail.Part#setHeader(String, String)
     */
    public static void setHeaders(Message message,MailSendRequest mailSendRequest) throws MessagingException{
        // 邮件的优先级
        Priority priority = mailSendRequest.getPriority();
        if (null != priority){
            message.addHeader(X_PRIORITY, priority.getLevelValue());
        }

        // 是否需要回执
        if (mailSendRequest.getIsNeedReturnReceipt()){
            message.setHeader(DISPOSITION_NOTIFICATION_TO, "1");
        }

        //---------------------------------------------------------------
        // 邮件客户端
        message.setHeader(X_MAILER, X_MAILER_VALUE);

    }

}
