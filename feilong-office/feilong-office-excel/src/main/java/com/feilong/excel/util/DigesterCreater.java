/*
 * Copyright (C) 2008 feilong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.feilong.excel.util;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.digester3.xmlrules.FromXmlRulesModule;
import org.xml.sax.InputSource;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.io.InputStreamUtil;

public class DigesterCreater{

    /** Don't let anyone instantiate this class. */
    private DigesterCreater(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    public static Digester create(String location){
        try{
            Digester digester = DigesterLoader.newLoader(new FromXmlRulesModule(){

                @Override
                protected void loadRules(){
                    loadXMLRules(new InputSource(InputStreamUtil.getInputStream(location)));
                }

            }).newDigester();

            digester.setValidating(false);
            return digester;
        }catch (Exception e){
            throw new DefaultRuntimeException("can not create Digester from: " + location, e);
        }
    }
}
