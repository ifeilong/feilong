package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.PathCallParamRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#callParamPath()}.
 *
 * @since 3.0
 */
public final class PathCallParamBuilder extends AbstractBackToLinkedRuleBuilder<PathCallParamRule>{

    private int paramIndex = 0;

    PathCallParamBuilder(String keyPattern, String namespaceURI, RulesBinder mainBinder, LinkedRuleBuilder mainBuilder){
        super(keyPattern, namespaceURI, mainBinder, mainBuilder);
    }

    /**
     * Sets the zero-relative parameter number.
     *
     * @param paramIndex
     *            The zero-relative parameter number
     * @return this builder instance
     */
    public PathCallParamBuilder ofIndex(int paramIndex){
        if (paramIndex < 0){
            reportError("callParamPath().ofIndex( int )", "negative index argument not allowed");
        }

        this.paramIndex = paramIndex;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PathCallParamRule createRule(){
        return new PathCallParamRule(paramIndex);
    }

}
