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
package com.feilong.core.net.urlutil;

import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.core.net.URLUtil;
import com.feilong.test.AbstractTest;

public class IfNoHttpOrHttpsProtocolPrependPreTest extends AbstractTest{

    String url = "stora/945/E4/XDFO/png";

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPreUrlNull(){
        assertEquals(EMPTY, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(null, "http://www.baidu.com"));
    }

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPreUrlEmpty(){
        assertEquals(EMPTY, URLUtil.ifNoHttpOrHttpsProtocolPrependPre("", "http://www.baidu.com"));
    }

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPreUrlBlank(){
        assertEquals(EMPTY, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(" ", "http://www.baidu.com"));
    }

    //---------------------------------------------------------------

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPrePerfixNull(){
        assertEquals(url, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(url, null));
    }

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPrePerfixEmpty(){
        assertEquals(url, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(url, ""));
    }

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPrePerfixBlank(){
        assertEquals(url, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(url, " "));
    }

    //---------------------------------------------------------------

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPrePerfixNo(){
        String bString = "http://" + url;
        assertEquals(bString, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(bString, "http://aaaaaa"));
    }

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPrePerfixHttps(){
        String bString = "https://" + url;
        assertEquals(bString, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(bString, "http://aaaaaa"));
    }

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPrePerfixHttps222(){
        assertEquals("http://aaaaaa" + url, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(url, "http://aaaaaa"));
    }

    @Test
    public void testifNoHttpAndHttpsProtocolPrependPrePerfixHttps2(){
        String bString = "https://" + url;
        String perfix = "http://aaaaaa";
        assertEquals(bString, URLUtil.ifNoHttpOrHttpsProtocolPrependPre(bString, perfix));
    }

}
