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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.net.bot.Bot;

/**
 * The Class DingTalkBotTextTest.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.1.0
 */
public class DingTalkBotTextTest{

    /** The bot. */
    Bot bot = new DefaultDingTalkBot(getKey(), "SECd2325d14c67a3ec585568e00b49d749c7094a2a1579beb86369d88a5b161c981");

    @Test
    public void test(){
        String content = "## 今晚去喝酒吗😁 \n" + //
        //  "@15001841318 \n" + //
        //  "@金鑫 \n" + //
                        "![](https://img.alicdn.com/tfs/TB1bB9QKpzqK1RjSZFoXXbfcXXa-576-96.png) \n" + //
                        "> 曾经有一段真挚的爱情 \n" + //
                        "1. 美女 \n" + //
                        "2. 帅哥 \n" + //
                        "- **喝酒** \n" + //
                        "- [百度](http://baidu.com) \n" + //
                        "- *唱歌* \n";
        boolean result = bot.sendMessage(content);
        assertEquals(true, result);
    }

    //---------------------------------------------------------------
    protected String getKey(){
        //测试机器人
        return "a99ef44aad3b80810d86529e9fb16902255a5928908f13fa15e9dbd75c7749d2";
    }

}
