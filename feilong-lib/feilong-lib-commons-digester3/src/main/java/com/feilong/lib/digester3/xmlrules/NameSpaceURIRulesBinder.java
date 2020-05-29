package com.feilong.lib.digester3.xmlrules;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.util.Stack;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;
import com.feilong.lib.digester3.binder.RulesModule;

/**
 * @since 3.0
 */
final class NameSpaceURIRulesBinder
    implements RulesBinder
{

    // a stack is needed because of includes!!!
    private final Stack<String> namespaceURIs = new Stack<String>();

    private final RulesBinder wrappedBinder;

    public NameSpaceURIRulesBinder( RulesBinder wrappedBinder )
    {
        this.wrappedBinder = wrappedBinder;
    }

    /**
     * 
     * @param namespaceURI
     */
    public void addNamespaceURI( String namespaceURI )
    {
        namespaceURIs.push( namespaceURI );
    }

    /**
     * 
     */
    public void removeNamespaceURI()
    {
        namespaceURIs.pop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getContextClassLoader()
    {
        return wrappedBinder.getContextClassLoader();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError( String messagePattern, Object... arguments )
    {
        wrappedBinder.addError( messagePattern, arguments );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addError( Throwable t )
    {
        wrappedBinder.addError( t );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void install( RulesModule rulesModule )
    {
        wrappedBinder.install( rulesModule );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedRuleBuilder forPattern( String pattern )
    {
        return wrappedBinder.forPattern( pattern ).withNamespaceURI( namespaceURIs.peek() );
    }

}
