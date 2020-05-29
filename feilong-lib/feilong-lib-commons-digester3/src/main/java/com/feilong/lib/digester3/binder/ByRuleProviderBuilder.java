package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.Rule;

/**
 * Builder chained when invoking
 * {@link LinkedRuleBuilder#addRuleCreatedBy(org.apache.commons.digester3.binder.RuleProvider)}.
 *
 * @param <R>
 *            The rule type will be returned by this builder
 * @since 3.0
 */
public final class ByRuleProviderBuilder<R extends Rule> extends AbstractBackToLinkedRuleBuilder<R>{

    private final RuleProvider<R> provider;

    ByRuleProviderBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder,
                    RuleProvider<R> provider){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder);
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected R createRule(){
        return provider.get();
    }

}
