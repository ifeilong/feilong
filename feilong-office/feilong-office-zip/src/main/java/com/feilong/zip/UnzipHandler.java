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
package com.feilong.zip;

/**
 * 解压缩的接口.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.11.4
 */
public interface UnzipHandler{

    /**
     * 将指定的压缩文件 <code>unZipFilePath</code>, 解压缩到指定的目录 {@code outputDirectory}.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * UnzipHandler unzipHandler = new CompressUnzipHandler();
     * unzipHandler.unzip(
     *                 SystemUtil.USER_HOME+ "/feilong/logs/test.zip", //需要被解压的zip文件
     *                 SystemUtil.USER_HOME+ "/feilong/logs/" + DateUtil.nowString(TIMESTAMP) + "Compress"); // 解压到文件路径
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <p>
     * 如果 <code>unZipFilePath</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>unZipFilePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 如果 <code>outputDirectory</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>outputDirectory</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     * 
     * @param unZipFilePath
     *            需要被解压缩的文件路径
     * @param outputDirectory
     *            解压到哪个目录
     */
    void unzip(String unZipFilePath,String outputDirectory);
}
