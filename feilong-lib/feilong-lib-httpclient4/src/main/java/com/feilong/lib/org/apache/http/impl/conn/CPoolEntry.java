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
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.org.apache.http.HttpClientConnection;
import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.conn.ManagedHttpClientConnection;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.pool.PoolEntry;

/**
 * @since 4.3
 */
@Contract(threading = ThreadingBehavior.SAFE)
class CPoolEntry extends PoolEntry<HttpRoute, ManagedHttpClientConnection>{

    /** The Constant log. */
    private static final Logger log = LoggerFactory.getLogger(CPoolEntry.class);

    //---------------------------------------------------------------
    private volatile boolean    routeComplete;

    public CPoolEntry(final String id, final HttpRoute route, final ManagedHttpClientConnection conn, final long timeToLive,
                    final TimeUnit timeUnit){
        super(id, route, conn, timeToLive, timeUnit);
    }

    public void markRouteComplete(){
        this.routeComplete = true;
    }

    public boolean isRouteComplete(){
        return this.routeComplete;
    }

    public void closeConnection() throws IOException{
        final HttpClientConnection conn = getConnection();
        conn.close();
    }

    public void shutdownConnection() throws IOException{
        final HttpClientConnection conn = getConnection();
        conn.shutdown();
    }

    @Override
    public boolean isExpired(final long now){
        final boolean expired = super.isExpired(now);
        if (expired && log.isDebugEnabled()){
            log.debug("Connection " + this + " expired @ " + new Date(getExpiry()));
        }
        return expired;
    }

    @Override
    public boolean isClosed(){
        final HttpClientConnection conn = getConnection();
        return !conn.isOpen();
    }

    @Override
    public void close(){
        try{
            closeConnection();
        }catch (final IOException ex){
            log.debug("I/O error closing connection", ex);
        }
    }

}
