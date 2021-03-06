/*
 * Copyright (C) 2006, 2007, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 07. April 2006 by Joerg Schaible
 */
package com.feilong.lib.xstream.converters.extended;

import java.nio.charset.Charset;

import com.feilong.lib.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * Converts a java.nio.charset.Carset to a string.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.2
 */
public class CharsetConverter extends AbstractSingleValueConverter{

    @Override
    public boolean canConvert(Class type){
        return type != null && Charset.class.isAssignableFrom(type);
    }

    @Override
    public String toString(Object obj){
        return obj == null ? null : ((Charset) obj).name();
    }

    @Override
    public Object fromString(String str){
        return Charset.forName(str);
    }
}