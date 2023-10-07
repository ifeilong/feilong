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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.net.bot.dingtalk.DefaultDingTalkBot;
import com.feilong.net.bot.dingtalk.DingTalkBotBuilder;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 4.0.0
 */
public class LogAndBotCombinationTest{

    private static final Logger      LOGGER = LoggerFactory.getLogger(LogAndBotCombinationTest.class);

    /** The bot. */
    private final DefaultDingTalkBot bot    = (DefaultDingTalkBot) DingTalkBotBuilder.newAsyncCatchExceptionDingTalkBot(
                    getKey(),
                    "SECd2325d14c67a3ec585568e00b49d749c7094a2a1579beb86369d88a5b161c981",
                    "test");

    {

        bot.setIsAsync(false);
    }

    @Test
    public void test(){
        LogAndBotCombination.error(LOGGER, bot, "hello {} error", "jim");
    }

    @Test
    public void testerror(){
        LogAndBotCombination.error(LOGGER, bot, "hello {} errorLastExceptionException", "jim", new NullPointerException("test"));
    }

    //---------------------------------------------------------------
    @Test
    public void testWarn(){
        LogAndBotCombination.warn(LOGGER, bot, "hello {} warn", "jim");
    }

    @Test
    public void testWarn2(){
        LogAndBotCombination.warn(LOGGER, bot, "hello {} warnLastExceptionException", "jim", new NullPointerException("test"));
    }

    //---------------------------------------------------------------
    @Test
    public void testInfo(){
        LogAndBotCombination.info(LOGGER, bot, "hello {} info", "jim");
    }

    @Test
    public void testInfo2(){
        LogAndBotCombination.info(LOGGER, bot, "hello {} infoLastExceptionException", "jim", new NullPointerException("test"));
    }

    //---------------------------------------------------------------
    @Test
    public void testDebug(){
        LogAndBotCombination.debug(LOGGER, bot, "hello {} Debug", "jim");
    }

    @Test
    public void testDebug2(){
        LogAndBotCombination.debug(LOGGER, bot, "hello {} DebugLastExceptionException", "jim", new NullPointerException("test"));
    }

    //---------------------------------------------------------------
    protected String getKey(){
        //测试机器人
        return "a99ef44aad3b80810d86529e9fb16902255a5928908f13fa15e9dbd75c7749d2";
    }
}
