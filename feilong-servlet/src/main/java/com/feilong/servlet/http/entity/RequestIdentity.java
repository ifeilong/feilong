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
package com.feilong.servlet.http.entity;

import java.io.Serializable;

/**
 * 确认请求信息的身份.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.4.0
 */
public class RequestIdentity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3557763656213641299L;

    /** 用户客户端ip. */
    private String            clientIP;

    /** 用户userAgent. */
    private String            userAgent;

    /** 用户 sessionId. */
    private String            sessionId;

    //---------------------------------------------------------------

    /**
     * 获得 用户客户端ip.
     *
     * @return the clientIP
     */
    public String getClientIP(){
        return clientIP;
    }

    /**
     * 设置 用户客户端ip.
     *
     * @param clientIP
     *            the clientIP to set
     */
    public void setClientIP(String clientIP){
        this.clientIP = clientIP;
    }

    /**
     * 获得 用户userAgent.
     *
     * @return the userAgent
     */
    public String getUserAgent(){
        return userAgent;
    }

    /**
     * 设置 用户userAgent.
     *
     * @param userAgent
     *            the userAgent to set
     */
    public void setUserAgent(String userAgent){
        this.userAgent = userAgent;
    }

    /**
     * 获得 用户 sessionId.
     *
     * @return the sessionId
     */
    public String getSessionId(){
        return sessionId;
    }

    /**
     * 设置 用户 sessionId.
     *
     * @param sessionId
     *            the sessionId to set
     */
    public void setSessionId(String sessionId){
        this.sessionId = sessionId;
    }
}
