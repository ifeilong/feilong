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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.feilong.test.AbstractTest;

@lombok.extern.slf4j.Slf4j
public class MultiSFTPFileTransferDownloadServer2 extends AbstractTest{

    /** The file transfer. */
    @Autowired
    @Qualifier("sftpFileTransfer")
    private FileTransfer fileTransfer;

    @Test
    @SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
    public void downloadFile(){
        log.info("begin mutiSFTPFileTransferDownloadTest1");
        String[] remotePaths = {
                                 "/upload/Inbound/InventoryAdjustments/Archive/2016-07-15_11-58-58.389-INVENTORY_ADJUSTMENTS_3PL_feilong_20160715-154626-073.XML" };
        String localAbsoluteDirectoryPath = "E:\\test\\1";
        fileTransfer.download(localAbsoluteDirectoryPath, remotePaths);
        log.info("end mutiSFTPFileTransferDownloadTest1");
    }

}
