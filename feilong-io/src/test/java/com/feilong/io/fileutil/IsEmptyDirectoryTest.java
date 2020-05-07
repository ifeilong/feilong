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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.feilong.io.FileUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
public class IsEmptyDirectoryTest{

    /**
     * Checks if is empty directory.
     */
    @Test
    
    public void isEmptyDirectory(){
        // 不存在的文件
        try{
            FileUtil.isEmptyDirectory("E:\\test\\1\\2011-07-07\\test\\1\\2011-07-07");
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        // 文件
        try{
            FileUtil.isEmptyDirectory("E:\\1.txt");
            fail();
        }catch (IllegalArgumentException e){
            assertTrue(true);
        }

        // 非空目录
        assertEquals(false, FileUtil.isEmptyDirectory("E:\\Workspaces"));

        // 正确的 空目录
        assertEquals(true, FileUtil.isEmptyDirectory("E:\\empty"));
    }

    /**
     * Test is empty directory1.
     */
    @Test
    public void testIsEmptyDirectory1(){
        // 正确的 空目录
        assertEquals(true, FileUtil.isEmptyDirectory("E:\\empty"));
    }
}
