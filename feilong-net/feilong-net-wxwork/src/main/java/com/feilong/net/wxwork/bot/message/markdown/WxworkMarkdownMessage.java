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
package com.feilong.net.wxwork.bot.message.markdown;

import com.feilong.net.wxwork.bot.message.BotMessage;

/**
 * Markdown格式的message.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.9
 */
public class WxworkMarkdownMessage extends BotMessage{

    /** The text. */
    private Markdown markdown;

    //---------------------------------------------------------------
    /**
     * Instantiates a new wxwork bot message.
     */
    public WxworkMarkdownMessage(){
        super("markdown");
    }

    /**
     * Instantiates a new wxwork bot message.
     *
     * @param markdown
     *            the markdown
     */
    public WxworkMarkdownMessage(Markdown markdown){
        super("markdown");
        this.markdown = markdown;
    }

    //---------------------------------------------------------------
    /**
     * 获得 text.
     *
     * @return the markdown
     */
    public Markdown getMarkdown(){
        return markdown;
    }

    /**
     * 设置 text.
     *
     * @param markdown
     *            the markdown to set
     */
    public void setMarkdown(Markdown markdown){
        this.markdown = markdown;
    }

}