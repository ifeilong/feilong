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

package com.feilong.lib.org.apache.http.impl.conn;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;

import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.config.ConnectionConfig;
import com.feilong.lib.org.apache.http.conn.HttpConnectionFactory;
import com.feilong.lib.org.apache.http.conn.ManagedHttpClientConnection;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.entity.ContentLengthStrategy;
import com.feilong.lib.org.apache.http.impl.entity.LaxContentLengthStrategy;
import com.feilong.lib.org.apache.http.impl.entity.StrictContentLengthStrategy;
import com.feilong.lib.org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import com.feilong.lib.org.apache.http.io.HttpMessageParserFactory;
import com.feilong.lib.org.apache.http.io.HttpMessageWriterFactory;

/**
 * Factory for {@link ManagedHttpClientConnection} instances.
 * 
 * @since 4.3
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class ManagedHttpClientConnectionFactory implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection>{

    private static final AtomicLong                        COUNTER  = new AtomicLong();

    public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();

    private final HttpMessageWriterFactory<HttpRequest>    requestWriterFactory;

    private final HttpMessageParserFactory<HttpResponse>   responseParserFactory;

    private final ContentLengthStrategy                    incomingContentStrategy;

    private final ContentLengthStrategy                    outgoingContentStrategy;

    /**
     * @since 4.4
     */
    public ManagedHttpClientConnectionFactory(final HttpMessageWriterFactory<HttpRequest> requestWriterFactory,
                    final HttpMessageParserFactory<HttpResponse> responseParserFactory, final ContentLengthStrategy incomingContentStrategy,
                    final ContentLengthStrategy outgoingContentStrategy){
        super();
        this.requestWriterFactory = requestWriterFactory != null ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE;
        this.responseParserFactory = responseParserFactory != null ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE;
        this.incomingContentStrategy = incomingContentStrategy != null ? incomingContentStrategy : LaxContentLengthStrategy.INSTANCE;
        this.outgoingContentStrategy = outgoingContentStrategy != null ? outgoingContentStrategy : StrictContentLengthStrategy.INSTANCE;
    }

    public ManagedHttpClientConnectionFactory(final HttpMessageWriterFactory<HttpRequest> requestWriterFactory,
                    final HttpMessageParserFactory<HttpResponse> responseParserFactory){
        this(requestWriterFactory, responseParserFactory, null, null);
    }

    public ManagedHttpClientConnectionFactory(final HttpMessageParserFactory<HttpResponse> responseParserFactory){
        this(null, responseParserFactory);
    }

    public ManagedHttpClientConnectionFactory(){
        this(null, null);
    }

    @Override
    public ManagedHttpClientConnection create(final HttpRoute route,final ConnectionConfig config){
        final ConnectionConfig cconfig = config != null ? config : ConnectionConfig.DEFAULT;
        CharsetDecoder charDecoder = null;
        CharsetEncoder charEncoder = null;
        final Charset charset = cconfig.getCharset();
        final CodingErrorAction malformedInputAction = cconfig.getMalformedInputAction() != null ? cconfig.getMalformedInputAction()
                        : CodingErrorAction.REPORT;
        final CodingErrorAction unmappableInputAction = cconfig.getUnmappableInputAction() != null ? cconfig.getUnmappableInputAction()
                        : CodingErrorAction.REPORT;
        if (charset != null){
            charDecoder = charset.newDecoder();
            charDecoder.onMalformedInput(malformedInputAction);
            charDecoder.onUnmappableCharacter(unmappableInputAction);
            charEncoder = charset.newEncoder();
            charEncoder.onMalformedInput(malformedInputAction);
            charEncoder.onUnmappableCharacter(unmappableInputAction);
        }
        final String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
        return new LoggingManagedHttpClientConnection(
                        id,
                        cconfig.getBufferSize(),
                        cconfig.getFragmentSizeHint(),
                        charDecoder,
                        charEncoder,
                        cconfig.getMessageConstraints(),
                        incomingContentStrategy,
                        outgoingContentStrategy,
                        requestWriterFactory,
                        responseParserFactory);
    }

}
