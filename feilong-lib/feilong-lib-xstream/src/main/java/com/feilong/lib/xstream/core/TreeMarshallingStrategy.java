/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 16. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.core;

import com.feilong.lib.xstream.converters.ConverterLookup;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.Mapper;

public class TreeMarshallingStrategy extends AbstractTreeMarshallingStrategy{

    @Override
    protected TreeUnmarshaller createUnmarshallingContext(
                    Object root,
                    HierarchicalStreamReader reader,
                    ConverterLookup converterLookup,
                    Mapper mapper){
        return new TreeUnmarshaller(root, reader, converterLookup, mapper);
    }

    @Override
    protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer,ConverterLookup converterLookup,Mapper mapper){
        return new TreeMarshaller(writer, converterLookup, mapper);
    }
}
