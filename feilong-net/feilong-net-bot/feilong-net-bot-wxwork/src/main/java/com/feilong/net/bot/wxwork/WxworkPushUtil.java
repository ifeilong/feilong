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

import static com.feilong.core.TimeInterval.MILLISECOND_PER_MINUTE;
import static com.feilong.net.http.HttpClientUtil.getResponseBodyAsString;
import static com.feilong.net.http.HttpMethodType.POST;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.net.bot.message.BotMessage;
import com.feilong.net.bot.wxwork.message.WxworkResponse;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;

/**
 * 企业微信机器人推送器.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://work.weixin.qq.com/help?person_id=1&doc_id=13376">如何配置群机器人？</a>
 * @since 4.0.8
 */
public class WxworkPushUtil{

    /** The Constant BOT_WEBHOOK_URL. */
    private static final String BOT_WEBHOOK_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send";
    //---------------------------------------------------------------

    /**
     * Push message.
     *
     * @param <T>
     *            the generic type
     * @param botMessage
     *            the bot message
     * @param key
     *            the key
     * @return the wxwork response
     */
    public static <T extends BotMessage> WxworkResponse pushMessage(T botMessage,String key){
        String msgtype = botMessage.getMsgtype();
        Validate.notBlank(msgtype, "msgtype can't be blank!");

        //---------------------------------------------------------------
        String url = BOT_WEBHOOK_URL + "?key=" + key;

        HttpRequest httpRequest = new HttpRequest(url, POST);
        httpRequest.setRequestBody(JsonUtil.toString(botMessage));

        String json = getResponseBodyAsString(httpRequest, new ConnectionConfig(2 * MILLISECOND_PER_MINUTE));
        return JsonUtil.toBean(json, WxworkResponse.class);
    }
}
