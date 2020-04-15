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
 * The Class FileTransferDownloadTest.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
public abstract class FileTransferDownloadTest extends AbstractFileTransferTest{

    //---------------------------------------------------------------

    /**
     * 下载单个文件.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void downloadFile() throws Exception;

    /**
     * 下载整个文件夹.
     * 
     * @throws Exception
     *             the exception
     */
    protected abstract void downloadDir() throws Exception;

    //---------------------------------------------------------------

    /**
     * Download null local absolute directory path.
     */
    protected abstract void downloadNullLocalAbsoluteDirectoryPath();

    /**
     * Download empty local absolute directory path.
     */
    protected abstract void downloadEmptyLocalAbsoluteDirectoryPath();

    /**
     * Download blank local absolute directory path.
     */
    protected abstract void downloadBlankLocalAbsoluteDirectoryPath();

    /**
     * Download null remote paths.
     */
    protected abstract void downloadNullRemotePaths();

    /**
     * Download empty remote paths.
     */
    protected abstract void downloadEmptyRemotePaths();

    /**
     * Download empty with null remote path.
     */
    protected abstract void downloadEmptyWithNullRemotePath();

    /**
     * Download empty with empty remote path.
     */
    protected abstract void downloadEmptyWithEmptyRemotePath();

    /**
     * Download empty with blank remote path.
     */
    public abstract void downloadEmptyWithBlankRemotePath();
}
