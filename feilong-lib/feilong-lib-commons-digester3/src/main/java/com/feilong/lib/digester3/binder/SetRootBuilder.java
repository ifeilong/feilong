package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.SetRootRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#setRoot(String)}.
 *
 * @since 3.0
 */
public final class SetRootBuilder extends AbstractParamTypeBuilder<SetRootRule>{

    SetRootBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder, String methodName,
                    ClassLoader classLoader){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder, methodName, classLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SetRootRule createRule(){
        SetRootRule rule;

        if (getParamType() != null){
            rule = new SetRootRule(getMethodName(), getParamType());
        }else{
            rule = new SetRootRule(getMethodName());
        }

        rule.setExactMatch(isUseExactMatch());
        rule.setFireOnBegin(isFireOnBegin());
        return rule;
    }

}
