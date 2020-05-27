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

import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;
import static com.feilong.io.entity.FileType.DIRECTORY;
import static com.feilong.io.entity.FileType.FILE;
import static com.feilong.io.entity.FileWriteMode.APPEND;
import static com.feilong.core.lang.StringUtil.EMPTY;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;
import com.feilong.io.entity.FileWriteMode;
import com.feilong.lib.io.FileUtils;
import com.feilong.core.Validate;
import com.feilong.lib.lang3.tuple.Pair;

/**
 * {@link File}文件操作.
 * 
 * <h3>File的几个属性:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link File#getAbsolutePath()} 得到的是全路径</li>
 * <li>{@link File#getPath()} 得到的是构造file的时候的路径</li>
 * <li>{@link File#getCanonicalPath()} 可以看到CanonicalPath不但是全路径,而且把..或者.这样的符号解析出来.</li>
 * </ul>
 * </blockquote>
 * 
 * 
 * <h3>关于大小:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@link FileUtils#ONE_KB} 1024</li>
 * <li>{@link FileUtils#ONE_MB} 1024 * 1024 1048576</li>
 * <li>{@link FileUtils#ONE_GB} 1024 * 1024 * 1024 1073741824
 * <p style="color:red">
 * <b>注意,{@link Integer#MAX_VALUE}=2147483647 是2G大小</b>
 * </p>
 * </ul>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see java.io.File
 * @see com.feilong.lib.io.FilenameUtils
 * @see com.feilong.lib.io.FileUtils
 * @since 1.0.0
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
public final class FileUtil{

    /** The Constant LOGGER. */
    private static final Logger            LOGGER                = LoggerFactory.getLogger(FileUtil.class);

    //---------------------------------------------------------------

    /**
     * The number of bytes in a kilobyte.
     */
    public static final Long               ONE_KB                = 1024L;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long               ONE_MB                = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long               ONE_GB                = ONE_KB * ONE_MB;

    /**
     * The number of bytes in a terabyte.
     */
    public static final long               ONE_TB                = ONE_KB * ONE_GB;

    //---------------------------------------------------------------

    /** 默认缓冲大小 10k <code>{@value}</code>. */
    public static final int                DEFAULT_BUFFER_LENGTH = (int) (10 * ONE_KB);

    /** 除数和单位的map,必须是有顺序的 从大到小. */
    private static final Map<Long, String> DIVISOR_AND_UNIT_MAP  = toMapUseEntrys(
                    Pair.of(ONE_TB, "TB"),                                                                 //(Terabyte,太字节,或百万兆字节)=1024GB,其中1024=2^10 ( 2 的10次方)。 
                    Pair.of(ONE_GB, "GB"),                                                                 //(Gigabyte,吉字节,又称“千兆”)=1024MB, 
                    Pair.of(ONE_MB, "MB"),                                                                 //(Megabyte,兆字节,简称“兆”)=1024KB, 
                    Pair.of(ONE_KB, "KB"));                                                                //(Kilobyte 千字节)=1024B

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private FileUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将文件 <code>filePath</code> 转成 <code>byte[] bytes</code>.
     *
     * @param filePath
     *            文件路径
     * @return 如果 <code>fileName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>fileName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #toByteArray(File)
     * @see java.io.ByteArrayOutputStream#toByteArray()
     * @since 1.2.1
     */
    public static byte[] toByteArray(String filePath){
        Validate.notBlank(filePath, "filePath can't be blank!");

        File file = new File(filePath);
        return toByteArray(file);
    }

    //---------------------------------------------------------------

    /**
     * 将文件转成 <code>byte[] bytes</code>.
     *
     * @param file
     *            file
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     * @see #getFileInputStream(File)
     * @see java.io.ByteArrayOutputStream#toByteArray()
     * @see com.feilong.lib.io.FileUtils#readFileToByteArray(File)
     * @see com.feilong.lib.io.IOUtils#toByteArray(InputStream, int)
     */
    public static byte[] toByteArray(File file){
        Validate.notNull(file, "file can't be null!");

        //---------------------------------------------------------------
        try{
            return FileUtils.readFileToByteArray(file);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 获得 {@link java.io.FileOutputStream FileOutputStream} 文件输出流.
     * 
     * <p>
     * 该方法输出是使用 覆盖模式, 如果要使用追加模式, 请调用 {@link #getFileOutputStream(String, FileWriteMode)}
     * </p>
     * 
     * <p>
     * {@link java.io.FileOutputStream FileOutputStream} 用于写入诸如图像数据之类的原始字节的流.
     * </p>
     * 
     * <p>
     * 如果要写入字符流,请考虑使用 {@link java.io.FileWriter}.
     * </p>
     * 
     * @param filePath
     *            文件路径
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see java.io.FileOutputStream#FileOutputStream(String)
     * @see #getFileOutputStream(String, boolean)
     */
    public static FileOutputStream getFileOutputStream(String filePath){
        Validate.notBlank(filePath, "filePath can't be blank!");

        return getFileOutputStream(filePath, false);//默认 append 是 false
    }

    /**
     * 获得 {@link java.io.FileOutputStream FileOutputStream} 文件输出流.
     * 
     * <p>
     * {@link java.io.FileOutputStream FileOutputStream} 用于写入诸如图像数据之类的原始字节的流.
     * </p>
     * 
     * <p>
     * 如果要写入字符流,请考虑使用 {@link java.io.FileWriter}.
     * </p>
     *
     * @param filePath
     *            the file path
     * @param fileWriteMode
     *            the file write mode
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>fileWriteMode</code> 是null,抛出 {@link NullPointerException}<br>
     * @see #getFileOutputStream(String, boolean)
     * @since 1.2.0
     */
    public static FileOutputStream getFileOutputStream(String filePath,FileWriteMode fileWriteMode){
        Validate.notBlank(filePath, "filePath can't be blank!");
        Validate.notNull(fileWriteMode, "fileWriteMode can't be null!");

        //---------------------------------------------------------------
        boolean append = fileWriteMode == APPEND;
        return getFileOutputStream(filePath, append);
    }

    //---------------------------------------------------------------

    /**
     * 获得 {@link java.io.FileOutputStream FileOutputStream} 文件输出流.
     * 
     * <p>
     * {@link java.io.FileOutputStream FileOutputStream} 用于写入诸如图像数据之类的原始字节的流.
     * </p>
     * 
     * <p>
     * 如果要写入字符流,请考虑使用 {@link java.io.FileWriter}.
     * </p>
     * 
     * @param filePath
     *            the file path
     * @param append
     *            if {@code true}, then bytes will be added to the end of the file rather than overwriting
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see java.io.FileOutputStream#FileOutputStream(String, boolean)
     * @see com.feilong.lib.io.FileUtils#openOutputStream(File, boolean)
     * @since 1.2.0
     */
    //默认 Access Modifiers 权限修饰符
    static FileOutputStream getFileOutputStream(String filePath,boolean append){
        Validate.notBlank(filePath, "filePath can't be blank!");

        return getFileOutputStream(new File(filePath), append);
    }

    /**
     * 获得 {@link java.io.FileOutputStream} 文件输出流 (或其他文件写入对象)打开文件进行写入 .
     * 
     * <p>
     * {@link java.io.FileOutputStream} 用于写入诸如图像数据之类的原始字节的流.<br>
     * 如果要写入字符流,请考虑使用 {@link java.io.FileWriter}.
     * </p>
     *
     * @param file
     *            the file
     * @param append
     *            if {@code true}, then bytes will be added to the end of the file rather than overwriting
     * @return the file output stream
     * @see com.feilong.lib.io.FileUtils#openOutputStream(File, boolean)
     * @since 1.4.0
     */
    //默认 Access Modifiers 权限修饰符
    static FileOutputStream getFileOutputStream(File file,boolean append){
        Validate.notNull(file, "file can't be null!");

        //---------------------------------------------------------------
        try{
            return FileUtils.openOutputStream(file, append);
        }catch (IOException e){
            throw new UncheckedIOException("file:[" + file + "],append:" + append, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 从文件系统中的文件 <code>filePath</code> 中获得输入字节.
     * 
     * <p>
     * {@link java.io.FileInputStream} 用于读取诸如图像数据之类的原始字节流.
     * </p>
     * 
     * <p>
     * 要读取字符流,请考虑使用 {@link java.io.FileReader}
     * </p>
     *
     * @param filePath
     *            该文件通过文件系统中的路径名 filePath 指定.
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #getFileInputStream(File)
     */
    public static FileInputStream getFileInputStream(String filePath){
        Validate.notBlank(filePath, "filePath can't be blank!");

        return getFileInputStream(new File(filePath));
    }

    /**
     * 从文件系统中的文件 <code>file</code> 中获得输入字节.
     * 
     * <p>
     * {@link java.io.FileInputStream} 用于读取诸如图像数据之类的原始字节流.
     * </p>
     * 
     * <p>
     * 要读取字符流,请考虑使用 {@link java.io.FileReader}
     * </p>
     * 
     * <p>
     * 如果指定文件不存在;或者它是一个目录,而不是一个常规文件;亦或因为其他某些原因而无法打开进行读取,则抛出 {@link FileNotFoundException}
     * </p>
     *
     * @param file
     *            为了进行读取而打开的文件.
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     * @see com.feilong.lib.io.FileUtils#openInputStream(File)
     */
    public static FileInputStream getFileInputStream(File file){
        Validate.notNull(file, "file can't be null!");

        //---------------------------------------------------------------
        try{
            return FileUtils.openInputStream(file);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 判断一个目录是否是空目录 <span style="color:red">(判断的原则:里面没有文件)</span>.
     *
     * @param directory
     *            指定一个存在的文件夹
     * @return
     *         <ul>
     *         <li>如果directory isNullOrEmpty,抛出 {@link IllegalArgumentException}</li>
     *         <li>如果directory don't exists,抛出 {@link IllegalArgumentException}</li>
     *         <li>如果directory is not Directory,抛出 {@link IllegalArgumentException}</li>
     *         <li>return file.list() ==0</li>
     *         </ul>
     * @see com.feilong.lib.io.FileUtils#sizeOf(File)
     * @see com.feilong.lib.io.FileUtils#sizeOfDirectory(File)
     */
    public static boolean isEmptyDirectory(String directory){
        Validate.notBlank(directory, "directory can't be null/empty!");

        //---------------------------------------------------------------

        File file = new File(directory);
        Validate.isTrue(file.exists(), "directory file " + directory + " don't exists!");
        Validate.isTrue(file.isDirectory(), "directory file " + directory + " is not Directory!");

        //---------------------------------------------------------------
        // Returns an array of strings naming the files and directories in the directory denoted by this abstract pathname.
        // 如果此抽象路径名不表示一个目录,那么此方法将返回 null

        // ubuntu 已经 测试ok
        File[] listFiles = file.listFiles();
        int fileListLength = listFiles.length;
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("file :[{}] list length:[{}]", directory, fileListLength);
            for (File tempFile : listFiles){
                LOGGER.debug("[{}] [{}]", tempFile.getName(), tempFile.isDirectory() ? DIRECTORY : FILE);
            }
        }
        return 0 == fileListLength;
    }

    // [start] 文件夹操作(createDirectory/deleteFileOrDirectory/deleteFileOrDirectory)
    /**
     * 创建文件夹,by文件路径 <code>filePath</code> ,支持级联创建.
     * 
     * <h3>注意:</h3>
     * 
     * <ol>
     * <li>此处<span style="color:red">参数是文件路径</span>,如果需要传递文件夹路径自动创建文件夹,那么请调用 {@link #createDirectory(String)}</li>
     * <li>对于不存在的文件夹/文件夹: "E:\\test\\1\\2011-07-07" 这么一个路径,没有办法自动区别到底你是要创建文件还是文件夹</li>
     * <li>{@link File#isDirectory()} 这个方法,必须文件存在 才能判断</li>
     * <li>如果文件所属的文件夹已经存在,那么仅会记录debug level的日志</li>
     * </ol>
     * 
     * <h3>代码流程:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}</li>
     * <li>如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}</li>
     * <li>{@link #getParent(String) getParent}(filePath)</li>
     * <li>{@link #createDirectory(String) createDirectory}(directory)</li>
     * </ol>
     * </blockquote>
     *
     * @param filePath
     *            <span style="color:red">文件路径</span>
     * @see #getParent(String)
     * @see #createDirectory(String)
     * @since 1.2.0
     */
    public static void createDirectoryByFilePath(String filePath){
        Validate.notBlank(filePath, "filePath can't be null/empty!");

        //---------------------------------------------------------------
        String directory = getParent(filePath);
        createDirectory(directory);
    }

    //---------------------------------------------------------------

    /**
     * 创建文件夹,支持<span style="color:green">级联创建</span>.
     * 
     * <h3>注意:</h3>
     * 
     * <ol>
     * <li>此处<span style="color:red">参数是文件夹</span>,如果需要传递文件路径自动创建父文件夹,那么请调用 {@link #createDirectoryByFilePath(String)}</li>
     * <li>对于不存在的文件夹/文件夹: "E:\\test\\1\\2011-07-07" 这么一个路径, 没有办法自动区别到底你是要创建文件还是文件夹</li>
     * <li>{@link File#isDirectory()} 这个方法,必须文件存在才能判断</li>
     * <li>如果文件夹已经存在,那么仅会记录trace level的日志</li>
     * </ol>
     * 
     * <h3>代码流程:</h3>
     * 
     * <blockquote>
     * <ol>
     * <li>如果 <code>directory</code> 是null,抛出 {@link NullPointerException}</li>
     * <li>如果 <code>directory</code> 是blank,抛出 {@link IllegalArgumentException}</li>
     * 
     * <li>{@code if directory exists---->log debug and return}</li>
     * <li>{@link java.io.File#mkdirs()}</li>
     * <li>{@code if mkdirs's result is false ---> return IllegalArgumentException}</li>
     * <li>{@code if mkdirs's result is true ---> log debug}</li>
     * </ol>
     * </blockquote>
     *
     * @param directory
     *            <span style="color:red">文件夹路径</span>
     * @see #createDirectoryByFilePath(String)
     */
    public static void createDirectory(String directory){
        Validate.notBlank(directory, "directory can't be null/empty!");

        //---------------------------------------------------------------
        File directoryFile = new File(directory);

        boolean isExists = directoryFile.exists();
        //---------------do with 存在------------------------------------------------
        if (isExists){//存在
            LOGGER.trace("directory:[{}] exists,don't need mkdirs,nothing to do~", directoryFile);
            return;
        }

        //----------------do with 不存在-----------------------------------------------
        String absolutePath = directoryFile.getAbsolutePath();

        // mkdir 如果 parent 目录不存在 会返回false 不会报错
        boolean flag = directoryFile.mkdirs();
        // 级联创建 父级文件夹
        Validate.isTrue(flag, "could't create directory:[%s]", absolutePath);

        //创建成功 记录下日志
        LOGGER.debug("success mkdirs:[{}]~~", absolutePath);
    }

    //---------------------------------------------------------------

    /**
     * 删除文件或者文件夹,如果是文件夹 ,递归深层次 删除.
     * 
     * <p>
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * </p>
     * 
     * <h3>difference between {@link File#delete()} and current method:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     * </blockquote>
     *
     * @param filePath
     *            文件或者文件夹路径
     * @return 如果 <code>fileName</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>fileName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         {@code true} if the file or directory was deleted, otherwise {@code false}
     * @see #deleteFileOrDirectory(File)
     */
    public static boolean deleteFileOrDirectory(String filePath){
        Validate.notBlank(filePath, "filePath can't be blank!");

        return deleteFileOrDirectory(new File(filePath));
    }

    /**
     * 删除文件或者文件夹,如果是文件夹 ,递归深层次删除.
     * 
     * <p>
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * </p>
     * 
     * <h3>difference between {@link File#delete()} and current method:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     * </blockquote>
     *
     * @param file
     *            file or directory to delete, can be {@code null}
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     *         {@code true} if the file or directory was deleted, otherwise {@code false}
     * @see com.feilong.lib.io.FileUtils#deleteQuietly(File)
     */
    public static boolean deleteFileOrDirectory(File file){
        Validate.notNull(file, "file can't be null!");

        return FileUtils.deleteQuietly(file);
    }

    // [end]

    //---------------------------------------------------------------

    /**
     * 返回此路径 <code>filePath</code> 父目录的路径名字符串.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li><code>filePath</code> 可以不存在</li>
     * <li><code>filePath</code> 可以是文件,也可以是文件夹</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * FileUtil.getParent("/");// null
     * FileUtil.getParent("/Users/feilong/feilong/logs/createDirectoryByFilePath");// /Users/feilong/feilong/logs
     * 
     * <span style="color:green">// 不存在</span>
     * FileUtil.getParent("/Users/feilong/feilong/logs/getParent/getParent");// /Users/feilong/feilong/logs/getParent
     * 
     * <span style="color:green">// 文件</span>
     * FileUtil.getParent("/Users/feilong/feilong/logs/1.txt");// /Users/feilong/feilong/logs
     * </pre>
     * 
     * </blockquote>
     *
     * @param filePath
     *            the path
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     *         如果此路径名没有指定父目录,则返回 null.
     * @see java.io.File#getParent()
     */
    public static String getParent(String filePath){
        Validate.notBlank(filePath, "filePath can't be null/empty!");

        //---------------------------------------------------------------

        File file = new File(filePath);
        return file.getParent();
    }

    /**
     * 判断文件 <code>filePath</code> 是否存在.
     * 
     * @param filePath
     *            the file path
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果文件存在,返回true
     * @see java.io.File#exists()
     */
    public static boolean isExistFile(String filePath){
        Validate.notBlank(filePath, "filePath can't be blank!");

        //---------------------------------------------------------------

        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 判断文件 <code>filePath</code> 不存在.
     *
     * @param filePath
     *            the file path
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果文件不存在,返回true
     * @see com.feilong.io.FileUtil#isExistFile(String)
     * @since 1.0.3
     */
    public static boolean isNotExistFile(String filePath){
        Validate.notBlank(filePath, "filePath can't be blank!");

        return !isExistFile(filePath);
    }

    //---------------------------------------------------------------

    /**
     * 返回指定的文件或目录 <code>file</code> 的大小, 取得文件大小(单位字节).
     * 
     * <p>
     * 返回指定的文件或目录的大小。<br>
     * 如果提供的文件是一个常规文件,那么文件的长度将被返回。<br>
     * 如果参数是一个目录,那么目录的大小是递归计算的。
     * </p>
     * 
     * @param file
     *            文件
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     *         此抽象路径名表示的文件的长度,以字节为单位;<br>
     *         如果文件不存在,则返回 0L.<br>
     *         对于表示特定于系统的实体(比如设备或管道)的路径名,某些操作系统可能返回 0L.
     * @see File#length()
     * @see com.feilong.lib.io.FileUtils#sizeOf(File)
     */
    public static long getFileSize(File file){
        Validate.notNull(file, "file can't be null!");

        return FileUtils.sizeOf(file);
    }

    //---------------------------------------------------------------

    /**
     * 获得文件 <code>file</code> 格式化大小.
     * 
     * <p>
     * 比如文件3834字节,格式化大小 3.74KB<br>
     * 比如文件36830335 字节, 格式化大小:35.12MB<br>
     * 比如文件2613122669 字节, 格式化大小 : 2.43GB<br>
     * </p>
     * 
     * <p>
     * 目前支持单位有TB GB MB KB以及最小单位 Bytes
     * </p>
     *
     * @param file
     *            the file
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     * @see #getFileSize(File)
     * @see com.feilong.io.FileUtil#formatSize(long)
     * @see "com.feilong.lib.io.FileUtils#byteCountToDisplaySize(long)"
     * @since 1.0.7
     */
    public static String getFileFormatSize(File file){
        Validate.notNull(file, "file can't be null!");

        long fileSize = getFileSize(file);
        return formatSize(fileSize);
    }

    /**
     * 文件大小 <code>fileSize</code> 格式化.
     * 
     * <p>
     * 目前支持单位有TB GB MB KB以及最小单位 Bytes
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * LOGGER.info(FileUtil.formatSize(8981528));
     * LOGGER.info(org.apache.commons.io.FileUtils.byteCountToDisplaySize(8981528));
     * 
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     * 00:06 INFO (FileUtilTest.java:204) [formatFileSize()] 8.56MB
     * 00:07 INFO (FileUtilTest.java:205) [formatFileSize()] 8 MB
     * </pre>
     * 
     * </blockquote>
     * 
     * <p>
     * Common-io 2.4{@link com.feilong.lib.io.FileUtils#byteCountToDisplaySize(long)}有缺点,显示的是整数GB 不带小数(比如1.99G 显示为1G),apache 论坛上争议比较大
     * </p>
     * 
     * @param fileSize
     *            文件大小 单位byte
     * @return 如果 {@code fileSize < 0} ,抛出 {@link IllegalArgumentException}<br>
     *         如果 {@code fileSize < } {@link FileUtils#ONE_KB},直接返回 <code>fileSize + "Bytes"</code><br>
     * @see com.feilong.lib.io.FileUtils#ONE_TB
     * @see com.feilong.lib.io.FileUtils#ONE_GB
     * @see com.feilong.lib.io.FileUtils#ONE_MB
     * @see com.feilong.lib.io.FileUtils#ONE_KB
     */
    public static String formatSize(long fileSize){
        Validate.isTrue(fileSize >= 0, "fileSize :[%s] must >=0");
        if (fileSize < ONE_KB){
            return fileSize + "Bytes";
        }
        //---------------------------------------------------------------
        for (Map.Entry<Long, String> entry : DIVISOR_AND_UNIT_MAP.entrySet()){
            Long divisor = entry.getKey();
            String unit = entry.getValue();
            if (fileSize >= divisor){
                //100的作用是 保持2位小数
                long remainder = 100 * (fileSize % divisor) / divisor; // 除完之后的余数
                return fileSize / divisor + (0 == remainder ? EMPTY : ("." + remainder)) + unit;
            }
        }
        //---------------------------------------------------------------
        throw new UnsupportedOperationException("fileSize:[" + fileSize + "] not support!");//理论上不会到这里
    }

    //---------------------------------------------------------------

    /**
     * 将 <code>filePathList</code> 转成 {@link URL} 数组.
     *
     * @param filePathList
     *            the paths
     * @return 如果 <code>filePathList</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePathList</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     * @see #toURLs(String...)
     * @since 1.4.0
     */
    public static URL[] toURLs(List<String> filePathList){
        Validate.notEmpty(filePathList, "filePathList can't be null/empty!");

        String[] filePaths = ConvertUtil.toArray(filePathList, String.class);
        return toURLs(filePaths);
    }

    //---------------------------------------------------------------

    /**
     * 将 <code>filePaths</code> 转成 {@link URL} 数组.
     *
     * @param filePaths
     *            the file paths
     * @return 如果 <code>filePaths</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePaths</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     * @see com.feilong.core.bean.ConvertUtil#toArray(String[], Class)
     * @see com.feilong.lib.io.FileUtils#toURLs(File[])
     * @since 1.4.0
     */
    public static URL[] toURLs(String...filePaths){
        Validate.notEmpty(filePaths, "filePaths can't be null/empty!");

        //---------------------------------------------------------------
        File[] files = ConvertUtil.toArray(filePaths, File.class);
        try{
            return FileUtils.toURLs(files);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
