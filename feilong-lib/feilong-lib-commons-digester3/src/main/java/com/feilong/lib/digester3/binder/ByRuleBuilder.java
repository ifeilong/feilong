package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.Rule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#addRule(Rule)}.
 *
 * @param <R>
 *            The rule type will be returned by this builder
 */
public final class ByRuleBuilder<R extends Rule> extends AbstractBackToLinkedRuleBuilder<R>{

    private final R rule;

    ByRuleBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder, R rule){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder);
        this.rule = rule;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected R createRule(){
        return rule;
    }

}
