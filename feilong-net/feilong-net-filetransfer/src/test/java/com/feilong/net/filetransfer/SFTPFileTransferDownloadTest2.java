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

import com.feilong.net.filetransfer.FileTransfer;

/**
 * The Class SFTPFileTransferDownloadTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class SFTPFileTransferDownloadTest2 extends FileTransferDownloadTest{

    /** The file transfer. */
    @Autowired
    @Qualifier("sftpFileTransfer")
    private FileTransfer fileTransfer;

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyWithBlankRemotePath()
     */
    @Override
    @Test
    public void downloadEmptyWithBlankRemotePath(){
        fileTransfer.getFileEntityMap("/Article Master/miadidas/History", "ChinaAttributes20160601041056.txt");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadFile()
     */
    @Override
    protected void downloadFile() throws Exception{
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadDir()
     */
    @Override
    protected void downloadDir() throws Exception{
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadNullLocalAbsoluteDirectoryPath()
     */
    @Override
    protected void downloadNullLocalAbsoluteDirectoryPath(){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyLocalAbsoluteDirectoryPath()
     */
    @Override
    protected void downloadEmptyLocalAbsoluteDirectoryPath(){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadBlankLocalAbsoluteDirectoryPath()
     */
    @Override
    protected void downloadBlankLocalAbsoluteDirectoryPath(){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadNullRemotePaths()
     */
    @Override
    protected void downloadNullRemotePaths(){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyRemotePaths()
     */
    @Override
    protected void downloadEmptyRemotePaths(){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyWithNullRemotePath()
     */
    @Override
    protected void downloadEmptyWithNullRemotePath(){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferDownloadTest#downloadEmptyWithEmptyRemotePath()
     */
    @Override
    protected void downloadEmptyWithEmptyRemotePath(){
        // TODO Auto-generated method stub

    }
}
