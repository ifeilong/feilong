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

import java.util.List;

/**
 * The Interface ReadStatus.
 */
public interface ReadStatus{

    /** The Constant STATUS_SUCCESS. */
    public static final int STATUS_SUCCESS = 0;

    //---------------------------------------------------------------

    /**
     * Gets the status.
     *
     * @return the status
     */
    int getStatus();

    /**
     * Sets the status.
     *
     * @param status
     *            the new status
     */
    void setStatus(int status);

    /**
     * Gets the message.
     *
     * @return the message
     */
    String getMessage();

    /**
     * Sets the message.
     *
     * @param message
     *            the new message
     */
    void setMessage(String message);

    /**
     * Gets the exceptions.
     *
     * @return the exceptions
     */
    List<Exception> getExceptions();

    /**
     * Sets the exceptions.
     *
     * @param exceptions
     *            the new exceptions
     */
    void setExceptions(List<Exception> exceptions);

    /**
     * 添加 exception.
     *
     * @param exception
     *            the exception
     */
    void addException(Exception exception);
}
