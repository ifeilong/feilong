/*
 * Copyright (C) 2004, 2005, 2006 Joe Walnes.
 * Copyright (C) 2006, 2007, 2009, 2011 XStream Committers.
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 02. September 2004 by Joe Walnes
 */
package com.feilong.lib.xstream.io.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.feilong.lib.xstream.io.naming.NameCoder;

/**
 * @author Michael Kopp
 */
public class DomWriter extends AbstractDocumentWriter{

    private final Document document;

    private boolean        hasRootElement;

    public DomWriter(final Document document){
        this(document, new XmlFriendlyNameCoder());
    }

    public DomWriter(final Element rootElement){
        this(rootElement, new XmlFriendlyNameCoder());
    }

    /**
     * @since 1.4
     */
    public DomWriter(final Document document, final NameCoder nameCoder){
        this(document.getDocumentElement(), document, nameCoder);
    }

    /**
     * @since 1.4
     */
    public DomWriter(final Element element, final Document document, final NameCoder nameCoder){
        super(element, nameCoder);
        this.document = document;
        hasRootElement = document.getDocumentElement() != null;
    }

    /**
     * @since 1.4
     */
    public DomWriter(final Element rootElement, final NameCoder nameCoder){
        this(rootElement, rootElement.getOwnerDocument(), nameCoder);
    }

    @Override
    protected Object createNode(final String name){
        final Element child = document.createElement(encodeNode(name));
        final Element top = top();
        if (top != null){
            top().appendChild(child);
        }else if (!hasRootElement){
            document.appendChild(child);
            hasRootElement = true;
        }
        return child;
    }

    @Override
    public void addAttribute(final String name,final String value){
        top().setAttribute(encodeAttribute(name), value);
    }

    @Override
    public void setValue(final String text){
        top().appendChild(document.createTextNode(text));
    }

    private Element top(){
        return (Element) getCurrent();
    }
}
