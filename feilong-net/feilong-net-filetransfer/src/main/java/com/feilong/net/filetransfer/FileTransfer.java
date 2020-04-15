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

import com.feilong.io.entity.FileInfoEntity;

/**
 * 通用的文件传输 .
 * 
 * <h3>开放出来的方法有:</h3>
 * 
 * <blockquote>
 * 下载:
 * <ul>
 * <li>{@link #download(String, String...)}</li>
 * </ul>
 * 
 * 上传:
 * <ul>
 * <li>{@link #upload(String, String...)}</li>
 * </ul>
 * 
 * 删除:
 * <ul>
 * <li>{@link #delete(String[])}</li>
 * </ul>
 * 
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.7.1
 */
public interface FileTransfer{

    //-----------------------------下载-----------------------------------------------------

    /**
     * 打开一次链接,将一批远程文件/文件夹 (<code>remotePaths</code>)循环下载到本地目录 <code>localAbsoluteDirectoryPath</code>.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果 <code>localAbsoluteDirectoryPath</code>本地目录地址不存在,会自动<b>级联创建</b></li>
     * <li>如果 <code>remotePaths</code>里面有文件是个文件(file),会将此文件直接下载到 <code>localAbsoluteDirectoryPath</code> 目录下面</li>
     * <li>
     * 如果 <code>remotePaths</code>里面有文件是个文件夹(Directory),会先在 <b>localAbsoluteDirectoryPath</b> 下面创建同名文件夹,再<b>递归下载</b>文件(相当于整个文件夹下载下来)
     * </li>
     * <li>如果 <code>localAbsoluteDirectoryPath</code> 目录下面已经存在和<code>remotePaths</code>里面同名的文件,那么会<b>下载覆盖</b></li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * String[] remotePaths = {
     *                          "/upload/Inbound/InventoryAdjustments/Archive/2016-07-15_11-58-58.389-INVENTORY_ADJUSTMENTS_3PL_20160715-154626-073.XML",
     *                          "/upload/Inbound/InventoryAdjustments/Archive/2016-07-22_10-46-00.318-INVENTORY_ADJUSTMENTS_3PL_20160722-144626-073.XML" };
     * String localAbsoluteDirectoryPath = "E:\\test\\1";
     * fileTransfer.download(localAbsoluteDirectoryPath, remotePaths);
     * </pre>
     * 
     * </blockquote>
     * 
     * @param localAbsoluteDirectoryPath
     *            本地绝对的目录<br>
     *            如果不存在,支持级联创建 <br>
     *            如果 <code>localAbsoluteDirectoryPath</code> 是null,抛出 {@link NullPointerException}<br>
     *            如果 <code>localAbsoluteDirectoryPath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @param remotePaths
     *            一批远程文件远程路径,<br>
     *            可以是文件,也可以文件夹 <br>
     *            如果 <code>remotePaths</code> 是null,抛出 {@link NullPointerException}<br>
     *            如果 <code>remotePaths</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     *            任意一个值 null或者empty, {@link IllegalArgumentException}
     */
    void download(String localAbsoluteDirectoryPath,String...remotePaths);

    //-----------------------------上传-----------------------------------------------------
    /**
     * 支持将多个不定路径文件 <code>batchLocalFileFullPaths</code> 上传到远程文件夹 <code>remoteDirectory</code>.
     * 
     * <pre class="code">
     * 
     * String[] batchLocalFileFullPaths = { "E:\\test", "E:\\1.jpg" };
     * 
     * String remoteDirectory = "/webstore/InlineSales_Test/2011-07-05/";
     * </pre>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果 localFileFullPath 是个文件(file), 会将此文件直接上传到 remoteDirectory 目录下面</li>
     * <li>如果 localFileFullPath 是个文件夹(Directory), 首先会先在 remoteDirectory 下面创建同名文件夹,并<b>递归上传</b>文件(相当于整个文件夹上传)</li>
     * <li>新的文件的名称和你上传文件的名称一样的,也就是,你传的什么名称文件/文件夹到服务器就是什么名称文件/文件夹</li>
     * </ol>
     * </blockquote>
     *
     * @param remoteDirectory
     *            远程文件夹名称,你要传到哪个文件夹下面
     * @param batchLocalFileFullPaths
     *            上传的文件名(数组),全路径 <br>
     *            不能为 null或者empty,否则 {@link IllegalArgumentException}<br>
     *            任意一个值 null或者empty, {@link IllegalArgumentException}<br>
     *            任意一个值localFileFullPath文件不存在, {@link IllegalArgumentException}
     * @return 全部成功返回true,否则一旦有失败则返回false<br>
     *         如果 <code>remoteDirectory</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>remoteDirectory</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    boolean upload(String remoteDirectory,String...batchLocalFileFullPaths);

    //-----------------------------删除-----------------------------------------------------
    /**
     * 删除远程的一组文件 <code>remoteAbsolutePaths</code>.
     * 
     * <pre class="code">
     * 
     * String[] remoteAbsolutePaths = { "/webstore/InlineSales_Test/2011-07-05/test", "/webstore/InlineSales_Test/2011-07-05/1.cvs" };
     * </pre>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>不管是windows还是linux、unix,都不能在同一目录结构下创建同名的文件夹和文件</li>
     * <li>如果 remoteAbsolutePath 是个文件(file), 会将此文件直接删除</li>
     * <li>如果 remoteAbsolutePath 是个文件夹(Directory),首先<b>递归删除</b>该文件夹下面 所有的文件/文件夹再删除此文件夹</li>
     * <li>不支持删除 '/'根目录 (危险)</li>
     * </ol>
     * </blockquote>
     * 
     * @param remoteAbsolutePaths
     *            一组文件,绝对路径 <br>
     *            任意一个值 null或者empty, {@link IllegalArgumentException}<br>
     *            任意一个值= / , {@link IllegalArgumentException} 危险!!
     * @return 如果 <code>remoteAbsolutePaths</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>remoteAbsolutePaths</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     * 
     *         删除成功返回true,否则false<br>
     *         不支持 删除全部 危险<br>
     *         一旦有一个文件 null或者empty抛错
     */
    boolean delete(String...remoteAbsolutePaths);

    //----------------------------读取-----------------------------------------------------
    /**
     * 获得某特定文件夹下面指定文件名相关信息.
     *
     * @param remotePath
     *            远程地址
     * @param fileNames
     *            文件名称组
     * @return 如果 <code>remotePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>remotePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>fileNames</code> 是null 或者是 empty,那么返回这个 <code>remotePath</code> 下面所有的文件<br>
     */
    Map<String, FileInfoEntity> getFileEntityMap(String remotePath,String...fileNames);
}
