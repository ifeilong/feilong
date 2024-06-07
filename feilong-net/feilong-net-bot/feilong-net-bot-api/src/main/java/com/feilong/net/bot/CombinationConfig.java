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
package com.feilong.net.bot;

import org.slf4j.Logger;

/**
 * 混合相关配置.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.1.0
 */
public class CombinationConfig{

    /** 使用的logger. */
    private Logger  logger;

    /** 使用的bot. */
    private Bot     bot;

    //---------------------------------------------------------------
    /** 签名. */
    private String  signName;

    /** 是否输出日志, 默认是true , 如果是false 那么不输出日志. */
    private boolean isPrintLog       = true;

    /**
     * 是否发送机器人消息, 默认是true , 如果是false 那么不发送消息.
     * 
     * <p>
     * <b>场景:</b> 原先有相关代码发送了消息, 后来需要加个逻辑, 判定部分id不发送 可以把这个值设置为false
     * </p>
     */
    private boolean isSendBotMessage = true;

    //---------------------------------------------------------------

    /**
     * Instantiates a new combination config.
     */
    public CombinationConfig(){
        super();
    }

    /**
     * Instantiates a new combination config.
     *
     * @param logger
     *            the logger
     * @param bot
     *            the bot
     * @param isSendBotMessage
     *            the is send bot message
     */
    public CombinationConfig(Logger logger, Bot bot, boolean isSendBotMessage){
        super();
        this.logger = logger;
        this.bot = bot;
        this.isSendBotMessage = isSendBotMessage;
    }

    /**
     * Instantiates a new combination config.
     *
     * @param logger
     *            the logger
     * @param bot
     *            the bot
     * @param signName
     *            the sign name
     * @param isSendBotMessage
     *            是否发送机器人消息, 默认是true , 如果是false 那么不发送消息.
     * 
     *            <p>
     *            <b>场景:</b> 原先有相关代码发送了消息, 后来需要加个逻辑, 判定部分id不发送 可以把这个值设置为false
     *            </p>
     */
    public CombinationConfig(Logger logger, Bot bot, String signName, boolean isSendBotMessage){
        super();
        this.logger = logger;
        this.bot = bot;
        this.signName = signName;
        this.isSendBotMessage = isSendBotMessage;
    }

    //---------------------------------------------------------------

    /**
     * Instantiates a new combination config.
     *
     * @param logger
     *            the logger
     * @param bot
     *            the bot
     * @param signName
     *            the sign name
     * @param isPrintLog
     *            the is print log
     * @param isSendBotMessage
     *            是否发送机器人消息, 默认是true , 如果是false 那么不发送消息.
     * 
     *            <p>
     *            <b>场景:</b> 原先有相关代码发送了消息, 后来需要加个逻辑, 判定部分id不发送 可以把这个值设置为false
     *            </p>
     */
    public CombinationConfig(Logger logger, Bot bot, String signName, boolean isPrintLog, boolean isSendBotMessage){
        super();
        this.logger = logger;
        this.bot = bot;
        this.signName = signName;
        this.isPrintLog = isPrintLog;
        this.isSendBotMessage = isSendBotMessage;
    }

    //---------------------------------------------------------------

    /**
     * 获得 使用的logger.
     *
     * @return the logger
     */
    public Logger getLogger(){
        return logger;
    }

    /**
     * 设置 使用的logger.
     *
     * @param logger
     *            the logger to set
     */
    public void setLogger(Logger logger){
        this.logger = logger;
    }

    /**
     * 获得 使用的bot.
     *
     * @return the bot
     */
    public Bot getBot(){
        return bot;
    }

    /**
     * 设置 使用的bot.
     *
     * @param bot
     *            the bot to set
     */
    public void setBot(Bot bot){
        this.bot = bot;
    }

    /**
     * 获得 签名.
     *
     * @return the signName
     */
    public String getSignName(){
        return signName;
    }

    /**
     * 设置 签名.
     *
     * @param signName
     *            the signName to set
     */
    public void setSignName(String signName){
        this.signName = signName;
    }

    /**
     * 获得 是否输出日志, 默认是true , 如果是false 那么不输出日志.
     *
     * @return the isPrintLog
     */
    public boolean getIsPrintLog(){
        return isPrintLog;
    }

    /**
     * 获得 是否发送机器人消息, 默认是true , 如果是false 那么不发送消息.
     *
     * @return the isSendBotMessage
     */
    public boolean getIsSendBotMessage(){
        return isSendBotMessage;
    }

    /**
     * 设置 是否输出日志, 默认是true , 如果是false 那么不输出日志.
     *
     * @param isPrintLog
     *            the isPrintLog to set
     */
    public void setIsPrintLog(boolean isPrintLog){
        this.isPrintLog = isPrintLog;
    }

    /**
     * 设置 是否发送机器人消息, 默认是true , 如果是false 那么不发送消息.
     *
     * @param isSendBotMessage
     *            the isSendBotMessage to set
     */
    public void setIsSendBotMessage(boolean isSendBotMessage){
        this.isSendBotMessage = isSendBotMessage;
    }

}
