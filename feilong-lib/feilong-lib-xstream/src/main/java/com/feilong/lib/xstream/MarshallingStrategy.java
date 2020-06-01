/*
 * Copyright (C) 2004, 2006 Joe Walnes.
 * Copyright (C) 2007, 2009 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 16. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream;

import com.feilong.lib.xstream.converters.ConverterLookup;
import com.feilong.lib.xstream.converters.DataHolder;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.Mapper;

public interface MarshallingStrategy{

    Object unmarshal(Object root,HierarchicalStreamReader reader,DataHolder dataHolder,ConverterLookup converterLookup,Mapper mapper);

    void marshal(HierarchicalStreamWriter writer,Object obj,ConverterLookup converterLookup,Mapper mapper,DataHolder dataHolder);
}
