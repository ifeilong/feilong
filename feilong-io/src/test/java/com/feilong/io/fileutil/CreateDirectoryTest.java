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

import org.junit.Test;

import com.feilong.io.FileUtil;
import com.feilong.lib.lang3.SystemUtils;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.5
 */
public class CreateDirectoryTest{

    /**
     * Creates the directory.
     */
    @Test
    public void testCreateDirectory(){
        FileUtil.createDirectory(SystemUtils.USER_HOME + "/feilong/logs/createDirectory/test/test");
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testCreateDirectoryTestNull(){
        FileUtil.createDirectory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDirectoryTestEmpty(){
        FileUtil.createDirectory("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDirectoryTestBlank(){
        FileUtil.createDirectory(" ");
    }

}
