package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.Rule;
import com.feilong.lib.digester3.binder.NestedPropertiesBuilder;

/**
 *
 */
final class SetNestedPropertiesAliasRule extends Rule{

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(String namespace,String name,Attributes attributes) throws Exception{
        String elementName = attributes.getValue("attr-name");
        String propertyName = attributes.getValue("prop-name");

        NestedPropertiesBuilder builder = getDigester().peek();
        builder.addAlias(elementName).forProperty(propertyName);
    }

}
