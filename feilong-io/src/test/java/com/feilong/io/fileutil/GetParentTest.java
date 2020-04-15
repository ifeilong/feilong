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
package com.feilong.io.fileutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.feilong.io.FileUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
public class GetParentTest{

    @Test
    public void testGetParentTestNull1(){
        assertNull(FileUtil.getParent("/"));
    }

    @Test
    public void testGetParentTestNull11(){
        assertEquals("/Users/feilong/feilong/logs", FileUtil.getParent("/Users/feilong/feilong/logs/createDirectoryByFilePath"));
    }

    @Test
    public void testGetParentTestNull11NotExist(){
        assertEquals("/Users/feilong/feilong/logs/getParent", FileUtil.getParent("/Users/feilong/feilong/logs/getParent/getParent"));
    }

    @Test
    public void testGetParentTestFile(){
        assertEquals("/Users/feilong/feilong/logs", FileUtil.getParent("/Users/feilong/feilong/logs/1.txt"));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testGetParentTestNull(){
        FileUtil.getParent((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParentTestEmpty(){
        FileUtil.getParent("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParentTestBlank(){
        FileUtil.getParent(" ");
    }
}
