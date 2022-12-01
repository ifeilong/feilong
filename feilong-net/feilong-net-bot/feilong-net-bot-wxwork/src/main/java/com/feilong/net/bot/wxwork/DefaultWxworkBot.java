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
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.net.http.HttpClientUtil.getResponseBodyAsString;
import static com.feilong.net.http.HttpMethodType.POST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.core.lang.StringUtil;
import com.feilong.json.JsonUtil;
import com.feilong.net.bot.AbstractBot;
import com.feilong.net.bot.message.BotMessage;
import com.feilong.net.bot.wxwork.message.WxworkResponse;
import com.feilong.net.bot.wxwork.message.markdown.Markdown;
import com.feilong.net.bot.wxwork.message.markdown.WxworkMarkdownMessage;
import com.feilong.net.bot.wxwork.message.news.Article;
import com.feilong.net.bot.wxwork.message.news.News;
import com.feilong.net.bot.wxwork.message.news.WxworkNewsMessage;
import com.feilong.net.http.ConnectionConfig;
import com.feilong.net.http.HttpRequest;

/**
 * 默认的微信机器人.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.1.0 change packagename from com.feilong.net.bot.wxwork to com.feilong.net.bot.wxwork
 */
public class DefaultWxworkBot extends AbstractBot implements WxworkBot{

    /** The Constant log. */
    private static final Logger LOGGER          = LoggerFactory.getLogger(DefaultWxworkBot.class);

    //---------------------------------------------------------------
    private static final String BOT_WEBHOOK_URL = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send";

    /** The key. */
    private String              key;

    //---------------------------------------------------------------

    /**
     * Instantiates a new default wxwork bot.
     */
    public DefaultWxworkBot(){
        super();
    }

    /**
     * Instantiates a new default wxwork bot.
     *
     * @param key
     *            the key
     */
    public DefaultWxworkBot(String key){
        super();
        this.key = key;
    }

    //---------------------------------------------------------------

    @Override
    protected boolean doSendMessage(String content){
        Validate.notBlank(content, "content can't be blank!");
        WxworkResponse wxworkResponse = pushMessage(new WxworkMarkdownMessage(new Markdown(content)));
        return wxworkResponse.getIsSuccess();
    }

    @Override
    public WxworkResponse sendNewsMessage(Article...articles){
        if (!isAsync){
            LOGGER.info("[SyncSendNewsMessage]articles:[{}]", JsonUtil.toString(articles));
            return doSendWxNewsMessage(articles);
        }

        //异步
        new Thread(() -> {
            LOGGER.info("[AsyncSendNewsMessage]articles:[{}]", JsonUtil.toString(articles));
            doSendWxNewsMessage(articles);
        }).start();

        return null;
    }

    private WxworkResponse doSendWxNewsMessage(Article...articles){
        try{
            Validate.notEmpty(articles, "articles can't be null/empty!");
            News news = new News(toList(articles));
            return pushMessage(new WxworkNewsMessage(news));
        }catch (Exception e){
            if (!isCatchException){
                throw e;
            }
            LOGGER.error(StringUtil.formatPattern("articles:[{}],returnFalse", JsonUtil.toString(articles)), e);
            return null;
        }
    }

    //---------------------------------------------------------------

    private <T extends BotMessage> WxworkResponse pushMessage(T botMessage){
        String msgtype = botMessage.getMsgtype();
        Validate.notBlank(msgtype, "msgtype can't be blank!");

        //---------------------------------------------------------------
        String url = BOT_WEBHOOK_URL + "?key=" + key;

        HttpRequest httpRequest = new HttpRequest(url, POST);
        httpRequest.setRequestBody(JsonUtil.toString(botMessage));

        String json = getResponseBodyAsString(httpRequest, new ConnectionConfig(2 * MILLISECOND_PER_MINUTE));
        return JsonUtil.toBean(json, WxworkResponse.class);
    }

    //---------------------------------------------------------------

    /**
     * Sets the key.
     *
     * @param key
     *            the key to set
     */
    public void setKey(String key){
        this.key = key;
    }

}
