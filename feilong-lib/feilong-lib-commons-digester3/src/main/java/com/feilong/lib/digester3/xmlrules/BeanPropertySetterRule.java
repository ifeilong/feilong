package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.BeanPropertySetterBuilder;
import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 * 
 */
final class BeanPropertySetterRule
    extends AbstractXmlRule
{

    public BeanPropertySetterRule( RulesBinder targetRulesBinder, PatternStack patternStack )
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
        BeanPropertySetterBuilder builder =
            linkedRuleBuilder.setBeanProperty().withName( attributes.getValue( "propertyname" ) );

        int propertyNameFromAttributeIndex = attributes.getIndex( "propertynameFromAttribute" );
        if ( propertyNameFromAttributeIndex >= 0 )
        {
            builder.extractPropertyNameFromAttribute( attributes.getValue( propertyNameFromAttributeIndex ) );
        }
    }

}
