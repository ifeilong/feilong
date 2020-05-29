package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 * 
 */
abstract class AbstractXmlMethodRule
    extends AbstractXmlRule
{

    public AbstractXmlMethodRule( RulesBinder targetRulesBinder, PatternStack patternStack )
    {
        super( targetRulesBinder, patternStack );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void bindRule( LinkedRuleBuilder linkedRuleBuilder, Attributes attributes )
        throws Exception
    {
        String methodName = attributes.getValue( "methodname" );
        String paramType = attributes.getValue( "paramtype" );
        String exactMatch = attributes.getValue( "exactMatch" );
        String fireOnBegin = attributes.getValue( "fireOnBegin" );

        bindRule( linkedRuleBuilder, methodName, paramType, "true".equals( exactMatch ), "true".equals( fireOnBegin ) );
    }

    /**
     * @param methodName
     */
    protected abstract void bindRule( LinkedRuleBuilder linkedRuleBuilder, String methodName, String paramType,
                                      boolean exactMatch, boolean fireOnBegin );

}
