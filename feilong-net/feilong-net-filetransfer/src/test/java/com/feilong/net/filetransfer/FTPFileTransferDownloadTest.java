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

import static com.feilong.lib.lang3.ArrayUtils.EMPTY_STRING_ARRAY;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.feilong.net.filetransfer.FileTransfer;

public class FTPFileTransferDownloadTest extends FileTransferDownloadTest{

    /** The file transfer. */
    @Autowired
    @Qualifier("ftpFileTransfer")
    private FileTransfer fileTransfer;

    //---------------------------------------------------------------

    /**
     * 下载单个文件
     */
    @Override
    @Test
    public void downloadFile(){
        String remotePath = "/maven/settings.xml";
        String localAbsoluteDirectoryPath = "E:\\test\\1";
        fileTransfer.download(localAbsoluteDirectoryPath, remotePath);
    }

    /**
     * 下载整个目录
     */
    @Override
    @Test
    public void downloadDir(){
        String remotePath = "/webstore/InlineSales_Test/2011-07-05";
        String localAbsoluteDirectoryPath = "E:\\test\\1";
        fileTransfer.download(localAbsoluteDirectoryPath, remotePath);
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadNullLocalAbsoluteDirectoryPath()
     */
    @Override
    @Test(expected = NullPointerException.class)
    public void downloadNullLocalAbsoluteDirectoryPath(){
        fileTransfer.download(null, "/home/feilongtest/out/test");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyLocalAbsoluteDirectoryPath()
     */
    @Override
    @Test(expected = IllegalArgumentException.class)
    public void downloadEmptyLocalAbsoluteDirectoryPath(){
        fileTransfer.download("", "/home/feilongtest/out/test");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadBlankLocalAbsoluteDirectoryPath()
     */
    @Override
    @Test(expected = IllegalArgumentException.class)
    public void downloadBlankLocalAbsoluteDirectoryPath(){
        fileTransfer.download(" ", "/home/feilongtest/out/test");

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadNullRemotePaths()
     */
    @Override
    @Test(expected = NullPointerException.class)
    public void downloadNullRemotePaths(){
        fileTransfer.download("E:\\test\\1", null);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyRemotePaths()
     */
    @Override
    @Test(expected = IllegalArgumentException.class)
    public void downloadEmptyRemotePaths(){
        fileTransfer.download("E:\\test\\1", EMPTY_STRING_ARRAY);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyWithNullRemotePath()
     */
    @Override
    @Test(expected = NullPointerException.class)
    public void downloadEmptyWithNullRemotePath(){
        String[] remotePaths = {
                                 "/upload/Inbound/InventoryAdjustments/Archive/2016-07-15_11-58-58.389-INVENTORY_ADJUSTMENTS_3PL_feilong_20160715-154626-073.XML",
                                 null };
        fileTransfer.download("E:\\test\\1", remotePaths);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyWithEmptyRemotePath()
     */
    @Override
    @Test(expected = IllegalArgumentException.class)
    public void downloadEmptyWithEmptyRemotePath(){
        String[] remotePaths = {
                                 "/upload/Inbound/InventoryAdjustments/Archive/2016-07-15_11-58-58.389-INVENTORY_ADJUSTMENTS_3PL_feilong_20160715-154626-073.XML",
                                 "" };
        fileTransfer.download("E:\\test\\1", remotePaths);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyWithBlankRemotePath()
     */
    @Override
    @Test(expected = IllegalArgumentException.class)
    public void downloadEmptyWithBlankRemotePath(){
        String[] remotePaths = {
                                 "/upload/Inbound/InventoryAdjustments/Archive/2016-07-15_11-58-58.389-INVENTORY_ADJUSTMENTS_3PL_feilong_20160715-154626-073.XML",
                                 " " };
        fileTransfer.download("E:\\test\\1", remotePaths);
    }

}
