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

package com.feilong.lib.org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

import com.feilong.lib.org.apache.http.FormattedHeader;
import com.feilong.lib.org.apache.http.Header;
import com.feilong.lib.org.apache.http.HttpHost;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.auth.AuthOption;
import com.feilong.lib.org.apache.http.auth.AuthScheme;
import com.feilong.lib.org.apache.http.auth.AuthSchemeProvider;
import com.feilong.lib.org.apache.http.auth.AuthScope;
import com.feilong.lib.org.apache.http.auth.Credentials;
import com.feilong.lib.org.apache.http.auth.MalformedChallengeException;
import com.feilong.lib.org.apache.http.client.AuthCache;
import com.feilong.lib.org.apache.http.client.AuthenticationStrategy;
import com.feilong.lib.org.apache.http.client.CredentialsProvider;
import com.feilong.lib.org.apache.http.client.config.AuthSchemes;
import com.feilong.lib.org.apache.http.client.config.RequestConfig;
import com.feilong.lib.org.apache.http.client.protocol.HttpClientContext;
import com.feilong.lib.org.apache.http.config.Lookup;
import com.feilong.lib.org.apache.http.protocol.HTTP;
import com.feilong.lib.org.apache.http.protocol.HttpContext;
import com.feilong.lib.org.apache.http.util.Args;
import com.feilong.lib.org.apache.http.util.CharArrayBuffer;

@lombok.extern.slf4j.Slf4j
@Contract(threading = ThreadingBehavior.IMMUTABLE)
abstract class AuthenticationStrategyImpl implements AuthenticationStrategy{

    private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(
                    Arrays.asList(
                                    AuthSchemes.SPNEGO,
                                    AuthSchemes.KERBEROS,
                                    AuthSchemes.NTLM,
                                    AuthSchemes.CREDSSP,
                                    AuthSchemes.DIGEST,
                                    AuthSchemes.BASIC));

    private final int                 challengeCode;

    private final String              headerName;

    /**
     * @param challengeCode
     *            for example SC_PROXY_AUTHENTICATION_REQUIRED or SC_UNAUTHORIZED
     * @param headerName
     *            for example "Proxy-Authenticate" or "WWW-Authenticate"
     */
    AuthenticationStrategyImpl(final int challengeCode, final String headerName){
        super();
        this.challengeCode = challengeCode;
        this.headerName = headerName;
    }

    @Override
    public boolean isAuthenticationRequested(final HttpHost authhost,final HttpResponse response,final HttpContext context){
        Args.notNull(response, "HTTP response");
        final int status = response.getStatusLine().getStatusCode();
        return status == this.challengeCode;
    }

    /**
     * Generates a map of challenge auth-scheme =&gt; Header entries.
     *
     * @return map: key=lower-cased auth-scheme name, value=Header that contains the challenge
     */
    @Override
    public Map<String, Header> getChallenges(final HttpHost authhost,final HttpResponse response,final HttpContext context)
                    throws MalformedChallengeException{
        Args.notNull(response, "HTTP response");
        final Header[] headers = response.getHeaders(this.headerName);
        final Map<String, Header> map = new HashMap<>(headers.length);
        for (final Header header : headers){
            final CharArrayBuffer buffer;
            int pos;
            if (header instanceof FormattedHeader){
                buffer = ((FormattedHeader) header).getBuffer();
                pos = ((FormattedHeader) header).getValuePos();
            }else{
                final String s = header.getValue();
                if (s == null){
                    throw new MalformedChallengeException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                pos = 0;
            }
            while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))){
                pos++;
            }
            final int beginIndex = pos;
            while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))){
                pos++;
            }
            final int endIndex = pos;
            final String s = buffer.substring(beginIndex, endIndex);
            map.put(s.toLowerCase(Locale.ROOT), header);
        }
        return map;
    }

    abstract Collection<String> getPreferredAuthSchemes(RequestConfig config);

    @Override
    public Queue<AuthOption> select(
                    final Map<String, Header> challenges,
                    final HttpHost authhost,
                    final HttpResponse response,
                    final HttpContext context) throws MalformedChallengeException{
        Args.notNull(challenges, "Map of auth challenges");
        Args.notNull(authhost, "Host");
        Args.notNull(response, "HTTP response");
        Args.notNull(context, "HTTP context");
        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        final Queue<AuthOption> options = new LinkedList<>();
        final Lookup<AuthSchemeProvider> registry = clientContext.getAuthSchemeRegistry();
        if (registry == null){
            log.debug("Auth scheme registry not set in the context");
            return options;
        }
        final CredentialsProvider credsProvider = clientContext.getCredentialsProvider();
        if (credsProvider == null){
            log.debug("Credentials provider not set in the context");
            return options;
        }
        final RequestConfig config = clientContext.getRequestConfig();
        Collection<String> authPrefs = getPreferredAuthSchemes(config);
        if (authPrefs == null){
            authPrefs = DEFAULT_SCHEME_PRIORITY;
        }
        if (log.isDebugEnabled()){
            log.debug("Authentication schemes in the order of preference: " + authPrefs);
        }

        for (final String id : authPrefs){
            final Header challenge = challenges.get(id.toLowerCase(Locale.ROOT));
            if (challenge != null){
                final AuthSchemeProvider authSchemeProvider = registry.lookup(id);
                if (authSchemeProvider == null){
                    if (log.isWarnEnabled()){
                        log.warn("Authentication scheme " + id + " not supported");
                        // Try again
                    }
                    continue;
                }
                final AuthScheme authScheme = authSchemeProvider.create(context);
                authScheme.processChallenge(challenge);

                final AuthScope authScope = new AuthScope(authhost, authScheme.getRealm(), authScheme.getSchemeName());

                final Credentials credentials = credsProvider.getCredentials(authScope);
                if (credentials != null){
                    options.add(new AuthOption(authScheme, credentials));
                }
            }else{
                if (log.isDebugEnabled()){
                    log.debug("Challenge for " + id + " authentication scheme not available");
                    // Try again
                }
            }
        }
        return options;
    }

    @Override
    public void authSucceeded(final HttpHost authhost,final AuthScheme authScheme,final HttpContext context){
        Args.notNull(authhost, "Host");
        Args.notNull(authScheme, "Auth scheme");
        Args.notNull(context, "HTTP context");

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        if (isCachable(authScheme)){
            AuthCache authCache = clientContext.getAuthCache();
            if (authCache == null){
                authCache = new BasicAuthCache();
                clientContext.setAuthCache(authCache);
            }
            if (log.isDebugEnabled()){
                log.debug("Caching '" + authScheme.getSchemeName() + "' auth scheme for " + authhost);
            }
            authCache.put(authhost, authScheme);
        }
    }

    protected boolean isCachable(final AuthScheme authScheme){
        if (authScheme == null || !authScheme.isComplete()){
            return false;
        }
        final String schemeName = authScheme.getSchemeName();
        return schemeName.equalsIgnoreCase(AuthSchemes.BASIC);
    }

    @Override
    public void authFailed(final HttpHost authhost,final AuthScheme authScheme,final HttpContext context){
        Args.notNull(authhost, "Host");
        Args.notNull(context, "HTTP context");

        final HttpClientContext clientContext = HttpClientContext.adapt(context);

        final AuthCache authCache = clientContext.getAuthCache();
        if (authCache != null){
            if (log.isDebugEnabled()){
                log.debug("Clearing cached auth scheme for " + authhost);
            }
            authCache.remove(authhost);
        }
    }

}
