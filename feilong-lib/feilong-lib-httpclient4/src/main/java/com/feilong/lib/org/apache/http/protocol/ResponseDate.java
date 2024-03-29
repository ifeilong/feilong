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

package com.feilong.lib.org.apache.http.protocol;

import java.io.IOException;

import com.feilong.lib.org.apache.http.HttpException;
import com.feilong.lib.org.apache.http.HttpResponse;
import com.feilong.lib.org.apache.http.HttpResponseInterceptor;
import com.feilong.lib.org.apache.http.HttpStatus;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.util.Args;

/**
 * ResponseDate is responsible for adding {@code Date} header to the
 * outgoing responses. This interceptor is recommended for server side protocol
 * processors.
 *
 * @since 4.0
 */
@Contract(threading = ThreadingBehavior.SAFE)
public class ResponseDate implements HttpResponseInterceptor{

    private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();

    public ResponseDate(){
        super();
    }

    @Override
    public void process(final HttpResponse response,final HttpContext context) throws HttpException,IOException{
        Args.notNull(response, "HTTP response");
        final int status = response.getStatusLine().getStatusCode();
        if ((status >= HttpStatus.SC_OK) && !response.containsHeader(HTTP.DATE_HEADER)){
            final String httpdate = DATE_GENERATOR.getCurrentDate();
            response.setHeader(HTTP.DATE_HEADER, httpdate);
        }
    }

}
