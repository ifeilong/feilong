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
package com.feilong.net.bot.wxwork.message.markdown;

/**
 * The Class Markdown.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.1.0 change packagename from com.feilong.net.bot.wxwork to com.feilong.net.bot.wxwork
 */
public class Markdown{

    /** markdown内容，最长不超过4096个字节，必须是utf8编码. */
    private String content;

    //---------------------------------------------------------------
    /**
     * Instantiates a new markdown.
     */
    public Markdown(){
        super();
    }

    /**
     * Instantiates a new markdown.
     *
     * @param content
     *            the content
     */
    public Markdown(String content){
        super();
        this.content = content;
    }

    //---------------------------------------------------------------

    /**
     * 获得 markdown内容，最长不超过4096个字节，必须是utf8编码.
     *
     * @return the content
     */
    public String getContent(){
        return content;
    }

    /**
     * 设置 markdown内容，最长不超过4096个字节，必须是utf8编码.
     *
     * @param content
     *            the content to set
     */
    public void setContent(String content){
        this.content = content;
    }

}