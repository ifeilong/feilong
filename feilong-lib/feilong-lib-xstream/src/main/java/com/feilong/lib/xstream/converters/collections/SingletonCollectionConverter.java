/*
 * Copyright (C) 2011, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 11. October 2011 by Joerg Schaible
 */
package com.feilong.lib.xstream.converters.collections;

import java.util.Collections;

import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.mapper.Mapper;

/**
 * Converts singleton collections (list and set) to XML, specifying a nested element for the
 * item.
 * <p>
 * Supports Collections.singleton(Object) and Collections.singletonList(Object).
 * </p>
 * 
 * @author J&ouml;rg Schaible
 * @since 1.4.2
 */
public class SingletonCollectionConverter extends CollectionConverter{

    private static final Class LIST = Collections.singletonList(Boolean.TRUE).getClass();

    private static final Class SET  = Collections.singleton(Boolean.TRUE).getClass();

    /**
     * Construct a SingletonCollectionConverter.
     * 
     * @param mapper
     *            the mapper
     * @since 1.4.2
     */
    public SingletonCollectionConverter(Mapper mapper){
        super(mapper);
    }

    @Override
    public boolean canConvert(Class type){
        return LIST == type || SET == type;
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        final Object item = readCompleteItem(reader, context, null);
        return context.getRequiredType() == LIST ? (Object) Collections.singletonList(item) : (Object) Collections.singleton(item);
    }
}
