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

import static com.feilong.core.Validator.isNullOrEmpty;

import java.io.IOException;
import java.io.UncheckedIOException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.feilong.io.FilenameUtil;
import com.feilong.io.InputStreamUtil;
import com.feilong.io.entity.MimeType;
import com.feilong.lib.io.IOUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 设置附件.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.0
 * @since 1.13.2 move package
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AttachmentSetter{

    /** contentId前缀. */
    public static final String PREFIX_CONTENTID = "image";

    //---------------------------------------------------------------

    /**
     * 设置附件.
     *
     * @param mimeMultipart
     *            MiniMultipart类是一个容器类,包含MimeBodyPart类型的对象
     * @param attachFileLocations
     *            the attach file paths
     * @throws MessagingException
     *             the messaging exception
     * @since 1.1.1
     */
    public static void setAttachment(MimeMultipart mimeMultipart,String[] attachFileLocations) throws MessagingException{
        if (isNullOrEmpty(attachFileLocations)){
            return;// nothing to do
        }

        //-------------------以HTML格式发送邮件 带附件的邮件图片--------------------------------------------
        System.setProperty("mail.mime.encodefilename", "true");

        // 用于组合文本和图片,"related"型的MimeMultipart对象  
        mimeMultipart.setSubType("related");

        int i = 0;
        for (String attachFileLocation : attachFileLocations){
            //附件文件名字
            String fileName = FilenameUtil.getFileName(attachFileLocation);

            try{
                byte[] byteArray = IOUtils.toByteArray(InputStreamUtil.getInputStream(attachFileLocation));

                // 将含有附件的BodyPart加入到MimeMultipart对象中
                mimeMultipart.addBodyPart(buildMimeBodyPart(fileName, byteArray, PREFIX_CONTENTID + i));
                i++;
            }catch (IOException e){
                throw new UncheckedIOException(e);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * Builds the mime body part.
     * 
     * @param fileName
     *            the file name
     * @param data
     *            the data
     * @param cid
     *            the cid
     *
     * @return the mime body part
     * @throws MessagingException
     *             the messaging exception
     * @since 1.8.2
     */
    private static MimeBodyPart buildMimeBodyPart(String fileName,byte[] data,String cid) throws MessagingException{
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
