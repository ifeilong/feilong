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

import static com.feilong.core.bean.ConvertUtil.toMap;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.feilong.net.bot.Bot;
import com.feilong.net.bot.MarkdownStringCreator;

public class DingTalkBotMarkDownStringTest{

    /** The bot. */
    Bot bot = new DefaultDingTalkBot(getKey(), "SECd2325d14c67a3ec585568e00b49d749c7094a2a1579beb86369d88a5b161c981");

    @Test
    public void test(){
        //        return formatPattern(
        //                        "### {}处理结束  \n  "//
        //                                        + "- 单次配置抓取数:[{}]  \n  "//
        //                                        + "- 本次实际处理数:[{}]  \n  "//
        //                                        + "- 本次耗时:[{}]  \n  "//
        //
        //                                        + "- total:[**{}**]  \n  "//
        //                                        + "- {}",
        Map<String, String> map = toMap("单次配置抓取数", "50", "本次实际处理数", "50");
        map.put("本次耗时", "2分钟");
        map.put("total", "80000");
        map.put("etc", "222小时");
        boolean result = bot.sendMessage(MarkdownStringCreator.createListString("数据推送成功", map));
        assertEquals(true, result);
    }

    //---------------------------------------------------------------
    protected String getKey(){
        //测试机器人
        return "a99ef44aad3b80810d86529e9fb16902255a5928908f13fa15e9dbd75c7749d2";
    }

}
