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
package com.feilong.core.util.collectionsutil;

import static com.feilong.core.bean.ConvertUtil.toList;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.core.util.CollectionsUtil;

public class ContainsTest{

    @Test
    public void test(){
        assertEquals(false, CollectionsUtil.contains(null, "关羽"));
    }

    @Test
    public void test1(){
        assertEquals(true, CollectionsUtil.contains(toList("张飞", "关羽", "刘备"), "关羽"));
        assertEquals(false, CollectionsUtil.contains(toList("张飞", "关羽", "刘备"), " 关羽"));
        assertEquals(false, CollectionsUtil.contains(toList("张飞", "关羽", "刘备"), " 关羽 "));

        assertEquals(false, CollectionsUtil.contains(toList("张飞", "关羽1", "刘备"), " 关羽 "));
    }

    @Test
    public void test12(){
        assertEquals(true, CollectionsUtil.contains(toList("track", "debug"), "debug"));
        assertEquals(false, CollectionsUtil.contains(toList("track", "debug"), " debug"));
        assertEquals(false, CollectionsUtil.contains(toList("track", "DEBUG"), " debug"));
        assertEquals(false, CollectionsUtil.contains(toList("track", " DEBUG"), " debug"));
    }

}
