package com.feilong.net.mail.ics;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.junit.Test;

import com.feilong.net.mail.AbstractMailSenderTest;
import com.feilong.net.mail.SessionFactory;

public class EmailCalendarTest extends AbstractMailSenderTest{

    @Test
    public void testEmailTest() throws Exception{
        String from = "feilongtestemail@163.com";
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.163.com");

        Session session = SessionFactory.createSession(mailSenderConfig);
        // Define message
        MimeMessage message = new MimeMessage(session);
        message.addHeaderLine("method=REQUEST");
        message.addHeaderLine("charset=UTF-8");
        message.addHeaderLine("component=VEVENT");

        message.setFrom(new InternetAddress(from));
        String to = AbstractMailSenderTest.toEmail;
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Outlook Meeting Request Using JavaMail");

        //---------------------------------------------------------------

        StringBuffer sb = new StringBuffer();

        StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" + //
                        "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" + //
                        "VERSION:2.0\n" + //
                        "METHOD:REQUEST\n" + //

                        "BEGIN:VEVENT\n" + //

                        "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:" + to + "\n" + //

                        "ORGANIZER:MAILTO:" + to + "\n" + //

                        "DTSTART:20171208T053000Z\n" + //
                        "DTEND:20171208T060000Z\n" + //

                        "LOCATION:Conference room\n" + //
                        "TRANSP:OPAQUE\n" + //
                        "SEQUENCE:0\n" + //
                        "UID:0400000082004842BF9440448399EB02\n" + //
                        //"DTSTAMP:20051206T120102Z\n" + //
                        "CATEGORIES:Meeting\n" + //
                        "DESCRIPTION:This the description of the meeting.\n\n" + //
                        "SUMMARY:Test meeting request\n" + //
                        "PRIORITY:5\n" + //
                        "CLASS:PUBLIC\n" + //

                        "BEGIN:VALARM\n" + //
                        "TRIGGER:PT1440M\n" + //
                        "ACTION:DISPLAY\n" + //
                        "DESCRIPTION:Reminder\n" + //
                        "END:VALARM\n" + //

                        "END:VEVENT\n" + //
                        "END:VCALENDAR");

        //---------------------------------------------------------------

        // Create the message part
        BodyPart messageBodyPart = new MimeBodyPart();

        // Fill the message
        messageBodyPart.setHeader("Content-Class", "urn:content-  classes:calendarmessage");
        messageBodyPart.setHeader("Content-ID", "calendar_message");
        messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(), "text/calendar")));// very important

        //---------------------------------------------------------------

        // Create a Multipart
        Multipart multipart = new MimeMultipart();

        // Add part one
        multipart.addBodyPart(messageBodyPart);

        // Put parts in message
        message.setContent(multipart);

        //---------------------------------------------------------------

        // send message
        Transport.send(message);
    }
}