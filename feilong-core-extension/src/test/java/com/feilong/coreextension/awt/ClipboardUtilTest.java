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
package com.feilong.coreextension.awt;

import org.junit.Test;

import com.feilong.test.AbstractTest;

/**
 * The Class ClipboardUtilTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class ClipboardUtilTest extends AbstractTest{

    /**
     * Test set clipboard contents.
     */
    @Test
    public void testSetClipboardContents(){
        LOGGER.debug(ClipboardUtil.getClipboardContent());
    }

    /**
     * Test.
     */
    @Test
    public void test(){
        String aString = "L,M,S,XL,XS,XXL";
        StringBuilder sb = new StringBuilder();
        String[] strings = aString.split(",");
        sb.append("<enum type=\"string\">");
        for (String string : strings){
            sb.append("<item>" + string + "</item>");
        }
        sb.append("</enum>");
        ClipboardUtil.setClipboardContent(sb.toString());
        LOGGER.debug(sb.toString());
    }
}