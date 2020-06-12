/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2010, 2013, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 25. March 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.extended;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.feilong.lib.xstream.converters.ConversionException;
import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.core.ClassLoaderReference;
import com.feilong.lib.xstream.core.util.Fields;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.mapper.DynamicProxyMapper;
import com.feilong.lib.xstream.mapper.Mapper;

/**
 * Converts a dynamic proxy to XML, storing the implemented
 * interfaces and handler.
 *
 * @author Joe Walnes
 */
public class DynamicProxyConverter implements Converter{

    private ClassLoaderReference           classLoaderReference;

    private Mapper                         mapper;

    private static final Field             HANDLER = Fields.locate(Proxy.class, InvocationHandler.class, false);

    private static final InvocationHandler DUMMY   = new InvocationHandler(){

                                                       @Override
                                                       public Object invoke(Object proxy,Method method,Object[] args) throws Throwable{
                                                           return null;
                                                       }
                                                   };

    /**
     * @deprecated As of 1.4.5 use {@link #DynamicProxyConverter(Mapper, ClassLoaderReference)}
     */
    public DynamicProxyConverter(Mapper mapper){
        this(mapper, DynamicProxyConverter.class.getClassLoader());
    }

    /**
     * Construct a DynamicProxyConverter.
     * 
     * @param mapper
     *            the Mapper chain
     * @param classLoaderReference
     *            the reference to the {@link ClassLoader} of the XStream instance
     * @since 1.4.5
     */
    public DynamicProxyConverter(Mapper mapper, ClassLoaderReference classLoaderReference){
        this.classLoaderReference = classLoaderReference;
        this.mapper = mapper;
    }

    /**
     * @deprecated As of 1.4.5 use {@link #DynamicProxyConverter(Mapper, ClassLoaderReference)}
     */
    public DynamicProxyConverter(Mapper mapper, ClassLoader classLoader){
        this(mapper, new ClassLoaderReference(classLoader));
    }

    @Override
    public boolean canConvert(Class type){
        return type != null && (type.equals(DynamicProxyMapper.DynamicProxy.class) || Proxy.isProxyClass(type));
    }

    @Override
    public void marshal(Object source,HierarchicalStreamWriter writer,MarshallingContext context){
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(source);
        addInterfacesToXml(source, writer);
        writer.startNode("handler");
        String attributeName = mapper.aliasForSystemAttribute("class");
        if (attributeName != null){
            writer.addAttribute(attributeName, mapper.serializedClass(invocationHandler.getClass()));
        }
        context.convertAnother(invocationHandler);
        writer.endNode();
    }

    private void addInterfacesToXml(Object source,HierarchicalStreamWriter writer){
        Class[] interfaces = source.getClass().getInterfaces();
        for (int i = 0; i < interfaces.length; i++){
            Class currentInterface = interfaces[i];
            writer.startNode("interface");
            writer.setValue(mapper.serializedClass(currentInterface));
            writer.endNode();
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,UnmarshallingContext context){
        List interfaces = new ArrayList();
        InvocationHandler handler = null;
        Class handlerType = null;
        while (reader.hasMoreChildren()){
            reader.moveDown();
            String elementName = reader.getNodeName();
            if (elementName.equals("interface")){
                interfaces.add(mapper.realClass(reader.getValue()));
            }else if (elementName.equals("handler")){
                String attributeName = mapper.aliasForSystemAttribute("class");
                if (attributeName != null){
                    handlerType = mapper.realClass(reader.getAttribute(attributeName));
                    break;
                }
            }
            reader.moveUp();
        }
        if (handlerType == null){
            throw new ConversionException("No InvocationHandler specified for dynamic proxy");
        }
        Class[] interfacesAsArray = new Class[interfaces.size()];
        interfaces.toArray(interfacesAsArray);
        Object proxy = null;
        if (HANDLER != null){ // we will not be able to resolve references to the proxy
            proxy = Proxy.newProxyInstance(classLoaderReference.getReference(), interfacesAsArray, DUMMY);
        }
        handler = (InvocationHandler) context.convertAnother(proxy, handlerType);
        reader.moveUp();
        if (HANDLER != null){
            Fields.write(HANDLER, proxy, handler);
        }else{
            proxy = Proxy.newProxyInstance(classLoaderReference.getReference(), interfacesAsArray, handler);
        }
        return proxy;
    }
}
