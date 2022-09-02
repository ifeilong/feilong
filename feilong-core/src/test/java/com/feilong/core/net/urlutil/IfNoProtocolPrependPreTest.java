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

import static com.feilong.core.bean.ConvertUtil.toArray;
import static com.feilong.core.lang.ArrayUtil.EMPTY_STRING_ARRAY;
import static com.feilong.core.lang.StringUtil.EMPTY;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.core.net.URLUtil;
import com.feilong.test.AbstractTest;

public class IfNoProtocolPrependPreTest extends AbstractTest{

    String   url          = "stora/945/E4/XDFO/png";

    String[] protocolPres = toArray("https://", "http://");

    @Test
    public void testIfNoProtocolPrependPreUrlNull(){
        assertEquals(EMPTY, URLUtil.ifNoProtocolPrependPre(null, toArray("https://"), "http://www.baidu.com"));
    }

    @Test
    public void testIfNoProtocolPrependPreUrlEmpty(){
        assertEquals(EMPTY, URLUtil.ifNoProtocolPrependPre("", toArray("https://"), "http://www.baidu.com"));
    }

    @Test
    public void testIfNoProtocolPrependPreUrlBlank(){
        assertEquals(EMPTY, URLUtil.ifNoProtocolPrependPre(" ", toArray("https://"), "http://www.baidu.com"));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testIfNoProtocolPrependPrePoNull(){
        URLUtil.ifNoProtocolPrependPre(url, null, "http://www.baidu.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIfNoProtocolPrependPrePoEmpty(){
        URLUtil.ifNoProtocolPrependPre(url, EMPTY_STRING_ARRAY, "http://www.baidu.com");
    }
    //---------------------------------------------------------------

    @Test
    public void testIfNoProtocolPrependPrePerfixNull(){
        assertEquals(url, URLUtil.ifNoProtocolPrependPre(url, protocolPres, null));
    }

    @Test
    public void testIfNoProtocolPrependPrePerfixEmpty(){
        assertEquals(url, URLUtil.ifNoProtocolPrependPre(url, protocolPres, ""));
    }

    @Test
    public void testIfNoProtocolPrependPrePerfixBlank(){
        assertEquals(url, URLUtil.ifNoProtocolPrependPre(url, protocolPres, " "));
    }

    //---------------------------------------------------------------

    @Test
    public void testIfNoProtocolPrependPrePerfixNo(){
        String bString = "http://" + url;
        assertEquals(bString, URLUtil.ifNoProtocolPrependPre(bString, protocolPres, "http://aaaaaa"));
    }

    @Test
    public void testIfNoProtocolPrependPrePerfixHttps(){
        String bString = "https://" + url;
        assertEquals(bString, URLUtil.ifNoProtocolPrependPre(bString, protocolPres, "http://aaaaaa"));
    }

    @Test
    public void testIfNoProtocolPrependPrePerfixHttps2(){
        String bString = "https://" + url;
        String perfix = "http://aaaaaa";
        assertEquals(perfix + bString, URLUtil.ifNoProtocolPrependPre(bString, toArray("http://"), perfix));
    }

}
