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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.feilong.json.JsonUtil;

/**
 * The Class SFTPUtilTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public class SFTPFileTransferTest extends FileTransferTest{

    /** The file transfer. */
    @Autowired
    @Qualifier("sftpFileTransfer")
    private FileTransfer fileTransfer;

    /** The remote directory. */
    //private final String        remoteDirectory = "/home/bzuser/test";
    //    private final String        remoteDirectory = "/home/sftp-speedo/test/20160616/201606160101";
    private final String remoteDirectory = "/upload/Inbound/BuyableandDisplayable/";

    @Override
    @Test
    public void upload() throws Exception{
        String singleLocalFileFullPath = "E:\\1.txt";
        fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
        assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferTest#sendLocalFileToRemote_dir()
     */
    @Override
    @Test
    public void uploadDir() throws Exception{
        String singleLocalFileFullPath = "E:\\test";
        fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
        assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferTest#sendLocalFileToRemote_dirs()
     */
    @Override
    @Test
    public void uploadDirs() throws Exception{
        String[] batchLocalFileFullPaths = { "E:\\test", "E:\\1.txt", "E:\\test1" };
        fileTransfer.upload(remoteDirectory, batchLocalFileFullPaths);
        assertTrue(true);
    }

    //---------------------------------------------------------------
    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransferTest#delete()
     */
    @Override
    @Test
    public void delete() throws Exception{
        String remoteAbsolutePath = "/home/appuser/test/pg_ctl.conf";
        fileTransfer.delete(remoteAbsolutePath);
        assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransferTest#delete_dir()
     */
    @Override
    @Test
    public void deleteDir() throws Exception{
        String remoteAbsolutePath = "/home/feilongtest/out/test/2011-07-07";
        fileTransfer.delete(remoteAbsolutePath);
        assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransferTest#delete_dir_empty()
     */
    @Override
    @Test
    public void deleteDirEmpty() throws Exception{
        String remoteAbsolutePath = "/home/appuser/test/2013-01-06";
        fileTransfer.delete(remoteAbsolutePath);
        assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransferTest#delete_not_exist()
     */
    @Override
    @Test
    public void deleteNotExist() throws Exception{
        String remoteAbsolutePath = "/home/appuser/test/2011-07-051/";
        fileTransfer.delete(remoteAbsolutePath);
        assertTrue(true);
    }

    /**
     * Delete_not_exist1.
     * 
     * @throws Exception
     *             the exception
     */
    @Test
    public void deleteNotExist1() throws Exception{
        String remoteAbsolutePath = "/";
        fileTransfer.delete(remoteAbsolutePath);
        assertTrue(true);
    }

    //------------------------------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.filetransfer.FileTransferTest#testGetFileEntityMap()
     */
    @Override
    @Test
    public void testGetFileEntityMap() throws Exception{
        String remoteAbsolutePath = "/home/appuser/test/2013-12-04-1938";
        String[] fileNames = { "SportActivity.dat", "SubCategory.dat", "aaa" };
        LOGGER.debug(JsonUtil.format(fileTransfer.getFileEntityMap(remoteAbsolutePath, fileNames)));
        assertTrue(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.tools.net.FileTransferTest#sendLocalFileToRemote_dir_chinese()
     */
    @Override
    public void sendLocalFileToRemote_dir_chinese() throws Exception{
        assertTrue(true);
    }

    //    /**
    //     * Gets the working directory.
    //     *
    //     * @return the working directory
    //     */
    //    private String getWorkingDirectory(){
    //        try{
    //            return channelSftp.pwd();
    //        }catch (SftpException e){
    //            LOGGER.error("", e);
    //            throw new FileTransferException(e);
    //        }
    //    }

}
