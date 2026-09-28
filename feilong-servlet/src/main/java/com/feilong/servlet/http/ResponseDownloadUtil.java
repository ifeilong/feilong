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
package com.feilong.servlet.http;

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.date.DateUtil.formatElapsedTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.feilong.core.net.URIUtil;
import com.feilong.io.FileUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.io.MimeTypeUtil;
import com.feilong.io.entity.MimeType;
import com.feilong.lib.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 关于 {@link javax.servlet.http.HttpServletResponse HttpServletResponse} 下载的工具类.
 * 
 * <h3>关于下载过程:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>调用 {@link HttpServletResponse#reset()} , 清空当前响应(已设置的响应头以及缓冲区中的内容)</li>
 * <li>设置 {@code Content-Disposition} 响应头</li>
 * <li>设置 {@code Content-Type} 响应头</li>
 * <li>如果 <code>contentLength</code> 不是 <code>null</code>, 设置 {@code Content-Length} 响应头</li>
 * <li>把 <code>inputStream</code> 的数据写到 {@link HttpServletResponse#getOutputStream()}, 参见
 * {@link IOWriteUtil#write(InputStream, OutputStream)}</li>
 * </ol>
 * </blockquote>
 * 
 * <h3>关于默认响应头:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>{@code Content-Disposition} 没有指定时, 默认使用附件形式: {@code attachment; filename=} 加上 URL 编码后的文件名, <br>
 * 编码参见 {@link URIUtil#encode(String, String)}, 编码格式是 {@link CharsetType#UTF8}, 以便支持中文等非 ASCII 字符的文件名; <br>
 * 如果需要使用 RFC 5987 规范的 {@code filename*=UTF-8''xxx} 形式, 可以自己传入完整的 {@code contentDisposition}</li>
 * <li>{@code Content-Type} 没有指定时, 先根据文件名推断, 参见 {@link MimeTypeUtil#getContentTypeByFileName(String)}; <br>
 * 推断不出来时, 使用 {@link MimeType#BIN}, 即 {@code application/octet-stream}</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>关于响应已提交:</h3>
 * 
 * <blockquote>
 * <p>
 * 下载是"独占"响应的操作, 内部会调用 {@link HttpServletResponse#reset()}, <br>
 * 因此必须在向响应输出任何内容之前调用下载方法(包括不能已经调用过 {@link HttpServletResponse#getWriter()}), <br>
 * 否则响应已提交, {@link HttpServletResponse#reset()} 会抛出 {@link IllegalStateException}.
 * </p>
 * </blockquote>
 * 
 * <h3>关于异常:</h3>
 * 
 * <blockquote>
 * <ul>
 * <li>如果客户端主动中断下载, 比如用户取消了下载, 或者迅雷这类多线程下载工具 kill 掉多余的下载线程, 导致服务器端连接被重置, <br>
 * 那么服务端继续写入数据时会抛出 {@code ClientAbortException}, 这是正常现象, 本类只记录 warn 日志,
 * <span style="color:red">不会向外抛出异常</span></li>
 * <li>其他 {@link IOException} 会包装成 {@link UncheckedIOException} 抛出</li>
 * </ul>
 * </blockquote>
 * 
 * <h3>示例:</h3>
 * 
 * <pre class="code">
 * {@code
 * //下载文件系统中的文件, 保存文件名取 file.getName()
 * ResponseDownloadUtil.download("/home/feilong/feilong.zip", request, response);
 * 
 * //下载数据流, 自己指定保存文件名以及文件大小
 * ResponseDownloadUtil.download("feilong.zip", inputStream, 1024L * 1024, request, response);
 * }
 * </pre>
 * 
 * <p>
 * 此类是线程安全的, 只包含静态方法, 不能被实例化.
 * </p>
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see javax.servlet.http.HttpServletResponse
 * @see ResponseUtil
 * @see IOWriteUtil#write(InputStream, OutputStream)
 * @see MimeTypeUtil#getContentTypeByFileName(String)
 * @since 1.5.1
 */
@lombok.extern.slf4j.Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseDownloadUtil{

    /**
     * 下载文件系统中的文件.
     * 
     * <p>
     * 内部使用 {@code new File(pathname)} 构建文件对象, 参见 {@link #download(File, HttpServletRequest, HttpServletResponse)}.
     * </p>
     * 
     * <h3>相关规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>如果 <code>pathname</code> 是null, 抛出 {@link NullPointerException}</li>
     * <li>如果文件不存在/是一个目录/无法打开读取, 抛出 {@link UncheckedIOException}</li>
     * </ul>
     * </blockquote>
     *
     * @param pathname
     *            文件的路径名, 参见 {@link File#File(String)}
     * @param request
     *            请求对象, 仅用于获取 User-Agent 记录日志
     * @param response
     *            响应对象
     * @exception NullPointerException
     *                如果 <code>pathname</code> 是null
     * @exception UncheckedIOException
     *                如果文件不存在/是一个目录/无法打开读取
     * @see #download(File, HttpServletRequest, HttpServletResponse)
     * @since 1.4.1
     */
    public static void download(String pathname,HttpServletRequest request,HttpServletResponse response){
        download(new File(pathname), request, response);
    }

    /**
     * 下载文件.
     * 
     * <h3>相关规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>下载的保存文件名取 {@link File#getName()}, 将会被设置到 {@code Content-Disposition} 响应头中</li>
     * <li>文件大小取 {@link FileUtil#getFileSize(File)}, 将会被设置到 {@code Content-Length} 响应头中</li>
     * <li>文件以流的形式读取, 参见 {@link FileUtil#getFileInputStream(File)}</li>
     * <li>如果 <code>file</code> 是null, 抛出 {@link NullPointerException}</li>
     * <li>如果文件不存在/是一个目录/无法打开读取, 抛出 {@link UncheckedIOException}</li>
     * </ul>
     * </blockquote>
     *
     * @param file
     *            待下载的文件
     * @param request
     *            请求对象, 仅用于获取 User-Agent 记录日志
     * @param response
     *            响应对象
     * @exception NullPointerException
     *                如果 <code>file</code> 是null
     * @exception UncheckedIOException
     *                如果文件不存在/是一个目录/无法打开读取
     * @see File#getName()
     * @see FileUtil#getFileInputStream(File)
     * @see FileUtil#getFileSize(File)
     * @see #download(String, InputStream, Number, HttpServletRequest, HttpServletResponse)
     * @since 1.4.1
     */
    public static void download(File file,HttpServletRequest request,HttpServletResponse response){
        String saveFileName = file.getName();
        // 以流的形式下载文件.
        InputStream inputStream = FileUtil.getFileInputStream(file);
        long contentLength = FileUtil.getFileSize(file);

        download(saveFileName, inputStream, contentLength, request, response);
    }

    /**
     * 下载数据, <code>contentType</code> 以及 <code>contentDisposition</code> 均使用默认值.
     * 
     * <p>
     * 内部调用 {@link #download(String, InputStream, Number, String, String, HttpServletRequest, HttpServletResponse)} , 传入的
     * <code>contentType</code> 以及 <code>contentDisposition</code> 均是 <code>null</code>.
     * </p>
     * 
     * <h3>相关规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>{@code Content-Disposition} 使用默认值: {@code attachment; filename=} 加上 URL 编码后的文件名</li>
     * <li>{@code Content-Type} 使用默认值: 先按文件名推断, 推断不出来使用 {@code application/octet-stream}</li>
     * </ul>
     * </blockquote>
     *
     * @param saveFileName
     *            保存文件的文件名, 将会被设置到 {@code Content-Disposition} 响应头中
     * @param inputStream
     *            保存数据输入流, 如果 <code>inputStream</code> 是null, 抛出 {@link NullPointerException}
     * @param contentLength
     *            内容长度(字节数), 如果 <code>contentLength</code> 是null, 不会设置 {@code Content-Length} 响应头, 参见
     *            {@link #download(String, InputStream, Number, String, String, HttpServletRequest, HttpServletResponse)}
     * @param request
     *            请求对象, 仅用于获取 User-Agent 记录日志
     * @param response
     *            响应对象
     * @exception NullPointerException
     *                如果 <code>inputStream</code> 是null
     * @exception UncheckedIOException
     *                如果在写数据时发生 {@link IOException}
     * @see IOWriteUtil#write(InputStream, OutputStream)
     * @see "org.springframework.http.MediaType"
     */
    public static void download(
                    String saveFileName,
                    InputStream inputStream,
                    Number contentLength,
                    HttpServletRequest request,
                    HttpServletResponse response){
        //均采用默认的
        String contentType = null;
        String contentDisposition = null;
        download(saveFileName, inputStream, contentLength, contentType, contentDisposition, request, response);
    }

    /**
     * 下载数据.
     * 
     * <h3>相关规则:</h3>
     * 
     * <blockquote>
     * <ul>
     * <li>先设置响应头, 参见 {@link #setDownloadResponseHeader(String, Number, String, String, HttpServletResponse)} , <br>
     * 其中会调用 {@link HttpServletResponse#reset()} 清空当前响应</li>
     * <li>然后把 <code>inputStream</code> 的数据写到 {@link HttpServletResponse#getOutputStream()}</li>
     * <li>如果 <code>contentType</code> 是null或者empty, 使用默认值, 参见 {@link #resolverContentType(String, String)}</li>
     * <li>如果 <code>contentDisposition</code> 是null或者empty, 使用默认值, 参见
     * {@link #resolverContentDisposition(String, String)}</li>
     * <li>如果 <code>contentLength</code> 是null, 不会设置 {@code Content-Length} 响应头</li>
     * <li>数据写完之后, <code>inputStream</code> 以及 response 的输出流会被关闭, 参见
     * {@link IOWriteUtil#write(InputStream, OutputStream)}</li>
     * <li>如果 <code>inputStream</code> 是null, 抛出 {@link NullPointerException}</li>
     * <li>如果响应的 {@link HttpServletResponse#getWriter()} 已经被调用过, 抛出 {@link IllegalStateException}</li>
     * </ul>
     * </blockquote>
     *
     * @param saveFileName
     *            保存文件的文件名, 将会被设置到 {@code Content-Disposition} 响应头中
     * @param inputStream
     *            保存数据输入流
     * @param contentLength
     *            内容长度(字节数), 如果是网络流就需要自己来取到大小了, 比如
     * 
     *            <pre class="code">
     *            {@code
     * HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
     * long contentLength = httpConn.getContentLengthLong();
     *            }
     *            </pre>
     * 
     *            <span style="color:red">{@link InputStream#available()} 不适用于网络流</span>;<br>
     *            如果是 <code>null</code>, 不会设置 {@code Content-Length} 响应头
     * @param contentType
     *            内容类型; 如果传递了该参数, 使用传递的值; 如果没有传递, 即为 <code>null</code>, 那么使用默认的值, 参见
     *            {@link #resolverContentType(String, String)}
     * @param contentDisposition
     *            内容处置; 如果传递了该参数, 使用传递的值; 如果没有传递, 即为 <code>null</code>, 那么使用默认的值, 参见
     *            {@link #resolverContentDisposition(String, String)}
     * @param request
     *            请求对象, 仅用于获取 User-Agent 记录日志
     * @param response
     *            响应对象
     * @exception NullPointerException
     *                如果 <code>inputStream</code> 是null
     * @exception IllegalStateException
     *                如果响应已经提交, 或者已经调用过 {@link HttpServletResponse#getWriter()}
     * @exception UncheckedIOException
     *                如果在写数据时发生 {@link IOException}
     * @see IOWriteUtil#write(InputStream, OutputStream)
     * @see "org.springframework.http.MediaType"
     * @see "org.apache.http.HttpHeaders"
     * @see "org.springframework.http.HttpHeaders"
     * @see com.feilong.io.MimeTypeUtil#getContentTypeByFileName(String)
     * @see javax.servlet.ServletContext#getMimeType(String)
     */
    public static void download(
                    String saveFileName,
                    InputStream inputStream,
                    Number contentLength,
                    String contentType,
                    String contentDisposition,
                    HttpServletRequest request,
                    HttpServletResponse response){

        setDownloadResponseHeader(saveFileName, contentLength, contentType, contentDisposition, response);

        //下载数据
        downLoadData(saveFileName, inputStream, contentLength, request, response);
    }

    //---------------------------------------------------------------

    /**
     * 把 <code>inputStream</code> 的数据写到 response 的输出流, 并记录下载耗时日志.
     * 
     * <p>
     * 如果是客户端主动中断下载(比如 {@code ClientAbortException}), 只记录 warn 日志, 不抛出异常; <br>
     * 其他 {@link IOException} 包装成 {@link UncheckedIOException} 抛出.
     * </p>
     *
     * @param saveFileName
     *            保存文件的文件名, 仅用于记录日志
     * @param inputStream
     *            保存数据输入流
     * @param contentLength
     *            内容长度(字节数), 仅用于记录日志
     * @param request
     *            请求对象, 仅用于获取 User-Agent 记录日志
     * @param response
     *            响应对象
     */
    private static void downLoadData(
                    String saveFileName,
                    InputStream inputStream,
                    Number contentLength,
                    HttpServletRequest request,
                    HttpServletResponse response){
        long beginTimeMillis = System.currentTimeMillis();

        String length = null == contentLength ? null : FileUtil.formatSize(contentLength.longValue());
        log.info("begin download~~,saveFileName:[{}],contentLength:[{}]", saveFileName, length);
        try{
            OutputStream outputStream = response.getOutputStream();
            IOWriteUtil.write(inputStream, outputStream);

            if (log.isInfoEnabled()){
                String pattern = "end download,saveFileName:[{}],contentLength:[{}],time use:[{}]";
                log.info(pattern, saveFileName, length, formatElapsedTime(beginTimeMillis));
            }
        }catch (IOException e){
            /*
             * 在写数据的时候, 对于 ClientAbortException 之类的异常, 是因为客户端取消了下载,而服务器端继续向浏览器写入数据时,
             * 抛出这个异常,这个是正常的.
             * 尤其是对于迅雷这种吸血的客户端软件, 明明已经有一个线程在读取
             * 如果短时间内没有读取完毕,迅雷会再启第二个、第三个...线程来读取相同的字节段,
             * 直到有一个线程读取完毕,迅雷会 KILL掉其他正在下载同一字节段的线程, 强行中止字节读出,造成服务器抛 ClientAbortException.
             */
            //ClientAbortException:  java.net.SocketException: Connection reset by peer: socket write error
            final String exceptionName = e.getClass().getName();

            if (StringUtils.contains(exceptionName, "ClientAbortException")
                            || StringUtils.contains(e.getMessage(), "ClientAbortException")){
                String pattern = "[ClientAbortException],maybe user use Thunder soft or abort client soft download,exceptionName:[{}],exception message:[{}] ,request User-Agent:[{}]";
                log.warn(pattern, exceptionName, e.getMessage(), RequestUtil.getHeaderUserAgent(request));
            }else{
                log.error("[download exception],exception name: " + exceptionName, e);
                throw new UncheckedIOException(e);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置下载相关的响应头.
     * 
     * <p>
     * 会先调用 {@link HttpServletResponse#reset()} 清空当前响应, 再依次设置 {@code Content-Disposition}、{@code Content-Type}
     * 以及 {@code Content-Length} 响应头.
     * </p>
     *
     * @param saveFileName
     *            保存文件的文件名, 将会被设置到 {@code Content-Disposition} 响应头中
     * @param contentLength
     *            内容长度(字节数); 如果是 <code>null</code>, 不会设置 {@code Content-Length} 响应头
     * @param contentType
     *            如果传递了该参数,使用传递的值;如果没有传递,即为 <code>null</code>,那么使用默认的值,参见 {@link #resolverContentType(String, String)}
     * @param contentDisposition
     *            如果传递了该参数,使用传递的值;如果没有传递,即为 <code>null</code>,那么使用默认的值,参见 {@link #resolverContentDisposition(String, String)}
     * @param response
     *            响应对象
     */
    private static void setDownloadResponseHeader(
                    String saveFileName,
                    Number contentLength,
                    String contentType,
                    String contentDisposition,
                    HttpServletResponse response){

        //清空response
        //getResponse的getWriter()方法连续两次输出流到页面的时候,第二次的流会包括第一次的流,所以可以使用将response.reset或者resetBuffer的方法.
        //getOutputStream() has already been called for this response问题的解决
        //在jsp向页面输出图片的时候,使用response.getOutputStream()会有这样的提示:java.lang.IllegalStateException:getOutputStream() has already been called for this response,会抛出Exception
        response.reset();

        //---------------------------------------------------------------
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, resolverContentDisposition(saveFileName, contentDisposition));

        // ===================== Default MIME Type Mappings =================== -->
        //浏览器接收到文件后,会进入插件系统进行查找,查找出哪种插件可以识别读取接收到的文件.如果浏览器不清楚调用哪种插件系统,它可能会告诉用户缺少某插件,
        response.setContentType(resolverContentType(saveFileName, contentType));

        if (isNotNullOrEmpty(contentLength)){
            response.setContentLength(contentLength.intValue());
        }

        //************************about buffer***********************************************************

        //缺省情况下:服务端要输出到客户端的内容,不直接写到客户端,而是先写到一个输出缓冲区中.
        //只有在下面三中情况下,才会把该缓冲区的内容输出到客户端上: 
        //该JSP网页已完成信息的输出 
        //输出缓冲区已满 
        //JSP中调用了out.flush()或response.flushbuffer() 

        //缓冲区的优点是:我们暂时不输出,直到确定某一情况时,才将写入缓冲区的数据输出到浏览器,否则就将缓冲区的数据取消.
        //XXX 确认是否需要 response.setBufferSize(10240); ^_^

        //see org.apache.commons.io.IOUtils.copyLarge(InputStream, OutputStream) javadoc
        //This method buffers the input internally, so there is no need to use a BufferedInputStream
    }

    /**
     * 解析 {@code Content-Disposition} 响应头的值.
     * 
     * <p>
     * 默认 附件形式
     * </p>
     * 
     * <pre class="code">
     * Content-Disposition takes one of two values, `inline' and  `attachment'.  
     * 'Inline' indicates that the entity should be immediately displayed to the user, 
     * whereas `attachment' means that the user should take additional action to view the entity.
     * The `filename' parameter can be used to suggest a filename for storing the bodypart, if the user wishes to store it in an external file.
     * </pre>
     * 
     * <p>
     * 如果 <code>contentDisposition</code> 是null或者empty, 返回 {@code attachment; filename=} 加上使用
     * {@link CharsetType#UTF8} 编码之后的 {@code saveFileName}, 参见 {@link URIUtil#encode(String, String)}.
     * </p>
     *
     * @param saveFileName
     *            保存文件的文件名
     * @param contentDisposition
     *            内容处置, 如果传递了该参数, 使用传递的值
     * @return 如果 <code>contentDisposition</code> 是null或者empty, 返回默认的附件形式, 否则返回 <code>contentDisposition</code>
     * @see URIUtil#encode(String, String)
     * @since 1.4.0
     */
    private static String resolverContentDisposition(String saveFileName,String contentDisposition){
        return isNotNullOrEmpty(contentDisposition) ? contentDisposition : "attachment; filename=" + URIUtil.encode(saveFileName, UTF8);
    }

    /**
     * 解析 {@code Content-Type} 响应头的值.
     * 
     * <p>
     * 如果 <code>inputContentType</code> 是null或者empty, 先根据 {@code saveFileName} 推断, 参见
     * {@link MimeTypeUtil#getContentTypeByFileName(String)}; 推断不出来时, 使用 {@link MimeType#BIN}, 即
     * {@code application/octet-stream}.
     * </p>
     *
     * @param saveFileName
     *            保存文件的文件名, 用于推断 content type
     * @param inputContentType
     *            传入的 content type, 如果传递了该参数, 使用传递的值
     * @return 如果 <code>inputContentType</code> 是null或者empty, 返回根据文件名推断出来的 content type, 推断不出来时返回
     *         {@code application/octet-stream}, 否则返回 <code>inputContentType</code>
     * @see MimeTypeUtil#getContentTypeByFileName(String)
     * @see MimeType#BIN
     * @since 1.4.0
     */
    private static String resolverContentType(String saveFileName,String inputContentType){
        //See tomcat web.xml
        //When serving static resources, Tomcat will automatically generate a "Content-Type" header based on the resource's filename extension, based on these mappings.  
        //Additional mappings can be added here (to apply to all web applications), or in your own application's web.xml deployment descriptor.                                               -->

        if (isNotNullOrEmpty(inputContentType)){
            return inputContentType;
        }
        String contentTypeByFileName = MimeTypeUtil.getContentTypeByFileName(saveFileName);

        //contentType = "application/force-download";//,php强制下载application/force-download,将发送HTTP 标头您的浏览器并告诉它下载,而不是在浏览器中运行的文件
        //application/x-download

        //.*( 二进制流,不知道下载文件类型)   application/octet-stream
        return isNotNullOrEmpty(contentTypeByFileName) ? contentTypeByFileName : MimeType.BIN.getMime();
        //The HTTP specification recommends setting the Content-Type to application/octet-stream. 
        //Unfortunately, this causes problems with Opera 6 on Windows (which will display the raw bytes for any file whose extension it doesn't recognize) and on Internet Explorer 5.1 on the Mac (which will display inline content that would be downloaded if sent with an unrecognized type).
    }
}
