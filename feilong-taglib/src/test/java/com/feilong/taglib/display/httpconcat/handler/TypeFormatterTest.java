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
package com.feilong.taglib.display.httpconcat.handler;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Test;

import com.feilong.taglib.display.httpconcat.builder.TemplateFactory;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.1
 */
public class TypeFormatterTest{

    @Test
    public void testFormat(){
        assertEquals(TemplateFactory.TYPE_JS, TypeFormatter.format(TemplateFactory.TYPE_JS, null));
        assertEquals(TemplateFactory.TYPE_CSS, TypeFormatter.format(TemplateFactory.TYPE_CSS, null));
    }

    @Test
    public void testFormat1(){
        assertEquals(TemplateFactory.TYPE_JS, TypeFormatter.format(TemplateFactory.TYPE_JS, toList("a.js")));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testTypeFormatterTestNull(){
        TypeFormatter.format("", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFormatterTestEmpty(){
        TypeFormatter.format(null, Collections.<String> emptyList());
    }

    //---------------------------------------------------------------

}
