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
package com.feilong.net.http;

import static com.feilong.core.date.DateUtil.formatDuration;
import static com.feilong.core.date.DateUtil.now;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.net.URLUtil;
import com.feilong.io.IOWriteUtil;
import com.feilong.net.UncheckedHttpException;
import com.feilong.tools.slf4j.Slf4jUtil;

/**
 * 专注于下载的工具类.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.5.1
 */
public final class URLDownloadUtil{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(URLDownloadUtil.class);

    /** Don't let anyone instantiate this class. */
    private URLDownloadUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将网络文件下载到文件夹.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <p>
     * 下载一个小说文件到桌面
     * </p>
     * 
     * <pre class="code">
     * 
     * public void down(){
     *     String url = "http://www.kenwen.com/egbk/31/31186/4395342.txt";
     *     String directoryName = SpecialFolder.getDesktop();
     *     URLDownloadUtil.download(url, directoryName);
     * }
     * 
     * </pre>
     * 
     * </blockquote>
     * 
     * <h3>说明:</h3>
     * <blockquote>
     * <ol>
     * <li>取到网络文件的文件名 原样下载到目标文件夹</li>
     * <li>如果 <code>urlString</code> 是null,抛出 {@link NullPointerException}</li>
     * <li>如果 <code>urlString</code> 是blank,抛出 {@link IllegalArgumentException}</li>
     * <li>如果 <code>directoryName</code> 是null,抛出 {@link NullPointerException}</li>
     * <li>如果 <code>directoryName</code> 是blank,抛出 {@link IllegalArgumentException}</li>
     * </ol>
     * </blockquote>
     *
     * @param urlString
     *            网络任意文件<br>
     *            url 不能带参数
     * @param directoryName
     *            目标文件夹
     * @see IOWriteUtil#write(InputStream, String, String)
     * @see org.apache.commons.io.FileUtils#copyURLToFile(URL, File)
     * @see org.apache.commons.io.FileUtils#copyURLToFile(URL, File, int, int)
     */
    public static void download(String urlString,String directoryName){
        Validate.notBlank(urlString, "urlString can't be null/empty!");
        Validate.notBlank(directoryName, "directoryName can't be null/empty!");

        //---------------------------------------------------------------
        Date beginDate = now();
        LOGGER.info("begin download,urlString:[{}],directoryName:[{}]", urlString, directoryName);

        URL url = URLUtil.toURL(urlString);

        //---------------------------------------------------------------
        try (InputStream inputStream = URLUtil.openStream(url)){
            IOWriteUtil.write(inputStream, directoryName, createFileName(urlString));

            //---------------------------------------------------------------
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("end download,url:[{}],directoryName:[{}],use time: [{}]", urlString, directoryName, formatDuration(beginDate));
            }
        }catch (IOException e){
            throw new UncheckedHttpException(Slf4jUtil.format("urlString:[{}],directoryName:[{}]", urlString, directoryName), e);
        }

    }

    //---------------------------------------------------------------

    /**
     * 创建 file name.
     *
     * @param urlString
     *            the url string
     * @return the string
     * @since 1.7.3
     */
    private static String createFileName(String urlString){
        File file = new File(urlString);
        return file.getName();
    }
}
