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
import com.feilong.lib.lang3.StringUtils;

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
     * Debug级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param signName
     *            the sign name
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     * @since 4.0.3
     */
    public static void debugWithSignName(Logger logger,Bot bot,String signName,String pattern,Object...arguments){
        debug(logger, bot, joinPattern(signName, pattern), arguments);
    }

    /**
     * Info级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param signName
     *            the sign name
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     * @since 4.0.3
     */
    public static void infoWithSignName(Logger logger,Bot bot,String signName,String pattern,Object...arguments){
        info(logger, bot, joinPattern(signName, pattern), arguments);
    }

    /**
     * Warn级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param signName
     *            the sign name
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     * @since 4.0.3
     */
    public static void warnWithSignName(Logger logger,Bot bot,String signName,String pattern,Object...arguments){
        warn(logger, bot, joinPattern(signName, pattern), arguments);
    }

    /**
     * Error级别输出日志以及发送机器人.
     *
     * @param logger
     *            the log
     * @param bot
     *            机器人, 目前支持微信机器人 {@link com.feilong.net.bot.wxwork.DefaultWxworkBot} 和
     *            钉钉机器人{@link com.feilong.net.bot.dingtalk.DefaultDingTalkBot}
     * @param signName
     *            the sign name
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     * @since 4.0.3
     */
    public static void errorWithSignName(Logger logger,Bot bot,String signName,String pattern,Object...arguments){
        error(logger, bot, joinPattern(signName, pattern), arguments);
    }

    /**
     * Join pattern.
     *
     * @param signName
     *            the sign name
     * @param pattern
     *            the pattern
     * @return the string
     * @since 4.0.3
     */
    private static String joinPattern(String signName,String pattern){
        if (isNullOrEmpty(signName)){
            return pattern;
        }
        return StringUtils.join("[", signName, "]", pattern);
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
        if (null == bot){
            return;
        }

        //---------------------------------------------------------------
        String sendMessage = createSendMessage(pattern, arguments);
        bot.sendMessage(sendMessage);
    }

    /**
     * 创建 send message.
     *
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     * @return the string
     */
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
            return formatPatternExcludeLastArgument(pattern, arguments);
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
        //这isNullOrEmpty 里面已经判断了length是否=0
        if (isNullOrEmpty(arguments)){
            logData(logger, type, pattern);
            return;
        }

        //---------------------------------------------------------------
        //获取最后一个
        Object lastOneArgument = arguments[arguments.length - 1];
        //如果最后一个参数是 Throwable 类型
        if (ClassUtil.isInstance(lastOneArgument, Throwable.class)){
            String message = formatPatternExcludeLastArgument(pattern, arguments);
            if (Objects.equals(type, "debug")){
                logger.debug(message, (Throwable) lastOneArgument);
            }else if (Objects.equals(type, "info")){
                logger.info(message, (Throwable) lastOneArgument);
            }else if (Objects.equals(type, "warn")){
                logger.warn(message, (Throwable) lastOneArgument);
            }else if (Objects.equals(type, "error")){
                logger.error(message, (Throwable) lastOneArgument);
            }
            return;
        }
        //---------------------------------------------------------------
        String message = formatPattern(pattern, arguments);
        logData(logger, type, message);
    }

    /**
     * 剔除最后一个参数 format
     * 
     * @param pattern
     * @param arguments
     * @return
     * @since 4.0.3
     */
    private static String formatPatternExcludeLastArgument(String pattern,Object...arguments){
        //起始索引包含，结束索引不包含。空数组输入产生空输出。
        return formatPattern(pattern, ArrayUtils.subarray(arguments, 0, arguments.length - 1));
    }

    private static void logData(Logger logger,String type,String message){
        if (Objects.equals(type, "debug")){
            logger.debug(message);
        }else if (Objects.equals(type, "info")){
            logger.info(message);
        }else if (Objects.equals(type, "warn")){
            logger.warn(message);
        }else if (Objects.equals(type, "error")){
            logger.error(message);
        }
    }
}