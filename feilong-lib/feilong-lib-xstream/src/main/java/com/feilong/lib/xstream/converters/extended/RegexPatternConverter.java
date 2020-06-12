/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2013, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 31. July 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.extended;

import java.util.regex.Pattern;

import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;

/**
 * Ensures java.util.regex.Pattern is compiled upon deserialization.
 * 
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 */
public class RegexPatternConverter implements Converter{

    /**
     * @since 1.4.5
     */
    public RegexPatternConverter(){
    }

    /**
     * @deprecated As of 1.4.5, use {@link #RegexPatternConverter()} instead
     */
    public RegexPatternConverter(Converter defaultConverter){
    }

    @Override
    public boolean canConvert(final Class type){
        return type == Pattern.class;
    }

    @Override
    public void marshal(Object source,HierarchicalStreamWriter writer,MarshallingContext context){
        Pattern pattern = (Pattern) source;
        writer.startNode("pattern");
        writer.setValue(pattern.pattern());
        writer.endNode();
        writer.startNode("flags");
        writer.setValue(String.valueOf(pattern.flags()));
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        reader.moveDown();
        String pattern = reader.getValue();
        reader.moveUp();
        reader.moveDown();
        int flags = Integer.parseInt(reader.getValue());
        reader.moveUp();
        return Pattern.compile(pattern, flags);
    }

}
