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

import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.lang.StringUtil.formatPattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.net.bot.message.MessageParams;

/**
 * 通用.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.3.0
 */
public abstract class AbstractBot implements Bot{

    /** The Constant log. */
    private static final Logger LOGGER           = LoggerFactory.getLogger(AbstractBot.class);

    //---------------------------------------------------------------
    /** 是否异步,默认false,表示是同步. */
    protected boolean           isAsync          = false;

    /**
     * 是否捕获异常,默认false,表示不捕获异常.
     * 
     * @deprecated since 4.0.0 请使用 {@link #isThrowException} 如果两个值都设置以 {@link #isThrowException} 为准
     */
    @Deprecated
    protected boolean           isCatchException = false;

    /**
     * 当出现异常, 是否抛出异常.
     * 
     * <p>
     * 默认false,表示不抛出exception; 如果是true 那么会抛出异常,需要自定捕获异常处理.
     * </p>
     * 
     * @since 4.0.0
     * 
     */
    protected boolean           isThrowException = false;

    //---------------------------------------------------------------

    /**
     * Send message.
     *
     * @param content
     *            the content
     * @return true, if successful
     */
    @Override
    public boolean sendMessage(String content){
        Validate.notNull(content, "content can't be null!");
        return sendMessage(content, null);
    }

    @Override
    public boolean sendMessage(String content,MessageParams inputMessageParams){
        Validate.notNull(content, "content can't be null!");
        MessageParams useMessageParams = defaultIfNull(inputMessageParams, new MessageParams());

        String logPrefix = formatPattern(
                        "[{}] content:[{}],useMessageParams:[{}]",
                        !isAsync ? "SyncSendDingTalkMessage" : "AsyncSendDingTalkMessage",
                        content,
                        JsonUtil.toString(useMessageParams));
        if (!isAsync){
            LOGGER.info(logPrefix);
            return handleSendMessage(logPrefix, content, useMessageParams);
        }
        //---------------------------------------------------------------
        //异步
        new Thread(() -> {
            LOGGER.info(logPrefix);
            handleSendMessage(logPrefix, content, useMessageParams);
        }).start();

        return true;
    }

    /**
     * @param logPrefix
     * @param content
     * @param useMessageParams
     * @return
     * @since 4.0.8
     */
    private boolean handleSendMessage(String logPrefix,String content,MessageParams useMessageParams){
        try{
            return doSendMessage(logPrefix, content, useMessageParams);
        }catch (Exception e){
            if (!isCatchException || isThrowException){
                throw e;
            }

            //只打印log 方便排查问题
            LOGGER.error(formatPattern("[{}],returnFalse", logPrefix), e);
            return false;
        }
    }

    //---------------------------------------------------------------

    /**
     * @param content
     * @param messageParams
     * @return
     * @since 4.0.8
     */
    protected abstract boolean doSendMessage(String logPrefix,String content,MessageParams messageParams);

    /**
     * 获得 是否捕获异常,默认false,表示不捕获异常.
     *
     * @return the isCatchException
     * @deprecated since 4.0.0 请使用 {@link #isThrowException} 如果两个值都设置以 {@link #isThrowException} 为准
     */
    @Deprecated
    public boolean getIsCatchException(){
        return isCatchException;
    }

    /**
     * 设置 是否捕获异常,默认false,表示不捕获异常.
     *
     * @param isCatchException
     *            the isCatchException to set
     * @deprecated since 4.0.0 请使用 {@link #isThrowException} 如果两个值都设置以 {@link #isThrowException} 为准
     */
    @Deprecated
    public void setIsCatchException(boolean isCatchException){
        this.isCatchException = isCatchException;
    }

    /**
     * 当出现异常, 是否抛出异常.
     * 
     * <p>
     * 默认false,表示不抛出exception; 如果是true 那么会抛出异常,需要自定捕获异常处理.
     * </p>
     * 
     * @return 默认false,表示抛出, 会抛出exception;如果是true 那么不会抛出异常.
     * @since 4.0.0
     */
    public boolean getIsThrowException(){
        return isThrowException;
    }

    /**
     * 当出现异常, 是否抛出异常.
     * 
     * <p>
     * 默认false,表示不抛出exception; 如果是true 那么会抛出异常,需要自定捕获异常处理.
     * </p>
     *
     * @param isThrowException
     *            默认false,表示抛出, 会抛出exception;如果是true 那么不会抛出异常.
     * @since 4.0.0
     */
    public void setIsThrowException(boolean isThrowException){
        this.isThrowException = isThrowException;
    }

    //---------------------------------------------------------------

    /**
     * 获得 是否异步,默认false,表示是同步.
     *
     * @return the isAsync
     */
    public boolean getIsAsync(){
        return isAsync;
    }

    /**
     * 设置 是否异步,默认false,表示是同步.
     *
     * @param isAsync
     *            the isAsync to set
     */
    public void setIsAsync(boolean isAsync){
        this.isAsync = isAsync;
    }

}
