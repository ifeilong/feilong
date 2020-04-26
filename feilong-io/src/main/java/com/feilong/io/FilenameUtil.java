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
package com.feilong.io;

import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.StringUtil;

/**
 * The Class FilenameUtil.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.commons.io.FilenameUtils
 * @since 1.4.0
 */
public final class FilenameUtil{

    /** The Constant LOGGER. */
    private static final Logger     LOGGER       = LoggerFactory.getLogger(FilenameUtil.class);

    /**
     * 文件名称由文件名和扩展名组成,两者由小黑点分隔,扩展名通常是用来表示文件的类 别.
     * <p>
     * Windows 中整个文件名称最长 255 个字符(一个中文字算两个字符); <br>
     * DOS 中,文件名最长 8 字符,扩展名最长 3 字符,故又称 DOS 8.3 命名规则. <br>
     * 文件名称可仅有前半部,即无扩展名,如文件名称最短可以是 " 1 " 、 " C " 等. <br>
     * 给文件命名还应注意以下规则:
     * </p>
     * <ul>
     * <li>文件名不能包含下列任何字符之一(共 9 个): \/:*?"<>|</li>
     * <li>不能单独使用 " 设备名 " 作文件名. " 设备名 " 包括: con , aux , com0 ~ com9 , lpt0 ~ lpt9 , nul , prn</li>
     * <li>文件名不区分大小写,如 A.txt 和 a.TxT 表示同一文件</li>
     * </ul>
     * 
     * @see <a href="http://support.microsoft.com/kb/177506/zh-cn">错误消息: 文件名是无效的或不能包含任何以下字符</a>
     * @since 1.0.7
     */
    private static final String[][] MICROSOFT_PC = {                                           //
                                                     //            { "\\", "" }, // \
                                                     //  { "/", "" }, // /
                                                     { "\"", "" },                             // "
                                                     { ":", "" },                              // :
                                                     { "*", "" },                              // *
                                                     { "?", "" },                              // ?
                                                     { "<", "" },                              // <
                                                     { ">", "" },                              // >
                                                     { "|", "" },                              // |
    };

    /** Don't let anyone instantiate this class. */
    private FilenameUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 不同的操作系统 对系统文件名称有要求,此方法的作用就是处理这些文件名称.
     * 
     * @param fileName
     *            文件名称
     * @return 可用的文件名称
     * @see #MICROSOFT_PC
     * @since 1.0.7
     */
    public static String getFormatFileName(final String fileName){
        String formatFileName = fileName;

        for (int i = 0, j = MICROSOFT_PC.length; i < j; ++i){
            String[] arrayElement = MICROSOFT_PC[i];

            String oldChar = arrayElement[0];
            if (formatFileName.contains(oldChar)){
                String newChar = arrayElement[1];
                LOGGER.warn("formatFileName:[{}] contains oldChar:[{}],will replace newChar:[{}]", formatFileName, oldChar, newChar);
                formatFileName = StringUtil.replace(formatFileName, oldChar, newChar);
            }
        }
        return formatFileName;
    }

    //---------------------------------------------------------------

    /**
     * 获得带后缀的文件纯名称.
     * 
     * <p>
     * 如:F:/pie2.png,返回 pie2.png
     * </p>
     * 
     * @param fileName
     *            the file name
     * @return 如果 <code>fileName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>fileName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see java.io.File#getName()
     * @see org.apache.commons.io.FilenameUtils#getName(String)
     */
    public static String getFileName(String fileName){
        Validate.notBlank(fileName, "fileName can't be blank!");

        File file = new File(fileName);
        return file.getName();
    }

    /**
     * 获得文件的不带后缀名的名称.
     * 
     * <pre class="code">
     * Example 1: 
     * F:/pie2.png, 返回  F:/pie2
     * 
     * Example 2: 
     * pie2.png, 返回  pie2
     * </pre>
     * 
     * @param fileName
     *            文件名称
     * @return 获得文件的不带后缀名的名称
     * @see java.lang.String#substring(int, int)
     * @see org.apache.commons.io.FilenameUtils#getBaseName(String)
     */
    public static String getFilePreName(String fileName){
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    //---------------------------------------------------------------

    /**
     * 获得文件后缀名(不带. 的后缀),并返回原样字母.
     * 
     * <p>
     * 如果文件没有后缀名 返回 "" (EMPTY)
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * F:/pie2.png  返回  png
     * F:/pie2      返回 {@link StringUtils#EMPTY}
     * </pre>
     * 
     * </blockquote>
     * 
     * Gets the extension of a filename.
     * <p>
     * This method returns the textual part of the filename after the last dot. There must be no directory separator after the dot.
     * 
     * <pre class="code">
     * foo.txt      {@code -->} "txt"
     * a/b/c.jpg    {@code -->} "jpg"
     * a/b.txt/c    {@code -->} ""
     * a/b/c        {@code -->} ""
     * </pre>
     * <p>
     * The output will be the same irrespective of the machine that the code is running on.
     * 
     * @param fileName
     *            文件名称
     * @return 不带. 的后缀,<br>
     *         如果 <code>fileName</code> 是null,返回 {@link StringUtils#EMPTY}<br>
     * @see org.apache.commons.io.FilenameUtils#getExtension(String)
     * @see java.lang.String#substring(int, int)
     * @since 1.4.0
     */
    public static String getExtension(String fileName){
        return StringUtils.defaultString(FilenameUtils.getExtension(fileName));
    }

    /**
     * 获得文件后缀名,并返回<span style="color:red">小写字母</span>.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * FilenameUtil.getExtensionLowerCase("苍老师.AVI") = "avi"
     * FilenameUtil.getExtensionLowerCase("苍老师") = ""
     * FilenameUtil.getExtensionLowerCase(".苍老师") = "苍老师"
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>如果文件没有后缀名, 返回 {@link org.apache.commons.lang3.StringUtils#EMPTY}</li>
     * </ol>
     * </blockquote>
     * 
     * 
     * @param fileName
     *            文件名称
     * @return 如果 <code>fileName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>fileName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see org.apache.commons.io.FilenameUtils#getExtension(String)
     * @see #getExtension(String)
     * @since 1.7.1
     */
    public static String getExtensionLowerCase(String fileName){
        Validate.notBlank(fileName, "fileName can't be blank!");
        return getExtension(fileName).toLowerCase();
    }

    // [start] 解析文件名称

    /**
     * 将一个文件使用新的文件后缀,其余部分不变.
     * 
     * <p>
     * 如果一个文件没有后缀,将会添加 .+newPostfixName
     * </p>
     * 
     * <h3>Example 1:</h3>
     * 
     * <pre class="code">
     * String fileName="F:/pie2.png";
     * FileUtil.getNewFileName(fileName, "gif")
     * 
     * return F:/pie2.gif
     * </pre>
     *
     * @param fileName
     *            文件名称,比如 F:/pie2.png
     * @param newPostfixName
     *            不带.号, 比如 gif
     * @return 如果 <code>fileName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>fileName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>newPostfixName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>newPostfixName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     */
    public static String getNewFileName(String fileName,String newPostfixName){
        Validate.notBlank(fileName, "fileName can't be null/empty!");
        Validate.notBlank(newPostfixName, "newPostfixName can't be null/empty!");

        //---------------------------------------------------------------

        // 有后缀
        if (hasExtension(fileName)){
            return fileName.substring(0, fileName.lastIndexOf('.') + 1) + newPostfixName;
        }

        //---------------------------------------------------------------
        // 没有后缀直接拼接
        return fileName + "." + newPostfixName;
    }

    // [end]

    //---------------------------------------------------------------

    /**
     * 获得文件的最顶层 父文件夹名称.
     * 
     * <pre class="code">
     *   Example 1:
     *      "mp2-product\\mp2-product-impl\\src\\main\\java\\com\\mp2\\rpc\\impl\\item\\repo\\package-info.java"
     *      
     *      返回 mp2-product
     * </pre>
     *
     * @param pathname
     *            通过将给定路径名字符串转换为抽象路径名来创建一个新 File 实例.
     * @return 如果没有父文件夹,返回自己,比如 E:/ 直接返回 E:/ <br>
     *         如果 <code>pathname</code> 是blank,抛出 {@link IllegalArgumentException}
     * @since 1.0.7
     */
    public static String getFileTopParentName(String pathname){
        Validate.notBlank(pathname, "pathname can't be null/empty!");
        String parent = FileUtil.getParent(pathname);
        if (isNullOrEmpty(parent)){
            return pathname;
        }

        //---------------------------------------------------------------

        //递归
        String fileTopParentName = getFileTopParentName(parent);
        LOGGER.debug("pathname:[{}],fileTopParentName:[{}]", pathname, fileTopParentName);
        return fileTopParentName;
    }

    /**
     * 获得文件的最顶层父文件夹名称.
     * 
     * <pre class="code">
     *   Example 1:
     *      "mp2-product\\mp2-product-impl\\src\\main\\java\\com\\mp2\\rpc\\impl\\item\\repo\\package-info.java"
     *      
     *      返回 mp2-product
     * </pre>
     *
     * @param file
     *            the file
     * @return 如果没有父文件夹,返回自己,比如 E:/ 直接返回 E:/
     * @since 1.0.7
     */
    public static String getFileTopParentName(File file){
        Validate.notNull(file, "file can't be null!");

        File parent = file.getParentFile();
        if (isNullOrEmpty(parent)){
            String patch = file.getPath();//E:/--->E:\

            LOGGER.debug("parent isNullOrEmpty,return file patch:{}", patch);
            return patch;
        }

        //---------------------------------------------------------------
        //递归
        String fileTopParentName = getFileTopParentName(parent);

        LOGGER.debug("file.getAbsolutePath():[{}],fileTopParentName:[{}]", file.getAbsolutePath(), fileTopParentName);
        return fileTopParentName;
    }

    //---------------------------------------------------------------

    /**
     * 获得路径的所有 parent路径.
     * 
     * <p>
     * 目前的使用场景有, 创建远程sftp目录地址
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * String remoteDirectory = "/home/sftp-speedo/test/aa/bbb/ccc/ddd/201606160101/";
     * List{@code <String>} list = getParentPathList(remoteDirectory);
     * LOGGER.debug(JsonUtil.format(list));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * [
     *  "/home/sftp-speedo/test/aa/bbb/ccc/ddd",
     *  "/home/sftp-speedo/test/aa/bbb/ccc",
     *  "/home/sftp-speedo/test/aa/bbb",
     *  "/home/sftp-speedo/test/aa",
     *  "/home/sftp-speedo/test",
     *  "/home/sftp-speedo",
     *  "/home",
     *  "/"
     * ]
     * 
     * </pre>
     * 
     * </blockquote>
     *
     * @param path
     *            the path
     * @return 如果 <code>path</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>path</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.7.1
     */
    public static List<String> getParentPathList(String path){
        Validate.notBlank(path, "path can't be blank!");

        List<String> list = newArrayList();
        resolverGetParentPath(path, list);
        return list;
    }

    //---------------------------------------------------------------

    /**
     * Resolver get parent path.
     *
     * @param path
     *            the path
     * @param list
     *            the list
     */
    private static void resolverGetParentPath(String path,List<String> list){
        String parent = FileUtil.getParent(path);
        if (null != parent){
            parent = StringUtil.replace(parent, "\\", "/");
            if (isNotNullOrEmpty(parent)){
                list.add(parent);
                resolverGetParentPath(parent, list);//级联
            }
        }
    }

    /**
     * 判断文件名是否有后缀.
     * 
     * @param fileName
     *            the file name
     * @return true, if successful
     * @see org.apache.commons.io.FilenameUtils#indexOfExtension(String)
     * @since 1.4.0
     * @since 1.11.0 change to private
     */
    private static boolean hasExtension(String fileName){
        return StringUtils.INDEX_NOT_FOUND != FilenameUtils.indexOfExtension(fileName);
    }
}
