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
package com.feilong.net.bot.dingtalk;

import com.feilong.net.bot.Bot;

/**
 * 钉钉机器人.
 * 
 * <p>
 * 企业内部有较多系统支撑着公司的核心业务流程，譬如CRM系统、交易系统、监控报警系统等等。通过钉钉的自定义机器人，可以将这些系统事件同步到钉钉的聊天群。
 * </p>
 * 
 * <p>
 * 当前自定义机器人支持文本（text）、markdown（markdown）两种消息类型。
 * </p>
 * 
 * <h3>说明:</h3>
 * <blockquote>
 * <ol>
 * <li>每个机器人每分钟最多发送20条消息到群里，如果超过20条，会限流10分钟。</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see <a href=
 *      "https://developers.dingtalk.com/document/robots/custom-robot-access-1?spm=ding_open_doc.document.0.0.6d9d10afLWgSfH#topic-2026027">如何配置群机器人？</a>
 * @since 3.1.0
 */
public interface DingTalkBot extends Bot{

    /**
     * 消息, 支持markdown格式,也支持纯文本.
     *
     * @param title
     *            the title
     * @param content
     *            markdown内容，最长不超过4096个字节，必须是utf8编码
     * @param atMobiles
     *            被@人的手机号。 注意在text内容里要有@人的手机号
     * @return 成功返回true<br>
     *         如果 <code>content</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>content</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 3.1.0
     */
    boolean sendMessage(String title,String content,String...atMobiles);
}
