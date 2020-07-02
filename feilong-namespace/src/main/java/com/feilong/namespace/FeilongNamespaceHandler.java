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
package com.feilong.namespace;

import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;

import java.util.Map;

import org.springframework.beans.factory.xml.BeanDefinitionParser;

import com.feilong.lib.lang3.tuple.Pair;
import com.feilong.namespace.parser.CookieAccessorBeanDefinitionParser;
import com.feilong.namespace.parser.SessionAccessorBeanDefinitionParser;
import com.feilong.namespace.parser.SessionKeyAccessorBeanDefinitionParser;
import com.feilong.namespace.parser.SftpFileTransferBeanDefinitionParser;
import com.feilong.namespace.parser.SimpleHttpTypeBeanPropertyBeanDefinitionParser;

/**
 * The Class FeilongNamespaceHandler.
 *
 * @author <a href="https://github.com/ifeilong/feilong">feilong</a>
 * @since 2.0.0
 */
public class FeilongNamespaceHandler extends org.springframework.beans.factory.xml.NamespaceHandlerSupport{

    /** The Constant map. */
    private static final Map<String, BeanDefinitionParser> map = toMapUseEntrys(//
                    //Pair.of("http-responseStringBuilder", (BeanDefinitionParser) new HttpResponseStringBuilderBeanDefinitionParser()),

                    Pair.of("accessor-session", new SessionAccessorBeanDefinitionParser()),
                    Pair.of("accessor-sessionkey", new SessionKeyAccessorBeanDefinitionParser()),
                    Pair.of("accessor-cookie",new CookieAccessorBeanDefinitionParser()),

                    //SimpleHttpTypeBeanProperty
                    Pair.of("httpTypeBeanProperty",new SimpleHttpTypeBeanPropertyBeanDefinitionParser()),

                    //since 3.0.8
                    Pair.of("sftpFileTransfer",new SftpFileTransferBeanDefinitionParser())
    //
    );

    //---------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
     */
    @Override
    public void init(){
        for (Map.Entry<String, BeanDefinitionParser> entry : map.entrySet()){
            registerBeanDefinitionParser(entry.getKey(), entry.getValue());
        }
    }
}