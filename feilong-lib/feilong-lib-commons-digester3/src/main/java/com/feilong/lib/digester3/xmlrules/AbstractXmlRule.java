package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 * 
 */
abstract class AbstractXmlRule
    extends PatternRule
{

    private final RulesBinder targetRulesBinder;

    public AbstractXmlRule( RulesBinder targetRulesBinder, PatternStack patternStack )
    {
        super( "pattern", patternStack );
        this.targetRulesBinder = targetRulesBinder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin( String namespace, String name, Attributes attributes )
        throws Exception
    {
        super.begin( namespace, name, attributes );
        this.bindRule( this.targetRulesBinder.forPattern( this.getMatchingPattern() ), attributes );
    }

    /**
     * @param linkedRuleBuilder
     * @param attributes
     */
    protected abstract void bindRule( LinkedRuleBuilder linkedRuleBuilder, Attributes attributes )
        throws Exception;

}
