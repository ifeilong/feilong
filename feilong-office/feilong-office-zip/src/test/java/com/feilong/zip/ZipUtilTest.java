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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.junit.Test;

import com.feilong.io.FileUtil;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.11.4
 */
public class ZipUtilTest{

    @Test
    public void test() throws FileNotFoundException,IOException{
        String tobeZipFilePath = "/Users/feilong/workspace/feilong/feilong/feilong-office/feilong-office-zip/src/test/resources/for-zip";

        String outputZipPath = "/Users/feilong/feilong/logs/" + nowTimestamp() + "compresszip.zip";
        a(tobeZipFilePath, outputZipPath);
    }

    private static void a(String tobeZipFilePath,String outputZipPath) throws FileNotFoundException,IOException{
        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(
                        new BufferedOutputStream(FileUtil.getFileOutputStream(outputZipPath)));
        ZipUtil.zip(zipArchiveOutputStream, tobeZipFilePath, outputZipPath);
    }

}
