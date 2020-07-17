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

import org.junit.Test;

public class GetDocumentTest{

    /**
     * 伪造的 useragent.
     * 
     * @since 1.8.1
     */
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    @Test(expected = NullPointerException.class)
    public void testJsoupUtilTest3Null(){
        JsoupUtil.getDocument(null, DEFAULT_USER_AGENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsoupUtilTest3Empty(){
        JsoupUtil.getDocument("", DEFAULT_USER_AGENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJsoupUtilTest3Blank(){
        JsoupUtil.getDocument(" ", DEFAULT_USER_AGENT);
    }
}
