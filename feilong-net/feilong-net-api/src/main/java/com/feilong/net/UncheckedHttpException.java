/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.net;

import com.feilong.core.DefaultRuntimeException;

/**
 * Un checked HttpException.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 1.7.3
 */
public final class UncheckedHttpException extends DefaultRuntimeException{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1699987643831455524L;

    //---------------------------------------------------------------

    /**
     * Instantiates a new http client exception.
     *
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public UncheckedHttpException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Instantiates a new http client exception.
     *
     * @param cause
     *            the cause
     */
    public UncheckedHttpException(Throwable cause){
        super(cause);
    }
}
