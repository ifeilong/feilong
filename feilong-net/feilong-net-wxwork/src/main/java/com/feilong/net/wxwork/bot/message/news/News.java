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

import java.util.List;

/**
 * The Class News.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.9
 */
public class News{

    /** The articles. */
    private List<Article> articles;

    //---------------------------------------------------------------
    /**
     * Instantiates a new news.
     */
    public News(){
        super();
    }

    /**
     * Instantiates a new news.
     *
     * @param articles
     *            the articles
     */
    public News(List<Article> articles){
        super();
        this.articles = articles;
    }

    //---------------------------------------------------------------

    /**
     * Gets the articles.
     *
     * @return the articles
     */
    public List<Article> getArticles(){
        return articles;
    }

    /**
     * Sets the articles.
     *
     * @param articles
     *            the articles to set
     */
    public void setArticles(List<Article> articles){
        this.articles = articles;
    }

}
