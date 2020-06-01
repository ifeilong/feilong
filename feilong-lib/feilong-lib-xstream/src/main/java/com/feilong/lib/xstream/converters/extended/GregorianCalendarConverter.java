/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 24. July 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.extended;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;

/**
 * Converts a java.util.GregorianCalendar to XML. Note that although it currently only contains one field, it nests
 * it inside a child element, to allow for other fields to be stored in the future.
 *
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 */
public class GregorianCalendarConverter implements Converter {

    @Override
    public boolean canConvert(Class type) {
        return type == GregorianCalendar.class;
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        GregorianCalendar calendar = (GregorianCalendar) source;
        ExtendedHierarchicalStreamWriterHelper.startNode(writer, "time", long.class);
        long timeInMillis = calendar.getTime().getTime(); // calendar.getTimeInMillis() not available under JDK 1.3
        writer.setValue(String.valueOf(timeInMillis));
        writer.endNode();
        ExtendedHierarchicalStreamWriterHelper.startNode(writer, "timezone", String.class);
        writer.setValue(calendar.getTimeZone().getID());
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        reader.moveDown();
        long timeInMillis = Long.parseLong(reader.getValue());
        reader.moveUp();
        final String timeZone;
        if (reader.hasMoreChildren()) {
            reader.moveDown();
            timeZone = reader.getValue();
            reader.moveUp();
        } else { // backward compatibility to XStream 1.1.2 and below
            timeZone = TimeZone.getDefault().getID();
        }

        GregorianCalendar result = new GregorianCalendar();
        result.setTimeZone(TimeZone.getTimeZone(timeZone));
        result.setTime(new Date(timeInMillis)); // calendar.setTimeInMillis() not available under JDK 1.3

        return result;
    }

}
