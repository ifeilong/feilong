/*
 * Copyright (C) 2009, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 *
 * Created on 15. August 2009 by Joerg Schaible
 */
package com.feilong.lib.xstream.io.naming;

/**
 * A wrapper for another NameCoder.
 * 
 * @author J&ouml;rg Schaible
 * @since 1.4
 */
public class NameCoderWrapper implements NameCoder{

    private final NameCoder wrapped;

    /**
     * Construct a new wrapper for a NameCoder.
     * 
     * @param inner
     *            the wrapped NameCoder
     * @since 1.4
     */
    public NameCoderWrapper(NameCoder inner){
        this.wrapped = inner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decodeAttribute(String attributeName){
        return wrapped.decodeAttribute(attributeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String decodeNode(String nodeName){
        return wrapped.decodeNode(nodeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeAttribute(String name){
        return wrapped.encodeAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeNode(String name){
        return wrapped.encodeNode(name);
    }

}
