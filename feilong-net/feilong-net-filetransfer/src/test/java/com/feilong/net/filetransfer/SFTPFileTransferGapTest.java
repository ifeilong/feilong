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
import org.springframework.test.context.ContextConfiguration;

import com.feilong.json.JsonUtil;

@SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
@ContextConfiguration(value = { "classpath*:spring/spring-sftp-gap.xml" })
public class SFTPFileTransferGapTest extends FileTransferTest{

    /** The file transfer. */
    @Autowired
    @Qualifier("sftpFileTransfer")
    private FileTransfer fileTransfer;

    private final String remoteDirectory = "/upload/Inbound/BuyableandDisplayable/";

    @Override
    @Test
    public void upload(){
        String singleLocalFileFullPath = "E:\\1.txt";
        fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
    }

    @Override
    @Test
    public void uploadDir() {
        String singleLocalFileFullPath = "E:\\test";
        fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
    }

    @Override
    @Test
    public void uploadDirs() {
        String[] batchLocalFileFullPaths = { "E:\\test", "E:\\1.txt", "E:\\test1" };
        fileTransfer.upload(remoteDirectory, batchLocalFileFullPaths);
    }

    //---------------------------------------------------------------
    @Override
    @Test
    public void delete() {
        String remoteAbsolutePath = "/home/appuser/test/pg_ctl.conf";
        fileTransfer.delete(remoteAbsolutePath);
    }

    @Override
    @Test
    public void deleteDir() {
        String remoteAbsolutePath = "/home/feilongtest/out/test/2011-07-07";
        fileTransfer.delete(remoteAbsolutePath);
    }

    @Override
    @Test
    public void deleteDirEmpty(){
        String remoteAbsolutePath = "/home/appuser/test/2013-01-06";
        fileTransfer.delete(remoteAbsolutePath);
    }

    @Override
    @Test
    public void deleteNotExist() {
        String remoteAbsolutePath = "/home/appuser/test/2011-07-051/";
        fileTransfer.delete(remoteAbsolutePath);
    }

    @Test
    public void deleteNotExist1() {
        String remoteAbsolutePath = "/";
        fileTransfer.delete(remoteAbsolutePath);
    }

    //------------------------------------------------------------------------------------------

    @Override
    @Test
    public void testGetFileEntityMap() {
        String remoteAbsolutePath = "/home/appuser/test/2013-12-04-1938";
        String[] fileNames = { "SportActivity.dat", "SubCategory.dat", "aaa" };
        LOGGER.debug(JsonUtil.format(fileTransfer.getFileEntityMap(remoteAbsolutePath, fileNames)));
    }

    @Test
    public void testGetFileEntityNotexist() {
        String remoteAbsolutePath = "/home/appuser/test/2013-12-04-1938";
        LOGGER.debug(JsonUtil.format(fileTransfer.getFileEntityMap(remoteAbsolutePath)));
    }

    @Test
    public void testGetFileEntityEMpty() {
        String remoteAbsolutePath = "/upload/uat";
        LOGGER.debug(JsonUtil.format(fileTransfer.getFileEntityMap(remoteAbsolutePath)));
    }

    @Test
    public void testGetFileEntityEMpty1(){
        String remoteAbsolutePath = "/upload/test";
        LOGGER.debug(JsonUtil.format(fileTransfer.getFileEntityMap(remoteAbsolutePath)));
    }

    @Test
    public void testGetFileEntityEMpty12() {
        String remoteAbsolutePath = "/upload/speedo";
        LOGGER.debug(JsonUtil.format(fileTransfer.getFileEntityMap(remoteAbsolutePath)));
    }

    @Test
    public void testGetFileEntityEMpty12222(){
        String remoteAbsolutePath = "/upload/test/HERSCHEL";
        LOGGER.debug(JsonUtil.format(fileTransfer.getFileEntityMap(remoteAbsolutePath)));
    }

    @Override
    public void sendLocalFileToRemote_dir_chinese() {
    }

}
