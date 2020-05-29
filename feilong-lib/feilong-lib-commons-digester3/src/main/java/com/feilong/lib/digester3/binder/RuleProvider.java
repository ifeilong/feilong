package com.feilong.lib.digester3.binder;

import com.feilong.lib.digester3.Rule;

/**
 * An object capable of providing instances of {@link Rule}.
 *
 * @param <R>
 *            The Rule type created by the provider.
 * @since 3.0
 */
public interface RuleProvider<R extends Rule> {

    /**
     * Provides an instance of {@link Rule}. Must never return null.
     *
     * @return an instance of {@link Rule}.
     */
    R get();

}
