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

import java.util.Objects;

import org.slf4j.Logger;

import com.feilong.core.lang.StringUtil;

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
     * Debug.
     *
     * @param logger
     *            the log
     * @param bot
     *            the ding talk bot
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    public static void debug(Logger logger,Bot bot,String pattern,Object...arguments){
        log(logger, bot, "debug", pattern, arguments);
    }

    /**
     * Info.
     *
     * @param logger
     *            the log
     * @param bot
     *            the ding talk bot
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    public static void info(Logger logger,Bot bot,String pattern,Object...arguments){
        log(logger, bot, "info", pattern, arguments);
    }

    /**
     * Warn.
     *
     * @param logger
     *            the log
     * @param bot
     *            the ding talk bot
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    public static void warn(Logger logger,Bot bot,String pattern,Object...arguments){
        log(logger, bot, "warn", pattern, arguments);
    }

    /**
     * Error.
     *
     * @param logger
     *            the log
     * @param bot
     *            the ding talk bot
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
     *            the bot
     * @param type
     *            the type
     * @param pattern
     *            the pattern
     * @param arguments
     *            the arguments
     */
    private static void log(Logger logger,Bot bot,String type,String pattern,Object...arguments){
        String format = StringUtil.formatPattern(pattern, arguments);

        if (null != logger){
            log(logger, type, pattern, arguments);
        }

        //---------------------------------------------------------------

        if (null != bot){
            bot.sendMessage(format);
        }
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