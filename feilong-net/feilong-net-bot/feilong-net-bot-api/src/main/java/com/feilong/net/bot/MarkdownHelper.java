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

import com.feilong.core.Validate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * markdown字符串生成器.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://open.dingtalk.com/document/orgapp/robot-overview">机器人</a>
 * @since 4.5.1
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MarkdownHelper{

    /**
     * 用font标签包裹.
     * 
     * <p>
     * <span style="color:red">使用{@code <font>}标签设置颜色，但该标签不在钉钉Markdown格式消息的官方支持范围内，为了业务稳定性不建议使用</span>
     * </p>
     *
     * @param text
     *            the text
     * @param color
     *            the color
     * @return the string
     */
    public static String font(String text,String color){
        Validate.notBlank(color, "color can't be blank!");

        return "<font color=\"" + color + "\">" + text + "</font>";
    }

    /**
     * 生成标签.
     *
     * @param text
     *            the text
     * @param url
     *            the url
     * @return the string
     */
    public static String link(String text,String url){
        Validate.notBlank(text, "text can't be blank!");
        Validate.notBlank(url, "url can't be blank!");

        return "[" + text + "](" + url + ")";
    }

    /**
     * 加粗.
     *
     * @param text
     *            the text
     * @return the string
     */
    public static String b(String text){
        return "**" + text + "**";
    }

    /**
     * 一个\n有时无法成功换行，建议使用两个\n\n确保换行效果。
     */
    public static String newLine(){
        return "\n\n"; //一个\n有时无法成功换行，建议使用两个\n\n确保换行效果
    }

}
