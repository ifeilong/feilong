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

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.util.Map;

/**
 * markdown字符串生成器.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href="https://open.dingtalk.com/document/orgapp/robot-overview">机器人</a>
 * @since 4.3.0
 */
public class MarkdownStringCreator{

    /**
     * 生成 类似于的markdown string.
     *
     * @param title
     *            the title
     * @param itemMap
     *            the item map
     * @return the string
     * @since 4.3.0
     */
    public static String createListString(String title,Map<String, String> itemMap){
        StringBuilder sb = new StringBuilder();
        sb.append(formatPattern("### {}  \n  ", title));

        if (isNotNullOrEmpty(itemMap)){
            for (Map.Entry<String, String> entry : itemMap.entrySet()){
                String key = entry.getKey();
                String value = entry.getValue();

                sb.append(formatPattern("- {}: [**{}**]  \n  ", key, value));
            }
        }
        return sb.toString();
    }
}
