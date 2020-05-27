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

/**
 * 邮件发送链接配置.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.0
 */
public class MailSendConnectionConfig extends BaseConfig{

    /**
     * The is tls enable.
     * 
     * <p>
     * If true, enables the use of the STARTTLS command (if supported by the server) to switch the connection to a TLS-protected connection
     * before issuing any login commands.<br>
     * 
     * Note that an appropriate trust store must configured so that the client will trust the server's certificate.<br>
     * Defaults to false.
     * </p>
     * 
     * @since 1.13.2
     */
    private boolean isSmtpStarttlsEnable = false;

    /**
     * mail.smtp.ssl.enable SSL 开关.
     * 
     * <p>
     * If set to true, use SSL to connect and use the SSL port by default.<br>
     * Defaults to false for the <b>"smtp"</b> protocol and true for the <b>"smtps"</b> protocol.
     * </p>
     * 
     * @since 1.13.2
     */
    private boolean isSmtpSSLEnable      = false;
    //---------------------------------------------------------------

    /**
     * mail.smtp.ssl.enable SSL 开关.
     * 
     * <p>
     * If set to true, use SSL to connect and use the SSL port by default.<br>
     * Defaults to false for the <b>"smtp"</b> protocol and true for the <b>"smtps"</b> protocol.
     * </p>
     *
     * @return the isSmtpSSLEnable
     * @since 1.13.2
     */
    public boolean getIsSmtpSSLEnable(){
        return isSmtpSSLEnable;
    }

    /**
     * mail.smtp.ssl.enable SSL 开关.
     * 
     * <p>
     * If set to true, use SSL to connect and use the SSL port by default.<br>
     * Defaults to false for the <b>"smtp"</b> protocol and true for the <b>"smtps"</b> protocol.
     * </p>
     *
     * @param isSmtpSSLEnable
     *            the isSmtpSSLEnable to set
     * @since 1.13.2
     */
    public void setIsSmtpSSLEnable(boolean isSmtpSSLEnable){
        this.isSmtpSSLEnable = isSmtpSSLEnable;
    }

    /**
     * The is tls enable.
     * 
     * <p>
     * If true, enables the use of the STARTTLS command (if supported by the server) to switch the connection to a TLS-protected connection
     * before issuing any login commands.<br>
     * 
     * Note that an appropriate trust store must configured so that the client will trust the server's certificate.<br>
     * Defaults to false.
     * </p>
     *
     * @return the is tls enable
     * @since 1.13.2
     */
    public boolean getIsSmtpStarttlsEnable(){
        return isSmtpStarttlsEnable;
    }

    /**
     * The is tls enable.
     * 
     * <p>
     * If true, enables the use of the STARTTLS command (if supported by the server) to switch the connection to a TLS-protected connection
     * before issuing any login commands.<br>
     * 
     * Note that an appropriate trust store must configured so that the client will trust the server's certificate.<br>
     * Defaults to false.
     * </p>
     *
     * @param isSmtpStarttlsEnable
     *            the new is tls enable
     * @since 1.13.2
     */
    public void setIsSmtpStarttlsEnable(boolean isSmtpStarttlsEnable){
        this.isSmtpStarttlsEnable = isSmtpStarttlsEnable;
    }

}