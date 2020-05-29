package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.SetTopRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#setTop(String)}.
 *
 * @since 3.0
 */
public final class SetTopBuilder extends AbstractParamTypeBuilder<SetTopRule>{

    SetTopBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder, String methodName,
                    ClassLoader classLoader){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder, methodName, classLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SetTopRule createRule(){
        SetTopRule rule;

        if (getParamType() != null){
            rule = new SetTopRule(getMethodName(), getParamType());
        }else{
            rule = new SetTopRule(getMethodName());
        }

        rule.setExactMatch(isUseExactMatch());
        rule.setFireOnBegin(isFireOnBegin());
        return rule;
    }

}
