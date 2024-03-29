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

package com.feilong.lib.org.apache.http.entity.mime;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * HttpRFC6532Multipart represents a collection of MIME multipart encoded content bodies,
 * implementing the strict (RFC 822, RFC 2045, RFC 2046 compliant) interpretation
 * of the spec, with the exception of allowing UTF-8 headers, as per RFC6532.
 *
 * @since 4.3
 */
class HttpRFC6532Multipart extends AbstractMultipartForm{

    private final List<FormBodyPart> parts;

    public HttpRFC6532Multipart(final Charset charset, final String boundary, final List<FormBodyPart> parts){
        super(charset, boundary);
        this.parts = parts;
    }

    @Override
    public List<FormBodyPart> getBodyParts(){
        return this.parts;
    }

    @Override
    protected void formatMultipartHeader(final FormBodyPart part,final OutputStream out) throws IOException{

        // For RFC6532, we output all fields with UTF-8 encoding.
        final Header header = part.getHeader();
        for (final MinimalField field : header){
            writeField(field, MIME.UTF8_CHARSET, out);
        }
    }

}
