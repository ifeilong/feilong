/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 03. April 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.core;

import com.feilong.lib.xstream.converters.ConverterLookup;
import com.feilong.lib.xstream.io.AbstractReader;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.path.Path;
import com.feilong.lib.xstream.io.path.PathTracker;
import com.feilong.lib.xstream.io.path.PathTrackingReader;
import com.feilong.lib.xstream.mapper.Mapper;

public class ReferenceByXPathUnmarshaller extends AbstractReferenceUnmarshaller {

    private PathTracker pathTracker = new PathTracker();
    protected boolean isNameEncoding;

    public ReferenceByXPathUnmarshaller(Object root, HierarchicalStreamReader reader,
                                        ConverterLookup converterLookup, Mapper mapper) {
        super(root, reader, converterLookup, mapper);
        this.reader = new PathTrackingReader(reader, pathTracker);
        isNameEncoding = reader.underlyingReader() instanceof AbstractReader;
    }

    @Override
    protected Object getReferenceKey(String reference) {
        final Path path = new Path(isNameEncoding ? ((AbstractReader)reader.underlyingReader()).decodeNode(reference) : reference);
        // We have absolute references, if path starts with '/'
        return reference.charAt(0) != '/' ? pathTracker.getPath().apply(path) : path;
    }

    @Override
    protected Object getCurrentReferenceKey() {
        return pathTracker.getPath();
    }

}
