package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.Rule;
import com.feilong.lib.digester3.binder.NestedPropertiesBuilder;

/**
 *
 */
final class SetNestedPropertiesIgnoreRule extends Rule{

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(String namespace,String name,Attributes attributes) throws Exception{
        String elementName = attributes.getValue("elem-name");

        NestedPropertiesBuilder builder = getDigester().peek();
        builder.ignoreElement(elementName);
    }

}
