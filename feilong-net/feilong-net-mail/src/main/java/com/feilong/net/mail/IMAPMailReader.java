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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;

import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import com.feilong.json.JsonUtil;
import com.feilong.net.mail.entity.MailInfo;
import com.feilong.net.mail.entity.MailReaderConfig;
import com.feilong.net.mail.exception.MailException;
import com.feilong.net.mail.util.FolderUtil;
import com.feilong.net.mail.util.MessageUtil;

/**
 * IMAP模式来读取邮件.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.9
 */
@lombok.extern.slf4j.Slf4j
public class IMAPMailReader implements MailReader{

    /** imap or pop3. */
    private static final String PROTOCOL    = "imap";

    /** The folder name. */
    private static final String FOLDER_NAME = "INBOX";

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.mail.MailReader#getMailInfoList(com.feilong.tools.mail.entity.mailReaderConfig)
     */
    @Override
    public List<MailInfo> read(MailReaderConfig mailReaderConfig){
        return read(mailReaderConfig, null, null);
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.mail.MailReader#getMailInfoList(com.feilong.tools.mail.entity.mailReaderConfig, java.lang.Integer,
     * javax.mail.search.SearchTerm)
     */
    @Override
    public List<MailInfo> read(MailReaderConfig mailReaderConfig,Integer newstIndex,SearchTerm searchTerm){
        if (log.isDebugEnabled()){
            log.debug("input mailReaderConfig:[{}],searchTerm:[{}]", JsonUtil.toString(mailReaderConfig), JsonUtil.toString(searchTerm));
        }

        //---------------------------------------------------------------

        String serverHost = mailReaderConfig.getServerHost();
        String userName = mailReaderConfig.getUserName();
        String password = mailReaderConfig.getPassword();

        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session session = SessionFactory.createSession(mailReaderConfig);

        //---------------------------------------------------------------

        Store store = null;
        Folder folder = null;
        try{
            store = session.getStore(PROTOCOL);
            store.connect(serverHost, userName, password);

            folder = getFolder(store);
            //---------------------------------------------------------------
            Message[] messages = getMessages(folder, searchTerm, newstIndex);
            return MessageUtil.toMailInfoList(messages);
        }catch (MessagingException e){
            throw new MailException(e);
        }finally{
            close(store);
            close(folder);
        }
    }

    //---------------------------------------------------------------

    /**
     * Close.
     *
     * @param store
     *            the store
     * @since 1.8.3
     */
    private static void close(Store store){
        try{
            if (null != store && store.isConnected()){
                store.close();
            }
        }catch (MessagingException e){
            throw new MailException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Close.
     *
     * @param folder
     *            the folder
     * @since 1.8.3
     */
    private static void close(Folder folder){
        try{
            if (null != folder && folder.isOpen()){
                folder.close(false); // Close connection 
            }
        }catch (MessagingException e){
            throw new MailException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 获得 folder.
     *
     * @param store
     *            the store
     * @return the folder
     * @throws MessagingException
     *             the messaging exception
     * @since 1.0.9
     */
    private static Folder getFolder(Store store) throws MessagingException{
        Folder folder = store.getFolder(FOLDER_NAME);
        folder.open(Folder.READ_ONLY);

        if (log.isDebugEnabled()){
            log.debug("folder info :{}", JsonUtil.toString(FolderUtil.getMapForLog(folder)));
        }
        return folder;
    }

    //---------------------------------------------------------------

    /**
     * 获得 messages.
     *
     * @param folder
     *            the folder
     * @param searchTerm
     *            the search term
     * @param newstIndex
     *            the newst index
     * @return the messages
     * @throws MessagingException
     *             the messaging exception
     * @since 1.0.9
     */
    private static Message[] getMessages(Folder folder,SearchTerm searchTerm,Integer newstIndex) throws MessagingException{
        // Get directory
        Message[] messages = null;
        //最近的多少条
        if (isNotNullOrEmpty(newstIndex)){
            int messageCount = folder.getMessageCount();
            int start = messageCount - newstIndex;
            start = start < 1 ? 1 : start;
            messages = folder.getMessages(start, messageCount);
        }else{
            //所有
            messages = folder.getMessages();
        }
        return isNullOrEmpty(searchTerm) ? messages : folder.search(searchTerm, messages);
    }
}
