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
package com.feilong.xml.xstream;

import static com.feilong.core.bean.ConvertUtil.toList;

import java.util.Map;

import com.feilong.xml.xstream.converters.SimpleMapConverter;

/**
 * The Class XStreamConfigBuilder.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 1.10.7
 */
public final class XStreamConfigBuilder{

    /** Don't let anyone instantiate this class. */
    private XStreamConfigBuilder(){
        //AssertionError不是必须的. 但它可以避免不小心在类的内部调用构造器. 保证该类在任何情况下都不会被实例化.
        //see 《Effective Java》 2nd
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    //---------------------------------------------------------------

    /**
     * Builds the simple map X stream config.
     *
     * @param rootName
     *            the root name
     * @return the x stream config
     */
    public static XStreamConfig buildSimpleMapXStreamConfig(String rootName){
        return buildSimpleMapXStreamConfig(rootName, true);
    }

    /**
     * Builds the simple map X stream config.
     *
     * @param rootElementName
     *            the root name
     * @param isPrettyPrint
     *            the is pretty print
     * @return the x stream config
     */
    public static XStreamConfig buildSimpleMapXStreamConfig(String rootElementName,boolean isPrettyPrint){
        XStreamConfig xStreamConfig = new XStreamConfig();
        xStreamConfig.getAliasMap().put(rootElementName, Map.class);

        xStreamConfig.setConverterList(toList(SimpleMapConverter.INSTANCE));
        xStreamConfig.setIsPrettyPrint(isPrettyPrint);

        return xStreamConfig;
    }
}
