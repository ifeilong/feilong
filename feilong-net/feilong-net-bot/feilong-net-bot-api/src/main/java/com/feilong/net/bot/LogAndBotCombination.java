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

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.util.Objects;

import org.slf4j.Logger;

import com.feilong.core.lang.ClassUtil;
import com.feilong.lib.lang3.ArrayUtils;

/**
 * 
 * 日志和bot 的组合.
 * 
 * <p>
 * 即输出日志,又发送bot 消息
 * </p>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.3.6
 */
public class LogAndBotCombination{

    /**
     * Debug级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    public static void debug(Logger logger,Bot bot,String pattern,Object...arguments){
        log(logger, bot, "debug", pattern, arguments);
    }

    /**
     * Info级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    public static void info(Logger logger,Bot bot,String pattern,Object...arguments){
        log(logger, bot, "info", pattern, arguments);
    }

    /**
     * Warn级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    public static void warn(Logger logger,Bot bot,String pattern,Object...arguments){
        log(logger, bot, "warn", pattern, arguments);
    }

    /**
     * Error级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    public static void error(Logger logger,Bot bot,String pattern,Object...arguments){
        log(logger, bot, "error", pattern, arguments);
    }

    //---------------------------------------------------------------

    /**
     * Log.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param type
     *            the type
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    private static void log(Logger logger,Bot bot,String type,String pattern,Object...arguments){
        if (null != logger){
            log(logger, type, pattern, arguments);
        }

        //---------------------------------------------------------------
        sendBot(bot, pattern, arguments);
    }

    private static void sendBot(Bot bot,String pattern,Object...arguments){
        if (null == bot){
            return;
        }

        //---------------------------------------------------------------
        String sendMessage = createSendMessage(pattern, arguments);
        bot.sendMessage(sendMessage);
    }

    private static String createSendMessage(String pattern,Object...arguments){
        //这isNullOrEmpty 里面已经判断了length是否=0
        if (isNullOrEmpty(arguments)){
            return formatPattern(pattern, arguments);
        }

        //---------------------------------------------------------------
        //获取最后一个
        Object lastOneArgument = arguments[arguments.length - 1];
        //如果最后一个参数是 Throwable 类型
        if (ClassUtil.isInstance(lastOneArgument, Throwable.class)){

            //起始索引包含，结束索引不包含。空数组输入产生空输出。
            return formatPattern(pattern, ArrayUtils.subarray(arguments, 0, arguments.length - 1));
        }
        //---------------------------------------------------------------
        return formatPattern(pattern, arguments);
    }

    /**
     * Log.
     *
     * @param logger
     *            the log
     * @param type
     *            the type
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    private static void log(Logger logger,String type,String pattern,Object...arguments){
        if (Objects.equals(type, "debug")){
            logger.debug(pattern, arguments);
        }else if (Objects.equals(type, "info")){
            logger.info(pattern, arguments);
        }else if (Objects.equals(type, "warn")){
            logger.warn(pattern, arguments);
        }else if (Objects.equals(type, "error")){
            logger.error(pattern, arguments);
        }
    }
}