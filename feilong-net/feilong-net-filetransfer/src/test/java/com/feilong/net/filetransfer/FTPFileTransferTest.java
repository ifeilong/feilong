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

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import com.feilong.core.lang.SystemUtil;
import com.feilong.io.entity.FileInfoEntity;
import com.feilong.json.JsonUtil;

/**
 * The Class FTPUtilTest.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 */
@ContextConfiguration(value = { "classpath*:spring/spring-ftp.xml" })
@SuppressWarnings("squid:S2699") //Tests should include assertions //https://stackoverflow.com/questions/10971968/turning-sonar-off-for-certain-code
public class FTPFileTransferTest extends FileTransferTest{

    /** The file transfer. */
    @Autowired
    @Qualifier("ftpFileTransfer")
    private FileTransfer fileTransfer;

    /** The remote directory. */
    // private final String remoteDirectory =
    // "/webstore/InlineSales_Test/2011-07-05/";

    private final String remoteDirectory = "/upload/test/20160616/201606160101";

    /**
     * Inits the.
     */
    @Before
    public void init(){
    }

    /**
     * 传送单个文件.
     */
    @Override
    @Test
    public void upload() throws Exception{
        String singleLocalFileFullPath = SystemUtil.USER_HOME + "/Downloads/接口.png";
        fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
    }

    /**
     * 传送文件夹.
     */
    @Override
    @Test
    public void uploadDir() throws Exception{
        String singleLocalFileFullPath = "F:\\2013-12-04-1938";
        fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
    }

    /**
     * 批量传文件.
     */
    @Override
    @Test
    public void uploadDirs() throws Exception{
        String[] batchLocalFileFullPaths = { "E:\\test", "E:\\1.jpg" };
        fileTransfer.upload(remoteDirectory, batchLocalFileFullPaths);

        String singleLocalFileFullPath = "E:\\config.jsp";
        fileTransfer.upload(remoteDirectory, singleLocalFileFullPath);
    }

    /**
     * 传送文件夹(中文目录).
     * 
     * @throws Exception
     *             the exception
     */
    @Override
    @Test
    public void sendLocalFileToRemote_dir_chinese() throws Exception{
        String localFileFullPath = "E:\\test - 副本";
        fileTransfer.upload(localFileFullPath, remoteDirectory);
    }

    /**
     * 删除单个文件
     */
    @Override
    @Test
    public void delete() throws Exception{
        String remoteAbsolutePath = "/webstore/InlineSales_Test/2011-07-05/1.jpg";
        fileTransfer.delete(remoteAbsolutePath);
    }

    /**
     * 删除整个目录
     */
    @Override
    @Test
    public void deleteDir() throws Exception{
        String remoteAbsolutePath = "/webstore/InlineSales_Test/2011-07-05/test";
        fileTransfer.delete(remoteAbsolutePath);
    }

    @Override
    @Test
    public void deleteDirEmpty() throws Exception{
        String remoteAbsolutePath = "/webstore/InlineSales_Test/a/";
        fileTransfer.delete(remoteAbsolutePath);
    }

    @Override
    @Test
    public void deleteNotExist() throws Exception{
        String remoteAbsolutePath = "/webstore/InlineSales_Test/2011-07-051/";
        fileTransfer.delete(remoteAbsolutePath);
    }

    /**
     * Delete_not_exist1.
     */
    @Test
    public void delete_not_exist1(){
        String remoteAbsolutePath = "/";
        fileTransfer.delete(remoteAbsolutePath);
    }

    @Override
    @Test
    public void testGetFileEntityMap(){
        String remotePath = "/webstore/InlineSales_Test/2011-07-05/2013-12-04-1938";
        String[] fileNames = { "SportActivity.dat", "SubCategory.dat", "aaa" };
        Map<String, FileInfoEntity> fileEntityMap = fileTransfer.getFileEntityMap(remotePath, fileNames);

        LOGGER.debug("fileEntityMap:{}", JsonUtil.format(fileEntityMap));
    }
}
