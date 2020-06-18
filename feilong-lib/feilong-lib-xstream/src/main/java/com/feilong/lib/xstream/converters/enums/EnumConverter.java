/*
 * Copyright (C) 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009, 2013, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 18. March 2005 by Joe Walnes
 */

// ***** READ THIS *****
// This class will only compile with JDK 1.5.0 or above as it test Java enums.
// If you are using an earlier version of Java, just don't try to build this class. XStream should work fine without it.

package com.feilong.lib.xstream.converters.enums;

import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;

/**
 * Converter for JDK 1.5 enums. Combined with EnumMapper this also deals with polymorphic enums.
 *
 * @author Eric Snell
 * @author Bryan Coleman
 */
public class EnumConverter implements Converter{

    @Override
    public boolean canConvert(Class type){
        return type != null && type.isEnum() || Enum.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(Object source,HierarchicalStreamWriter writer,MarshallingContext context){
        writer.setValue(((Enum) source).name());
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        Class type = context.getRequiredType();
        if (type.getSuperclass() != Enum.class){
            type = type.getSuperclass(); // polymorphic enums
        }
        String name = reader.getValue();
        try{
            return Enum.valueOf(type, name);
        }catch (IllegalArgumentException e){
            // failed to find it, do a case insensitive match
            for (Enum c : (Enum[]) type.getEnumConstants()){
                if (c.name().equalsIgnoreCase(name)){
                    return c;
                }
            }
            // all else failed
            throw e;
        }
    }

}
