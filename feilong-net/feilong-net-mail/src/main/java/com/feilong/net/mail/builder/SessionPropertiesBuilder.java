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
package com.feilong.net.mail.builder;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.util.Properties;

import com.feilong.core.lang.ClassUtil;
import com.feilong.json.JsonUtil;
import com.feilong.net.mail.entity.MailSendConnectionConfig;
import com.feilong.net.mail.entity.SessionConfig;

/**
 * The Class SessionPropertiesBuilder.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.13.2
 */
public class SessionPropertiesBuilder{

    /** Don't let anyone instantiate this class. */
    private SessionPropertiesBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Properties object. Used only if a new Session object is created.
     * 
     * <p>
     * It is expected that the client supplies values for the properties listed in Appendix A of the JavaMail spec <br>
     * (particularly
     * mail.store.protocol, mail.transport.protocol, mail.host, mail.user, and mail.from) as the defaults are unlikely to work in all cases.
     * </p>
     * 
     * @param sessionConfig
     *            the base config
     * @return the properties
     */
    public static Properties build(SessionConfig sessionConfig){
        String serverHost = sessionConfig.getServerHost();
        if (isNullOrEmpty(serverHost)){
            throw new IllegalArgumentException(
                            formatPattern("serverHost can't be null/empty! sessionConfig:[{}]", JsonUtil.toString(sessionConfig)));
        }

        //---------------------------------------------------------------
        Properties properties = new Properties();
        //The SMTP server to connect to.
        properties.put("mail.smtp.host", serverHost);

        //端口号
        //The SMTP server port to connect to, if the connect() method doesn't explicitly specify one Defaults to 25.
        String serverPort = sessionConfig.getServerPort();
        if (isNotNullOrEmpty(serverPort)){
            properties.put("mail.smtp.port", serverPort);
        }

        //---------------------------------------------------------------

        //If true, attempt to authenticate the user using the AUTH command. 
        //Defaults to false.
        properties.put("mail.smtp.auth", sessionConfig.getIsValidate() ? "true" : "false");

        //---------------------------------------------------------------

        //if(mailConfig.getTimeout() > 0)
        //     properties.put("mail.smtp.connectiontimeout", mailConfig.getTimeout() * 1000)
        //     properties.put("mail.smtp.timeout", mailConfig.getTimeout() * 1000)
        // }else
        //     properties.put("mail.smtp.connectiontimeout", MailConfig.TIMEOUT)
        //     properties.put("mail.smtp.timeout", MailConfig.TIMEOUT)

        //---------------------------------------------------------------

        //ssl
        if (ClassUtil.isInstance(sessionConfig, MailSendConnectionConfig.class)){
            MailSendConnectionConfig mailSendConnectionConfig = (MailSendConnectionConfig) sessionConfig;

            //ssl
            if (mailSendConnectionConfig.getIsSmtpSSLEnable()){
                setSSL(properties);
            }

            //Starttls
            if (mailSendConnectionConfig.getIsSmtpStarttlsEnable()){
                setStarttls(properties);
            }
        }

        //---------------------------------------------------------------

        return properties;
    }

    //    Name    Type    Description
    //    mail.smtp.connectiontimeout int Socket connection timeout value in milliseconds. Default is infinite timeout.
    //    mail.smtp.timeout   int Socket I/O timeout value in milliseconds. Default is infinite timeout.
    //    mail.smtp.localhost String  Local host name used in the SMTP HELO or EHLO command. Defaults toInetAddress.getLocalHost().getHostName(). Should not normally need to be set if your JDK and your name service are configured properly.
    //    mail.smtp.localaddress  String  Local address (host name) to bind to when creating the SMTP socket. Defaults to the address picked by the Socket class. Should not normally need to be set, but useful with multi-homed hosts where it's important to pick a particular local address to bind to.
    //    mail.smtp.localport int Local port number to bind to when creating the SMTP socket. Defaults to the port number picked by the Socket class.
    //    mail.smtp.ehlo  boolean If false, do not attempt to sign on with the EHLO command. Defaults to true. Normally failure of the EHLO command will fallback to the HELO command; this property exists only for servers that don't fail EHLO properly or don't implement EHLO properly.
    //    mail.smtp.auth.mechanisms   String  If set, lists the authentication mechanisms to consider, and the order in which to consider them. Only mechanisms supported by the server and supported by the current implementation will be used. The default is"LOGIN PLAIN DIGEST-MD5", which includes all the authentication mechanisms supported by the current implementation.
    //    mail.smtp.submitter String  The submitter to use in the AUTH tag in the MAIL FROM command. Typically used by a mail relay to pass along information about the original submitter of the message. See also the setSubmitter method of SMTPMessage. Mail clients typically do not use this.
    //    mail.smtp.dsn.notify    String  The NOTIFY option to the RCPT command. Either NEVER, or some combination of SUCCESS, FAILURE, and DELAY (separated by commas).
    //    mail.smtp.dsn.ret   String  The RET option to the MAIL command. Either FULL or HDRS.
    //    mail.smtp.allow8bitmime boolean If set to true, and the server supports the 8BITMIME extension, text parts of messages that use the "quoted-printable" or "base64" encodings are converted to use "8bit" encoding if they follow the RFC2045 rules for 8bit text.
    //    mail.smtp.sendpartial   boolean If set to true, and a message has some valid and some invalid addresses, send the message anyway, reporting the partial failure with a SendFailedException. If set to false (the default), the message is not sent to any of the recipients if there is an invalid recipient address.
    //    mail.smtp.sasl.realm    String  The realm to use with DIGEST-MD5 authentication.
    //    mail.smtp.quitwait  boolean If set to false, the QUIT command is sent and the connection is immediately closed. If set to true (the default), causes the transport to wait for the response to the QUIT command.
    //    mail.smtp.reportsuccess boolean If set to true, causes the transport to include anSMTPAddressSucceededException for each address that is successful. Note also that this will cause aSendFailedException to be thrown from thesendMessage method of SMTPTransport even if all addresses were correct and the message was sent successfully.
    //    mail.smtp.mailextension String  Extension string to append to the MAIL command. The extension string can be used to specify standard SMTP service extensions as well as vendor-specific extensions. Typically the application should use the SMTPTransportmethod supportsExtension to verify that the server supports the desired service extension. See RFC 1869and other RFCs that define specific extensions.
    //    mail.smtp.userset   boolean If set to true, use the RSET command instead of the NOOP command in the isConnected method. In some cases sendmail will respond slowly after many NOOP commands; use of RSET avoids this sendmail issue. Defaults to false.

    private static void setSSL(Properties properties){

        // If set to true, use SSL to connect and use the SSL port by default. 
        // Defaults to false for the "smtp" protocol and true for the "smtps" protocol.
        properties.put("mail.smtp.ssl.enable", "true");

        //If set, specifies the name of a class that implements the javax.net.SocketFactory interface. 
        //This class will be used to create SMTP sockets.
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        //---------------------------------------------------------------

        //SocketFactory
        //If set to a class that implements thejavax.net.SocketFactory interface, this class will be used to create SMTP sockets. 
        //Note that this is an instance of a class, not a name, and must be set using the putmethod, not the setProperty method.
        //        properties.put("mail.smtp.socketFactory", "")

        //boolean If set to true, failure to create a socket using the specified socket factory class will cause the socket to be created using the java.net.Socket class. 
        //Defaults to true.
        //        properties.put("mail.smtp.socketFactory.fallback", "")

        //int Specifies the port to connect to when using the specified socket factory. 
        //If not set, the default port will be used.
        //properties.put("mail.smtp.socketFactory.port", "")

        //boolean If set to true, check the server identity as specified by RFC 2595. 
        //These additional checks based on the content of the server's certificate are intended to prevent man-in-the-middle attacks. Defaults to false.
        //        properties.put("mail.smtp.ssl.checkserveridentity", "")

        //If set to a class that extends thejavax.net.ssl.SSLSocketFactory class, this class will be used to create SMTP SSL sockets. 
        //Note that this is an instance of a class, not a name, and must be set using theput method, not the setProperty method.
        //        properties.put("mail.smtp.ssl.socketFactory SSLSocketFactory", "")

        //String  If set, specifies the name of a class that extends thejavax.net.ssl.SSLSocketFactory class. 
        //This class will be used to create SMTP SSL sockets.
        //        properties.put("mail.smtp.ssl.socketFactory.class", "")

        //int Specifies the port to connect to when using the specified socket factory. 
        //If not set, the default port will be used.
        //        properties.put("mail.smtp.ssl.socketFactory.port", "")

        //string  Specifies the SSL protocols that will be enabled for SSL connections. The property value is a whitespace separated list of tokens acceptable to thejavax.net.ssl.SSLSocket.setEnabledProtocolsmethod.
        //        properties.put("mail.smtp.ssl.protocols", "")

        //string  Specifies the SSL cipher suites that will be enabled for SSL connections. 
        //The property value is a whitespace separated list of tokens acceptable to thejavax.net.ssl.SSLSocket.setEnabledCipherSuitesmethod.
        //        properties.put("mail.smtp.ssl.ciphersuites", "")

    }

    private static void setStarttls(Properties properties){
        // 使用 STARTTLS安全连接:prop.put(“mail.smtp.starttls.enable”,”true”);
        //    Caused by: com.sun.mail.smtp.SMTPSendFailedException: 530 5.7.0 Must issue a STARTTLS command first

        //boolean If true, enables the use of the STARTTLS command (if supported by the server) to switch the connection to a TLS-protected connection before issuing any login commands. 
        //Note that an appropriate trust store must configured so that the client will trust the server's certificate. 

        //Defaults to false.
        properties.put("mail.smtp.starttls.enable", "true");//STARTTLS requested but already using SSL

        //If true, requires the use of the STARTTLS command. 
        //If the server doesn't support the STARTTLS command, or the command fails, the connect method will fail. 
        //Defaults to false.
        // properties.put("mail.smtp.starttls.required", "true")
    }
    //
    //    private static void setSSL(Properties properties){
    //        //-------当需使用SSL验证时添加，邮箱不需SSL验证时删除即可（测试SSL验证使用QQ企业邮箱）
    //        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
    //        properties.put("mail.smtp.socketFactory.fallback", "false")
    //        properties.put("mail.smtp.socketFactory.port", "465")
    //        //        MailSSLSocketFactory sf = new MailSSLSocketFactory()
    //        //        sf.setTrustAllHosts(true)
    //        properties.put("mail.smtp.ssl.socketFactory", "javax.net.ssl.SSLSocketFactory")
    //    }

}
