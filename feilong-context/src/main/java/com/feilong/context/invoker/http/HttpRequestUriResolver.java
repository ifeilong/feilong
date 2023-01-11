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
package com.feilong.context.invoker.http;

import static com.feilong.core.bean.ConvertUtil.toMap;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.Validate;
import com.feilong.core.lang.StringUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.lang3.StringUtils;
import com.feilong.template.TemplateUtil;

/**
 * 解析 http request uri.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.12.9
 */
class HttpRequestUriResolver{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUriResolver.class);

    /** Don't let anyone instantiate this class. */
    private HttpRequestUriResolver(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 使用 request 来解析uri.
     * 
     * 目前uri 支持3种类型
     * 
     * <ol>
     * <li>固定的, 比如https://api.xxxx.com//openxxxxx-app/album_browse_records</li>
     * <li>动态的简单占位符,比如 https://api.sandbox.midtrans.com/v2/%s/status</li>
     * <li>动态的,复杂模版,比如 https://api.sandbox.midtrans.com/v2/${defaultCloseRequest.tradeNo}/cancel,使用Velocity 来解析</li>
     * </ol>
     *
     * @param <T>
     *            the generic type
     * @param uri
     *            the uri
     * @param request
     *            the request
     * @return 如果 <code>uri</code> 是null,抛出 {@link NullPointerException}<br>
     *         如果 <code>uri</code> 是blank,抛出 {@link IllegalArgumentException}<br>
     *         如果 <code>uri</code> 不包含 $ 符号,那么直接返回 <code>uri</code><br>
     *         如果 <code>uri</code> 包含 $ 符号,那么如果 <code>request</code> 是null,抛出 {@link NullPointerException}<br>
     *         否则使用 模板来解析 <code>uri</code><br>
     * @see <a href="https://github.com/venusdrogon/feilong-platform/issues/233">支持将请求参数放在路径中的配置</a>
     */
    static <T> String resolve(String uri,T request){
        Validate.notBlank(uri, "uri can't be blank!");

        //是不是format 或者模版url,如果不是,那么直接返回
        boolean isFormatOrTemplateUri = StringUtils.containsAny(uri, "%s", "${");
        if (!isFormatOrTemplateUri){
            return uri;
        }

        //---------------------------------------------------------------
        String result = parse(uri, request);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("parseUri:[{}],request:[{}],result:[{}]", uri, JsonUtil.toString(request), result);
        }
        return result;
    }

    //---------------------------------------------------------------

    /**
     * Parses the.
     *
     * @param <T>
     *            the generic type
     * @param uri
     *            the uri
     * @param request
     *            the request
     * @return the string
     * @see <a href=
     *      "https://github.com/venusdrogon/feilong-platform/issues/303">HttpRequestUriResolver.resolveTemplate(String, T) 要支持 %s 这样的参数</a>
     * @since 1.14.0
     */
    private static <T> String parse(String uri,T request){
        //since 1.14.0
        //常用于只有1个参数的 简单动态url 解析, 比如https://api.sandbox.midtrans.com/v2/%s/status
        //参数是 56677 这种字符串查询订单状态
        if (uri.contains("%s")){
            return StringUtil.format(uri, request);
        }

        //---------------------------------------------------------------
        //use Velocity
        //适用于 https://api.sandbox.midtrans.com/v2/${defaultCloseRequest.tradeNo}/cancel
        if (uri.contains("${")){
            return resolveTemplateUri(uri, request);
        }
        //---------------------------------------------------------------
        return uri;
    }

    //---------------------------------------------------------------

    /**
     * 动态的,复杂模版,比如 https://api.sandbox.midtrans.com/v2/${defaultCloseRequest.tradeNo}/cancel,使用Velocity 来解析.
     * 
     * 其中 Velocity key 是 指定参数T request 的class SimpleName 首字母小写
     *
     * @param <T>
     *            the generic type
     * @param uri
     *            支持复杂模版,比如 https://api.sandbox.midtrans.com/v2/${defaultCloseRequest.tradeNo}/cancel
     * @param request
     *            the request
     * @return the string
     */
    private static <T> String resolveTemplateUri(String uri,T request){
        Validate.notNull(request, "request can't be null!");

        //---------------------------------------------------------------
        //key 是类名首字母小写
        String attributeName = StringUtils.uncapitalize(request.getClass().getSimpleName());
        Map<String, T> templateParams = toMap(attributeName, request);

        String result = TemplateUtil.parseString(uri, templateParams);
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("parseTemplateUri:[{}],useTemplateParams:[{}],result:[{}]", uri, JsonUtil.formatSimpleMap(templateParams), result);
        }
        return result;
    }

}
