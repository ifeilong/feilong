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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.File;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.feilong.io.FileUtil;
import com.feilong.io.FilenameUtil;
import com.feilong.io.entity.MimeType;

/**
 * 设置附件.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.13.0
 * @since 1.13.2 move package
 */
public final class AttachmentSetter{

    /** contentId前缀. */
    public static final String PREFIX_CONTENTID = "image";

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private AttachmentSetter(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 设置附件.
     *
     * @param mimeMultipart
     *            MiniMultipart类是一个容器类,包含MimeBodyPart类型的对象
     * @param attachFilePaths
     *            the attach file paths
     * @throws MessagingException
     *             the messaging exception
     * @since 1.1.1
     */
    public static void setAttachment(MimeMultipart mimeMultipart,String[] attachFilePaths) throws MessagingException{
        // html
        if (isNullOrEmpty(attachFilePaths)){
            return;// nothing to do
        }

        //-------------------以HTML格式发送邮件 带附件的邮件图片--------------------------------------------

        //附件文件名字
        List<String> attachFileNames = newArrayList();
        List<byte[]> attachList = newArrayList();

        for (String attachFilePath : attachFilePaths){
            attachFileNames.add(FilenameUtil.getFileName(attachFilePath));
            attachList.add(FileUtil.toByteArray(new File(attachFilePath)));
        }

        //---------------------------------------------------------------
        System.setProperty("mail.mime.encodefilename", "true");

        // 用于组合文本和图片,"related"型的MimeMultipart对象  
        mimeMultipart.setSubType("related");

        for (int i = 0, j = attachList.size(); i < j; i++){
            String cid = PREFIX_CONTENTID + i;
            MimeBodyPart mimeBodyPart = buildMimeBodyPart(attachList.get(i), attachFileNames.get(i), cid);

            // 将含有附件的BodyPart加入到MimeMultipart对象中
            mimeMultipart.addBodyPart(mimeBodyPart);
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the mime body part.
     *
     * @param data
     *            the data
     * @param fileName
     *            the file name
     * @param cid
     *            the cid
     * @return the mime body part
     * @throws MessagingException
     *             the messaging exception
     * @since 1.8.2
     */
    private static MimeBodyPart buildMimeBodyPart(byte[] data,String fileName,String cid) throws MessagingException{
        String mimeType = MimeType.BIN.getMime();

        // 新建一个存放附件的BodyPart
        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        mimeBodyPart.setDataHandler(buildDataHandler(data, mimeType));
        // 加上这句将作为附件发送,否则将作为信件的文本内容
        mimeBodyPart.setFileName(fileName);
        mimeBodyPart.setContentID(cid);
        return mimeBodyPart;
    }

    //---------------------------------------------------------------

    /**
     * 构造 {@link DataHandler}.
     *
     * @param data
     *            the data
     * @param mimeType
     *            the mime type
     * @return the data handler
     * @see FileDataSource
     * @see ByteArrayDataSource
     * @since 1.8.2
     */
    private static DataHandler buildDataHandler(byte[] data,String mimeType){
        //FileDataSource
        ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(data, mimeType);
        return new DataHandler(byteArrayDataSource);
    }

}
