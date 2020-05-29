package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.NestedPropertiesBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 *
 */
final class SetNestedPropertiesRule
    extends AbstractXmlRule
{

    public SetNestedPropertiesRule( RulesBinder targetRulesBinder, PatternStack patternStack )
    {
        super( targetRulesBinder, patternStack );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindRule( LinkedRuleBuilder linkedRuleBuilder, Attributes attributes )
        throws Exception
    {
        boolean allowUnknownChildElements =
            "true".equalsIgnoreCase( attributes.getValue( "allow-unknown-child-elements" ) );
        NestedPropertiesBuilder builder = linkedRuleBuilder
                                            .setNestedProperties()
                                            .allowUnknownChildElements( allowUnknownChildElements );
        getDigester().push( builder );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end( String namespace, String name )
        throws Exception
    {
        getDigester().pop();
    }

}
