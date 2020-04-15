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

/**
 * 通用的 FileTransferTest.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public abstract class FileTransferTest extends AbstractFileTransferTest{

    /**
     * 传送单个文件.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void upload() throws Exception;

    /**
     * 传送文件夹.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void uploadDir() throws Exception;

    /**
     * 批量传文件.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void uploadDirs() throws Exception;

    /**
     * 传送文件夹(中文目录).
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void sendLocalFileToRemote_dir_chinese() throws Exception;

    //---------------------------------------------------------------

    /**
     * 删除一个普通文件/文件夹.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void delete() throws Exception;

    /**
     * 删除文件夹.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void deleteDir() throws Exception;

    /**
     * 删除空的文件夹.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void deleteDirEmpty() throws Exception;

    /**
     * 删除不存在的文件/文件夹.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void deleteNotExist() throws Exception;

    //---------------------------------------------------------------

    /**
     * 获得某特定文件夹下面 指定文件名相关信息.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void testGetFileEntityMap() throws Exception;
}
