package com.feilong.net.mail.ics;

import static com.feilong.core.date.DateUtil.now;

import java.io.IOException;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.junit.Test;

import com.feilong.core.CharsetType;
import com.feilong.core.date.DateUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.lib.lang3.SystemUtils;
import com.feilong.net.mail.AbstractMailSenderTest;
import com.feilong.net.mail.builder.MessageBuilder;
import com.feilong.net.mail.entity.MailSendRequest;
import com.feilong.net.mail.util.MessageSendUtil;
import com.feilong.net.mail.util.MimeType;

public class Email1 extends AbstractMailSenderTest{

    @Test
    public void send() throws Exception{
        mailSendRequest.setiCalendar(ICalendarBuilder.build());

        Message message = MessageBuilder.build(mailSendRequest, mailSendConnectionConfig);
        if (null != mailSendRequest.getiCalendar()){
            message.setContent(buildContent(mailSendRequest));
        }
        MessageSendUtil.send(message);
    }

    static Multipart buildContent(MailSendRequest mailSendRequest) throws MessagingException,IOException{
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(build(mailSendRequest));
        return multipart;
    }

    static BodyPart build(MailSendRequest mailSendRequest) throws MessagingException,IOException{
        String ics = IcsBuilder.buildIcs(mailSendRequest);

        IOWriteUtil.writeStringToFile(SystemUtils.USER_HOME + "/feilong/email/" + DateUtil.getTime(now()) + ".ics", ics, CharsetType.UTF8);

        //测试下来如果不这么转换的话，会以纯文本的形式发送过去，  
        //如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求  

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(ics, MimeType.TYPE_ICS)));

        return messageBodyPart;
    }
}