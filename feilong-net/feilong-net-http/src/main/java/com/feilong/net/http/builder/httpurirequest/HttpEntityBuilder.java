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

import static com.feilong.core.CharsetType.UTF8;
import static com.feilong.core.Validator.isNotNullOrEmpty;
import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.lang.ObjectUtil.defaultIfNull;
import static com.feilong.core.lang.ObjectUtil.defaultZero;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.feilong.core.lang.StringUtil;
import com.feilong.json.JsonUtil;
import com.feilong.lib.org.apache.http.HttpEntity;
import com.feilong.lib.org.apache.http.NameValuePair;
import com.feilong.lib.org.apache.http.client.entity.UrlEncodedFormEntity;
import com.feilong.lib.org.apache.http.entity.ByteArrayEntity;
import com.feilong.lib.org.apache.http.entity.ContentType;
import com.feilong.lib.org.apache.http.entity.StringEntity;
import com.feilong.net.UncheckedHttpException;
import com.feilong.net.http.HttpRequest;
import com.feilong.net.http.RequestByteArrayBody;

/**
 * {@link HttpEntity} 构造器.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @see com.feilong.lib.org.apache.http.client.entity.EntityBuilder
 * @since 1.10.6
 */
final class HttpEntityBuilder{

    /** Don't let anyone instantiate this class. */
    private HttpEntityBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }
    //---------------------------------------------------------------

    /**
     * Builds the.
     *
     * @param httpRequest
     *            the http request
     * @return the http entity
     */
    static HttpEntity build(HttpRequest httpRequest){
        String requestBody = httpRequest.getRequestBody();
        if (isNotNullOrEmpty(requestBody)){
            return new StringEntity(requestBody, UTF8);
        }
        //---------------------------------------------------------------

        //since 4.0.1
        RequestByteArrayBody requestByteArrayBody = httpRequest.getRequestByteArrayBody();
        if (null != requestByteArrayBody && isNotNullOrEmpty(requestByteArrayBody.getBuf())){
            return toByteArrayEntity(requestByteArrayBody);
        }
        //---------------------------------------------------------------
        return buildFormHttpEntity(httpRequest.getParamMap());
    }

    //---------------------------------------------------------------

    /**
     * 转成 ByteArrayEntity.
     * 
     * @param requestByteArrayBody
     * @return
     * @since 4.0.1
     */
    private static HttpEntity toByteArrayEntity(RequestByteArrayBody requestByteArrayBody){
        byte[] buf = requestByteArrayBody.getBuf();
        String mimeType = requestByteArrayBody.getMimeType();

        return new ByteArrayEntity(
                        buf,
                        defaultZero(requestByteArrayBody.getOff()),
                        defaultIfNull(requestByteArrayBody.getLength(), buf.length),
                        null == mimeType ? null : ContentType.create(mimeType)

        );
    }

    /**
     * Builds the form http entity.
     *
     * @param paramMap
     *            the param map
     * @return the http entity
     */
    private static HttpEntity buildFormHttpEntity(Map<String, String> paramMap){
        if (isNullOrEmpty(paramMap)){
            return null;
        }

        //---------------------------------------------------------------
        List<NameValuePair> nameValuePairList = NameValuePairBuilder.build(paramMap);
        try{
            return new UrlEncodedFormEntity(nameValuePairList, UTF8);
        }catch (UnsupportedEncodingException e){
            String message = StringUtil.formatPattern("paramMap:[{}]", JsonUtil.toString(paramMap));
            throw new UncheckedHttpException(message, e);
        }
    }
}
