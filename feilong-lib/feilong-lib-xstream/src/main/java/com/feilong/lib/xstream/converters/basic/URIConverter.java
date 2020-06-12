/*
 * Copyright (C) 2010, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 3. August 2010 by Joerg Schaible
 */
package com.feilong.lib.xstream.converters.basic;

import java.net.URI;
import java.net.URISyntaxException;

import com.feilong.lib.xstream.converters.ConversionException;

/**
 * Converts a java.net.URI to a string.
 * 
 * @author Carlos Roman
 */
public class URIConverter extends AbstractSingleValueConverter{

    @Override
    public boolean canConvert(Class type){
        return type == URI.class;
    }

    @Override
    public Object fromString(String str){
        try{
            return new URI(str);
        }catch (URISyntaxException e){
            throw new ConversionException(e);
        }
    }
}
