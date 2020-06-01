/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 15. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.core;

import java.util.Iterator;

import com.feilong.lib.xstream.converters.ConversionException;
import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.ConverterLookup;
import com.feilong.lib.xstream.converters.DataHolder;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.core.util.ObjectIdDictionary;
import com.feilong.lib.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.Mapper;


public class TreeMarshaller implements MarshallingContext {

    protected HierarchicalStreamWriter writer;
    protected ConverterLookup converterLookup;
    private Mapper mapper;
    private ObjectIdDictionary parentObjects = new ObjectIdDictionary();
    private DataHolder dataHolder;

    public TreeMarshaller(
        HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper) {
        this.writer = writer;
        this.converterLookup = converterLookup;
        this.mapper = mapper;
    }

    @Override
    public void convertAnother(Object item) {
        convertAnother(item, null);
    }

    @Override
    public void convertAnother(Object item, Converter converter) {
        if (converter == null) {
            converter = converterLookup.lookupConverterForType(item.getClass());
        } else {
            if (!converter.canConvert(item.getClass())) {
                ConversionException e = new ConversionException(
                    "Explicit selected converter cannot handle item");
                e.add("item-type", item.getClass().getName());
                e.add("converter-type", converter.getClass().getName());
                throw e;
            }
        }
        convert(item, converter);
    }

    protected void convert(Object item, Converter converter) {
        if (parentObjects.containsId(item)) {
            ConversionException e = new CircularReferenceException(
                "Recursive reference to parent object");
            e.add("item-type", item.getClass().getName());
            e.add("converter-type", converter.getClass().getName());
            throw e;
        }
        parentObjects.associateId(item, "");
        converter.marshal(item, writer, this);
        parentObjects.removeId(item);
    }

    public void start(Object item, DataHolder dataHolder) {
        this.dataHolder = dataHolder;
        if (item == null) {
            writer.startNode(mapper.serializedClass(null));
            writer.endNode();
        } else {
            ExtendedHierarchicalStreamWriterHelper.startNode(writer, mapper
                .serializedClass(item.getClass()), item.getClass());
            convertAnother(item);
            writer.endNode();
        }
    }

    @Override
    public Object get(Object key) {
        lazilyCreateDataHolder();
        return dataHolder.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        lazilyCreateDataHolder();
        dataHolder.put(key, value);
    }

    @Override
    public Iterator keys() {
        lazilyCreateDataHolder();
        return dataHolder.keys();
    }

    private void lazilyCreateDataHolder() {
        if (dataHolder == null) {
            dataHolder = new MapBackedDataHolder();
        }
    }

    protected Mapper getMapper() {
        return this.mapper;
    }

    public static class CircularReferenceException extends ConversionException {

        public CircularReferenceException(String msg) {
            super(msg);
        }
    }
}
