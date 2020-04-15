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
package com.feilong.io.filenameutil;

import static com.feilong.core.bean.ConvertUtil.toArray;
import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.Test;

import com.feilong.io.FilenameUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.0
 */
public class GetParentPathListTest{

    /**
     * Test get file top parent name1.
     */
    @Test
    public void testGetFileTopParentName1(){
        String remoteDirectory = "/home/sftp-speedo/test/aa/bbb/ccc/ddd/201606160101/";
        List<String> list = FilenameUtil.getParentPathList(remoteDirectory);

        String[] array = toArray(
                        "/home/sftp-speedo/test/aa/bbb/ccc/ddd",
                        "/home/sftp-speedo/test/aa/bbb/ccc",
                        "/home/sftp-speedo/test/aa/bbb",
                        "/home/sftp-speedo/test/aa",
                        "/home/sftp-speedo/test",
                        "/home/sftp-speedo",
                        "/home",
                        "/");
        assertArrayEquals(array, toArray(list, String.class));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testGetParentPathListTestNull(){
        FilenameUtil.getParentPathList(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParentPathListTestEmpty(){
        FilenameUtil.getParentPathList("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetParentPathListTestBlank(){
        FilenameUtil.getParentPathList(" ");
    }

}
