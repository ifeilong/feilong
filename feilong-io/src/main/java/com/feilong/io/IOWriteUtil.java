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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.io.entity.FileWriteMode.COVER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.io.entity.FileWriteMode;
import com.feilong.lib.lang3.Validate;

/**
 * 专注于写文件的操作的工具类.
 * 
 * <ul>
 * <li>{@link #write(InputStream, OutputStream)} 写资源,速度最快的方法,速度比较请看 电脑资料 {@code <<压缩解压性能探究>>}</li>
 * <li>{@link #write(InputStream, String, String)} 将inputStream 写到 某个文件夹,名字为fileName</li>
 * <li>{@link #writeStringToFile(String, String, String)} 将字符串/文字写到文件中</li>
 * <li>{@link #writeStringToFile(String, String, String, FileWriteMode)} 将字符串写到文件中</li>
 * </ul>
 * 
 * 如果需要覆盖写文件,可以调用 {@link #writeStringToFile(String, String, String, FileWriteMode)}.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see "org.springframework.util.StreamUtils"
 * @see "org.springframework.util.FileCopyUtils"
 * @see com.feilong.lib.io.IOUtils
 * @since 1.0.6
 */
public final class IOWriteUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                = LoggerFactory.getLogger(IOWriteUtil.class);

    /** 默认缓冲大小 10k <code>{@value}</code>. */
    public static final int     DEFAULT_BUFFER_LENGTH = (int) (10 * FileUtil.ONE_KB);

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private IOWriteUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将字符串/文字写到文件中.
     * 
     * <h3>相关规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}</li>
     * <li>如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}</li>
     * <li>如果文件不存在,自动创建,包括其父文件夹 (支持级联创建 文件夹)</li>
     * <li>如果文件存在则覆盖旧文件,可以设置{@link FileWriteMode#APPEND}表示追加内容而非覆盖</li>
     * <li>如果不设置 <code>charsetType</code>,则默认使用{@link CharsetType#UTF8}编码</li>
     * </ul>
     * </blockquote>
     *
     * @param filePath
     *            文件路径
     * @param content
     *            字符串内容
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量,如果isNullOrEmpty,则默认使用 {@link CharsetType#UTF8}编码 {@link CharsetType}
     * @see FileWriteMode
     * @see CharsetType
     * @see #writeStringToFile(String, String, String, FileWriteMode)
     * @see "com.feilong.lib.io.FileUtils#writeStringToFile(File, String, Charset)"
     * @since 1.5.4
     */
    public static void writeStringToFile(String filePath,String content,String charsetType){
        writeStringToFile(filePath, content, charsetType, COVER);//default_fileWriteMode
    }

    //---------------------------------------------------------------

    /**
     * 将字符串写到文件中.
     * 
     * <p style="color:red">
     * <b>(注意,本方法最终会关闭 <code>inputStream</code>以及 <code>outputStream</code>).</b>
     * </p>
     * 
     * <h3>相关规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}</li>
     * <li>如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}</li>
     * 
     * <li>如果文件不存在,自动创建,包括其父文件夹 (支持级联创建 文件夹)</li>
     * <li>如果文件存在则覆盖旧文件,可以设置{@link FileWriteMode#APPEND}表示追加内容而非覆盖</li>
     * <li>如果不设置 <code>charsetType</code>,则默认使用{@link CharsetType#UTF8}编码</li>
     * </ul>
     * </blockquote>
     *
     * @param filePath
     *            文件路径
     * @param content
     *            字符串内容
     * @param charsetType
     *            字符编码,建议使用 {@link CharsetType} 定义好的常量,如果isNullOrEmpty,则默认使用 {@link CharsetType#UTF8}编码
     * @param fileWriteMode
     *            写模式 {@link FileWriteMode}
     * @see java.io.FileOutputStream#FileOutputStream(File, boolean)
     * @see #write(InputStream, OutputStream)
     * @see com.feilong.lib.io.FileUtils#writeStringToFile(File, String, Charset, boolean)
     * @since 1.5.4
     */
    public static void writeStringToFile(String filePath,String content,String charsetType,FileWriteMode fileWriteMode){
        Validate.notBlank(filePath, "filePath can't be null/empty!");

        //---------------------------------------------------------------
        Date beginDate = now();

        String useEncode = defaultIfNullOrEmpty(charsetType, UTF8);
        FileWriteMode useFileWriteMode = defaultIfNullOrEmpty(fileWriteMode, COVER);

        //---------------------------------------------------------------

        FileUtil.createDirectoryByFilePath(filePath);

        //---------------------------------------------------------------

        InputStream inputStream = InputStreamUtil.newByteArrayInputStream(content, useEncode);
        OutputStream outputStream = FileUtil.getFileOutputStream(filePath, useFileWriteMode);

        //---------------------------------------------------------------
        write(inputStream, outputStream);
        //---------------------------------------------------------------

        if (LOGGER.isInfoEnabled()){
            File file = new File(filePath);
            String size = FileUtil.getFileFormatSize(file);
            String pattern = "fileWriteMode:[{}],encode:[{}],contentLength:[{}],fileSize:[{}],absolutePath:[{}],time:[{}]";
            String useTime = formatDuration(beginDate);
            LOGGER.info(pattern, useFileWriteMode, useEncode, content.length(), size, file.getAbsolutePath(), useTime);
        }
    }

    //---------------------------------------------------------------

    /**
     * 将inputStream 写到某个文件夹 <code>directoryPath</code> ,名字为 <code>fileName</code>.
     * 
     * <p style="color:red">
     * <b>(注意,本方法最终会关闭 <code>inputStream</code>以及 <code>outputStream</code>).</b>
     * </p>
     * 
     * <p>
     * 如果拼接完的文件路径,父路径不存在,则自动创建<span style="color:green">(支持级联创建文件夹)</span>
     * </p>
     * 
     * <h3>说明:</h3>
     * 
     * <blockquote>
     * <p>
     * 支持 fileName 是路径的写法:
     * </p>
     * 
     * <pre>
     * IOWriteUtil.write(getInputStream(), "/Users/feilong/logs/",<span style="color:green">"a/a.txt"</span>);
     * </pre>
     * 
     * <p>
     * 此时会将内容写到 /Users/feilong/logs/a/a.txt 文件
     * </p>
     * 
     * <hr>
     * 
     * <p>
     * 也支持 fileName 带相对路径的写法:
     * </p>
     * 
     * <pre>
     * IOWriteUtil.write(getInputStream(), "/Users/feilong/logs/",<span style="color:green">"../a/a.txt"</span>);
     * </pre>
     * 
     * <p>
     * 此时会将内容写到 /Users/feilong/a/a.txt 文件
     * </p>
     * 
     * </blockquote>
     * 
     * <p>
     * 如果 <code>inputStream</code> 是null,抛出 {@link NullPointerException}<br>
     * 
     * 如果 <code>directoryPath</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>directoryPath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     * 如果 <code>fileName</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>fileName</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * </p>
     * 
     * @param inputStream
     *            上传的文件流
     * @param directoryPath
     *            文件夹路径, 支持格式类似于是 <code>/Users/feilong/logs/</code> 或者 <code>/Users/feilong/logs</code>
     * @param fileName
     *            文件名称
     * @see #write(InputStream, OutputStream)
     */
    public static void write(InputStream inputStream,String directoryPath,String fileName){
        Validate.notNull(inputStream, "inputStream can't be null!");
        Validate.notBlank(directoryPath, "directoryPath can't be blank!");
        Validate.notBlank(fileName, "fileName can't be blank!");

        //---------------------------------------------------------------
        Date beginDate = now();

        //---------------------------------------------------------------
        //since 1.12.9
        Path path = Paths.get(directoryPath, fileName).normalize();
        String filePath = path.toString();
        //---------------------------------------------------------------

        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("directoryPath:[{}],fileName:[{}] ==> file final Path:[{}]", directoryPath, fileName, filePath);
        }
        //---------------------------------------------------------------

        FileUtil.createDirectoryByFilePath(filePath);

        OutputStream outputStream = FileUtil.getFileOutputStream(filePath);
        write(inputStream, outputStream);

        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            File file = new File(filePath);
            String messagePattern = "fileSize:[{}],absolutePath:[{}],use time:[{}]";
            LOGGER.info(messagePattern, FileUtil.getFileFormatSize(file), file.getAbsolutePath(), formatDuration(beginDate));
        }
    }

    //---------------------------------------------------------------

    /**
     * 写资源,速度最快的方法,速度比较请看 电脑资料 {@code  <<压缩解压性能探究>>}.
     * 
     * <p style="color:red">
     * <b>(注意,本方法最终会关闭 <code>inputStream</code>以及 <code>outputStream</code>).</b>
     * </p>
     * 
     * <p>
     * Just write in blocks instead of copying it entirely into Java's memory first.<br>
     * The below basic example writes it in blocks of 10KB.<br>
     * This way you end up with a consistent memory usage of only 10KB instead of the complete content length.<br>
     * Also the enduser will start getting parts of the content much sooner.
     * </p>
     * 
     * @param inputStream
     *            inputStream
     * @param outputStream
     *            outputStream
     * @see java.io.OutputStream#write(byte[], int, int)
     * @see #write(InputStream, OutputStream,int)
     * @see com.feilong.lib.io.IOUtils#copyLarge(InputStream, OutputStream)
     */
    public static void write(InputStream inputStream,OutputStream outputStream){
        write(inputStream, outputStream, DEFAULT_BUFFER_LENGTH);
    }

    //---------------------------------------------------------------

    /**
     * 写资源,速度最快的方法,速度比较请看电脑资料 {@code <<压缩解压性能探究>>} .
     * 
     * <p style="color:red">
     * <b>(注意,本方法最终会关闭 <code>inputStream</code>以及 <code>outputStream</code>).</b>
     * </p>
     * 
     * <p>
     * 如果 <code>inputStream</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>outputStream</code> 是null,抛出 {@link NullPointerException}<br>
     * </p>
     *
     * @param inputStream
     *            inputStream
     * @param outputStream
     *            outputStream
     * @param bufferLength
     *            每次循环buffer大小 ,必须 {@code >}0
     * @see #writeUseNIO(InputStream, OutputStream,int)
     * @see java.io.OutputStream#write(byte[], int, int)
     * @see com.feilong.lib.io.IOUtils#copyLarge(InputStream, OutputStream)
     * @see com.feilong.lib.io.IOUtils#copy(InputStream, OutputStream, int)
     * @see <a href="http://stackoverflow.com/questions/10142409/write-an-inputstream-to-an-httpservletresponse">As creme de la creme with
     *      regard to performance, you could use NIO Channels and ByteBuffer. Create the following utility/helper method in some custom
     *      utility class,</a>
     */
    public static void write(InputStream inputStream,OutputStream outputStream,int bufferLength){
        writeUseNIO(inputStream, outputStream, bufferLength);
    }

    //---------------------------------------------------------------

    /**
     * 使用NIO API 来写数据 (效率高).
     * 
     * <p style="color:red">
     * <b>(注意,本方法最终会关闭 <code>inputStream</code>以及 <code>outputStream</code>).</b>
     * </p>
     * 
     * <h3>关于NIO</h3>
     * 
     * <blockquote>
     * <p>
     * nio是new io的简称,从jdk1.4就被引入了,可以说不是什么新东西了.<br>
     * nio的主要作用就是用来解决速度差异的.<br>
     * </p>
     * 
     * <p>
     * 举个例子:计算机处理的速度,和用户按键盘的速度.这两者的速度相差悬殊.<br>
     * 如果按照经典的方法:一个用户设定一个线程,专门等待用户的输入,无形中就造成了严重的资源浪费 :每一个线程都需要珍贵的cpu时间片,由于速度差异造成了在这个交互线程中的cpu都用来等待. <br>
     * 在以前的 Java IO 中,都是阻塞式 IO,NIO 引入了非阻塞式 IO.
     * </p>
     * 
     * <p>
     * The new I/O (NIO) APIs introduced in v 1.4 provide new features and improved performance in the areas of buffer management, scalable
     * network and file I/O, character-set support, and regular-expression matching. The NIO APIs supplement the I/O facilities in the
     * java.io package.
     * </p>
     * 
     * <p>
     * The NIO APIs include the following features:
     * </p>
     * 
     * <ol>
     * <li>Buffers for data of primitive types</li>
     * <li>Character-set encoders and decoders</li>
     * <li>A pattern-matching facility based on Perl-style regular expressions</li>
     * <li>Channels, a new primitive I/O abstraction</li>
     * <li>A file interface that supports locks and memory mapping</li>
     * <li>A multiplexed, non-blocking I/O facility for writing scalable servers</li>
     * </ol>
     * </blockquote>
     * 
     * <p>
     * As creme de la creme with regard to performance,you could use NIO {@link java.nio.channels.Channels} and {@link java.nio.ByteBuffer}.
     * </p>
     * 
     * <p>
     * 如果 <code>inputStream</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>outputStream</code> 是null,抛出 {@link NullPointerException}<br>
     * </p>
     *
     * @param inputStream
     *            the input stream
     * @param outputStream
     *            the output stream
     * @param bufferLength
     *            the buffer length
     * @since 1.0.8
     * @since jdk1.4
     */
    private static void writeUseNIO(InputStream inputStream,OutputStream outputStream,int bufferLength){
        Validate.notNull(inputStream, "inputStream can't be null!");
        Validate.notNull(outputStream, "outputStream can't be null!");
        //---------------------------------------------------------------
        Date beginDate = now();

        int loopCount = 0;
        int sumSize = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferLength);

        try (ReadableByteChannel readableByteChannel = Channels.newChannel(inputStream);
                        WritableByteChannel writableByteChannel = Channels.newChannel(outputStream);){

            while (readableByteChannel.read(byteBuffer) != IOUtil.EOF){
                byteBuffer.flip();
                sumSize += writableByteChannel.write(byteBuffer);
                byteBuffer.clear();
                loopCount++;
            }

            //---------------------------------------------------------------
            if (LOGGER.isDebugEnabled()){
                String pattern = "Write data over,sumSize:[{}],bufferLength:[{}],loopCount:[{}],use time:[{}]";
                LOGGER.debug(pattern, FileUtil.formatSize(sumSize), bufferLength, loopCount, formatDuration(beginDate));
            }
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            IOUtil.closeQuietly(outputStream, inputStream);
        }
    }
}