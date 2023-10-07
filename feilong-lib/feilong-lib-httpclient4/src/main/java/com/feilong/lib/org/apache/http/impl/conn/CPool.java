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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.lib.org.apache.http.annotation.Contract;
import com.feilong.lib.org.apache.http.annotation.ThreadingBehavior;
import com.feilong.lib.org.apache.http.conn.ManagedHttpClientConnection;
import com.feilong.lib.org.apache.http.conn.routing.HttpRoute;
import com.feilong.lib.org.apache.http.pool.AbstractConnPool;
import com.feilong.lib.org.apache.http.pool.ConnFactory;
import com.feilong.lib.org.apache.http.pool.PoolEntryCallback;

/**
 * @since 4.3
 */
@Contract(threading = ThreadingBehavior.SAFE)
class CPool extends AbstractConnPool<HttpRoute, ManagedHttpClientConnection, CPoolEntry>{

    private static final AtomicLong COUNTER = new AtomicLong();

    /** The Constant log. */
    private static final Logger     log     = LoggerFactory.getLogger(CPool.class);

    //---------------------------------------------------------------
    private final long              timeToLive;

    private final TimeUnit          timeUnit;

    public CPool(final ConnFactory<HttpRoute, ManagedHttpClientConnection> connFactory, final int defaultMaxPerRoute, final int maxTotal,
                    final long timeToLive, final TimeUnit timeUnit){
        super(connFactory, defaultMaxPerRoute, maxTotal);
        this.timeToLive = timeToLive;
        this.timeUnit = timeUnit;
    }

    @Override
    protected CPoolEntry createEntry(final HttpRoute route,final ManagedHttpClientConnection conn){
        final String id = Long.toString(COUNTER.getAndIncrement());
        return new CPoolEntry(id, route, conn, this.timeToLive, this.timeUnit);
    }

    @Override
    protected boolean validate(final CPoolEntry entry){
        return !entry.getConnection().isStale();
    }

    @Override
    protected void enumAvailable(final PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> callback){
        super.enumAvailable(callback);
    }

    @Override
    protected void enumLeased(final PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> callback){
        super.enumLeased(callback);
    }

}
