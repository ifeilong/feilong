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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.lang.StringUtil.formatPattern;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.lib.lang3.ArrayUtils;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.net.mail.util.InternetAddressUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 设置相关接收方, 含 TO,抄送CC,密送 BCC.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RecipientsSetter{

    /**
     * 设置邮件接受人群.
     * 
     * <p>
     * 支持 to cc bcc.
     * </p>
     *
     * @param message
     *            the message
     * @param mailSendRequest
     *            the new recipients
     */
    public static void setRecipients(Message message,MailSendRequest mailSendRequest){
        // 创建邮件的接收者地址,并设置到邮件消息中
        // Message.RecipientType.TO属性表示接收者的类型为TO
        set(message, Message.RecipientType.TO, mailSendRequest.getTos());
        //cc 抄送
        String[] cc = buildCC(mailSendRequest.getCcs(), mailSendRequest.getFromAddress(), mailSendRequest.getIsDefaultCcSelf());
        set(message, Message.RecipientType.CC, cc);
        //bcc 密送
        set(message, Message.RecipientType.BCC, mailSendRequest.getBccs());
    }

    //---------------------------------------------------------------

    /**
     * 解决 163邮箱 554 DT:SPM smtp12异常问题.
     *
     * @param ccs
     *            the ccs
     * @param fromAddress
     *            the from address
     * @param isDefaultCcSelf
     *            the is default cc self
     * @return the string[]
     * @since 2.1.0
     */
    private static String[] buildCC(String[] ccs,String fromAddress,boolean isDefaultCcSelf){
        //如果默认不追加自己,那么直接返回 ccs 不管里面有没有自己
        if (!isDefaultCcSelf){
            return ccs;
        }
        //---------------------------------------------------------------
        //如果cc已经有了自己, 那么直接返回cc
        if (ArrayUtils.contains(ccs, fromAddress)){
            return ccs;
        }
        //如果没有自己, 那么追加一个
        return ArrayUtils.add(ccs, fromAddress);
    }

    //---------------------------------------------------------------

    /**
     * 设置.
     *
     * @param message
     *            the message
     * @param recipientType
     *            the recipient type
     * @param addressArray
     *            the address array
     */
    private static void set(Message message,RecipientType recipientType,String[] addressArray){
        try{
            if (isNotNullOrEmpty(addressArray)){
                message.setRecipients(recipientType, InternetAddressUtil.toAddressArray(addressArray));
            }
        }catch (MessagingException e){
            //since 1.13.2
            throw new DefaultRuntimeException(formatPattern("addressArray:[{}],recipientType:[{}]", addressArray, recipientType), e);
        }
    }
}
