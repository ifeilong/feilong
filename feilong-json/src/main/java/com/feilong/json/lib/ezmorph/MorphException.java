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

package com.feilong.json.lib.ezmorph;

import com.feilong.core.DefaultRuntimeException;

/**
 * A <strong>MorphException</strong> indicates that a call to
 * <code>Morpher.morph()</code> has failed to complete successfully.<br>
 * Based on common-beauntils ConversionException.<br>
 *
 * @author <a href="mailto:aalmiray@users.sourceforge.net">Andres Almiray</a>
 */
public class MorphException extends DefaultRuntimeException{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -540093801787033824L;

    //---------------------------------------------------------------

    /**
     * Construct a new exception with the specified message.
     *
     * @param message
     *            The message describing this exception
     */
    public MorphException(String message){
        super(message);
    }

    /**
     * Construct a new exception with the specified message and root cause.
     *
     * @param message
     *            The message describing this exception
     * @param cause
     *            The root cause of this exception
     */
    public MorphException(String message, Throwable cause){
        super(message, cause);
    }

    /**
     * Instantiates a new morph exception.
     *
     * @param cause
     *            the cause
     */
    public MorphException(Throwable cause){
        super(cause);
    }

}