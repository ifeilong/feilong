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
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.date.DateUtil.formatElapsedTime;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newArrayList;
import static com.feilong.core.util.CollectionsUtil.newLinkedHashSet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.feilong.core.CharsetType;
import com.feilong.core.Validate;
import com.feilong.json.JsonUtil;
import com.feilong.lib.io.IOUtils;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.validator.ValidatorUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * focus 在文件读取以及解析.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see com.feilong.lib.io.IOUtils
 * @since 1.0.6
 */
@SuppressWarnings("squid:S1192") //String literals should not be duplicated
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IOReaderUtil{

    /** 默认编码. */
    private static final String DEFAULT_CHARSET_NAME = UTF8;

    //---------------------------------------------------------------

    /**
     * 获得内容直接转成字节数组.
     * 
     * <p>
     * <span style="color:red">方法内部已经关闭了相关流</span>
     * </p>
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>file</code> 不存在,抛出 {@link UncheckedIOException}<br>
     * @since 4.0.1
     */
    public static byte[] readToByteArray(String location){
        InputStream inputStream = getInputStream(location);
        try{
            return IOUtils.toByteArray(inputStream);
        }catch (IOException e){
            throw new UncheckedIOException("location:" + location, e);
        }
    }
    //---------------------------------------------------------------

    /**
     * 获得文件内容直接转成字符串.
     * 
     * <p>
     * <span style="color:red">方法内部已经关闭了相关流</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 比如在 /Users/feilong/feilong/logs/readFileToString.txt 文件中有 内容如下:
     * 
     * <pre class="code">
     *     feilong 我爱你
     *     feilong
     * </pre>
     * 
     * 此时你可以直接使用
     * 
     * <pre class="code">
     * IOReaderUtil.readToString("/Users/feilong/feilong/logs/readFileToString.txt");
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
     *     feilong 我爱你
     *     feilong
     * </pre>
     * 
     * </blockquote>
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>file</code> 不存在,抛出 {@link UncheckedIOException}<br>
     * @see "org.apache.commons.io.FilenameUtils#readFileToString(File, Charset)"
     * @see #readToString(File, String)
     * @since 2.1.0
     */
    public static String readToString(String location){
        return readToString(location, DEFAULT_CHARSET_NAME);
    }

    /**
     * 获得文件内容直接转成字符串.
     * 
     * <p>
     * <span style="color:red">方法内部已经关闭了相关流</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 比如在 /Users/feilong/feilong/logs/readFileToString.txt 文件中有 内容如下:
     * 
     * <pre class="code">
    feilong 我爱你
    feilong
     * </pre>
     * 
     * 此时你可以直接使用
     * 
     * <pre class="code">
     * IOReaderUtil.readToString("/Users/feilong/feilong/logs/readFileToString.txt", UTF8);
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    feilong 我爱你
    feilong
     * </pre>
     * 
     * </blockquote>
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @param charsetName
     *            字符编码,如果是isNullOrEmpty,那么默认使用 {@link CharsetType#UTF8}
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>file</code> 不存在,抛出 {@link UncheckedIOException}<br>
     * @see "org.apache.commons.io.FilenameUtils#readFileToString(File, Charset)"
     * @see #readToString(File, String)
     * @since 1.0.8
     * @since 1.14.0 rename from readFileToString
     */
    public static String readToString(String location,String charsetName){
        InputStream inputStream = getInputStream(location);
        return readToString(inputStream, charsetName);
    }

    //---------------------------------------------------------------

    /**
     * 获得文件内容直接转成字符串.
     * <p>
     * <span style="color:red">方法内部已经关闭了相关流</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 比如在 /Users/feilong/feilong/logs/readFileToString.txt 文件中有 内容如下:
     * 
     * <pre class="code">
    feilong 我爱你
    feilong
     * </pre>
     * 
     * 此时你可以直接使用
     * 
     * <pre class="code">
     * IOReaderUtil.readToString(new File("/Users/feilong/feilong/logs/readFileToString.txt"), UTF8);
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    feilong 我爱你
    feilong
     * </pre>
     * 
     * </blockquote>
     * 
     * @param file
     *            文件
     * @param charsetName
     *            字符编码,如果是isNullOrEmpty,那么默认使用 {@link CharsetType#UTF8}
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>file</code> 不存在,抛出 {@link UncheckedIOException}<br>
     * @see "org.apache.commons.io.FilenameUtils#readFileToString(File, Charset)"
     * @since 1.5.3
     * @since 1.14.0 rename from readFileToString
     */
    public static String readToString(File file,String charsetName){
        Validate.notNull(file, "file can't be null!");
        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("will read file:[{}] to String,use charsetName:[{}]", file.getAbsolutePath(), charsetName);
        }
        //---------------------------------------------------------------
        try (FileInputStream fileInputStream = FileUtil.getFileInputStream(file)){
            return readToString(fileInputStream, charsetName);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 读取文件内容.
     * 
     * <p>
     * <span style="color:red">方法内部已经关闭了相关流</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 比如在 /Users/feilong/feilong/logs/readFileToString.txt 文件中有 内容如下:
     * 
     * <pre class="code">
    feilong 我爱你
    feilong
     * </pre>
     * 
     * 此时你可以直接使用
     * 
     * <pre class="code">
     * IOReaderUtil.readToString(FileUtil.getFileInputStream("/Users/feilong/feilong/logs/readFileToString.txt"), UTF8);
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    feilong 我爱你
    feilong
     * </pre>
     * 
     * </blockquote>
     * 
     * @param fileInputStream
     *            the file input stream
     * @param charsetName
     *            字符编码,如果是isNullOrEmpty,那么默认使用 {@link CharsetType#UTF8}
     * @return 如果 <code>fileInputStream</code> 是null,抛出 {@link NullPointerException}<br>
     * @see "org.apache.commons.io.FilenameUtils#readFileToString(File, Charset)"
     * @since 1.5.3
     * @since 1.14.0 rename from getContent
     */
    public static String readToString(FileInputStream fileInputStream,String charsetName){
        Validate.notNull(fileInputStream, "inputStream can't be null!");

        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("start read fileInputStream:[{}] to String,use charsetName:[{}]", fileInputStream, charsetName);
        }
        //---------------------------------------------------------------
        long beginTimeMillis = System.currentTimeMillis();

        Charset charset = Charset.forName(defaultIfNullOrEmpty(charsetName, DEFAULT_CHARSET_NAME));
        //---------------------------------------------------------------

        // 分配新的直接字节缓冲区
        final int capacity = 186140;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(capacity);

        //---------------------------------------------------------------
        // 用于读取、写入、映射和操作文件的通道.
        FileChannel fileChannel = fileInputStream.getChannel();
        try{
            StringBuilder sb = new StringBuilder(capacity);
            while (fileChannel.read(byteBuffer) != IOUtil.EOF){
                // 反转此缓冲区
                byteBuffer.flip();
                sb.append(charset.decode(byteBuffer));
                byteBuffer.clear();
            }

            //---------------------------------------------------------------
            String result = sb.toString();
            if (log.isInfoEnabled()){
                log.info("end read fileInputStream:[{}],use time: [{}]", fileInputStream, formatElapsedTime(beginTimeMillis));
            }
            return result;
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            // 用完关闭流 是个好习惯,^_^
            IOUtil.closeQuietly(fileInputStream);
        }
    }

    //---------------------------------------------------------------

    /**
     * 读取 {@link InputStream} 内容.
     * 
     * <p>
     * <span style="color:red">方法内部已经关闭了相关流</span>
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 比如你想访问 jd 的robots.txt,此时你可以使用 (注意此处仅做示例, 网络访问建议使用 feilong-net jar 的HttpClientUtil)
     * 
     * <pre class="code">
     * String spec = "https://www.jd.com/robots.txt";
     * InputStream openStream = URLUtil.openStream(URLUtil.toURL(spec));
     * log.debug(IOReaderUtil.readToString(openStream, UTF8));
     * </pre>
     * 
     * <b>返回:</b>
     * 
     * <pre class="code">
    User-agent: * 
    Disallow: /?* 
    Disallow: /pop/*.html 
    Disallow: /pinpai/*.html?* 
    User-agent: EtaoSpider 
    Disallow: / 
    User-agent: HuihuiSpider 
    Disallow: / 
    User-agent: GwdangSpider 
    Disallow: / 
    User-agent: WochachaSpider 
    Disallow: /
     * </pre>
     * 
     * </blockquote>
     * 
     * @param inputStream
     *            the input stream
     * @param charsetName
     *            字符编码,如果是isNullOrEmpty,那么默认使用 {@link CharsetType#UTF8}
     * @return 如果 <code>inputStream</code> 是null,抛出 {@link NullPointerException}<br>
     * @see "org.apache.commons.io.IOUtils#toString(InputStream, String)"
     * @see InputStreamUtil#toString(InputStream, String)
     * @since 1.5.3
     * @since 1.14.0 rename from getContent
     */
    public static String readToString(InputStream inputStream,String charsetName){
        Validate.notNull(inputStream, "inputStream can't be null!");

        long beginTimeMillis = System.currentTimeMillis();
        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("start read inputStream:[{}] to String,use charsetName:[{}]", inputStream, charsetName);
        }
        //---------------------------------------------------------------
        try{
            String result = InputStreamUtil.toString(inputStream, defaultIfNullOrEmpty(charsetName, DEFAULT_CHARSET_NAME));
            if (log.isInfoEnabled()){
                log.info("end read inputStream:[{}],use time: [{}]", inputStream, formatElapsedTime(beginTimeMillis));
            }
            return result;
        }finally{
            // 用完关闭流 是个好习惯,^_^
            IOUtil.closeQuietly(inputStream);
        }
    }

    //---------------------------------------------------------------

    /**
     * 直接解析 {@code location} 成 {@link Set}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.14.0
     */
    public static Set<String> readToSet(String location){
        return readToSet(location, null);
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式</li>
     * <li>如果readerConfig 是null,使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * Set{@code <String>} codes = new LinkedHashSet{@code <>}();
     * try{
     *     BufferedReader bufferedReader = new BufferedReader(STRING_READER);
     *     String lineTxt = null;
     *     while ((lineTxt = bufferedReader.readLine()) != null {@code &&} lineTxt.trim() != ""){
     *         codes.add(lineTxt.trim());
     *     }
     *     
     *     Iterator{@code <String>} iterator = codes.iterator();
     *     while (iterator.hasNext()){
     *         String code = iterator.next();
     *         if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
     *             iterator.remove();
     *         }
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close();
     * }
     * return codes;
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * return IOReaderUtil.readToSet(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
     * </pre>
     * 
     * </blockquote>
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.12.10
     * @since 1.14.0 remove readerConfig NPE validate, will use {@link ReaderConfig#DEFAULT}
     * @since 1.14.0 rename from read
     */
    public static Set<String> readToSet(String location,ReaderConfig readerConfig){
        InputStream inputStream = getInputStream(location);
        return readToSet(InputStreamUtil.toBufferedReader(inputStream, UTF8), readerConfig);
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式</li>
     * <li>如果readerConfig 是null,使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * Set{@code <String>} codes = new LinkedHashSet{@code <>}();
     * try{
     *     BufferedReader bufferedReader = new BufferedReader(STRING_READER);
     *     String lineTxt = null;
     *     while ((lineTxt = bufferedReader.readLine()) != null {@code &&} lineTxt.trim() != ""){
     *         codes.add(lineTxt.trim());
     *     }
     *     
     *     Iterator{@code <String>} iterator = codes.iterator();
     *     while (iterator.hasNext()){
     *         String code = iterator.next();
     *         if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
     *             iterator.remove();
     *         }
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close();
     * }
     * return codes;
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * return IOReaderUtil.readToSet(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
     * </pre>
     * 
     * </blockquote>
     * 
     * @param file
     *            the file
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>readerConfig</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.12.10
     * @since 1.14.0 remove readerConfig NPE validate, will use {@link ReaderConfig#DEFAULT}
     * @since 1.14.0 rename from read
     */
    public static Set<String> readToSet(File file,ReaderConfig readerConfig){
        Validate.notNull(file, "file can't be null!");

        try (Reader reader = new FileReader(file);){
            return readToSet(reader, readerConfig);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式</li>
     * <li>如果readerConfig 是null,使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li><span style="color:red">会自动关闭 <code>reader</code></span></li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * Set{@code <String>} codes = new LinkedHashSet{@code <>}();
     * try{
     *     BufferedReader bufferedReader = new BufferedReader(STRING_READER);
     *     String lineTxt = null;
     *     while ((lineTxt = bufferedReader.readLine()) != null {@code &&} lineTxt.trim() != ""){
     *         codes.add(lineTxt.trim());
     *     }
     *     
     *     Iterator{@code <String>} iterator = codes.iterator();
     *     while (iterator.hasNext()){
     *         String code = iterator.next();
     *         if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
     *             iterator.remove();
     *         }
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close();
     * }
     * return codes;
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * return IOReaderUtil.readToSet(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
     * </pre>
     * 
     * </blockquote>
     *
     * @param reader
     *            the reader
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @return 如果 <code>reader</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>readerConfig</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.12.10
     * @since 1.14.0 remove readerConfig NPE validate, will use {@link ReaderConfig#DEFAULT}
     * @since 1.14.0 rename from read
     */
    public static Set<String> readToSet(Reader reader,ReaderConfig readerConfig){
        Set<String> set = newLinkedHashSet();
        return readToCollection(reader, readerConfig, set);
    }

    //---------------------------------------------------------------

    /**
     * 直接解析 {@code location} 成 {@link List}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 4.1.2
     */
    public static List<String> readToList(String location){
        return readToList(location, null);
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式</li>
     * <li>如果readerConfig 是null,使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * Set{@code <String>} codes = new LinkedHashSet{@code <>}();
     * try{
     *     BufferedReader bufferedReader = new BufferedReader(STRING_READER);
     *     String lineTxt = null;
     *     while ((lineTxt = bufferedReader.readLine()) != null {@code &&} lineTxt.trim() != ""){
     *         codes.add(lineTxt.trim());
     *     }
     *     
     *     Iterator{@code <String>} iterator = codes.iterator();
     *     while (iterator.hasNext()){
     *         String code = iterator.next();
     *         if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
     *             iterator.remove();
     *         }
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close();
     * }
     * return set;
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * return IOReaderUtil.readToSet(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
     * </pre>
     * 
     * </blockquote>
     *
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 4.1.2
     */
    public static List<String> readToList(String location,ReaderConfig readerConfig){
        InputStream inputStream = getInputStream(location);
        return readToList(InputStreamUtil.toBufferedReader(inputStream, UTF8), readerConfig);
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式</li>
     * <li>如果readerConfig 是null,使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * Set{@code <String>} codes = new LinkedHashSet{@code <>}();
     * try{
     *     BufferedReader bufferedReader = new BufferedReader(STRING_READER);
     *     String lineTxt = null;
     *     while ((lineTxt = bufferedReader.readLine()) != null {@code &&} lineTxt.trim() != ""){
     *         codes.add(lineTxt.trim());
     *     }
     *     
     *     Iterator{@code <String>} iterator = codes.iterator();
     *     while (iterator.hasNext()){
     *         String code = iterator.next();
     *         if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
     *             iterator.remove();
     *         }
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close();
     * }
     * return set;
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * return IOReaderUtil.readToSet(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
     * </pre>
     * 
     * </blockquote>
     * 
     * @param file
     *            the file
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @return 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>readerConfig</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 4.1.2
     */
    public static List<String> readToList(File file,ReaderConfig readerConfig){
        Validate.notNull(file, "file can't be null!");

        try (Reader reader = new FileReader(file);){
            return readToList(reader, readerConfig);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式</li>
     * <li>如果readerConfig 是null,使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li><span style="color:red">会自动关闭 <code>reader</code></span></li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * List{@code <String>} codes = newArrayList();
     * try{
     *     BufferedReader bufferedReader = new BufferedReader(STRING_READER);
     *     String lineTxt = null;
     *     while ((lineTxt = bufferedReader.readLine()) != null {@code &&} lineTxt.trim() != ""){
     *         codes.add(lineTxt.trim());
     *     }
     *     
     *     Iterator{@code <String>} iterator = codes.iterator();
     *     while (iterator.hasNext()){
     *         String code = iterator.next();
     *         if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
     *             iterator.remove();
     *         }
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close();
     * }
     * return codes;
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * return IOReaderUtil.readToList(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"));
     * </pre>
     * 
     * </blockquote>
     *
     * @param reader
     *            the reader
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @return 如果 <code>reader</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>readerConfig</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 4.1.2
     */
    public static List<String> readToList(Reader reader,ReaderConfig readerConfig){
        List<String> list = newArrayList();
        return readToCollection(reader, readerConfig, list);
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式</li>
     * <li>如果readerConfig 是null,使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.</li>
     * <li><span style="color:red">会自动关闭 <code>reader</code></span></li>
     * <li>如果出现异常,返回 {@link UncheckedIOException}</li>
     * </ol>
     * </blockquote>
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * Set{@code <String>} codes = new LinkedHashSet{@code <>}();
     * try{
     *     BufferedReader bufferedReader = new BufferedReader(STRING_READER);
     *     String lineTxt = null;
     *     while ((lineTxt = bufferedReader.readLine()) != null {@code &&} lineTxt.trim() != ""){
     *         codes.add(lineTxt.trim());
     *     }
     *     
     *     Iterator{@code <String>} iterator = codes.iterator();
     *     while (iterator.hasNext()){
     *         String code = iterator.next();
     *         if (!code.matches("[0-9a-zA-Z\\-]{6,20}")){
     *             iterator.remove();
     *         }
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close();
     * }
     * return codes;
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * return IOReaderUtil.readToCollection(STRING_READER, new ReaderConfig("[0-9a-zA-Z\\-]{6,20}"), newLinkedHashSet());
     * </pre>
     * 
     * 此外建议,如果需要set可以使用{@link #readToSet(Reader, ReaderConfig)}, 如果需要list可以使用 {@link #readToList(Reader, ReaderConfig)}
     * 
     * </blockquote>
     *
     * @param <T>
     *            the generic type
     * @param reader
     *            the reader
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @param collection
     *            the collection
     * @return 如果 <code>reader</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>readerConfig</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>collection</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>collection</code> 是empty,抛出 {@link IllegalArgumentException}<br>
     * @since 4.1.2
     */
    public static <T extends Collection<String>> T readToCollection(Reader reader,ReaderConfig readerConfig,T collection){
        Validate.notNull(reader, "reader can't be null!");
        Validate.notNull(collection, "collection can't be null!");

        ReaderConfig useReaderConfig = defaultIfNull(readerConfig, ReaderConfig.DEFAULT);
        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("startReadReader:[{}], readerConfig:[{}]", reader, JsonUtil.toString(useReaderConfig));
        }

        //---------------------------------------------------------------
        long beginTimeMillis = System.currentTimeMillis();
        //---------------------------------------------------------------
        try (LineNumberReader lineNumberReader = new LineNumberReader(reader);){
            String line = null;
            while ((line = lineNumberReader.readLine()) != null){
                if (useReaderConfig.getIsTrim()){
                    line = StringUtils.trim(line);
                }
                if (isIgnoreLine(line, useReaderConfig)){
                    continue;
                }
                collection.add(line);
            }
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            IOUtil.closeQuietly(reader);
        }
        //---------------------------------------------------------------
        if (log.isInfoEnabled()){
            log.info(
                            "endReadReader:[{}],readerConfig:[{}],use time: [{}]",
                            reader,
                            JsonUtil.toString(useReaderConfig),
                            formatElapsedTime(beginTimeMillis));
        }
        return collection;
    }

    /**
     * Checks if is ignore line.
     *
     * @param line
     *            the line
     * @param readerConfig
     *            the reader config
     * @return true, if is ignore line
     */
    private static boolean isIgnoreLine(String line,ReaderConfig readerConfig){
        String regexPattern = readerConfig.getRegexPattern();
        //不符合正则
        if (isNotNullOrEmpty(regexPattern) && !ValidatorUtil.isMatches(regexPattern, line)){
            return true;
        }
        //空白
        return isNullOrEmpty(line) && readerConfig.getIgnoreBlankLine();
    }

    //---------------------------------------------------------------

    /**
     * 使用 {@link LineNumberReaderResolver}解析文件.
     * 
     * <p>
     * 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     * 如果 <code>lineNumberReaderResolver</code> 是null,抛出 {@link NullPointerException}<br>
     * </p>
     * 
     * @param location
     *            <ul>
     *            <li>支持全路径, 比如. "file:C:/test.dat".</li>
     *            <li>支持classpath 伪路径, e.g. "classpath:test.dat".</li>
     *            <li>支持相对路径, e.g. "WEB-INF/test.dat".</li>
     *            <li>如果上述都找不到,会再次转成FileInputStream,比如 "/Users/feilong/feilong-io/src/test/resources/readFileToString.txt"</li>
     *            </ul>
     * @param lineNumberReaderResolver
     *            the line number reader resolver
     * @see #resolverFile(File, LineNumberReaderResolver)
     * @since 1.4.1
     */
    public static void resolverFile(String location,LineNumberReaderResolver lineNumberReaderResolver){
        Validate.notNull(lineNumberReaderResolver, "lineNumberReaderResolver can't be null!");

        //---------------------------------------------------------------
        if (log.isTraceEnabled()){
            log.trace("will resolverFile location:[{}],use lineNumberReaderResolver:[{}]", location, lineNumberReaderResolver);
        }
        //---------------------------------------------------------------
        InputStream inputStream = getInputStream(location);
        resolverFile(InputStreamUtil.toBufferedReader(inputStream, UTF8), lineNumberReaderResolver);
    }

    /**
     * 使用 {@link LineNumberReaderResolver}解析文件.
     * 
     * <p>
     * 如果 <code>file</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>lineNumberReaderResolver</code> 是null,抛出 {@link NullPointerException}<br>
     * </p>
     *
     * @param file
     *            the file
     * @param lineNumberReaderResolver
     *            the line number reader resolver
     * @since 1.4.1
     */
    public static void resolverFile(File file,LineNumberReaderResolver lineNumberReaderResolver){
        Validate.notNull(file, "file can't be null!");
        Validate.notNull(lineNumberReaderResolver, "lineNumberReaderResolver can't be null!");

        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("will resolverFile file:[{}], lineNumberReaderResolver:[{}]", file.getAbsolutePath(), lineNumberReaderResolver);
        }
        //---------------------------------------------------------------
        try (Reader reader = new FileReader(file);){
            resolverFile(reader, lineNumberReaderResolver);
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 使用 {@link LineNumberReaderResolver}解析 {@link Reader}.
     * 
     * <h3>如果你以前这么写代码:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * try{
     *     Set{@code <String>} set = new HashSet{@code <>}();
     *     BufferedReader bufferedReader = new BufferedReader(read);
     *     String txt = null;
     *     while ((txt = bufferedReader.readLine()) != null){ <span style="color:green">// 读取文件,将文件内容放入到set中</span>
     *         txt = txt.trim();<span style="color:green">// 忽略前面前后空格</span>
     *         txt = txt.replace(" ", "");<span style="color:green">// 文中过滤空格</span>
     *         set.add(txt);
     *     }
     * }catch (Exception e){
     *     log.error(e.getMessage());
     * }finally{
     *     read.close(); <span style="color:green">// 关闭文件流</span>
     * }
     * return set;
     * 
     * </pre>
     * 
     * 现在可以重构为:
     * 
     * <pre class="code">
     * InputStreamReader read = new InputStreamReader(resourceAsStream, ENCODING);
     * 
     * final Set{@code <String>} set = new HashSet{@code <>}();
     * 
     * IOReaderUtil.resolverFile(read, new LineNumberReaderResolver(){
     * 
     *     {@code @Override}
     *     public boolean resolve(int lineNumber,String line){
     *         line = line.trim();<span style="color:green">// 忽略前面前后空格</span>
     *         line = line.replace(" ", "");<span style="color:green">// 文中过滤空格</span>
     *         set.add(line);<span style="color:green">// 读取文件,将文件内容放入到set中</span>
     *         return true;
     *     }
     * });
     * return set;
     * </pre>
     * 
     * </blockquote>
     * 
     * <p>
     * 如果 <code>reader</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>lineNumberReaderResolver</code> 是null,抛出 {@link NullPointerException}<br>
     * </p>
     *
     * @param reader
     *            the reader
     * @param lineNumberReaderResolver
     *            the line number reader resolver
     * @since 1.4.1
     */
    public static void resolverFile(Reader reader,LineNumberReaderResolver lineNumberReaderResolver){
        Validate.notNull(reader, "reader can't be null!");
        Validate.notNull(lineNumberReaderResolver, "lineNumberReaderResolver can't be null!");

        //---------------------------------------------------------------
        if (log.isDebugEnabled()){
            log.debug("start resolverFile reader:[{}], lineNumberReaderResolver:[{}]", reader, lineNumberReaderResolver);
        }

        long beginTimeMillis = System.currentTimeMillis();
        //---------------------------------------------------------------
        try (LineNumberReader lineNumberReader = new LineNumberReader(reader);){
            String line = null;
            while ((line = lineNumberReader.readLine()) != null){
                int lineNumber = lineNumberReader.getLineNumber();
                boolean result = lineNumberReaderResolver.resolve(lineNumber, line);
                if (!result){
                    break;
                }
            }
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            IOUtil.closeQuietly(reader);
        }
        //---------------------------------------------------------------
        if (log.isInfoEnabled()){
            String format = "end resolverFile reader:[{}],lineNumberReaderResolver:[{}],use time: [{}]";
            log.info(format, reader, lineNumberReaderResolver, formatElapsedTime(beginTimeMillis));
        }
    }

    /**
     * Gets the input stream.
     *
     * @param location
     *            the location
     * @return 如果 <code>location</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>location</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 4.0.1
     */
    private static InputStream getInputStream(String location){
        Validate.notBlank(location, "location can't be blank!");
        //---------------------------------------------------------------
        if (log.isTraceEnabled()){
            log.trace("will read location:[{}] to String", location);
        }
        //---------------------------------------------------------------
        return InputStreamUtil.getInputStream(location);
    }
}