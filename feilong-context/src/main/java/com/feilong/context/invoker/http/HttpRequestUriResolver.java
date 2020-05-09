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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.lang.StringUtil;
import com.feilong.json.JsonUtil;
import com.feilong.template.TemplateUtil;

/**
 * 解析 http request uri.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
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
     * Resolve.
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

        //---------------------------------------------------------------
        String result = parse(uri, request);
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("parse uri:[{}],request:[{}],result:[{}]", uri, JsonUtil.format(request), result);
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
        if (uri.contains("%s")){
            return StringUtil.format(uri, request);
        }

        //---------------------------------------------------------------
        //use Velocity
        if (uri.contains("${")){
            return resolveTemplate(uri, request);
        }
        //---------------------------------------------------------------
        return uri;
    }

    //---------------------------------------------------------------

    /**
     * Resolve template.
     *
     * @param <T>
     *            the generic type
     * @param uri
     *            the uri
     * @param request
     *            the request
     * @return the string
     */
    private static <T> String resolveTemplate(String uri,T request){
        Validate.notNull(request, "request can't be null!");

        //---------------------------------------------------------------

        //key 是类名首字母小写
        String attributeName = StringUtils.uncapitalize(request.getClass().getSimpleName());
        Map<String, T> map = toMap(attributeName, request);

        String result = TemplateUtil.parseString(uri, map);
        //---------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("parse uri:[{}],use map:[{}],result:[{}]", uri, JsonUtil.formatSimpleMap(map), result);
        }
        return result;
    }

}
