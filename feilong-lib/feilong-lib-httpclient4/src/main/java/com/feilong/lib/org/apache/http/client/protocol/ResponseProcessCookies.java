/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.feilong.lib.org.apache.http.client.protocol;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.org.apache.http.Header;
import com.feilong.lib.org.apache.http.HeaderIterator;
import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.HttpResponseInterceptor;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.client.CookieStore;
import com.feilong.lib.org.apache.http.cookie.Cookie;
import com.feilong.lib.org.apache.http.cookie.CookieOrigin;
import com.feilong.lib.org.apache.http.cookie.CookieSpec;
import com.feilong.lib.org.apache.http.cookie.MalformedCookieException;
import com.feilong.lib.org.apache.http.cookie.SM;
import com.feilong.lib.org.apache.http.protocol.HttpContext;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * Response interceptor that populates the current {@link CookieStore} with data
 * contained in response cookies received in the given the HTTP response.
 *
 * @since 4.0
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public class ResponseProcessCookies implements HttpResponseInterceptor{

    private static final Logger log = LoggerFactory.getLogger(ResponseProcessCookies.class);

    public ResponseProcessCookies(){
        super();
    }

    @Override
    public void process(final HttpResponse response,final HttpContext context) throws HttpException,IOException{
        Args.notNull(response, "HTTP request");
        Args.notNull(context, "HTTP context");

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        // Obtain actual CookieSpec instance
        final CookieSpec cookieSpec = clientContext.getCookieSpec();
        if (cookieSpec == null){
            log.debug("Cookie spec not specified in HTTP context");
            return;
        }
        // Obtain cookie store
        final CookieStore cookieStore = clientContext.getCookieStore();
        if (cookieStore == null){
            log.debug("Cookie store not specified in HTTP context");
            return;
        }
        // Obtain actual CookieOrigin instance
        final CookieOrigin cookieOrigin = clientContext.getCookieOrigin();
        if (cookieOrigin == null){
            log.debug("Cookie origin not specified in HTTP context");
            return;
        }
        HeaderIterator it = response.headerIterator(SM.SET_COOKIE);
        processCookies(it, cookieSpec, cookieOrigin, cookieStore);

        // see if the cookie spec supports cookie versioning.
        if (cookieSpec.getVersion() > 0){
            // process set-cookie2 headers.
            // Cookie2 will replace equivalent Cookie instances
            it = response.headerIterator(SM.SET_COOKIE2);
            processCookies(it, cookieSpec, cookieOrigin, cookieStore);
        }
    }

    private void processCookies(
                    final HeaderIterator iterator,
                    final CookieSpec cookieSpec,
                    final CookieOrigin cookieOrigin,
                    final CookieStore cookieStore){
        while (iterator.hasNext()){
            final Header header = iterator.nextHeader();
            try{
                final List<Cookie> cookies = cookieSpec.parse(header, cookieOrigin);
                for (final Cookie cookie : cookies){
                    try{
                        cookieSpec.validate(cookie, cookieOrigin);
                        cookieStore.addCookie(cookie);

                        if (log.isDebugEnabled()){
                            log.debug("Cookie accepted [" + formatCooke(cookie) + "]");
                        }
                    }catch (final MalformedCookieException ex){
                        if (log.isWarnEnabled()){
                            log.warn("Cookie rejected [" + formatCooke(cookie) + "] " + ex.getMessage());
                        }
                    }
                }
            }catch (final MalformedCookieException ex){
                if (log.isWarnEnabled()){
                    log.warn("Invalid cookie header: \"" + header + "\". " + ex.getMessage());
                }
            }
        }
    }

    private static String formatCooke(final Cookie cookie){
        final StringBuilder buf = new StringBuilder();
        buf.append(cookie.getName());
        buf.append("=\"");
        String v = cookie.getValue();
        if (v != null){
            if (v.length() > 100){
                v = v.substring(0, 100) + "...";
            }
            buf.append(v);
        }
        buf.append("\"");
        buf.append(", version:");
        buf.append(Integer.toString(cookie.getVersion()));
        buf.append(", domain:");
        buf.append(cookie.getDomain());
        buf.append(", path:");
        buf.append(cookie.getPath());
        buf.append(", expiry:");
        buf.append(cookie.getExpiryDate());
        return buf.toString();
    }

}
