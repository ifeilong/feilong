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
import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.date.DateUtil.now;
import static com.feilong.core.lang.ObjectUtil.defaultIfNullOrEmpty;
import static com.feilong.core.util.CollectionsUtil.newLinkedHashSet;
import static org.apache.commons.io.IOUtils.EOF;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

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
import java.util.Date;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.CharsetType;
import com.feilong.json.JsonUtil;
import com.feilong.validator.ValidatorUtil;

/**
 * focus 在文件读取以及解析.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see org.apache.commons.io.IOUtils
 * @since 1.0.6
 */
public final class IOReaderUtil{

    /** The Constant log. */
    private static final Logger LOGGER               = LoggerFactory.getLogger(IOReaderUtil.class);

    //---------------------------------------------------------------

    /** 默认编码. */
    private static final String DEFAULT_CHARSET_NAME = UTF8;

    //---------------------------------------------------------------

    /** Don't let anyone instantiate this class. */
    private IOReaderUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
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
     * @param filePath
     *            the path
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>file</code> 不存在,抛出 {@link UncheckedIOException}<br>
     * @see org.apache.commons.io.FileUtils#readFileToString(File, Charset)
     * @see #readToString(File, String)
     * @since 2.1.0
     */
    public static String readToString(String filePath){
        return readToString(filePath, DEFAULT_CHARSET_NAME);
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
     * @param filePath
     *            the path
     * @param charsetName
     *            字符编码,如果是isNullOrEmpty,那么默认使用 {@link CharsetType#UTF8}
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>file</code> 不存在,抛出 {@link UncheckedIOException}<br>
     * @see org.apache.commons.io.FileUtils#readFileToString(File, Charset)
     * @see #readToString(File, String)
     * @since 1.0.8
     * @since 1.14.0 rename from readFileToString
     */
    public static String readToString(String filePath,String charsetName){
        Validate.notBlank(filePath, "filePath can't be blank!");

        //---------------------------------------------------------------
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("will read filePath:[{}] to String,use charsetName:[{}]", filePath, charsetName);
        }
        //---------------------------------------------------------------
        File file = new File(filePath);
        return readToString(file, charsetName);
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
     * @see org.apache.commons.io.FileUtils#readFileToString(File, Charset)
     * @since 1.5.3
     * @since 1.14.0 rename from readFileToString
     */
    public static String readToString(File file,String charsetName){
        Validate.notNull(file, "file can't be null!");
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will read file:[{}] to String,use charsetName:[{}]", file.getAbsolutePath(), charsetName);
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
     * @see org.apache.commons.io.FileUtils#readFileToString(File, Charset)
     * @since 1.5.3
     * @since 1.14.0 rename from getContent
     */
    public static String readToString(FileInputStream fileInputStream,String charsetName){
        Validate.notNull(fileInputStream, "inputStream can't be null!");

        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("start read fileInputStream:[{}] to String,use charsetName:[{}]", fileInputStream, charsetName);
        }
        //---------------------------------------------------------------
        Date beginDate = now();

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
            while (fileChannel.read(byteBuffer) != EOF){
                // 反转此缓冲区
                byteBuffer.flip();
                sb.append(charset.decode(byteBuffer));
                byteBuffer.clear();
            }

            //---------------------------------------------------------------
            String result = sb.toString();
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("end read fileInputStream:[{}],use time: [{}]", fileInputStream, formatDuration(beginDate));
            }
            return result;
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            // 用完关闭流 是个好习惯,^_^
            IOUtils.closeQuietly(fileInputStream);
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
     * LOGGER.debug(IOReaderUtil.readToString(openStream, UTF8));
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
     * @see org.apache.commons.io.IOUtils#toString(InputStream, String)
     * @see InputStreamUtil#toString(InputStream, String)
     * @since 1.5.3
     * @since 1.14.0 rename from getContent
     */
    public static String readToString(InputStream inputStream,String charsetName){
        Validate.notNull(inputStream, "inputStream can't be null!");

        Date beginDate = now();
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("start read inputStream:[{}] to String,use charsetName:[{}]", inputStream, charsetName);
        }
        //---------------------------------------------------------------
        try{
            String result = IOUtils.toString(inputStream, defaultIfNullOrEmpty(charsetName, DEFAULT_CHARSET_NAME));
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("end read inputStream:[{}],use time: [{}]", inputStream, formatDuration(beginDate));
            }
            return result;
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            // 用完关闭流 是个好习惯,^_^
            IOUtils.closeQuietly(inputStream);
        }
    }

    //---------------------------------------------------------------

    /**
     * 直接解析 {@code filePath} 成 {@link Set}.
     * 
     * <p>
     * 使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * </p>
     *
     * @param filePath
     *            the file path
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.14.0
     */
    public static Set<String> readToSet(String filePath){
        return readToSet(filePath, null);
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <p>
     * 适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式
     * </p>
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
     * @param filePath
     *            the file path
     * @param readerConfig
     *            读取配置, 如果传入的是 null,那么会使用默认的 {@link ReaderConfig#DEFAULT},忽略空白行,且去空格.
     * @return 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @since 1.12.10
     * @since 1.14.0 remove readerConfig NPE validate, will use {@link ReaderConfig#DEFAULT}
     * @since 1.14.0 rename from read
     */
    public static Set<String> readToSet(String filePath,ReaderConfig readerConfig){
        Validate.notBlank(filePath, "filePath can't be blank!");
        return readToSet(new File(filePath), readerConfig);
    }

    /**
     * 使用 {@link ReaderConfig}解析 {@link Reader}.
     * 
     * <p>
     * 适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式
     * </p>
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
     * <p>
     * 适合于对一个文件或者流,去除空白行, trim 并且读取指定的正则表达式内容形式
     * </p>
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
        Validate.notNull(reader, "reader can't be null!");

        ReaderConfig useReaderConfig = defaultIfNull(readerConfig, ReaderConfig.DEFAULT);
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("start read reader:[{}], readerConfig:[{}]", reader, JsonUtil.format(useReaderConfig));
        }

        //---------------------------------------------------------------
        String regexPattern = useReaderConfig.getRegexPattern();

        Date beginDate = now();

        Set<String> set = newLinkedHashSet();
        //---------------------------------------------------------------
        try (LineNumberReader lineNumberReader = new LineNumberReader(reader);){
            String line = null;
            while ((line = lineNumberReader.readLine()) != null){
                if (useReaderConfig.getIsTrim()){
                    line = StringUtils.trim(line);
                }
                if (isNotNullOrEmpty(regexPattern) && !ValidatorUtil.isMatches(regexPattern, line)){
                    continue;
                }
                if (isNullOrEmpty(line) && useReaderConfig.getIgnoreBlankLine()){
                    continue;
                }
                set.add(line);
            }
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }finally{
            IOUtils.closeQuietly(reader);
        }
        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            String format = "end read reader:[{}],readerConfig:[{}],use time: [{}]";
            LOGGER.info(format, reader, JsonUtil.format(useReaderConfig), formatDuration(beginDate));
        }
        return set;
    }

    //---------------------------------------------------------------

    /**
     * 使用 {@link LineNumberReaderResolver}解析文件.
     * 
     * <p>
     * 如果 <code>filePath</code> 是null,抛出 {@link NullPointerException}<br>
     * 如果 <code>filePath</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * 
     * 如果 <code>lineNumberReaderResolver</code> 是null,抛出 {@link NullPointerException}<br>
     * </p>
     * 
     * @param filePath
     *            the file path
     * @param lineNumberReaderResolver
     *            the line number reader resolver
     * @see #resolverFile(File, LineNumberReaderResolver)
     * @since 1.4.1
     */
    public static void resolverFile(String filePath,LineNumberReaderResolver lineNumberReaderResolver){
        Validate.notBlank(filePath, "filePath can't be blank!");
        Validate.notNull(lineNumberReaderResolver, "lineNumberReaderResolver can't be null!");

        //---------------------------------------------------------------
        if (LOGGER.isTraceEnabled()){
            LOGGER.trace("will resolverFile filePath:[{}],use lineNumberReaderResolver:[{}]", filePath, lineNumberReaderResolver);
        }
        //---------------------------------------------------------------
        resolverFile(new File(filePath), lineNumberReaderResolver);
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
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will resolverFile file:[{}], lineNumberReaderResolver:[{}]", file.getAbsolutePath(), lineNumberReaderResolver);
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
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("start resolverFile reader:[{}], lineNumberReaderResolver:[{}]", reader, lineNumberReaderResolver);
        }

        Date beginDate = now();
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
            IOUtils.closeQuietly(reader);
        }
        //---------------------------------------------------------------
        if (LOGGER.isInfoEnabled()){
            String format = "end resolverFile reader:[{}],lineNumberReaderResolver:[{}],use time: [{}]";
            LOGGER.info(format, reader, lineNumberReaderResolver, formatDuration(beginDate));
        }
    }
}