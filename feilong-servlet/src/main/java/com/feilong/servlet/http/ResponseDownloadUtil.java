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
import static com.feilong.core.date.DateExtensionUtil.formatDuration;
import static com.feilong.core.date.DateUtil.now;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.UncheckedIOException;
import com.feilong.core.net.URIUtil;
import com.feilong.io.FileUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.io.MimeTypeUtil;
import com.feilong.io.entity.MimeType;

/**
 * 关于 {@link javax.servlet.http.HttpServletResponse}下载的工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @see javax.servlet.http.HttpServletResponse
 * @since 1.5.1
 */
public final class ResponseDownloadUtil{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseDownloadUtil.class);

    /** Don't let anyone instantiate this class. */
    private ResponseDownloadUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 下载文件.
     *
     * @param pathname
     *            the pathname
     * @param request
     *            用来获取request相关信息 ,仅用来记录log
     * @param response
     *            the response
     * @see #download(File, HttpServletRequest, HttpServletResponse)
     * @since 1.4.1
     */
    public static void download(String pathname,HttpServletRequest request,HttpServletResponse response){
        download(new File(pathname), request, response);
    }

    /**
     * 下载文件.
     *
     * @param file
     *            the file
     * @param request
     *            用来获取request相关信息 ,仅用来记录log
     * @param response
     *            the response
     * @see com.feilong.io.FilenameUtil#getFileName(String)
     * @see com.feilong.io.FileUtil#getFileInputStream(File)
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
     * 下载(以 contentType=application/force-download) 强制下载.
     *
     * @param saveFileName
     *            保存文件的文件名,将会被设置到 Content-Disposition header 中
     * @param inputStream
     *            保存数据输入流
     * @param contentLength
     *            如果是网络流就需要自己来取到大小了,比如
     *            <p>
     *            HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();<br>
     *            httpconn.getContentLength(); 
     *            </p>
     *            {@link InputStream#available()} 不使用于网络情况
     * @param request
     *            用来获取request相关信息 ,仅用来记录log
     * @param response
     *            response
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
     * 下载.
     *
     * @param saveFileName
     *            保存文件的文件名,将会被设置到 Content-Disposition header 中
     * @param inputStream
     *            保存数据输入流
     * @param contentLength
     *            如果是网络流就需要自己来取到大小了
     * @param contentType
     *            the content type
     * @param contentDisposition
     *            the content disposition
     * @param request
     *            用来获取request相关信息 ,仅用来记录log
     * @param response
     *            response
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
     * Down load data.
     *
     * @param saveFileName
     *            the save file name
     * @param inputStream
     *            the input stream
     * @param contentLength
     *            the content length
     * @param request
     *            the request
     * @param response
     *            the response
     */
    private static void downLoadData(
                    String saveFileName,
                    InputStream inputStream,
                    Number contentLength,
                    HttpServletRequest request,
                    HttpServletResponse response){
        Date beginDate = now();
        String length = FileUtil.formatSize(contentLength.longValue());
        LOGGER.info("begin download~~,saveFileName:[{}],contentLength:[{}]", saveFileName, length);
        try{
            OutputStream outputStream = response.getOutputStream();
            IOWriteUtil.write(inputStream, outputStream);
            if (LOGGER.isInfoEnabled()){
                String pattern = "end download,saveFileName:[{}],contentLength:[{}],time use:[{}]";
                LOGGER.info(pattern, saveFileName, length, formatDuration(beginDate));
            }
        }catch (IOException e){
            /*
             * 在写数据的时候, 对于 ClientAbortException 之类的异常, 是因为客户端取消了下载,而服务器端继续向浏览器写入数据时, 抛出这个异常,这个是正常的.
             * 尤其是对于迅雷这种吸血的客户端软件, 明明已经有一个线程在读取
             * 如果短时间内没有读取完毕,迅雷会再启第二个、第三个...线程来读取相同的字节段,
             * 直到有一个线程读取完毕,迅雷会 KILL掉其他正在下载同一字节段的线程, 强行中止字节读出,造成服务器抛 ClientAbortException.
             */
            //ClientAbortException:  java.net.SocketException: Connection reset by peer: socket write error
            final String exceptionName = e.getClass().getName();

            if (StringUtils.contains(exceptionName, "ClientAbortException")
                            || StringUtils.contains(e.getMessage(), "ClientAbortException")){
                String pattern = "[ClientAbortException],maybe user use Thunder soft or abort client soft download,exceptionName:[{}],exception message:[{}] ,request User-Agent:[{}]";
                LOGGER.warn(pattern, exceptionName, e.getMessage(), RequestUtil.getHeaderUserAgent(request));
            }else{
                LOGGER.error("[download exception],exception name: " + exceptionName, e);
                throw new UncheckedIOException(e);
            }
        }
    }

    //---------------------------------------------------------------

    /**
     * 设置 download response header.
     *
     * @param saveFileName
     *            the save file name
     * @param contentLength
     *            the content length
     * @param contentType
     *            如果传递了改参数,使用传递的值;如果没有传递,即为 <code>null</code>,那么使用默认的值,参见 {@link #resolverContentType(String, String)}
     * @param contentDisposition
     *            如果传递了改参数,使用传递的值;如果没有传递,即为 <code>null</code>,那么使用默认的值,参见 {@link #resolverContentDisposition(String, String)}
     * @param response
     *            the response
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
     * Resolver content disposition.
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
     * @param saveFileName
     *            the save file name
     * @param contentDisposition
     *            the content disposition
     * @return the string
     * @since 1.4.0
     */
    private static String resolverContentDisposition(String saveFileName,String contentDisposition){
        return isNotNullOrEmpty(contentDisposition) ? contentDisposition : "attachment; filename=" + URIUtil.encode(saveFileName, UTF8);
    }

    /**
     * Resolver content type.
     *
     * @param saveFileName
     *            the save file name
     * @param inputContentType
     *            the content type
     * @return the string
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
