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
package com.feilong.net.mail.entity;

import java.util.Date;

/**
 * 邮件信息.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.9
 */
public class MailInfo{

    /** 发件人. */
    private String   from;

    /** 发送时间. */
    private Date     sentDate;

    /** 大小. */
    private String   size;

    /** 标题. */
    private String   subject;

    /** 收件人. */
    private String[] recipients;

    //---------------------------------------------------------------

    /** The content type. */
    private String   contentType;

    /** 内容. */
    private String   content;

    /** 接收时间. */
    private Date     receivedDate;

    //---------------------------------------------------------------

    /**
     * 获得 发件人.
     *
     * @return the from
     */
    public String getFrom(){
        return from;
    }

    /**
     * 设置 发件人.
     *
     * @param from
     *            the from to set
     */
    public void setFrom(String from){
        this.from = from;
    }

    /**
     * 获得 发送时间.
     *
     * @return the sentDate
     */
    public Date getSentDate(){
        return sentDate;
    }

    /**
     * 设置 发送时间.
     *
     * @param sentDate
     *            the sentDate to set
     */
    public void setSentDate(Date sentDate){
        this.sentDate = sentDate;
    }

    /**
     * 获得 大小.
     *
     * @return the size
     */
    public String getSize(){
        return size;
    }

    /**
     * 设置 大小.
     *
     * @param size
     *            the size to set
     */
    public void setSize(String size){
        this.size = size;
    }

    /**
     * 获得 标题.
     *
     * @return the subject
     */
    public String getSubject(){
        return subject;
    }

    /**
     * 设置 标题.
     *
     * @param subject
     *            the subject to set
     */
    public void setSubject(String subject){
        this.subject = subject;
    }

    /**
     * 获得 收件人.
     *
     * @return the recipients
     */
    public String[] getRecipients(){
        return recipients;
    }

    /**
     * 设置 收件人.
     *
     * @param recipients
     *            the recipients to set
     */
    public void setRecipients(String[] recipients){
        this.recipients = recipients;
    }

    /**
     * 获得 content type.
     *
     * @return the contentType
     */
    public String getContentType(){
        return contentType;
    }

    /**
     * 设置 content type.
     *
     * @param contentType
     *            the contentType to set
     */
    public void setContentType(String contentType){
        this.contentType = contentType;
    }

    /**
     * 获得 内容.
     *
     * @return the content
     */
    public String getContent(){
        return content;
    }

    /**
     * 设置 内容.
     *
     * @param content
     *            the content to set
     */
    public void setContent(String content){
        this.content = content;
    }

    /**
     * 获得 接收时间.
     *
     * @return the receivedDate
     */
    public Date getReceivedDate(){
        return receivedDate;
    }

    /**
     * 设置 接收时间.
     *
     * @param receivedDate
     *            the receivedDate to set
     */
    public void setReceivedDate(Date receivedDate){
        this.receivedDate = receivedDate;
    }

}
