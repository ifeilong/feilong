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

import static java.lang.Integer.parseInt;

import org.xml.sax.Attributes;

import com.feilong.lib.beanutils.ConvertUtils;
import com.feilong.lib.digester3.binder.LinkedRuleBuilder;
import com.feilong.lib.digester3.binder.ObjectParamBuilder;
import com.feilong.lib.digester3.binder.RulesBinder;

/**
 * @since 3.2
 */
final class ObjectParamRule extends AbstractXmlRule{

    /**
     * @param targetRulesBinder
     * @param patternStack
     */
    public ObjectParamRule(RulesBinder targetRulesBinder, PatternStack patternStack){
        super(targetRulesBinder, patternStack);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void bindRule(LinkedRuleBuilder linkedRuleBuilder,Attributes attributes) throws Exception{
        // create callparamrule
        String paramNumber = attributes.getValue("paramnumber");
        String attributeName = attributes.getValue("attrname");
        String type = attributes.getValue("type");
        String value = attributes.getValue("value");

        int paramIndex = parseInt(paramNumber);

        // create object instance
        Class<?> clazz = getDigester().getClassLoader().loadClass(type);
        Object param;
        if (value != null){
            param = ConvertUtils.convert(value, clazz);
        }else{
            param = clazz.newInstance();
        }

        ObjectParamBuilder<?> builder = linkedRuleBuilder.objectParam(param).ofIndex(paramIndex);

        if (attributeName != null){
            builder.matchingAttribute(attributeName);
        }
    }

}
