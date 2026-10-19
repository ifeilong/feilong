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
package com.feilong.net.jsoup;

import static com.feilong.core.TimeInterval.MILLISECOND_PER_SECONDS;
import static com.feilong.core.lang.StringUtil.formatPattern;

import java.io.IOException;

import javax.net.ssl.SSLContext;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.feilong.core.Validate;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.net.SSLContextBuilder;
import com.feilong.net.SSLProtocol;
import com.feilong.net.UriProcessor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 基于 <a href="https://jsoup.org/">jsoup</a> 的 html 工具类.
 * 
 * <h3>依赖(使用前必读):</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>本类依赖 <code>org.jsoup:jsoup</code>, 它在 <code>feilong-net-jsoup</code> 的 pom 中被声明为 <code>optional</code>,
 * <b>不会传递</b>到使用方, 必须自己声明</li>
 * <li>聚合包 <code>com.github.ifeilong:feilong</code> 发布到中央仓库的 pom 中只有 <code>slf4j-api</code> 一个依赖, 其余的第三方库
 * (jsoup, poi, javax.mail, jsch, velocity, xstream, cxf 等) 都需要使用方按需自己声明</li>
 * <li>如果 classpath 中缺少 jsoup, 调用本类的方法会抛出 {@link JsoupUtilException}, message 中会直接提示需要添加的依赖,
 * 而不是难以定位的 <code>java.lang.NoClassDefFoundError: org/jsoup/Jsoup</code></li>
 * </ol>
 * </blockquote>
 * 
 * <h3>需要添加的 maven 依赖:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * &lt;dependency&gt;
 *     &lt;groupId&gt;org.jsoup&lt;/groupId&gt;
 *     &lt;artifactId&gt;jsoup&lt;/artifactId&gt;
 *     &lt;version&gt;1.15.4&lt;/version&gt;
 * &lt;/dependency&gt;
 * </pre>
 * 
 * <code>1.15.4</code> 是 feilong 编译和测试使用的版本, 建议使用该版本或者更高的版本.
 * </blockquote>
 * 
 * <h3>示例:</h3>
 * 
 * <blockquote>
 * 
 * <pre class="code">
 * 
 * String url = "https://github.com/ifeilong/feilong";
 * 
 * // 1. 获得整个文档
 * Document document = JsoupUtil.getDocument(url);
 * 
 * // 2. 按 css selector 获得元素
 * Elements links = JsoupUtil.getElementsBySelect(url, "a[href]");
 * 
 * // 3. 按 id 获得元素
 * Element element = JsoupUtil.getElementById(url, "readme");
 * 
 * // 4. 把 html 字符串解析成文档
 * Document parsed = JsoupUtil.parse("&lt;a href=\"https://jsoup.org/\"&gt;jsoup&lt;/a&gt;");
 * 
 * </pre>
 * 
 * </blockquote>
 * 
 * <h3>说明:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>默认请求超时时间是 {@value #DEFAULT_TIMEOUT_MILLIS} 毫秒(20 秒)</li>
 * <li><code>https</code> 请求会使用 {@link SSLProtocol#TLSv12} 的 {@link SSLContext}</li>
 * <li>请求的 url 会先 trim, 并把空格转义成 <code>%20</code>, 见 {@link UriProcessor#process(String, boolean)}</li>
 * <li>请求过程发生的 {@link IOException} 会被包装成 {@link JsoupUtilException}, 原始异常在 cause 中</li>
 * <li>本类是工具类, 不需要也不允许创建实例</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see JsoupUtilException
 * @see <a href="https://jsoup.org/cookbook/extracting-data/selector-syntax">jsoup selector 语法</a>
 * @since 1.0.6
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsoupUtil{

    /**
     * 默认请求超时时间, 单位毫秒, 目前是 <code>20</code> 秒.
     */
    public static final int     DEFAULT_TIMEOUT_MILLIS = 20 * MILLISECOND_PER_SECONDS;

    /**
     * 伪造的 useragent.
     * 
     * @since 1.8.1
     */
    private static final String DEFAULT_USER_AGENT     = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    /**
     * jsoup 是否在 classpath 中.
     * 
     * @since 4.5.6
     */
    private static final boolean JSOUP_PRESENT         = isJsoupPresent();

    //---------------------------------------------------------------

    /**
     * 判断 jsoup 是否在 classpath 中.
     * 
     * @return 如果 jsoup 在 classpath 中,返回 <code>true</code>
     * @since 4.5.6
     */
    private static boolean isJsoupPresent(){
        try{
            Class.forName("org.jsoup.Jsoup", false, JsoupUtil.class.getClassLoader());
            return true;
        }catch (ClassNotFoundException e){
            return false;
        }
    }

    /**
     * 检查 jsoup 是否在 classpath 中.
     * 
     * <p>
     * jsoup 是 <code>optional</code> 依赖, 如果使用方没有添加, 那么给出友好的提示信息, 而不是 {@code java.lang.NoClassDefFoundError}
     * </p>
     * 
     * @throws JsoupUtilException
     *             如果 jsoup 不在 classpath 中
     * @see <a href="https://github.com/ifeilong/feilong/issues/525">提供 jsoup 依赖的文档描述,和对应的友好报错信息</a>
     * @since 4.5.6
     */
    private static void checkJsoupPresent(){
        if (!JSOUP_PRESENT){
            throw new JsoupUtilException(
                            "jsoup not found in classpath! feilong does not ship jsoup transitively, please add maven dependency [org.jsoup:jsoup:1.15.4], see https://jsoup.org/");
        }
    }

    //---------------------------------------------------------------

    /**
     * 通过 url 获得文档.
     * 
     * <p>
     * 使用伪造的浏览器 User-Agent 发起请求; 需要自定义 User-Agent 时, 使用 {@link #getDocument(String, String)}
     * </p>
     * 
     * @param urlString
     *            the url string
     * @return 请求该 url 得到的 {@link Document},请求过程发生异常时抛出 {@link JsoupUtilException}<br>
     * @throws JsoupUtilException
     *             if Exception
     * @see #getDocument(String, String)
     */
    public static Document getDocument(String urlString){
        return getDocument(urlString, DEFAULT_USER_AGENT);
    }

    //---------------------------------------------------------------

    /**
     * 把 html 字符串解析成 {@link Document}.
     * 
     * <p>
     * 没有指定 base uri 时, {@link Document} 中对绝对路径的识别, 依赖于 html 中是否包含 {@code <base href>} 标签
     * </p>
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * <pre class="code">
     * 
     * String html = "&lt;form id=\"pay_form\" action=\"https://gateway.test.95516.com/gateway/api/frontTransReq.do\" method=\"post\"&gt;"
     *                 + "&lt;input type=\"hidden\" name=\"bizType\" id=\"bizType\" value=\"000201\"/&gt;"
     *                 + "&lt;input type=\"hidden\" name=\"signMethod\" id=\"signMethod\" value=\"01\"/&gt;"
     *                 + "&lt;input type=\"hidden\" name=\"txnAmt\" id=\"txnAmt\" value=\"100\"/&gt;"
     *                 + "&lt;/form&gt;";
     * 
     * Document document = JsoupUtil.parse(html);
     * 
     * Element bizType = document.getElementById("bizType");
     * 
     * </pre>
     * 
     * </blockquote>
     *
     * @param html
     *            the html
     * @return 解析 <code>html</code> 得到的 {@link Document}<br>
     *         如果 <code>html</code> 是null,抛出 {@link NullPointerException}<br>
     * @throws JsoupUtilException
     *             如果 classpath 中缺少 jsoup 依赖
     * @see Jsoup#parse(String)
     * @since 1.13.0
     */
    public static Document parse(String html){
        //since 4.5.6
        checkJsoupPresent();

        Validate.notNull(html, "html can't be null!");
        return Jsoup.parse(html);
    }

    //---------------------------------------------------------------

    /**
     * 通过 url 获得文档.
     * 
     * @param urlString
     *            the url
     * @param userAgent
     *            the user agent
     * @return 请求该 url 得到的 {@link Document},请求过程发生异常时抛出 {@link JsoupUtilException}<br>
     * @throws JsoupUtilException
     *             if Exception
     * @see #getDocument(String)
     * @see Jsoup#connect(String)
     * @see Connection#userAgent(String)
     * @see Connection#timeout(int)
     * @see Connection#get()
     */
    public static Document getDocument(String urlString,String userAgent){
        //since 4.5.6
        checkJsoupPresent();

        Validate.notBlank(urlString, "urlString can't be blank!");

        //since 3.0.10
        urlString = UriProcessor.process(urlString, true);
        //---------------------------------------------------------------
        try{
            Connection connection = Jsoup.connect(urlString)//
                            .userAgent(userAgent)//
                            .timeout(DEFAULT_TIMEOUT_MILLIS);
            if (StringUtils.startsWithIgnoreCase(urlString, "https://")){
                SSLContext build = SSLContextBuilder.build(SSLProtocol.TLSv12);
                //since 3.0.7
                connection.sslSocketFactory(build.getSocketFactory());
            }
            return connection.get();
        }catch (IOException e){
            throw new JsoupUtilException(formatPattern("urlString:[{}],userAgent:[{}]", urlString, userAgent), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * 通过 url 获得文档, 再按 css selector 获得匹配的元素集合.
     * 
     * @param url
     *            the url
     * @param selectQuery
     *            the css select query
     * @return 该 url 文档中匹配 <code>selectQuery</code> 的元素集合,匹配不到时返回空集合<br>
     *         如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>selectQuery</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>selectQuery</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @throws JsoupUtilException
     *             如果 classpath 中缺少 jsoup 依赖, 或者请求过程发生异常
     * @see #getDocument(String)
     * @see <a href="https://jsoup.org/cookbook/extracting-data/selector-syntax">jsoup selector 语法</a>
     * @see Element#select(String)
     */
    public static Elements getElementsBySelect(String url,String selectQuery){
        Validate.notBlank(url, "url can't be blank!");
        Validate.notBlank(selectQuery, "selectQuery can't be blank!");

        Document document = getDocument(url);
        return document.select(selectQuery);
    }

    //---------------------------------------------------------------

    /**
     * 通过 url 获得文档, 再获得 id 对应的元素.
     * 
     * @param url
     *            the url
     * @param id
     *            the element id
     * @return 该 url 页面中 id 为 <code>id</code> 的元素,找不到时返回 null<br>
     *         如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>id</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>id</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @throws JsoupUtilException
     *             如果 classpath 中缺少 jsoup 依赖, 或者请求过程发生异常
     * @see #getDocument(String)
     * @see Element#getElementById(String)
     */
    public static Element getElementById(String url,String id){
        Validate.notBlank(url, "url can't be blank!");
        Validate.notBlank(id, "id can't be blank!");

        Document document = getDocument(url);
        return document.getElementById(id);
    }
}
