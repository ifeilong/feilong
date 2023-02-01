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
package com.feilong.net.bot.dingtalk;

import org.junit.Test;

import com.feilong.net.bot.Bot;

public class DingTalkBotHellowordTest{

    /** The bot. */
    Bot bot = new DefaultDingTalkBot(getKey(), "SECd2325d14c67a3ec585568e00b49d749c7094a2a1579beb86369d88a5b161c981");

    @Test
    public void test(){
        bot.sendMessage("hello world");
    }

    //---------------------------------------------------------------
    protected String getKey(){
        //测试机器人
        return "a99ef44aad3b80810d86529e9fb16902255a5928908f13fa15e9dbd75c7749d2";
    }

}
