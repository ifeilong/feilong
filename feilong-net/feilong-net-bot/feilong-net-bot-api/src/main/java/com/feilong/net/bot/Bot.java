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
package com.feilong.net.bot;

/**
 * 机器人接口.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.1.0
 */
public interface Bot{

    /**
     * 消息, 支持markdown格式,也支持纯文本.
     *
     * @param content
     *            markdown内容，最长不超过4096个字节，必须是utf8编码
     * @return 如果 <code>content</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>content</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    boolean sendMessage(String content);

    /**
     * 获取该机器人的key(微信机器人是key属性, 钉钉是accessToken属性).
     *
     * @return the key
     * @since 4.0.6
     */
    String getKey();

}
