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

package com.feilong.lib.org.apache.http.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import com.feilong.lib.org.apache.http.HttpEntity;
import com.feilong.lib.org.apache.http.HttpEntityEnclosingRequest;
import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.HttpServerConnection;
import com.feilong.lib.org.apache.http.config.MessageConstraints;
import com.feilong.lib.org.apache.http.entity.ContentLengthStrategy;
import com.feilong.lib.org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import com.feilong.lib.org.apache.http.impl.io.DefaultHttpRequestParserFactory;
import com.feilong.lib.org.apache.http.impl.io.DefaultHttpResponseWriterFactory;
import com.feilong.lib.org.apache.http.io.HttpMessageParser;
import com.feilong.lib.org.apache.http.io.HttpMessageParserFactory;
import com.feilong.lib.org.apache.http.io.HttpMessageWriter;
import com.feilong.lib.org.apache.http.io.HttpMessageWriterFactory;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * Default implementation of {@link HttpServerConnection}.
 *
 * @since 4.3
 */
public class DefaultBHttpServerConnection extends BHttpConnectionBase implements HttpServerConnection{

    private final HttpMessageParser<HttpRequest>  requestParser;

    private final HttpMessageWriter<HttpResponse> responseWriter;

    /**
     * Creates new instance of DefaultBHttpServerConnection.
     *
     * @param bufferSize
     *            buffer size. Must be a positive number.
     * @param fragmentSizeHint
     *            fragment size hint.
     * @param charDecoder
     *            decoder to be used for decoding HTTP protocol elements.
     *            If {@code null} simple type cast will be used for byte to char conversion.
     * @param charEncoder
     *            encoder to be used for encoding HTTP protocol elements.
     *            If {@code null} simple type cast will be used for char to byte conversion.
     * @param constraints
     *            Message constraints. If {@code null}
     *            {@link MessageConstraints#DEFAULT} will be used.
     * @param incomingContentStrategy
     *            incoming content length strategy. If {@code null}
     *            {@link DisallowIdentityContentLengthStrategy#INSTANCE} will be used.
     * @param outgoingContentStrategy
     *            outgoing content length strategy. If {@code null}
     *            {@link com.feilong.lib.org.apache.http.impl.entity.StrictContentLengthStrategy#INSTANCE} will be used.
     * @param requestParserFactory
     *            request parser factory. If {@code null}
     *            {@link DefaultHttpRequestParserFactory#INSTANCE} will be used.
     * @param responseWriterFactory
     *            response writer factory. If {@code null}
     *            {@link DefaultHttpResponseWriterFactory#INSTANCE} will be used.
     */
    public DefaultBHttpServerConnection(final int bufferSize, final int fragmentSizeHint, final CharsetDecoder charDecoder,
                    final CharsetEncoder charEncoder, final MessageConstraints constraints,
                    final ContentLengthStrategy incomingContentStrategy, final ContentLengthStrategy outgoingContentStrategy,
                    final HttpMessageParserFactory<HttpRequest> requestParserFactory,
                    final HttpMessageWriterFactory<HttpResponse> responseWriterFactory){
        super(bufferSize, fragmentSizeHint, charDecoder, charEncoder, constraints,
                        incomingContentStrategy != null ? incomingContentStrategy : DisallowIdentityContentLengthStrategy.INSTANCE,
                        outgoingContentStrategy);
        this.requestParser = (requestParserFactory != null ? requestParserFactory : DefaultHttpRequestParserFactory.INSTANCE)
                        .create(getSessionInputBuffer(), constraints);
        this.responseWriter = (responseWriterFactory != null ? responseWriterFactory : DefaultHttpResponseWriterFactory.INSTANCE)
                        .create(getSessionOutputBuffer());
    }

    public DefaultBHttpServerConnection(final int bufferSize, final CharsetDecoder charDecoder, final CharsetEncoder charEncoder,
                    final MessageConstraints constraints){
        this(bufferSize, bufferSize, charDecoder, charEncoder, constraints, null, null, null, null);
    }

    public DefaultBHttpServerConnection(final int bufferSize){
        this(bufferSize, bufferSize, null, null, null, null, null, null, null);
    }

    protected void onRequestReceived(final HttpRequest request){
    }

    protected void onResponseSubmitted(final HttpResponse response){
    }

    @Override
    public void bind(final Socket socket) throws IOException{
        super.bind(socket);
    }

    @Override
    public HttpRequest receiveRequestHeader() throws HttpException,IOException{
        ensureOpen();
        final HttpRequest request = this.requestParser.parse();
        onRequestReceived(request);
        incrementRequestCount();
        return request;
    }

    @Override
    public void receiveRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException,IOException{
        Args.notNull(request, "HTTP request");
        ensureOpen();
        final HttpEntity entity = prepareInput(request);
        request.setEntity(entity);
    }

    @Override
    public void sendResponseHeader(final HttpResponse response) throws HttpException,IOException{
        Args.notNull(response, "HTTP response");
        ensureOpen();
        this.responseWriter.write(response);
        onResponseSubmitted(response);
        if (response.getStatusLine().getStatusCode() >= 200){
            incrementResponseCount();
        }
    }

    @Override
    public void sendResponseEntity(final HttpResponse response) throws HttpException,IOException{
        Args.notNull(response, "HTTP response");
        ensureOpen();
        final HttpEntity entity = response.getEntity();
        if (entity == null){
            return;
        }
        final OutputStream outStream = prepareOutput(response);
        entity.writeTo(outStream);
        outStream.close();
    }

    @Override
    public void flush() throws IOException{
        ensureOpen();
        doFlush();
    }

}
