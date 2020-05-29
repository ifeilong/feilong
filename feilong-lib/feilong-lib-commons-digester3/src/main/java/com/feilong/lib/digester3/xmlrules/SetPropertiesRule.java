package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;
import com.feilong.lib.digester3.binder.SetPropertiesBuilder;

/**
 *
 */
final class SetPropertiesRule
    extends AbstractXmlRule
{

    public SetPropertiesRule( RulesBinder targetRulesBinder, PatternStack patternStack )
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
        boolean ignoreMissingProperty = "true".equalsIgnoreCase( attributes.getValue( "ignore-missing-property" ) );

        SetPropertiesBuilder builder = linkedRuleBuilder.setProperties().ignoreMissingProperty( ignoreMissingProperty );
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
