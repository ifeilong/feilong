/*
 * Copyright (C) 2004 Joe Walnes.
 * Copyright (C) 2006, 2007 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 04. October 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.feilong.lib.xstream.converters.DataHolder;

public class MapBackedDataHolder implements DataHolder {
    private final Map map;

    public MapBackedDataHolder() {
        this(new HashMap());
    }

    public MapBackedDataHolder(Map map) {
        this.map = map;
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public void put(Object key, Object value) {
        map.put(key, value);
    }

    @Override
    public Iterator keys() {
        return Collections.unmodifiableCollection(map.keySet()).iterator();
    }
}
