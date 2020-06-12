/*
 * Copyright (C) 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 24. April 2005 by Joe Walnes
 */
package com.feilong.lib.xstream.io.xml;

import java.util.Iterator;

import com.feilong.lib.xstream.converters.ErrorWriter;
import com.feilong.lib.xstream.core.util.FastStack;
import com.feilong.lib.xstream.io.AttributeNameIterator;
import com.feilong.lib.xstream.io.naming.NameCoder;

public abstract class AbstractDocumentReader extends AbstractXmlReader implements DocumentReader{

    private final FastStack pointers = new FastStack(16);

    private Object          current;

    protected AbstractDocumentReader(Object rootElement){
        this(rootElement, new XmlFriendlyNameCoder());
    }

    /**
     * @since 1.4
     */
    protected AbstractDocumentReader(Object rootElement, NameCoder nameCoder){
        super(nameCoder);
        this.current = rootElement;
        pointers.push(new Pointer());
        reassignCurrentElement(current);
    }

    protected abstract void reassignCurrentElement(Object current);

    protected abstract Object getParent();

    protected abstract Object getChild(int index);

    protected abstract int getChildCount();

    private static class Pointer{

        public int v;
    }

    @Override
    public boolean hasMoreChildren(){
        Pointer pointer = (Pointer) pointers.peek();

        if (pointer.v < getChildCount()){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void moveUp(){
        current = getParent();
        pointers.popSilently();
        reassignCurrentElement(current);
    }

    @Override
    public void moveDown(){
        Pointer pointer = (Pointer) pointers.peek();
        pointers.push(new Pointer());

        current = getChild(pointer.v);

        pointer.v++;
        reassignCurrentElement(current);
    }

    @Override
    public Iterator getAttributeNames(){
        return new AttributeNameIterator(this);
    }

    @Override
    public void appendErrors(ErrorWriter errorWriter){
    }

    @Override
    public Object getCurrent(){
        return this.current;
    }

    @Override
    public void close(){
        // don't need to do anything
    }
}
