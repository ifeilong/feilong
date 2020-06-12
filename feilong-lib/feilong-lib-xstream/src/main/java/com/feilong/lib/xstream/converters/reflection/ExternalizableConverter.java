/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2008, 2010, 2011, 2013, 2014, 2015, 2016 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 24. August 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.converters.reflection;

import java.io.Externalizable;
import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectInputValidation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import com.feilong.lib.xstream.converters.ConversionException;
import com.feilong.lib.xstream.converters.Converter;
import com.feilong.lib.xstream.converters.MarshallingContext;
import com.feilong.lib.xstream.converters.UnmarshallingContext;
import com.feilong.lib.xstream.core.ClassLoaderReference;
import com.feilong.lib.xstream.core.JVM;
import com.feilong.lib.xstream.core.ReferencingMarshallingContext;
import com.feilong.lib.xstream.core.util.CustomObjectInputStream;
import com.feilong.lib.xstream.core.util.CustomObjectOutputStream;
import com.feilong.lib.xstream.core.util.HierarchicalStreams;
import com.feilong.lib.xstream.core.util.SerializationMembers;
import com.feilong.lib.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.feilong.lib.xstream.io.HierarchicalStreamReader;
import com.feilong.lib.xstream.io.HierarchicalStreamWriter;
import com.feilong.lib.xstream.io.StreamException;
import com.feilong.lib.xstream.mapper.Mapper;

/**
 * Converts any object that implements the java.io.Externalizable interface, allowing compatibility with native Java
 * serialization.
 *
 * @author Joe Walnes
 */
public class ExternalizableConverter implements Converter{

    private Mapper                         mapper;

    private final ClassLoaderReference     classLoaderReference;

    private transient SerializationMembers serializationMembers;

    /**
     * Construct an ExternalizableConverter.
     *
     * @param mapper
     *            the Mapper chain
     * @param classLoaderReference
     *            the reference to XStream's {@link ClassLoader} instance
     * @since 1.4.5
     */
    public ExternalizableConverter(Mapper mapper, ClassLoaderReference classLoaderReference){
        this.mapper = mapper;
        this.classLoaderReference = classLoaderReference;
        serializationMembers = new SerializationMembers();
    }

    /**
     * @deprecated As of 1.4.5 use {@link #ExternalizableConverter(Mapper, ClassLoaderReference)}
     */
    public ExternalizableConverter(Mapper mapper, ClassLoader classLoader){
        this(mapper, new ClassLoaderReference(classLoader));
    }

    /**
     * @deprecated As of 1.4 use {@link #ExternalizableConverter(Mapper, ClassLoader)}
     */
    public ExternalizableConverter(Mapper mapper){
        this(mapper, ExternalizableConverter.class.getClassLoader());
    }

    @Override
    public boolean canConvert(Class type){
        return type != null && JVM.canCreateDerivedObjectOutputStream() && Externalizable.class.isAssignableFrom(type);
    }

    @Override
    public void marshal(final Object original,final HierarchicalStreamWriter writer,final MarshallingContext context){
        final Object source = serializationMembers.callWriteReplace(original);
        if (source != original && context instanceof ReferencingMarshallingContext){
            ((ReferencingMarshallingContext) context).replace(original, source);
        }
        if (source.getClass() != original.getClass()){
            final String attributeName = mapper.aliasForSystemAttribute("resolves-to");
            if (attributeName != null){
                writer.addAttribute(attributeName, mapper.serializedClass(source.getClass()));
            }
            context.convertAnother(source);
        }else{
            try{
                Externalizable externalizable = (Externalizable) source;
                CustomObjectOutputStream.StreamCallback callback = new CustomObjectOutputStream.StreamCallback(){

                    @Override
                    public void writeToStream(final Object object){
                        if (object == null){
                            writer.startNode("null");
                            writer.endNode();
                        }else{
                            ExtendedHierarchicalStreamWriterHelper
                                            .startNode(writer, mapper.serializedClass(object.getClass()), object.getClass());
                            context.convertAnother(object);
                            writer.endNode();
                        }
                    }

                    @Override
                    public void writeFieldsToStream(final Map fields){
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void defaultWriteObject(){
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void flush(){
                        writer.flush();
                    }

                    @Override
                    public void close(){
                        throw new UnsupportedOperationException(
                                        "Objects are not allowed to call ObjectOutput.close() from writeExternal()");
                    }
                };
                final CustomObjectOutputStream objectOutput = CustomObjectOutputStream.getInstance(context, callback);
                externalizable.writeExternal(objectOutput);
                objectOutput.popCallback();
            }catch (IOException e){
                throw new StreamException("Cannot serialize " + source.getClass().getName() + " using Externalization", e);
            }
        }
    }

    @Override
    public Object unmarshal(final HierarchicalStreamReader reader,final UnmarshallingContext context){
        final Class type = context.getRequiredType();
        final Constructor defaultConstructor;
        try{
            defaultConstructor = type.getDeclaredConstructor((Class[]) null);
            if (!defaultConstructor.isAccessible()){
                defaultConstructor.setAccessible(true);
            }
            final Externalizable externalizable = (Externalizable) defaultConstructor.newInstance((Object[]) null);
            CustomObjectInputStream.StreamCallback callback = new CustomObjectInputStream.StreamCallback(){

                @Override
                public Object readFromStream(){
                    reader.moveDown();
                    Class type = HierarchicalStreams.readClassType(reader, mapper);
                    Object streamItem = context.convertAnother(externalizable, type);
                    reader.moveUp();
                    return streamItem;
                }

                @Override
                public Map readFieldsFromStream(){
                    throw new UnsupportedOperationException();
                }

                @Override
                public void defaultReadObject(){
                    throw new UnsupportedOperationException();
                }

                @Override
                public void registerValidation(ObjectInputValidation validation,int priority) throws NotActiveException{
                    throw new NotActiveException("stream inactive");
                }

                @Override
                public void close(){
                    throw new UnsupportedOperationException("Objects are not allowed to call ObjectInput.close() from readExternal()");
                }
            };
            CustomObjectInputStream objectInput = CustomObjectInputStream.getInstance(context, callback, classLoaderReference);
            externalizable.readExternal(objectInput);
            objectInput.popCallback();
            return serializationMembers.callReadResolve(externalizable);
        }catch (NoSuchMethodException e){
            throw new ConversionException("Missing default constructor of type", e);
        }catch (InvocationTargetException e){
            throw new ConversionException("Cannot construct type", e);
        }catch (InstantiationException e){
            throw new ConversionException("Cannot construct type", e);
        }catch (IllegalAccessException e){
            throw new ObjectAccessException("Cannot construct type", e);
        }catch (IOException e){
            throw new StreamException("Cannot externalize " + type.getClass(), e);
        }catch (ClassNotFoundException e){
            throw new ConversionException("Cannot construct type", e);
        }
    }

    private Object readResolve(){
        serializationMembers = new SerializationMembers();
        return this;
    }
}
