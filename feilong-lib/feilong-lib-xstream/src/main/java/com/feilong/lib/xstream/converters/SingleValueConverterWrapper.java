/*
 * Copyright (C) 2006, 2007, 2011, 2014 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 20. February 2006 by Mauro Talevi
 */
package com.feilong.lib.xstream.converters;

import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;

/**
 * Wrapper to convert a  {@link com.feilong.lib.xstream.converters.SingleValueConverter} into a
 * {@link com.feilong.lib.xstream.converters.Converter}.
 *
 * @author J&ouml;rg Schaible
 * @see com.feilong.lib.xstream.converters.Converter
 * @see com.feilong.lib.xstream.converters.SingleValueConverter
 */
public class SingleValueConverterWrapper implements Converter, SingleValueConverter, ErrorReporter {

    private final SingleValueConverter wrapped;

    public SingleValueConverterWrapper(SingleValueConverter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean canConvert(Class type) {
        return wrapped.canConvert(type);
    }

    @Override
    public String toString(Object obj) {
        return wrapped.toString(obj);
    }

    @Override
    public Object fromString(String str) {
        return wrapped.fromString(str);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        writer.setValue(toString(source));
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        return fromString(reader.getValue());
    }

    @Override
    public void appendErrors(ErrorWriter errorWriter) {
        errorWriter.add("wrapped-converter", wrapped == null ? "(null)" : wrapped.getClass().getName());
        if (wrapped instanceof ErrorReporter) {
            ((ErrorReporter)wrapped).appendErrors(errorWriter);
        }
    }
}
