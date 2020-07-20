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
package com.feilong.net.wxwork.bot.message.news;

import com.feilong.net.wxwork.bot.message.BotMessage;

/**
 * The Class WxworkNewsMessage.
 */
public class WxworkNewsMessage extends BotMessage{

    /** The news. */
    private News news;

    //---------------------------------------------------------------
    /**
     * Instantiates a new wxwork bot message.
     */
    public WxworkNewsMessage(){
        super("news");
    }

    /**
     * Instantiates a new wxwork bot message.
     *
     * @param news
     *            the news
     */
    public WxworkNewsMessage(News news){
        super("news");
        this.news = news;
    }

    //--------------------------------------------------------------- 
    /**
     * Gets the news.
     *
     * @return the news
     */
    public News getNews(){
        return news;
    }

    /**
     * Sets the news.
     *
     * @param news
     *            the news to set
     */
    public void setNews(News news){
        this.news = news;
    }

}