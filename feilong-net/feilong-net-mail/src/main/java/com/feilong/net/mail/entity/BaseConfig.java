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

import com.feilong.json.SensitiveWords;
import com.feilong.net.UriProcessor;

/**
 * 父类配置.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.5.3
 */
public abstract class BaseConfig implements SessionConfig{

    /**
     * 发送邮件的服务器的IP.
     * 
     * <p>
     * example:smtp.126.com
     * </p>
     * 
     * @since 1.13.2 rename from mailServerHost
     */
    private String  serverHost;

    /**
     * 邮件服务的端口 默认25.
     * 
     * @since 1.13.2 rename from mailServerPort
     */
    private String  serverPort = "25";

    //---------------------------------------------------------------

    /** 登录邮件发送服务器的用户名. */
    private String  userName;

    /**
     * 登录邮件发送服务器的密码.
     * <p>
     * example:******.
     * </p>
     */
    @SensitiveWords
    private String  password;

    //---------------------------------------------------------------

    /** 是否debug 输出. */
    private boolean isDebug    = false;

    //---------------------------------------------------------------

    /** 是否需要身份验证,默认 true. */
    private boolean isValidate = true;

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.mail.entity.SessionConfig#getUserName()
     */
    @Override
    public String getUserName(){
        return userName;
    }

    /**
     * 设置 登录邮件发送服务器的用户名.
     *
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.mail.entity.SessionConfig#getPassword()
     */
    @Override
    public String getPassword(){
        return password;
    }

    /**
     * 设置 登录邮件发送服务器的密码.
     *
     * @param password
     *            the password to set
     */
    public void setPassword(String password){
        this.password = password;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.mail.entity.SessionConfig#getServerHost()
     */
    @Override
    public String getServerHost(){
        //since 3.0.10 host通常是没有前后空格的
        return UriProcessor.process(serverHost, true);
    }

    /**
     * 设置 发送邮件的服务器的IP.
     *
     * @param serverHost
     *            the new 发送邮件的服务器的IP
     */
    public void setServerHost(String serverHost){
        this.serverHost = serverHost;
    }

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.net.mail.entity.SessionConfig#getServerPort()
     */
    @Override
    public String getServerPort(){
        return serverPort;
    }

    /**
     * 设置 邮件服务的端口 默认25.
     *
     * @param serverPort
     *            the new 邮件服务的端口 默认25
     */
    public void setServerPort(String serverPort){
        this.serverPort = serverPort;
    }

    //---------------------------------------------------------------

    /**
     * 获得 是否debug 输出.
     *
     * @return the isDebug
     */
    @Override
    public boolean getIsDebug(){
        return isDebug;
    }

    /**
     * 设置 是否debug 输出.
     *
     * @param isDebug
     *            the isDebug to set
     */
    public void setIsDebug(boolean isDebug){
        this.isDebug = isDebug;
    }

    //---------------------------------------------------------------

    /**
     * 获得 是否需要身份验证,默认 true.
     *
     * @return the isValidate
     */
    @Override
    public boolean getIsValidate(){
        return isValidate;
    }

    /**
     * 设置 是否需要身份验证,默认 true.
     *
     * @param isValidate
     *            the isValidate to set
     */
    public void setIsValidate(boolean isValidate){
        this.isValidate = isValidate;
    }
}
