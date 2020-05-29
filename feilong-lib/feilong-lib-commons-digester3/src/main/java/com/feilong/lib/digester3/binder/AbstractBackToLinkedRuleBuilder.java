package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.Rule;

/**
 * Builder invoked to back to main {@link LinkedRuleBuilder}.
 *
 * @since 3.0
 */
abstract class AbstractBackToLinkedRuleBuilder<R extends Rule> implements RuleProvider<R>{

    private final String      keyPattern;

    private final String      namespaceURI;

    private final RulesBinder mainBinder;

    AbstractBackToLinkedRuleBuilder(final String keyPattern, final String namespaceURI, final RulesBinder mainBinder,
                    final LinkedRuleBuilder mainBuilder){
        this.keyPattern = keyPattern;
        this.namespaceURI = namespaceURI;
        this.mainBinder = mainBinder;
    }

    /**
     * Returns the namespace URI for which this Rule is relevant, if any.
     *
     * @return The namespace URI for which this Rule is relevant, if any
     */
    public final String getNamespaceURI(){
        return this.namespaceURI;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final R get(){
        R rule = this.createRule();
        if (rule != null && this.namespaceURI != null){
            rule.setNamespaceURI(this.namespaceURI);
        }
        return rule;
    }

    protected final void reportError(String methodChain,String message){
        this.mainBinder.addError("{ forPattern( \"%s\" ).%s } %s", this.keyPattern, methodChain, message);
    }

    /**
     * Returns the rule pattern associated to this builder.
     *
     * @return The rule pattern associated to this builder
     */
    public final String getPattern(){
        return keyPattern;
    }

    /**
     * Provides an instance of {@link Rule}. Must never return null.
     *
     * @return an instance of {@link Rule}.
     * @see #get()
     */
    protected abstract R createRule();

}
