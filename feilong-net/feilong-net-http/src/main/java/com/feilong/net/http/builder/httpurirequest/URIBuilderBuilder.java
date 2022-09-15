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
package com.feilong.net.http.builder.httpurirequest;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.tools.slf4j.Slf4jUtil.format;

import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.json.JsonUtil;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.http.HttpRequest;

/**
 * 将{@link HttpRequest} 转成 {@link URIBuilder} 的构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see <a href=
 *      "https://stackoverflow.com/questions/22038957/httpclient-getparams-deprecated-what-should-i-use-instead">HttpClient.getParams()
 *      deprecated. What should I use instead?
 *      </a>
 * @since 1.10.6
 */
class URIBuilderBuilder{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(URIBuilderBuilder.class);

    /** Don't let anyone instantiate this class. */
    private URIBuilderBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * 将{@link HttpRequest} 转成 {@link URIBuilder} 的构造器.
     *
     * @param httpRequest
     *            the http request
     * @return the URI builder
     */
    static URIBuilder build(HttpRequest httpRequest){
        String uri = httpRequest.getUri();
        Map<String, String> paramMap = httpRequest.getParamMap();

        try{
            URIBuilder uriBuilder = new URIBuilder(uri);

            if (isNullOrEmpty(paramMap)){
                if (LOGGER.isTraceEnabled()){
                    LOGGER.trace("httpRequest [paramMap] isNullOrEmpty,skip!,httpRequestInfo:[{}]", JsonUtil.toString(httpRequest, true));
                }
                return uriBuilder;
            }
            return buildWithParameters(uriBuilder, paramMap);
        }catch (Exception e){
            String message = format("httpRequest:[{}]", JsonUtil.toString(httpRequest, true));
            throw new UncheckedHttpException(message, e);
        }
    }

    //---------------------------------------------------------------

    /**
     * set params
     *
     * @param uriBuilder
     *            the uri builder
     * @param paramMap
     *            the param map
     * @return the URI builder
     * @since 1.12.9
     */
    private static URIBuilder buildWithParameters(URIBuilder uriBuilder,Map<String, String> paramMap){
        for (Map.Entry<String, String> entry : paramMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            LOGGER.trace("httpUriRequest.setHeader({}, {})", key, value);
            uriBuilder.setParameter(key, value);
        }
        return uriBuilder;
    }
}
