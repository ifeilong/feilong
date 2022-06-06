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
package com.feilong.net.bot.dingtalk.message.markdown;

/**
 * 目前只支持markdown语法的子集，具体支持的元素如下：
 * 
 * 标题
 * # 一级标题
 * ## 二级标题
 * ### 三级标题
 * #### 四级标题
 * ##### 五级标题
 * ###### 六级标题
 * 
 * 引用
 * > A man who stands fornothing will fall foranything.
 * 
 * 文字加粗、斜体
 ** bold**
 * *italic*
 * 
 * 链接
 * [this is a link](http://name.com)
 * 
 * 图片
 * ![](http://name.com/pic.jpg)
 * 
 * 无序列表
 * - item1
 * - item2
 * 
 * 有序列表
 * 1.item1
 * 2.item2
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 3.0.9
 */
public class Markdown{

    /** 首屏会话透出的展示内容。. */
    private String title;

    /** markdown格式的消息。. */
    private String text;

    //---------------------------------------------------------------

    /**
     * Instantiates a new markdown.
     *
     * @param title
     *            the title
     * @param text
     *            the text
     */
    public Markdown(String title, String text){
        super();
        this.title = title;
        this.text = text;
    }

    //---------------------------------------------------------------

    /**
     * 获得 首屏会话透出的展示内容。.
     *
     * @return the title
     */
    public String getTitle(){
        return title;
    }

    /**
     * 设置 首屏会话透出的展示内容。.
     *
     * @param title
     *            the title to set
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * 获得 markdown格式的消息。.
     *
     * @return the text
     */
    public String getText(){
        return text;
    }

    /**
     * 设置 markdown格式的消息。.
     *
     * @param text
     *            the text to set
     */
    public void setText(String text){
        this.text = text;
    }

}