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
package com.feilong.net.http;

import java.io.Serializable;

/**
 * The Class HttpHeader.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.6
 */
public class HttpHeader implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6820488777344163281L;

    //---------------------------------------------------------------

    /** The name. */
    private String            name;

    /** The value. */
    private String            value;

    //---------------------------------------------------------------

    /**
     * Instantiates a new http header.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public HttpHeader(String name, String value){
        super();
        this.name = name;
        this.value = value;
    }

    //---------------------------------------------------------------

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue(){
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the value to set
     */
    public void setValue(String value){
        this.value = value;
    }

}
