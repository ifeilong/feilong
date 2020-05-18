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
package com.feilong.net.mail.setter;

import static com.feilong.core.date.DateUtil.now;

import javax.mail.Message;
import javax.mail.MessagingException;

import com.feilong.net.mail.MailHeader;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.net.mail.entity.Priority;

/**
 * 专门用来设置header.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class HeaderSetter{

    /** Don't let anyone instantiate this class. */
    private HeaderSetter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

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
            message.addHeader(MailHeader.X_PRIORITY, priority.getLevelValue());
        }

        // 是否需要回执
        if (mailSendRequest.getIsNeedReturnReceipt()){
            message.setHeader(MailHeader.DISPOSITION_NOTIFICATION_TO, "1");
        }

        //---------------------------------------------------------------
        // 邮件客户端
        message.setHeader(MailHeader.X_MAILER, MailHeader.X_MAILER_VALUE);

        // 设置邮件消息发送的时间
        message.setSentDate(now());
    }

}
