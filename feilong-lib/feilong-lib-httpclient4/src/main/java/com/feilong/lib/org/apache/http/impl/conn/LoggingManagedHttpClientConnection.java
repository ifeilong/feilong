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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import com.feilong.lib.org.apache.http.Header;
import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.config.MessageConstraints;
import com.feilong.lib.org.apache.http.entity.ContentLengthStrategy;
import com.feilong.lib.org.apache.http.io.HttpMessageParserFactory;
import com.feilong.lib.org.apache.http.io.HttpMessageWriterFactory;

@lombok.extern.slf4j.Slf4j
class LoggingManagedHttpClientConnection extends DefaultManagedHttpClientConnection{

    private final Wire wire;

    public LoggingManagedHttpClientConnection(final String id, final int bufferSize, final int fragmentSizeHint,
                    final CharsetDecoder charDecoder, final CharsetEncoder charEncoder, final MessageConstraints constraints,
                    final ContentLengthStrategy incomingContentStrategy, final ContentLengthStrategy outgoingContentStrategy,
                    final HttpMessageWriterFactory<HttpRequest> requestWriterFactory,
                    final HttpMessageParserFactory<HttpResponse> responseParserFactory){
        super(id, bufferSize, fragmentSizeHint, charDecoder, charEncoder, constraints, incomingContentStrategy, outgoingContentStrategy,
                        requestWriterFactory, responseParserFactory);
        this.wire = new Wire(id);
    }

    @Override
    public void close() throws IOException{

        if (super.isOpen()){
            if (log.isDebugEnabled()){
                log.debug(getId() + ": Close connection");
            }
            super.close();
        }
    }

    @Override
    public void setSocketTimeout(final int timeout){
        if (log.isDebugEnabled()){
            log.debug(getId() + ": set socket timeout to " + timeout);
        }
        super.setSocketTimeout(timeout);
    }

    @Override
    public void shutdown() throws IOException{
        if (log.isDebugEnabled()){
            log.debug(getId() + ": Shutdown connection");
        }
        super.shutdown();
    }

    @Override
    protected InputStream getSocketInputStream(final Socket socket) throws IOException{
        InputStream in = super.getSocketInputStream(socket);
        if (this.wire.enabled()){
            in = new LoggingInputStream(in, this.wire);
        }
        return in;
    }

    @Override
    protected OutputStream getSocketOutputStream(final Socket socket) throws IOException{
        OutputStream out = super.getSocketOutputStream(socket);
        if (this.wire.enabled()){
            out = new LoggingOutputStream(out, this.wire);
        }
        return out;
    }

    @Override
    protected void onResponseReceived(final HttpResponse response){
        if (response != null && log.isDebugEnabled()){
            log.debug(getId() + " << " + response.getStatusLine().toString());
            final Header[] headers = response.getAllHeaders();
            for (final Header header : headers){
                log.debug(getId() + " << " + header.toString());
            }
        }
    }

    @Override
    protected void onRequestSubmitted(final HttpRequest request){
        if (request != null && log.isDebugEnabled()){
            log.debug(getId() + " >> " + request.getRequestLine().toString());
            final Header[] headers = request.getAllHeaders();
            for (final Header header : headers){
                log.debug(getId() + " >> " + header.toString());
            }
        }
    }

}
