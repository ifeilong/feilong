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
import org.slf4j.LoggerFactory;

import com.feilong.tools.slf4j.Slf4jUtil;

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

    /** 是否捕获异常,默认false,表示不捕获异常. */
    protected boolean           isCatchException = false;

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

    /**
     * 获得 是否捕获异常,默认false,表示不捕获异常.
     *
     * @return the isCatchException
     */
    public boolean getIsCatchException(){
        return isCatchException;
    }

    /**
     * 设置 是否捕获异常,默认false,表示不捕获异常.
     *
     * @param isCatchException
     *            the isCatchException to set
     */
    public void setIsCatchException(boolean isCatchException){
        this.isCatchException = isCatchException;
    }

    //---------------------------------------------------------------

    @Override
    public boolean sendMessage(String content){
        if (!isAsync){
            String type = Slf4jUtil.format("[SyncSend[{}]Message]", this.getClass().getSimpleName());
            LOGGER.info("{},content:[{}]", type, content);
            return handleSendMessage(content, type);
        }

        //异步
        new Thread(() -> {
            String type = Slf4jUtil.format("[AsyncSend[{}]Message]", this.getClass().getSimpleName());
            LOGGER.info("{},content:[{}]", type, content);
            handleSendMessage(content, type);
        }).start();

        return true;
    }

    protected boolean handleSendMessage(String content,String type){
        try{
            return doSendMessage(content);
        }catch (Exception e){
            if (!isCatchException){
                throw e;
            }
            LOGGER.error(Slf4jUtil.format("[{}],typecontent:[{}],returnFalse", type, content), e);
            return false;
        }
    }

    //---------------------------------------------------------------

    protected abstract boolean doSendMessage(String content);

}
