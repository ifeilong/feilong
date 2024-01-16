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

import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.lang.StringUtil.formatPattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.net.bot.AbstractBot;
import com.feilong.net.bot.message.MessageParams;
import com.feilong.net.bot.wxwork.message.WxworkResponse;
import com.feilong.net.bot.wxwork.message.markdown.Markdown;
import com.feilong.net.bot.wxwork.message.markdown.WxworkMarkdownMessage;
import com.feilong.net.bot.wxwork.message.news.Article;
import com.feilong.net.bot.wxwork.message.news.News;
import com.feilong.net.bot.wxwork.message.news.WxworkNewsMessage;

/**
 * 默认的企业微信机器人.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://open.work.weixin.qq.com/help2/pc/14931?person_id=1&is_tencent=">如何配置群机器人？</a>
 * @since 3.1.0 change packagename from com.feilong.net.bot.wxwork to com.feilong.net.bot.wxwork
 */
public class DefaultWxworkBot extends AbstractBot implements WxworkBot{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWxworkBot.class);

    //---------------------------------------------------------------
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
    protected boolean doSendMessage(String logPrefix,String content,MessageParams messageParams){
        Validate.notBlank(content, "content can't be blank!");

        WxworkMarkdownMessage wxworkMarkdownMessage = new WxworkMarkdownMessage(new Markdown(content));
        WxworkResponse wxworkResponse = WxworkPushUtil.pushMessage(wxworkMarkdownMessage, key);
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
            return WxworkPushUtil.pushMessage(new WxworkNewsMessage(news), key);
        }catch (Exception e){
            if (!isCatchException || isThrowException){
                throw e;
            }
            LOGGER.error(formatPattern("articles:[{}],returnFalse", JsonUtil.toString(articles)), e);
            return null;
        }
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

    @Override
    public String getKey(){
        return key;
    }

}
