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

import java.util.StringTokenizer;

import org.xml.sax.Attributes;

import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.ObjectCreateBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 *
 */
final class ObjectCreateRule extends AbstractXmlRule{

    public ObjectCreateRule(RulesBinder targetRulesBinder, PatternStack patternStack){
        super(targetRulesBinder, patternStack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindRule(LinkedRuleBuilder linkedRuleBuilder,Attributes attributes) throws Exception{
        ObjectCreateBuilder builder = linkedRuleBuilder.createObject().ofType(attributes.getValue("classname"))
                        .ofTypeSpecifiedByAttribute(attributes.getValue("attrname"));

        String paramTypesStr = attributes.getValue("paramtypes");
        if (paramTypesStr != null && paramTypesStr.length() > 0){
            StringTokenizer tokens = new StringTokenizer(paramTypesStr, " \t\n\r,");
            String[] paramTypeNames = new String[tokens.countTokens()];
            int counter = 0;
            while (tokens.hasMoreTokens()){
                paramTypeNames[counter++] = tokens.nextToken();
            }
            builder.usingConstructor(paramTypeNames);
        }
        String paramsStr = attributes.getValue("params");
        if (paramsStr != null && paramsStr.length() > 0){
            throw new UnsupportedOperationException("paramsStr not support!");
            //            
            //            StringTokenizer tokens = new StringTokenizer(paramTypesStr, " \t\n\r,");
            //            Object[] params = new Object[tokens.countTokens()];
            //            int counter = 0;
            //            while (tokens.hasMoreTokens()){
            //                params[counter++] = tokens.nextToken();
            //            }
            //builder.usingDefaultConstructorArguments( params );
        }
    }

}
