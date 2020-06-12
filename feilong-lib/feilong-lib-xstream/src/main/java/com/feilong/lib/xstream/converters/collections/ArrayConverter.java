/*
 * Copyright (C) 2003, 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. October 2003 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.Mapper;

/**
 * Converts an array of objects or primitives to XML, using
 * a nested child element for each item.
 *
 * @author Joe Walnes
 */
public class ArrayConverter extends AbstractCollectionConverter{

    public ArrayConverter(Mapper mapper){
        super(mapper);
    }

    @Override
    public boolean canConvert(Class type){
        return type != null && type.isArray();
    }

    @Override
    public void marshal(Object source,HierarchicalStreamWriter writer,MarshallingContext context){
        int length = Array.getLength(source);
        for (int i = 0; i < length; i++){
            final Object item = Array.get(source, i);
            writeCompleteItem(item, context, writer);
        }

    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        // read the items from xml into a list
        List items = new ArrayList();
        while (reader.hasMoreChildren()){
            final Object item = readCompleteItem(reader, context, null); // TODO: arg, what should replace null?
            items.add(item);
        }
        // now convertAnother the list into an array
        // (this has to be done as a separate list as the array size is not
        //  known until all items have been read)
        Object array = Array.newInstance(context.getRequiredType().getComponentType(), items.size());
        int i = 0;
        for (Iterator iterator = items.iterator(); iterator.hasNext();){
            Array.set(array, i++, iterator.next());
        }
        return array;
    }
}
