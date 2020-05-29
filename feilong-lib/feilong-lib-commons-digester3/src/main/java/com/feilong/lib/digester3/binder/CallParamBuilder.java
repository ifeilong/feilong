package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.CallParamRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#callParam()}.
 *
 * @since 3.0
 */
public final class CallParamBuilder extends AbstractBackToLinkedRuleBuilder<CallParamRule>{

    private int     paramIndex = 0;

    private int     stackIndex = 0;

    private boolean fromStack  = false;

    private String  attributeName;

    CallParamBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder);
    }

    /**
     * Sets the zero-relative parameter number.
     *
     * @param paramIndex
     *            The zero-relative parameter number
     * @return this builder instance
     */
    public CallParamBuilder ofIndex(int paramIndex){
        if (paramIndex < 0){
            reportError("callParam().ofIndex( int )", "negative index argument not allowed");
        }

        this.paramIndex = paramIndex;
        return this;
    }

    /**
     * Sets the attribute from which to save the parameter value.
     *
     * @param attributeName
     *            The attribute from which to save the parameter value
     * @return this builder instance
     */
    public CallParamBuilder fromAttribute( /* @Nullable */String attributeName){
        this.attributeName = attributeName;
        return this;
    }

    /**
     * Flags the parameter to be set from the stack.
     *
     * @param fromStack
     *            the parameter flag to be set from the stack
     * @return this builder instance
     */
    public CallParamBuilder fromStack(boolean fromStack){
        this.fromStack = fromStack;
        return this;
    }

    /**
     * Sets the position of the object from the top of the stack.
     *
     * @param stackIndex
     *            The position of the object from the top of the stack
     * @return this builder instance
     */
    public CallParamBuilder withStackIndex(int stackIndex){
        this.stackIndex = stackIndex;
        this.fromStack = true;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CallParamRule createRule(){
        CallParamRule rule;

        if (fromStack){
            rule = new CallParamRule(paramIndex, stackIndex);
        }else{
            rule = new CallParamRule(paramIndex);
        }

        rule.setAttributeName(attributeName);

        return rule;
    }

}
