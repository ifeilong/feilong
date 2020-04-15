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
package com.feilong.tools.compress;

import static com.feilong.core.date.DateUtil.nowTimestamp;

import org.junit.Test;

public class AntZipHandlerTest{

    private final ZipHandler zipHandler = new AntZipHandler();

    private final String     filePath   = "/Users/feilong/Downloads/product-reports20200324.xlsx";

    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testAntZipHandlerTestNull(){
        zipHandler.zip(null, "/Users/feilong/feilong/logs/" + nowTimestamp() + "antZip3.zip");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntZipHandlerTestEmpty(){
        zipHandler.zip("", "/Users/feilong/feilong/logs/" + nowTimestamp() + "antZip3.zip");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntZipHandlerTestBlank(){
        zipHandler.zip(" ", "/Users/feilong/feilong/logs/" + nowTimestamp() + "antZip3.zip");
    }
    //---------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void testAntZipHandlerTestNull1(){
        zipHandler.zip(filePath, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntZipHandlerTestEmpty1(){
        zipHandler.zip(filePath, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAntZipHandlerTestBlank1(){
        zipHandler.zip(filePath, " ");
    }
}
