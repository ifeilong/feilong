/*
 * Copyright (C) 2014 XStream Committers.
 * All rights reserved.
 *
 * Created on 08. January 2014 by Joerg Schaible
 */
package com.feilong.lib.xstream.security;

import com.feilong.lib.xstream.XStreamException;

/**
 * Exception thrown for a forbidden class.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.4.7
 */
public class ForbiddenClassException extends XStreamException {

    /**
     * Construct a ForbiddenClassException.
     * @param type the forbidden class
     * @since 1.4.7
     */
    public ForbiddenClassException(Class type) {
        super(type == null ? "null" : type.getName());
    }
}
