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
package com.feilong.office.excel;

/**
 * The Interface WriteStatus.
 */
public class WriteStatus{

    /** The Constant STATUS_SUCCESS. */
    public static final int STATUS_SUCCESS = 0;

    //---------------------------------------------------------------

    /** The status. */
    private int             status         = STATUS_SUCCESS;

    /** The message. */
    private String          message;

    //---------------------------------------------------------------

    /**
     * Instantiates a new write status.
     */
    public WriteStatus(){
        super();
    }

    /**
     * Instantiates a new write status.
     *
     * @param status
     *            the status
     */
    public WriteStatus(int status){
        super();
        this.status = status;
    }

    /**
     * Instantiates a new write status.
     *
     * @param status
     *            the status
     * @param message
     *            the message
     */
    public WriteStatus(int status, String message){
        super();
        this.status = status;
        this.message = message;
    }

    //---------------------------------------------------------------

    /**
     * Gets the status.
     *
     * @return the status
     */

    public int getStatus(){
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */

    public void setStatus(int status){
        this.status = status;
    }

    //---------------------------------------------------------------

    /**
     * Gets the message.
     *
     * @return the message
     */

    public String getMessage(){
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */

    public void setMessage(String message){
        this.message = message;
    }
}
