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
package com.feilong.lib.org.apache.http.client.methods;

import java.util.concurrent.atomic.AtomicMarkableReference;

import com.feilong.lib.org.apache.http.HttpRequest;
import com.feilong.lib.org.apache.http.client.utils.CloneUtils;
import com.feilong.lib.org.apache.http.concurrent.Cancellable;
import com.feilong.lib.org.apache.http.message.AbstractHttpMessage;

@SuppressWarnings("deprecation")
public abstract class AbstractExecutionAwareRequest extends AbstractHttpMessage implements HttpExecutionAware,Cloneable,HttpRequest{

    private final AtomicMarkableReference<Cancellable> cancellableRef;

    protected AbstractExecutionAwareRequest(){
        super();
        this.cancellableRef = new AtomicMarkableReference<>(null, false);
    }

    //    @Override
    public void abort(){
        while (!cancellableRef.isMarked()){
            final Cancellable actualCancellable = cancellableRef.getReference();
            if (cancellableRef.compareAndSet(actualCancellable, actualCancellable, false, true)){
                if (actualCancellable != null){
                    actualCancellable.cancel();
                }
            }
        }
    }

    @Override
    public boolean isAborted(){
        return this.cancellableRef.isMarked();
    }

    /**
     * @since 4.2
     */
    @Override
    public void setCancellable(final Cancellable cancellable){
        final Cancellable actualCancellable = cancellableRef.getReference();
        if (!cancellableRef.compareAndSet(actualCancellable, cancellable, false, false)){
            cancellable.cancel();
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        final AbstractExecutionAwareRequest clone = (AbstractExecutionAwareRequest) super.clone();
        clone.headergroup = CloneUtils.cloneObject(this.headergroup);
        clone.params = CloneUtils.cloneObject(this.params);
        return clone;
    }

    /**
     * Resets internal state of the request making it reusable.
     *
     * @since 4.2
     */
    public void reset(){
        for (;;){
            final boolean marked = cancellableRef.isMarked();
            final Cancellable actualCancellable = cancellableRef.getReference();
            if (actualCancellable != null){
                actualCancellable.cancel();
            }
            if (cancellableRef.compareAndSet(actualCancellable, null, marked, false)){
                break;
            }
        }
    }

}
