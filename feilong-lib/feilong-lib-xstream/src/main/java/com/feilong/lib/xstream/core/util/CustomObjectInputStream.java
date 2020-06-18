/*
 * Copyright (C) 2004, 2005 Joe Walnes.
 * Copyright (C) 2006, 2007, 2010, 2011, 2013, 2016, 2018 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 23. August 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.core.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.util.Map;

import com.feilong.lib.xstream.converters.DataHolder;
import com.feilong.lib.xstream.converters.reflection.ObjectAccessException;
import com.feilong.lib.xstream.core.ClassLoaderReference;
import com.feilong.lib.xstream.io.StreamException;

public class CustomObjectInputStream extends ObjectInputStream{

    private FastStack                  callbacks       = new FastStack(1);

    private final ClassLoaderReference classLoaderReference;

    private static final String        DATA_HOLDER_KEY = CustomObjectInputStream.class.getName();

    public static interface StreamCallback{

        Object readFromStream() throws IOException;

        Map readFieldsFromStream() throws IOException;

        void defaultReadObject() throws IOException;

        void registerValidation(ObjectInputValidation validation,int priority) throws NotActiveException,InvalidObjectException;

        void close() throws IOException;
    }

    /**
     * @deprecated As of 1.4 use {@link #getInstance(DataHolder, StreamCallback, ClassLoader)}
     */
    @Deprecated
    public static CustomObjectInputStream getInstance(DataHolder whereFrom,CustomObjectInputStream.StreamCallback callback){
        return getInstance(whereFrom, callback, (ClassLoader) null);
    }

    /**
     * @deprecated As of 1.4.5 use {@link #getInstance(DataHolder, StreamCallback, ClassLoaderReference)}
     */
    @Deprecated
    public static synchronized CustomObjectInputStream getInstance(
                    DataHolder whereFrom,
                    CustomObjectInputStream.StreamCallback callback,
                    ClassLoader classLoader){
        return getInstance(whereFrom, callback, new ClassLoaderReference(classLoader));
    }

    public static synchronized CustomObjectInputStream getInstance(
                    DataHolder whereFrom,
                    CustomObjectInputStream.StreamCallback callback,
                    ClassLoaderReference classLoaderReference){
        try{
            CustomObjectInputStream result = (CustomObjectInputStream) whereFrom.get(DATA_HOLDER_KEY);
            if (result == null){
                result = new CustomObjectInputStream(callback, classLoaderReference);
                whereFrom.put(DATA_HOLDER_KEY, result);
            }else{
                result.pushCallback(callback);
            }
            return result;
        }catch (SecurityException e){
            throw new ObjectAccessException("Cannot create CustomObjectStream", e);
        }catch (IOException e){
            throw new StreamException("Cannot create CustomObjectStream", e);
        }
    }

    /**
     * Warning, this object is expensive to create (due to functionality inherited from superclass).
     * Use the static fetch() method instead, wherever possible.
     *
     * @see #getInstance(DataHolder, StreamCallback, ClassLoaderReference)
     */
    public CustomObjectInputStream(StreamCallback callback, ClassLoaderReference classLoaderReference) throws IOException,SecurityException{
        super();
        this.callbacks.push(callback);
        this.classLoaderReference = classLoaderReference;
    }

    /**
     * @deprecated As of 1.4.5 use {@link #CustomObjectInputStream(StreamCallback, ClassLoaderReference)}
     */
    @Deprecated
    public CustomObjectInputStream(StreamCallback callback, ClassLoader classLoader) throws IOException,SecurityException{
        this(callback, new ClassLoaderReference(classLoader));
    }

    /**
     * Allows the CustomObjectInputStream (which is expensive to create) to be reused.
     */
    public void pushCallback(StreamCallback callback){
        this.callbacks.push(callback);
    }

    public StreamCallback popCallback(){
        return (StreamCallback) this.callbacks.pop();
    }

    public StreamCallback peekCallback(){
        return (StreamCallback) this.callbacks.peek();
    }

    @Override
    protected Class resolveClass(ObjectStreamClass desc) throws IOException,ClassNotFoundException{
        ClassLoader classLoader = classLoaderReference.getReference();
        if (classLoader == null){
            return super.resolveClass(desc);
        }else{
            return Class.forName(desc.getName(), false, classLoader);
        }
    }

    @Override
    public void defaultReadObject() throws IOException{
        peekCallback().defaultReadObject();
    }

    @Override
    protected Object readObjectOverride() throws IOException{
        return peekCallback().readFromStream();
    }

    @Override
    public Object readUnshared() throws IOException,ClassNotFoundException{
        return readObject();
    }

    @Override
    public boolean readBoolean() throws IOException{
        return ((Boolean) peekCallback().readFromStream()).booleanValue();
    }

    @Override
    public byte readByte() throws IOException{
        return ((Byte) peekCallback().readFromStream()).byteValue();
    }

    @Override
    public int readUnsignedByte() throws IOException{
        return ((Byte) peekCallback().readFromStream()).intValue() & 0xff;
    }

    @Override
    public int readInt() throws IOException{
        return ((Integer) peekCallback().readFromStream()).intValue();
    }

    @Override
    public char readChar() throws IOException{
        return ((Character) peekCallback().readFromStream()).charValue();
    }

    @Override
    public float readFloat() throws IOException{
        return ((Float) peekCallback().readFromStream()).floatValue();
    }

    @Override
    public double readDouble() throws IOException{
        return ((Double) peekCallback().readFromStream()).doubleValue();
    }

    @Override
    public long readLong() throws IOException{
        return ((Long) peekCallback().readFromStream()).longValue();
    }

    @Override
    public short readShort() throws IOException{
        return ((Short) peekCallback().readFromStream()).shortValue();
    }

    @Override
    public int readUnsignedShort() throws IOException{
        return ((Short) peekCallback().readFromStream()).intValue() & 0xffff;
    }

    @Override
    public String readUTF() throws IOException{
        return (String) peekCallback().readFromStream();
    }

    @Override
    public void readFully(byte[] buf) throws IOException{
        readFully(buf, 0, buf.length);
    }

    @Override
    public void readFully(byte[] buf,int off,int len) throws IOException{
        byte[] b = (byte[]) peekCallback().readFromStream();
        System.arraycopy(b, 0, buf, off, len);
    }

    @Override
    public int read() throws IOException{
        return readUnsignedByte();
    }

    @Override
    public int read(byte[] buf,int off,int len) throws IOException{
        byte[] b = (byte[]) peekCallback().readFromStream();
        if (b.length != len){
            throw new StreamCorruptedException("Expected " + len + " bytes from stream, got " + b.length);
        }
        System.arraycopy(b, 0, buf, off, len);
        return len;
    }

    @Override
    public int read(byte b[]) throws IOException{
        return read(b, 0, b.length);
    }

    @Override
    public GetField readFields() throws IOException{
        return new CustomGetField(peekCallback().readFieldsFromStream());
    }

    private class CustomGetField extends GetField{

        private Map fields;

        public CustomGetField(Map fields){
            this.fields = fields;
        }

        @Override
        public ObjectStreamClass getObjectStreamClass(){
            throw new UnsupportedOperationException();
        }

        private Object get(String name){
            return fields.get(name);
        }

        @Override
        public boolean defaulted(String name){
            return !fields.containsKey(name);
        }

        @Override
        public byte get(String name,byte val){
            return defaulted(name) ? val : ((Byte) get(name)).byteValue();
        }

        @Override
        public char get(String name,char val){
            return defaulted(name) ? val : ((Character) get(name)).charValue();
        }

        @Override
        public double get(String name,double val){
            return defaulted(name) ? val : ((Double) get(name)).doubleValue();
        }

        @Override
        public float get(String name,float val){
            return defaulted(name) ? val : ((Float) get(name)).floatValue();
        }

        @Override
        public int get(String name,int val){
            return defaulted(name) ? val : ((Integer) get(name)).intValue();
        }

        @Override
        public long get(String name,long val){
            return defaulted(name) ? val : ((Long) get(name)).longValue();
        }

        @Override
        public short get(String name,short val){
            return defaulted(name) ? val : ((Short) get(name)).shortValue();
        }

        @Override
        public boolean get(String name,boolean val){
            return defaulted(name) ? val : ((Boolean) get(name)).booleanValue();
        }

        @Override
        public Object get(String name,Object val){
            return defaulted(name) ? val : get(name);
        }

    }

    @Override
    public void registerValidation(ObjectInputValidation validation,int priority) throws NotActiveException,InvalidObjectException{
        peekCallback().registerValidation(validation, priority);
    }

    @Override
    public void close() throws IOException{
        peekCallback().close();
    }

    /****** Unsupported methods ******/

    @Override
    public int available(){
        throw new UnsupportedOperationException();
    }

    @Override
    public String readLine(){
        throw new UnsupportedOperationException();
    }

    @Override
    public int skipBytes(int len){
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(long n){
        throw new UnsupportedOperationException();
    }

    @Override
    public void mark(int readlimit){
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset(){
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported(){
        return false;
    }

}
