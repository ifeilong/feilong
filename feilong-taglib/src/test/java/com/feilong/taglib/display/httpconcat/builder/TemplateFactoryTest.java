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
package com.feilong.taglib.display.httpconcat.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.12.5
 */
public class TemplateFactoryTest{

    @Test
    public void test(){
        assertEquals(
                        "<link rel=\"stylesheet\" type=\"text/css\" media=\"screen\" href=\"{0}\" />\n",
                        TemplateFactory.getTemplate(TemplateFactory.TYPE_CSS));
    }

    @Test
    public void testJs(){
        assertEquals("<script type=\"text/javascript\" src=\"{0}\"></script>\n", TemplateFactory.getTemplate(TemplateFactory.TYPE_JS));
    }

    //---------------------------------------------------------------

    @Test(expected = UnsupportedOperationException.class)
    public void testNull(){
        TemplateFactory.getTemplate(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmpty(){
        TemplateFactory.getTemplate("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testBlank(){
        TemplateFactory.getTemplate(" ");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testElse(){
        TemplateFactory.getTemplate("video");
    }
}
