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
package com.feilong.net.mail.util;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY;
import static com.feilong.core.lang.StringUtil.formatPattern;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.MapUtil.newLinkedHashMap;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

import com.feilong.core.lang.StringUtil;
import com.feilong.io.FileUtil;
import com.feilong.net.mail.entity.MailInfo;
import com.feilong.net.mail.exception.MailException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The Class MessageUtil.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.9
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessageUtil{

    /**
     * To mail info list.
     *
     * @param messages
     *            the messages
     * @return the list
     * @throws MessagingException
     *             the messaging exception
     */
    public static final List<MailInfo> toMailInfoList(Message[] messages) throws MessagingException{
        int messagesLength = messages.length;
        log.info("messages length:[{}]", messagesLength);
        //---------------------------------------------------------------
        List<MailInfo> list = newArrayList();
        for (int i = 0; i < messagesLength; ++i){
            Message message = messages[i];

            String format = StringUtil.format("%0" + String.valueOf(messagesLength).length() + "d", i + 1);
            String pattern = "[{}/{}] start convert Message to MailInfo,from:[{}],contentType:[{}],subject:[{}]";
            log.info(pattern, format, messagesLength, getFromAddress(message), message.getContentType(), message.getSubject());

            list.add(toMailInfoList(message));
        }
        return list;
    }

    /**
     * To mail info list.
     *
     * @param message
     *            the message
     * @return the mail info
     * @throws MessagingException
     *             the messaging exception
     */
    private static final MailInfo toMailInfoList(Message message) throws MessagingException{
        String from = getFromAddress(message);
        String subject = message.getSubject();
        try{
            MailInfo mailInfo = new MailInfo();

            mailInfo.setFrom(from);

            mailInfo.setContentType(message.getContentType());
            mailInfo.setContent(getContent(message));
            mailInfo.setReceivedDate(message.getReceivedDate());

            mailInfo.setRecipients(toRecipients(message.getAllRecipients()));
            mailInfo.setSentDate(message.getSentDate());
            mailInfo.setSize(FileUtil.formatSize(message.getSize()));
            mailInfo.setSubject(subject);
            return mailInfo;
        }catch (Exception e){
            throw new MailException(formatPattern("from:[{}],subject:[{}]", from, subject), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * To recipients.
     *
     * @param allRecipients
     *            the all recipients
     * @return 如果 <code>allRecipients</code> 是null或者empty,返回 {@link com.feilong.core.lang.ArrayUtil#EMPTY_STRING_ARRAY}<br>
     * @since 1.9.0
     */
    private static String[] toRecipients(Address[] allRecipients){
        if (isNullOrEmpty(allRecipients)){
            return EMPTY_STRING_ARRAY;
        }
        int length = allRecipients.length;
        String[] recipients = new String[length];
        for (int i = 0, j = length; i < j; ++i){
            recipients[i] = getAddress(allRecipients[i]);
        }
        return recipients;
    }

    //---------------------------------------------------------------

    /**
     * 获得 map for log.
     *
     * @param message
     *            the message
     * @return the map for log
     */
    public static final Map<String, Object> getMapForLog(Message message){
        try{
            Map<String, Object> map = newLinkedHashMap();
            map.put("from", getFromAddress(message));
            map.put("sentDate", message.getSentDate());
            map.put("size", FileUtil.formatSize(message.getSize()));
            map.put("subject", message.getSubject());

            map.put("allHeaders", message.getAllHeaders());
            map.put("allRecipients", message.getAllRecipients());
            map.put("contentType", message.getContentType());
            map.put("description", message.getDescription());
            map.put("getFileName", message.getFileName());
            map.put("getFlags", message.getFlags().toString());
            map.put("getFolder", message.getFolder().getFullName());
            map.put("getLineCount", message.getLineCount());
            map.put("getMessageNumber", message.getMessageNumber());
            map.put("getReceivedDate", message.getReceivedDate());
            map.put("getReplyTo", message.getReplyTo());
            return map;
        }catch (Exception e){
            throw new MailException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 获得 from address.
     *
     * @param message
     *            the message
     * @return the from address
     * @since 1.0.9
     */
    private static String getFromAddress(Message message){
        try{
            Address[] from = message.getFrom();
            return getAddress(from[0]);
        }catch (Exception e){
            throw new MailException(e);
        }
    }

    /**
     * 获得 address.
     *
     * @param address
     *            the address
     * @return the address
     * @since 1.0.9
     */
    private static String getAddress(Address address){
        InternetAddress internetAddress = (InternetAddress) address;
        return internetAddress.getAddress();
    }

    /**
     * 获得 content.
     *
     * @param part
     *            the part
     * @return the content
     */
    private static String getContent(Part part){
        try{
            // Using isMimeType to determine the content type 
            // avoids fetching the actual content data until we need it.

            //用于标准化地表示的文本信息,文本消息可以是多种字符集和或者多种格式的
            if (part.isMimeType(MimeType.TEXT_ALL)){
                return doWithTextAll(part);
            }
            //用于连接消息体的多个部分构成一个消息,这些部分可以是不同类型的数据
            else if (part.isMimeType(MimeType.MULTIPART_ALL)){
                return doWithMultipartAll(part);
            }
            //用于包装一个E-mail消息
            else if (part.isMimeType(MimeType.MESSAGE_RFC822)){
                log.debug("content mimeType:[{}],match with:--->[{}]", part.getContentType(), MimeType.MESSAGE_RFC822);
                return getContent((Part) part.getContent());
            }else{
                log.debug("part getContentType:{}", part.getContentType());
                return null;
            }
        }catch (Exception e){
            throw new MailException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Do with text all.
     *
     * @param part
     *            the part
     * @return the string
     * @throws MessagingException
     *             the messaging exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @since 1.8.3
     */
    private static String doWithTextAll(Part part) throws MessagingException,IOException{
        //com.sun.mail.imap.IMAPMessage cannot be cast to javax.mail.internet.MimeBodyPart

        //see 2016年9月7日(星期三) 下午5:21 yang.wang email
        Object content = part.getContent();

        String pattern = "content mimeType:[{}],match with:--->[{}],getContent value:[{}]";
        log.debug(pattern, part.getContentType(), MimeType.TEXT_ALL, content);

        return (String) content;
    }

    //---------------------------------------------------------------

    /**
     * Do with multipart all.
     *
     * @param part
     *            the part
     * @return the string
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws MessagingException
     *             the messaging exception
     * @since 1.8.3
     */
    private static String doWithMultipartAll(Part part) throws IOException,MessagingException{
        Multipart multipart = (Multipart) part.getContent();
        int count = multipart.getCount();

        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("content mimeType:[{}],match with:--->[{}],count:[{}]", multipart.getContentType(), MimeType.MULTIPART_ALL, count);
        }
        //---------------------------------------------------------------
        boolean alternativeFlag = part.isMimeType(MimeType.MULTIPART_ALTERNATIVE);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++){
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (isNeedAppend(bodyPart, alternativeFlag)){
                sb.append(getContent(bodyPart));
            }
        }
        return sb.toString();
    }

    /**
     * Checks if is need append.
     *
     * @param bodyPart
     *            the body part
     * @param alternativeFlag
     *            the alternative flag
     * @return true, if is need append
     * @throws MessagingException
     *             the messaging exception
     */
    private static boolean isNeedAppend(BodyPart bodyPart,boolean alternativeFlag) throws MessagingException{
        //不是 alternative 或者(是 alternative且是html)
        if (!alternativeFlag){
            return true;
        }
        return bodyPart.isMimeType(MimeType.TEXT_HTML);
    }
}
