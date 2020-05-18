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
 * 压缩.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 2.1.0
 */
public interface ZipHandler{

    /**
     * 压缩制定的文件 <code>tobeZipFilePath</code>, 成新的 {@code outputZipPath}.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * ZipHandler zipHandler = new AntZipHandler();
     * zipHandler.zip(
     *                 SystemUtils.USER_HOME+ "/workspace/resources/product-reports20200324.xlsx", //需要压缩的文件夹 或者文件
     *                 SystemUtils.USER_HOME+ "/feilong/logs/" + DateUtil.nowString(TIMESTAMP) + "zip"); //  需要压缩输出到的文件
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <p>
     * 如果 <code>tobeZipFilePath</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>tobeZipFilePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 如果 <code>outputZipPath</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>outputZipPath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     *
     * @param tobeZipFilePath
     *            需要 压缩的目录或者文件
     * @param outputZipPath
     *            需要压缩输出到的文件
     */
    void zip(String tobeZipFilePath,String outputZipPath);
}
