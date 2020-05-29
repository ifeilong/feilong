package com.feilong.lib.digester3.xmlrules;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;
import com.feilong.lib.digester3.binder.RulesModule;

final class PrefixedRulesBinder
    implements RulesBinder
{

    private final RulesBinder wrappedRulesBinder;

    private final String prefix;

    public PrefixedRulesBinder( RulesBinder wrappedRulesBinder, String prefix )
    {
        this.wrappedRulesBinder = wrappedRulesBinder;
        this.prefix = prefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getContextClassLoader()
    {
        return this.wrappedRulesBinder.getContextClassLoader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError( String messagePattern, Object... arguments )
    {
        this.wrappedRulesBinder.addError( messagePattern, arguments );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError( Throwable t )
    {
        this.wrappedRulesBinder.addError( t );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void install( RulesModule rulesModule )
    {
        this.wrappedRulesBinder.install( rulesModule );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedRuleBuilder forPattern( String pattern )
    {
        if ( this.prefix != null && this.prefix.length() > 0 )
        {
            pattern = this.prefix + '/' + pattern;
        }
        return this.wrappedRulesBinder.forPattern( pattern );
    }

}
