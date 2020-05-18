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

import static com.feilong.lib.lang3.StringUtils.EMPTY;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.feilong.net.mail.entity.MailSendRequest;

/**
 * 设置 BodyPart.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.2
 */
public class BodySetter{

    /** Don't let anyone instantiate this class. */
    private BodySetter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * setBody.
     *
     * @param message
     *            the message
     * @param mailSendRequest
     *            the body
     * @throws MessagingException
     *             the messaging exception
     */
    public static void setBody(Message message,MailSendRequest mailSendRequest) throws MessagingException{
        //since 1.13.0
        if (null == mailSendRequest.getContent()){
            mailSendRequest.setContent(EMPTY);
        }

        //---------------------------------------------------------------

        // if txt
        // message.setText(mailContent);
        MimeMultipart mimeMultipart = buildContent(mailSendRequest);

        // 将MiniMultipart对象设置为邮件内容
        message.setContent(mimeMultipart);
    }

    //---------------------------------------------------------------

    /**
     * 构造邮件内容.
     *
     * @param mailSendRequest
     *            the mail sender config
     * @return the mime multipart
     * @throws MessagingException
     *             the messaging exception
     * @since 1.10.2
     */
    private static MimeMultipart buildContent(MailSendRequest mailSendRequest) throws MessagingException{
        // 以HTML格式发送邮件 (不带附件的邮件)

        // MiniMultipart类是一个容器类,包含MimeBodyPart类型的对象
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(buildHtmlContentBody(mailSendRequest));

        //------------设置附件---------------------------------------------------
        AttachmentSetter.setAttachment(mimeMultipart, mailSendRequest.getAttachFilePaths());
        return mimeMultipart;
    }

    //---------------------------------------------------------------

    /**
     * Builds the html content body.
     *
     * @param mailSendRequest
     *            the mail sender config
     * @return the body part
     * @throws MessagingException
     *             the messaging exception
     * @since 1.10.2
     */
    private static BodyPart buildHtmlContentBody(MailSendRequest mailSendRequest) throws MessagingException{
        // 创建一个包含HTML内容的MimeBodyPart
        BodyPart bodyPart = new MimeBodyPart();
        // 设置HTML内容
        bodyPart.setContent(mailSendRequest.getContent(), mailSendRequest.getContentMimeType());

        return bodyPart;
    }
}
