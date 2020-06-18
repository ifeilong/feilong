package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.Rule;
import com.feilong.lib.digester3.binder.SetPropertiesBuilder;

/**
 *
 */
final class SetPropertiesIgnoreRule extends Rule{

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(String namespace,String name,Attributes attributes) throws Exception{
        String attributeName = attributes.getValue("attr-name");

        SetPropertiesBuilder builder = getDigester().peek();
        builder.ignoreAttribute(attributeName);
    }

}
