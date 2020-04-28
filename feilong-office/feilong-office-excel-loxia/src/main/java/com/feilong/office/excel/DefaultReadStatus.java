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
 * The Class DefaultReadStatus.
 */
public class DefaultReadStatus implements ReadStatus{

    /** The status. */
    private int             status     = STATUS_SUCCESS;

    /** The message. */
    private String          message;

    /** The exceptions. */
    private List<Exception> exceptions = new ArrayList<>();

    //---------------------------------------------------------------

    /**
     * Gets the status.
     *
     * @return the status
     */
    @Override
    public int getStatus(){
        return status;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    @Override
    public void setStatus(int status){
        this.status = status;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    @Override
    public String getMessage(){
        return message;
    }

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    @Override
    public void setMessage(String message){
        this.message = message;
    }

    /**
     * Gets the exceptions.
     *
     * @return the exceptions
     */
    @Override
    public List<Exception> getExceptions(){
        return exceptions;
    }

    /**
     * Sets the exceptions.
     *
     * @param exceptions
     *            the new exceptions
     */
    @Override
    public void setExceptions(List<Exception> exceptions){
        this.exceptions = exceptions;
    }

    /**
     * 添加 exception.
     *
     * @param exception
     *            the exception
     */
    @Override
    public void addException(Exception exception){
        this.exceptions.add(exception);
    }
}
