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
package com.feilong.json.lib.json;

import com.feilong.core.DefaultRuntimeException;

/**
 * The JSONException is thrown when things are amiss.
 * 
 * @author JSON.org
 * @version 4
 */
public class JSONException extends DefaultRuntimeException{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6995087065217051815L;

    //---------------------------------------------------------------

    /**
     * Instantiates a new JSON exception.
     *
     * @param msg
     *            the msg
     */
    public JSONException(String msg){
        super(msg);
    }

    /**
     * Instantiates a new JSON exception.
     *
     * @param msg
     *            the msg
     * @param cause
     *            the cause
     */
    public JSONException(String msg, Throwable cause){
        super(msg, cause);
    }

    /**
     * Instantiates a new JSON exception.
     *
     * @param cause
     *            the cause
     */
    public JSONException(Throwable cause){
        super((cause == null ? null : cause.toString()), cause);
    }
}