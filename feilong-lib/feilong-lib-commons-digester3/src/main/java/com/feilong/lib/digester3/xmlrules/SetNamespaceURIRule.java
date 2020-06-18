package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.Rule;

/**
 * @since 3.0
 */
final class SetNamespaceURIRule extends Rule{

    private final NameSpaceURIRulesBinder rulesBinder;

    public SetNamespaceURIRule(NameSpaceURIRulesBinder rulesBinder){
        this.rulesBinder = rulesBinder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin(String namespace,String name,Attributes attributes) throws Exception{
        rulesBinder.addNamespaceURI(attributes.getValue("namespaceURI"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void end(String namespace,String name) throws Exception{
        rulesBinder.removeNamespaceURI();
    }

}
