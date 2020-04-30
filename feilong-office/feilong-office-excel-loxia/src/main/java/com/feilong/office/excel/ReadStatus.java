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

import java.util.ArrayList;
import java.util.List;

/**
 * The Interface ReadStatus.
 */
public class ReadStatus{

    /** The Constant STATUS_SUCCESS. */
    public static final int STATUS_SUCCESS = 0;

    //---------------------------------------------------------------

    /** The status. */
    private int             status         = STATUS_SUCCESS;

    /** The message. */
    private String          message;

    /** The exceptions. */
    private List<Exception> exceptions     = new ArrayList<>();

    //---------------------------------------------------------------

    /**
     * Instantiates a new read status.
     */
    public ReadStatus(){
        super();
    }

    /**
     * Instantiates a new read status.
     *
     * @param status
     *            the status
     * @param message
     *            the message
     */
    public ReadStatus(int status, String message){
        super();
        this.status = status;
        this.message = message;
    }

    /**
     * Instantiates a new read status.
     *
     * @param status
     *            the status
     * @param message
     *            the message
     * @param exceptions
     *            the exceptions
     */
    public ReadStatus(int status, String message, List<Exception> exceptions){
        super();
        this.status = status;
        this.message = message;
        this.exceptions = exceptions;
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

    /**
     * Gets the exceptions.
     *
     * @return the exceptions
     */

    public List<Exception> getExceptions(){
        return exceptions;
    }

    /**
     * Sets the exceptions.
     *
     * @param exceptions
     *            the new exceptions
     */

    public void setExceptions(List<Exception> exceptions){
        this.exceptions = exceptions;
    }

    //---------------------------------------------------------------

    /**
     * 添加 exception.
     *
     * @param exception
     *            the exception
     */
    public void addException(Exception exception){
        this.exceptions.add(exception);
    }
}
