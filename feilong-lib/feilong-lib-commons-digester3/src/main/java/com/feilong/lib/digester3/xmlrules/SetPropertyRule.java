package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 * 
 */
final class SetPropertyRule extends AbstractXmlRule{

    public SetPropertyRule(RulesBinder targetRulesBinder, PatternStack patternStack){
        super(targetRulesBinder, patternStack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindRule(LinkedRuleBuilder linkedRuleBuilder,Attributes attributes) throws Exception{
        String name = attributes.getValue("name");
        String value = attributes.getValue("value");
        linkedRuleBuilder.setProperty(name).extractingValueFromAttribute(value);
    }

}
