/*
 * Copyright (C) 2004 Joe Walnes.
 * Copyright (C) 2006, 2007, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 06. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.collections;

import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;

/**
 * Converts a char[] to XML, storing the contents as a single
 * String.
 *
 * @author Joe Walnes
 */
public class CharArrayConverter implements Converter {

    @Override
    public boolean canConvert(Class type) {
        return type != null && type.isArray() && type.getComponentType().equals(char.class);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        char[] chars = (char[]) source;
        writer.setValue(new String(chars));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return reader.getValue().toCharArray();
    }
}
