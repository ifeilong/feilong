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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.feilong.io.FilenameUtil;
import com.feilong.test.AbstractTest;

public class GetFileNameTest extends AbstractTest{

    /** <code>{@value}</code>. */
    private static String FILE_NAME = "F:/pie2.png";

    @Test
    public void testGetFileName(){
        assertEquals("pie2.png", FilenameUtil.getFileName(FILE_NAME));
    }

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testGetFileNameTestNull(){
        FilenameUtil.getFileName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFileNameTestEmpty(){
        FilenameUtil.getFileName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFileNameTestBlank(){
        FilenameUtil.getFileName(" ");

    }

}
