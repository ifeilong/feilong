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
public interface WriteStatus{

    /** The Constant STATUS_SUCCESS. */
    public static final int STATUS_SUCCESS                  = 0;

    /** The Constant STATUS_READ_TEMPLATE_FILE_ERROR. */
    public static final int STATUS_READ_TEMPLATE_FILE_ERROR = 1;

    /** The Constant STATUS_SETTING_ERROR. */
    public static final int STATUS_SETTING_ERROR            = 2;

    /** The Constant STATUS_WRITE_FILE_ERROR. */
    public static final int STATUS_WRITE_FILE_ERROR         = 3;

    /** The Constant STATUS_SYSTEM_ERROR. */
    public static final int STATUS_SYSTEM_ERROR             = 5;

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
}
