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

import java.io.IOException;

import javax.net.ssl.SSLContext;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.feilong.core.Validate;
import com.feilong.core.lang.StringUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.net.SSLContextBuilder;
import com.feilong.net.SSLProtocol;
import com.feilong.net.UriProcessor;

/**
 * Jsoup 的工具类.
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.0.6
 */
public final class JsoupUtil{

    /** <code>{@value}</code>. */
    public static final int     DEFAULT_TIMEOUT_MILLIS = 20 * MILLISECOND_PER_SECONDS;

    /**
     * 伪造的 useragent.
     * 
     * @since 1.8.1
     */
    private static final String DEFAULT_USER_AGENT     = "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21";

    //---------------------------------------------------------------
    /** Don't let anyone instantiate this class. */
    private JsoupUtil(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 通过url 获得文档.
     * 
     * @param urlString
     *            the url string
     * @return the document
     * @throws JsoupUtilException
     *             if Exception
     */
    public static Document getDocument(String urlString){
        return getDocument(urlString, DEFAULT_USER_AGENT);
    }

    //---------------------------------------------------------------

    /**
     * Parse HTML into a Document. As no base URI is specified, absolute URL detection relies on the HTML including a
     * {@code <base href>} tag.
     * 
     * <h3>示例:</h3>
     * 
     * <blockquote>
     * 
     * 比如
     * 
     * <pre>
    {@code
    <form id = "pay_form" action="https://gateway.test.95516.com/gateway/api/frontTransReq.do" method="post">
     * <input type="hidden" name="bizType" id="bizType" value="000201"/>
     * <input type="hidden" name="signMethod" id="signMethod" value="01"/>
     * <input type="hidden" name="txnAmt" id="txnAmt" value="100"/>
     * <input type="hidden" name="orderDesc" id="orderDesc" value="reebok 香港官方商城商品"/>
     * </form>
    }
     * </pre>
     * 
     * 可以将这个字符串 转成 {@link Document}
     * 
     * </blockquote>
     *
     * @param html
     *            the html
     * @return 如果 <code>html</code> 是null,抛出 {@link NullPointerException}<br>
     * @since 1.13.0
     */
    public static Document parse(String html){
        Validate.notNull(html, "html can't be null!");
        return Jsoup.parse(html);
    }

    //---------------------------------------------------------------

    /**
     * 通过url 获得文档.
     * 
     * @param urlString
     *            the url
     * @param userAgent
     *            the user agent
     * @return the document
     * @throws JsoupUtilException
     *             if Exception
     * @see org.jsoup.Jsoup#connect(String)
     * @see org.jsoup.Connection#userAgent(String)
     * @see org.jsoup.Connection#timeout(int)
     * @see org.jsoup.Connection#get()
     */
    public static Document getDocument(String urlString,String userAgent){
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
            throw new JsoupUtilException(StringUtil.formatPattern("urlString:[{}],userAgent:[{}]", urlString, userAgent), e);
        }
    }

    //---------------------------------------------------------------

    /**
     * Gets the elements by select.
     * 
     * @param url
     *            the url
     * @param selectQuery
     *            the select query
     * @return 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>selectQuery</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>selectQuery</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #getDocument(String)
     * @see org.jsoup.nodes.Element#select(String)
     */
    public static Elements getElementsBySelect(String url,String selectQuery){
        Validate.notBlank(url, "url can't be blank!");
        Validate.notBlank(selectQuery, "selectQuery can't be blank!");

        Document document = getDocument(url);
        return document.select(selectQuery);
    }

    //---------------------------------------------------------------

    /**
     * Gets the element by id.
     *
     * @param url
     *            the url
     * @param id
     *            the id
     * @return 如果 <code>url</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>url</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>id</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>id</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     * @see #getDocument(String)
     * @see org.jsoup.nodes.Element#getElementById(String)
     */
    public static Element getElementById(String url,String id){
        Validate.notBlank(url, "url can't be blank!");
        Validate.notBlank(id, "id can't be blank!");

        Document document = getDocument(url);
        return document.getElementById(id);
    }
}
