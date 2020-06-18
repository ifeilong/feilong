/*
 * Copyright (C) 2004, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 07. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.io;

import com.feilong.lib.xstream.XStreamException;

public class StreamException extends XStreamException{

    /**
     * 
     */
    private static final long serialVersionUID = -7851633675108453472L;

    public StreamException(Throwable e){
        super(e);
    }

    public StreamException(String message){
        super(message);
    }

    /**
     * @since 1.4
     */
    public StreamException(String message, Throwable cause){
        super(message, cause);
    }
}
