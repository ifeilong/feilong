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
package com.feilong.net.bot.wxwork;

import org.junit.After;

import com.feilong.json.JsonUtil;
import com.feilong.net.bot.wxwork.DefaultWxworkBot;
import com.feilong.net.bot.wxwork.WxworkBot;
import com.feilong.net.bot.wxwork.message.WxworkResponse;
import com.feilong.test.AbstractTest;

public abstract class AbstractWxworkBotTest extends AbstractTest{

    protected final WxworkBot wxworkBot = new DefaultWxworkBot(getKey());

    //---------------------------------------------------------------

    protected WxworkResponse  wxworkResponse;

    //---------------------------------------------------------------

    @After
    public void after(){
        LOGGER.debug(JsonUtil.format(wxworkResponse));
    }

    //---------------------------------------------------------------
    protected String getKey(){
        //测试机器人
        return "80fa900e-c601-41a9-987c-ffa48f1d9e27";
    }

}
