package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 * 
 */
final class FactoryCreateRule
    extends AbstractXmlRule
{

    public FactoryCreateRule( RulesBinder targetRulesBinder, PatternStack patternStack )
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
        linkedRuleBuilder.factoryCreate()
            .ofType( attributes.getValue( "classname" ) )
            .overriddenByAttribute( attributes.getValue( "attrname" ) )
            .ignoreCreateExceptions( "true".equalsIgnoreCase( attributes.getValue( "ignore-exceptions" ) ) );
    }

}
