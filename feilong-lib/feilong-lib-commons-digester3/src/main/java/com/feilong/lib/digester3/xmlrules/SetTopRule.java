package com.feilong.lib.digester3.xmlrules;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;
import com.feilong.lib.digester3.binder.SetTopBuilder;

/**
 * 
 */
final class SetTopRule extends AbstractXmlMethodRule{

    /**
     * @param targetRulesBinder
     * @param patternStack
     */
    public SetTopRule(RulesBinder targetRulesBinder, PatternStack patternStack){
        super(targetRulesBinder, patternStack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindRule(LinkedRuleBuilder linkedRuleBuilder,String methodName,String paramType,boolean exactMatch,boolean fireOnBegin){
        SetTopBuilder builder = linkedRuleBuilder.setTop(methodName);

        if (paramType != null && paramType.length() > 0){
            builder.withParameterType(paramType);
        }

        builder.useExactMatch(exactMatch);
        builder.fireOnBegin(fireOnBegin);
    }

}
