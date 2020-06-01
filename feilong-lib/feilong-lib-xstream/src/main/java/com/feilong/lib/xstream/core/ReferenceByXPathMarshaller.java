/*
 * Copyright (C) 2004, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2009 XStream Committers.
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
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.io.path.Path;
import com.feilong.lib.xstream.mapper.Mapper;

public class ReferenceByXPathMarshaller extends AbstractReferenceMarshaller {

    private final int mode;

    public ReferenceByXPathMarshaller(HierarchicalStreamWriter writer, ConverterLookup converterLookup, Mapper mapper, int mode) {
        super(writer, converterLookup, mapper);
        this.mode = mode;
    }

    @Override
    protected String createReference(Path currentPath, Object existingReferenceKey) {
        Path existingPath = (Path)existingReferenceKey;
        Path referencePath = (mode & ReferenceByXPathMarshallingStrategy.ABSOLUTE) > 0 ? existingPath : currentPath.relativeTo(existingPath);
        return (mode & ReferenceByXPathMarshallingStrategy.SINGLE_NODE) > 0 ? referencePath.explicit() : referencePath.toString();
    }

    @Override
    protected Object createReferenceKey(Path currentPath, Object item) {
        return currentPath;
    }

    @Override
    protected void fireValidReference(Object referenceKey) {
        // nothing to do
    }
}
