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
package com.feilong.excel;

import static com.feilong.lib.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import java.io.IOException;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.feilong.core.DefaultRuntimeException;
import com.feilong.core.Validate;
import com.feilong.excel.definition.ExcelSheet;
import com.feilong.io.InputStreamUtil;
import com.feilong.lib.digester3.Digester;
import com.feilong.lib.digester3.binder.DigesterLoader;
import com.feilong.lib.digester3.xmlrules.FromXmlRulesModule;

/**
 * 
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 3.0.3
 */
@lombok.extern.slf4j.Slf4j
public class ExcelSheetListBuilder{

    private static final Digester DIGESTER = create(CLASSPATH_URL_PREFIX + "config/excel/definition-rule.xml");

    /** Don't let anyone instantiate this class. */
    private ExcelSheetListBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    static List<ExcelSheet> build(String sheetDefinitionPath) throws IOException,SAXException{
        Validate.notBlank(sheetDefinitionPath, "sheetDefinitionPath can't be blank!");
        return DIGESTER.parse(InputStreamUtil.getInputStream(sheetDefinitionPath));
    }

    public static Digester create(String location){
        log.debug("will parse:[{}]", location);
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
            throw new DefaultRuntimeException("can't create Digester from: " + location, e);
        }
    }

}
