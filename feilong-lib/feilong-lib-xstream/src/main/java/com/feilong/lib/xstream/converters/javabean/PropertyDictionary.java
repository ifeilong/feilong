/*
 * Copyright (C) 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2009, 2011, 2016, 2017 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 12. April 2005 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.javabean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.feilong.lib.xstream.converters.reflection.MissingFieldException;
import com.feilong.lib.xstream.converters.reflection.ObjectAccessException;
import com.feilong.lib.xstream.core.Caching;
import com.feilong.lib.xstream.core.util.OrderRetainingMap;

/**
 * Builds the properties maps for each bean and caches them.
 * 
 * @author Joe Walnes
 * @author J&ouml;rg Schaible
 */
public class PropertyDictionary implements Caching{

    private transient Map        propertyNameCache = Collections.synchronizedMap(new HashMap());

    private final PropertySorter sorter;

    public PropertyDictionary(){
        this(new NativePropertySorter());
    }

    public PropertyDictionary(PropertySorter sorter){
        this.sorter = sorter;
    }

    public Iterator propertiesFor(Class type){
        return buildMap(type).values().iterator();
    }

    /**
     * Locates a property descriptor.
     * 
     * @param type
     * @param name
     * @throws MissingFieldException
     *             if property does not exist
     */
    public PropertyDescriptor propertyDescriptor(Class type,String name){
        PropertyDescriptor descriptor = propertyDescriptorOrNull(type, name);
        if (descriptor == null){
            throw new MissingFieldException(type.getName(), name);
        }
        return descriptor;
    }

    /**
     * Locates a property descriptor.
     * 
     * @param type
     * @param name
     * @return {@code null} if property does not exist
     * @since 1.4.10
     */
    public PropertyDescriptor propertyDescriptorOrNull(Class type,String name){
        return (PropertyDescriptor) buildMap(type).get(name);
    }

    private Map buildMap(Class type){
        Map nameMap = (Map) propertyNameCache.get(type);
        if (nameMap == null){
            BeanInfo beanInfo;
            try{
                beanInfo = Introspector.getBeanInfo(type, Object.class);
            }catch (IntrospectionException e){
                ObjectAccessException oaex = new ObjectAccessException("Cannot get BeanInfo of type", e);
                oaex.add("bean-type", type.getName());
                throw oaex;
            }
            nameMap = new OrderRetainingMap();
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++){
                PropertyDescriptor descriptor = propertyDescriptors[i];
                nameMap.put(descriptor.getName(), descriptor);
            }
            nameMap = sorter.sort(type, nameMap);
            propertyNameCache.put(type, nameMap);
        }
        return nameMap;
    }

    @Override
    public void flushCache(){
        propertyNameCache.clear();
    }
}
