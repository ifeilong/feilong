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

import com.feilong.net.bot.Bot;
import com.feilong.net.bot.wxwork.message.WxworkResponse;
import com.feilong.net.bot.wxwork.message.news.Article;

/**
 * 微信机器人.
 * 
 * <p>
 * 当前自定义机器人支持文本（text）、markdown（markdown）两种消息类型。
 * </p>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href="https://work.weixin.qq.com/help?person_id=1&doc_id=13376">如何配置群机器人？</a>
 * @since 3.1.0 change packagename from com.feilong.net.bot.wxwork to com.feilong.net.bot.wxwork
 */
public interface WxworkBot extends Bot{

    /**
     * 推送图文类型消息.
     *
     * @param articles
     *            the article
     * @return 如果 <code>articles</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>articles</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     */
    WxworkResponse sendNewsMessage(Article...articles);

}
