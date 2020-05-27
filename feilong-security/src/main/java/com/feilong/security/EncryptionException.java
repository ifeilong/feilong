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
package com.feilong.security;

import com.feilong.core.DefaultRuntimeException;

/**
 * 加密解密的异常.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @version 1.0.7 2014年7月2日 下午5:53:31
 * @see java.lang.SecurityException
 * @since 1.0.7
 */
public class EncryptionException extends DefaultRuntimeException{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4887057961178296909L;

    //---------------------------------------------------------------

    /**
     * 加密解密的异常.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public EncryptionException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * 加密解密的异常.
     * 
     * @param cause
     *            the cause
     */
    public EncryptionException(Throwable cause){
        super(cause);
    }
}
