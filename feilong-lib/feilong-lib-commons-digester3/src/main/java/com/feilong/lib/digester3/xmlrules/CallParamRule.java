package com.feilong.lib.digester3.xmlrules;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.CallParamBuilder;
import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 * 
 */
final class CallParamRule extends AbstractXmlRule{

    public CallParamRule(RulesBinder targetRulesBinder, PatternStack patternStack){
        super(targetRulesBinder, patternStack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindRule(LinkedRuleBuilder linkedRuleBuilder,Attributes attributes) throws Exception{
        int paramIndex = Integer.parseInt(attributes.getValue("paramnumber"));
        CallParamBuilder builder = linkedRuleBuilder.callParam().ofIndex(paramIndex);

        String attributeName = attributes.getValue("attrname");
        String fromStack = attributes.getValue("from-stack");
        String stackIndex = attributes.getValue("stack-index");

        if (attributeName == null){
            if (stackIndex != null){
                builder.withStackIndex(Integer.parseInt(stackIndex));
            }else if (fromStack != null){
                builder.fromStack(Boolean.valueOf(fromStack).booleanValue());
            }
        }else{
            if (fromStack == null){
                builder.fromAttribute(attributeName);
            }else{
                // specifying both from-stack and attribute name is not allowed
                throw new RuntimeException("Attributes from-stack and attrname cannot both be present.");
            }
        }
    }

}
