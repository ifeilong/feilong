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
package com.feilong.net.bot.wxwork.message.news;

/**
 * The Class Article.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.1.0 change packagename from com.feilong.net.bot.wxwork to com.feilong.net.bot.wxwork
 */
public class Article{

    /** 是 标题，不超过128个字节，超过会自动截断. */
    private String title;

    /** 否 描述，不超过512个字节，超过会自动截断. */
    private String description;

    /** 是 点击后跳转的链接。. */
    private String url;

    /** 否 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。. */
    private String picurl;

    //---------------------------------------------------------------

    /**
     * Instantiates a new article.
     */
    public Article(){
        super();
    }

    /**
     * Instantiates a new article.
     *
     * @param title
     *            the title
     * @param url
     *            the url
     */
    public Article(String title, String url){
        super();
        this.title = title;
        this.url = url;
    }

    /**
     * Instantiates a new article.
     *
     * @param title
     *            the title
     * @param description
     *            描述，不超过512个字节，超过会自动截断w
     * @param url
     *            the url
     * @param picurl
     *            the picurl
     */
    public Article(String title, String description, String url, String picurl){
        super();
        this.title = title;
        this.description = description;
        this.url = url;
        this.picurl = picurl;
    }

    //---------------------------------------------------------------

    /**
     * 获得 是 标题，不超过128个字节，超过会自动截断.
     *
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * 设置 是 标题，不超过128个字节，超过会自动截断.
     *
     * @param title
     *            the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * 获得 否 描述，不超过512个字节，超过会自动截断.
     *
     * @return the description
     */
    public String getDescription(){
        return description;
    }

    /**
     * 设置 否 描述，不超过512个字节，超过会自动截断.
     *
     * @param description
     *            the description to set
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * 获得 是 点击后跳转的链接。.
     *
     * @return the url
     */
    public String getUrl(){
        return url;
    }

    /**
     * 设置 是 点击后跳转的链接。.
     *
     * @param url
     *            the url to set
     */
    public void setUrl(String url){
        this.url = url;
    }

    /**
     * 获得 否 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。.
     *
     * @return the picurl
     */
    public String getPicurl(){
        return picurl;
    }

    /**
     * 设置 否 图文消息的图片链接，支持JPG、PNG格式，较好的效果为大图 1068*455，小图150*150。.
     *
     * @param picurl
     *            the picurl to set
     */
    public void setPicurl(String picurl){
        this.picurl = picurl;
    }

}
