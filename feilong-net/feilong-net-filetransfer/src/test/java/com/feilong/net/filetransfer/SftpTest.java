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
package com.feilong.net.filetransfer;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.feilong.net.filetransfer.FileTransfer;

@ContextConfiguration(locations = { "classpath:spring/spring-sftp-adidas.xml" })
public class SftpTest extends AbstractJUnit4SpringContextTests{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SftpTest.class);

    /** The file transfer. */
    @Autowired
    @Qualifier("sftpFileTransfer")
    private FileTransfer        fileTransfer;

    @Test
    public void test(){
        LOGGER.debug("begin mutiSFTPFileTransferDownloadTest1");

        for (int i = 0, j = 3; i < j; ++i){
            fileTransfer.getFileEntityMap("/Article Master/miadidas", "Product");
        }

        LOGGER.debug("end mutiSFTPFileTransferDownloadTest1");
    }

}
