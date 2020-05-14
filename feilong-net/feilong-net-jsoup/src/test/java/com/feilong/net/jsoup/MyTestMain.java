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
package com.feilong.net.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

import com.feilong.test.AbstractTest;

public class MyTestMain extends AbstractTest{

    @Test
    public void testMyTestMain(){
        // 直接从字符串中输入 HTML 文档
        String html = "<html><head><title> 开源中国社区 </title></head>" + "<body><p> 这里是 jsoup 项目的相关文章 </p></body></html>";
        Document doc = Jsoup.parse(html);
        //	LOGGER.debug(doc.getElementsByClass(""));
        String unsafe = "<p><a href='http://www.oschina.net/' onclick='stealCookies()'>  开源中国社区 </a></p>";
        String safe = Jsoup.clean(unsafe, Whitelist.basic());
        LOGGER.debug(safe);

        LOGGER.debug(doc.text());
        // 输出 : 
        // <p><a href="http://www.oschina.net/" rel="nofollow"> 开源中国社区 </a></p> 
    }
}