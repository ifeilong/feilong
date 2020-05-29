package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.SetNextRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#setNext(String)}.
 *
 * @since 3.0
 */
public final class SetNextBuilder extends AbstractParamTypeBuilder<SetNextRule>{

    SetNextBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder, String methodName,
                    ClassLoader classLoader){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder, methodName, classLoader);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SetNextRule createRule(){
        SetNextRule rule;

        if (getParamType() != null){
            rule = new SetNextRule(getMethodName(), getParamType());
        }else{
            rule = new SetNextRule(getMethodName());
        }

        rule.setExactMatch(isUseExactMatch());
        rule.setFireOnBegin(isFireOnBegin());
        return rule;
    }

}
