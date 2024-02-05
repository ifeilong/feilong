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

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.taglib.display.httpconcat.BaseHttpConcatTest;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.8
 */
public class ItemSrcListResolverTest extends BaseHttpConcatTest{

    @Test
    public void testEmptyLines(){
        List<String> list = ItemSrcListResolver.resolve(read("concat-empty-line.vm"), "http://www.feilong.com");
        assertThat(list, allOf(hasItem("a.js"), hasItem("b.js")));
    }

    @Test
    public void testDuplicateLines(){
        List<String> list = ItemSrcListResolver.resolve(read("concat-Duplicate.vm"), "http://www.feilong.com");
        assertThat(list, allOf(hasItem("a.js"), hasItem("b.js")));
    }

    @Test
    public void testCommentLines(){
        List<String> list = ItemSrcListResolver.resolve(read("concat-coment-line.vm"), "http://www.feilong.com");
        assertThat(list, allOf(hasItem("a.js"), hasItem("b.js")));
    }

    @Test
    public void testEmptyLinesTrim(){
        List<String> list = ItemSrcListResolver.resolve(read("concat-empty-line-trim.vm"), "http://www.feilong.com");
        assertThat(list, allOf(hasItem("a.js"), hasItem("b.js")));
    }

    @Test
    public void testEmptyLinesTrim1(){
        List<String> list = ItemSrcListResolver.resolve(read("concat-empty-comment.vm"), "http://www.feilong.com");
        assertEquals(emptyList(), list);
    }

    //---------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testNull(){
        ItemSrcListResolver.resolve(read("concat-null.vm"), "http://www.feilong.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmpty(){
        ItemSrcListResolver.resolve(read("concat-empty.vm"), "http://www.feilong.com");
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testItemSrcListResolverTestNull(){
        ItemSrcListResolver.resolve(null, "http://www.feilong.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSrcListResolverTestEmpty(){
        ItemSrcListResolver.resolve("", "http://www.feilong.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testItemSrcListResolverTestBlank(){
        ItemSrcListResolver.resolve("  ", "http://www.feilong.com");
    }
}
