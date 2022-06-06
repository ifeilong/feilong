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
package com.feilong.net.bot.message;

/**
 * 机器人消息.
 * 
 * <p>
 * 作用,在转json使用
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.1.0
 */
public class BotMessage{

    /** The msgtype. */
    private String msgtype = "text";

    //---------------------------------------------------------------
    /**
     * Instantiates a new bot message.
     */
    public BotMessage(){
        super();
    }

    /**
     * Instantiates a new bot message.
     *
     * @param msgtype
     *            the msgtype
     */
    public BotMessage(String msgtype){
        super();
        this.msgtype = msgtype;
    }

    //---------------------------------------------------------------

    /**
     * 获得 msgtype.
     *
     * @return the msgtype
     */
    public String getMsgtype(){
        return msgtype;
    }

    /**
     * 设置 msgtype.
     *
     * @param msgtype
     *            the msgtype to set
     */
    public void setMsgtype(String msgtype){
        this.msgtype = msgtype;
    }

}