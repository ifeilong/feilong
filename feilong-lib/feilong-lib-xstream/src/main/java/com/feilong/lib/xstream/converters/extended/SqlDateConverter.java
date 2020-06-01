/*
 * Copyright (C) 2004 Joe Walnes.
 * Copyright (C) 2006, 2007, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 24. July 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.extended;

import java.sql.Date;

import com.feilong.lib.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Converts a java.sql.Date to text.
 *
 * @author Jose A. Illescas 
 */
public class SqlDateConverter extends AbstractSingleValueConverter {

    @Override
    public boolean canConvert(Class type) {
        return type == Date.class;
    }

    @Override
    public Object fromString(String str) {
        return Date.valueOf(str);
    }

}
