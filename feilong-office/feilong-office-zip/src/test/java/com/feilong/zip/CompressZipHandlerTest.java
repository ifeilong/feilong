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
package com.feilong.zip;

import static com.feilong.core.date.DateUtil.nowTimestamp;

import org.junit.Test;

import com.feilong.lib.lang3.SystemUtils;
import com.feilong.test.AbstractTest;

public class CompressZipHandlerTest extends AbstractTest{

    private final ZipHandler zipHandler        = new CompressZipHandler();

    String                   tobeZipFileFloder = SystemUtils.USER_HOME
                    + "/workspace/feilong/feilong/feilong-office/feilong-office-zip/src/test/resources";

    //---------------------------------------------------------------

    String                   outputZipFloder   = SystemUtils.USER_HOME + "/feilong/zip-unzip/";

    @Test
    public void test(){
        zipHandler.zip(
                        tobeZipFileFloder + "/for-zip", //
                        outputZipFloder + nowTimestamp() + "compresszip.zip");
    }

    @Test
    public void zipFile(){
        zipHandler.zip(
                        tobeZipFileFloder + "/product-reports20200324.xlsx", //
                        outputZipFloder + nowTimestamp() + "compresszip.zip");
    }

    @Test
    public void zipEmptyFloder(){
        zipHandler.zip(
                        tobeZipFileFloder + "/for-zip-empty", //
                        outputZipFloder + nowTimestamp() + "compresszip.zip");
    }
}
